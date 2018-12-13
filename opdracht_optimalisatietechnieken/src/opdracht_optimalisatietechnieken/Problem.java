package opdracht_optimalisatietechnieken;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Problem {
    private final int TRUCK_CAPACITY;
    private final int TRUCK_WORKING_TIME;
    private final List<Location> locationList;
    private final List<Depot> depotList;
    private List<Truck> truckList;
    private List<MachineType> machineTypeList;
    private List<Machine> machineList;
    private List<Drop> dropList;
    private List<Collect> collectList;
    private int[][] timeMatrix;
    private int[][] distanceMatrix;
    private Solution bestSolution;
    Random random;


    public Problem(int tRUCK_CAPACITY, int tRUCK_WORKING_TIME, List<Location> locationList, List<Depot> depotList,
                   List<Truck> truckList, List<MachineType> machineTypeList, List<Machine> machineList, List<Drop> dropList,
                   List<Collect> collectList, int[][] timeMatrix, int[][] distanceMatrix) {
        TRUCK_CAPACITY = tRUCK_CAPACITY;
        TRUCK_WORKING_TIME = tRUCK_WORKING_TIME;
        this.locationList = locationList;
        this.depotList = depotList;
        this.truckList = truckList;
        this.machineTypeList = machineTypeList;
        this.machineList = machineList;
        this.dropList = dropList;
        this.collectList = collectList;
        this.timeMatrix = timeMatrix;
        this.distanceMatrix = distanceMatrix;
        this.bestSolution = new Solution(distanceMatrix, timeMatrix);
    }

    public void solve(String inputFilename, String outputfilename, int randomSeed, long startime, long timeLimit) {
        Long first = System.currentTimeMillis();
        random = new Random(randomSeed);
        int initialTruckListSize = truckList.size();
        int dummys = 0;
        Solution initialSolution = null;
        boolean feasible = false;
        int poging = 1;
        while (!feasible) {
            if (poging % 10 == 0) {
                truckList.add(new Truck(initialTruckListSize + dummys, locationList.get(random.nextInt(locationList.size())), locationList.get(random.nextInt(locationList.size()))));
                dummys++;
            }
            initialSolution = generateInitialSolution();
            if (initialSolution.isFeasible()) {
                feasible = true;

            } else poging++;
        }
        Long feasableWith = System.currentTimeMillis();
        System.out.println("First solution generated, " + (dummys) + " dummytrucks. At " + (feasableWith - first) + " na " + poging + " pogingen");

        bestSolution = new Solution(initialSolution);
        poging = 0;
        do {
            poging++;
            List<Truck> emptyTrucks = bestSolution.getEmptyTrucks();
            List<Truck> dummyTrucks = bestSolution.getDummyTrucks(initialTruckListSize);
            if (poging % 1000 == 0)
                System.out.println("Na neighbour " + poging + " : " + bestSolution.totalDistance + " Dummytrucks: " + dummys);
            Solution newSolution = null;
            int neighbourmethode = random.nextInt(2);
            switch (neighbourmethode) {
                case 0: //CD verplaatsen
                    newSolution = moveDropCollect(bestSolution);
                    break;
                case 1: //C1D1 en C2D2 wisselen
                    newSolution = swapDropCollect(bestSolution);
                    break;

                //TODO: item verplaatsen van dummy naar gewone truck if possible!

                default:
                    System.out.println("Geen neighbourmethode gevonden");
                    break;
            }
            if (newSolution != null) {
                newSolution.updateTrucksDistancesAndTimes();
                if (newSolution.isFeasible()) {
                    if (newSolution.getTotalDistanceWithPenalty(initialTruckListSize) < bestSolution.getTotalDistanceWithPenalty(initialTruckListSize)) {

                        //eventuele lege dummytruck verwijderen
                        Truck toRemove = null;
                        for (Map.Entry<Truck, List<Action>> e : newSolution.routes.entrySet()) {
                            if (e.getKey().getId() >= initialTruckListSize && e.getValue().isEmpty()) {
                                toRemove = e.getKey();
                                dummys--;
                                break;
                            }
                        }
                        if (toRemove != null) {
                            newSolution.routes.remove(toRemove);
                            for (int i = initialTruckListSize - 1; i < truckList.size(); i++) {
                                if (truckList.get(i).getId() == toRemove.getId()) {
                                    truckList.remove(i);
                                    break;
                                }
                            }
                        }

                        //System.out.println("Betere oplossing: " + newSolution.totalDistance + " at: " + (nu-current));
                        bestSolution = newSolution;
                    }
                }
            } else System.out.println("Route null???");

            //TODO
            if (!emptyTrucks.isEmpty()) {
                System.out.println("----------EMPTY TRUCKS----------");
                emptyTrucks.stream().forEach(truck -> System.out.println(truck.getId()));
                dummyTrucks.stream().forEach(truck -> System.out.println(truck.getId()));

                Truck emptyTruck = emptyTrucks.get(0);
                Truck dummyTruck = dummyTrucks.get(0);

                bestSolution.deleteRoute(emptyTruck, bestSolution.getSolutionRoute(emptyTruck));
                bestSolution.addSolution(emptyTruck, bestSolution.getSolutionRoute(dummyTruck));
                bestSolution.deleteRoute(dummyTruck, bestSolution.getSolutionRoute(dummyTruck));
                dummys--;

            }


        } while (dummys > 0);


        Long feasableWithout = System.currentTimeMillis();
        System.out.println("Initial solution: " + truckList.size() + " total trucks. At " + (feasableWithout - first));
        //start heuristic with feasable solution without dummytrucks
        try {
            bestSolution.updateTrucksDistancesAndTimes();
            bestSolution.writeOuput(inputFilename, outputfilename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(bestSolution.totalDistance);


        long current = System.currentTimeMillis();
        long nu = 0;

        do {

            Solution newSolution = null;
            int neighbourmethode = random.nextInt(2);
            switch (neighbourmethode) {
                case 0: //CD verplaatsen
                    newSolution = moveDropCollect(bestSolution);
                    break;
                case 1: //C1D1 en C2D2 wisselen
                    newSolution = swapDropCollect(bestSolution);
                    break;
                case 2: //Cs en Ds 'beter' plaatsen van 1 truck
                    newSolution = rearrangeRoute(bestSolution);
                    break;
            }
            nu = System.currentTimeMillis();
            if (newSolution != null) {
                newSolution.updateTrucksDistancesAndTimes();
                if (newSolution.isFeasible()) {
                    if (newSolution.totalDistance < bestSolution.totalDistance) {
                        //System.out.println("Betere oplossing: " + newSolution.totalDistance + " at: " + (nu-current));
                        bestSolution = newSolution;
                        try {
                            bestSolution.writeOuput(inputFilename, outputfilename);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            } else System.out.println("Route null???");


        } while ((System.currentTimeMillis()) < (startime + timeLimit));
        System.out.println("end time  " + (System.currentTimeMillis() - startime));
        System.out.println(bestSolution.totalDistance);




        /*
        for(int i = 1; i<100; i++){
            System.out.println("ITERATIE " + i + "----------------------******************************");

            Solution s = moveDropCollect(bestSolution);
            if(s.totalDistance<bestSolution.totalDistance && s.isFeasible()){
                bestSolution = s;
                System.out.println("Huidige beste afstand: " + s.totalDistance);
            }
        }

        System.out.println(initialSolution.totalDistance);
        System.out.println(bestSolution.totalDistance);
        */

    }


    private Map<Machine, Depot> calculateInventory(List<Machine> machineList) {
        Map<Machine, Depot> depotInventory = new HashMap<>();
        for (Machine m : machineList) {
            for (Depot d : depotList) {
                if (m.getLocation().getId() == d.getLocation().getId()) {
                    depotInventory.put(m, d);
                }
            }
        }

        return depotInventory;
    }


    //generate initial solution
    public Solution generateInitialSolution() {
        Solution solution = null;
        solution = new Solution(this.distanceMatrix, this.timeMatrix);
        solution.setTempDrop(new ArrayList<>(dropList));
        solution.setTempCollect(new ArrayList<>(collectList));
        List<Truck> tempTrucks = new ArrayList<>(truckList);
        List<Machine> tempMachines = new ArrayList<>(machineList);
        List<Action> depotdrops = new ArrayList<>();

        //TODO: aanpassen naar optimalisatie, routes van huidige trucks proberen uitbreiden na herschikking?

        //blijven uitvoeren zolang er drops/collects zijn. TODO: eventueel splitsen in 2 while loops! eest collects uitvoeren, daarna overblijvende drops
        while (!solution.tempDrop.isEmpty() && !tempTrucks.isEmpty()) {

            //selecteer een random truck uit de trucklist
            Truck randomTruck = tempTrucks.get(random.nextInt(tempTrucks.size()));

            List<Action> actions = new ArrayList<>();

            for (int qq = 0; qq < 10; qq++) {
                //stel feasible route voor deze truck op
                actions = createRouteNew(randomTruck, solution.tempCollect, solution.tempDrop, tempMachines, actions, depotdrops);
                //actions = rearrangeRoute(actions, randomTruck);
            }

            if (actions.isEmpty()) {

            } else {
                // voeg deze truck met zijn route toe aan de solution
                solution.addSolution(randomTruck, actions);
                tempTrucks.remove(randomTruck);
            }
        }

        return solution;
    }

    private List<Action> createRoute(Truck randomTruck, List<Collect> tempCollect, List<Drop> tempDrop, List<Machine> tempMachines, List<Action> route) {

        Map<Machine, Depot> inventory = calculateInventory(tempMachines);
        Collect collect;
        Drop drop;
        Depot depot;

        Action collectAction;
        Action dropAction;

        //tempCollect is leeg. enkel nog drops uit te voeren naar depots
        if (tempCollect.isEmpty()) {
            //stopcriterium
            if (tempDrop.isEmpty()) {
                return route;

                //DEPOTCOLLECT --> DROP
            } else {
                drop = getClosestDrop(randomTruck, tempDrop, tempMachines, route);
                depot = drop.getClosestMachineDepot(distanceMatrix, inventory);

                collectAction = new Action(true, depot.getLocation(), getMachine(inventory, drop.getMachineType(), depot));
                dropAction = new Action(false, drop.getLocation(), collectAction.getMachine());

                route.add(collectAction);
                route.add(dropAction);

                if (isFeasible(randomTruck, route)) {
                    //route blijft behouden en nog extra acties toegevoegd uit resterende lijst.
                    inventory.remove(collectAction.getMachine(), depot);
                    tempMachines.remove(collectAction.getMachine());
                    tempDrop.remove(drop);

                    createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

                } else {
                    //niet feasible, voortgaan volgende truck
                    route.remove(collectAction);
                    route.remove(dropAction);

                    //TODO: mogelijk uit te breiden (momenteel neemt hij gewoon nieuwe truck wanneer toegevoegde Collect & Drop niet resulteren in feasible route)
                    //uitbreiding: eerst andere resterende collects en drops selecteren.

                }
            }
            //er zijn nog collects die uitgevoerd moeten worden.
        } else {
            collect = getClosestCollect(randomTruck, tempCollect, tempMachines, route);

            collectAction = new Action(true, collect.getLocation(), collect.getMachine());

            //geen drops meer of geen drop van zelfde type --> collect droppen in depot
            //COLLECT --> DEPOTDROP
            if (tempDrop.isEmpty() || !collect.hasRelatedDrop(tempDrop)) {
                //TODO: controleren of het niet slimmer is om meteen in eindlocatie te droppen ipv in dichtste depot.
                depot = collectAction.getLocation().getClosestDepot(distanceMatrix, depotList);
                dropAction = new Action(false, randomTruck.getStartLocation(), collectAction.getMachine());

                route.add(collectAction);
                route.add(dropAction);

                if (isFeasible(randomTruck, route)) {
                    //route blijft behouden en nog extra acties toegevoegd uit resterende lijst.
                    inventory.put(collect.getMachine(), depot);
                    tempCollect.remove(collect);

                    createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

                } else {
                    //niet feasible, voortgaan volgende truck
                    route.remove(collectAction);
                    route.remove(dropAction);

                    //TODO: mogelijk uit te breiden (momenteel neemt hij gewoon nieuwe truck wanneer toegevoegde Collect & Drop niet resulteren in feasible route)
                    //uitbreiding: eerst andere resterende collects en drops selecteren.

                }

                //nog overeenkomstige drops beschikbaar
                //COLLECT --> DROP
            } else {
                drop = collect.getClosestRelatedDrop(distanceMatrix, tempDrop);
                dropAction = new Action(false, drop.getLocation(), collectAction.getMachine());

                route.add(collectAction);
                route.add(dropAction);

                if (isFeasible(randomTruck, route)) {
                    //route blijft behouden en nog extra acties toegevoegd uit resterende lijst.
                    tempCollect.remove(collect);
                    tempDrop.remove(drop);

                    createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

                } else {
                    //niet feasible, voortgaan volgende truck
                    route.remove(collectAction);
                    route.remove(dropAction);

                    //TODO: mogelijk uit te breiden (momenteel neemt hij gewoon nieuwe truck wanneer toegevoegde Collect & Drop niet resulteren in feasible route)
                    //uitbreiding: eerst andere resterende collects en drops selecteren.

                }

            }

        }


        //TODO: Mogelijkheid tot route te verbeteren?


        return route;
    }

    public List<Action> rearrangeRoute(List<Action> route, Truck randomTruck) {
        List<Location> depotLocations = new ArrayList<>();
        for (Depot d : depotList) depotLocations.add(d.getLocation());

        for (int i = route.size() - 1; i > 0; i--) {
            List<Action> currentRoute = new ArrayList<>(route);
            if (!route.get(i).getType()) {
                if (depotLocations.contains(route.get(i).getLocation())) {
                    Action newDrop = new Action(false, randomTruck.getEndLocation(), route.get(i).getMachine());
                    currentRoute.remove(i);
                    currentRoute.add(newDrop);
                    if (isFeasible(randomTruck, currentRoute)) route = currentRoute;
                }
            }
        }
        for (int i = 0; i < route.size(); i++) {
            List<Action> currentRoute = new ArrayList<>(route);
            if (route.get(i).getType()) {
                if (route.get(i).getLocation() == randomTruck.getStartLocation()) {
                    Action newCollect = new Action(true, randomTruck.getStartLocation(), route.get(i).getMachine());
                    currentRoute.remove(i);
                    currentRoute.add(0, newCollect);
                    if (isFeasible(randomTruck, currentRoute)) route = currentRoute;
                }
            }
        }

        return route;
    }

    private Machine getMachine(Map<Machine, Depot> inventory, MachineType machineType, Depot depot) {
        Machine m = null;

        for (Map.Entry<Machine, Depot> entry : inventory.entrySet()) {
            if (entry.getValue() == depot && entry.getKey().getMachineType() == machineType) {
                m = entry.getKey();
            }
        }

        if (m == null) {
            System.out.println("ERROR GET MACHINE AT DEPOT");
        }

        return m;
    }

    private Drop getClosestDrop(Truck truck, List<Drop> tempDrop, List<Machine> tempMachines, List<Action> route) {
        Drop drop = null;

        if (tempDrop.isEmpty()) {
            System.out.println("ERROR EMPTY TEMPDROPLIST");
        }
        //trucks first action?
        if (route.isEmpty()) {
            drop = truck.getStartLocation().getClosestDrop(distanceMatrix, tempDrop);
        } else {
            drop = route.get(route.size() - 1).getLocation().getClosestDrop(distanceMatrix, tempDrop);
        }
        return drop;
    }

    //return closest (first) collect from tempcollect.
    private Collect getClosestCollect(Truck truck, List<Collect> tempCollect, List<Machine> tempMachines, List<Action> route) {
        Collect collect = null;
        if (tempCollect.isEmpty()) {
            System.out.println("ERROR EMPTY TEMPCOLLECTLIST");
            //errorhandling
        }
        //Trucks first action?
        if (route.isEmpty()) {
            collect = truck.getStartLocation().getClosestCollect(distanceMatrix, tempCollect);
        } else {
            collect = route.get(route.size() - 1).getLocation().getClosestCollect(distanceMatrix, tempCollect);
        }
        return collect;
    }

    public boolean isFeasible(Truck truck, List<Action> route) {
        int volume = truck.getResterendVolume();
        int time = truck.getTotalTime();


        for (Action a : route) {
            //Time constraint
            if (route.indexOf(a) == 0) {
                time += timeMatrix[truck.getStartLocation().getId()][a.getLocation().getId()];
            } else {
                time += timeMatrix[route.get(route.indexOf(a) - 1).getLocation().getId()][a.getLocation().getId()];
            }
            time += a.getServiceTime();

            //Volume constraint
            if (a.getType()) {
                volume -= a.getVolumeChange();
            } else {
                volume += a.getVolumeChange();
                if (volume > 100) {
                    volume = 100;
                }
            }

            if (!truck.checkRelatedCollect(route, a)) {
                return false;
            }

            if (volume < 0 || time > 600) {
                return false;
            }


        }

        time += timeMatrix[route.get(route.size() - 1).getLocation().getId()][truck.getEndLocation().getId()];
        if (volume < 0 || time > 600) {
            return false;
        }
        return true;
    }

    //swap 2 drop/collect
    private Solution swapDropCollect(Solution initialSolution) {
        //System.out.println("start neighbour searching");
        Solution bestSolution = new Solution(initialSolution);
        int iterations = 0;
        while (true) {
            //System.out.println("lus");
            Solution solution = new Solution(bestSolution);
            Action collect1 = null;
            Action drop1 = null;
            Action collect2 = null;
            Action drop2 = null;

            int collect1Index = 0;
            int drop1Index = 0;
            int collect2Index = 0;
            int drop2Index = 0;

            List<Truck> trucks = new ArrayList(solution.getRoutes().keySet());
            int aantalTrucks = trucks.size();

            //2 verschillende randomtrucks nemen
            Truck randomTruck1 = trucks.get(random.nextInt(aantalTrucks));
            while (solution.getRoutes().get(randomTruck1).size() <= 2) {
                randomTruck1 = trucks.get(random.nextInt(aantalTrucks));
            }
            Truck randomTruck2 = trucks.get(random.nextInt(aantalTrucks));
            while (randomTruck1.getId() == randomTruck2.getId() || solution.getRoutes().get(randomTruck2).size() <= 2) {
                randomTruck2 = trucks.get(random.nextInt(aantalTrucks));
            }

            List<Action> lijst1 = solution.getRoutes().get(randomTruck1);
            List<Action> lijst2 = solution.getRoutes().get(randomTruck2);


            //zoek  drop in randomtruck1
            while (true) {
                drop1Index = random.nextInt(lijst1.size());
                Action action = lijst1.get(drop1Index);
                if (action.getType() == false) {
                    drop1 = action;
                    break;
                }

            }

            //zoek bijhorende collection in randomtruck1
            while (true) {
                collect1Index = random.nextInt(lijst1.size());
                Action action = lijst1.get(collect1Index);
                if (action.getType() == true && action.getMachine().getId() == drop1.getMachine().getId()) {
                    collect1 = action;
                    break;
                }

            }
            //zoek drop in randomtruck2
            while (true) {
                drop2Index = random.nextInt(lijst2.size());
                Action action = lijst2.get(drop2Index);
                if (action.getType() == false) {
                    drop2 = action;
                    break;
                }

            }


            //zoek bijhorende collection in randomTruck2
            while (true) {
                collect2Index = random.nextInt(lijst2.size());
                Action action = lijst2.get(collect2Index);
                if (action.getType() == true && action.getMachine().getId() == drop2.getMachine().getId()) {
                    collect2 = action;
                    break;
                }

            }

            solution.getRoutes().get(randomTruck1).remove(collect1);
            solution.getRoutes().get(randomTruck1).remove(drop1);
            solution.getRoutes().get(randomTruck2).remove(collect2);
            solution.getRoutes().get(randomTruck2).remove(drop2);

            int newDrop1Index = 1 + random.nextInt(solution.getRoutes().get(randomTruck1).size());
            int newCollect1Index = random.nextInt(newDrop1Index);
            int newDrop2Index = 1 + random.nextInt(solution.getRoutes().get(randomTruck2).size());
            int newCollect2Index = random.nextInt(newDrop2Index);

            solution.getRoutes().get(randomTruck1).add(newCollect1Index, collect2);
            solution.getRoutes().get(randomTruck1).add(newDrop1Index, drop2);
            solution.getRoutes().get(randomTruck2).add(newCollect2Index, collect1);
            solution.getRoutes().get(randomTruck2).add(newDrop2Index, drop1);

            return solution;
            /*
            if(solution.isFeasible()){
            	System.out.println("new feasible solution met afstand "+ solution.getTotalDistance()+" de beste oplossing heeft een afstand "+bestSolution.getTotalDistance());
            	if(solution.getTotalDistance()<=bestSolution.getTotalDistance()){
            		bestSolution=new Solution(solution);
            		try {
						bestSolution.writeOuput("best.txt");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		System.out.println("new better solution: "+bestSolution.getTotalDistance()+bestSolution.isFeasible());
            	}
            }
            
        	if(iterations==100000){
        		break;
        	}
        	iterations++;
        	*/
        }

    }

    //Neighbour methode op basis van rearrangeRoute(...)
    private Solution rearrangeRoute(Solution solution) {
        Solution tempSolution = new Solution(solution);

        List<Map.Entry<Truck, List<Action>>> tempset = new ArrayList<>(tempSolution.routes.entrySet());
        Collections.shuffle(tempset);

        List<Action> newRoute = rearrangeRoute(tempset.get(0).getValue(), tempset.get(0).getKey());
        tempSolution.routes.put(tempset.get(0).getKey(), newRoute);

        return tempSolution;
    }

    // move collect-drop pair from one to another truck
    private Solution moveDropCollect(Solution solution) {
        //temp solution is kopie waarin de verwerkingen gebeuren van de acties
        Solution tempSolution = new Solution(solution);
        int aantalRoutes = tempSolution.routes.size();
        // from is de route waar de acties uit worden verwijderd
        // to is de route waar de acties aan toegevoegd worden
        List<Action> routesFrom = new ArrayList<>();
        List<Action> routesTo = null;

        Truck fromTruck = null;
        Truck toTruck = null;

        //starten van een "niet-lege" route
        while (routesFrom.isEmpty()) {
            List<Map.Entry<Truck, List<Action>>> tempset = new ArrayList<>(tempSolution.routes.entrySet());
            Collections.shuffle(tempset);

            routesFrom = tempset.get(0).getValue();
            routesTo = tempset.get(1).getValue();

            fromTruck = tempset.get(0).getKey();
            toTruck = tempset.get(1).getKey();
        }



        /*
        // 2 (verschillende) random getallen nemen die zullen overeenstemmen met 2 entries (of 2 routes)
        int indexFromTruck = 0;
        boolean fromRouteEmpty = true;
        while (fromRouteEmpty) {
            indexFromTruck = random.nextInt(aantalRoutes);

            for (Map.Entry<Truck, List<Action>> entry : tempSolution.routes.entrySet()) {
                Truck truck = entry.getKey();
                List<Action> list = entry.getValue();

                if (truck.getId() == indexFromTruck) {
                    // from-truck en -list gevonden
                    routesFrom = list;
                    if (routesFrom.size() == 0)  // geen acties in de lijst, kan niets verwijderd worden
                        fromRouteEmpty = true;
                    else
                        fromRouteEmpty = false;
                    break; // break from for loop
                }

            }
        }

        int indexToTruck = random.nextInt(aantalRoutes);
        Truck truckTo = null;
        // while loop verzekert 2 verschillende indexen van 0 - hoogste
        while(indexFromTruck==indexToTruck){
            indexToTruck = random.nextInt(aantalRoutes);
        }


        for (Map.Entry<Truck, List<Action>> entry : tempSolution.routes.entrySet()) {
            Truck truck = entry.getKey();
            List<Action> list = entry.getValue();
            if(truck.getId()==indexToTruck){
                truckTo = truck;
                routesTo = list;
                break; // break from for loop
            }
        }*/

        // find and delete collect-drop pair in FROM route
        int index = random.nextInt(routesFrom.size());

        Action collect = null, drop = null;
        Action temp = routesFrom.remove(index);

        if (temp.getType()) {
            collect = temp;
        } else {
            drop = temp;
        }

        // find associated collect or drop
        for (int i = 0; i < routesFrom.size(); i++) {
            if (routesFrom.get(i).getMachine().equals(temp.getMachine())) {
                if (temp.getType())
                    drop = routesFrom.remove(i);
                else collect = routesFrom.remove(i);
                break;
            }
        }


        //System.out.println(fromTruck.getId() + "   " + toTruck.getId());

        //Add them in ToRoute
        if (routesTo.size() != 0) {
            routesTo.add(random.nextInt(routesTo.size()), collect);
            int indexOfCollect = routesTo.indexOf(collect);
            int indexOfDrop = -1;
            while (indexOfDrop <= indexOfCollect) indexOfDrop = random.nextInt(routesTo.size());
            routesTo.add(indexOfDrop, drop);
        } else {
            routesTo.add(collect);
            routesTo.add(drop);
        }

        /*
        if(addCollectDropPairToRoute(collect,drop,truckTo,routesTo)){
            // feasible route gevonden met het extra collectdrop paar
            tempSolution.updateTrucksDistancesAndTimes();
            System.out.println("*********MOVE UITGEVOERD*************");
        }
        else
            System.out.println("----------MOVE NIET UITGEVOERD-----------");
        */
        return tempSolution;
    }

    private boolean addCollectDropPairToRoute(Action collect, Action drop, Truck to, List<Action> routesTo) {
        // toevoegen van drop/collect, collect en drop worden aansluitend aan mekaar gedaan (CD)
        // returnt true als er een feasible 'inert is gevonden voor die route'
        int aantalActions = routesTo.size();
        // collect en drop op random plaats toevoegen enkel drop mag niet op de laatste positie
        // (laatste drop is meestal op einddepot)
        // collect mag niet op laatste en voorlaatste

        List<Integer> indexen = new ArrayList<>();
        for (int i = 0; i < aantalActions - 2; i++)
            indexen.add(i);

        while (!indexen.isEmpty()) {
            int i = indexen.get(random.nextInt(indexen.size()));
            //for(int i = 0; i<aantalActions-2;i++){
            routesTo.add(i, collect);
            routesTo.add(i + 1, drop);
            if (Solution.isFeasibleTruck(to, routesTo)) {
                return true;
            }
            // wanneer de truck niet feasible is, acties terug verwijderen en op andere plaatsen invoegen
            indexen.remove(Integer.valueOf(i));
            routesTo.remove(collect);
            routesTo.remove(drop);
        }

        return false;
    }

    //Getters & Setters
    public List<Location> getLocationList() {
        return locationList;
    }

    public List<Depot> getDepotList() {
        return depotList;
    }

    public List<Truck> getTruckList() {
        return truckList;
    }

    public List<MachineType> getMachineTypeList() {
        return machineTypeList;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public List<Drop> getDropList() {
        return dropList;
    }

    public List<Collect> getCollectList() {
        return collectList;
    }

    public int[][] getTimeMatrix() {
        return timeMatrix;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public int getTRUCK_CAPACITY() {
        return TRUCK_CAPACITY;
    }

    private List<Action> createRouteNew(Truck randomTruck, List<Collect> tempCollect, List<Drop> tempDrop, List<Machine> tempMachines, List<Action> route, List<Action> drops) {
        Map<Machine, Depot> inventory = calculateInventory(tempMachines);
        Collect collect;
        Drop drop;
        Depot depot;

        Action collectAction;
        Action dropAction;

        //drops verwijderen om ze achteraf achteraan toe te voegen.
        drops.stream().forEach(action -> {
            route.remove(action);
        });

        //geen drops en collects meer resterend: stopcriterium
        if (tempCollect.isEmpty() && tempDrop.isEmpty()) {
            drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));
        }
        //geen drops meer, enkel nog collects uit te voeren en te droppen in depot
        else if (!tempCollect.isEmpty() && tempDrop.isEmpty()) {
            //Selecteer dichtste collect
            //selecteer eindlocatie
            //blijf nieuwe collect selecteren
            //zolang volumeconstraint niet wordt overschreden
            //timeconstraint met return naar eindlocatie indien oppikken niet wordt overschreden
            //drop alles opgepikte collects op eindlocatie.
            for (int i = 0; i < tempCollect.size(); i++) {
                collect = getClosestCollect(randomTruck, tempCollect, tempMachines, route);
                collectAction = new Action(true, collect.getLocation(), collect.getMachine());
                dropAction = new Action(false, randomTruck.getEndLocation(), collectAction.getMachine());

                route.add(collectAction);
                drops.add(dropAction);
                drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));

                //controleren of toevoegen van deze collect en alle drops nog feasible is
                if (isFeasible(randomTruck, route)) {

                    tempCollect.remove(collect);
                    tempMachines.remove(collectAction.getMachine());

                    createRouteNew(randomTruck, tempCollect, tempDrop, tempMachines, route, drops);
                } else {
                    //extra toevoeging niet feasible:
                    route.remove(collectAction);
                    route.removeAll(drops);
                    drops.remove(dropAction);
                    drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));
                    drops.clear();
                }
            }
        }
        //geen collects meer, enkel nog drops uit te voeren vanuit depots
        else if (tempCollect.isEmpty() && !tempDrop.isEmpty()) {
            //TODO!!
            drop = getClosestDrop(randomTruck, tempDrop, tempMachines, route);
            depot = drop.getClosestMachineDepot(distanceMatrix, inventory);

            collectAction = new Action(true, depot.getLocation(), getMachine(inventory, drop.getMachineType(), depot));
            dropAction = new Action(false, drop.getLocation(), collectAction.getMachine());

            route.add(collectAction);
            drops.add(dropAction);
            drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));

            if (isFeasible(randomTruck, route)) {
                tempDrop.remove(drop);
                tempMachines.remove(collectAction.getMachine());

                createRouteNew(randomTruck, tempCollect, tempDrop, tempMachines, route, drops);
            } else {
                //extra toevoeging niet feasible:
                route.remove(collectAction);
                route.removeAll(drops);
                drops.remove(dropAction);
                drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));
                drops.clear();
            }

        }
        //zowel nog drops als collects uit te voeren
        else if (!tempCollect.isEmpty() && !tempDrop.isEmpty()) {
            collect = getClosestCollect(randomTruck, tempCollect, tempMachines, route);
            collectAction = new Action(true, collect.getLocation(), collect.getMachine());

            if (collect.hasRelatedDrop(tempDrop)) {
                drop = collect.getClosestRelatedDrop(distanceMatrix, tempDrop);
                dropAction = new Action(false, drop.getLocation(), collectAction.getMachine());

                route.add(collectAction);
                drops.add(dropAction);
                drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));

                if (isFeasible(randomTruck, route)) {
                    tempCollect.remove(collect);
                    tempDrop.remove(drop);

                    createRouteNew(randomTruck, tempCollect, tempDrop, tempMachines, route, drops);
                } else {
                    //extra toevoeging niet feasible:
                    route.remove(collectAction);
                    route.removeAll(drops);
                    drops.remove(dropAction);
                    drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));
                    drops.clear();
                }
            }
            //Collect has no related drop --> must drop in depot
            //TODO
            else {
                dropAction = new Action(false, randomTruck.getStartLocation(), collectAction.getMachine());

                route.add(collectAction);
                drops.add(dropAction);
                drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));

                if (isFeasible(randomTruck, route)) {
                    tempCollect.remove(collect);
                    tempMachines.remove(collectAction.getMachine());

                    createRouteNew(randomTruck, tempCollect, tempDrop, tempMachines, route, drops);
                } else {
                    //extra toevoeging niet feasible:
                    route.remove(collectAction);
                    route.removeAll(drops);
                    drops.remove(dropAction);
                    drops.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator().forEachRemaining(action -> route.add(action));
                    drops.clear();
                }
            }
        }

        return route;
    }


}
