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

    //returns related collect from collectList for this drop.
    public Collect getClosestRelatedCollect(int[][] distanceMatrix, List<Collect> collectList) {
        Collect collect = null;
        List<Collect> tempCollects = new ArrayList<>();

<<<<<<< HEAD
        for (Collect c : collectList) {
            if (this.machineType == c.getMachine().getMachineType()) {
                tempCollects.add(c);
            }
        }

        collect = this.location.getClosestCollect(distanceMatrix, tempCollects);
        return collect;
    }

    //Returns closest available machine at depot
    public Depot getClosestMachineDepot(int[][] distanceMatrix, Map<Machine, Depot> inventory) {
        Depot depot = null;
        int distance = 50000;

        for (Map.Entry<Machine, Depot> entry : inventory.entrySet()) {
            if (entry.getKey().getMachineType() == this.getMachineType() &&
                    distanceMatrix[this.getLocation().getId()][entry.getValue().getLocation().getId()] < distance) {
                depot = entry.getValue();
            }
        }
        if(depot == null)
            System.out.println("null!");
        return depot;
=======
    public List<Machine> calculatAvailableMachines(List<Collect> tempCollect, Map<Machine, Depot> depotInventory) {
        List<Machine> oplossing = null;
        if (tempCollect != null) {
            tempCollect = tempCollect.stream().filter(p -> p.getMachine().getMachineType() == this.machineType).collect(Collectors.toList());
            if(tempCollect != null) {
                oplossing = new ArrayList<>();
                for(Collect c : tempCollect) oplossing.add(c.getMachine());
            }
        }
        if (depotInventory!=null){
            oplossing = new ArrayList<>();
            for(Machine m : depotInventory.keySet()) if(m.getMachineType() == this.machineType) oplossing.add(m);
        }
        return oplossing;
>>>>>>> e418c1c3d8233da63877cca894d45d2258ec7178
    }
}
