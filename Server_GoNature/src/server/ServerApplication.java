package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ServerApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/ServerScreen.fxml"));
		AnchorPane root = fxmlLoader.load();
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		// disconnect before closing waiting to run on main javaFX thread.
		primaryStage.setOnCloseRequest(e -> Platform.runLater(()->GoNatureServer.stopServer()));
		primaryStage.setTitle("GoNature - Parks&Vacations - Server Side");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
