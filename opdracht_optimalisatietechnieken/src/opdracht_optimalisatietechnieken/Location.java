package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
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

    @Override
    public String toString() {
        return "Locatie met id: " + id + "\tlatitude: " + latitude + "\tlongitude: " + longitude + "\tnaam: " + name;
    }
}
