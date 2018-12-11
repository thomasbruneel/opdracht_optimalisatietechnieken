package opdracht_optimalisatietechnieken;

import java.util.List;

public class Truck {
    private int id;
    private Location startLocation;
    private Location endLocation;
    private int totalKm, totalTime;
    private int resterendVolume;

    public Truck(int id, Location startLocation, Location endLocation) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.resterendVolume = 100;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public int getTotalKm() {
        return totalKm;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getResterendVolume() {
        return resterendVolume;
    }

    //calculates and sets the total distance & total time for this truck with Actionlist actions
    public void updateTruckInfo(int[][] distanceMatrix, int[][] timeMatrix, Route route) {
        this.totalKm = getMatrixResult(distanceMatrix, route);
        this.totalTime = getMatrixResult(timeMatrix, route);
        for (Action a : route.getRoute()) {
            this.totalTime += a.getServiceTime();
        }
    }

    //returns results for Action sequence in actionList out of matrix
    public int getMatrixResult(int[][] matrix, Route route) {
        int result = 0;
        if (route.getRoute().isEmpty()) {
            return 0;
        } else {
            for (Action action : route.getRoute()) {
                if (route.getRoute().indexOf(action) == 0) {
                    result += matrix[startLocation.getId()][action.getLocation().getId()];
                } else {
                    result += matrix[route.getRoute().get(route.getRoute().indexOf(action) - 1).getLocation().getId()][action.getLocation().getId()];
                }
            }
            result += matrix[route.getRoute().get(route.getRoute().size() - 1).getLocation().getId()][endLocation.getId()];

            return result;
        }
    }

    //Checks if there is a drop, the collect from that machine is already executed
    public boolean checkRelatedCollect(List<Action> actions, Action action) {
        //collect action
        if (action.getType()) {
            return true;
            //drop action
        } else {
            for (Action a : actions) {
                if (action.getMachine().getMachineType() == a.getMachine().getMachineType() && a.getType()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String toString() {
        return "Truck met id: " + id + " met\nstartlocatie met gegevens:( " + startLocation +
                ")\nen eindlocatie met gegevens:( " + endLocation + ")";
    }
}
