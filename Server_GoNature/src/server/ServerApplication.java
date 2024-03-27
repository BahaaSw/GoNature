package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * ServerApplication extends the JavaFX Application class to create and manage the server's graphical user interface (GUI). 
 * It loads and displays the server-side UI from an FXML file and sets up the necessary UI properties, including the stage's scene, 
 * title, and resize behavior. It also ensures that the server is properly stopped when the application window is closed.
 * Authors: Nadav Reubens, Gal Bitton, Tamer Amer, Rabea Lahham, Bahaldeen Swied, Ron Sisso
 */
public class ServerApplication extends Application {
	
	/**
	 * Starts the primary stage of the server application, loading the GUI layout from an FXML file, setting the scene, and configuring 
	 * stage properties. It establishes the application window's behavior upon closure to ensure the server is stopped gracefully.
	 *
	 * @param primaryStage The primary stage for this application, onto which the application scene can be set. The primary stage is 
	 *                     created by the platform itself and passed to this method as an argument.
	 * @throws Exception if the FXML file cannot be loaded correctly, which could occur if the file is not found or if there's an issue 
	 *                   within the FXML file itself (such as a malformed structure or a failed controller instantiation).
	 */
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
	/**
	 * The entry point for the server application. This method is called when the application starts. It delegates to the JavaFX 
	 * Application.launch method to bootstrap the JavaFX application. This includes setting up the application's threading model 
	 * and creating the initial stage and scene.
	 *
	 * @param args The command line arguments passed to the application. Any arguments provided are forwarded to the launch method 
	 *             and can be retrieved by the application if needed.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
