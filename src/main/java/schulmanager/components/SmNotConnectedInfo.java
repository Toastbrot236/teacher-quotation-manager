package schulmanager.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinService;

import schulmanager.api.InvalidCredentialsException;
import schulmanager.api.Session;
import service.DataManager;
import jakarta.servlet.http.Cookie;

public class SmNotConnectedInfo extends VerticalLayout {
	
	public SmNotConnectedInfo(boolean quotesLoggedIn, LoginHandler loginHandler) {
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		add(new H3("Schulmanager-Login"));
		if (quotesLoggedIn) {
			add(new Span("Gib hier deine Anmeldedaten vom Schulmanager ein, um Zugriff auf den Stundenplan und die Nachrichten zu erhalten. Im Anschluss kannst du deinen Schulmanager-Account mit deinem Schulhub-Account verknüpfen, um dich nicht mehr jedes Mal anmelden zu müssen und Zugriff auf zusätzliche Funktionen zu erhalten."));
		}
		else {
			add(new Span("Alternativ kannst du hier deine Anmeldedaten vom Schulmanager eingeben, um ohne Verknüpfung auf Schulmanager-Funktionen zugreifen zu können."));
		}
		
		TextField usernameField = new TextField("E-Mail / Benutzername");
		TextField passwordField = new TextField("Passwort");
		passwordField.setErrorMessage("Anmeldedaten falsch");
		
		Button submitButton = new Button("Bestätigen");
		submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitButton.addClickListener(event -> {
			System.out.println("A user tries to login into SM: " + ((DataManager.getUserID() == null) ? "Not logged in into SH" : ("SH-ID: " + DataManager.getUserID())) + "; name: " + usernameField.getValue() + "; pass: " + passwordField.getValue().substring(0, 3) + "...");
			try {
				Session session = new Session(usernameField.getValue(), passwordField.getValue());
				saveCookies(usernameField.getValue(), passwordField.getValue());
				loginHandler.handleLogin(session);
			} catch (InvalidCredentialsException e) {
				usernameField.setInvalid(true);
				passwordField.setInvalid(true);
			}
		});
		add(usernameField, passwordField, submitButton);
	}
	
	public interface LoginHandler {
		public void handleLogin(Session session);
	}
	
	public static void saveCookies(String username, String password) {
		Cookie nameCookie = new Cookie("smName", username);
		nameCookie.setMaxAge(90000000);
		nameCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		VaadinService.getCurrentResponse().addCookie(nameCookie);
		
		Cookie passwordCookie = new Cookie("smPassword", password);
		passwordCookie.setMaxAge(90000000);
		passwordCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		VaadinService.getCurrentResponse().addCookie(passwordCookie);
	}

}
