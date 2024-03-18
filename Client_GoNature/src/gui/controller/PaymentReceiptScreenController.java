package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import logic.ExternalUser;
import logic.Order;
import logic.Park;
import logic.Visitor;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;

public class PaymentReceiptScreenController implements Initializable {

	@FXML
	public Label dateLabel;
	@FXML
	public Label paymentReceiptMessageLabel;
	
	private Order order;
	private Park park;
	private ExternalUser visitor;
	
	public PaymentReceiptScreenController(Object order,Object park,Object visitor) {
		this.order=(Order)order;
		this.visitor=(ExternalUser)visitor;
		this.park=(Park)park;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		calculatePriceAfterDiscount();
		Visitor traveler =(Visitor) visitor;
//		String paymentReceipt = NotificationMessageTemplate.paymentReceiptMessage(order.getParkName(),traveler.getFirstName(),traveler.getLastName(),(double)order.getPrice(),park.getCurrentEstimatedStayTime());
//		paymentReceiptMessageLabel.setText(paymentReceipt);
	}
	
	private void calculatePriceAfterDiscount() {
		
	}

}
