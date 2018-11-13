package opdracht_optimalisatietechnieken;

import java.util.List;

public class Truck {
    private int id;
    private Location startLocation;
    private Location endLocation;
    private int totalKm, totalTime;

    public Truck(int id, Location startLocation, Location endLocation) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
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

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    //calculates and sets the total distance & total time for this truck with Actionlist actions
    public void calculateTotalDistanceAndTime(int[][] distanceMatrix, int [][] timeMatrix, List<Action> actions){
        this.totalKm = getMatrixResult(distanceMatrix, actions);
        this.totalTime = getMatrixResult(timeMatrix, actions);
    }

    //returns results for Action sequence in actionList out of matrix
    public int getMatrixResult(int [][] matrix, List<Action> actions){
        int result = 0;

        for (Action action : actions){
            if (actions.indexOf(action) == 0){
                result +=  matrix[startLocation.getId()][action.getLocation().getId()];
            }else{
                result += matrix[actions.get(actions.indexOf(action)-1).getLocation().getId()][action.getLocation().getId()];
            }

        }
        result+= matrix[actions.get(actions.size()-1).getLocation().getId()][endLocation.getId()];

        return result;
    }

    @Override
    public String toString() {
        return "Truck met id: " + id + " met\nstartlocatie met gegevens:( " + startLocation +
                ")\nen eindlocatie met gegevens:( " + endLocation + ")";
    }
}
