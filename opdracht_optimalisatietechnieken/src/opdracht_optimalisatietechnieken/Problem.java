package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;

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
	 // TODO svekke-generated method	
	}
	
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
