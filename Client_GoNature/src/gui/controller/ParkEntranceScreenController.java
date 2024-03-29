package gui.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import client.ClientApplication;
import client.ClientCommunication;
import gui.view.ApplicationViewType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.EntitiesContainer;
import logic.Order;
import logic.OrderInTable;
import logic.SceneLoaderHelper;
import logic.ServerResponseBackToClient;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.EntranceDiscount;
import utils.NotificationMessageTemplate;
import utils.enums.ClientRequest;

public class ParkEntranceScreenController implements Initializable, IThreadController {
	@FXML
	public Label dateLabel;
	@FXML
	public TextField orderIdField;
	@FXML
	public Button enterParkButton;
	@FXML
	public Button newVisitButton;
	@FXML
	public Button exitParkButton;
	@FXML
	public Button cancelSelectedOrder;
	@FXML
	public TableView<OrderInTable> inParkTable;
	@FXML
	private TableColumn<OrderInTable, String> orderIdCol;
	@FXML
	private TableColumn<OrderInTable, String> amountCol;
	@FXML
	private TableColumn<OrderInTable, String> isPaidCol;
	@FXML
	private TableColumn<OrderInTable, String> phoneCol;
	@FXML
	private TableColumn<OrderInTable, String> estimatedEnterCol;
	@FXML
	private TableColumn<OrderInTable, String> estimatedExitCol;
	@FXML
	private TableColumn<OrderInTable, String> fromWhichTableCol;
	@FXML
	private TableColumn<OrderInTable, String> statusCol;

	private ObservableList<OrderInTable> ordersForNow = FXCollections.observableArrayList();

	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;

	private BorderPane screen;
	private Employee employee;
	private OrderInTable selectedOrder;
	private ScheduledExecutorService scheduler;

	public ParkEntranceScreenController(BorderPane screen, Object employee) {
		this.screen = screen;
		this.employee = (Employee) employee;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		setupTable();
		hideErrorMessage();
		startBackgroundUpdateTask();
	}

