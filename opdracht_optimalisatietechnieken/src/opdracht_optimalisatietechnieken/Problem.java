package opdracht_optimalisatietechnieken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        // STAP1: Initiële feasible oplossing maken

        // ---WORK IN PROGRESS---

        boolean isFeasible = true;
        Random random = new Random();
        int attempt = 0;
        do {
            outerloop:

            attempt++;
            System.out.println("--------------------New solution attempt: " + attempt + "--------------------");

            isFeasible = true;
            List<Drop> tempDrop = new ArrayList<>(dropList);
            List<Collect> tempCollect = new ArrayList<>(collectList);
            bestSolution = new Solution(distanceMatrix, timeMatrix);
            Map<Machine,Depot> depotInventory = calculateInventory();

            for (Drop r: tempDrop) {

                List<Machine> availableMachines = r.calculatAvailableMachines(tempCollect, depotInventory);
                Machine chosenMachine;
                chosenMachine = availableMachines.size() < 2 ? availableMachines.get(0) : availableMachines.get(random.nextInt(availableMachines.size() - 1));
                depotInventory.remove(chosenMachine);
                Collect collect = null;
                for (Collect c : tempCollect){
                    if (c.getMachine() == chosenMachine) collect = c;
                }
                tempCollect.remove(collect);

                Action collectAction = new Action(chosenMachine);
                Action dropAction = new Action(r.getLocation(), chosenMachine);

                Truck randomTruck = truckList.get(random.nextInt(truckList.size() - 1));

                bestSolution.addPaar(randomTruck, collectAction, dropAction);
                bestSolution.calculateTotalDistanceAndTime();
                if (randomTruck.getTotalTime() > TRUCK_WORKING_TIME) {
                    isFeasible = false;
                    break;
                }
                System.out.println("Truck: " + randomTruck.getId() + " TotTime: " + randomTruck.getTotalTime());
            }

            System.out.println("Rest van collects verwerken: " + tempCollect.size());

            for(Collect c: tempCollect){
                if(!isFeasible) break;
                Location randomDepot = depotList.get(random.nextInt(depotList.size()-1)).getLocation();
                Action collectAction = new Action(c.getMachine());
                Action dropAction = new Action(randomDepot,c.getMachine());

                Truck randomTruck = truckList.get(random.nextInt(truckList.size() - 1));

                bestSolution.addPaar(randomTruck,collectAction,dropAction);
                bestSolution.calculateTotalDistanceAndTime();
                if (randomTruck.getTotalTime() > TRUCK_WORKING_TIME) isFeasible = false;
                System.out.println("Truck: " + randomTruck.getId() + " TotTime: " + randomTruck.getTotalTime());
            }

        } while (!isFeasible);

        //Beste = initiële

        //STAP2: Neighbours zoeken +feasible checken

        //STAP3: Stopcriterium

        try {
            bestSolution.writeOuput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<Machine,Depot> calculateInventory() {
    	Map <Machine,Depot> depotInventory=new HashMap<>();
    	for(Machine m:machineList){
    		for(Depot d:depotList){
    			if(m.getLocation().getId()==d.getLocation().getId()){
    				depotInventory.put(m, d);
    				break;
    			}
    		}
    	}
    	
        return depotInventory;
    }

    public boolean checkTemporaryFeasibility(){
        /*TODO: Truck totaltime < 600
          TODO: Capacity < 100%
          TODO: check if drop/collectlists are empty
          TODO: ...
        */

    return true;
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
}
