package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;

public class Route extends ArrayList {

    private List<Action> route;

    //default constructor
    public Route() {
        route = new ArrayList<>();
    }

    //copy constructor
    public Route(Route route) {
        this.route = route.route;
    }

    public Route(List<Action> route) {
        this.route = route;
    }

    public List<Action> getRoute() {
        return route;
    }

}
