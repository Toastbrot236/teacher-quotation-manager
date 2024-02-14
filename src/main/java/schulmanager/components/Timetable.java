package schulmanager.components;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import service.CalendarCalc;
import service.ColorReceiver;

public class Timetable extends HorizontalLayout implements CalendarCalc {
	
	/**
	 * lessons[day][hour]: First index reanges from 0 (Mon) to 4 (Fri), second index ranges from 0 (1st lesson) to 11 (12th lesson)
	 */
	private JsonObject[][] lessons;
	private String[][] lessonColors;
	private HashMap<String, String> subjectColors;
	
	private LocalDate monday;
	
	private int userID;
	
	private boolean isOwnTimetable;
	
	private String html;
	
	public Timetable(JsonObject[] allLessons, LocalDate monday, boolean isOwnTimetable, int userID) {
		
		html = "<div style=\"display: flex; flex-direction: row; width: 100%; justify-content: space-between\">";
		
		this.monday = monday;
		this.isOwnTimetable = isOwnTimetable;
		this.userID = userID;
		
		//General layout adjustments
		setWidthFull();
		setMaxWidth("700px");
		this.setAlignItems(Alignment.STRETCH);
		
		//Creating the lessons
		lessons = new JsonObject[5][12];
		for (int i = 0; i < allLessons.length; i++) {
			int dayOfWeek = fromString(allLessons[i].get("date").getAsString()).getDayOfWeek().getValue()-1;
			int hour = allLessons[i].get("classHour").getAsJsonObject().get("number").getAsInt()-1;
			lessons[dayOfWeek][hour] = allLessons[i];
		}
		
		//Receiving the colors
		lessonColors = ColorReceiver.getLessonColors(userID);
		subjectColors = ColorReceiver.getSubjectColors(userID);
		
		//Adding the timetable
		add(hourColumn());
		for (int i = 0; i < 5; i++) {
			add(dayColumn(lessons[i], monday.plusDays(i)));
		}
		
		html += "</div>";
		//System.out.println("START:\n" + html);
		
	}
	
	private Div dayColumn(JsonObject[] lessons, LocalDate day) {
		Div column = new Div();
		column.addClassName("vertical-container");
		column.addClassName("timetable");
		column.setWidthFull();
		
		
		/*HTML*/
		html += String.format(
					"""
				    <div class="vertical-container timetable">
						<div class="centered-element" style="color: darkgrey">
							<span style="font-size: 20px">%s</span>
						</div>
						<div class="centered-element" style="color: darkgrey">
							<span style="font-size: 14px">%s</span>
						</div>
					""",
					String.valueOf(day.getDayOfMonth()),
					String.valueOf(getDayName(day))
				);
		/**/
		
		//Display for Day Number ( 1 to 31)
		Div dayNumberContainer = new Div();
		dayNumberContainer.addClassName("centered-element");
		if (LocalDate.now().equals(day))
			dayNumberContainer.getStyle().setColor("var(--lumo-primary-color)");
		else 
			dayNumberContainer.getStyle().setColor("darkgrey");
		
		Span dayNumberSpan = new Span(String.valueOf(day.getDayOfMonth()));
		dayNumberSpan.getStyle().set("font-size", "20px");
		dayNumberContainer.add(dayNumberSpan);
		
		column.add(dayNumberContainer);
		
		//Display for Day Name (MO to FR)
		Div dayNameContainer = new Div();
		dayNameContainer.addClassName("centered-element");
		if (LocalDate.now().equals(day))
			dayNameContainer.getStyle().setColor("var(--lumo-primary-color)");
		else 
			dayNameContainer.getStyle().setColor("darkgrey");
		
		Span dayNameSpan = new Span(String.valueOf(getDayName(day)));
		dayNameSpan.getStyle().set("font-size", "14px");
		dayNameContainer.add(dayNameSpan);
		column.add(dayNameContainer);
		
		for (int i = 0; i < lessons.length; i++) {
			if (lessons[i] == null) {
				Div freeDiv = new Div();
				freeDiv.addClassName("free");
				column.add(freeDiv);
				/*HTML*/
				html += "<div class=\"free\"/></div>";
				/**/
			}
			else {
				LessonBox box = new LessonBox(
						lessons[i],
						subjectColors,
						lessonColors,
						this,
						isOwnTimetable);
				column.add(box);
				/*HTML*/
				html += box.getHtml();
				/*HTML*/
			}
			
			if(i % 2 == 1 && i < 11) {
				Div breakDiv = new Div();
				breakDiv.addClassName("break");
				column.add(breakDiv);
				/*HTML*/
				html += "<div class=\"break\"></div>";
				/**/
			}		
		}
		
		/*HTML*/
		html += "</div>";
		/**/

		return column;
	}
	
