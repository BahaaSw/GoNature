package gui.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.GoNatureServer;

/**
 * 
 * @Author NadavReubens
 * @Author Gal Bitton
 * @Author Tamer Amer
 * @Author Rabea Lahham
 * @Author Bahaldeen Swied
 * @Author Ron Sisso
 * @version 1.0.0 
 */

/**
 * This is the main application class, which extends the javaFX application class.
 */
public class ServerGuiView extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerGui.fxml"));
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
