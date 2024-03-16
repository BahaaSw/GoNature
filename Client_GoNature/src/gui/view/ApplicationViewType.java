package gui.view;

public enum ApplicationViewType {
	
	Landing_Page_Screen, // Common Landing Page : Include Connect Server and Login app.
	CustomerScreen, // Customer Screen : BorderPane load to center each relevant frame
	CustomerHomgepageScreen, // Customer Homepage : Common for all Customers
	IdenticationScreen, // Customer (Guide/Visitor) : Search for order to manage
	FinishOrderScreen, //Customer : Choose new Date or Enter waiting list.
	HandleOrderScreen, // Customer (Guide/Visitor) : Manage current order, opens after Identication Screen
	MakeOrderScreen, // Customer (Guide/Visitor/New Visitor) : Make New Order to Park.
	ConfirmMessageScreen, // Customer (Visitor/Guide/New Visitor) : Order Confirmation Message.
	
	EmployeeHomepageScreen, // Employee Homepage : Common for all employees
	EmployeeScreen, // Employee Screen : BorderPane load to center each relevant frame
	ParkAvailableSpotsScreen, // Employee (Department Manager/Park Manager/Park Employee) : See current in Park and available spots
	ParkEntranceScreen, // Park Employee : Handle Entrance To Park.HandleOccasionalVisitScreen, // Park Employee: Handle Visitor who arrived at the park
	PaymentReceiptScreen, // Park Employee : Show Receipts to Visitors
	
	ManageGuidesScreen, // Service Employee : Search for guide to register.
	AddNewGuideScreen, // Service Employee : Add New Guide.
	
	ParkSettingsScreen, // Park Manager : Change Park Settings Requests.
	CreateReportsScreen, // Employee (Department Manager/Park Manager) : Can Create Reports.
	RequestTableScreen, // Department Manager : Requests need to be approved.
	ViewReportsScreen, // Department Manager : Can view reports.
	
	
}
