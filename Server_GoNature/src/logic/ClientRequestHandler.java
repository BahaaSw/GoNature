package logic;

import java.time.LocalDateTime;
import java.util.ArrayList;

import gui.controller.ServerScreenController;
import javafx.application.Platform;
import jdbc.DatabaseResponse;
import jdbc.query.QueryControl;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
import utils.enums.OrderStatusEnum;
import utils.enums.ServerResponse;

public class ClientRequestHandler {

	private ServerScreenController serverController;
	public ClientRequestHandler(ServerScreenController serverController) {
		this.serverController = serverController;
	}
	
	public ServerResponseBackToClient handleRequest(ClientRequestDataContainer data,ConnectionToClient client) {
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
					|| response.getRensponse() == ServerResponse.Password_Incorrect
					|| response.getRensponse() == ServerResponse.User_Does_Not_Found)
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
		case Update_Order_Status_Confirmed:
			response = handleUpdateOrderStatusConfirmed(data,client);

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

		case Create_Total_Visitors_Report:
			response = handleCreateTotalAmountDivisionReport(data, client);
			break;

		case Import_Total_Visitors_Report:
			response = handleImportTotalAmountDivisionReport(data, client);
			break;

		case Search_For_Notified_Orders:
			response = handleSearchForNotifiedOrders(data, client);
			break;

		case Show_Payment_At_Entrance:
			response = handleShowPaymentAtEntrance(data, client);
			break;
			
		case Delete_Old_Order:
			response = handleDeleteOldOrder(data,client);
			break;

		default:
			break;
		}
		// Print to Log
		String message = String.format("Client: %s, Sent request: %s, Server Response: %s",client.getInetAddress().getHostAddress(),request,response.getRensponse());
		Platform.runLater(()->serverController.printToLogConsole(message));
		return response;
	}

	private ServerResponseBackToClient handleDeleteOldOrder(ClientRequestDataContainer data, ConnectionToClient client) {
		Order order = (Order)data.getData();
		boolean isDeleted = QueryControl.orderQueries.deleteOrderFromTable(order);
		if(isDeleted)
			return new ServerResponseBackToClient(ServerResponse.Order_Deleted_Successfully, order);
		else
			return new ServerResponseBackToClient(ServerResponse.Order_Deleted_Failed, order);
	}
	
	private ServerResponseBackToClient handlePrepareNewOccasionalOrder(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		Park requestedPark = new Park(order.getParkName().getParkId());
		ServerResponseBackToClient response;
		boolean foundPark = QueryControl.parkQueries.getParkById(requestedPark);
		if (foundPark) {
			double estimatedVisitTime = requestedPark.getCurrentEstimatedStayTime();
			// Split the estimatedVisitTime into whole days and fractional day components
			long estimatedVisitTimeInHours = (long) (estimatedVisitTime);

			LocalDateTime enterTime = order.getEnterDate();
			LocalDateTime exitTime = enterTime.plusHours(estimatedVisitTimeInHours);
			order.setExitDate(exitTime);
			order.setPrice(requestedPark.getPrice());
			response = new ServerResponseBackToClient(ServerResponse.Occasional_Visit_Order_Ready, order);
		} else
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, order);
		return response;
	}

	private ServerResponseBackToClient handleAddOccasionalVisitAsInPark(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
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
		@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unused")
	private ServerResponseBackToClient handleImportAllOrdersForNow(ClientRequestDataContainer data,
			ConnectionToClient client) {
		int parkId = (int) data.getData();
		ServerResponseBackToClient response;
		ArrayList<Order> listOfOrders = QueryControl.orderQueries.importAllOrdersForToday(parkId);
		if(listOfOrders.isEmpty())
			response = new ServerResponseBackToClient(ServerResponse.No_Orders_For_Today, listOfOrders);
		
		else if (listOfOrders == null)
			response = new ServerResponseBackToClient(ServerResponse.Query_Failed, null);
		
		else
			response = new ServerResponseBackToClient(ServerResponse.Import_All_Orders_Successfully, listOfOrders);
		
		return response;

	}

	private ServerResponseBackToClient handleUpdateOrderStatusConfirmed(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;
		boolean isUpdated = QueryControl.orderQueries.updateOrderStatus(order, OrderStatusEnum.Confirmed);
		if (isUpdated) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Successfully, order);
		} else
			response = new ServerResponseBackToClient(ServerResponse.Order_Updated_Failed, order);

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

	public void notifyOrdersFromWaitingList(LocalDateTime time, int parkId) {
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
		if (addedToDatabase) {
			response = new ServerResponseBackToClient(ServerResponse.Order_Added_Successfully, order);
			String message = String.format("Order: %d, Was created successfully, a confirmation message has been sent by email to %s and SMS to %s"
					,order.getOrderId(),order.getEmail(),order.getTelephoneNumber());
			Platform.runLater(()->serverController.printToLogConsole(message));
		}
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

	@SuppressWarnings("incomplete-switch")
	private ServerResponseBackToClient handleImportAllPendingRequests(ClientRequestDataContainer data,
			ConnectionToClient client) {
		@SuppressWarnings("unchecked")
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
		@SuppressWarnings("unchecked")
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

	@SuppressWarnings("incomplete-switch")
	private ServerResponseBackToClient handleSearchForGuidesWithStatusPending(ClientRequestDataContainer data,
			ConnectionToClient client) {
		@SuppressWarnings("unchecked")
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
		@SuppressWarnings("unchecked")
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
}
