package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import gui.view.ApplicationViewType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import logic.EntitiesContainer;
import logic.Order;
import logic.SceneLoaderHelper;
import logic.User;
import utils.CurrentDateAndTime;
import utils.NotificationMessageTemplate;

public class ConfirmMessageScreenController implements Initializable {
	@FXML
	public Label dateLabel;
	@FXML
	public Label messageLabel;
	
	private Order order;
	private String orderSummaryMessage;
	private BorderPane screen;
	
	public ConfirmMessageScreenController(BorderPane screen,Object order) {
		this.screen=screen;
		this.order=(Order)order;
		orderSummaryMessage=NotificationMessageTemplate.orderConfirmMessage(orderSummaryMessage, orderSummaryMessage, orderSummaryMessage,
				orderSummaryMessage, orderSummaryMessage, orderSummaryMessage, orderSummaryMessage);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		messageLabel.setText(orderSummaryMessage);
	}
	
	public void onHomeClicked() {
		SceneLoaderHelper guiHelper = new SceneLoaderHelper();
		guiHelper.loadRightScreenToBorderPaneWithController(screen,"", ApplicationViewType.CustomerHomgepageScreen, new EntitiesContainer(order));
	}

}
