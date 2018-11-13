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
    public boolean isFeasible() {
/*        Map<Truck, List<Action>> route = s.getSolution();        //alle trucks
        for (List<Action> actions : route.values()) {        //actions per truck
            double serviceTime = 0;
            double drivingTime = 0;
            double workingTime = 0;
            double capacity = 0;
            int previousLocation = -1;

            for (Action a : actions) {
                if (previousLocation == -1) {    //startlocatie enkel collect mogelijk
                    if (a.getType() == true) {        //type is collect
                        capacity = capacity + a.getMachine().getMachineType().getVolume();

                    }

                    // nog geen driving time berekenen

                } else {
                    if (a.getType() == true) {        //type is collect
                        capacity = capacity + a.getMachine().getMachineType().getVolume();

                    } else {        //type is drop
                        capacity = capacity - a.getMachine().getMachineType().getVolume();
                    }
                    drivingTime = drivingTime + timeMatrix[previousLocation][a.getLocation().getId()];
                }
                previousLocation = a.getLocation().getId();
                serviceTime = serviceTime + a.getMachine().getMachineType().getServiceTime();
                workingTime = serviceTime + drivingTime;

                if (capacity > TRUCK_CAPACITY || workingTime > TRUCK_WORKING_TIME) {
                    return false;
                }


            }
        }*/
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

    public void addPaar(Truck randomTruck, Action collectAction, Action dropAction) {
        if (!route.keySet().contains(randomTruck)) addTruck(randomTruck);
        route.get(randomTruck).add(collectAction);
        route.get(randomTruck).add(dropAction);
        randomTruck.calculateTotalDistanceAndTime(distanceMatrix, timeMatrix, route.get(randomTruck));
    }
}

