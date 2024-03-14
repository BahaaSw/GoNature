package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserTypeEnum {
	Occasional_Visitor ("Occasional Visitor"),
	Visitor ("Visitor"),
	Guide ("Guide"),
	Department_Manager ("Department Manager"),
	Park_Manager ("Park Manager"),
	Park_Employee ("Park Employee"),
	Service_Employee ("Service Employee");

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
