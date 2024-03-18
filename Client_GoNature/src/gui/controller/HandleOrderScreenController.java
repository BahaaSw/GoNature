package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.ExternalUser;
import logic.Guide;
import logic.ICustomer;
import logic.Order;
import logic.Visitor;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class HandleOrderScreenController implements Initializable {
	@FXML
	public Label dateLabel;

	@FXML
	public TextField firstNameField;
	@FXML
	public TextField lastNameField;
	@FXML
	public TextField idField;
	@FXML
	public TextField phoneNumberField;
	@FXML
	public TextField emailField;
	@FXML
	public TextField orderDateOfVisitField;
	@FXML
	public DatePicker pickDate;
	@FXML
	public TextField numberOfVisitorsField;
	@FXML
	public TextField statusField;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	@FXML
	public Button cancelButton;
	@FXML
	public Button confirmButton;
	
	private BorderPane screen;
	private ParkNameEnum selectedPark;
	private String selectedTime;
	private OrderTypeEnum selectedVisitType;
	private Order requestedOrder;
	
	@FXML
	public ComboBox<ParkNameEnum> parksList;
	@FXML
	public ComboBox<String> pickTime;
	@FXML
	public ComboBox<OrderTypeEnum> visitType;
	
	private ObservableList<ParkNameEnum> parks = FXCollections.observableArrayList(
			ParkNameEnum.Banias,
			ParkNameEnum.Herodium,
			ParkNameEnum.Masada
			);
	
	private ObservableList<String> timeForVisits=FXCollections.observableArrayList(
			"09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00"
			);
	
	private ObservableList<OrderTypeEnum> visitTypesList = FXCollections.observableArrayList();
	
	private ICustomer customer;
	
	public HandleOrderScreenController(BorderPane screen,Object order,ICustomer info) {
		this.screen=screen;
		requestedOrder =(Order) order;
		this.customer=info;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		parksList.getItems().addAll(parks);
		parksList.setOnAction(this::onParkChangeSelection);
		pickTime.getItems().addAll(timeForVisits);
		pickTime.setOnAction(this::onTimeChangeSelection);
		
		switch(customer.getUserType()) {
		case Visitor:
			visitTypesList.add(OrderTypeEnum.Solo_PreOrder);
			visitTypesList.add(OrderTypeEnum.Family_PreOrder);
			customer=(Visitor)customer;
			break;
		case Guide:
			visitTypesList.add(OrderTypeEnum.Group_PreOrder);
			customer=(Guide)customer;
			break;
		}
		visitType.getItems().addAll(visitTypesList);
		visitType.setOnAction(this::onVisitTypeChangeSelection);
		initializeAllGuiFields();
		hideErrorMessage();
	}
	
	private void onParkChangeSelection(ActionEvent event) {
		selectedPark=parksList.getValue();
	}
	
	private void onTimeChangeSelection(ActionEvent event) {
		selectedTime=pickTime.getValue();
	}
	
	private void onVisitTypeChangeSelection(ActionEvent event) {
		selectedVisitType=visitType.getValue();
	}
	
	private void initializeAllGuiFields() {
		parksList.setValue(requestedOrder.getParkName());
		firstNameField.setText(requestedOrder.getFirstName());
		lastNameField.setText(requestedOrder.getLastName());
		idField.setText(requestedOrder.getUserId());
		phoneNumberField.setText(requestedOrder.getTelephoneNumber());
		emailField.setText(requestedOrder.getEmail());
		LocalDateTime orderTime = requestedOrder.getEnterDate();
		LocalDate enterTime = orderTime.toLocalDate();
		String exitTime = orderTime.toString().split("T")[1];
		pickDate.setValue(enterTime);
		pickTime.setValue(exitTime);
		numberOfVisitorsField.setText(String.format("%d",requestedOrder.getNumberOfVisitors()));
		visitType.setValue(requestedOrder.getOrderType());
		statusField.setText(requestedOrder.getStatus().toString());
	}
	
	
	private void hideErrorMessage() {
		errorMessageLabel.setText("");
		errorSection.setVisible(false);
	}
	
	private void showErrorMessage(String error) {
		errorSection.setVisible(true);
		errorMessageLabel.setText(error);
	}
	
	public void onCancelClicked() {
		AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION, "test", "test", "test");
		alert.showAndWait();
	}
	
	public void onConfirmClicked() {
		AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION, "test", "test", "test");
		alert.showAndWait();
	}

}