	private void setupTable() {
		orderIdCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("orderId"));
		amountCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("amountOfVisitors"));
		isPaidCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("isPaid"));
		phoneCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("ownerPhone"));
		estimatedEnterCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("estimatedEnterTime"));
		estimatedExitCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("estimatedExitTime"));
		statusCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("status"));
		fromWhichTableCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("orderTable"));

		statusCol.setCellFactory(column -> new TableCell<OrderInTable, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					OrderInTable order = getTableView().getItems().get(getIndex());
					if (order.getStatus().equals("In Park")) {
						setTextFill(Color.GREEN);
					} else {
						setTextFill(Color.RED);
					}
				}
			}
		});
		inParkTable.setItems(ordersForNow);
		hideErrorMessage();
	}

	private void startBackgroundUpdateTask() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			// Fetch and update the list of orders
			Platform.runLater(() -> updateOrdersList());
		}, 0, 5, TimeUnit.SECONDS);
	}

	private void updateOrdersList() {
		Platform.runLater(() -> {
			ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Import_All_Orders_For_Now,
					employee.getRelatedPark().getParkId());
			ClientApplication.client.accept(request);
			ServerResponseBackToClient response = ClientCommunication.responseFromServer;

			@SuppressWarnings("unchecked")
			ArrayList<Order> listOfOrders = (ArrayList<Order>) response.getMessage();
			if (listOfOrders != null) {
				ordersForNow.clear();
				for (Order order : listOfOrders) {
					ordersForNow.add(new OrderInTable(order));
				}

				inParkTable.refresh();
			}
		});
	}

	public void onEnterParkClicked() {
		selectedOrder = inParkTable.getSelectionModel().getSelectedItem();
		if (selectedOrder == null) {
			showErrorMessage("You must select order from table before clicking!");
			return;
		} else if (selectedOrder.getStatus().equals("In Park")) {
			showErrorMessage("You choose order already inside park");
			return;
		}
		
//		LocalDateTime currentTime = LocalDateTime.now().minusMinutes(5);
//		LocalDateTime orderTime = LocalDateTime.parse(selectedOrder.getEstimatedEnterTime());
//		// Calculate the time that is 5 minutes before the original enter time
//		LocalDateTime fiveMinutesBeforeOrderTime = orderTime.minusMinutes(5);
//
//		// Check if the current time is within the 5-minute window before the order time or exactly at the order time
//		if (currentTime.isBefore(fiveMinutesBeforeOrderTime) && currentTime.isAfter(orderTime)) {
//			showErrorMessage("Orders can enter park maximum 5 minutes before their order");
//			return;
//		}


		Integer orderId = Integer.parseInt(selectedOrder.getOrderId());
		if (selectedOrder.getIsPaid().equals("No")) {
			ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Show_Payment_At_Entrance,
					orderId);
			ClientApplication.client.accept(request);
			ServerResponseBackToClient response = ClientCommunication.responseFromServer;
			Order order = (Order) response.getMessage();

			ButtonType payNow = new ButtonType("Pay Now");
			double price = calculatePriceByOrderType(order);
			Duration duration = Duration.between(order.getEnterDate(), order.getExitDate());
			long estimatedVisitTime = duration.toHours();

			// ParkNameEnum parkName,OrderTypeEnum type,String firstName,String
			// lastName,double totalPrice,long estimatedTimeVisit
			String paymentReceipt = NotificationMessageTemplate.entrancePaymentReceiptMessage(order.getParkName(),
					order.getOrderType(), order.getFirstName(), order.getLastName(), order.getNumberOfVisitors(), price,
					estimatedVisitTime);

			AlertPopUp alert = new AlertPopUp(AlertType.CONFIRMATION, "Payment Notification", "Pay Now", paymentReceipt,
					payNow, ButtonType.CLOSE);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.CLOSE) {
				return;
			}

		}
		selectedOrder.setIsPaid("Yes");
		inParkTable.refresh();
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Update_Order_Status_In_Park,
				orderId);
		ClientApplication.client.accept(request);
		@SuppressWarnings("unused")
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		hideErrorMessage();

		return;

	}

	public void onNewVisitClicked() {
		// TODO: open handleoccasionalvisit screen
		// TODO : create new order and mark status as "In Park".
		AnchorPane dashboard = SceneLoaderHelper.getInstance().loadRightScreenToBorderPaneWithController(screen,
				"/gui/view/HandleOccasionalVisitScreen.fxml", ApplicationViewType.HandleOccasionalVisitScreen,
				new EntitiesContainer(employee));
		screen.setCenter(dashboard);

	}

	public void onNotArrivedClicked() {
		selectedOrder = inParkTable.getSelectionModel().getSelectedItem();
		if (selectedOrder == null) {
			showErrorMessage("You must select order from table before clicking!");
			return;
		} else if (selectedOrder.getStatus().equals("In Park")) {
			showErrorMessage("You choose order already inside park");
			return;
		}

		String enterTime = selectedOrder.getEstimatedEnterTime();
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime estimatedEnterTime = LocalDateTime.parse(enterTime);
		if (currentTime.isBefore(estimatedEnterTime)) {
			showErrorMessage("Too early to mark this order as not arrived");
			return;
		}

		if (selectedOrder.getOrderTable().equals("Occasional")) {
			showErrorMessage("Can't make occasional visits as not arrived!");
			return;
		}

		selectedOrder.setStatus("Time Passed");
		inParkTable.refresh();
		Integer orderId = Integer.parseInt(selectedOrder.getOrderId());
		ClientRequestDataContainer request = new ClientRequestDataContainer(
				ClientRequest.Update_Order_Status_Time_Passed, orderId);
		ClientApplication.client.accept(request);
		@SuppressWarnings("unused")
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		hideErrorMessage();

		return;

	}

	public void onExitParkClicked() {
		selectedOrder = inParkTable.getSelectionModel().getSelectedItem();
		if (selectedOrder == null) {
			showErrorMessage("You must select order from table before clicking!");
			return;
		} else if (selectedOrder.getStatus().equals("Confirmed")) {
			showErrorMessage("You choose order which not in park yet");
			return;
		}

		selectedOrder.setStatus("Completed");
		inParkTable.refresh();
		ArrayList<Object> dataForServer = new ArrayList<Object>();
		dataForServer.add(Integer.parseInt(selectedOrder.getOrderId()));
		dataForServer.add(selectedOrder.getOrderTable());
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Update_Order_Status_Completed,
				dataForServer);
		ClientApplication.client.accept(request);
		@SuppressWarnings("unused")
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		hideErrorMessage();

		return;

	}

	private void hideErrorMessage() {
		errorMessageLabel.setText("");
		errorSection.setVisible(false);
	}

	private void showErrorMessage(String error) {
		errorSection.setVisible(true);
		errorMessageLabel.setText(error);
	}

	@SuppressWarnings("incomplete-switch")
	private double calculatePriceByOrderType(Order order) {
		double priceAtEntrance = 0;

		switch (order.getOrderType()) {
		case Solo_PreOrder:
		case Family_PreOrder:
			priceAtEntrance = order.getPrice() * order.getNumberOfVisitors()
					* EntranceDiscount.SOLO_FAMILY_PREORDER_DISCOUNT;
			break;
		case Group_PreOrder:
			priceAtEntrance = order.getPrice() * (order.getNumberOfVisitors() + 1)
					* EntranceDiscount.GROUP_PREORDER_DISCOUNT;
			break;
		}
		return priceAtEntrance;

	}

	@Override
	public void cleanUp() {
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdownNow();
		}

	}
}
