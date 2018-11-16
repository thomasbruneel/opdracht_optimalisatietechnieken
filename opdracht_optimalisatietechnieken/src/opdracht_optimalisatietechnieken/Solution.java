package opdracht_optimalisatietechnieken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static opdracht_optimalisatietechnieken.Main.*;

public class Solution {

    Map<Truck, List<Action>> route;
    int totalDistance, totalTime;

    int[][] distanceMatrix;
    int[][] timeMatrix;

    public Solution(int[][] distanceMatrix, int[][] timeMatrix) {
        route = new HashMap<>();
        int totalKm = 0;
        int totalTime = 0;
        this.distanceMatrix = distanceMatrix;
        this.timeMatrix = timeMatrix;
    }

    public void addTruck(Truck truck) {
        route.put(truck, new ArrayList<>());
    }

    public void addSolution(Truck truck, List<Action> locations) {
        truck.calculateTotalDistanceAndTime(this.distanceMatrix, this.timeMatrix, locations);
        route.put(truck, locations);
    }

    public List<Action> getSolutionRoute(Truck truck) {
        return route.get(truck);
    }

    public Map<Truck, List<Action>> getSolution() {
        return route;
    }

    public void setSolution(Map<Truck, List<Action>> locations) {
        this.route = locations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Map<Truck, List<Action>> getRoute() {
        return route;
    }

    public void setRoute(Map<Truck, List<Action>> route) {
        this.route = route;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    //calculate total time of complete solution
    public void calculateTotalDistanceAndTime() {
        this.totalTime = 0;
        this.totalDistance = 0;
        for (Map.Entry<Truck, List<Action>> entry : this.route.entrySet()) {
            this.totalDistance += entry.getKey().getTotalKm();
            this.totalTime += entry.getKey().getTotalTime();
        }
    }

    //Updates Trucks totalTime and totalDistance
    public void updateTrucksDistanceAndTime() {
        for (Map.Entry<Truck, List<Action>> entry : this.route.entrySet()) {
            entry.getKey().calculateTotalDistanceAndTime(this.distanceMatrix, this.timeMatrix, entry.getValue());
        }
    }

    //TODO:
    public boolean isFeasible(int machineSize) {
    	//System.out.println("aantal trucks: "+route.size());
    	boolean[] collect=new boolean[machineSize];// wordt gebruikt voor te checken of machine 2 maal verplaatst zou worden
    	boolean[] drop=new boolean[machineSize];// idem + wordt gebruikt voor te checken of machine eerst gecollect wordt en dan gedropt wordt
    	//double t=0;
    	for (Map.Entry<Truck, List<Action>> entry : route.entrySet()) {
    		Truck truck=entry.getKey();
    		List<Action> actions=entry.getValue();
            double truckTime = 0;
            double capacity = 0;
            int currentLocation = -1;
            Machine currentMachine = null;
            
            int previousLocation = truck.getStartLocation().getId();
            for(Action action:actions){
                double serviceTime = 0;
                double drivingTime = 0;
 
                currentLocation = action.getLocation().getId();
                currentMachine = action.getMachine();

                
                if(action.getType()==true){
                	//action is collect
                	capacity=capacity+currentMachine.getMachineType().getVolume();
                	
                	if(collect[currentMachine.getId()]==false){
                		collect[currentMachine.getId()]=true;
                	}
                	else{
                		return false;
                	}
                	
                }
                else{
                	//action is drop
                	capacity=capacity-currentMachine.getMachineType().getVolume();
                	
                	if(drop[currentMachine.getId()]==false){
                		drop[currentMachine.getId()]=true;
                	}
                	else{
                		return false;
                	}
                	
                	if(collect[currentMachine.getId()]==false){
                		return false;
                	}

                }
                	
                serviceTime=currentMachine.getMachineType().getServiceTime();
                drivingTime=timeMatrix[previousLocation][currentLocation];
                truckTime=truckTime+serviceTime+drivingTime;
                if(truckTime>600||capacity>100){
                	return false;
                }
                
                previousLocation = currentLocation;
                

            }
            if(previousLocation!=truck.getEndLocation().getId()){
            	truckTime=truckTime+timeMatrix[previousLocation][truck.getEndLocation().getId()];
            	if(truckTime>600){
            		return false;
            	}
            }
            
            //t=t+truckTime;

           }
    	//System.out.println("total time "+t);
    	return true;
    	}
    	
    

    public void writeOuput() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("tvh_solution.txt"));
        bw.write("PROBLEM: " + FILENAME);
        bw.write("\n");
        bw.write("DISTANCE: " + totalDistance);
        bw.write("\n");
        bw.write("TRUCKS: " + route.size());
        bw.write("\n");
        for (Map.Entry<Truck, List<Action>> entry : route.entrySet()) {
            bw.write(entry.getKey().getId() + " " + entry.getKey().getTotalKm() + " " + entry.getKey().getTotalTime() + " " + showStops(entry.getKey(),entry.getValue()));
            bw.write("\n");
        }
        bw.close();
    }

    private String showStops(Truck truck,List<Action> actions) {
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
                sb.append(" " + currentLocation + ":" + currentMachine + " ");

            }
            previousLocation = currentLocation;

        }
        if(previousLocation!=truck.getEndLocation().getId()){

        	sb.append(" "+truck.getEndLocation().getId());

        }
        
        return sb.toString();
    }
        //Kan suuuuuuuuuuuuuuuper veel eficiënter :p
    public void addPaar(Truck randomTruck, Action collectAction, Action dropAction) {
        if (!route.keySet().contains(randomTruck)) addTruck(randomTruck);
        route.get(randomTruck).add(collectAction);
        route.get(randomTruck).add(dropAction);
        randomTruck.calculateTotalDistanceAndTime(distanceMatrix, timeMatrix, route.get(randomTruck));
    }
}

