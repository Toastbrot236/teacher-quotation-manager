package schulmanager.components;

import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.vaadin.addons.tatu.ColorPicker;
import org.vaadin.addons.tatu.ColorPicker.CaptionMode;
import org.vaadin.addons.tatu.ColorPicker.ColorPreset;
import org.vaadin.addons.tatu.ColorPickerVariant;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import database.TableReceiver;
import elemental.json.Json;
import service.CalendarCalc;
import service.DataManager;

@CssImport(value = "themes/zitate-sammlung/schulmanager.css")
public class LessonBox extends VerticalLayout implements CalendarCalc {
	
	private Timetable timetable;
	private boolean isOwnTimetable;
	
	private JsonObject lesson;
	
	private String html;
	private String subjectColor, lessonColor;
	private boolean hasSubjectColor = true;
	private boolean hasLessonColor = true;
	private String comment, subjectLabel;
	private JsonObject originalLesson;
	
	public LessonBox(JsonObject lesson, Timetable timetable) {
		this(lesson, null, null, timetable, true);
		//this(lesson, "#630d24");
	}
	
	public LessonBox(JsonObject lesson, HashMap<String, String> subjectColors, String[][] lessonColors, Timetable timetable, boolean isOwnTimetable) {
		this.timetable = timetable;
		this.isOwnTimetable = isOwnTimetable;
		this.lesson = lesson;

		setMargin(false);
		setPadding(false);
		setSpacing(false);
		
		//this.setWidthFull();
		
		String date = lesson.get("date").getAsString();
		JsonElement temp = lesson.get("comment");
		comment = null;
		if (temp != null && !temp.isJsonNull())
			comment = temp.getAsString();
		int classHour = lesson.get("classHour").getAsJsonObject().get("number").getAsInt();
		boolean cancelled = false;
		boolean substitution = false;
		originalLesson = null;
		JsonObject substitutionLesson = null;
		
		if(lesson.get("isCancelled") != null && !lesson.get("isCancelled").isJsonNull()) {
			cancelled = true;
			originalLesson = lesson.getAsJsonArray("originalLessons").get(0).getAsJsonObject();
		}
		else if (lesson.get("isSubstitution") != null && !lesson.get("isSubstitution").isJsonNull()) {
			substitution = true;
			originalLesson = lesson.getAsJsonArray("originalLessons").get(0).getAsJsonObject();
			substitutionLesson = lesson.getAsJsonObject("actualLesson");
		}
		else 
			originalLesson = lesson.getAsJsonObject("actualLesson");

		//For students having French at RWG, room and teacher can be null
		JsonElement roomElement = originalLesson.get("room");
		JsonObject room;
		if (roomElement.isJsonNull()) {
			room = new Gson().fromJson("{\"id\":-1,\"name\":\"RWG\"}", JsonObject.class);
		} else {
			room = originalLesson.getAsJsonObject("room");
		}
		
		JsonArray teacherArray = originalLesson.getAsJsonArray("teachers");
		JsonObject teacher;
		if (teacherArray.size() == 0) {
			teacher = new Gson().fromJson("{\"id\":-1,\"abbreviation\":\"Rdrm\",\"firstname\":\"Maximilian\",\"lastname\":\"Radermacher\"}", JsonObject.class);
		} else {
			teacher = originalLesson.getAsJsonArray("teachers").get(0).getAsJsonObject();
		}
		
		JsonObject subject = originalLesson.getAsJsonObject("subject");
		
		subjectLabel = originalLesson.get("subjectLabel").getAsString();
		
		
		//--------Color handling----------
		String subjectColor = subjectColors.get(subjectLabel);
		String lessonColor = lessonColors[fromString(date).getDayOfWeek().getValue()-1][classHour-1];
				
		this.subjectColor = subjectColor;
		this.lessonColor = lessonColor;
		
		if (subjectColor == null) {
			hasSubjectColor = false;
			subjectColor = "#f7f3a1";
			this.subjectColor = subjectColor;
		}
		if (lessonColor == null) {
			hasLessonColor = false;
		}
		
		
		
		if (!substitution) {
			html = String.format(
				"<div class=\"timetable box %s%s%s\" style=\"--dark: %s; --light: %s; background-color: var(--light); color: var(--dark); width: 100%%\">\r\n"
				+ "    <div class=\"vertical-bar\" style=\"background-color: var(--dark)\"></div>\r\n"
				+ "    <span><b>%s</b></span>\r\n"
				+ "    <span class=\"bottom\">%s</span>\r\n"
				+ "    <span>%s</span>\r\n"
				+ "  </div>",
				subjectLabel,
				cancelled ? " cancelled" : "",
				(comment != null) ? " additional-info" : "",
						
				/*HTML*/
				darker(getColor()),
				getColor(),
				/**/
						
				subjectLabel,
				teacher.get("abbreviation").getAsString(),
				room.get("name").getAsString()
				);
			
			Html htmlBox = new Html(html);
			htmlBox.getStyle().set("--dark", darker(getColor()));
			htmlBox.getStyle().set("--light", getColor());
			add(htmlBox);
		}
		else {
			html = String.format(
					"<div class=\"timetable box %s%s\" style=\"--dark: %s; --light: %s; background-color: var(--light); color: var(--dark); width: 100%%\">\r\n"
					+ "    <div class=\"vertical-bar\" style=\"background-color: var(--dark)\"></div>\r\n"
					+ "    <div>\r\n"
					+ "      <span class=\"subst\" style=\"background-color: var(--dark); color: var(--light);\"><b>%s</b></span>\r\n"
					+ "      <span class=\"origi\"><b>%s</b></span>\r\n"
					+ "    </div>\r\n"
					+ "    <div>\r\n"
					+ "      <span class=\"bottom subst\" style=\"background-color: var(--dark); color: var(--light);\">%s</span>\r\n"
					+ "      <span class=\"bottom origi\">%s</span>\r\n"
					+ "    </div>\r\n"
					+ "    <div>\r\n"
					+ "      <span class=\"subst\" style=\"background-color: var(--dark); color: var(--light);\">%s</span>\r\n"
					+ "      <span class=\"origi\">%s</span>\r\n"
					+ "    </div>\r\n"
					+ "  </div>",
					subjectLabel, //Class name of Box
					(comment != null) ? " additional-info" : "",
							
					/*HTML*/
					darker(getColor()),
					getColor(),
					/**/
					
					substitutionLesson.get("subjectLabel").getAsString(),
					subjectLabel,
					substitutionLesson.getAsJsonArray("teachers").get(0).getAsJsonObject().get("abbreviation").getAsString(),
					teacher.get("abbreviation").getAsString(),
					substitutionLesson.getAsJsonObject("room").get("name").getAsString(),
					room.get("name").getAsString()
					);
			
			Html htmlBox = new Html(html);
			htmlBox.getStyle().set("--dark", darker(getColor()));
			htmlBox.getStyle().set("--light", getColor());
			add(htmlBox);
		}

		//Details dialog
		this.addClickListener(event -> {
			Dialog details = new Dialog();
			details.setWidth("80%");
			details.setMaxWidth("700px");
			
			VerticalLayout layout = new VerticalLayout();
			layout.setSpacing(false);
			layout.setPadding(false);
			
			layout.add(new Span(toBeautifulString(fromString(date)) + ", " + this.getLessonTime(classHour)[0] + "-" + this.getLessonTime(classHour)[1]));
			
			layout.add(new H3(subject.get("name").getAsString()));
			
			if (comment != null) {
				layout.add(new Html("<h4 style=\"color: var(--lumo-primary-color);\"><i>" + comment + "</i></h4>"));
			}
			
			layout.add(createDetailSpan("Raum", room.get("name").getAsString()));
			layout.add(createDetailSpan("Lehrer", teacher.get("firstname").getAsString() + " " + teacher.get("lastname").getAsString()));
			layout.add(createDetailSpan("Klasse", originalLesson.getAsJsonArray("classes").get(0).getAsJsonObject().get("name").getAsString()));
			
			layout.add(new Hr());
			
			
			if (DataManager.accountsConnected() && isOwnTimetable) {
				///-----------------Adding a color picker----------------
				ColorPicker colorPicker = new ColorPicker();
				
				//Option true = Use for all boxes of same subject
				//Option false = Use for only one this lesson box
				RadioButtonGroup<Boolean> colorOptions = new RadioButtonGroup<>();
				colorPicker.setHelperText("Farbe");
				colorOptions.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
				colorOptions.setRenderer(new ComponentRenderer<>(
		                i -> {
							if (i)
								return new Span("Farbe für das Fach " + subjectLabel + " setzen");
							else
								return new Span("Farbe für speziell diese Unterrichtsstunde setzen");
		                }));
				colorOptions.setItems(true, false);
				colorOptions.setValue(!hasLessonColor);
				colorOptions.addValueChangeListener(e -> {
					colorPicker.setValue(e.getValue() ? this.subjectColor : this.lessonColor);
				});
				colorPicker.setPresets(colorPresets());
				layout.add(colorOptions);

				colorPicker.addThemeVariants(ColorPickerVariant.LUMO_SMALL);
				colorPicker.setValue(getColor());
				Button saveButton = new Button("Speichern");
				saveButton.addClickListener(e -> {
					saveColor(colorPicker.getValue(), colorOptions.getValue(), classHour);
					details.close();
				});
				saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
				layout.add(colorPicker, saveButton);
			}
			else if (isOwnTimetable){
				layout.add(new Span("Du musst erst deine Accounts verknüpfen, um Farben zu ändern. Dafür findest du unten einen Button."));
			}
			else {
				layout.add(new Span("Du kannst die Farbgebung fremder Stundenpläne nicht verändern. Sonst wärst du ein richtiger Rabauke!"));
			}
			
			details.add(layout);
			details.open();
		});
	}
	
