package utils.enums;

public enum ServerResponse {
	

	Password_Incorrect, // user found but password is incorrect
	User_Already_Connected, // user is already connected to the server
	User_Does_Not_Found, // there is no such user in database table
	
	Employee_Connected_Successfully, // when user (guide/employee) connected successfully.
	Guide_Status_Pending, // when guide is in database, but still waiting for service employee approve.
	Guide_Connected_Successfully,
	Visitor_Have_No_Orders_Yet, // did not find visitor with such ID in orders table with relevant order
	Visitor_Connected_Successfully, //did find visitor with such ID in orders table with relevant order


	Exception_Was_Thrown, // when the server catch an exception
	Server_Closed, // when server is closing itself, he should send to all clients
	Server_Disconnected, // when server is disconnected, he should send to all clients
	
	User_Logout_Successfully, // irrelevant, user can logout by it's own.
	
}
