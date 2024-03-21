package logic;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GuideInTable {
	private Guide guide;
	private final SimpleStringProperty userId;
	private final SimpleStringProperty username;
	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;
	private final SimpleStringProperty email;
	private final SimpleStringProperty phone;
	private final SimpleStringProperty status; // This will hold the selected status

	public GuideInTable(String userId, String username, String firstName, String lastName, String email, String phone,
			String status) {
		this.userId = new SimpleStringProperty(userId);
		this.username = new SimpleStringProperty(username);
		this.firstName=new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.email = new SimpleStringProperty(email);
		this.phone = new SimpleStringProperty(phone);
		this.status = new SimpleStringProperty(status);
	}

	// Getters and setters
	public String getUserId() {
		return userId.get();
	}

	public String getUsername() {
		return username.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getPhone() {
		return phone.get();
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public String getFirstName() {
		return firstName.get();
	}

	public String getLastName() {
		return lastName.get();
	}
	
	public void setGuide(Guide guide) {
		this.guide=guide;
	}
	
	public Guide getGuide() {
		return guide;
	}
}
