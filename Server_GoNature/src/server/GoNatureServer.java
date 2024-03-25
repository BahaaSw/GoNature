package server;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;

import gui.controller.ServerScreenController;
import javafx.application.Platform;
import jdbc.DBConnectionDetails;
import jdbc.QueryType;
import jdbc.query.QueryControl;
import logic.AmountDivisionReport;
import logic.CancellationsReport;
import logic.UsageReport;
import logic.ClientConnection;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.Guide;
import logic.ICustomer;
import logic.Order;
import logic.OrderInTable;
import logic.Park;
import logic.Request;
import logic.ServerResponseBackToClient;
import logic.User;
import logic.Visitor;
import logic.VisitsReport;
import jdbc.DatabaseResponse;
import jdbc.MySqlConnection;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
import utils.enums.OrderStatusEnum;
import utils.enums.ServerResponse;

/**
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

/**
 * The GoNatureServer class is the main system's server class. This class
 * extends AbstractServer ocsf class, and manage all the Client-Server design.
 */
public class GoNatureServer extends AbstractServer {

	// Use Singleton DesignPattern -> only 1 server may be running in our system.
	private static GoNatureServer server = null;
	private ServerScreenController serverController;
	private static Thread sendNotifications24HoursBefore = null;
	private static Thread cancelOrdersNotConfirmedWithin2Hours = null;
	private static Thread cancelTimePassedWaitingListOrders = null;

	/**
	 * Constructor
	 * 
	 * @param port             - The port number to connect on
	 * @param serverController - ServerGuiController to send information to the
	 *                         server gui view.
	 * @param dbConn           - The Connection instance to the database.
	 */
	private GoNatureServer(int port, ServerScreenController serverController) {
		super(port);
		this.serverController = serverController;
		initializeThreadsAndStartRun();
	}

