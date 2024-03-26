package logic;

import utils.enums.EmployeeTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserStatus;
import utils.enums.UserTypeEnum;

public class Employee extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4509648140746139466L;
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
		userStatus=UserStatus.Approved;
	}
	
	public Employee(String username,String password) {
		super(username,password);
		userStatus=UserStatus.Approved;
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
