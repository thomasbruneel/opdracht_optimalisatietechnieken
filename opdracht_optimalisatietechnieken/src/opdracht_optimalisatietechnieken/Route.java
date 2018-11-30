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

    public void addAction(Action a) {
        this.route.add(a);
    }

    public void removeAction(Action a) {
        this.route.remove(a);
    }

    public void removeAction(int index) {
        this.route.remove(index);
    }

    public Action getAction(int index) {
        return this.getRoute().get(index);
    }
}
