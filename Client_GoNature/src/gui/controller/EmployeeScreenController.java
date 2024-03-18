package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.ClientCommunication;
import gui.view.ApplicationViewType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import logic.ClientRequestDataContainer;
import logic.Employee;
import logic.EntitiesContainer;
import logic.ExternalUser;
import logic.Guide;
import logic.Park;
import logic.SceneLoaderHelper;
import logic.ServerResponseBackToClient;
import logic.User;
import logic.Visitor;
import utils.AlertPopUp;
import utils.CurrentWindow;
import utils.enums.ClientRequest;
import utils.enums.ParkNameEnum;
import utils.enums.UserTypeEnum;

public class EmployeeScreenController implements Initializable,IScreenController {
	@FXML
	public BorderPane screen;
	@FXML
	public Label userIdLabel;
	@FXML
	public Label employeeTypeLabel;
	@FXML
	public Button homeButton;
	@FXML
	public Button addNewGuideButton;
	@FXML
	public Button parkEntranceButton;
	@FXML
	public Button createReportsButton;
	@FXML
	public Button viewReportsButton;
	@FXML
	public Button parkSettingsButton;
	@FXML
	public Button requestsButton;
	@FXML
	public Button parkSpotsButton;
	@FXML
	public Button logoutButton;
	
	private ExternalUser user;
	private Employee employee;
	private SceneLoaderHelper GuiHelper= new SceneLoaderHelper();
	
	public EmployeeScreenController(ExternalUser user) {
		this.user=user;
		employee=(Employee)user;
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO: hide buttons according to employee type
		initializeScreenAccordingToEmployeeType();
		onHomeClicked();
	}
	
	private void initializeScreenAccordingToEmployeeType() {
		switch(employee.getEmployeeType()) {
			case Department_Manager:
				addNewGuideButton.setVisible(false);
				addNewGuideButton.setManaged(false);
				parkEntranceButton.setVisible(false);
				parkEntranceButton.setManaged(false);
				parkSettingsButton.setVisible(false);
				parkSettingsButton.setManaged(false);
				break;
			case Park_Employee:
				addNewGuideButton.setVisible(false);
				addNewGuideButton.setManaged(false);
				createReportsButton.setVisible(false);
				createReportsButton.setManaged(false);
				viewReportsButton.setVisible(false);
				viewReportsButton.setManaged(false);
				parkSettingsButton.setVisible(false);
				parkSettingsButton.setManaged(false);
				requestsButton.setVisible(false);
				requestsButton.setManaged(false);
				break;
			case Park_Manager:
				addNewGuideButton.setVisible(false);
				addNewGuideButton.setManaged(false);
				parkEntranceButton.setVisible(false);
				parkEntranceButton.setManaged(false);
				viewReportsButton.setVisible(false);
				viewReportsButton.setManaged(false);
				requestsButton.setVisible(false);
				requestsButton.setManaged(false);
				break;
			case Service_Employee:
				parkEntranceButton.setVisible(false);
				parkEntranceButton.setManaged(false);
				createReportsButton.setVisible(false);
				createReportsButton.setManaged(false);
				viewReportsButton.setVisible(false);
				viewReportsButton.setManaged(false);
				parkSettingsButton.setVisible(false);
				parkSettingsButton.setManaged(false);
				requestsButton.setVisible(false);
				requestsButton.setManaged(false);
				parkSpotsButton.setVisible(false);
				parkSpotsButton.setManaged(false);
				break;
		}
		userIdLabel.setText(employee.getUserId());
		employeeTypeLabel.setText(employee.getEmployeeType().toString());
	}
	
	
	public void onLogoutClicked() {
		ClientRequestDataContainer request = new ClientRequestDataContainer(ClientRequest.Logout, employee);
		ClientApplication.client.accept(request);
		ServerResponseBackToClient response = ClientCommunication.responseFromServer;
		GuiHelper.setScreenAfterLogoutOrBack();
		
	}
	
	public void onServerCrashed() {
		AlertPopUp alert = new AlertPopUp(AlertType.ERROR,"FATAL ERROR","Server is Down","Server Crashed - The application will be closed.");
		alert.showAndWait();
		try {
			ClientApplication.client.getClient().closeConnection();
			Platform.runLater(()->CurrentWindow.getCurrentWindow().close());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onHomeClicked() {
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/EmployeeHomepageScreen.fxml",
				ApplicationViewType.EmployeeHomepageScreen,new EntitiesContainer(employee));
		screen.setCenter(dashboard);
	}
	
	public void onAddNewGuideClicked() {
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/ManageGuidesScreen.fxml",
				ApplicationViewType.ManageGuidesScreen,null);
		screen.setCenter(dashboard);
	}
	
	public void onParkEntranceClicked() {
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/ParkEntranceScreen.fxml",
				ApplicationViewType.ParkEntranceScreen,new EntitiesContainer(employee));
		screen.setCenter(dashboard);
	}
	
	public void onCreateReportsClicked() {
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/CreateReportsScreen.fxml",
				ApplicationViewType.CreateReportsScreen,new EntitiesContainer(employee));
		screen.setCenter(dashboard);
	}
	
	public void onViewReportsClicked() {
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/ViewReportsScreen.fxml",
				ApplicationViewType.ViewReportsScreen,new EntitiesContainer(employee));
		screen.setCenter(dashboard);
	}
	
	public void onParkSettingsClicked() {
		Park park = new Park(1,ParkNameEnum.Banias,100,50,4,20);
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/ParkSettingsScreen.fxml",
				ApplicationViewType.ParkSettingsScreen,new EntitiesContainer(park,employee));
		screen.setCenter(dashboard);
	}
	
	public void onRequestsClicked() {
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/RequestTableScreen.fxml",
				ApplicationViewType.RequestTableScreen,new EntitiesContainer(employee));
		screen.setCenter(dashboard);
	}
	
	public void onParkSpotsClicked() {
		Park park = new Park(1,ParkNameEnum.Banias,100,50,4,20);
		AnchorPane dashboard = GuiHelper.loadRightScreenToBorderPaneWithController(screen,"/gui/view/ParkAvailableSpotsScreen.fxml",
				ApplicationViewType.ParkAvailableSpotsScreen,new EntitiesContainer(employee,park));
		screen.setCenter(dashboard);
	}

}