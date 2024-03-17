package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import gui.view.ApplicationViewType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import logic.ClientRequestDataContainer;
import logic.EntitiesContainer;
import logic.ExternalUser;
import logic.Guide;
import logic.ICustomer;
import logic.Order;
import logic.SceneLoaderHelper;
import logic.ServerResponseBackToClient;
import logic.Visitor;
import utils.CurrentDateAndTime;
import utils.ValidationRules;
import utils.enums.ClientRequest;
import utils.enums.UserTypeEnum;

public class IdenticationScreenController implements Initializable{

	@FXML
	public Label dateLabel;
	@FXML
	public TextField orderIdField;
	@FXML
	public Button searchButton;
	@FXML
	public HBox errorSection;
	@FXML
	public Label errorMessageLabel;
	
	private BorderPane screen;
	private ExternalUser customer;
	private SceneLoaderHelper GuiHelper = new SceneLoaderHelper();
	
	public IdenticationScreenController(BorderPane screen,Object customer) {
		this.screen=screen;
		this.customer=(ExternalUser)customer;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(CurrentDateAndTime.getCurrentDate("'Today' yyyy-MM-dd"));
		hideErrorMessage();
		
	}
	
	public void onSearchClicked() {
		if(ValidationRules.isFieldEmpty(orderIdField.getText())) {
			showErrorMessage("Order ID cannot be empty!");
			return;
		}
		if(!ValidationRules.isNumeric(orderIdField.getText())) {
			showErrorMessage("Order ID must contain only digits");
			return;
		}
		Order order = new Order(orderIdField.getText());
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Search_For_Relevant_Order,order);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		
		switch(response.getRensponse()) {
		case Order_Not_Found:
			showErrorMessage("Such Order does not exist!");
			return;
		case Order_Found:
			ICustomer currentCustomer;
			if(((Order)response.getMessage()).getOwnerType().equals("Visitor"))
				currentCustomer = (Visitor)customer;
			else
				currentCustomer = (Guide)customer;
			
			AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/HandleOrderScreen.fxml",
					ApplicationViewType.HandleOrderScreen,new EntitiesContainer(response.getMessage(),currentCustomer));
			screen.setCenter(dashboard);
			return;
		}
		
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
