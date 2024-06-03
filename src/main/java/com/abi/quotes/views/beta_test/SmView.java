package com.abi.quotes.views.beta_test;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.theme.lumo.Lumo;

import components.NotLoggedInScreen;
import database.User;
import jakarta.servlet.http.Cookie;
import schulmanager.api.InvalidCredentialsException;
import schulmanager.api.Session;
import schulmanager.components.SmNotConnectedInfo;
import service.DataManager;

/**
 * This class gets inherited by both ChatsView and ChatDetailView.
 */
public abstract class SmView extends VerticalLayout {
	
	protected Session session;
	
	public SmView() {
		
    	setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	MainLayout.cookieLogin();
        }
        else {
        	getElement().executeJs("document.documentElement.setAttribute('theme', $0)", (DataManager.getDarkMode()) ? Lumo.DARK : Lumo.LIGHT);
        }
        
        if (DataManager.getSmSession() == null) {
	        Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
	    	String smUsername = null;
	    	String smPassword = null;
	    	for (Cookie c : cookies) {
	    		if (c.getName().equals("smName"))
	    			smUsername = c.getValue();
	    		if (c.getName().equals("smPassword"))
	    			smPassword = c.getValue();
	    	}
	    	if (smUsername != null && smPassword != null) {
	    		try {
	    			System.out.println("Trying to login via cookies: " + smUsername + ", " + smPassword);
		    		session = new Session(smUsername, smPassword);
		    		DataManager.setSmSession(session);
	    		} catch (InvalidCredentialsException e) {}
	    	}
        }
    		
        
        //Schulmanager is not connected
        if (DataManager.getSmSession() == null && session == null) {
        	
        	//Zitate and Schulmanager are both not connected
        	if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        		add(new NotLoggedInScreen());
        		add(new SmNotConnectedInfo(false, session -> {
        			this.session = session;
        			DataManager.setSmSession(session);
        			removeAll();
        			initialise();
        		}));
        	}
        	//Schulmanager is not connected, but Zitate is connected
        	else {
        		//Schulmanager is not connected and Zitate-account does not contain Schulmanager-credentials
        		if (DataManager.getSmId() == null || DataManager.getSmId() == 0) {
        			add(new SmNotConnectedInfo(true, session -> {
	        			this.session = session;
	        			DataManager.setSmSession(session);
	        			removeAll();
	        			initialise();
        			}));
        		}
        		//Schulmanager is not connected, but Zitate-account contains Schulmanager-credentials
        		else {
        			String[] smCredentials = User.smCredentials(DataManager.getUserID());
        			session = new Session(smCredentials[0], smCredentials[1]);
        			DataManager.setSmSession(session);
        			SmNotConnectedInfo.saveCookies(smCredentials[0], smCredentials[1]);
        			removeAll();
        			initialise();
        		}
        	}
        }
        
        //Schulmanager is connected
        else {
        	if (session == null)
        		session = DataManager.getSmSession();
        	initialise();
        }
	}
	
	protected abstract void initialise();

}
