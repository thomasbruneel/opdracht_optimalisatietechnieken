package opdracht_optimalisatietechnieken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static opdracht_optimalisatietechnieken.Main.*;

public class Solution {

    Map<Truck, List<Action>> routes;
    int totalDistance, totalTime;

    int[][] distanceMatrix;
    int[][] timeMatrix;

    public Solution(int[][] distanceMatrix, int[][] timeMatrix) {
        routes = new HashMap<>();
        int totalKm = 0;
        int totalTime = 0;
        this.distanceMatrix = distanceMatrix;
        this.timeMatrix = timeMatrix;
    }
    
    public Solution(Solution s){
    	this.routes=s.routes;
    	this.totalDistance=s.totalDistance;
    	this.totalTime=s.totalTime;
    	this.distanceMatrix=s.distanceMatrix;
    	this.timeMatrix=s.timeMatrix;
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

    public int getTotalDistanceWithPenalty(){
        int totalDistanceWithPenalty = 0;
        for (Map.Entry<Truck, List<Action>> entry : this.routes.entrySet()) {
            Truck truck = entry.getKey();
            List<Action> list = entry.getValue();
            int totalDistanceTruck = truck.getMatrixResult(distanceMatrix,list);
            if(truck.getId()>39)
                totalDistanceTruck*=100;
            totalDistanceWithPenalty += totalDistanceTruck;
        }

        return totalDistanceWithPenalty;
    }


    // Updates Trucks totalTime and totalDistance
    public void updateTrucksDistancesAndTimes() {
        for (Map.Entry<Truck, List<Action>> entry : this.routes.entrySet()) {
            entry.getKey().updateTruckInfo(this.distanceMatrix, this.timeMatrix, entry.getValue());
            this.totalDistance += entry.getKey().getTotalKm();
            this.totalTime += entry.getKey().getTotalTime();
        }
    }

    // TODO: feasibility van totale oplossing checken
    public boolean isFeasible(int machineSize) {
    	boolean[] collect=new boolean[machineSize];// wordt gebruikt voor te checken of machine 2 maal verplaatst zou worden
    	boolean[] drop=new boolean[machineSize];// idem + wordt gebruikt voor te checken of machine eerst gecollect wordt en dan gedropt wordt
    	//double t=0;
    	for (Map.Entry<Truck, List<Action>> entry : routes.entrySet()) {
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

    public void writeOuput(String outputfilename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputfilename));
        bw.write("PROBLEM: " + FILENAME);
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
    
    public void swapCollects(){
    	while(true){
        	Random rand = new Random(); 
        	List<Truck> trucks = new ArrayList<Truck>(routes.keySet());
        	Truck randomTruck=null;
        	//neem een randomtruck met minstens 2 collects
        	while(true){
        		randomTruck=trucks.get(rand.nextInt(trucks.size()));
        		if(routes.get(randomTruck).size()>2){
        			break;
        		}
        	}

        	List<Action>actions=routes.get(randomTruck);
        	
        	int action1Index=-1;
        	Action action1;
        	while(true){
        		action1Index=rand.nextInt(actions.size());
        		action1=actions.get(action1Index);
        		if(action1.getType()==true){
        			break;
        		}
        		
        	}

        	int action2Index=-1;
        	Action action2;
        	while(true){
        		action2Index = rand.nextInt(actions.size());
        		action2=actions.get(action2Index);
        		if(action2.getType()==true&&action1Index!=action2Index){
        			break;
        		}
        		
        	}

        	Collections.swap(routes.get(randomTruck), action1Index, action2Index);
        	
        	if(isFeasibleTruck(randomTruck, routes.get(randomTruck))){
        		break;
        	}
        	else{
        		Collections.swap(routes.get(randomTruck), action1Index, action2Index);
        	}
    		
    	}

    	
    	
    }
    
    public boolean isFeasibleTruck(Truck truck, List<Action> route) {
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

            if (!truck.checkRelatedCollect(route, a)){
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

}

