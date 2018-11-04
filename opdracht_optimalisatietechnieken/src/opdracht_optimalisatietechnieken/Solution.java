package opdracht_optimalisatietechnieken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    Map<Integer, List<Location>> oplossing;
    int AantalKm;

    public Solution() {
        oplossing = new HashMap<>();
    }

    public Map<Integer, List<Location>> getOplossing() {
        return oplossing;
    }

    public void setOplossing(Map<Integer, List<Location>> oplossing) {
        this.oplossing = oplossing;
    }

    public int getAantalKm() {
        return AantalKm;
    }

    public void setAantalKm(int aantalKm) {
        AantalKm = aantalKm;
    }
}
