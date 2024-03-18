package gui.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import gui.view.ApplicationViewType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import logic.EntitiesContainer;
import logic.Order;
import logic.SceneLoaderHelper;
import logic.User;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;
import utils.enums.ParkNameEnum;

public class OrderSummaryScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public Label messageLabel;
	@FXML
	public Button cancelButton;
	@FXML
	public Button continueButton;
	
	
	private Order order;
	private String orderSummaryMessage;
	private BorderPane screen;
	
	public OrderSummaryScreenController(BorderPane screen,Object order) {
		this.screen=screen;
		this.order=(Order)order;
		double priceBeforeDiscount = this.order.getPrice()*this.order.getNumberOfVisitors();
		double priceAfterDiscount = priceBeforeDiscount*0.85;
		orderSummaryMessage=NotificationMessageTemplate.orderSummaryMessage(this.order.getOrderId(), this.order.getParkName().name(), this.order.getEnterDate().toString(),
				this.order.getOrderType().name(), this.order.getNumberOfVisitors(), priceAfterDiscount, priceBeforeDiscount);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		messageLabel.setText(orderSummaryMessage);
	}
	
	public void onCancelClicked() {
		AlertPopUp alert = new AlertPopUp(AlertType.CONFIRMATION, "Order Cancel", "Are you sure?", "Order will not be saved.",ButtonType.YES,ButtonType.CLOSE);
		Optional<ButtonType> result = alert.showAndWait();

        // Check which button was clicked and act accordingly
        if (result.isPresent() && result.get() == ButtonType.YES) {
    		SceneLoaderHelper guiHelper = new SceneLoaderHelper();
    		AnchorPane view = guiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/CustomerHomepageScreen.fxml", ApplicationViewType.Customer_Homepage_Screen, new EntitiesContainer(order));
    		screen.setCenter(view);
        }

	}
	
	public void onContinueClicked() {
		//TODO: update the order in database as Confirmed-Paid
		ButtonType payNow=new ButtonType("Pay Now");
		ButtonType payLater=new ButtonType("Pay Later");
		
		String paymentReceipt = NotificationMessageTemplate.prePaymentReceiptMessage(order.getParkName(), order.getOrderId(),order.getEnterDate().toString(),
				order.getFirstName(),order.getLastName(),order.getPrice()*order.getNumberOfVisitors(), 4,0.85);
		AlertPopUp alert = new AlertPopUp(AlertType.CONFIRMATION,"Payment Notification", "Pay Now", paymentReceipt,payNow,payLater,ButtonType.CLOSE);
		Optional<ButtonType> result = alert.showAndWait();

        // Check which button was clicked and act accordingly
        if (result.isPresent() && result.get() == payNow) {
        	//TODO: add query to save the order in database as Wait and Paid!
    		SceneLoaderHelper guiHelper = new SceneLoaderHelper();
    		AnchorPane view = guiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/CustomerHomepageScreen.fxml", ApplicationViewType.Customer_Homepage_Screen, new EntitiesContainer(order));
    		screen.setCenter(view);
        }
        else if (result.isPresent() && result.get() == payLater) {
        	//TODO: add query to save the order in database as Wait not paid!
    		SceneLoaderHelper guiHelper = new SceneLoaderHelper();
    		AnchorPane view = guiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/CustomerHomepageScreen.fxml", ApplicationViewType.Customer_Homepage_Screen, new EntitiesContainer(order));
    		screen.setCenter(view);
        }

	}


}
