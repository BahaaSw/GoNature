package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class User implements Serializable{
	private String userId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	private UserTypeEnum userType;
	private ParkNameEnum relatedPark;
	
	public User(String userId,String username,String password,String firstName,
			String lastName,String phoneNumber,String emailAddress) {
		this.userId=userId;
		this.username=username;
		this.password=password;
		this.firstName=firstName;
		this.lastName=lastName;
		this.phoneNumber=phoneNumber;
		this.emailAddress=emailAddress;
	}
	
	public User(String userId,String username,String password) {
		this.userId=userId;
		this.username=username;
		this.password=password;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId=userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username=username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName=firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName=lastName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber=phoneNumber;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress=emailAddress;
	}
	
	public UserTypeEnum getUserType() {
		return userType;
	}
	
	public void setUserType(UserTypeEnum userType) {
		this.userType=userType;
	}
	
	public ParkNameEnum getRelatedPark() {
		return relatedPark;
	}
	
	public void setRelatedPark(ParkNameEnum relatedPark) {
		this.relatedPark=relatedPark;
	}
	
	
	
	
	
	
}
