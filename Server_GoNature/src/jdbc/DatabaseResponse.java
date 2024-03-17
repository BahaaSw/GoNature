package jdbc;

public enum DatabaseResponse {
	Exception_Was_Thrown,
	Failed,
	Success,
	// Login Section
	User_Not_Exists,
	Password_Incorrect,
	Doesnt_Have_Active_Order,
	Visitor_Connected_Successfully,
	Such_Guide_Not_Found,
	Guide_Not_Approve_Yet,
	Guide_Connected_Successfully,
	Such_Employee_Not_Found,
	Employee_Connected_Successfully,

	
	// Order Section
	Such_Order_Does_Not_Exists,
	Order_Found_Successfully,
	
	// Park Section
	
	
	// Request Section
	
	
	// Report Section
	
	

//	UserAlreadyLoggedIn,
//	UserChangedToConnected,
//	UserChangedToDisconnected,

	Added_New_User_To_Database,
	
}
