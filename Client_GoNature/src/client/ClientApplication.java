package client;

import gui.controller.CommonLandingPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientApplication extends Application {
	public static ClientMainControl client;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/LandingPage.fxml"));
			CommonLandingPageController landingPageController = new CommonLandingPageController();
			loader.setController(landingPageController);
			landingPageController.setStage(primaryStage);
			loader.load();
			Parent p = loader.getRoot();
			primaryStage.setTitle("GoNature Client - Landing Page");
	//		primaryStage.getIcons().add(new Image(GoNatureFinals.APP_ICON));
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
