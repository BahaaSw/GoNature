package utils.enums;

public enum UserType {
	Occasional_Visitor ("Occasional Visitor"),
	Visitor ("Visitor"),
	Guide ("Guide"),
	Department_Manager ("Department Manager"),
	Park_Manager ("Park Manager"),
	Park_Employee ("Park Employee"),
	Service_Employee ("Service Employee");
	
	private String name;
	private UserType(String name) {
		this.name=name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
