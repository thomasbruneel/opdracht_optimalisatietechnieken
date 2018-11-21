package opdracht_optimalisatietechnieken;

import java.util.ArrayList;
import java.util.List;

public class Collect {
    private int id;
    private Machine machine;

    public Collect(int id, Machine machine) {
        this.id = id;
        this.machine = machine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    //returns closest drop with same machine type
    public Drop getClosestRelatedDrop(int[][] distanceMatrix, List<Drop> dropList){
        Drop d= null;
        List<Drop> tempDrops = new ArrayList<>();
        for (Drop drop : dropList){
            if (this.getMachine().getMachineType() == drop.getMachineType()){
                tempDrops.add(drop);
            }
        }

        d = this.getMachine().getLocation().getClosestDrop(distanceMatrix, tempDrops);

        return d;
    }

    public boolean hasRelatedDrop (List<Drop> drops){
        for(Drop d: drops){
            if(this.getMachine().getMachineType() == d.getMachineType()){
                return true;
            }else{
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Collect request met id " + id + "voor de machine met gegevens: (" + machine + ")\n";
    }

}