	private Html hourColumn() {
		String html = "<div style=\"margin-top:17px\">" 
				+ "<div class=\"vertical-container timetable\" style=\"margin-bottom:19px\">\r\n"
				+ "  <div class=\"centered-element vertical-container\" style=\"color:darkgrey\">\r\n"
				+ "    <span style=\"font-size:14px;\">" + getMonthName(monday) + "</span>\r\n"
				+ "</div></div>";
		for (int i = 1; i <= 12; i++) {
			html += String.format("<div class=\"vertical-container timetable\" style=\"margin-bottom:19px\">\r\n"
					+ "  <div class=\"centered-element vertical-container\" style=\"color:darkgrey\">\r\n"
					+ "    <span style=\"font-size:9px;\">%s</span>\r\n"
					+ "    <span style=\"font-size:18px\">%d</span>\r\n"
					+ "    <span style=\"font-size:9px\">%s</span>\r\n"
					+ "  </div>\r\n"
					+ "</div>",
					getLessonTime(i)[0],
					i,
					getLessonTime(i)[1]
					);
			if (i % 2 == 0 && i < 12) {
				html += "<div class=\"break\" style=\"margin-bottom:11px\"></div>";
			}
		}
		
		/*HTML*/
		this.html += html + "</div>";
		/*HTML*/
		
		return new Html(html + "</div>");
	}
	
	public void reload() {
		this.removeAll();
		add(hourColumn());
		
		//Receiving the colors
		lessonColors = ColorReceiver.getLessonColors(userID);
		subjectColors = ColorReceiver.getSubjectColors(userID);
		
		for (int i = 0; i < 5; i++) {
			add(dayColumn(lessons[i], monday.plusDays(i)));
		}
	}
	
	public void changeValues(JsonObject[] allLessons, LocalDate monday, boolean forward, int userID) {
		
		this.userID = userID;
		this.monday = monday;
		
		//addClassName(forward ? "sliding-right1" : "sliding-right2r");
		//getStyle().set("transform", "translateX(100%)");
		
		this.removeAll();
		
		//Creating the lessons
		lessons = new JsonObject[5][12];
		for (int i = 0; i < allLessons.length; i++) {
			int dayOfWeek = fromString(allLessons[i].get("date").getAsString()).getDayOfWeek().getValue()-1;
			int hour = allLessons[i].get("classHour").getAsJsonObject().get("number").getAsInt()-1;
			lessons[dayOfWeek][hour] = allLessons[i];
		}
		
		//Receiving the colors
		lessonColors = ColorReceiver.getLessonColors(userID);
		subjectColors = ColorReceiver.getSubjectColors(userID);
		
		//Adding the timetable
		add(hourColumn());
		for (int i = 0; i < 5; i++) {
			add(dayColumn(lessons[i], monday.plusDays(i)));
		}
		
		//getStyle().set("transform", "translateX(-100%)");
		//removeClassName(forward ? "sliding-right1" : "sliding-right2r");
		/*getStyle().set("transform", "translateX(0)");
		addClassName(forward ? "sliding-right2" : "sliding-right1r");
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		removeClassName(forward ? "sliding-right2" : "sliding-right1r");*/
		
	}
	
	public void setIsOwnTimetable(boolean isOwnTimetable) {
		this.isOwnTimetable = isOwnTimetable;
	}
	
	public String getHtml() {
		return html;
	}

}
