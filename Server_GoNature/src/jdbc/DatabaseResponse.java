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
	Order_Status_Updated, // added
	Order_PhoneNumber_Updated, //added
	Order_Email_Updated, //added
	Order_EnterDate_Updated, //added
	Order_ExitDate_Updated, //added
	Order_Number_Of_Visitors_Updated, //added
	Order_Type_Updated, //added
	Order_Added_Into_Table, //added
	Current_Date_Is_Full,// gal - added
	Requested_Date_Is_Available, // gal - added
	Number_Of_Visitors_More_Than_Max_Capacity,
	
	// Park Section
	Park_Found_Successfully,
	Such_Park_Does_Not_Exists,
	Park_Parameter_Updated_Successfully,
	No_Pending_Request_Exists, // tamir/siso added
	Pending_Request_Pulled, // tamir/siso added
	No_Request_Exsists, // tamir/siso added
	Request_Was_Updated, // tamir/siso added
	
	//ServiceEmployee
	Guide_Was_Approved, // tamir/siso added
	
	Park_MaxCapacity_Was_Updated,
	Park_EstimatedStayTime_Was_Updated,
	Park_ReservedSpots_Was_Updated,
	Park_List_Names_Is_Created,
	Park_Table_Is_Empty,
	Park_Price_Returned_Successfully,
	
	//currentEstimatedStayTime
	Order_Details_Updated,
	Order_Paid_And_Confirmed,
	Order_Not_Confirmed,
	Order_Not_Paid,
	Order_Paid,
	
	
	// Request Section
	
	
	// Report Section
	
	

//	UserAlreadyLoggedIn,
//	UserChangedToConnected,
//	UserChangedToDisconnected,

	Added_New_User_To_Database,
	
}