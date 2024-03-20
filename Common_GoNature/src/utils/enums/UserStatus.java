package utils.enums;

public enum UserStatus {
	
	
    None("None"), // None - for visitors they aren't users in the system
    Pending("Pending"), // Pending - for guides before service employee approved them
    Approved("Approved"), // Approved - for employees and guides after service employee approved them
    Denied("Denied"); // Denied - for guides after service employee deny their register

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserStatus fromString(String text) {
        for (UserStatus status : UserStatus.values()) {
            if (status.value.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return None;
    }
}
