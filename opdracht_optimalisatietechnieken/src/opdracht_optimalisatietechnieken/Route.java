package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private List<Action> route;

    //default constructor
    public Route(int[][] distanceMatrix, int[][] timeMatrix) {
        route = new ArrayList<>();
    }

    //copy constructor
    public Route(Route r) {
        this.route = r.route;
    }

    public Route(List<Action> route, int[][] distanceMatrix, int[][] timeMatrix) {
        this.route = route;
    }

    public List<Action> getRoute() {
        return route;
    }

    public void addAction(Action a) {
        this.route.add(a);
    }


}
