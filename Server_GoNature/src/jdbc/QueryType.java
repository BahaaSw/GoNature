package jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates the types of SQL queries supported by the application. Each enum constant
 * represents a distinct type of SQL operation such as INSERT, SELECT, or UPDATE.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */

public enum QueryType {
	Insert ("Insert"),
	Select ("Select"),
	Update ("Update");
	
	private static final Map<String, QueryType> enumMap = new HashMap<>();
	private String name;
	
	// Populates the enum map for quick lookup by name.
    static {
        for (QueryType queryType : QueryType.values()) {
            enumMap.put(queryType.name, queryType);
        }
    }
	
    /**
     * Constructs a new QueryType with the specified name.
     * 
     * @param name The name of the SQL query type.
     */
	private QueryType(String name) {
		this.name=name;
	}
	
    /**
     * Returns the name of the SQL query type.
     * 
     * @return A string representing the name of the query type.
     */
	@Override
	public String toString() {
		return name;
	}
	
    /**
     * Retrieves a QueryType by its name.
     * 
     * @param name The name of the query type to retrieve.
     * @return The corresponding QueryType, or null if no matching QueryType is found.
     */
    public static QueryType fromString(String name) {
        return enumMap.get(name);
    }
}
