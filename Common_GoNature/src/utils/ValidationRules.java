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
			@SuppressWarnings("unused")
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
	
	public static boolean isValidName(String name) {
		String namePattern="^[a-zA-Z]+$";
		return Pattern.matches(namePattern, name);
	}
	
	public static boolean isFieldEmpty(String field) {
		return field.equals("");
	}
	
	public static boolean isValidPassword(String password) {
		String passwordPattern = "\\d{6,}";
		return Pattern.matches(passwordPattern, password);
	}
	
	public static boolean isValidUsername(String username) {
		String usernamePattern="^[a-zA-Z0-9]+$";
		return Pattern.matches(usernamePattern, username);
	}
	
	public static boolean isValidId(String id) {
		return isNumeric(id);
	}
	
	public static boolean isValidIsraeliId(String id) {
        // Pad the ID to 9 digits
        String paddedId = String.format("%9s", id).replace(' ', '0');
        
        // Check if the padded ID is exactly 9 digits and all are numbers
        if (!paddedId.matches("\\d{9}")) {
            return false;
        }

        int sum = 0;
        // Process each digit
        for (int i = 0; i < 9; i++) {
            // Convert char to int ('0' character has an int value of 48)
            int digit = paddedId.charAt(i) - '0';

            // Even positions (from human perspective, odd indexes in zero-based indexing) are multiplied by 2
            int product = (i % 2 == 0) ? digit : digit * 2;

            // If product is 10 or more, sum its digits (equivalent to subtracting 9 for numbers 10-18)
            sum += (product > 9) ? product - 9 : product;
        }

        // The ID is valid if the sum is divisible by 10
        return sum % 10 == 0;
	}
	
	public static boolean isNumeric(String str) {
		String numericPattern="^[0-9]+$";
		return Pattern.matches(numericPattern, str);
	}
	
	public static boolean isPositiveNumeric(String number) {
		if(isNumeric(number))
			return Integer.parseInt(number)>0;
		return false;
	}
	
	
}
