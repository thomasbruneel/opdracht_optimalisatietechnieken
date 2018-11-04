package opdracht_optimalisatietechnieken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    Map<Truck, List<Action>> route;
    int totalKm;

    public Solution() {
        route = new HashMap<>();
        int totalKm = 0;
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

    //calculate total distance of the different trucks and their routes in the solution
    public int calculateTotalKm(int[][] distanceMatrix) {
        int result = 0;

        for (Map.Entry<Truck, List<Action>> entry : route.entrySet()) {
            int temp = 0;
            Location start = entry.getKey().getStartLocation();
            Location end = entry.getKey().getEndLocation();
            for (Location l : entry.getValue()) {
                if (entry.getValue().indexOf(l) == 0) {
                    temp += distanceMatrix[start.getId()][l.getId()];
                } else {
                    int index = entry.getValue().indexOf(l);
                    temp += distanceMatrix[entry.getValue().get(index - 1).getId()][l.getId()];
                }
            }
            result += temp;
        }
        this.totalKm = result;
        return result;
    }

}

