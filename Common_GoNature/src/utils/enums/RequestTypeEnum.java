package utils.enums;

public enum RequestTypeEnum {
	
	None ("None"),
	Reserved_Spots_Change ("Reserved_Spots_Change"),
	Max_Visitors_In_Park_Change ("Max_Visitors_In_Park_Change"),
	Estimated_VisitTime_Change ("EstimatedVisitTime_Change"),
	CurrentInPark_Change ("CurrentInPark_Change");
	
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
	
