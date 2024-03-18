package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import gui.view.ApplicationViewType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import logic.EntitiesContainer;
import logic.Order;
import logic.SceneLoaderHelper;
import logic.User;
import utils.AlertPopUp;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;

public class OrderSummaryScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public Label messageLabel;
	@FXML
	public Button cancelButton;
	@FXML
	public Button payNowButton;
	@FXML
	public Button payLaterButton;
	
	
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
		SceneLoaderHelper guiHelper = new SceneLoaderHelper();
		guiHelper.loadRightScreenToBorderPaneWithController(screen,"", ApplicationViewType.CustomerHomgepageScreen, new EntitiesContainer(order));
	}
	
	public void onPayNowClicked() {
		//TODO: update the order in database as Confirmed-Paid
		AlertPopUp alert = new AlertPopUp(AlertType.CONFIRMATION, "Payment Confirmation", "test", "test");
		alert.showAndWait();
	}

	public void onPayLaterClicked() {
		//TODO: update the order in database as Wait-Notification
		AlertPopUp alert = new AlertPopUp(AlertType.CONFIRMATION, "Order Summary", "test", "test");
		alert.showAndWait();
	}

}
