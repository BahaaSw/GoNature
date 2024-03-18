package utils.enums;

public enum RequestStatusEnum {
	
	None("None"),
	Pending ("Pending"),
	Approved ("Approved"),
	Denied ("Denied");

	private final String status;

	RequestStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}
	
	public static RequestStatusEnum fromString(String status) {
        for (RequestStatusEnum request : RequestStatusEnum.values()) {
            if (request.status.equalsIgnoreCase(status)) {
                return request;
            }
        }
        return None;
    }

}
