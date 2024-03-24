package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderTypeEnum {
	None ("None"),
	Solo_PreOrder ("Solo Preorder"),
	Solo_Occasional("Solo Occasional"),
	Family_PreOrder ("Family Preorder"),
	Family_Occasional ("Family Occasional"),
	Group_PreOrder ("Guide Preorder"),
	Group_Occasional ("Group Occasional");
	
	
	private static final Map<String, OrderTypeEnum> enumMap = new HashMap<>();

    static {
        for (OrderTypeEnum orderStatusEnum : OrderTypeEnum.values()) {
            enumMap.put(orderStatusEnum.name, orderStatusEnum);
        }
    }
	
	private String name;
	private OrderTypeEnum(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    public static OrderTypeEnum fromString(String name) {
        return enumMap.get(name);
    }
}
