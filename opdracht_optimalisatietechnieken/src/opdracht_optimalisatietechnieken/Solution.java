package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    Map<Truck, List<Action>> route;
    int totalKm, totalTime;

    public Solution() {
        route = new HashMap<>();
        int totalKm = 0;
        int totalTime = 0;
    }

    public void addTruck(Truck truck) {
        route.put(truck, new ArrayList<>());
    }

    public void addSolution(Truck truck, List<Action> locations) {
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

    public int getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(int totalKm) {
        this.totalKm = totalKm;
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

    //calculate total distance of the different trucks and their routes in the solution
    public int calculateTotalKm(int[][] distanceMatrix) {
        int result = 0;

        result = getMatrixResult(distanceMatrix, result);
        this.totalKm = result;
        return result;
    }

    //calculate total time of tdifferent truck routes in the solution
    public int calculateTotalTime(int[][] TimeMatrix) {
        int result = 0;

        result = getMatrixResult(TimeMatrix, result);
        this.totalTime = result;
        return result;
    }

    //Calculate sum from matrix
    private int getMatrixResult(int[][] TimeMatrix, int result) {
        for (Map.Entry<Truck, List<Action>> entry : route.entrySet()) {
            int temp = 0;
            Location start = entry.getKey().getStartLocation();
            Location end = entry.getKey().getEndLocation();
            for (Action action : entry.getValue()) {
                if (entry.getValue().indexOf(action) == 0) {
                    temp += TimeMatrix[start.getId()][action.getLocation().getId()];
                } else {
                    int index = entry.getValue().indexOf(action);
                    temp += TimeMatrix[entry.getValue().get(index - 1).getLocation().getId()][action.getLocation().getId()];
                }
            }
            result += temp;
        }
        return result;
    }

}

