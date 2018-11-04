package opdracht_optimalisatietechnieken;

public class Truck {
    private int id;
    private Location startLocation;
    private Location endLocation;

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

    @Override
    public String toString() {
        return "Truck met id: " + id + " met\nstartlocatie met gegevens:( " + startLocation +
                ")\nen eindlocatie met gegevens:( " + endLocation + ")";
    }
}