	/**
	 * This method handle the message from client and sends them to the correct
	 * method according to the sent Object instance.
	 * 
	 * @param msg    - The Object instance the client sent to the server.
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// get the ip of the client who have sent the message.
		String clientIp = client.getInetAddress().getHostAddress();
		ClientRequestDataContainer data = (ClientRequestDataContainer) msg;
		ClientRequest request = data.getRequest();
		ServerResponseBackToClient response = null;
		switch (request) {
		case Login_As_Employee:
			response = handleLoginAsEmployee(data, client);
			if (response.getRensponse() == ServerResponse.User_Already_Connected
					|| response.getRensponse() == ServerResponse.User_Does_Not_Found
					|| response.getRensponse() == ServerResponse.Password_Incorrect)
				break;
			serverController.addToConnected(client, ((Employee) response.getMessage()).getUsername());
			break;

		case Login_As_Guide:
			response = handleLoginAsGuide(data, client);
			if (response.getRensponse() == ServerResponse.User_Already_Connected
					|| response.getRensponse() == ServerResponse.Guide_Status_Pending
					|| response.getRensponse() == ServerResponse.Password_Incorrect)
				break;
			serverController.addToConnected(client, ((Guide) response.getMessage()).getUsername());
			break;

		case Login_As_Visitor:
			response = handleLoginAsVisitor(data, client);
			if (response.getRensponse() == ServerResponse.User_Already_Connected
					|| response.getRensponse() == ServerResponse.Visitor_Have_No_Orders_Yet)
				break;
			serverController.addToConnected(client, "Visitor " + ((Visitor) response.getMessage()).getCustomerId());
			break;

		case Search_For_Relevant_Order:
			response = handleSearchForRelevantOrder(data, client);
			break;

		case Add_New_Order_If_Available:
			response = handleAddNewOrderIfAvailable(data, client);
			break;

		case Insert_New_Order_As_Wait_Notify:
			response = handleInsertNewOrderAsWaitNotify(data, client);
			break;

		case Search_For_Available_Date:
			response = handleSearchForAvailableDates(data, client);
			break;

		case Update_Order_Status_Canceled:
			response = handleUpdateOrderStatusCanceled(data, client);
			break;

		// Service Employee Section
		case Update_Guide_As_Approved:
			response = handleUpdateGuideAsApproved(data, client);
			break;
		case Search_For_Guides_Status_Pending:
			response = handleSearchForGuidesWithStatusPending(data, client);
			break;

		// Park Section
		case Search_For_Specific_Park:
			response = handleSearchForSpecificPark(data, client);
			break;

		case Update_Order_Status_Completed:
			response = handleUpdateOrderStatusCompleted(data, client);
			break;

		case Update_Order_Status_Time_Passed:
			response = handleUpdateOrderStatusTimePassed(data, client);
			break;

		case Update_Order_Status_In_Park:
			response = handleUpdateOrderStatusInPark(data, client);
			break;

		case Prepare_New_Occasional_Order:
			response = handlePrepareNewOccasionalOrder(data,client);
			break;
			
		case Add_Occasional_Visit_As_In_Park:
			response = handleAddOccasionalVisitAsInPark(data,client);
			break;
		// Requests Section
		case Make_New_Park_Estimated_Visit_Time_Request:
			response = handleMakeNewParkEstimatedVisitTimeRequest(data, client);
			break;
		case Make_New_Park_Reserved_Entries_Request:
			response = handleMakeNewParkReservedEntriesRequest(data, client);
			break;
		case Make_New_Park_Capacity_Request:
			response = handleMakeNewParkCapacityRequest(data, client);
			break;
		case Import_All_Pending_Requests:
			response = handleImportAllPendingRequests(data, client);
			break;
		case Update_Request_In_Database:
			response = handleUpdateRequestInDatabase(data, client);
			break;

		// Reports Section
		case Create_Visits_Report:
			response = handleCreateVisitsReport(data, client);
			break;
		case Import_Visits_Report:
			response = handleImportVisitsReport(data, client);
			break;

		case Create_Cancellations_Report:
			response = handleCreateCancellationsReport(data, client);
			break;

		case Import_Cancellations_Report:
			response = handleImportCancellationsReport(data, client);
			break;

		case Create_Usage_Report:
			response = handleCreateUsageReport(data, client);
			break;
		case Import_Usage_Report:
			response = handleImportUsageReport(data, client);
			break;

		case Import_All_Orders_For_Now:
			response = handleImportAllOrdersForNow(data, client);
			break;

		case Show_Payment_At_Entrance:
			response = handleShowPaymentAtEntrance(data, client);
			break;
		// added by nadav
		case Create_Total_Visitors_Report:
			response = handleCreateTotalAmountDivisionReport(data, client);
			break;

		case Import_Total_Visitors_Report:
			response = handleImportTotalAmountDivisionReport(data, client);
			break;

		case Search_For_Notified_Orders:
			response = handleSearchForNotifiedOrders(data, client);
			break;

		case Logout:
			handleUserLogoutFromApplication(data.getData(), client, clientIp);
			break;

		default:
			// TODO: implement all relevant actions
			break;
		}

		try {
			client.sendToClient(response);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ServerResponseBackToClient handlePrepareNewOccasionalOrder(ClientRequestDataContainer data,ConnectionToClient client) {
		Order order = (Order)data.getData();
		Park requestedPark = new Park(order.getParkName().getParkId());
		ServerResponseBackToClient response;
		boolean foundPark  = QueryControl.parkQueries.getParkById(requestedPark);
		if(foundPark) {
			double estimatedVisitTime = requestedPark.getCurrentEstimatedStayTime();
			// Split the estimatedVisitTime into whole days and fractional day components
			long estimatedVisitTimeInHours = (long) (estimatedVisitTime);
			
			LocalDateTime enterTime = order.getEnterDate();
			LocalDateTime exitTime = enterTime.plusHours(estimatedVisitTimeInHours);
			order.setExitDate(exitTime);
			order.setPrice(requestedPark.getPrice());
			response = new ServerResponseBackToClient(ServerResponse.Occasional_Visit_Order_Ready, order);
		}
		else
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed,order);
		return response;
	}
	
	private ServerResponseBackToClient handleAddOccasionalVisitAsInPark(ClientRequestDataContainer data,ConnectionToClient client) {
		Order order = (Order)data.getData();
		ServerResponseBackToClient response;
		
		QueryControl.occasionalQueries.insertOccasionalOrder(order);
		response = new ServerResponseBackToClient(ServerResponse.Occasional_Visit_Added_Successfully, order);
		return response;
	}
	
	private ServerResponseBackToClient handleShowPaymentAtEntrance(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Integer orderId = (Integer) data.getData();
		Order order = new Order(orderId);
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.orderQueries.fetchOrderByOrderID(order);
		if (DbResponse == DatabaseResponse.Such_Order_Does_Not_Exists) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Not_Found, order);
		} else if (DbResponse == DatabaseResponse.Order_Found_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Found, order);
		} else {
			serverController.printToLogConsole("SQL Exception was thrown during search relevant order query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private ServerResponseBackToClient handleUpdateOrderStatusCompleted(ClientRequestDataContainer data,
			ConnectionToClient client) {
		ArrayList<Object> details = (ArrayList<Object>) data.getData();
		Integer orderId = (Integer) details.get(0);
		String orderTable = (String) details.get(1);
		ServerResponseBackToClient response;
		boolean isUpdated;
		if (orderTable.equals("Occasional")) {
			isUpdated = QueryControl.occasionalQueries.UpdateOccasionalOrderStatus(new Order(orderId),
					OrderStatusEnum.Completed);
			if (isUpdated)
				response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Successfully, null);
			else
				response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Failed, null);
		} else {
			isUpdated = QueryControl.orderQueries.updateOrderStatus(new Order(orderId), OrderStatusEnum.Completed);
			if (isUpdated)
				response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Successfully, null);
			else
				response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Failed, null);
		}
		return response;

	}

	private ServerResponseBackToClient handleUpdateOrderStatusTimePassed(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Integer orderId = (Integer) data.getData();
		ServerResponseBackToClient response;
		boolean isUpdated;
		isUpdated = QueryControl.orderQueries.updateOrderStatus(new Order(orderId), OrderStatusEnum.Time_Passed);
		if (isUpdated)
			response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Successfully, null);
		else
			response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Failed, null);
		return response;
	}

	private ServerResponseBackToClient handleUpdateOrderStatusInPark(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Integer orderId = (Integer) data.getData();
		ServerResponseBackToClient response;
		boolean isUpdated;
		isUpdated = QueryControl.orderQueries.updateOrderStatus(new Order(orderId), OrderStatusEnum.In_Park);
		if (isUpdated)
			response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Successfully, null);
		else
			response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Failed, null);
		return response;
	}

	private ServerResponseBackToClient handleImportAllOrdersForNow(ClientRequestDataContainer data,
			ConnectionToClient client) {
		int parkId = (int) data.getData();
		ServerResponseBackToClient response;
		ArrayList<Order> listOfOrders = QueryControl.orderQueries.importAllOrdersForToday(parkId);
		if (listOfOrders == null)
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		else
			response = new ServerResponseBackToClient(ServerResponse.Import_All_Orders_Successfully, listOfOrders);
		return response;

	}

	private ServerResponseBackToClient handleUpdateOrderStatusCanceled(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;
		boolean isUpdated = QueryControl.orderQueries.updateOrderStatus(order, OrderStatusEnum.Cancelled);
		if (isUpdated) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Cancelled_Successfully, order);
			// TODO: notify next in waiting list.
			notifyOrdersFromWaitingList(order.getEnterDate(), order.getParkName().getParkId());

		} else
			response = new ServerResponseBackToClient(ServerResponse.Order_Cancelled_Failed, order);

		return response;
	}

	private void notifyOrdersFromWaitingList(LocalDateTime time, int parkId) {
		ArrayList<Order> ordersFromWaitingList = QueryControl.orderQueries.notifyTheNextOrdersInWaitingList(time,
				parkId);
		if (ordersFromWaitingList == null || ordersFromWaitingList.isEmpty())
			return;

		for (Order order : ordersFromWaitingList) {
			boolean canAdd = QueryControl.orderQueries.isThisDateAvailable(parkId, time, order.getNumberOfVisitors());
			if (canAdd) {
				QueryControl.orderQueries.updateOrderStatus(order, OrderStatusEnum.Notified_Waiting_List);
				serverController.printToLogConsole(String
						.format("Order :%d, notified about available spots from waiting list", order.getOrderId()));
			}
		}

		return;
	}

	private ServerResponseBackToClient handleSearchForNotifiedOrders(ClientRequestDataContainer data,
			ConnectionToClient client) {
		String customerId = ((ICustomer) data.getData()).getCustomerId();
		ServerResponseBackToClient response;
		ArrayList<Order> notifiedOrders = QueryControl.orderQueries.searchForNotifiedOrdersOfSpecificClient(customerId);
		if (notifiedOrders == null)
			response = new ServerResponseBackToClient(ServerResponse.No_Notifications_Found, notifiedOrders);
		else
			response = new ServerResponseBackToClient(ServerResponse.Notifications_Found, notifiedOrders);
		return response;

	}

	private ServerResponseBackToClient handleInsertNewOrderAsWaitNotify(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;
		boolean addedToDatabase = QueryControl.orderQueries.insertOrderIntoDB(order);
		if (addedToDatabase)
			response = new ServerResponseBackToClient(ServerResponse.Order_Added_Successfully, order);
		else
			response = new ServerResponseBackToClient(ServerResponse.Order_Added_Failed, order);
		return response;
	}

	private ServerResponseBackToClient handleSearchForSpecificPark(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Park park = (Park) data.getData();
		ServerResponseBackToClient response;
		boolean foundPark = QueryControl.parkQueries.getParkById(park);
		if (foundPark)
			response = new ServerResponseBackToClient(ServerResponse.Fetched_Park_Details_Successfully, park);
		else
			response = new ServerResponseBackToClient(ServerResponse.Fetched_Park_Details_Failed, park);
		return response;

	}

	private ServerResponseBackToClient handleMakeNewParkEstimatedVisitTimeRequest(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Request request = (Request) data.getData();
		ServerResponseBackToClient response;
		boolean success = QueryControl.requestsQueries.InsertNewRequest(request);
		if (success)
			response = new ServerResponseBackToClient(ServerResponse.Request_Sent_To_Department_Successfully, request);
		else
			response = new ServerResponseBackToClient(ServerResponse.Last_Request_With_Same_Type_Still_Pending,
					request);
		return response;
	}

	private ServerResponseBackToClient handleImportAllPendingRequests(ClientRequestDataContainer data,
			ConnectionToClient client) {
		ArrayList<Request> requestList = (ArrayList<Request>) data.getData();
		ServerResponseBackToClient response = null;
		DatabaseResponse dbResponse = QueryControl.requestsQueries.ShowAllParkManagerRequests(requestList);
		switch (dbResponse) {
		case No_Pending_Request_Exists:
			response = new ServerResponseBackToClient(ServerResponse.There_Are_Not_Pending_Requests, requestList);
			break;
		case Pending_Request_Pulled:
			response = new ServerResponseBackToClient(ServerResponse.Pending_Requests_Found_Successfully, requestList);
			break;
		}
		return response;

	}

	private ServerResponseBackToClient handleUpdateRequestInDatabase(ClientRequestDataContainer data,
			ConnectionToClient client) {
		ArrayList<Request> requestList = (ArrayList<Request>) data.getData();
		DatabaseResponse dbResponse;
		for (int i = 0; i < requestList.size(); i++) {
			dbResponse = QueryControl.requestsQueries.UpdateStatusRequest(requestList.get(i),
					requestList.get(i).getRequestStatus().name());
			if (dbResponse == DatabaseResponse.No_Request_Exists || dbResponse == DatabaseResponse.Failed)
				return new ServerResponseBackToClient(ServerResponse.Updated_Requests_Failed, null);
		}
		return new ServerResponseBackToClient(ServerResponse.Updated_Requests_Successfully, null);
	}

	private ServerResponseBackToClient handleMakeNewParkReservedEntriesRequest(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Request request = (Request) data.getData();
		ServerResponseBackToClient response;
		boolean success = QueryControl.requestsQueries.InsertNewRequest(request);
		if (success)
			response = new ServerResponseBackToClient(ServerResponse.Request_Sent_To_Department_Successfully, request);
		else
			response = new ServerResponseBackToClient(ServerResponse.Last_Request_With_Same_Type_Still_Pending,
					request);
		return response;
	}

	private ServerResponseBackToClient handleMakeNewParkCapacityRequest(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Request request = (Request) data.getData();
		ServerResponseBackToClient response;
		boolean success = QueryControl.requestsQueries.InsertNewRequest(request);
		if (success)
			response = new ServerResponseBackToClient(ServerResponse.Request_Sent_To_Department_Successfully, request);
		else
			response = new ServerResponseBackToClient(ServerResponse.Last_Request_With_Same_Type_Still_Pending,
					request);
		return response;
	}

	// added by nadav
	private ServerResponseBackToClient handleCreateTotalAmountDivisionReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		AmountDivisionReport report = (AmountDivisionReport) data.getData();
		ServerResponseBackToClient response;
		boolean result = QueryControl.reportsQueries.generateTotalAmountDivisionReport(report);
		if (result)
			response = new ServerResponseBackToClient(ServerResponse.Report_Generated_Successfully, report);
		else
			response = new ServerResponseBackToClient(ServerResponse.Report_Failed_Generate, report);

		return response;
	}

	// TODO change
	private ServerResponseBackToClient handleImportTotalAmountDivisionReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		AmountDivisionReport report = (AmountDivisionReport) data.getData();
		ServerResponseBackToClient response;
		byte[] blobInBytes = QueryControl.reportsQueries.getRequestedTotalAmountReport(report);
		if (blobInBytes == null)
			response = new ServerResponseBackToClient(ServerResponse.Such_Report_Not_Found, blobInBytes);
		else {
			response = new ServerResponseBackToClient(ServerResponse.Cancellations_Report_Found, blobInBytes);
		}
		return response;
	}

	private ServerResponseBackToClient handleImportVisitsReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		VisitsReport report = (VisitsReport) data.getData();
		ServerResponseBackToClient response;
		byte[] blobInBytes = QueryControl.reportsQueries.getRequestedVisitsReport(report);
		if (blobInBytes == null)
			response = new ServerResponseBackToClient(ServerResponse.Such_Report_Not_Found, blobInBytes);
		else {
			response = new ServerResponseBackToClient(ServerResponse.Cancellations_Report_Found, blobInBytes);
		}
		return response;
	}

	private ServerResponseBackToClient handleCreateVisitsReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		VisitsReport report = (VisitsReport) data.getData();
		ServerResponseBackToClient response;
		boolean result = QueryControl.reportsQueries.generateVisitsReport(report);
		if (result)
			response = new ServerResponseBackToClient(ServerResponse.Report_Generated_Successfully, report);
		else
			response = new ServerResponseBackToClient(ServerResponse.Report_Failed_Generate, report);

		return response;
	}

	private ServerResponseBackToClient handleCreateCancellationsReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		CancellationsReport report = (CancellationsReport) data.getData();
		ServerResponseBackToClient response;
		boolean result = QueryControl.reportsQueries.generateCancellationsReport(report);
		if (result)
			response = new ServerResponseBackToClient(ServerResponse.Report_Generated_Successfully, report);
		else
			response = new ServerResponseBackToClient(ServerResponse.Report_Failed_Generate, report);

		return response;
	}

	private ServerResponseBackToClient handleImportCancellationsReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		CancellationsReport report = (CancellationsReport) data.getData();
		ServerResponseBackToClient response;
		byte[] blobInBytes = QueryControl.reportsQueries.getRequestedCancellationsReport(report);
		if (blobInBytes == null)
			response = new ServerResponseBackToClient(ServerResponse.Such_Report_Not_Found, blobInBytes);
		else {
			response = new ServerResponseBackToClient(ServerResponse.Cancellations_Report_Found, blobInBytes);
		}
		return response;
	}

	/// Added by siso tamir and nadav

	private ServerResponseBackToClient handleCreateUsageReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		UsageReport report = (UsageReport) data.getData();
		ServerResponseBackToClient response;
		boolean result = QueryControl.reportsQueries.generateUsageReport(report);
		if (result)
			response = new ServerResponseBackToClient(ServerResponse.Report_Generated_Successfully, report);
		else
			response = new ServerResponseBackToClient(ServerResponse.Report_Failed_Generate, report);

		return response;
	}

	private ServerResponseBackToClient handleImportUsageReport(ClientRequestDataContainer data,
			ConnectionToClient client) {
		UsageReport report = (UsageReport) data.getData();
		ServerResponseBackToClient response;
		byte[] blobInBytes = QueryControl.reportsQueries.getRequestedUsageReport(report);
		if (blobInBytes == null)
			response = new ServerResponseBackToClient(ServerResponse.Such_Report_Not_Found, blobInBytes);
		else {
			response = new ServerResponseBackToClient(ServerResponse.Cancellations_Report_Found, blobInBytes);
		}
		return response;
	}

	private ServerResponseBackToClient handleSearchForGuidesWithStatusPending(ClientRequestDataContainer data,
			ConnectionToClient client) {
		ArrayList<Guide> guidesList = (ArrayList<Guide>) data.getData();
		ServerResponseBackToClient response = null;
		DatabaseResponse dbResponse = QueryControl.employeeQueries.ShowAllGuidesWithPendingStatus(guidesList);
		switch (dbResponse) {
		case No_Pending_Request_Exists:
			response = new ServerResponseBackToClient(ServerResponse.Guides_With_Status_Pending_Not_Found, guidesList);
			break;
		case Pending_Request_Pulled:
			response = new ServerResponseBackToClient(ServerResponse.Guides_With_Status_Pending_Found, guidesList);
			break;
		}
		return response;

	}

	private ServerResponseBackToClient handleUpdateGuideAsApproved(ClientRequestDataContainer data,
			ConnectionToClient client) {
		ArrayList<Guide> guidesList = (ArrayList<Guide>) data.getData();
		DatabaseResponse dbResponse;
		for (int i = 0; i < guidesList.size(); i++) {
			dbResponse = QueryControl.employeeQueries.UpdateGuideStatusToApprove(guidesList.get(i));
			if (dbResponse == DatabaseResponse.Failed)
				return new ServerResponseBackToClient(ServerResponse.Updated_Guides_To_Approved_Failed, null);
		}
		return new ServerResponseBackToClient(ServerResponse.Updated_Guides_To_Approved_Successfully, null);
	}

	private ServerResponseBackToClient handleSearchForAvailableDates(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;
		ArrayList<LocalDateTime> availableDates = QueryControl.orderQueries.searchForAvailableDates7DaysForward(order);
		return new ServerResponseBackToClient(null, availableDates);
	}

	private ServerResponseBackToClient handleAddNewOrderIfAvailable(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.orderQueries.checkIfNewOrderAvailableAtRequestedDate(order);
		switch (DbResponse) {
		case Such_Park_Does_Not_Exists:
		case Failed:
			return null;
		case Current_Date_Is_Full:
			response = new ServerResponseBackToClient(ServerResponse.Requested_Order_Date_Unavaliable, order);
			break;
		case Requested_Date_Is_Available:
			response = new ServerResponseBackToClient(ServerResponse.Requested_Order_Date_Is_Available, order);
			break;
		case Number_Of_Visitors_More_Than_Max_Capacity:
			response = new ServerResponseBackToClient(ServerResponse.Too_Many_Visitors, order);
			break;
		default:
			return null;
		}
		return response;
	}

	private ServerResponseBackToClient handleLoginAsEmployee(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Employee employee = (Employee) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.employeeQueries.searchForApprovedEmployee(employee);
		if (DbResponse == DatabaseResponse.Such_Employee_Not_Found) {
			response = new ServerResponseBackToClient(ServerResponse.User_Does_Not_Found, employee);

		} else if (DbResponse == DatabaseResponse.Password_Incorrect) {
			response = new ServerResponseBackToClient(ServerResponse.Password_Incorrect, employee);

		} else if (DbResponse == DatabaseResponse.Employee_Connected_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Employee_Connected_Successfully, employee);

			for (ClientConnection cl : serverController.getClientsList()) {
				if (cl.isAlreadyConnected(new ClientConnection(employee.getUsername(), client))) {
					response.setRensponse(ServerResponse.User_Already_Connected);
					break;
				}
			}
		} else {
			serverController.printToLogConsole("SQL Exception was thrown during search for approved employee query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private ServerResponseBackToClient handleLoginAsGuide(ClientRequestDataContainer data, ConnectionToClient client) {
		Guide guide = (Guide) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.customerQueries.searchForApprovedGuide(guide);
		if (DbResponse == DatabaseResponse.Such_Guide_Not_Found) {
			response = new ServerResponseBackToClient(ServerResponse.User_Does_Not_Found, guide);

		} else if (DbResponse == DatabaseResponse.Password_Incorrect) {
			response = new ServerResponseBackToClient(ServerResponse.Password_Incorrect, guide);

		} else if (DbResponse == DatabaseResponse.Guide_Connected_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Guide_Connected_Successfully, guide);
			for (ClientConnection cl : serverController.getClientsList()) {
				if (cl.isAlreadyConnected(new ClientConnection(guide.getUsername(), client))) {
					response.setRensponse(ServerResponse.User_Already_Connected);
					break;
				}
			}

		} else if (DbResponse == DatabaseResponse.Guide_Not_Approve_Yet) {
			response = new ServerResponseBackToClient(ServerResponse.Guide_Status_Pending, guide);
		}

		else {
			serverController.printToLogConsole("SQL Exception was thrown during search for approved guide query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private ServerResponseBackToClient handleLoginAsVisitor(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Visitor visitor = (Visitor) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.customerQueries.searchAccessForVisitor(visitor);
		if (DbResponse == DatabaseResponse.Doesnt_Have_Active_Order) {
			response = new ServerResponseBackToClient(ServerResponse.Visitor_Have_No_Orders_Yet, visitor);

		} else if (DbResponse == DatabaseResponse.Visitor_Connected_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Visitor_Connected_Successfully, visitor);
			for (ClientConnection cl : serverController.getClientsList()) {
				if (cl.isAlreadyConnected(new ClientConnection("Visitor " + visitor.getCustomerId(), client))) {
					response.setRensponse(ServerResponse.User_Already_Connected);
					break;
				}
			}
		} else {
			serverController.printToLogConsole("SQL Exception was thrown during search for login visitor query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private ServerResponseBackToClient handleSearchForRelevantOrder(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;
		DatabaseResponse DbResponse = QueryControl.orderQueries.fetchOrderByOrderID(order);
		if (DbResponse == DatabaseResponse.Such_Order_Does_Not_Exists) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Not_Found, order);
		} else if (DbResponse == DatabaseResponse.Order_Found_Successfully) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Found, order);
		} else {
			serverController.printToLogConsole("SQL Exception was thrown during search relevant order query");
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		}
		return response;
	}

	private void handleUserLogoutFromApplication(Object user, ConnectionToClient client, String clientIp) {
		try {
			String id = "";
			if (user instanceof Visitor) {
				id = "Visitor" + ((Visitor) user).getCustomerId();

			} else if (user instanceof User) {
				id = ((User) user).getUsername();
			}
			serverController.printToLogConsole(
					String.format("User : '%s' with IP : '%s' : Request Logout from Application", id, clientIp));
			serverController.getClientsList().remove(new ClientConnection("", client));
			serverController.printToLogConsole(
					String.format("User : '%s' with IP : '%s' : Logged Out Successfully", id, clientIp));
			client.sendToClient(new ServerResponseBackToClient(ServerResponse.User_Logout_Successfully, null));
		} catch (IOException ex) {
			serverController.printToLogConsole("Error while sending update message to client");
			return;
		}
	}

	/**
	 * This method handle a specific String message from client for now relevant
	 * only for message when client disconnecting the server.
	 * 
	 * @param msg    - The string message the client sent to the server.
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	private void handleDisconnectMsgFromClient(ConnectionToClient client) {
		clientDisconnected(client);
		return;
	}

	/**
	 * This method write to log screen the server has been started.
	 */
	@Override
	protected void serverStarted() {
		serverController.printToLogConsole(
				String.format("Server listening for connnections on address %s:%s", getServerIpAddress(), getPort()));
	}

