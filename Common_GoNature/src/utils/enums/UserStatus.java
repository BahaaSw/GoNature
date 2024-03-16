package utils.enums;

public enum UserStatus {
	None, // None - for visitors they aren't users in the system
	Pending, // Pending - for guides before service employee approved them
	Approved, // Approved - for employees and guides after service employee approved them
	Denied, // Denied - for guides after service employee deny their register
}
