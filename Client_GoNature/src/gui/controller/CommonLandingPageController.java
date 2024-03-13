package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientMainController;
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
import utils.AlertPopUp;
import utils.CurrentWindow;
import utils.ValidationRules;

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
		connect_to_server_vbox.setVisible(false);
		options_vbox.setVisible(true);
//		boolean isValidIp=ValidationRules.isValidIp(server_ip_fld.getText());
//		boolean isValidPort=ValidationRules.isValidPort(server_port_fld.getText());
//		if(!isValidIp||!isValidPort) {
//			message = (!isValidIp)? message + "Server IP isn't valid\n":message;
//			message= (!isValidPort)? message + "Server Port isn't valid\n":message;
//			showPopupMessage(0, "Error", "Fields Validation Error", message);
//			return;
//		}
//		
//		boolean isConnected = GoNatureClient.connectClientToServer(server_ip_fld.getText(),
//				server_port_fld.getText(),this);
//		if(!isConnected) {
//			showPopupMessage(0, "Error", "Connect To Server", "Failed to connect the server");
//			return;
//		}
//		showPopupMessage(0,"Success","Connected!","Connected Success!");
//		connect_to_server_vbox.setVisible(false);
//		options_vbox.setVisible(true);

	}
	
	public void onLoginAppClicked() {
//		AlertPopUp alert = new AlertPopUp(AlertType.INFORMATION,"LOGIN","Login Clicked","Test");
//		alert.showAndWait();
		try {
			Stage currentWindow = (Stage) icon.getScene().getWindow();
			String username = login_username_fld.getText();
			IScreenController controller;
			FXMLLoader loader;
			switch(username) {
			case "":
			case "visitor":
				loader = new FXMLLoader(getClass().getResource("/gui/view/CustomerScreen.fxml"));
				controller = new CustomerScreenController();
				break;
			case "department":
				loader = new FXMLLoader(getClass().getResource("/gui/view/DepartmentManagerScreen.fxml"));
				controller = new DepartmentManagerScreenController();
				break;
			case "park":
				loader = new FXMLLoader(getClass().getResource("/gui/view/ParkManagerScreen.fxml"));
				controller = new ParkManagerScreenController();
				break;
			case "service":
				loader = new FXMLLoader(getClass().getResource("/gui/view/ServiceEmployeeScreen.fxml"));
				controller = new ServiceEmployeeScreenController();
				break;
			case "employee":
				loader = new FXMLLoader(getClass().getResource("/gui/view/ParkEmployeeScreen.fxml"));
				controller = new ParkEmployeeScreenController();
				break;
			default:
				loader = new FXMLLoader(getClass().getResource("/gui/view/ServiceEmployeeScreen.fxml"));
				controller=new ServiceEmployeeScreenController();
				break;
			}

			loader.setController(controller);
			loader.load();
			Parent p = loader.getRoot();
			Stage newStage = new Stage();

			/* Block parent stage until child stage closes */
			newStage.initModality(Modality.WINDOW_MODAL);
			newStage.initOwner(currentWindow);

			newStage.setTitle("GoNature - Client Screen");
			newStage.setScene(new Scene(p));
			newStage.setResizable(false);
			newStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		GoNatureClient.loginClientToApp(new User(login_username_fld.getText(),login_password_fld.getText()));
	}
//	
//	public void openApplicationAccordingToUser(User userInformation) {
//		Stage currentWindow = CurrentWindow.getCurrentWindow();
//		UserTypeEnum userType =userInformation.getUserType();
//		switch(userType) {
//			case DepartmentManager:
//				ViewFactory.getInstance().showClientWindow(UserTypeEnum.DepartmentManager);
//				return;
//			case Visitor:
//				ViewFactory.getInstance().showClientWindow(UserTypeEnum.Visitor);
//				return;
//			case Guide:
//				ViewFactory.getInstance().showClientWindow(UserTypeEnum.Guide);
//				return;
//			case ServiceEmployee:
//				ViewFactory.getInstance().showClientWindow(UserTypeEnum.ServiceEmployee);
//				return;
//			case ParkEmployee:
//				ViewFactory.getInstance().showClientWindow(UserTypeEnum.ParkEmployee);
//				return;
//			case ParkManager:
//				ViewFactory.getInstance().showClientWindow(UserTypeEnum.ParkManager);
//				return;
//		}
//		
//		
//		
//	}
	
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
