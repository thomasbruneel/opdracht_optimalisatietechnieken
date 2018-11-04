package opdracht_optimalisatietechnieken;

public class Depot {

    private int id;
    private Location location;

    public Depot(int id, Location location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Depot met id: " + id + " op locatie met gegevens: " + location;
    }

}
