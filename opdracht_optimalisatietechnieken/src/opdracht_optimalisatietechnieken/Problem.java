package opdracht_optimalisatietechnieken;

import java.io.IOException;
import java.util.*;

public class Problem {
    private final int TRUCK_CAPACITY;
    private final int TRUCK_WORKING_TIME;
    private List<Location> locationList;
    private List<Depot> depotList;
    private List<Truck> truckList;
    private List<MachineType> machineTypeList;
    private List<Machine> machineList;
    private List<Drop> dropList;
    private List<Collect> collectList;
    private int[][] timeMatrix;
    private int[][] distanceMatrix;
    private Solution bestSolution;

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

    public void solve() {

        Solution initialSolution = generateInitialSolution();

        try {
            initialSolution.writeOuput();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<Machine, Depot> calculateInventory(List<Machine> machineList) {
        Map<Machine, Depot> depotInventory = new HashMap<>();
        for (Machine m : machineList) {
            for (Depot d : depotList) {
                if (m.getLocation().getId() == d.getLocation().getId()) {
                    depotInventory.put(m, d);
                    break;
                }
            }
        }

        return depotInventory;
    }

    //Getters & Setters
    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<Depot> getDepotList() {
        return depotList;
    }

    public void setDepotList(List<Depot> depotList) {
        this.depotList = depotList;
    }

    public List<Truck> getTruckList() {
        return truckList;
    }

    public void setTruckList(List<Truck> truckList) {
        this.truckList = truckList;
    }

    public List<MachineType> getMachineTypeList() {
        return machineTypeList;
    }

    public void setMachineTypeList(List<MachineType> machineTypeList) {
        this.machineTypeList = machineTypeList;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public List<Drop> getDropList() {
        return dropList;
    }

    public void setDropList(List<Drop> dropList) {
        this.dropList = dropList;
    }

    public List<Collect> getCollectList() {
        return collectList;
    }

    public void setCollectList(List<Collect> collectList) {
        this.collectList = collectList;
    }

    public int[][] getTimeMatrix() {
        return timeMatrix;
    }

    public void setTimeMatrix(int[][] timeMatrix) {
        this.timeMatrix = timeMatrix;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public int getTRUCK_CAPACITY() {
        return TRUCK_CAPACITY;
    }

    public int getTRUCK_WORKING_TIME() {
        return TRUCK_WORKING_TIME;
    }

    //generate initial solution
    //TODO
    public Solution generateInitialSolution() {
        Solution solution = new Solution(this.distanceMatrix, this.timeMatrix);
        Random random = new Random();
        List<Drop> tempDrop = new ArrayList<>(dropList);
        List<Collect> tempCollect = new ArrayList<>(collectList);
        List<Truck> tempTrucks = new ArrayList<>(truckList);
        List<Machine> tempMachines = new ArrayList<>(machineList);
        List<Action> actions;


        //blijven uitvoeren zolang er drops/collects zijn. TODO: eventueel splitsen in 2 while loops! eest collects uitvoeren, daarna overblijvende drops
        while (!tempCollect.isEmpty() && !tempDrop.isEmpty()) {

            //selecteer een random truck uit de trucklist
            Truck randomTruck = tempTrucks.get(random.nextInt(tempTrucks.size()));
            tempTrucks.remove(randomTruck);

            List<Action> route = new ArrayList<>();

            //stel feasible route voor deze truck op
            actions = createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

            // voeg deze truck met zijn route toe aan de solution
            solution.addSolution(randomTruck, actions);

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
        //Collect at Depot --> Drop at Drop
        if (tempCollect.isEmpty()) {
            drop = getClosestDrop(randomTruck, tempDrop, tempMachines, route);
            depot = drop.getClosestMachineDepot(distanceMatrix, inventory);

            collectAction = new Action(true, depot.getLocation(), getMachine(inventory, drop.getMachineType(), depot));
            dropAction = new Action(false, drop.getLocation(), collectAction.getMachine());

            route.add(collectAction);
            route.add(dropAction);

            if (isFeasible(randomTruck, route)) {
                //route blijft behouden en nog extra acties toegevoegd uit resterende lijst.
                tempMachines.get(tempMachines.indexOf(collectAction.getMachine())).setLocation(dropAction.getLocation());
                inventory.remove(collectAction.getMachine(), depot);
                tempDrop.remove(drop);

                createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

            } else {
                //niet feasible, voortgaan volgende truck
                route.remove(collectAction);
                route.remove(dropAction);

                //TODO: mogelijk uit te breiden (momenteel neemt hij gewoon nieuwe truck wanneer toegevoegde Collect & Drop niet resulteren in feasible route)
                //uitbreiding: eerst andere resterende collects en drops selecteren.

                return route;
            }

            //er zijn nog collects die uitgevoerd moeten worden.
        } else {
            collect = getClosestCollect(randomTruck, tempCollect, tempMachines, route);
            depot = collect.getMachine().getLocation().getClosestDepot(distanceMatrix, depotList);

            collectAction = new Action(true, collect.getMachine().getLocation(), collect.getMachine());

            //geen drops meer of geen drop van zelfde type --> collect droppen in depot
            //Collect at Collect --> Drop at Depot
            if (tempDrop.isEmpty() || !collect.hasRelatedDrop(tempDrop)) {
                dropAction = new Action(false, depot.getLocation(), collect.getMachine());

                route.add(collectAction);
                route.add(dropAction);

                if (isFeasible(randomTruck, route)) {
                    //route blijft behouden en nog extra acties toegevoegd uit resterende lijst.
                    tempMachines.get(tempMachines.indexOf(collectAction.getMachine())).setLocation(dropAction.getLocation());
                    inventory.put(collect.getMachine(), depot);
                    tempCollect.remove(collect);

                    createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

                } else {
                    //niet feasible, voortgaan volgende truck
                    route.remove(collectAction);
                    route.remove(dropAction);

                    //TODO: mogelijk uit te breiden (momenteel neemt hij gewoon nieuwe truck wanneer toegevoegde Collect & Drop niet resulteren in feasible route)
                    //uitbreiding: eerst andere resterende collects en drops selecteren.

                    return route;
                }

                //nog overeenkomstige drops beschikbaar
                //Collect at Collect --> Drop at Drop
            } else {
                drop = collect.getClosestRelatedDrop(distanceMatrix, tempDrop);
                dropAction = new Action(false, drop.getLocation(), collect.getMachine());

                route.add(collectAction);
                route.add(dropAction);

                if (isFeasible(randomTruck, route)) {
                    //route blijft behouden en nog extra acties toegevoegd uit resterende lijst.
                    tempCollect.remove(collect);
                    tempDrop.remove(drop);
                    tempMachines.get(tempMachines.indexOf(collectAction.getMachine())).setLocation(dropAction.getLocation());

                    createRoute(randomTruck, tempCollect, tempDrop, tempMachines, route);

                } else {
                    //niet feasible, voortgaan volgende truck
                    route.remove(collectAction);
                    route.remove(dropAction);

                    //TODO: mogelijk uit te breiden (momenteel neemt hij gewoon nieuwe truck wanneer toegevoegde Collect & Drop niet resulteren in feasible route)
                    //uitbreiding: eerst andere resterende collects en drops selecteren.

                    return route;
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

    private Drop getClosestDrop(Truck randomTruck, List<Drop> tempDrop, List<Machine> tempMachines, List<Action> route) {
        Drop drop = null;

        if (tempDrop.isEmpty()) {
            System.out.println("ERROR EMPTY TEMPDROPLIST");
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
        //Truck's first action?
        if (route.isEmpty()) {
            collect = truck.getStartLocation().getClosestCollect(distanceMatrix, tempCollect);
        } else {
            collect = route.get(route.size() - 1).getLocation().getClosestCollect(distanceMatrix, tempCollect);
        }

        tempCollect.remove(collect);

        return collect;
    }

    private boolean isFeasible(Truck truck, List<Action> route) {
        int volume = truck.getResterendVolume();
        int time = truck.getTotalTime();


        for (Action a : route) {
            //Time constraint
            if (route.indexOf(a) == 0) {
                time += distanceMatrix[truck.getStartLocation().getId()][a.getLocation().getId()];
            } else {
                time += distanceMatrix[route.get(route.indexOf(a) - 1).getLocation().getId()][a.getLocation().getId()];
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

            if (volume < 0 || time > 600) {
                return false;
            }

        }

        return true;
    }
}
