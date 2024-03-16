package logic;

import java.io.IOException;

import client.ClientApplication;
import gui.controller.AddNewGuideScreenController;
import gui.controller.ConfirmMessageScreenController;
import gui.controller.CreateReportsScreenController;
import gui.controller.CustomerHomepageScreenController;
import gui.controller.EmployeeHomepageScreenController;
import gui.controller.FinishOrderScreenController;
import gui.controller.HandleOrderScreenController;
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
	
	//UserType type
	public AnchorPane loadRightScreenToBorderPaneWithController(BorderPane screen, String screenUrl,ApplicationViewType viewToLoad,EntitiesContainer data) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(screenUrl));
		switch(viewToLoad) {
		
			case AddNewGuideScreen:{
				AddNewGuideScreenController controller = new AddNewGuideScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
				
			case ConfirmMessageScreen:{
				ConfirmMessageScreenController controller= new ConfirmMessageScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case CreateReportsScreen:{
				CreateReportsScreenController controller = new CreateReportsScreenController(data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case CustomerHomgepageScreen:{
				CustomerHomepageScreenController controller = new CustomerHomepageScreenController();
				loader.setController(controller);
				break;
			}
			case EmployeeHomepageScreen:{
				EmployeeHomepageScreenController controller = new EmployeeHomepageScreenController();
				loader.setController(controller);
				break;
			}

			case FinishOrderScreen:{
				FinishOrderScreenController controller = new FinishOrderScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case HandleOrderScreen:{
				HandleOrderScreenController controller = new HandleOrderScreenController(screen,data.getEntity1(),data.getCustomerInterface());
				loader.setController(controller);
				break;
			}
			case IdenticationScreen:{
				IdenticationScreenController controller = new IdenticationScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case MakeOrderScreen:{
				MakeOrderScreenController controller = new MakeOrderScreenController(screen,data.getEntity1());
				loader.setController(controller);
				break;
			}
			
			case ManageGuidesScreen:{
				ManageGuidesScreenController controller = new ManageGuidesScreenController();
				loader.setController(controller);
				break;
			}
			
			case ParkAvailableSpotsScreen:{
				ParkAvailableSpotsScreenController controller = new ParkAvailableSpotsScreenController(data.getEntity1(),data.getEntity2());
				loader.setController(controller);
				break;
			}
			
			case ParkEntranceScreen:{
				ParkEntranceScreenController controller = new ParkEntranceScreenController();
				loader.setController(controller);
				break;
			}
			
			case ParkSettingsScreen:{
				ParkSettingsScreenController controller = new ParkSettingsScreenController(data.getEntity1(),data.getEntity2());
				loader.setController(controller);
				break;
			}
			
			case PaymentReceiptScreen:{
				PaymentReceiptScreenController controller = new PaymentReceiptScreenController(data.getEntity1(),data.getEntity2(),data.getEntity3());
				loader.setController(controller);
				break;
			}
			
			case RequestTableScreen:{
				RequestTableScreenController controller = new RequestTableScreenController();
				loader.setController(controller);
				break;
			}
			
			case ViewReportsScreen:{
				ViewReportsScreenController controller = new ViewReportsScreenController();
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

	
	public void setScreenAfterLogoutOrBack() {

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
	
}
