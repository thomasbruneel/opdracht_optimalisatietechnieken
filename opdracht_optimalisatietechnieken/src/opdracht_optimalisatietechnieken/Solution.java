package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    Map<Truck, List<Action>> oplossing;
    int aantalKm;

    public Solution() {
        oplossing = new HashMap<>();
    }
    
    public void addOplossing(Truck truck,List<Action> locaties){
    	oplossing.put(truck, locaties);
    }

    public void addTruck(Truck truck){
        oplossing.put(truck, new ArrayList<>());
    }

    public Map<Truck, List<Action>> getOplossing() {
		return oplossing;
	}

	public void setOplossing(Map<Truck, List<Action>> oplossing) {
		this.oplossing = oplossing;
	}

	public int getAantalKm() {
		return aantalKm;
	}

	public void setAantalKm(int aantalKm) {
		this.aantalKm = aantalKm;
	}


}
