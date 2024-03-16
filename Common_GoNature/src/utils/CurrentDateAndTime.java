package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentDateAndTime {
	
//	"'Today' yyyy-MM-dd"
	public static String getCurrentDate(String pattern) {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String formattedDate = currentDate.format(formatter);
		return currentDate.format(formatter);
	}
	
	// "yyyy-MM-dd hh:mm"
	public static String getCurrentDateAndTime(String pattern) {
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String formattedDate = currentDate.format(formatter);
		return currentDate.format(formatter);
	}
	
//	public static String getDateAndTimeFormatted(String pattern) {
//		LocalDateTime formatted = n;
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
//		String formattedDate = formatted.format(formatter);
//		return formattedDate;
//	}

}
