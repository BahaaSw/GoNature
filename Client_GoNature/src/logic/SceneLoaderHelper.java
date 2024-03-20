package logic;

import java.io.IOException;

import client.ClientApplication;
import gui.controller.AddNewGuideScreenController;
import gui.controller.OrderSummaryScreenController;
import gui.controller.CreateReportsScreenController;
import gui.controller.CustomerHomepageScreenController;
import gui.controller.EmployeeHomepageScreenController;
import gui.controller.RescheduleOrderScreenController;
import gui.controller.HandleOrderScreenController;
import gui.controller.IThreadController;
import gui.controller.IdenticationScreenController;
import gui.controller.MakeOrderScreenController;
import gui.controller.ManageGuidesScreenController;
import gui.controller.ParkAvailableSpotsScreenController;
import gui.controller.ParkEntranceScreenController;
import gui.controller.ParkSettingsScreenController;
import gui.controller.PaymentReceiptScreenController;
import gui.controller.RequestTableScreenController;
import gui.controller.ViewReportsScreenController;
import gui.view.ApplicationViewType;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.CurrentWindow;

public class SceneLoaderHelper {
	
	private AnchorPane centerScreen;
	private IThreadController runningController=null;
	
	//UserType type
	public AnchorPane loadRightScreenToBorderPaneWithController(BorderPane screen, String screenUrl,ApplicationViewType viewToLoad,EntitiesContainer data) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(screenUrl));
		switch(viewToLoad) {
		
			case Add_New_Guide_Screen:{
				AddNewGuideScreenController controller = new AddNewGuideScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
				
			case Order_Summary_Screen:{
				OrderSummaryScreenController controller= new OrderSummaryScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case Create_Reports_Screen:{
				CreateReportsScreenController controller = new CreateReportsScreenController(data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case Customer_Homepage_Screen:{
				CustomerHomepageScreenController controller = new CustomerHomepageScreenController();
				loader.setController(controller);
				break;
			}
			case Employee_Homepage_Screen:{
				EmployeeHomepageScreenController controller = new EmployeeHomepageScreenController();
				loader.setController(controller);
				break;
			}

			case Reschedule_Order_Screen:{
				RescheduleOrderScreenController controller = new RescheduleOrderScreenController(screen,data.getEntity1());
				runningController = controller;
				loader.setController(controller);
				break;
			}
			
			case Handle_Order_Screen:{
				HandleOrderScreenController controller = new HandleOrderScreenController(screen,data.getEntity1(),data.getCustomerInterface());
				loader.setController(controller);
				break;
			}
			case Identication_Screen:{
				IdenticationScreenController controller = new IdenticationScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case Make_Order_Screen:{
				MakeOrderScreenController controller = new MakeOrderScreenController(screen,data.getEntity1(),data.getCustomerInterface());
				loader.setController(controller);
				break;
			}
			
			case Manage_Guides_Screen:{
				ManageGuidesScreenController controller = new ManageGuidesScreenController(screen);
				loader.setController(controller);
				break;
			}
			
			case Park_Available_Spots_Screen:{
				ParkAvailableSpotsScreenController controller = new ParkAvailableSpotsScreenController(data.getEntity1(),data.getEntity2());
				loader.setController(controller);
				break;
			}
			
			case Park_Entrance_Screen:{
				ParkEntranceScreenController controller = new ParkEntranceScreenController();
				loader.setController(controller);
				break;
			}
			
			case Park_Settings_Screen:{
				ParkSettingsScreenController controller = new ParkSettingsScreenController(data.getEntity1(),data.getEntity2());
				loader.setController(controller);
				break;
			}
			
			case Payment_Receipt_Screen:{
				PaymentReceiptScreenController controller = new PaymentReceiptScreenController(data.getEntity1(),data.getEntity2(),data.getEntity3());
				loader.setController(controller);
				break;
			}
			
			case Request_Table_Screen:{
				RequestTableScreenController controller = new RequestTableScreenController();
				loader.setController(controller);
				break;
			}
			
			case View_Reports_Screen:{
				ViewReportsScreenController controller = new ViewReportsScreenController(data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			default:
				return null;
			}
		
		try {
			if(runningController!=null) {
				runningController.cleanUp();
				runningController=null;
			}
			
			loader.load();
			centerScreen = loader.getRoot();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return centerScreen;
	}

	
	public void setScreenAfterLogoutOrBack() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/LandingPageScreen.fxml"));
			loader.setController(ClientApplication.landingPageController);
			Scene scene = new Scene(loader.load());
			Stage currentWindow = CurrentWindow.getCurrentWindow();
			currentWindow.setScene(scene);
			currentWindow.setTitle("GoNature Client - Landing Page");
	//		primaryStage.getIcons().add(new Image(GoNatureFinals.APP_ICON));
			currentWindow.setResizable(false);
			currentWindow.show();
			CurrentWindow.setCurrentWindow(currentWindow);
			ClientApplication.runningController=ClientApplication.landingPageController;
			Platform.runLater(()->ClientApplication.landingPageController.setScreenAfterLogout());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
