package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface CalendarCalc {

	public default LocalDate getCurrentMonday() {
		LocalDate today = LocalDate.now();
		int day = today.getDayOfWeek().getValue()-1; //0 = Monday, ... 6 = Sunday
		return today.minusDays(day);
	}
	
	public default String toString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public default String toBeautifulString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern("dd. MM. yyyy"));
	}
	
	public default LocalDate fromString(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public default String getDayName(LocalDate date) {
		return switch (date.getDayOfWeek().getValue()) {
			case 1 -> "MO";
			case 2 -> "DI";
			case 3 -> "MI";
			case 4 -> "DO";
			case 5 -> "FR";
			case 6 -> "SA";
			case 7 -> "SO";
			default -> "?";
		};
	}
	
	public default String getMonthName(LocalDate date) {
        return switch (date.getMonthValue()) {
	        case 1 -> "Jan";
	        case 2 -> "Feb";
	        case 3 -> "Mär";
	        case 4 -> "Apr";
	        case 5 -> "Mai";
	        case 6 -> "Jun";
	        case 7 -> "Jul";
	        case 8 -> "Aug";
	        case 9 -> "Sep";
	        case 10 -> "Okt";
	        case 11 -> "Nov";
	        case 12 -> "Dez";
	        default -> "?";
        };
	}
	
	public default String[] getLessonTime(int hour) {
	    return switch (hour) {
	        case 1 -> new String[]{"08:00", "08:44"};
	        case 2 -> new String[]{"08:46", "09:30"};
	        case 3 -> new String[]{"09:45", "10:30"};
	        case 4 -> new String[]{"10:35", "11:20"};
	        case 5 -> new String[]{"11:35", "12:19"};
	        case 6 -> new String[]{"12:21", "13:05"};
	        case 7 -> new String[]{"13:35", "14:20"};
	        case 8 -> new String[]{"14:25", "15:10"};
	        case 9 -> new String[]{"15:20", "16:05"};
	        case 10 -> new String[]{"16:10", "16:55"};
	        case 11 -> new String[]{"17:00", "17:45"};
	        case 12 -> new String[]{"17:50", "18:20"};
	        default -> new String[]{"Urknall", "Apokalypse"};
	    };
	}
	
}
