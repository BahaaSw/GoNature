package jdbc;

/**
 * Enumerates possible responses from database operations, encompassing general outcomes,
 * login-related responses, order and park operations, guide approval processes, and more.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */
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
	Order_Status_Updated,
	Order_PhoneNumber_Updated,
	Order_Email_Updated,
	Order_EnterDate_Updated,
	Order_ExitDate_Updated,
	Order_Number_Of_Visitors_Updated,
	Order_Type_Updated,
	Order_Added_Into_Table,
	Current_Date_Is_Full,
	Requested_Date_Is_Available,
	Number_Of_Visitors_More_Than_Max_Capacity,
	
	// Park Section
	Park_Found_Successfully,
	Such_Park_Does_Not_Exists,
	Park_Parameter_Updated_Successfully,
	No_Pending_Request_Exists,
	Park_Is_Full,
	Park_Is_Not_Full,
	Park_MaxCapacity_Was_Updated,
	Park_EstimatedStayTime_Was_Updated,
	Park_ReservedSpots_Was_Updated,
	Park_List_Names_Is_Created,
	Park_Table_Is_Empty,
	Park_Price_Returned_Successfully,
	Current_In_Park_Update_Failed,
	Current_In_Park_Updated_Successfully,
	Park_Reached_Full_Capacity_Updated_Successfully,
	Park_Reached_Full_Capacity_Updated_Failed,
	
	//ServiceEmployee
	Guide_Was_Approved,
	

	//currentEstimatedStayTime
	Order_Details_Updated,
	Order_Paid_And_Confirmed,
	Order_Not_Confirmed,
	Order_Not_Paid,
	Order_Paid,
	
	
	// Request Section
	Pending_Request_Pulled,
	No_Request_Exists,
	Request_Was_Updated,
	
	// Report Section
	
	

//	UserAlreadyLoggedIn,
//	UserChangedToConnected,
//	UserChangedToDisconnected,

	Added_New_User_To_Database,
	All_Guides_Approved,
}