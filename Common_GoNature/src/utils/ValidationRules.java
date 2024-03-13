package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ValidationRules {

	public static boolean isValidIp(String ip) {
		try {
			if(ip.equals(""))
				return false;
			InetAddress inetAddress = InetAddress.getByName(ip);
			return true;
		}catch(UnknownHostException e) {
			return false;
		}
	}
	
	public static boolean isValidPort(String port) {
		try {
			int serverPort = Integer.parseInt(port);
			if(serverPort>=0 && serverPort<=65535)
				return true;
			return false;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isValidEmail(String email) {
		String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
		return Pattern.matches(emailPattern, email);
	}
	
	public static boolean isValidPhone(String phoneNumber) {
		String phonePattern = "\\d{10}";
		return Pattern.matches(phonePattern, phoneNumber);
	}
	
	public static boolean areFieldsEmpty(ArrayList<String> fieldsInput) {
		for(String input : fieldsInput) {
			if(input.equals(""))
				return true;
		}
		return false;
	}
	
	public static boolean isValidPassword(String password) {
		String passwordPattern = "\\d{8,}";
		return Pattern.matches(password, passwordPattern);
	}
	
	public static boolean isValidUsername(String username) {
		String usernamePattern="^[a-zA-Z0-9]+$";
		return Pattern.matches(username, usernamePattern);
	}
	
	
}
