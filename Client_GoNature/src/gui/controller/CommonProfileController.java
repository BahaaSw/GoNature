package gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import logic.ClientRequestDataContainer;
import logic.ServerResponseBackToClient;
import logic.User;
import utils.AlertPopUp;
import utils.CurrentDate;
import utils.enums.ClientRequest;

public class CommonProfileController implements Initializable {

	@FXML
	public Label date_lbl;
	
	@FXML
	public TextField fname_txt;
	@FXML
	public TextField lname_txt;
	@FXML
	public TextField username_txt;
	@FXML
	public TextField password_txt;
	@FXML
	public TextField account_type_txt;
	@FXML
	public TextField related_park_txt;
	@FXML
	public TextField phone_number_txt;
	@FXML
	public TextField email_txt;
	@FXML
	public Button save_changes_btn;
	
	private boolean isEmployee=false;
	private User user;
	
	public CommonProfileController(User user) {
		this.user=user;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date_lbl.setText(CurrentDate.getCurrentDate("'Today' yyyy-MM-dd"));
		fname_txt.setText(user.getFirstName());
		lname_txt.setText(user.getLastName());;
		username_txt.setText(user.getUsername());;
		password_txt.setText(user.getPassword());;
		account_type_txt.setText(user.getUserType().toString());;
//		related_park_txt.setText(user.getFirstName());;
		phone_number_txt.setText(user.getPhoneNumber());;
		email_txt.setText(user.getEmailAddress());;
	}
	
	public void setIsEmployee(boolean flag) {
		isEmployee=flag;
	}
	
	public void onSaveChangesClicked() {
		user.setFirstName(lname_txt.getText());
		user.setLastName(lname_txt.getText());
		user.setUsername(username_txt.getText());
		user.setPassword(password_txt.getText());
		user.setPhoneNumber(phone_number_txt.getText());
		user.setEmailAddress(email_txt.getText());
		
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Update_User_Details,user,"");
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		
		AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION,"Success","Update Details","Updated Successfully");
		alert.showAndWait();
	}

}
