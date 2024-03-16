package logic;

import utils.enums.UserTypeEnum;

public class Guide extends User implements ICustomer {

	public Guide(String userId) {
		super(userId);
		userType=UserTypeEnum.Guide;
	}
	
	public Guide(String userId,String username,String password,String firstName,
			String lastName,String phoneNumber,String emailAddress) {
		super(userId,username,password,firstName,lastName,phoneNumber,emailAddress);
		userType=UserTypeEnum.Guide;
	}

	@Override
	public String getCustomerId() {
		return userId;
	}

	@Override
	public void setCustomerId(String id) {
		userId=id;
	}
	
}
