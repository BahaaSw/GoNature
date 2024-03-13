package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CurrentDate {
	
	public static String getCurrentDate(String pattern) {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String formattedDate = currentDate.format(formatter);
		return currentDate.format(formatter);
	}

}
