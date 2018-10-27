package opdracht_optimalisatietechnieken;

public class Collect {
	private int id;
	private Machine machine;
	private Location location;
	public Collect(int id, Machine machine, Location location) {
		this.id = id;
		this.machine = machine;
		this.location = location;
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
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "Collect request met id " + id + "voor de machine met gegevens: (" + machine + ")\n"+
				"op locatie met gegevens: (" + location + ")"; 
	}
	
	
}
