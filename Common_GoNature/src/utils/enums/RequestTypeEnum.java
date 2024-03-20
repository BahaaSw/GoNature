package utils.enums;

public enum RequestTypeEnum {
	
	None ("None"),
	ReservedSpots ("ReservedSpots"),
	MaxCapacity("MaxCapacity"),
	EstimatedVisitTime ("EstimatedVisitTime");
	
    private final String value;

    RequestTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequestTypeEnum fromString(String text) {
        for (RequestTypeEnum request : RequestTypeEnum.values()) {
            if (request.value.equalsIgnoreCase(text)) {
                return request;
            }
        }
        return None;
    }
}
	