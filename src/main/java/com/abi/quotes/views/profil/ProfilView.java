package com.abi.quotes.views.profil;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import components.ErrorMessage;
import components.NotLoggedInScreen;
import components.OptionField;
import database.TableReceiver;
import service.DataManager;

@PageTitle("Profil")
@Route(value = "profil", layout = MainLayout.class)
public class ProfilView extends VerticalLayout {

    private OptionField firstNameField;
    private OptionField lastNameField;
    private OptionField displayNameField;
    private OptionField passwordField;
    private OptionField emailField;
    private OptionField usernameField;
    private Span idLabel;

    public ProfilView() {
    	setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	add(new NotLoggedInScreen());
        } else {

        idLabel = new Span("Nutzer-ID: " + DataManager.getUserID());
        firstNameField = new OptionField("Vorname", DataManager.getFirstName(), null);
        lastNameField = new OptionField("Nachname", DataManager.getLastName(), null);
        displayNameField = new OptionField("Anzeigename", DataManager.getDisplayName(), "Hiermit kannst du einen alternativen Namen festlegen, mit dem du anderen angezeigt wirst. Dein Vorname sowie der Anfangbuchstabe deines Nachnamens bleiben dennoch ersichtlich.");
        passwordField = new OptionField("Passwort", DataManager.getPassword(), "WARNUNG: Dein Passwort kann unter Umständen von einem Administrator eingesehen werden! Verwende kein Passwort, das du auch auf anderen Webseiten benutzt!");
        emailField = new OptionField("E-Mail", DataManager.getEmail(), "Zurzeit wird deine Email-Adresse nicht benötigt, erlaubt aber zukünftig Kontowiederherstellungen bei vergessenem Passwort.");
        usernameField = new OptionField("Benutzername", DataManager.getUsername(), "WARNUNG: Dein Nutzername dient ausschließlich zum Einloggen. Ohne ihn kommst du nicht mehr in deinen Account.");

        usernameField.setValueSavedListener(this::updateValues);
        emailField.setValueSavedListener(this::updateValues);
        passwordField.setValueSavedListener(this::updateValues);
        firstNameField.setValueSavedListener(this::updateValues);
        lastNameField.setValueSavedListener(this::updateValues);
        displayNameField.setValueSavedListener(this::updateValues);
        
        idLabel.getStyle().set("transform", "translate(60px)");

        add(new H2("Dein Profil"), usernameField, emailField, passwordField, firstNameField, lastNameField, displayNameField, idLabel);
        }
    }

    private void updateValues(OptionField.ValueSavedEvent event) {
    	String oldValue = event.getOldValue();
        String newValue = event.getNewValue();
        String fieldName = "";

        if (event.getSource() == firstNameField) {
            DataManager.setFirstName(newValue);
            fieldName = "user_firstName";
        } else if (event.getSource() == lastNameField) {
            DataManager.setLastName(newValue);
            fieldName = "user_lastName";
        } else if (event.getSource() == displayNameField) {
        	if (newValue.equals(""))
        		newValue = null;
            DataManager.setDisplayName(newValue);
            fieldName = "user_displayName";
        } else if (event.getSource() == passwordField) {
            DataManager.setPassword(newValue);
            fieldName = "user_password";
        } else if (event.getSource() == emailField) {
        	if (newValue.equals(""))
        		newValue = null;
            DataManager.setEmail(newValue);
            fieldName = "user_email";
        } else if (event.getSource() == usernameField) {
            DataManager.setUsername(newValue);
            fieldName = "user_username";
        }

        try {
            databaseUpdate(fieldName, newValue);
            Notification.show("Daten erfolgreich aktualisiert!");
            if (event.getSource() == usernameField) {
            	DataManager.setUsername(newValue);
            	DataManager.setLoginCookies(newValue, DataManager.getPassword());
            }
            else if (event.getSource() == passwordField) {
            	DataManager.setPassword(newValue);
            	DataManager.setLoginCookies(DataManager.getUsername(), newValue);
            }
        } catch (Exception e) {
        	event.getSource().setValue(oldValue);
        	if (event.getSource() == emailField) 
                DataManager.setEmail(oldValue);
            else if (event.getSource() == usernameField)
                DataManager.setUsername(oldValue);
            Notification not = new Notification();
            not.addThemeVariants(NotificationVariant.LUMO_ERROR);
            not.setText("Fehler 202: " + e.getMessage());
            not.open();
        }
    }

    private void databaseUpdate(String columnName, String newValue) throws SQLIntegrityConstraintViolationException {
        try {
        	if (newValue == null)
        		new TableReceiver().runUpdate("UPDATE user SET " + columnName + " = " + newValue + " WHERE user_id = " + DataManager.getUserID());
        	else
        		new TableReceiver().runUpdate("UPDATE user SET " + columnName + " = '" + newValue + "' WHERE user_id = " + DataManager.getUserID());
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
        	e.printStackTrace();
        	Notification not = new Notification();
            not.addThemeVariants(NotificationVariant.LUMO_ERROR);
            not.setText("Fehler 201: " + e.getMessage());
            not.open();
        }
    }
}
