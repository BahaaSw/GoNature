package utils;

import javafx.stage.Stage;

public class CurrentWindow {
	private static Stage currentWindow;
	private static Object lock= new Object();
	
	public static void setCurrentWindow(Stage stage) {
		synchronized(lock){
			currentWindow=stage;
		}
	}
	
	public static Stage getCurrentWindow() {
		synchronized(lock) {
			return currentWindow;
		}
	}

}