	private Span createDetailSpan(String detailName, String value) {
		return new Span(detailName + ": " + value);
	}

	
	public String getHtml() {
		return html;
	}
	
	public String darker(String color) {
		Color c = Color.decode(color);
		if (c.getRed() + c.getGreen() + c.getBlue() > 200) 
			c = c.darker().darker();
		else
			c = c.brighter().brighter().brighter();
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		
		return "#" 
				+ ((r<16)?"0":"") + Integer.toHexString(r) 
				+ ((g<16)?"0":"") + Integer.toHexString(g) 
				+ ((b<16)?"0":"") + Integer.toHexString(b);
	}
	
	public String getColor() {
		if (lessonColor == null)
			return subjectColor;
		return lessonColor;
	}
	
	private void saveColor(String newColor, boolean isSubjectColor, int classHour) {
		if (newColor == null) return;
		try {
		
			if (!isSubjectColor) {
			//Is Lesson Color:
				int dayOfWeek = fromString(lesson.get("date").getAsString()).getDayOfWeek().getValue();
				if (hasLessonColor) {
					new TableReceiver().runUpdate(String.format("UPDATE "
							+ "smAbsoluteColors SET smColors_color = '%s' \r\n"
							+ "WHERE smColors_user = %d \r\n"
							+ "AND smColors_hour = %d \r\n"
							+ "AND smColors_day = %d;",
							newColor,
							DataManager.getUserID(),
							classHour,
							dayOfWeek
							));
				}
				else {
					new TableReceiver().runUpdate(String.format("INSERT INTO "
							+ "smAbsoluteColors (smColors_user, smColors_hour, smColors_day, smColors_color) "
							+ "VALUES (%d, %d, %d, '%s');",
							DataManager.getUserID(),
							classHour,
							dayOfWeek,
							newColor
							));
				}
			}
			else {
			//Is Subject color:
				int dayOfWeek = fromString(lesson.get("date").getAsString()).getDayOfWeek().getValue();
				new TableReceiver().runUpdate(String.format("DELETE FROM "
						+ "smAbsoluteColors "
						+ "WHERE smColors_user = %d\r\n "
						+ "AND smColors_hour = %d\r\n "
						+ "AND smColors_day = %d;",
						DataManager.getUserID(),
						classHour,
						dayOfWeek
						));
				if (hasSubjectColor) {
					new TableReceiver().runUpdate(String.format("UPDATE "
							+ "smColors SET smColors_color = '%s' \r\n"
							+ "WHERE smColors_user = %d\r\n "
							+ "AND smColors_subject = '%s';",
							newColor,
							DataManager.getUserID(),
							subjectLabel
							));
				}
				else {
					new TableReceiver().runUpdate(String.format("INSERT INTO "
							+ "smColors (smColors_user, smColors_subject, smColors_color) "
							+ "VALUES (%d, '%s', '%s');",
							DataManager.getUserID(),
							subjectLabel,
							newColor
							));
				}
			}
			
			timetable.reload();
		
		}
		catch (SQLException e) {
			e.printStackTrace();
			Notification.show("Fehler beim Versuch, neue Farbwerte in der Datenbank zu speichern.");
		}
	}
	
	private List<ColorPreset> colorPresets() {
		return Arrays.asList(
				new ColorPreset("#f28585", "Himbeerhauch"),
                new ColorPreset("#ffa447", "Sonnenkuss"),
                new ColorPreset("#fffC9b", "Vanilletraum"),
				new ColorPreset("#b7e5b4", "Limettenlicht"));
	}

}
