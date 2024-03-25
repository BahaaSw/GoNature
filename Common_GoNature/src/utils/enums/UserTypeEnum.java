package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserTypeEnum {
	ExternalUser ("External User"),
	Occasional ("Occasional"),
	Employee ("Employee"),
	Visitor ("Visitor"),
	Guide ("Guide");

	private static final Map<String, UserTypeEnum> enumMap = new HashMap<>();

    static {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            enumMap.put(userTypeEnum.name, userTypeEnum);
        }
    }
	
	private String name;
	private UserTypeEnum(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    public static UserTypeEnum fromString(String name) {
        return enumMap.get(name);
    }
	

}
