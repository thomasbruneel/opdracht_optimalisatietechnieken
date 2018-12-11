package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Route {

    private List<Action> route;

    //default constructor
    public Route() {
        route = new ArrayList<>();

    }

    //copy constructor
    public Route(Route r) {
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

    public void removeAction(Action a){this.route.remove(a);}

    public Route optimizeRoute() {
        Route temproute = new Route(this);
        Route betterRoute = new Route();

        for (Action a : temproute.route) {
            if (!betterRoute.route.contains(a)) {
                List<Action> collectsSameLocation = getCollectsOnLocation(a.getLocation());
                collectsSameLocation.remove(a);
                betterRoute.addAction(a);
                if (!collectsSameLocation.isEmpty()) {
                    betterRoute.route.addAll(collectsSameLocation);
                }
            }
        }
        return betterRoute;
    }

    public List<Action> getCollectsOnLocation(Location location) {
        List<Action> collects = new ArrayList<>();
        collects = this.route.stream().filter(action -> action.getLocation() == location && action.getType()).collect(Collectors.toList());
        return collects;
    }


}
