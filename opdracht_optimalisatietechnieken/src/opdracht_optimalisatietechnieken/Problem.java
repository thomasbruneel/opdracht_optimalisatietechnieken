package opdracht_optimalisatietechnieken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
            //add dummytruck
            if(attempt%100 == 0) truckList.add(new Truck(truckList.size(),
                    depotList.get(random.nextInt(depotList.size())).getLocation(),
                    depotList.get(random.nextInt(depotList.size())).getLocation()));

            attempt++;
            System.out.println("--------------------New solution attempt: " + attempt + "--------------------");

            isFeasible = true;
            List<Drop> tempDrop = new ArrayList<>(dropList);
            List<Collect> tempCollect = new ArrayList<>(collectList);
            bestSolution = new Solution(distanceMatrix, timeMatrix);
            Map<Machine,Depot> depotInventory = calculateInventory();
            List<Location> depotLocations = new ArrayList<>();
            for (Depot d : depotList) depotLocations.add(d.getLocation());

            for (Drop r: tempDrop) {

                List<Machine> availableMachines = r.calculatAvailableMachines(tempCollect, depotInventory);
                List<Location> availableMachinesLocations = new ArrayList<>();
                for (Machine m: availableMachines) availableMachinesLocations.add(m.getLocation());
                Machine chosenMachine;
                Location closestmachineLocation = getClosestLocation(r.getLocation(),availableMachinesLocations);
                //keuze machine
                chosenMachine = availableMachines.get(availableMachinesLocations.indexOf(closestmachineLocation));
                depotInventory.remove(chosenMachine);
                Collect collect = null;
                for (Collect c : tempCollect){
                    if (c.getMachine() == chosenMachine) collect = c;
                }
                tempCollect.remove(collect);

                Truck randomTruck = null;
                if(depotLocations.contains(chosenMachine.getLocation())){
                    List<Truck> truckswithstartDepot = truckList.stream().filter(t -> t.getStartLocation() == chosenMachine.getLocation()).collect(Collectors.toList());
                    randomTruck = truckswithstartDepot.get(random.nextInt(truckswithstartDepot.size()));
                } else {
                    Location depotLocation = getClosestLocation(r.getLocation(),depotLocations);
                    List<Truck> truckswithstartDepot = truckList.stream().filter(t -> t.getStartLocation() == depotLocation).collect(Collectors.toList());
                    randomTruck = truckswithstartDepot.get(random.nextInt(truckswithstartDepot.size()));
                }

                


                Action collectAction = new Action(chosenMachine);
                Action dropAction = new Action(r.getLocation(), chosenMachine);

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
                Truck randomTruck = truckList.get(random.nextInt(truckList.size() - 1));

                Location randomDepot = randomTruck.getEndLocation();
                Action collectAction = new Action(c.getMachine());
                Action dropAction = new Action(randomDepot,c.getMachine());

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
	private final int TRUCK_CAPACITY;
	private final int TRUCK_WORKING_TIME;
	private List<Location> locationList;
	private List<Depot> depotList;
	private List<Truck> truckList;
	private List<MachineType> machineTypeList;
	private List<Machine> machineList;
	private List<Drop> dropList;
	private List<Collect> collectList;
	private List<List<Location>> orderedLocations;
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
		
		
		
		/* TODO MAYBE 
		// Per locatie A een lijst van locaties bijhouden gesorteerd per afstand
		orderedLocations = new ArrayList<>();
		
		
		for(int i = 0; i<locationList.size(); i++) {
			Location loc = locationList.get(i);
			loc.initOrderedLocationList(distanceMatrix[i]);
		}
		*/
		
	}

	public void solve() {

		// STAP1: Initiële feasible oplossing maken

		// ---WORK IN PROGRESS---

		boolean isFeasible = true;
		Random random = new Random();
		int attempt = 0;
		do {
			if (attempt % 100 == 0)
				truckList.add(new Truck(truckList.size(), locationList.get(random.nextInt(locationList.size() - 1)),
						locationList.get(random.nextInt(locationList.size() - 1))));

			attempt++;
			
			System.out.println("--------------------New solution attempt: " + attempt + "--------------------");

			isFeasible = true;
			List<Drop> tempDrop = new ArrayList<>(dropList);
			List<Collect> tempCollect = new ArrayList<>(collectList);
			bestSolution = new Solution(distanceMatrix, timeMatrix);
			Map<Machine, Depot> depotInventory = calculateInventory();

			for (Drop r : tempDrop) {

				List<Machine> availableMachines = r.calculatAvailableMachines(tempCollect, depotInventory);
				Machine chosenMachine;
				chosenMachine = availableMachines.size() < 2 ? availableMachines.get(0)
						: availableMachines.get(random.nextInt(availableMachines.size() - 1));
				depotInventory.remove(chosenMachine);
				Collect collect = null;
				for (Collect c : tempCollect) {
					if (c.getMachine() == chosenMachine)
						collect = c;
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

			for (Collect c : tempCollect) {
				if (!isFeasible)
					break;
				Location randomDepot = depotList.get(random.nextInt(depotList.size() - 1)).getLocation();
				Action collectAction = new Action(c.getMachine());
				Action dropAction = new Action(randomDepot, c.getMachine());

				Truck randomTruck = truckList.get(random.nextInt(truckList.size() - 1));

				bestSolution.addPaar(randomTruck, collectAction, dropAction);
				bestSolution.calculateTotalDistanceAndTime();
				if (randomTruck.getTotalTime() > TRUCK_WORKING_TIME)
					isFeasible = false;
				System.out.println("Truck: " + randomTruck.getId() + " TotTime: " + randomTruck.getTotalTime());
			}

		} while (!isFeasible);

		// Beste = initiële

		// STAP2: Neighbours zoeken +feasible checken

		// STAP3: Stopcriterium

		try {
			bestSolution.writeOuput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<Machine, Depot> calculateInventory() {
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

	public boolean checkTemporaryFeasibility() {
		/*
		 * TODO: Truck totaltime < 600 TODO: Capacity < 100% TODO: check if
		 * drop/collectlists are empty TODO: ...
		 */

		return true;
	}

	// Getters & Setters
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
	
	public Location getClosestLocation (Location startLocation, List<Location> list) {
		int startLocationId = startLocation.getId();
		int[] distRow = distanceMatrix[startLocationId];
		int locationWithSmallesDistanceId = startLocationId;
		int smallestDistance = Integer.MAX_VALUE;
		
		if(list.isEmpty()) return null; // return null when list is empty
		
		for(Location loc : list) {
			int locId = loc.getId();
			
			// skip update of smallest distance if location to check is the same as the start location
			if(locId!=startLocationId){
				int distanceToLocation = distRow[locId];
				if(smallestDistance > distanceToLocation)
					smallestDistance = distanceToLocation;
					locationWithSmallesDistanceId = locId;
			}
		}
		
		return locationList.get(locationWithSmallesDistanceId);
	}
	
}
