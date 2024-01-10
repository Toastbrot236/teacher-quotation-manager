package service;

public class Toolkit {
	
	public static String formatTeacherName(String gender, String name) {
		return 
				switch(gender) {
				case "m" -> "Herr ";
				case "w" -> "Frau ";
				case "s" -> "";
				default -> "";
				}
		+ name;
	}

}
