package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import client.ClientMainControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.ClientRequestDataContainer;
import logic.ServerResponseBackToClient;
import logic.User;
import utils.AlertPopUp;
import utils.CurrentWindow;
import utils.ValidationRules;
import utils.enums.ClientRequest;
import utils.enums.ServerResponseEnum;

public class CommonLandingPageController implements Initializable {
	/* FXML Controls*/
	/* connect to server*/
	@FXML
	public ImageView icon;
	@FXML
	public Button connect_btn;
	@FXML
	public VBox connect_to_server_vbox;
	@FXML
	public TextField server_ip_fld;
	@FXML
	public TextField server_port_fld;
	
	/*login form*/
	@FXML
	public Button login_btn;
	@FXML
	public VBox login_vbox;
	@FXML
	public Button back_from_login_btn;
	@FXML
	public TextField login_username_fld;
	@FXML
	public PasswordField login_password_fld;
	
	/*signup form*/
	@FXML
	public Button signup_btn;
	@FXML
	public VBox signup_vbox;
	@FXML
	public Button back_from_signup_btn;
	@FXML
	public TextField signup_username_fld;
	@FXML
	public TextField signup_password_fld;
	@FXML
	public TextField signup_email_fld;
	@FXML
	public TextField signup_fname_fld;
	@FXML
	public TextField signup_lname_fld;
	@FXML
	public TextField signup_phone_fld;
	@FXML
	public RadioButton isGuide;
	
	
	/*options form*/
	@FXML
	public Button signup_option_btn;
	@FXML
	public Button login_option_btn;
	@FXML
	public VBox options_vbox;
	
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void onConnectToServerClicked() {
		String message="";
		boolean isValidIp=ValidationRules.isValidIp(server_ip_fld.getText());
		boolean isValidPort=ValidationRules.isValidPort(server_port_fld.getText());
		if(!isValidIp||!isValidPort) {
			message = (!isValidIp)? message + "Server IP isn't valid\n":message;
			message= (!isValidPort)? message + "Server Port isn't valid\n":message;
			AlertPopUp alert = new AlertPopUp(AlertType.ERROR, "Error", "Fields Validation Error", message);
			alert.showAndWait();
			return;
		}
		
		ClientApplication.client = new ClientMainControl(server_ip_fld.getText(), Integer.parseInt(server_port_fld.getText()));
		if(ClientApplication.client.getClient()==null) {
			AlertPopUp alert = new AlertPopUp(AlertType.ERROR, "Error", "Connect To Server", "Failed to connect the server");
			alert.showAndWait();
			return;
		}
		AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION,"Success","Connect To Server","Connected Success!");
		alert.showAndWait();
		connect_to_server_vbox.setVisible(false);
		options_vbox.setVisible(true);

	}
	
	public void onLoginAppClicked() {
		String message="";
		AlertPopUp alert;
		ArrayList<String> fields = new ArrayList<String>();
		//TODO: Add ID To GUI!!!!!!
		fields.add("1");
		fields.add(login_username_fld.getText());
		fields.add(login_password_fld.getText());
		
		boolean areFieldsEmpty = ValidationRules.areFieldsEmpty(fields);
		boolean isValidUsername=ValidationRules.isValidUsername(fields.get(1));
		boolean isValidPassword=ValidationRules.isValidPassword(fields.get(2));
		
		if(areFieldsEmpty) {
			message="All Fields Must be Filled!";
			alert = new AlertPopUp(AlertType.WARNING, "Warning", "Validation Error", message);
			alert.showAndWait();
			return;
		}
		
		User requestedUser = new User(fields.get(0),fields.get(1),fields.get(2));
		ClientRequestDataContainer requestMessage = new ClientRequestDataContainer(ClientRequest.Login, requestedUser, "");
		ClientApplication.client.accept(requestMessage);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		switch(response.getRensponse()) {
			case Password_Incorrect:
				alert = new AlertPopUp(AlertType.WARNING, "Warning", "Password Incorrect", "");
				alert.showAndWait();
				return;
			case User_Already_Connected:
				alert = new AlertPopUp(AlertType.WARNING, "Warning", "User Already Connected!", "");
				alert.showAndWait();
				return;
			case Request_Failed:
				alert = new AlertPopUp(AlertType.ERROR, "Server Got Error", "Application Will Close", "");
				alert.showAndWait();
				Stage currentWindow = (Stage) icon.getScene().getWindow();
				currentWindow.close();
				System.exit(0);
				return;	
		}
		
		switchMainScreenAccordingToUserLogin((User)response.getMessage());
			
		
	}
	
	private void switchMainScreenAccordingToUserLogin(User user) {
		try {
			CurrentWindow.setCurrentWindow((Stage) icon.getScene().getWindow());
			String username = login_username_fld.getText();
			IScreenController controller;
			FXMLLoader loader;
			switch(user.getUserType()) {
			case Visitor:
			case Guide:
				loader = new FXMLLoader(getClass().getResource("/gui/view/CustomerScreen.fxml"));
				controller = new CustomerScreenController(user);
				break;
			case Department_Manager:
				loader = new FXMLLoader(getClass().getResource("/gui/view/DepartmentManagerScreen.fxml"));
				controller = new DepartmentManagerScreenController(user);
				break;
			case Park_Manager:
				loader = new FXMLLoader(getClass().getResource("/gui/view/ParkManagerScreen.fxml"));
				controller = new ParkManagerScreenController(user);
				break;
			case Service_Employee:
				loader = new FXMLLoader(getClass().getResource("/gui/view/ServiceEmployeeScreen.fxml"));
				controller = new ServiceEmployeeScreenController(user);
				break;
			case Park_Employee:
				loader = new FXMLLoader(getClass().getResource("/gui/view/ParkEmployeeScreen.fxml"));
				controller = new ParkEmployeeScreenController(user);
				break;
			default:
				loader = new FXMLLoader(getClass().getResource("/gui/view/ServiceEmployeeScreen.fxml"));
				controller=new ServiceEmployeeScreenController(user);
				break;
			}
			
			loader.setController(controller);
			loader.load();
			Parent p = loader.getRoot();
	
			/* Block parent stage until child stage closes */
	//		newStage.initModality(Modality.WINDOW_MODAL);
	//		newStage.initOwner(currentWindow);
	
			CurrentWindow.getCurrentWindow().setTitle("GoNature - Client Screen");
			CurrentWindow.getCurrentWindow().setScene(new Scene(p));
			CurrentWindow.getCurrentWindow().setResizable(false);
			ClientApplication.runningController=controller;
			CurrentWindow.getCurrentWindow().show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	public void onSignupAppClicked() {
		AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION,"SIGNUP","SIGNUP Clicked","Test");
		alert.showAndWait();
//		//TODO: Logic of signup in database
//		boolean isRegistered=false;
//		String message="";
//		ArrayList<String> fields = new ArrayList<String>();
//		fields.add(signup_username_fld.getText());
//		fields.add(signup_password_fld.getText());
//		fields.add(signup_email_fld.getText());
//		fields.add(signup_fname_fld.getText());
//		fields.add(signup_lname_fld.getText());
//		fields.add(signup_phone_fld.getText());
//		fields.add(isGuide.isSelected() ? "1":"0");
//
//		boolean areFieldsEmpty = ValidationRules.areFieldsEmpty(fields);
//		boolean isValidPhone=ValidationRules.isValidPhone(signup_phone_fld.getText());
//		boolean isValidEmail=ValidationRules.isValidEmail(signup_email_fld.getText());
//		if(areFieldsEmpty) {
//			message = message +"All Fields must be Filled!";
//			showPopupMessage(0, "Error", "Fields Validation Error", message);
//			return;
//		}
//		if(!isValidPhone||!isValidEmail) {
//			message = (!isValidPhone)? message + "Phone Number isn't valid\n":message;
//			message= (!isValidEmail)? message + "Email Address isn't valid\n":message;
//			showPopupMessage(0, "Error", "Fields Validation Error", message);
//			return;
//	}
//		GoNatureClient.clientSignupRequest(new User(fields.get(0),fields.get(1)));
//		if(isRegistered) {
//			showPopupMessage(0, "Success", "Register New Client", "New Username Was Created Successfully!");
//            signup_vbox.setVisible(false);
//            options_vbox.setVisible(true);
//		}
//		else {
//			showPopupMessage(0, "Failed", "Register New Client", "New Username Creation was FAILED!");
//            signup_vbox.setVisible(false);
//            options_vbox.setVisible(true);
//		}
		
	}
	
	public void onLoginOptionClicked() {
		options_vbox.setVisible(false);
		login_vbox.setVisible(true);
	}
	
	public void onBackFromSignupClicked() {
		signup_vbox.setVisible(false);
		clearSignupFields();
		options_vbox.setVisible(true);
	}
	
	private void clearSignupFields() {
		signup_username_fld.clear();
		signup_password_fld.clear();
		signup_email_fld.clear();
		signup_fname_fld.clear();
		signup_lname_fld.clear();
		signup_phone_fld.clear();
		isGuide.setSelected(false);
	}
	
	public void onBackFromLoginClicked() {
		login_vbox.setVisible(false);
		clearLoginFields();
		options_vbox.setVisible(true);
	}
	
	private void clearLoginFields() {
		login_username_fld.clear();
		login_password_fld.clear();
	}
	
	public void onSignupOptionClicked() {
		options_vbox.setVisible(false);
		signup_vbox.setVisible(true);
	}
	
	public void setScreenAfterLogout() {
		connect_to_server_vbox.setVisible(false);
		options_vbox.setVisible(true);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
