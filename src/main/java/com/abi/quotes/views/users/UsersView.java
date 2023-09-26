package com.abi.quotes.views.users;

import java.sql.SQLException;
import java.util.ArrayList;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import components.*;
import database.Row;
import database.TableReceiver;
import database.User;
import service.DataManager;

@PageTitle("Nutzerverwaltung")
@Route(value = "nutzer", layout = MainLayout.class)
@JsModule("./copytoclipboard.js")
public class UsersView extends VerticalLayout {
	
	public UsersView() {
		
		if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
			add(new NotLoggedInScreen());
		} else if (!DataManager.isAdmin()) {
			add(new H1("Nur für Admins!"));
		} else {
			editFields = new ArrayList<UserEdit<?>>();
			
			Grid<User> userGrid = new Grid<User>();
		    userGrid.setItems(getUsers());
		    
		    userGrid.addColumn(u -> u.get("user_id")).setHeader("ID")
		    	.setFrozen(true).setSortable(true).setResizable(true);
		    Grid.Column<User> c1 = userGrid.addComponentColumn(u -> tf(u, "user_firstName")).setHeader("Vorname")
		    	.setFlexGrow(60).setResizable(true);
		    Grid.Column<User> c2 = userGrid.addComponentColumn(u -> tf(u, "user_lastName")).setHeader("Name").setFlexGrow(60).setResizable(true);
		    Grid.Column<User> c3 = userGrid.addComponentColumn(u -> tf(u, "user_displayName")).setHeader("Anzeigename").setFlexGrow(60).setResizable(true);
		    Grid.Column<User> c4 = userGrid.addComponentColumn(u -> tf(u, "user_username")).setHeader("Benutzername").setFlexGrow(60).setResizable(true);
		    Grid.Column<User> c5 = userGrid.addComponentColumn(u -> tf(u, "user_email")).setHeader("E-Mail").setFlexGrow(80).setResizable(true);
		    Grid.Column<User> c6 = userGrid.addComponentColumn(u -> cb(u, "user_read")).setHeader("Read").setFlexGrow(1).setAutoWidth(true).setResizable(true);
		    Grid.Column<User> c7 = userGrid.addComponentColumn(u -> cb(u, "user_write")).setHeader("Write").setFlexGrow(1).setAutoWidth(true).setResizable(true);
		    Grid.Column<User> c8 = userGrid.addComponentColumn(u -> cb(u, "user_rate")).setHeader("Rate").setFlexGrow(1).setAutoWidth(true).setResizable(true);
		    Grid.Column<User> c9 = userGrid.addComponentColumn(u -> cb(u, "user_edit")).setHeader("Edit").setFlexGrow(1).setAutoWidth(true).setResizable(true);
		    Grid.Column<User> c10 = userGrid.addComponentColumn(u -> cb(u, "user_delete")).setHeader("Delete").setFlexGrow(1).setAutoWidth(true).setResizable(true);
		    Grid.Column<User> c11 = userGrid.addComponentColumn(u -> cb(u, "user_admin")).setHeader("Admin").setFlexGrow(1).setAutoWidth(true).setResizable(true);
		    Grid.Column<User> c12 = userGrid.addComponentColumn(u -> rp(u)).setHeader("Passwort").setFlexGrow(60).setResizable(true);
		    
		    HeaderRow headerRow = userGrid.prependHeaderRow();
		    headerRow.join(c1, c2, c3, c4, c5).setText("Nutzerdaten");
		    headerRow.join(c6, c7, c8, c9, c10, c11).setText("Berechtigungen");
		    
		    userGrid.setMultiSort(true, true);
		    
		    userGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		    
		    add(userGrid);
		    
		    Button saveButton = new Button("Änderungen speichern");
		    saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
		    saveButton.getStyle().set("outline", "4px solid var(--lumo-warning-color)");
		    saveButton.addClickListener(e -> saveAll());
		    saveButton.addClassName("wiggle-effect");
		    
		    add(new HorizontalLayout(new NewUserButton(), saveButton));
		}
		
	}
	
	private User[] getUsers() {
		try {
			
            Row[] rows = new TableReceiver().runQueryAndGet("SELECT * FROM user").getRows();
            User[] users = new User[rows.length];
            
            for (int i = 0; i < users.length; i++) {
            	users[i] = User.fromRow(rows[i]);
            }
            
            return users;
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	ArrayList<UserEdit<?>> editFields;
	
	private Component tf(User user, String columnName) {
		UserEdit<String> e = new UserEditTextField(user, columnName);
		editFields.add(e);
		return e.create();
	}
	
	private Component cb(User user, String columnName) {
		UserEdit<Boolean> e = new UserEditCheckbox(user, columnName);
		editFields.add(e);
		return e.create();
	}
	
	private Component rp(User user) {
		Button resetButton = new Button("Zurücksetzen");
		resetButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
		resetButton.addClickListener(e -> {
			String newPassword = "";
			for (int i = 0; i < 8; i++) {
				newPassword += (char) (((int) (Math.random() * 26)) + 65);
			}
			user.setPassword(newPassword);
			createPasswordNotification(user, newPassword).open();
		});
		return resetButton;
	}

	private void saveAll() {
		for (UserEdit<?> edit : editFields) {
			edit.save();
		}
		Notification.show("Änderungen erfolgreich gespeichert!");
	}
	
	private Notification createPasswordNotification(User u, String newPassword) {
		Notification notification = new Notification();
	    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

	    Icon icon = VaadinIcon.CHECK_CIRCLE.create();
	    Div info = new Div(new Text("Passwort zurückgesetzt auf: " + newPassword));

	    Button copyButton = new Button("Kopieren");
	    copyButton.addClickListener(
	            e -> {
	            	UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", newPassword);
	            	copyButton.setText("Kopiert");
	            });
	    copyButton.getStyle().set("margin", "0 0 0 var(--lumo-space-l)");
	    
	    Button closeButton = new Button(
	            VaadinIcon.CLOSE_SMALL.create(),
	            e -> notification.close());
	    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

	    HorizontalLayout layout = new HorizontalLayout(
	            icon, info, copyButton, closeButton);
	    layout.setAlignItems(Alignment.CENTER);

	    notification.add(layout);
	    
	    notification.setPosition(Position.TOP_CENTER);

	    return notification;
	}
	
}
