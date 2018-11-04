package opdracht_optimalisatietechnieken;

public class Drop {
    private int id;
    private MachineType machineType;
    private Location location;

    public Drop(int id, MachineType machineType, Location location) {
        this.id = id;
        this.machineType = machineType;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Drop request met id " + id + "voor een machine met type (gegevens: " + machineType + ")\n" +
                "op locatie met gegevens: (" + location + ")";
    }


}
