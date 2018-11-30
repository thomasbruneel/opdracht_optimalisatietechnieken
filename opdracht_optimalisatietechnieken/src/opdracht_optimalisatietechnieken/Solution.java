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



    public boolean isFeasible(){
        for (Map.Entry<Truck, List<Action>> entry : this.routes.entrySet()){
            if (!isFeasibleTruck(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
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

        if (volume < 0 || time > 600) {
            return false;
        }
        return true;
    }



    public void moveCollectDropFromTruckToTruck(){
        Random random = new Random();
        int indexFromTruck = random.nextInt(routes.size());
        int indexToTruck = random.nextInt(routes.size());
        // while loop verzekert 2 verschillende indexen van 0 - hoogste
        while(indexFromTruck==indexToTruck){
            indexToTruck = random.nextInt(routes.size());
        }
        System.out.println("Aantal trucks: " + routes.size());
        System.out.println("From truck: " + indexFromTruck + "  To truck: " + indexToTruck);
        Truck truckFrom = null;
        Truck truckTo = null;

        List<Action> routesFrom = null;
        List<Action> routesTo = null;


        for (Map.Entry<Truck, List<Action>> entry : routes.entrySet()) {
            Truck truck = entry.getKey();
            List<Action> list = entry.getValue();

            if(truck.getId()==indexFromTruck){
                truckFrom = truck;
                routesFrom = list;
            }
            if(truck.getId()==indexToTruck){
                truckTo = truck;
                routesTo = list;
            }
        }

        int aantalActiesFromRoute = routesFrom.size();
        System.out.println("Aantel acties in from route: " + aantalActiesFromRoute);
        if(aantalActiesFromRoute==0){
            System.out.println("LEGE actieslijst -> returned");
            return;
        }

        // PRINTING ACTIONS
        System.out.println("*********ACTIONS FROM BEFORE MOVE***********");
        System.out.println(routesFrom);
        System.out.println("*********ACTIONS TO BEFORE MOVE***********");
        System.out.println(routesTo);



        // find and delete collect-drop pair in FROM route
        int index = random.nextInt(routesFrom.size());
        System.out.println("Index random actie: " + index);

        Action collect = null, drop = null;
        Action temp = routesFrom.remove(index);
        boolean collectPicked;
        if(temp.getType()) {
            collectPicked = true;
            collect = temp;
        }
        else {
            collectPicked = false;
            drop = temp;
        }

        for(int i = 0; i<routesFrom.size(); i++){
            if(routesFrom.get(i).getMachine().equals(temp.getMachine())){
                if(collectPicked)
                    drop = routesFrom.remove(i);
                else collect = routesFrom.remove(i);
                break;
            }
        }

        // add same collect-drop pair to TO route,routeToNew list updated
        System.out.println(collect);
        System.out.println(drop);
        System.out.println(truckTo);
        System.out.println(routesTo);
        if(addCollectDropPairToRoute(collect,drop,truckTo,routesTo)){
            // feasible route gevonden met het extra collectdrop paar
            int newDistance = getTotalDistanceWithPenalty();
            if(newDistance < totalDistance && isFeasible()) {
                // update solution if feasible and better
                updateTrucksDistancesAndTimes();
                System.out.println("*********MOVE UITGEVOERD*************");
            }
        }

        // PRINTING ACTIONS
        System.out.println("*********ACTIONS FROM AFTER MOVE***********");
        System.out.println(routesFrom);
        System.out.println("*********ACTIONS TO AFTER MOVE***********");
        System.out.println(routesTo);

    }

    private boolean addCollectDropPairToRoute(Action collect, Action drop, Truck to, List<Action> routesTo){
        // toevoegen van drop/collect, collect en drop worden aansluitend aan mekaar gedaan (CD)
        // returnt true als er een feasible 'inert is gevonden voor die route'
        int aantalActions = routesTo.size();
        // collect en drop op random plaats toevoegen enkel drop mag niet op de laatste positie
        // collect mag niet op laatste en voorlaatste
        Random random = new Random();

        for(int i = 0; i<aantalActions-2;i++){
            routesTo.add(i,collect);
            routesTo.add(i+1,drop);
            if(isFeasibleTruck(to,routesTo)){
                return true;
            }
            // wanneer de truck niet feasible is, acties terug verwijderen en op andere plaatsen invoegen
            routesTo.remove(collect);
            routesTo.remove(drop);
        }

        return false;
    }


}

