package logic;

import java.io.Serializable;

import utils.enums.UserTypeEnum;

public class ExternalUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5371858634046221075L;
	protected UserTypeEnum userType = UserTypeEnum.ExternalUser;
	
	public ExternalUser() {}
	
	public UserTypeEnum getUserType() {
		return userType;
	}
	
	public void setUserType(UserTypeEnum type) {
		this.userType=type;
	}
	
}
