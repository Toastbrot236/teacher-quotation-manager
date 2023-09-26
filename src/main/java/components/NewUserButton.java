package components;

import java.sql.SQLException;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import database.TableReceiver;
import service.DataManager;
import service.Permission;

public class NewUserButton extends Button {
	
	public NewUserButton() {
		
		setText("Neu");
		setIcon(VaadinIcon.PLUS.create());
		addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
		
		if (!DataManager.isAdmin()) {
			setEnabled(false);
			setTooltipText("Du bist nicht berechtigt, Nutzer zu erstellen! Wenn du denkst, es handelt sich um einen Fehler, kontaktiere bitte einen Administrator.");
			return;
		}
		
		addClickListener(e -> {
			
			Dialog dialog = new Dialog();
			dialog.setWidth("90%");
			dialog.setMaxWidth("600px");
			
			dialog.setHeaderTitle("Neuer Benutzer");

			VerticalLayout dialogLayout = new VerticalLayout();
			dialogLayout.setPadding(false);
			dialog.add(dialogLayout);
			
			TextField fNameField = new TextField("Vorname");
			dialog.add(fNameField);
			
			TextField nameField = new TextField("Nachname");
			dialog.add(nameField);
			
			Button saveButton = new Button("Erstellen");
			saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
			saveButton.addClickListener(event ->{
				if (!fNameField.getValue().equals("") && !nameField.getValue().equals("")) {
					
					try {
						new TableReceiver().runUpdate(
								String.format(
										"INSERT INTO user (user_firstName, user_lastName, user_username, user_password) VALUES (\"%s\", \"%s\", \"%s\", \"%s\");"
										, fNameField.getValue()
										, nameField.getValue()
										, fNameField.getValue().substring(0, 3).toLowerCase() + nameField.getValue().substring(0, 3).toLowerCase()
										, "123"
										)
								);
						dialog.close();
						UI.getCurrent().getPage().reload();
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				}
				else {
					dialog.addComponentAsFirst(new ErrorMessage("Fehlender Wert", "Stelle sicher, dass du einen Vor- und Nachnamen angegeben hast."));
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
