package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import logic.Order;
import logic.OrderInTable;
import utils.CurrentDateAndTime;

public class ParkEntranceScreenController implements Initializable {
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
	private TableColumn<OrderInTable,String> orderIdCol;
	@FXML
	private TableColumn<OrderInTable,String> amountCol;
	@FXML
	private TableColumn<OrderInTable,String> phoneCol;
	@FXML
	private TableColumn<OrderInTable,String> estimatedEnterCol;
	@FXML
	private TableColumn<OrderInTable,String> estimatedExitCol;
	@FXML
	private TableColumn<OrderInTable,String> actualEnterCol;
	@FXML
	private TableColumn<OrderInTable,String> timeLeftCol;
	
	private ObservableList<OrderInTable> ordersForNow= FXCollections.observableArrayList(
	        (new OrderInTable("1", "10", "0503418900", "00:20", "02:20", "00:22", "")),
	        (new OrderInTable("2", "15", "0503418901", "21:00", "23:30", "22:00", "")),
	        (new OrderInTable("3", "5", "0503418902", "21:00", "23:45", "23:00", "")));
	
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	
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
		phoneCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("ownerPhone"));
		estimatedEnterCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("estimatedEnterTime"));
		estimatedExitCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("estimatedExitTime"));
		actualEnterCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("actualEnterTime"));
		timeLeftCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("timeLeftInPark"));
		
		
	    // Your existing setup code...
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    actualEnterCol.setCellFactory(column -> new TableCell<OrderInTable, String>() {
	        @Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (item == null || empty) {
	                setText(null);
	                setStyle("");
	            } else {
	                setText(item);
	                OrderInTable order = getTableView().getItems().get(getIndex());
	                LocalTime estimatedEnter = LocalTime.parse(order.getEstimatedEnterTime(),formatter);
	                LocalTime actualEnter = LocalTime.parse(order.getActualEnterTime(),formatter);
	                if (ChronoUnit.MINUTES.between(estimatedEnter, actualEnter) <= 15) {
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
	    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	    scheduler.scheduleAtFixedRate(() -> {
	        // Fetch and update the list of orders
	        Platform.runLater(() -> updateOrdersList());
	    }, 0, 5, TimeUnit.SECONDS);
	}

	private void updateOrdersList() {
	    Platform.runLater(() -> {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	        LocalDate today = LocalDate.now();
//	        ordersForNow.clear(); // Clear the existing items
	        for (OrderInTable order : ordersForNow) {
	            LocalTime exitTime = LocalTime.parse(order.getEstimatedExitTime(), formatter);
	            LocalDateTime estimatedExit = LocalDateTime.of(today, exitTime);

	            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), estimatedExit);
	            order.setTimeLeftInPark(String.valueOf(minutesLeft)); // Assuming you have a setter for this property
	            inParkTable.refresh();
//	            ordersForNow.add(order);
	        }
	    });
	}
	
	public void onEnterParkClicked() {
		//TODO: search for relevant order in database
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		OrderInTable newOrder = new OrderInTable("5", "10", "0503418900","23:35" , "01:40", "23:35", "");
		ordersForNow.add(newOrder);
		inParkTable.refresh();
	}
	
	public void onNewVisitClicked() {
		//TODO: open handleoccasionalvisit screen
	}
	
	public void onCancelSelectedOrderClicked() {
		
	}
	
	public void onExitParkClicked() {
		
	}
	
	private void hideErrorMessage() {
		errorMessageLabel.setText("");
		errorSection.setVisible(false);
	}
	
	private void showErrorMessage(String error) {
		errorSection.setVisible(true);
		errorMessageLabel.setText(error);
	}
}
