package logic;

import java.time.LocalDateTime;
import java.util.ArrayList;

import gui.controller.ServerScreenController;
import jdbc.DatabaseResponse;
import jdbc.query.QueryControl;
import ocsf.ConnectionToClient;
import utils.enums.ClientRequest;
import utils.enums.OrderStatusEnum;
import utils.enums.ServerResponse;

/**
 * Handles client requests sent to the server, performing various operations based on the request type.
 * Each request type is associated with a specific method that processes the request and returns a response
 * to be sent back to the client.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */

public class ClientRequestHandler {

	private ServerScreenController serverController;
	
    /**
     * Constructs a ClientRequestHandler with a reference to the ServerScreenController.
     * @param serverController the controller used to interact with the server UI and log.
     */
	public ClientRequestHandler(ServerScreenController serverController) {
		this.serverController = serverController;
	}
	
    /**
     * Processes the incoming request from a client and generates an appropriate response.
     * @param data The data container holding the request and its associated data.
     * @param client The client connection from which the request was received.
     * @return A response object to be sent back to the client.
     */
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

		case Delete_Old_Order:
			response = handleDeleteOldOrder(data,client);
			break;

		default:
			// TODO: implement all relevant actions
			break;
		}
		return response;
	}
	
    /**
     * Handles the request to delete an old order.
     * @param data The data container with information about the order to delete.
     * @param client The client requesting the order deletion.
     * @return A response indicating whether the deletion was successful or failed.
     */
	private ServerResponseBackToClient handleDeleteOldOrder(ClientRequestDataContainer data, ConnectionToClient client) {
		Order order = (Order)data.getData();
		boolean isDeleted = QueryControl.orderQueries.deleteOrderFromTable(order);
		if(isDeleted)
			return new ServerResponseBackToClient(ServerResponse.Order_Deleted_Successfully, order);
		else
			return new ServerResponseBackToClient(ServerResponse.Order_Deleted_Failed, order);
	}
	
	/**
	 * Prepares a new occasional order by setting its exit date and price based on the estimated visit time and
	 * the price of the requested park. It looks up the park associated with the order to fetch the current
	 * estimated stay time and price, calculates the exit time from the enter time and estimated stay time,
	 * and sets the order's price.
	 *
	 * @param data  The data container holding the order to be prepared. It must contain an Order object.
	 * @param client The client connection associated with this request. Not directly used in the method but
	 *               passed for consistency and potential future use.
	 * @return A ServerResponseBackToClient object indicating the outcome of the operation. If the park is found
	 *         and the order is prepared successfully, it returns a response with the status
	 *         ServerResponse.Occasional_Visit_Order_Ready and the prepared Order object. If the park is not found,
	 *         it returns a response with the status ServerResponse.Query_Failed.
	 */
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
	
	/**
	 * Adds an occasional visit as 'In Park' by inserting the order into the database. This method is responsible
	 * for handling the addition of occasional visits to the park, marking them as present within the park.
	 *
	 * @param data  The data container holding the order to be added. It must contain an Order object.
	 * @param client The client connection associated with this request. Not directly used in the method but
	 *               passed for consistency and potential future use.
	 * @return A ServerResponseBackToClient object indicating the outcome of the operation. If the occasional
	 *         visit is added successfully, it returns a response with the status
	 *         ServerResponse.Occasional_Visit_Added_Successfully and the added Order object.
	 */
	private ServerResponseBackToClient handleAddOccasionalVisitAsInPark(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ServerResponseBackToClient response;

		QueryControl.occasionalQueries.insertOccasionalOrder(order);
		response = new ServerResponseBackToClient(ServerResponse.Occasional_Visit_Added_Successfully, order);
		return response;
	}
	
	/**
	 * Handles the display of payment information at the entrance for a specific order. It fetches the order
	 * by its ID and determines if it exists and is eligible for entrance.
	 *
	 * @param data  The data container holding the order ID to be checked. It must contain an Integer object representing
	 *              the order ID.
	 * @param client The client connection associated with this request. Not directly used in the method but
	 *               passed for consistency and potential future use.
	 * @return A ServerResponseBackToClient object indicating the outcome of the operation. If the order is found
	 *         and eligible for entrance, it returns a response with the status ServerResponse.Order_Found and the
	 *         order object. If the order does not exist, it returns a response with the status
	 *         ServerResponse.Order_Not_Found. If there's an SQL exception or the query fails, it returns a response
	 *         with the status ServerResponse.Query_Failed.
	 */
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
	
	/**
	 * Updates the status of an order to 'Completed'. This method handles the completion of an order, either
	 * an occasional visit or a pre-booked order, by updating its status in the database.
	 *
	 * @param data The data container holding details necessary to update the order status. It must contain an
	 *             ArrayList of Object, where the first element is the order ID (Integer) and the second element
	 *             is the type of order ("Occasional" or otherwise).
	 * @param client The client connection associated with this request. Not directly used in the method but
	 *               passed for consistency and potential future use.
	 * @return A ServerResponseBackToClient object indicating the outcome. If the order status is successfully
	 *         updated to 'Completed', it returns a response with the status ServerResponse.Order_Updated_Successfully.
	 *         Otherwise, it returns ServerResponse.Order_Updated_Failed.
	 */
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
	
	/**
	 * Updates the status of an order to 'Time Passed'. This method is responsible for marking an order whose
	 * visit time has passed without being utilized.
	 *
	 * @param data The data container holding the order ID to update. It must contain an Integer representing the order ID.
	 * @param client The client connection associated with this request. Not directly used in the method but
	 *               passed for consistency and potential future use.
	 * @return A ServerResponseBackToClient object indicating the outcome. If the order status is successfully
	 *         updated to 'Time Passed', it returns a response with the status ServerResponse.Order_Updated_Successfully.
	 *         Otherwise, it returns ServerResponse.Order_Updated_Failed.
	 */
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
	
	/**
	 * Updates the status of an order to 'In Park'. This method handles marking an order as utilized, indicating
	 * the visitors associated with the order are currently in the park.
	 *
	 * @param data The data container holding the order ID to update. It must contain an Integer representing the order ID.
	 * @param client The client connection associated with this request. Not directly used in the method but
	 *               passed for consistency and potential future use.
	 * @return A ServerResponseBackToClient object indicating the outcome. If the order status is successfully
	 *         updated to 'In Park', it returns a response with the status ServerResponse.Order_Updated_Successfully.
	 *         Otherwise, it returns ServerResponse.Order_Updated_Failed.
	 */
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
	
	/**
	 * Imports all active orders for a specified park for the current day. This method aims to provide
	 * park managers or staff with a comprehensive view of the day's bookings, including both pre-booked
	 * and occasional visits.
	 *
	 * @param data Contains the park ID for which orders are to be imported.
	 * @param client The connection instance to the client making the request. This parameter is not used directly in the method but is essential for server-client communication.
	 * @return A {@link ServerResponseBackToClient} object containing either a list of all orders for today or an indication that the query failed.
	 */
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
	
	/**
	 * Updates the status of a specific order to 'Confirmed'. This method is typically called when a visitor confirms
	 * their intention to visit, ensuring that their booking is secured and recognized by the park's management system.
	 *
	 * @param data Contains the order to be confirmed.
	 * @param client The connection instance to the client making the request. This parameter is not directly used in the method but is essential for the server-client communication.
	 * @return A {@link ServerResponseBackToClient} object indicating the success or failure of the operation.
	 */
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
	
	/**
	 * Cancels a specific order and updates its status to 'Cancelled'. This method is typically invoked either by the visitor
	 * or automatically by the system in certain scenarios (e.g., due to non-confirmation within a given timeframe).
	 * Additionally, it triggers a check for waiting list orders that might be affected by the newly available slot.
	 *
	 * @param data Contains the order to be cancelled, including relevant details such as order ID and park ID.
	 * @param client The connection instance to the client making the request. This parameter is not used directly in the method but is essential for server-client communication.
	 * @return A {@link ServerResponseBackToClient} object indicating whether the order was successfully cancelled.
	 */
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
	
	/**
	 * Notifies the next orders in the waiting list for a specific park and time if spaces become available. It checks each order's feasibility based on the current park capacity and the number of visitors for each order. Orders that can be accommodated are updated to a notified status.
	 * 
	 * @param time The specific time to check orders for.
	 * @param parkId The ID of the park to check orders for.
	 */
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
	
	/**
	 * Handles the search for orders that have been notified. It retrieves a list of orders for a specific customer that have been marked as notified, indicating that the customer needs to confirm their order.
	 * 
	 * @param data The container holding the customer ID to search notified orders for.
	 * @param client The connection to the client, used to send the response back.
	 * @return A ServerResponseBackToClient object containing the list of notified orders or an appropriate response status.
	 */
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
	
	/**
	 * Handles the insertion of a new order as 'wait notify'. If an order cannot be immediately confirmed due to park capacity limits, it is added to the database with a status indicating it is waiting for notification of available space.
	 * 
	 * @param data The order data to be added to the database.
	 * @param client The connection to the client, used to send the response back.
	 * @return A ServerResponseBackToClient object indicating whether the order was successfully added or if the addition failed.
	 */
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
	
	/**
	 * Searches for a specific park by its ID and retrieves its details. This method is used to fetch and send park details back to the client, including capacity and estimated visit time.
	 * 
	 * @param data The data container with park information to search for.
	 * @param client The connection to the client, used to send the response back.
	 * @return A ServerResponseBackToClient object containing the park details or an error if the park cannot be found.
	 */
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
	
	/**
	 * Processes a new request for changing the estimated visit time of a park. This method is responsible for inserting a new request into the database to update a park's estimated visit time.
	 * 
	 * @param data The request data, including the new estimated visit time and the park ID.
	 * @param client The connection to the client, used to send the response back.
	 * @return A ServerResponseBackToClient object indicating the success or failure of inserting the new request.
	 */
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
	
	/**
	 * Handles the import of all pending requests for park parameter changes. It retrieves all requests that have not yet been approved or denied and sends them back to the client for review.
	 * 
	 * @param data The data container, potentially holding filtering criteria.
	 * @param client The connection to the client, used to send the response back.
	 * @return A ServerResponseBackToClient object containing all pending requests or an indication that there are no pending requests.
	 */
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
	
	/**
	 * Processes an update request in the database with a list of requests. Each request's status is updated according to its current status.
	 * If any request update fails or if the request does not exist in the database, the operation is halted and a failure response is returned.
	 *
	 * @param data The client request data container holding the list of requests to be updated.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a success response if all requests are updated successfully. If any update fails or a request doesn't exist, returns a failure response.
	 */
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
	
	/**
	 * Handles a new park reservation request by attempting to insert a new request into the database. The request's success or failure is based on the database operation's result.
	 *
	 * @param data The client request data container that includes the park reservation request details.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a success response if the request is inserted successfully. Returns a failure response if the request insertion fails due to a similar pending request.
	 */
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
	
	/**
	 * Processes a new request to change the park capacity by attempting to insert the request into the database. The operation's outcome determines the response.
	 *
	 * @param data The client request data container holding the park capacity change request.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. If the request is successfully inserted, a success response is returned. If the insertion fails due to an existing pending request of the same type, a failure response is returned.
	 */
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
	
	/**
	 * Initiates the creation of a total amount division report. The method attempts to generate a report based on the provided data.
	 *
	 * @param data The client request data container with the details for the total amount division report.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a success response if the report is generated successfully. In case of failure to generate the report, a failure response is returned.
	 */
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

	/**
	 * Handles the import of a total amount division report by retrieving it from the database. The method checks if the requested report exists and returns the corresponding response.
	 *
	 * @param data The client request data container containing the report's details to be imported.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. If the report is found, it returns a response with the report data. If not found, a response indicating the report's absence is returned.
	 */
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
	
	/**
	 * Manages the import of a visits report from the database. It checks for the existence of the requested report and returns it if available.
	 *
	 * @param data The client request data container that includes the specifics of the visits report to be retrieved.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a response containing the report data if the report is found. If the report is not found, returns a response indicating its absence.
	 */
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
	
	/**
	 * Initiates the generation of a visits report based on the provided data. The method attempts to create the report and returns a response based on the outcome.
	 *
	 * @param data The client request data container with details for generating the visits report.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. A success response is returned if the report is generated successfully. If the report generation fails, a failure response is returned.
	 */
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
	
	/**
	 * Handles the creation of a cancellations report. It attempts to generate a report based on the given data and returns a response indicating the success or failure of the operation.
	 *
	 * @param data The client request data container with the details for the cancellations report.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a success response if the report is generated successfully, otherwise, returns a failure response indicating the report generation failed.
	 */
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
	
	/**
	 * Handles the request to import a cancellations report from the database. The method checks if the requested report exists and retrieves it.
	 *
	 * @param data The client request data container containing the report's details to be imported.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. If the report is found, it returns a response with the report data. If the report is not found, returns a response indicating the absence of such a report.
	 */
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

	/**
	 * Initiates the creation of a usage report. This method attempts to generate a new report based on the usage data provided and returns a response based on the result.
	 *
	 * @param data The client request data container that includes details for the usage report.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance indicating the outcome. It returns a success response if the report is generated successfully, or a failure response if the report generation fails.
	 */
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
	
	/**
	 * Manages the request to import a usage report by retrieving it from the database based on the provided details.
	 *
	 * @param data The client request data container with the specifics of the usage report to be retrieved.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a response with the report data if the report is found, or a response indicating the report's absence if it is not found.
	 */
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
	
	/**
	 * Searches for guides with a status of pending. It checks the database for any guides matching this criteria and returns the list of such guides.
	 *
	 * @param data The client request data container that includes the search criteria.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. If guides with a pending status are found, it returns a success response along with the list of guides. If no such guides are found, it returns a response indicating the absence of pending status guides.
	 */
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
	
	/**
	 * Updates the status of guides to 'Approved'. It processes each guide in the provided list and updates their status in the database.
	 *
	 * @param data The client request data container with the list of guides whose status needs to be updated to 'Approved'.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance. Returns a success response if all guides are updated successfully. If any guide's status update fails, it returns a failure response.
	 */
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
	
	/**
	 * Searches for available dates for an order within the next 7 days. It queries the database to find open slots and returns the available dates.
	 *
	 * @param data The client request data container with the order details used for searching available dates.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance containing a list of available dates for the order. The response object's server response field is not set in this method.
	 */
	private ServerResponseBackToClient handleSearchForAvailableDates(ClientRequestDataContainer data,
			ConnectionToClient client) {
		Order order = (Order) data.getData();
		ArrayList<LocalDateTime> availableDates = QueryControl.orderQueries.searchForAvailableDates7DaysForward(order);
		return new ServerResponseBackToClient(null, availableDates);
	}
	
	/**
	 * Attempts to add a new order based on the availability for the requested date. It evaluates the database response to determine if the new order can be added. The method handles various cases such as park non-existence, full capacity, and maximum visitor limit exceedance.
	 *
	 * @param data The client request data container containing the order details.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance indicating the outcome of the order addition attempt. Returns null if the park does not exist, the operation failed, or in case of an unexpected database response. Otherwise, it returns a response reflecting the specific reason the order could not be added or confirming the availability.
	 */
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
	
	/**
	 * Handles login requests for employees. It verifies an employee's credentials and connection status before allowing access. This method also checks for multiple connections and logs SQL exceptions.
	 *
	 * @param data The client request data container containing employee login information.
	 * @param client The connection to the client, used for identifying and managing the client's connection status. This parameter is indirectly used through checking multiple connections.
	 * @return A {@link ServerResponseBackToClient} instance indicating the login attempt's outcome. It covers various scenarios including not found, incorrect password, successful connection, user already connected, and query failure.
	 */
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
	
	/**
	 * Manages login requests from guides by verifying their approval status, credentials, and checking for existing connections. The method handles various outcomes including not approved guides, incorrect password, and successful login.
	 *
	 * @param data The client request data container with guide login details.
	 * @param client The connection to the client, crucial for assessing the guide's connection status. This parameter is indirectly utilized when checking for multiple connections.
	 * @return A {@link ServerResponseBackToClient} response detailing the result of the login attempt. This can range from user not found, incorrect password, guide not approved, successful connection, user already connected, to query failure.
	 */
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
	
	/**
	 * Processes login requests from visitors. It checks whether the visitor has an active order and handles their connection status, including checking for multiple connections. SQL exceptions during the login process are also logged.
	 *
	 * @param data The client request data container with visitor information for login.
	 * @param client The connection to the client, important for checking the visitor's connection status. This parameter is indirectly used through the assessment of multiple connections.
	 * @return A {@link ServerResponseBackToClient} instance reflecting the outcome of the login attempt. It handles cases of no active orders, successful connection, user already connected, and query failure.
	 */
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
	
	/**
	 * Searches for an order based on the provided order ID. It fetches the order from the database and returns the outcome of the search, including whether the order was found or does not exist.
	 *
	 * @param data The client request data container containing the order to search for.
	 * @param client The connection to the client, used for sending the response back. This parameter is currently not utilized in the method's logic.
	 * @return A {@link ServerResponseBackToClient} instance indicating whether the order was successfully found, not found, or if a query failure occurred. Each scenario is addressed with a specific server response.
	 */
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
