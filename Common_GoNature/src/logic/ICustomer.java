package logic;

import utils.enums.UserTypeEnum;

public interface ICustomer {
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	UserTypeEnum getUserType();
	void setUserType(UserTypeEnum type);
	
	String getPhoneNumber();
	void setPhoneNumber(String phoneNumber);
	
	String getEmailAddress();
	void setEmailAddress(String emailAddress);
	
	String getCustomerId();
	void setCustomerId(String id);
}
