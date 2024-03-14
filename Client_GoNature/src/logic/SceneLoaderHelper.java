package logic;

import java.io.IOException;

import client.ClientApplication;
import gui.controller.CommonLandingPageController;
import gui.controller.CommonProfileController;
import gui.controller.ControllerType;
import gui.controller.CustomerDashboardController;
import gui.controller.CustomerEnterParkController;
import gui.controller.CustomerMakeOrderController;
import gui.controller.CustomerManageOrdersController;
import gui.controller.DepartmentManagerDashboardController;
import gui.controller.DepartmentManagerViewReportsController;
import gui.controller.ParkEmployeeDashboardController;
import gui.controller.ParkManagerDashboardController;
import gui.controller.ServiceEmployeeDashboardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.CurrentWindow;

public class SceneLoaderHelper {
	
	private boolean isEmployee=false;
	private boolean isGuide=false;
	private AnchorPane centerScreen;
	private VBox leftMenuScreen;
	
	//UserType type
	public AnchorPane loadRightScreenToBorderPaneWithController(String screenUrl,ControllerType controllerType,User user) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(screenUrl));
		switch(controllerType) {
		
			case Common_LandingPage_Controller:{
				CommonLandingPageController controller = new CommonLandingPageController();
				loader.setController(controller);
				break;
			}
				
			case Common_Profile_Controller:{
				CommonProfileController controller= new CommonProfileController(user);
				loader.setController(controller);
				controller.setIsEmployee(true);
				break;
			}
			
			// Customer (Visitor/Guide) Controllers
			case Customer_Dashboard_Controller:{
				CustomerDashboardController controller = new CustomerDashboardController();
				loader.setController(controller);
				break;
			}
			
			case Customer_EnterPark_Controller:{
				CustomerEnterParkController controller = new CustomerEnterParkController();
				loader.setController(controller);
				break;
			}
			case Customer_MakeOrder_Controller:{
				CustomerMakeOrderController controller = new CustomerMakeOrderController();
				loader.setController(controller);
				break;
			}

			case Customer_ManageOrders_Controller:{
				CustomerManageOrdersController controller = new CustomerManageOrdersController();
				loader.setController(controller);
				break;
			}
			
			// Department Manager Controllers
			case Department_Manager_Dashboard_Controller:{
				DepartmentManagerDashboardController controller = new DepartmentManagerDashboardController();
				loader.setController(controller);
				break;
			}
			case Department_Manager_ViewReports_Controller:{
				DepartmentManagerViewReportsController controller = new DepartmentManagerViewReportsController();
				loader.setController(controller);
				break;
			}
			
			// Park Manager Controllers
			case Park_Manager_Dashboard_Controller:{
				ParkManagerDashboardController controller = new ParkManagerDashboardController();
				loader.setController(controller);
				break;
			}
			
			// Park Employee Controllers
			case Park_Employee_Dashboard_Controller:{
				ParkEmployeeDashboardController controller = new ParkEmployeeDashboardController();
				loader.setController(controller);
				break;
			}
			
			//Service Employee Controllers
			case Service_Employee_Dashboard_Controller:{
				ServiceEmployeeDashboardController controller = new ServiceEmployeeDashboardController();
				loader.setController(controller);
				break;
			}
			default:
				return null;
			}
		
		try {
			loader.load();
			centerScreen = loader.getRoot();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return centerScreen;
	}
	
	public void setScreenAfterLogout() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/LandingPage.fxml"));
			loader.setController(ClientApplication.landingPageController);
			Scene scene = new Scene(loader.load());
			Stage currentWindow = CurrentWindow.getCurrentWindow();
			currentWindow.setScene(scene);
			currentWindow.setTitle("GoNature Client - Landing Page");
	//		primaryStage.getIcons().add(new Image(GoNatureFinals.APP_ICON));
			currentWindow.setResizable(false);
			currentWindow.show();
			CurrentWindow.setCurrentWindow(currentWindow);
			Platform.runLater(()->ClientApplication.landingPageController.setScreenAfterLogout());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}
	
	public void setGuide(boolean isGuide) {
		this.isGuide=isGuide;
	}
	
}
