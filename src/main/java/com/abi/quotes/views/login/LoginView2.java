package com.abi.quotes.views.login;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import database.*;
import io.micrometer.common.util.StringUtils;

//@PageTitle("Login")
//@Route(value = "login", layout = MainLayout.class)
//@CssImport(value = "themes/zitate-sammlung/loginBackground.css", themeFor = "vaadin-login-overlay-wrapper")
public class LoginView2 extends LoginOverlay {

	private AtomicReference<String> username, password;

    public LoginView2() {
    	
    	username = new AtomicReference<String>("");
    	password = new AtomicReference<String>("");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("11Abi26: Willkommen");
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
        	
        		getUsername();
        		getPassword();
        		
        		Runnable later = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(10000);
							Integer userId = new TableReceiver().runQueryAndGetSingleValue(
				        			String.format(
				        					"SELECT user_id FROM user WHERE (user_email = '%s' OR user_username = '%s') AND user_password = '%s';",
				        					username.get(),
				        					username.get(),
				        					password.get()
				        					),
				        			Integer.class
				        			);
				        		
				        		System.out.println("Eingeloggt als " + username.get() + " mit Id = " + userId);
				        		
				        	} catch (SQLException exception) {
				        		exception.printStackTrace();
				        	} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
        		};
        		later.run();
        });
        
        this.addForgotPasswordListener(e -> {
        	
        });

        setForgotPasswordButtonVisible(true); //TODO Passwort vergessen
        setOpened(true);
    }
    
    private String getUsername() {
    	UI.getCurrent().getPage()
        .executeJs("return document.getElementById('vaadinLoginUsername').value;")
        .then(String.class, value -> {
            if (StringUtils.isNotBlank(value)) {
            	System.out.println(value);
                username.set(value);
            }
        });
    	return username.get();
    }
    
    private String getPassword() {
    	UI.getCurrent().getPage()
        .executeJs("return document.getElementById('vaadinLoginPassword').value;")
        .then(String.class, value -> {
            if (StringUtils.isNotBlank(value)) {
            	System.out.println(value);
                password.set(value);
            }
        });
    	return password.get();
    }
}
