package client;

import gui.controller.IScreenController;
import gui.controller.LandingPageScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application {
	public static ClientMainControl client;
	public static IScreenController runningController;
	public static LandingPageScreenController landingPageController;
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/LandingPageScreen.fxml"));
			landingPageController = new LandingPageScreenController();
			loader.setController(landingPageController);
			landingPageController.setStage(primaryStage);
			runningController=landingPageController;
			loader.load();
			Parent p = loader.getRoot();
			primaryStage.setTitle("GoNature Client - Landing Page");
	//		primaryStage.getIcons().add(new Image(GoNatureFinals.APP_ICON));
//			primaryStage.setOnCloseRequest(null);
			primaryStage.setScene(new Scene(p));
			primaryStage.setResizable(false);
			primaryStage.show();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
