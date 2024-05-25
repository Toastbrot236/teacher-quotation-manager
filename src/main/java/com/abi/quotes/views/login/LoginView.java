package com.abi.quotes.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import database.TableReceiver;
import service.DataManager;
import service.EmailSender;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.server.Cookie;

import java.sql.SQLException;
//import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@PageTitle("Login")
@Route(value = "login", layout = MainLayout.class)
@CssImport(value = "themes/zitate-sammlung/loginBackground.css", themeFor = "vaadin-login-overlay-wrapper")
/**
 * Page where you can log into zitate-sammlung
 */
public class LoginView extends LoginOverlay {

    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Willkommen");
        i18n.getHeader().setDescription("Melde dich mit deinen Anmeldedaten an, um Zitate lesen und einreichen zu können.");
        i18n.getForm().setUsername("Benutzername");
        i18n.getForm().setTitle("Login");
        i18n.getForm().setSubmit("Anmelden");
        i18n.getForm().setPassword("Passwort");
        i18n.getForm().setForgotPassword("Passwort vergessen?");
        i18n.setAdditionalInformation("Du hast noch keinen Account? Frage Jona persönlich, um dich registrieren zu lassen");
        i18n.getErrorMessage().setUsername("Du musst schon sagen, wer du bist. Sonst kannst du nicht angemeldet werden.");
        i18n.getErrorMessage().setPassword("Passwort fehlt.");
        i18n.getErrorMessage().setTitle("Login fehlgeschlagen");
        i18n.getErrorMessage().setMessage("Versuche es erneut. Vielleicht hast du dich vertippt. Wenn du dein Passwort vergessen hast, klicke auf 'Passwort vergessen?'. Sollte das Problem andauern, wende dich bitte an einen Administrator.");
        setI18n(i18n);

        addLoginListener(e -> {
            CompletableFuture<Void> usernameFuture = new CompletableFuture<>();
            CompletableFuture<Void> passwordFuture = new CompletableFuture<>();

            getUsername(usernameFuture);
            getPassword(passwordFuture);

            CompletableFuture<Void> loginProcess = CompletableFuture.allOf(usernameFuture, passwordFuture);
            loginProcess.thenRun(() -> {
                String username = e.getUsername();
                String password = e.getPassword();
                login(username, password);
            });
        });

        this.addForgotPasswordListener(e -> {
        	createForgotPasswordDialog();
        });

