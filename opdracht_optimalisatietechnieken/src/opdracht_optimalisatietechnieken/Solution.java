package opdracht_optimalisatietechnieken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    Map<Truck, List<Location>> oplossing;
    int aantalKm;

    public Solution() {
        oplossing = new HashMap<>();
    }
    
    public void addOplossing(Truck truck,List<Location> locaties){
    	oplossing.put(truck, locaties);
    }

    public Map<Truck, List<Location>> getOplossing() {
		return oplossing;
	}

	public void setOplossing(Map<Truck, List<Location>> oplossing) {
		this.oplossing = oplossing;
	}

	public int getAantalKm() {
		return aantalKm;
	}

	public void setAantalKm(int aantalKm) {
		this.aantalKm = aantalKm;
	}


}
