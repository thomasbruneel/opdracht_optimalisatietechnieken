package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;

public class Problem {
	private final int TRUCK_CAPACITY;
	private final int TRUCK_WORKING_TIME;
	private final int SERVICE_TIME;
	private List<Location> location;
	private List<Depot> depot;
	private List<Truck> truck;
	private List<MachineType> machineType;
	private List<Machine> machine;
	private List<Drop> drop;
	private List<Collect> collect;
	private int[][] timeMatrix;
	private int[][] distanceMatrix;
	
	public Problem(int TRUCK_CAPACITY, int TRUCK_WORKING_TIME, int SERVICE_TIME, List<Location> location,
			List<Depot> depot, List<Truck> truck, List<MachineType> machineType, List<Machine> machine, List<Drop> drop,
			List<Collect> collect, int[][] timeMatrix, int[][] distanceMatrix) {
		super();
		this.TRUCK_CAPACITY = TRUCK_CAPACITY;
		this.TRUCK_WORKING_TIME = TRUCK_WORKING_TIME;
		this.SERVICE_TIME = SERVICE_TIME;
		this.location = location;
		this.depot = depot;
		this.truck = truck;
		this.machineType = machineType;
		this.machine = machine;
		this.drop = drop;
		this.collect = collect;
		this.timeMatrix = timeMatrix;
		this.distanceMatrix = distanceMatrix;
	}
	
	public void Solve() {
		// TODO 
	}
	
	// GETTERS AND SETTERS
	public List<Location> getLocation() {
		return location;
	}
	public void setLocation(List<Location> location) {
		this.location = location;
	}
	public List<Depot> getDepot() {
		return depot;
	}
	public void setDepot(List<Depot> depot) {
		this.depot = depot;
	}
	public List<Truck> getTruck() {
		return truck;
	}
	public void setTruck(List<Truck> truck) {
		this.truck = truck;
	}
	public List<MachineType> getMachineType() {
		return machineType;
	}
	public void setMachineType(List<MachineType> machineType) {
		this.machineType = machineType;
	}
	public List<Machine> getMachine() {
		return machine;
	}
	public void setMachine(List<Machine> machine) {
		this.machine = machine;
	}
	public List<Drop> getDrop() {
		return drop;
	}
	public void setDrop(List<Drop> drop) {
		this.drop = drop;
	}
	public List<Collect> getCollect() {
		return collect;
	}
	public void setCollect(List<Collect> collect) {
		this.collect = collect;
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
	public int getSERVICE_TIME() {
		return SERVICE_TIME;
	}
	
	
}
