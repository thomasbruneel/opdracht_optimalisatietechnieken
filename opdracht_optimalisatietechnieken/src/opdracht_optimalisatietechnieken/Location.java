package opdracht_optimalisatietechnieken;

<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> e418c1c3d8233da63877cca894d45d2258ec7178
import java.util.List;

public class Location {
    private int id;
    private double latitude;
    private double longitude;
    private String name;
    private List<Location> orderedLocationList;
    public Location(int id, double latitude, double longitude, String name) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.orderedLocationList = new ArrayList<Location>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }
   
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void initOrderedLocationList(int[] distanceRow) {
    	
    	// TODO MAYBEEEEEEEEEE
    

    }

    //returns closestd collect from collectList
    public Collect getClosestCollect(int[][] distanceMatrix, List<Collect> collectList) {
        Collect c = null;
        int distance = 50000;

        for (Collect collect : collectList) {
            if (distanceMatrix[this.id][collect.getMachine().getLocation().getId()] < distance) {
                distance = distanceMatrix[this.id][collect.getMachine().getLocation().getId()];
                c = collect;
            }
        }
        return c;
    }

    //returns closest drop from dropList
    public Drop getClosestDrop(int[][] distanceMatrix, List<Drop> dropList) {
        Drop d = null;
        int distance = 50000;

        for (Drop drop : dropList) {
            if (distanceMatrix[this.id][drop.getLocation().getId()] < distance) {
                distance = distanceMatrix[this.id][drop.getLocation().getId()];
                d = drop;
            }
        }
        return d;
    }

    //returns closest depot
    public Depot getClosestDepot(int[][] distanceMatrix, List<Depot> depotList) {
        Depot d = null;
        int distance = 50000;

        for (Depot depot : depotList) {
            if (distanceMatrix[this.id][depot.getLocation().getId()] < distance) {
                distance = distanceMatrix[this.id][depot.getLocation().getId()];
                d = depot;
            }
        }

        return d;
    }

    @Override
    public String toString() {
        return "Locatie met id: " + id + "\tlatitude: " + latitude + "\tlongitude: " + longitude + "\tnaam: " + name;
    }
}
