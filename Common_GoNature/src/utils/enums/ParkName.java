package utils.enums;

public enum ParkName {
	
	Agamon_HaHula ("Agamon Hahula"),
	Hermon_Mountain ("Hermon Mountain"),
	Carmel_Forest ("Carmel Forest");
	
	private String name;
	
	private ParkName(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
	return name;
	}
}
