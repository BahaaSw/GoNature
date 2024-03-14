package utils.enums;

public enum ParkNameEnum {
	
	Agamon_HaHula ("Agamon Hahula"),
	Hermon_Mountain ("Hermon Mountain"),
	Carmel_Forest ("Carmel Forest");
	
	private String name;
	
	private ParkNameEnum(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
	return name;
	}
}
