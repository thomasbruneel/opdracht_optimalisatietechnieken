package opdracht_optimalisatietechnieken;

public class MachineType {

    private int id;
    private int volume;
    private int serviceTime;
    private String typeName;

    public MachineType(int id, int volume, int serviceTime, String typeName) {
        this.id = id;
        this.volume = volume;
        this.serviceTime = serviceTime;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "Machine-type met id: " + id + " met een volume van: " + volume + ", servicetijd van:" + serviceTime
                + " en typenaam : " + typeName;
    }


}
