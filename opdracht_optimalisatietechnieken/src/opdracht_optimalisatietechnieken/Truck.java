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

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public int getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(int totalKm) {
        this.totalKm = totalKm;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getResterendVolume() {
        return resterendVolume;
    }

    public void setResterendVolume(int resterendVolume) {
        this.resterendVolume = resterendVolume;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    //calculates and sets the total distance & total time for this truck with Actionlist actions
    public void updateTruckInfo(int[][] distanceMatrix, int[][] timeMatrix, List<Action> actions) {
        this.totalKm = getMatrixResult(distanceMatrix, actions);
        this.totalTime = getMatrixResult(timeMatrix, actions);
        for (Action a : actions) {
            this.totalTime += a.getServiceTime();
        }

        for (Action a: actions){
            if (a.getType()){
                this.resterendVolume -= a.getVolumeChange();
            }else{
                this.resterendVolume += a.getVolumeChange();
                if (this.resterendVolume > 100){
                    this.resterendVolume =100;
                }
            }
        }
    }

    //returns results for Action sequence in actionList out of matrix
    public int getMatrixResult(int[][] matrix, List<Action> actions) {
        int result = 0;

        for (Action action : actions) {
            if (actions.indexOf(action) == 0) {
                result += matrix[startLocation.getId()][action.getLocation().getId()];
            } else {
                result += matrix[actions.get(actions.indexOf(action) - 1).getLocation().getId()][action.getLocation().getId()];
            }
        }
        result += matrix[actions.get(actions.size() - 1).getLocation().getId()][endLocation.getId()];

        return result;
    }

    //checks if volume change from action is allowed for this truck (volume constraint truck)
    public boolean checkVolume(Action action) {
        //false = drop & true = collect
        if (action.getType()) {
            this.resterendVolume -= action.getVolumeChange();
        } else {
            this.resterendVolume += action.getVolumeChange();
        }

        return this.resterendVolume >= 0 && this.resterendVolume <= 100;
    }

    public boolean checkTime(Action action, List<Action> route, int[][] timeMatrix) {
        this.totalTime = getMatrixResult(timeMatrix, route);
        for (Action a : route) {
            this.totalTime += a.getServiceTime();
        }
        this.totalTime += timeMatrix[route.get(route.size() - 1).getLocation().getId()][action.getLocation().getId()];
        this.totalTime += action.getServiceTime();

        this.totalTime += timeMatrix[action.getLocation().getId()][this.endLocation.getId()];

        return this.totalTime <= 600;
    }

    public void updateTruckInfo(Action a, int [][] distanceMatrix, int[][] timeMatrix){

    }

    //Checks if there is a drop, the collect from that machine is already executed
    public boolean checkRelatedCollect(List<Action> actions, Action action) {
        if (action.getType()) {
            return true;
        } else {
            for (Action a : actions) {
                return action.getMachine() == a.getMachine() && !a.getType();
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