        setForgotPasswordButtonVisible(true);
        setOpened(true);
    }

    private void login(String username, String password) {
		try {	
			Integer userId = new TableReceiver().runQueryAndGetSingleValue(
					String.format(
							"SELECT user_id FROM user WHERE (user_email = '%s' OR user_username = '%s') AND user_password = '%s';",
							username,
							username,
							password
							),
					Integer.class
					);
			
			System.out.println("Eingeloggt als " + username + " mit Id = " + userId);
			
			if (userId == null) {
				setError(true);
				return;
			}
			
			DataManager.setLoginCookies(username, password);
			DataManager.login(userId);
			
			this.getUI().ifPresent(ui -> ui.navigate("test"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    		
    }

    private void getUsername(CompletableFuture<Void> usernameFuture) {
        UI.getCurrent().getPage()
                .executeJs("return document.getElementById('vaadinLoginUsername').value;")
                .then(String.class, value -> {
                    if (StringUtils.isNotBlank(value)) {
                        System.out.println(value);
                        usernameFuture.complete(null);
                    }
                });
    }

    private void getPassword(CompletableFuture<Void> passwordFuture) {
        UI.getCurrent().getPage()
                .executeJs("return document.getElementById('vaadinLoginPassword').value;")
                .then(String.class, value -> {
                    if (StringUtils.isNotBlank(value)) {
                        System.out.println(value);
                        passwordFuture.complete(null);
                    }
                });
    }
    
    private void createForgotPasswordDialog() {
    	Dialog dialog = new Dialog();
    	dialog.setWidth("calc(var(--lumo-size-m) * 9)");
        	
    	dialog.setHeaderTitle("Passwort vergessen?");
    	
        dialog.add(new Html(
        		"<p><i>Wenn du dein Passwort vergessen hast, kannst du es zurücksetzen und "
        		+ "ein neues per E-Mail erhalten. Das funktioniert nur, wenn in deinem "
        		+ "Profil auch eine Mail-Adresse angegeben ist. Gib deinen <b>Benutzernamen</b> "
        		+ "ein, wenn du dein Passwort zurücksetzen möchtest.</i></p>"));

        TextField nameField = new TextField("Benutzername");
        dialog.add(nameField);
        
        dialog.add(new Html(
        		"<p>Bist du sicher, dass du ein neues Passwort anfordern möchtest?</p>"
        		));
        
        Button cancelButton = new Button("Abbrechen");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        cancelButton.addClickListener(e -> dialog.close());
        
        Button confirmButton = new Button("Zurücksetzen");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(e -> {
        	
        	String email = null;
        	
        	try {
        		email = new TableReceiver().runQueryAndGetSingleValue("SELECT user_email FROM user WHERE user_username = '" + nameField.getValue() + "'", String.class);
        	} catch (Exception __) {
        		dialog.close();
        		new ConfirmDialog("Fehlgeschlagen", "Entweder hast du einen falschen Benutzernamen angegeben oder mit deinem Konto ist keine E-Mail-Adresse verknüpft.", "OK", null).open();
        	}
        	
        	if (email == null) {
        		dialog.close();
        		new ConfirmDialog("Fehlgeschlagen", "Entweder hast du einen falschen Benutzernamen angegeben oder mit deinem Konto ist keine E-Mail-Adresse verknüpft.", "OK", __ -> {}).open();
        		return;
        	}
        	
        	String newPassword = "";
			for (int i = 0; i < 8; i++) {
				newPassword += (char) (((int) (Math.random() * 26)) + 65);
			}
			
			boolean ok = EmailSender.sendMail("Passwort zurückgesetzt - Zitate-Sammlung",
					String.format(
							"Hallo %s,<br/>anscheinend hast du dein Passwort vergessen. "
							+ "Hiermit erhältst du ein neues Passwort, um dich in deinen "
							+ "Account einloggen zu können. Dein neues Passwort lautet:<br/><br/>"
							+ "<b>%s</b><br/><br/>Nachdem du dich eingeloggt hast, solltest du "
							+ "dein Passwort am besten wieder ändern. Klicke dazu auf deinen "
							+ "Namen oben rechts und wähle <i>Dein Profil</i> im Menü aus.",
							nameField.getValue(),
							newPassword
							), 
					email);
			
			dialog.close();
			
			if (!ok) {
        		new ConfirmDialog("Fehlgeschlagen", "Entweder hast du einen falschen Benutzernamen angegeben oder mit deinem Konto ist keine E-Mail-Adresse verknüpft.", "OK", __ -> {}).open();
        		return;
			}
			
    		new ConfirmDialog(
    				"Passwort zurückgesetzt", 
    				String.format(
    						"Dein Passwort wurde erfolgreich zurückgesetzt. "
    						+ "Dir wurde ein neues Passwort an deine E-Mail %s "
    						+ "geschickt. Überprüfe gegebenenfalls auch deinen "
    						+ "Spam-Ordner.",
    						disguiseEmail(email)
    						), 
    				"Verstanden", __ -> {})
    			.open();

			try {
				new TableReceiver().runUpdate(String.format(
						"UPDATE user SET user_password = '%s' WHERE user_username = '%s'",
						newPassword,
						nameField.getValue()
						));
			} catch (SQLException __) {
				__.printStackTrace();
			}

        });
        
        dialog.getFooter().add(cancelButton, confirmButton);
        
        dialog.open();
    }
    
    private String disguiseEmail(String email) {
    	String[] splitted = email.split("@");
    	String result = splitted[0].charAt(0) + (splitted[0].charAt(1) + "");
    	for (int i = 0; i < splitted[0].length()-2; i++) {
    		result += "*";
    	}
    	result += "@" + splitted[1];
    	return result;
    }
}
