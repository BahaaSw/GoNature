package logic;

import java.io.Serializable;

import utils.enums.ParkNameEnum;
import utils.enums.UserStatus;
import utils.enums.UserTypeEnum;

public class User extends ExternalUser implements Serializable{
	protected String userId;
	protected String username;
	protected String password;
	protected String firstName;
	protected String lastName;
	protected String phoneNumber;
	protected String emailAddress;
	protected UserStatus userStatus= UserStatus.None;
	
	public User(String userId,String username,String password,String firstName,
			String lastName,String phoneNumber,String emailAddress) {
		super();
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
	
	public User(String userId) {
		this.userId=userId;
	}
	
	
	public User(String username,String password) {
		this.username=username;
		this.password=password;
	}
	
	public User(UserTypeEnum userType) {
		this.userType=userType;
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
	
	
	public UserStatus getUserStatus() {
		return userStatus;
	}
	
	public void setUserStatus(UserStatus userStatus) {
		this.userStatus=userStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		return  this.username.equals(((User)obj).username) &&
				this.userId.equals(((User)obj).userId);
	}
	
	
	
}
