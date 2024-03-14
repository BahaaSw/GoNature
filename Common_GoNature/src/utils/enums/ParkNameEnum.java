package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum ParkNameEnum {
	None ("None"),
	Agamon_HaHula ("Agamon Hahula"),
	Hermon_Mountain ("Hermon Mountain"),
	Carmel_Forest ("Carmel Forest");
	
	private static final Map<String, ParkNameEnum> enumMap = new HashMap<>();

    static {
        for (ParkNameEnum parkNameEnum: ParkNameEnum.values()) {
            enumMap.put(parkNameEnum.name, parkNameEnum);
        }
    }
	
	private String name;
	
	private ParkNameEnum(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    public static ParkNameEnum fromString(String name) {
        return enumMap.get(name);
    }
}
