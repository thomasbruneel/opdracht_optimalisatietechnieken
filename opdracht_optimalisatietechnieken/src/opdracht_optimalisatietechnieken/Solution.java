package opdracht_optimalisatietechnieken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    Map<Truck, List<Action>> routes;
    int totalDistance, totalTime;
    List<Drop> tempDrop = new ArrayList<>();
    List<Collect> tempCollect = new ArrayList<>();
    Random random = new Random();

    int[][] distanceMatrix;
    int[][] timeMatrix;

    public Solution(int[][] distanceMatrix, int[][] timeMatrix) {
        routes = new HashMap<>();
        int totalKm = 0;
        int totalTime = 0;
        this.distanceMatrix = distanceMatrix;
        this.timeMatrix = timeMatrix;
    }

    public Solution(Solution s) {
        this.routes = new HashMap<>();
        for (Map.Entry<Truck, List<Action>> entry : s.routes.entrySet()) {
            List<Action> acties = new ArrayList<>();
            for (Action a : entry.getValue()) acties.add(new Action(a));
            this.routes.put(new Truck(entry.getKey()), acties);
        }
        this.totalDistance = s.totalDistance;
        this.totalTime = s.totalTime;
        this.distanceMatrix = s.distanceMatrix;
        this.timeMatrix = s.timeMatrix;
    }

    public void addTruck(Truck truck) {
        routes.put(truck, new ArrayList<>());
    }

    public void addSolution(Truck truck, List<Action> locations) {
        routes.put(truck, locations);
    }

    public List<Action> getSolutionRoute(Truck truck) {
        return routes.get(truck);
    }

    public Map<Truck, List<Action>> getSolution() {
        return routes;
    }

    public void setSolution(Map<Truck, List<Action>> locations) {
        this.routes = locations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Map<Truck, List<Action>> getRoutes() {
        return routes;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public List<Drop> getTempDrop() {
        return tempDrop;
    }

    public void setTempDrop(List<Drop> tempDrop) {
        this.tempDrop = tempDrop;
    }

    public List<Collect> getTempCollect() {
        return tempCollect;
    }

    public void setTempCollect(List<Collect> tempCollect) {
        this.tempCollect = tempCollect;
    }

    public int getTotalDistanceWithPenalty(int edge) {
        int totalDistanceWithPenalty = 0;
        for (Map.Entry<Truck, List<Action>> entry : this.routes.entrySet()) {
            Truck truck = entry.getKey();
            List<Action> list = entry.getValue();
            int totalDistanceTruck = truck.getMatrixResult(distanceMatrix, list);
            if (truck.getId() >= edge)
                totalDistanceTruck *= 100000;
            totalDistanceWithPenalty += totalDistanceTruck;
        }

        return totalDistanceWithPenalty;
    }


    // Updates Trucks totalTime and totalDistance
    public void updateTrucksDistancesAndTimes() {
        this.totalDistance = this.totalTime = 0;
        for (Map.Entry<Truck, List<Action>> entry : this.routes.entrySet()) {
            entry.getKey().updateTruckInfo(this.distanceMatrix, this.timeMatrix, entry.getValue());
            this.totalDistance += entry.getKey().getTotalKm();
            this.totalTime += entry.getKey().getTotalTime();
        }
    }

    public boolean isFeasible() {
        for (Map.Entry<Truck, List<Action>> entry : this.routes.entrySet()) {
            if (!isFeasibleTruck(entry.getKey(), entry.getValue())) {
                return false;
            }

        }
        return tempDrop.isEmpty() && tempCollect.isEmpty();
    }

    public void writeOuput(String inputFilename, String outputfilename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputfilename));
        bw.write("PROBLEM: " + inputFilename);
        bw.write("\n");
        bw.write("DISTANCE: " + totalDistance);
        bw.write("\n");
        bw.write("TRUCKS: " + routes.size());
        bw.write("\n");
        for (Map.Entry<Truck, List<Action>> entry : routes.entrySet()) {
            bw.write(entry.getKey().getId() + " " + entry.getKey().getTotalKm() + " " + entry.getKey().getTotalTime() + " " + showStops(entry.getKey(), entry.getValue()));
            bw.write("\n");
        }
        bw.close();
    }

    private String showStops(Truck truck, List<Action> actions) {
        StringBuilder sb = new StringBuilder();
        int currentLocation = -1;
        int currentMachine = -1;
        int previousLocation = truck.getStartLocation().getId();
        sb.append(previousLocation);
        for (Action action : actions) {
            currentLocation = action.getLocation().getId();
            currentMachine = action.getMachine().getId();
            if (previousLocation == currentLocation) {
                sb.append(":" + currentMachine);
            } else {
                sb.append(" " + currentLocation + ":" + currentMachine);

            }
            previousLocation = currentLocation;

        }
        if (previousLocation != truck.getEndLocation().getId()) {

            sb.append(" " + truck.getEndLocation().getId());

        }

        return sb.toString();
    }

    public void swapCollects() {
        while (true) {
            Random rand = new Random();
            List<Truck> trucks = new ArrayList<Truck>(routes.keySet());
            Truck randomTruck = null;
            //neem een randomtruck met minstens 2 collects
            while (true) {
                randomTruck = trucks.get(rand.nextInt(trucks.size()));
                if (routes.get(randomTruck).size() > 2) {
                    break;
                }
            }

            List<Action> actions = routes.get(randomTruck);

            int action1Index = -1;
            Action action1;
            while (true) {
                action1Index = rand.nextInt(actions.size());
                action1 = actions.get(action1Index);
                if (action1.getType() == true) {
                    break;
                }

            }

            int action2Index = -1;
            Action action2;
            while (true) {
                action2Index = rand.nextInt(actions.size());
                action2 = actions.get(action2Index);
                if (action2.getType() == true && action1Index != action2Index) {
                    break;
                }

            }

            Collections.swap(routes.get(randomTruck), action1Index, action2Index);

            if (isFeasibleTruck(randomTruck, routes.get(randomTruck))) {
                break;
            } else {
                Collections.swap(routes.get(randomTruck), action1Index, action2Index);
            }

        }

    }

    public static boolean isFeasibleTruck(Truck truck, List<Action> route) {
        int volume = truck.getResterendVolume();
        int time = truck.getTotalTime();

        for (Action a : route) {
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
        if (volume < 0 || time > 600) {
            return false;
        }
        return true;
    }

    public List<Truck> getEmptyTrucks() {
        List<Truck> result = new ArrayList<>();
        this.routes.forEach((truck, actions) -> {
            if (actions.isEmpty()) {
                result.add(truck);
            }
        });

        return result;
    }

    public List<Truck> getDummyTrucks(int initialTruckListSize) {
        List<Truck> result = new ArrayList<>();
        this.routes.forEach((truck, actions) -> {
            if (truck.getId() >= initialTruckListSize) {
                result.add(truck);
            }
        });
        return result;
    }

    public void deleteRoute(Truck t, List<Action> actions) {
        this.routes.remove(t, actions);
    }

    public Solution moveDummyPair(int initialTrucklistSize) {
        Solution result = new Solution(distanceMatrix, timeMatrix);
        List<Truck> dummys = this.getDummyTrucks(initialTrucklistSize);
        List<Truck> trucks = this.routes.keySet().stream().filter(truck -> truck.getId() < initialTrucklistSize).collect(Collectors.toList());


        Truck dummyTruck = dummys.get(dummys.size() - 1);
        List<Action> dummyRoute = this.routes.get(dummyTruck);

        while (!this.routes.get(dummyTruck).isEmpty()) {
        List<Action> collects = dummyRoute.stream().filter(action -> action.getType() == true).collect(Collectors.toList());
        List<Action> drops = dummyRoute.stream().filter(action -> action.getType() == false).collect(Collectors.toList());

            Action collect = collects.get(0);
            Action drop = drops.stream().filter(action -> action.getMachine() == collect.getMachine()).findAny().orElse(null);

            this.routes.forEach((truck, actions) -> {
                if(addDummyPairCheck(truck, actions, collect, drop)){
                    //swapDummyPair(dummyTruck, truck, collect, drop);
                }
            });


        }
        return result;
    }

    // move collect-drop pair from one to another truck
    public Solution moveDropCollect() {
        //temp solution is kopie waarin de verwerkingen gebeuren van de acties
        Solution tempSolution = new Solution(this);
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

    //swap 2 drop/collect
    public Solution swapDropCollect() {
        //System.out.println("start neighbour searching");
        Solution bestSolution = new Solution(this);
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

    public boolean addDummyPairCheck(Truck truck, List<Action> actions, Action collect, Action drop) {
        List<Action> route = actions;
        route.add(collect);
        route.add(drop);

        if (isFeasibleTruck(truck, route)) {
            return true;
        }
        return false;
    }

/*    public boolean swapDummyPair(Truck dummyTruck, Truck truck, Action collect, Action drop){
        this.routes.get(dummyTruck).remove(collect);
        this.routes.get(dummyTruck).remove(drop);
        this.routes.get(truck).add(collect);
        this.routes.get(truck).add(drop);
    }*/
}

