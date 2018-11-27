package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private List<Action> route;

    //default constructor
    public Route() {
        route = new ArrayList<>();

    }

    //copy constructor
    public Route(Route r){
        this.route = r.route;
    }

    public Route(List<Action> route) {
        this.route = route;
    }

    public List<Action> getRoute() {
        return route;
    }

    public void setRoute(List<Action> route) {
        this.route = route;
    }

    public void addAction(Action a) {
        this.route.add(a);
    }


}
