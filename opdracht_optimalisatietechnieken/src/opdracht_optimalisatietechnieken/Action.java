package opdracht_optimalisatietechnieken;

public class Action {
    private boolean type; //drop = false  & collect = true
    private Location location;
    private Machine machine;

    public Action(boolean type, Location location, Machine machine) {
        this.type = type;
        this.location = location;
        this.machine = machine;
    }

    public boolean getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public Machine getMachine() {
        return machine;
    }

    public int getServiceTime() {
        return this.machine.getServiceTime();
    }

    public int getVolumeChange() {
        return this.machine.getVolume();
    }

    @Override
    public String toString() {
        return "Action{" +
                "type=" + type +
                ", location=" + location +
                ", machine=" + machine +
                '}';
    }
}
