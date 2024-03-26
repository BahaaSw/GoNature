package jdbc;

import java.util.HashMap;
import java.util.Map;


public enum QueryType {
	Insert ("Insert"),
	Select ("Select"),
	Update ("Update");
	
	private static final Map<String, QueryType> enumMap = new HashMap<>();

    static {
        for (QueryType queryType : QueryType.values()) {
            enumMap.put(queryType.name, queryType);
        }
    }
	
	private String name;
	private QueryType(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    public static QueryType fromString(String name) {
        return enumMap.get(name);
    }
}
