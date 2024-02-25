package components;

import java.sql.SQLException;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;

import database.QuoteCreationListener;
import database.Row;
import database.TableReceiver;
import service.DataManager;
import service.Permission;
import service.Toolkit;

public class NewQuoteButton extends Button {
	
	private int teacherId;
	
	private ComboBox<Row> teacherBox;
	
	public NewQuoteButton() {
		this(-1);
	}
	
	public NewQuoteButton(int id) {
		this.teacherId = id;
		setIcon(VaadinIcon.PLUS.create());
		addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
		
		if (!Permission.canWrite(DataManager.getPermissions())) {
			setEnabled(false);
			setTooltipText("Du bist nicht berechtigt, Zitate zu erstellen! Wenn du denkst, es handelt sich um einen Fehler, kontaktiere bitte einen Administrator.");
		}
		
		addClickListener(e -> {
			
			Dialog dialog = new Dialog();
			dialog.setWidth("90%");
			dialog.setMaxWidth("600px");
			
			dialog.setHeaderTitle("Neues Zitat" + ((teacherId > 0) ? ": " + teacherName(teacherId) : "" ));

			VerticalLayout dialogLayout = new VerticalLayout();
			dialogLayout.setPadding(false);
			dialog.add(dialogLayout);
			
			teacherBox = new ComboBox<Row>("Lehrer");
			if (teacherId == -1) {
				teacherBox.setItems(retrieveTeachers());
				teacherBox.setItemLabelGenerator(r -> (Toolkit.formatTeacherName(r.get("teachers_gender", String.class), r.get("teachers_name", String.class))));

				HorizontalLayout teacherLayout = new HorizontalLayout(teacherBox, new NewTeacherButton(this));
				teacherLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
				dialogLayout.add(teacherLayout);
			}
			
			
			Checkbox studentCheckbox = new Checkbox("Schülerzitat");
			if (id != -1)
				studentCheckbox.setEnabled(false);
			if (id == 79) 
				studentCheckbox.setValue(true);
			
			studentCheckbox.addValueChangeListener(valueChangedEvent -> {
					teacherBox.setEnabled(!valueChangedEvent.getValue());
			});
			dialogLayout.add(studentCheckbox);
			
			
			TextArea textArea = new TextArea();
	        textArea.setWidthFull();
	        textArea.setLabel("Zitat");
	        dialogLayout.add(textArea);
	        
	        HorizontalLayout specialChars = new HorizontalLayout();
	        textArea.setClassName("quote-input");
	        specialChars.add(specialCharacterButton("„", textArea));
	        specialChars.add(specialCharacterButton("“", textArea));
	        dialogLayout.add(specialChars);
			
			Button saveButton = new Button("Hochladen");
			saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
			saveButton.addClickListener(event ->{
				int tempTeacherId = teacherId;
				if (studentCheckbox.getValue() == true)
					tempTeacherId = 79;
				if ((tempTeacherId > 0 || teacherBox.getValue() != null) && !textArea.getValue().equals("")) {
					String text = textArea.getValue().replace("\n", "<br/>").replace("\"", "\\\"").replace("'", "\\'");
					createQuote(
							DataManager.getUserID(),
							text,
							(tempTeacherId > 0) ? tempTeacherId : teacherBox.getValue().get("teachers_id", Integer.class),
							"l"
					);
					dialog.close();
				}
				else {
					dialogLayout.addComponentAsFirst(new ErrorMessage("Fehlender Wert", "Du musst einen Lehrer und ein Zitat eingeben. Ansonsten funktioniert es nicht. Steht der Lehrer schon im Titel dieses Fensters, musst du ihn nicht manuell angeben."));
				}
			});
			Button cancelButton = new Button("Abbruch", event -> dialog.close());
			cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
			dialog.getFooter().add(cancelButton);
			dialog.getFooter().add(saveButton);
			
			dialog.open();
			
		});
	}
	
	private void createQuote(int user, String text, int originator, String category) {
		try {
			new TableReceiver().runUpdate(
					String.format(
							"INSERT INTO quotes (quotes_user, quotes_text, quotes_originator, quotes_category, quotes_published) VALUES (%d, '%s', %d, '%s', now())",
							user,
							text,
							originator,
							category));
			onCreate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String teacherName(int id) {
		try {
			return new TableReceiver().runQueryAndGetSingleValue("SELECT CONCAT(IF (teachers_gender LIKE 'm', 'Herr ', 'Frau '), teachers_name) FROM teachers WHERE teachers_id = " + id, String.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Fehlercode 101";
	}
	
	private Row[] retrieveTeachers() {
		try {
			return new TableReceiver().runQueryAndGet("SELECT * FROM teachers ORDER BY teachers_name").getRows();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private QuoteCreationListener listener;
	
	public void setQuoteCreationListener(QuoteCreationListener listener) {
		this.listener = listener;
	}
	
	private void onCreate() {
		if (listener != null)
			listener.onCreate();
	}
	
	private Button specialCharacterButton(String c, TextArea area) {
		Button button = new Button(c);
		button.addClickListener(e -> {
			UI.getCurrent().getPage().executeJs("window.insertAtCursor(document.getElementsByClassName(\"quote-input\")[0].children[2], \"" + c + "\")");
		});
		return button;
	}
	
	void updateTeachers() {
		teacherBox.setItems(retrieveTeachers());
	}
	
	void setChosenTeacher() {
		//TODO
	}

}
