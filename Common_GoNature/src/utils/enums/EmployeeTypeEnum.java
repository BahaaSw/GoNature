package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum EmployeeTypeEnum {
	Not_Employee ("Not Employee"),
	Department_Manager ("Department Manager"),
	Park_Manager ("Park Manager"),
	Park_Employee ("Park Employee"),
	Service_Employee ("Service Employee");
	
	private static final Map<String, EmployeeTypeEnum> enumMap = new HashMap<>();

    static {
        for (EmployeeTypeEnum employeeType : EmployeeTypeEnum.values()) {
            enumMap.put(employeeType.name, employeeType);
        }
    }
	
	private String name;
	private EmployeeTypeEnum(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    public static EmployeeTypeEnum fromString(String name) {
        return enumMap.get(name);
    }
}