	/**
	 * The method enumerate through the network interface eliminate local host and
	 * virtual machines to return the right IP;
	 * 
	 * @return a String containing the correct IP
	 */
	private String getServerIpAddress() {
		String ip;
		try {
			@SuppressWarnings("rawtypes")
			Enumeration e1 = NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface network = (NetworkInterface) e1.nextElement();
				@SuppressWarnings("rawtypes")
				Enumeration e2 = network.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress) e2.nextElement();
					ip = inetAddress.getHostAddress();
					if (ip.contains(".") && !ip.equals("127.0.0.1")
							&& !network.getDisplayName().toLowerCase().contains("virtual")) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return "Not found network addresses. please use ipconfig in commandline";
	}

	/**
	 * This method write to log screen the server has been stopped.
	 */
	@Override
	protected void serverStopped() {
		serverController.printToLogConsole("Server has stopped listening for connections\n");
	}

	/**
	 * This method write to log screen the server has been stopped.
	 */
	@Override
	protected void serverClosed() {
		serverController.printToLogConsole("Server has been closed\n");
	}

	/**
	 * This method called when new client connect to the server. add the client to
	 * the server table view and write to log.
	 * 
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
		String test = client.toString();
		for (ClientConnection cl : serverController.getClientsList()) {
			if (cl.getConnection().equals(client)) {
				return;
			}
		}
		serverController.printToLogConsole(
				"Client " + details.getHostName() + " with IP:" + details.getHostAddress() + " Connected");
	}

	/**
	 * This method called when a client disconnected from the server. remove the
	 * client from the server's table view and write to log.
	 * 
	 * @param client - The ConnectionToClient instance which include the details of
	 *               the client in order to be able send him back answer.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		InetAddress details = client.getInetAddress();
		serverController.printToLogConsole(
				"Client " + details.getHostName() + " with IP:" + details.getHostAddress() + " Disconnected");
		serverController.removeFromConnected(client);
	}

	/**
	 * This method stop the server to listen on a specific port and close the
	 * server.
	 */
	public static void stopServer() {
		// there is no server yet
		if (server == null)
			return;

		try {
			// first tell all the clients to disconnect.
			closeAllThreads();
			server.sendToAllClients(new ServerResponseBackToClient(ServerResponse.Server_Disconnected, ""));
			server.stopListening();
			server.close();

		} catch (IOException ex) {
			System.out.println("Error while closing server");
			ex.printStackTrace();
		} finally {
			MySqlConnection.getInstance().closeConnection();
			server = null;
		}
	}

