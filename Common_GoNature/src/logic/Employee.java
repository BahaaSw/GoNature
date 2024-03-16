package logic;

import utils.enums.EmployeeTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class Employee extends User {
	ParkNameEnum relatedPark;
	EmployeeTypeEnum employeeType;
	
	public Employee(ParkNameEnum relatedPark,EmployeeTypeEnum employeeType) {
		super(UserTypeEnum.Employee);
		this.relatedPark=relatedPark;
		this.employeeType=employeeType;
	}
	
	public Employee(ParkNameEnum relatedPark,EmployeeTypeEnum employeeType,String userId,String username,String password,String firstName,
			String lastName,String phoneNumber,String emailAddress) {
		super(userId,username,password,firstName,lastName,phoneNumber,emailAddress);
		this.relatedPark=relatedPark;
		this.employeeType=employeeType;
	}
	
	public Employee() {
		super(UserTypeEnum.Employee);
	}
	
	public ParkNameEnum getRelatedPark() {
		return relatedPark;
	}
	
	public void setRelatedPark(ParkNameEnum relatedPark) {
		this.relatedPark=relatedPark;
	}
	
	public EmployeeTypeEnum getEmployeeType() {
		return employeeType;
	}
	
	public void setEmployeeType(EmployeeTypeEnum employeeType) {
		this.employeeType=employeeType;
	}
}
