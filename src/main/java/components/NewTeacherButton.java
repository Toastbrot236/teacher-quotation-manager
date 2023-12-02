package components;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;

import database.Row;
import database.TableReceiver;
import service.DataManager;
import service.Permission;

public class NewTeacherButton extends Button {
	
	private NewQuoteButton parent;
	
	public NewTeacherButton() {
		this(null);
	}
	
	public NewTeacherButton(NewQuoteButton parent) {
		this.parent = parent;
		
		setIcon(VaadinIcon.PLUS.create());
		addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
		
		if (!Permission.canWrite(DataManager.getPermissions())) {
			setEnabled(false);
			setTooltipText("Du bist nicht berechtigt, Lehrer zu erstellen! Wenn du denkst, es handelt sich um einen Fehler, kontaktiere bitte einen Administrator.");
			return;
		}
		
		addClickListener(e -> {
			
			Dialog dialog = new Dialog();
			dialog.setWidth("90%");
			dialog.setMaxWidth("600px");
			
			dialog.setHeaderTitle("Neuer Lehrer");

			VerticalLayout dialogLayout = new VerticalLayout();
			dialogLayout.setPadding(false);
			dialog.add(dialogLayout);
			
			ComboBox<String> genderBox = new ComboBox<String>("Anrede");
			genderBox.setItems("m", "w");
			genderBox.setItemLabelGenerator(s -> s.equals("m") ? "Herr" : "Frau");
			dialog.add(genderBox);
			
			TextField nameField = new TextField("Nachname");
			nameField.setHelperText("Falls es noch einen Lehrer mit gleichem Nachnamen gibt, den Anfangsbuchstaben des Vornamens nachstellen. Z. B.: Rauwolf, C");
			dialog.add(nameField);
			
			Button saveButton = new Button("Erstellen");
			saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
			saveButton.addClickListener(event ->{
				if (genderBox.getValue() != null && !nameField.getValue().equals("")) {
					
					try {
						new TableReceiver().runUpdate(
								String.format(
										"INSERT INTO teachers (teachers_gender, teachers_name) VALUES ('%s', '%s');"
										, genderBox.getValue()
										, nameField.getValue()
										)
								);
						dialog.close();
						if (parent != null)
							parent.updateTeachers();
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				}
				else {
					dialog.addComponentAsFirst(new ErrorMessage("Fehlender Wert", "Stelle sicher, dass du eine Anrede und einen Namen angegeben hast."));
				}
			});
			Button cancelButton = new Button("Abbruch", event -> dialog.close());
			cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
			dialog.getFooter().add(cancelButton);
			dialog.getFooter().add(saveButton);
			
			dialog.open();
			
		});
	}
	
}