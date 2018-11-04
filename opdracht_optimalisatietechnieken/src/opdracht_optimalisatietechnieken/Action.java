package opdracht_optimalisatietechnieken;

public class Action {
	private boolean type; //drop = 0  & collect = 1
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
	
	public void setType(boolean type) {
		this.type = type;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Machine getMachine() {
		return machine;
	}
	
	public void setMachine(Machine machine) {
		this.machine = machine;
	}
	
	
	@Override
	public String toString() {
		return "Action [type=" + type + ", location=" + location + ", machine=" + machine + "]";
	}
	
	

}
