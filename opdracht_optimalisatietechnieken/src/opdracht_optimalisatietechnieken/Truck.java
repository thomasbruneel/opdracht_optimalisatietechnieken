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

    public void calculateTotalDistanceAndTime(int[][] distanceMatrix, int [][] timeMatrix, List<Action> actions){
        
    }

    @Override
    public String toString() {
        return "Truck met id: " + id + " met\nstartlocatie met gegevens:( " + startLocation +
                ")\nen eindlocatie met gegevens:( " + endLocation + ")";
    }
}
