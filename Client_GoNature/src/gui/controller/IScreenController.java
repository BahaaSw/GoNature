package gui.controller;

/**
 * Interface for defining common functionalities of screen controllers.
 */
public interface IScreenController {

	/**
	 * Method called when the logout button is clicked.
	 */
	void onLogoutClicked();

	/**
	 * Method called when the server crashes.
	 */
	void onServerCrashed();

//	void onCloseApplication();
}
