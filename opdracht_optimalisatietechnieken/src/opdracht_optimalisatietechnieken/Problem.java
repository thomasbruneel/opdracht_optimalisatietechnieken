package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    }

    public void solve() {

        boolean isFeasible = false;
        // Initiële feasible oplossing maken
        do {

        } while (isFeasible);
    }
    
    public boolean checkFeasibility(Solution s){
    	Map<Truck, List<Action>> route=s.getSolution();		//alle trucks
    	for (List<Action> actions : route.values()) {		//actions per truck
    		double serviceTime=0;
    		double drivingTime=0;
    		double workingTime=0;
    		double capacity=0;
    		int previousLocation=-1;
    		
    	    for(Action a:actions){
    	    	if(previousLocation==-1){	//startlocatie
    	    		if(a.getType()==true){		//type is collect
        	    		capacity=capacity+a.getMachine().getMachineType().getVolume();
        	    		
        	    	}
        	    	else{		//type is drop
        	    		capacity=capacity-a.getMachine().getMachineType().getVolume();
        	    	}
    	    		// nog geen driving time berekenen
    	    		
    	    		
    	    	}
    	    	else{
    	    		if(a.getType()==true){		//type is collect
        	    		capacity=capacity+a.getMachine().getMachineType().getVolume();
        	    		
        	    	}
        	    	else{		//type is drop
        	    		capacity=capacity-a.getMachine().getMachineType().getVolume();
        	    	}
    	    		drivingTime=drivingTime+distanceMatrix[previousLocation][a.getLocation().getId()];
    	    	}
    	    	previousLocation=a.getLocation().getId();
    	    	serviceTime=serviceTime+a.getMachine().getMachineType().getServiceTime();
    	    	workingTime=serviceTime+drivingTime;
    	    	
    	    	if(capacity>TRUCK_CAPACITY||workingTime>TRUCK_WORKING_TIME){
    	    		return false;
    	    	}
    	    	
    	    	
    	    }
    	}
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
