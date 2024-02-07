package service;

import java.util.HashMap;

import com.google.gson.JsonObject;

import database.Row;
import database.Table;
import database.TableReceiver;

public class ColorReceiver {
	
	public static String[][] getLessonColors(int userID) {
		String[][] colors = new String[5][12];
		
		try {
			Table result = new TableReceiver().runQueryAndGet("SELECT * FROM smAbsoluteColors WHERE smColors_user = " + userID);
			for (Row r : result.getRows()) {
				int hour     = r.get("smColors_hour", int.class);
				int day      = r.get("smColors_day", int.class);
				String color = r.get("smColors_color", String.class);
				
				colors[day-1][hour-1] = color;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return colors;
	}
	
	public static HashMap<String, String> getSubjectColors(int userID) {
		HashMap<String, String> colors = new HashMap<String, String>();
		
		try {
			Table result = new TableReceiver().runQueryAndGet("SELECT * FROM smColors WHERE smColors_user = " + userID);
			for (Row r : result.getRows()) {
				String subject  = r.get("smColors_subject", String.class);
				String color = r.get("smColors_color", String.class);
				
				colors.put(subject, color);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return colors;
	}

}
