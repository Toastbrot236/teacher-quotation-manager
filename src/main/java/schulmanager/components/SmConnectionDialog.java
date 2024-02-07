package schulmanager.components;

import com.abi.quotes.views.login.LoginView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoIcon;

import database.TableReceiver;
import database.User;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import service.DataManager;

public class SmConnectionDialog extends Dialog {
	
	private Button originButton;
	
	private boolean isLoggedIn = true;
	
	public SmConnectionDialog(Button originButton) {
		this.originButton = originButton;
		
		addThemeVariants(DialogVariant.LUMO_NO_PADDING);
		
		setHeaderTitle("Accounts verknüpfen");
		Button closeButton = new Button(LumoIcon.CROSS.create(),
		        (e) -> close());
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		getHeader().add(closeButton);
		
		VerticalLayout layout = new VerticalLayout();
		layout.getStyle().setPadding("14px");
		layout.setJustifyContentMode(JustifyContentMode.CENTER);
		
		layout.add(new Html(
				"<div style=\"font-size:11px\">Wenn du deinen Schulmanager-Account mit deinem hiesigen Account verknüpfst, erhältst du Zugriff auf "
				+ "tolle Funktionen wie das Teilen deines Stundenplans mit anderen oder der Möglichkeit, die "
				+ "<span style=\"color:var(--lumo-primary-color)\">Farben</span> "
				+ "nach deinen Wünschen anzupassen. Ebenso wird geräteübergreifend gespeichert, ob du lieber den Light oder "
				+ "den Dark Mode verwendest."
				+ "</div>"));
		
		HorizontalLayout checkLayout = new HorizontalLayout();
		checkLayout.setWidthFull();
		checkLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
		checkLayout.add(smDataDisplay());
		checkLayout.add(new Html("<div style=\"border-left:thin solid var(--lumo-body-text-color)\"></div>"));	
		checkLayout.add(quotesDataDisplay());
		layout.add(checkLayout);
		
		layout.add(new Span("Möchtest du die beiden genannten Accounts verknüpfen?"));
		Span info = new Span("(Mit einem Klick auf „Verknüpfen“ akzeptierst du die Speicherung deiner Schulmanager-Anmeldedaten.)");
		info.getStyle().set("font-size", "11px");
		layout.add(info);
		
		add(layout);
		
		Button cancelButton = new Button("Abbruch");
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
		cancelButton.addClickListener(event -> close());
		getFooter().add(cancelButton);
		
		Button connectButton = new Button("Verknüpfen");
		connectButton.setEnabled(isLoggedIn);
		connectButton.setIcon(VaadinIcon.LINK.create());
		connectButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
		connectButton.addClickListener(event -> {close(); connect();});
		getFooter().add(connectButton);
	}
	
	private VerticalLayout smDataDisplay() {
		VerticalLayout layout = new VerticalLayout();
		layout.getStyle().set("font-size", "13px");
		layout.getStyle().set("overflow-wrap", "anywhere");
		layout.add("Schulmanager: ");
		layout.add(new Span(DataManager.getSmSession().getUsername()));
		layout.add(new Span(DataManager.getSmSession().getStudent().get("firstname").getAsString() + " " + DataManager.getSmSession().getStudent().get("lastname").getAsString()));
		return layout;
	}
	
	private VerticalLayout quotesDataDisplay() {
		VerticalLayout layout = new VerticalLayout();
		layout.getStyle().set("font-size", "13px");
		layout.getStyle().set("overflow-wrap", "anywhere");
		layout.add("Hier: ");
		if (DataManager.getLoggedIn() != null && DataManager.getLoggedIn()) {
			layout.add(new Span(DataManager.getUsername()));
			layout.add(new Span(DataManager.getFirstName() + " " + DataManager.getLastName()));
		}
		else {
			layout.add(new Span("Nicht angemeldet"));
			layout.add(new RouterLink("Zum Login", LoginView.class));
			isLoggedIn = false;
		}
		return layout;
	}
	
	private void connect() {
		System.out.println("CONNECTED");
		System.out.println(DataManager.getSmSession().getStudentId());
		System.out.println(DataManager.getUserID());
		
		try {
			new TableReceiver().runUpdate(String.format(
					"INSERT INTO smCredentials VALUES (%d, \'%s\', \'%s\');",
					DataManager.getSmSession().getStudentId(),
					DataManager.getSmSession().getUsername(),
					DataManager.getSmSession().getPassword()
					));
			User.updateNumber(
					DataManager.getUserID(),
					"user_smId",
					DataManager.getSmSession().getStudentId()+""
					);
			DataManager.setSmId(DataManager.getSmSession().getStudentId());
			
			Notification.show("Erfolgreich verknüpft!");
			
			originButton.setIcon(VaadinIcon.USER.create());
            originButton.setText(DataManager.getSmSession().getStudent().get("firstname").getAsString() + " " + DataManager.getSmSession().getStudent().get("lastname").getAsString());
            originButton.removeThemeVariants(ButtonVariant.LUMO_ERROR);
		} catch (Exception e) { e.printStackTrace(); }
	}

}