	private static void closeAllThreads() {
//		private static Thread sendNotifications24HoursBefore = null;
//		private static Thread cancelOrdersNotConfirmedWithin2Hours = null;
		// Stop the searchAvailableDates thread if it's running
		try {
			if (sendNotifications24HoursBefore != null && sendNotifications24HoursBefore.isAlive())
				sendNotifications24HoursBefore.interrupt();

			if (cancelOrdersNotConfirmedWithin2Hours != null && cancelOrdersNotConfirmedWithin2Hours.isAlive())
				cancelOrdersNotConfirmedWithin2Hours.interrupt();

			sendNotifications24HoursBefore.join(); // Wait for the thread to stop
			cancelOrdersNotConfirmedWithin2Hours.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}

	/**
	 * This method start the server to listen on a specific port and connect to
	 * database via database controller.
	 * 
	 * @param db               - The DBConnectionDetails entity which contains all
	 *                         required data to connect the database.
	 * @param port             - The port number to connect on
	 * @param serverController - ServerGuiController to send information to the
	 *                         server gui view.
	 */
	public static void startServer(DBConnectionDetails db, Integer port, ServerScreenController serverController) {
		// try to connect the database
		MySqlConnection.setDBConnectionDetails(db);
		Connection conn = (Connection) MySqlConnection.getInstance().getConnection();
		// if failed -> can't start the server.
		if (conn == null) {
			serverController.printToLogConsole("Can't start server! Connection to database failed!");
			return;
		}

		serverController.printToLogConsole("Connection to database succeed");

		// Singleton DesignPattern. Only 1 instance of server is available.
		if (server != null) {
			serverController.printToLogConsole("There is already a connected server");
			return;
		}

		server = new GoNatureServer(port, serverController);

		try {
			server.listen();
			// update connection in server gui.
			serverController.connectionSuccessfull();
			// Run the 2 relevant threads
			sendNotifications24HoursBefore.start();
			cancelOrdersNotConfirmedWithin2Hours.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			serverController.printToLogConsole("Error - could not listen for clients!");
//			stopServer();
			server = null;
		}
	}

	private void initializeThreadsAndStartRun() {
		if (sendNotifications24HoursBefore != null && sendNotifications24HoursBefore.isAlive()) {
			sendNotifications24HoursBefore.interrupt();
			try {
				sendNotifications24HoursBefore.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (cancelOrdersNotConfirmedWithin2Hours != null && cancelOrdersNotConfirmedWithin2Hours.isAlive()) {
			cancelOrdersNotConfirmedWithin2Hours.interrupt();
			try {
				cancelOrdersNotConfirmedWithin2Hours.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		if (cancelTimePassedWaitingListOrders != null && cancelTimePassedWaitingListOrders.isAlive()) {
			cancelTimePassedWaitingListOrders.interrupt();
			try {
				cancelTimePassedWaitingListOrders.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		sendNotifications24HoursBefore = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
					LocalDateTime currentTime = LocalDateTime.now();
					LocalDateTime relevantTimeTomorrow = currentTime.plusDays(1);
					ArrayList<Order> ordersToNotify = QueryControl.notificationQueries
							.CheckAllOrdersAndChangeToNotifedfNeeded(relevantTimeTomorrow);

					if (ordersToNotify != null && !ordersToNotify.isEmpty()) {
						Platform.runLater(() -> serverController
								.printToLogConsole("Confirmation Notification was sent to all order 24 before time"));
						for (Order order : ordersToNotify) {
							QueryControl.notificationQueries.UpdateAllWaitNotifyOrdersToNotify(order);
							String message = String.format(
									"Order: %d, Notification was sent by mail to %s and sms to %s", order.getOrderId(),
									order.getEmail(), order.getTelephoneNumber());
							Platform.runLater(() -> serverController.printToLogConsole(message));
						}
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});

		cancelOrdersNotConfirmedWithin2Hours = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
					LocalDateTime currentTime = LocalDateTime.now();
					LocalDateTime relevantTimeTomorrowMinus2Hours = currentTime.plusHours(22);
					ArrayList<Order> ordersToNotify = QueryControl.notificationQueries
							.CheckAllOrdersAndChangeToCancelledIfNeeded(relevantTimeTomorrowMinus2Hours);

					if (ordersToNotify != null && !ordersToNotify.isEmpty()) {
						for (Order order : ordersToNotify) {
							QueryControl.notificationQueries.automaticallyCancelAllNotifiedOrders(order);
							Platform.runLater(() -> serverController.printToLogConsole("Notification On Cancel Sent"));
						}
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});

//		cancelTimePassedWaitingListOrders = new Thread(()->{
//			while (!Thread.interrupted()) {
//				try {
//					Thread.sleep(1000);
//					LocalDateTime currentTime = LocalDateTime.now();
//					ArrayList<Order> ordersToNotify = QueryControl.notificationQueries
//							.CheckWaitingListAndRemoveAllIrrelcantOrders(currentTime);
//
//					if (ordersToNotify != null && !ordersToNotify.isEmpty()) {
//						for (Order order : ordersToNotify) {
//							QueryControl.notificationQueries.automaticallyCancelAllNotifiedOrders(order);
//							Platform.runLater(() -> serverController.printToLogConsole("Notification On Cancel Sent"));
//						}
//					}
//
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//					break;
//				}
//			}
//		});
	}
}
