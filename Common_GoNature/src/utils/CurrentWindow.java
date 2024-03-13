package utils;

import javafx.stage.Stage;

public class CurrentWindow {
	private static Stage currentWindow;
	
	public static void setCurrentWindow(Stage stage) {
		currentWindow=stage;
	}
	
	public static Stage getCurrentWindow() {
		return currentWindow;
	}

}
