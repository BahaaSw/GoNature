package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum ParkNameEnum {
	None ("None"),
	Banias ("Banias"),
	Herodium ("Herodium"),
	Masada ("Masada");
	
	private static final Map<String, ParkNameEnum> enumMap = new HashMap<>();

    static {
        for (ParkNameEnum parkNameEnum : ParkNameEnum.values()) {
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
