package gui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import gui.view.ApplicationViewType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.EntitiesContainer;
import logic.ExternalUser;
import logic.ICustomer;
import logic.Order;
import logic.SceneLoaderHelper;
import utils.CurrentDateAndTime;
import utils.enums.OrderTypeEnum;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class MakeOrderScreenController implements Initializable{
	
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
	public DatePicker pickDate;
	@FXML
	public TextField numberOfVisitorsField;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	@FXML
	public Button makeOrderButton;
	
	private BorderPane screen;
	private ParkNameEnum selectedPark=ParkNameEnum.None;
	private String selectedTime="";
	private OrderTypeEnum selectedVisitType=OrderTypeEnum.None;
	private UserTypeEnum customerType=UserTypeEnum.ExternalUser;
	private SceneLoaderHelper GuiHelper = new SceneLoaderHelper();
	private ICustomer customerDetails;
	
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
			"09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00"
			);
	
	private ObservableList<OrderTypeEnum> visitTypesList = FXCollections.observableArrayList();
	
	
	public MakeOrderScreenController(BorderPane screen,Object customer,ICustomer customerDetails) {
		this.screen=screen;
		customerType=((ExternalUser)customer).getUserType();
		this.customerDetails=customerDetails;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		parksList.getItems().addAll(parks);
		parksList.setOnAction(this::onParkChangeSelection);
		pickTime.getItems().addAll(timeForVisits);
		pickTime.setOnAction(this::onTimeChangeSelection);
		// initialize date picker up to 3 months forward.
		pickDate.setDayCellFactory(picker->new DateCell() {
			LocalDate maxDate = LocalDate.now().plusMonths(1);
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date,empty);
				setDisable(empty||date.compareTo(LocalDate.now())<=0||date.compareTo(maxDate)>0);
			}
		});
		
		switch(customerType) {
		case Visitor:
			visitTypesList.add(OrderTypeEnum.Solo_PreOrder);
			visitTypesList.add(OrderTypeEnum.Family_PreOrder);
			firstNameField.setEditable(false);
			firstNameField.setText(customerDetails.getFirstName());
			lastNameField.setEditable(false);
			lastNameField.setText(customerDetails.getLastName());
			emailField.setEditable(false);
			emailField.setText(customerDetails.getEmailAddress());
			phoneNumberField.setEditable(false);
			phoneNumberField.setText(customerDetails.getPhoneNumber());
			idField.setEditable(false);
			idField.setText(customerDetails.getCustomerId());
			break;
		case ExternalUser:
			visitTypesList.add(OrderTypeEnum.Solo_PreOrder);
			visitTypesList.add(OrderTypeEnum.Family_PreOrder);
			break;
		case Guide:
			visitTypesList.add(OrderTypeEnum.Group_PreOrder);
			firstNameField.setEditable(false);
			firstNameField.setText(customerDetails.getFirstName());
			lastNameField.setEditable(false);
			lastNameField.setText(customerDetails.getLastName());
			emailField.setEditable(false);
			emailField.setText(customerDetails.getEmailAddress());
			phoneNumberField.setEditable(false);
			phoneNumberField.setText(customerDetails.getPhoneNumber());
			idField.setEditable(false);
			idField.setText(customerDetails.getCustomerId());
			break;
		}
		visitType.getItems().addAll(visitTypesList);
		visitType.setOnAction(this::onVisitTypeChangeSelection);
		
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
	
	
	public void onMakeOrderClicked() {
		//TODO: logic for creation new order, check available
		//TODO: check all fields validation.
		Order order = new Order();
		order.setOrderId(5);
		order.setParkName(selectedPark);
		order.setFirstName(firstNameField.getText());
		order.setLastName(lastNameField.getText());
		order.setUserId(idField.getText());
		order.setTelephoneNumber(phoneNumberField.getText());
		order.setEmail(emailField.getText());
		LocalDate date = pickDate.getValue();
		String fullDateTime = date.toString()+"T"+selectedTime;
		order.setEnterDate(LocalDateTime.parse(fullDateTime));
		order.setNumberOfVisitors(Integer.parseInt(numberOfVisitorsField.getText()));
		order.setOrderType(selectedVisitType);
		order.setPrice(30);
		
		//TODO: decide which screen to load. 
		// if can't make new order, open rescheduleOrder
		// else, open order summary
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/OrderSummaryScreen.fxml",
				ApplicationViewType.Order_Summary_Screen,new EntitiesContainer(order));
		screen.setCenter(dashboard);
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
