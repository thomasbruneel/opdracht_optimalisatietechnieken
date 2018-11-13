package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public List<Machine> calculatAvailableMachines(List<Collect> tempCollect, Map<Machine, Depot> depotInventory) {
        List<Machine> oplossing = new ArrayList<>();
        tempCollect = tempCollect.stream().filter(p -> p.getMachine().getMachineType() == this.machineType).collect(Collectors.toList());
        for(Collect c : tempCollect) oplossing.add(c.getMachine());
        for(Machine m : depotInventory.keySet()) if(m.getMachineType() == this.machineType) oplossing.add(m);
        return oplossing;
    }
}
