package logic;

import utils.enums.UserTypeEnum;

public class Visitor extends ExternalUser implements ICustomer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6861529813077334712L;
	private String visitorId;
	private Order relevantOrder;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	
	public Visitor(String visitorId,String firstName,String lastName,String phoneNumber,String emailAddress) {
		super();
		this.visitorId=visitorId;
		this.firstName=firstName;
		this.lastName=lastName;
		this.phoneNumber=phoneNumber;
		this.emailAddress=emailAddress;
		userType=UserTypeEnum.Visitor;
	}
	

	public String getCustomerId() {
		return visitorId;
	}

	public void setCustomerId(String id) {
		visitorId=id;		
	}
	
	public String getVisitorId() {
		return visitorId;
	}
	
	public void setVisitorId(String visitorId) {
		this.visitorId=visitorId;
	}
	
	public Visitor(String visitorId) {
		super();
		this.visitorId=visitorId;
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
	
	public Order getRelevantOrder() {
		return relevantOrder;
	}
	
	public void setRelevantOrder(Order order) {
		relevantOrder=order;
	}


	
}
