package com.abi.quotes.views.beta_test;

import java.time.LocalDate;

import jakarta.servlet.http.Cookie;

import com.abi.quotes.views.MainLayout;
import com.abi.quotes.views.footer.FooterLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.Lumo;

import schulmanager.api.*;
import components.NotLoggedInScreen;
import database.User;
import schulmanager.components.LessonBox;
import schulmanager.components.SmConnectionDialog;
import schulmanager.components.SmNotConnectedInfo;
import schulmanager.components.Timetable;
import schulmanager.components.TimetableSharingDialog;
import service.CalendarCalc;
import service.DataManager;

@PageTitle("Beta-Tests")
@Route(value = "test", layout=MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@CssImport(value = "themes/zitate-sammlung/schulmanager.css")
@JavaScript("./interaction.js")
public class BetaTestView extends VerticalLayout implements CalendarCalc {

	private BetaTestView thisPointer;
	
	private LocalDate start, end;
	private boolean bigScreen;
	public Timetable timetable;
	
	private Session session;
	
	private Integer currUserID;
	
	public BetaTestView() {
		thisPointer = this;
		
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
	
	private void initialise() {
		start = getCurrentMonday();
        end = start.plusDays(4);
        
        currUserID = DataManager.getUserID();
        
        System.out.println(toString(start));
        System.out.println(toString(end));

        ActualLessonsRequest r = new ActualLessonsRequest(toString(start), toString(end));
        r.execute(session);
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        
        //Creating timetable
        timetable = new Timetable(r.getLessons(), start, true, currUserID);
        layout.add(timetable);
        
        /*HTML*/
        WebStorage.setItem("timetable", timetable.getHtml());
		/**/

        UI.getCurrent().getPage().retrieveExtendedClientDetails(receiver -> {
            int screenWidth = receiver.getScreenWidth();
            bigScreen = screenWidth > 900;
            System.out.println("Screen width: " + screenWidth); 
            if (bigScreen) {		
        		//layout.addComponentAsFirst(leftArrow());
        		//layout.add(rightArrow());
            }
        });        
        
        add(layout);

        provideElementToJs();
        
        TimetableNav tNav = new TimetableNav();
        DataManager.getMainLayout().addTimetableNav(tNav, this);
        
        UI.getCurrent().addBeforeLeaveListener(e -> {
        	if (e.getNavigationTarget() != BetaTestView.class)
        		DataManager.getMainLayout().removeTimetableNav();
        	tNav.personButton.setText(session.getStudent().get("firstname").getAsString() + " " + session.getStudent().get("lastname").getAsString());
        });

	}
	
	private void oneWeekBack() {
		start = start.minusDays(7);
		end = end.minusDays(7);
	}
	
	private void oneWeekForward() {
		start = start.plusDays(7);
		end = end.plusDays(7);
	}
	
	private void provideElementToJs() {
	     getElement().executeJs("greet($0,$1)", "client", getElement());
	}
	
	@ClientCallable
	public void greet(String name) {
		System.out.println("Hi, " + name);
	}
	
	@ClientCallable
	public void rightSwipe(int xDiff) {
		swipeTimetable(true);
		System.out.println("right: " + xDiff);
	}
	
	@ClientCallable
	public void leftSwipe(int xDiff) {
		swipeTimetable(false);
		System.out.println("left: " + xDiff);
	}
	
	public void swipeTimetable(boolean forward) {
		if (forward)
			oneWeekForward();
		else
			oneWeekBack();
		ActualLessonsRequest r2 = new ActualLessonsRequest(toString(start), toString(end));
		r2.execute(session);
		timetable.changeValues(r2.getLessons(), start, forward, currUserID);
	}
	
	private Button leftArrow() {
		Button leftArrow = new Button(VaadinIcon.ARROW_LEFT.create());
		leftArrow.addClassName("timetable-nav-item");
		//leftArrow.addThemeVariants(ButtonVariant.LUMO_SECONDARY);
		/*leftArrow.addClickListener(event -> {
			swipeTimetable(false);
		});*/
		leftArrow.addClickShortcut(Key.ARROW_LEFT);
		//leftArrow.getStyle().set("transform", "translateX(20px)");
		
		return leftArrow;
	}
	
	private Button rightArrow() {
		Button rightArrow = new Button(VaadinIcon.ARROW_RIGHT.create());
		rightArrow.addClassName("timetable-nav-item");
		/*rightArrow.addClickListener(event -> {
			swipeTimetable(true);
		});*/
		rightArrow.addClickShortcut(Key.ARROW_RIGHT);
		//rightArrow.getStyle().set("transform", "translateX(-20px)");
		
		return rightArrow;
	}
	
	public class TimetableNav extends VerticalLayout implements CalendarCalc {
		
		private Button leftArrow, rightArrow, personButton;
		private Registration leftReg, rightReg, personReg;
		
		public TimetableNav() {
	        setWidthFull();
	        setSpacing(false);
	        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	        Button reloadButton = new Button(VaadinIcon.REFRESH.create());
	        reloadButton.addClassName("timetable-nav-item");
			reloadButton.addClickListener(event -> {
				timetable.reload();
			});
			add(reloadButton);
	        
	        
	        HorizontalLayout navigationContainer = new HorizontalLayout();
	        
	        if(!bigScreen) {
	        	leftArrow = leftArrow();
		        navigationContainer.add(leftArrow);
	        }
	        
	        personButton = new Button();
	        personButton.addClassName("timetable-nav-item");
	                
	        if (DataManager.getSmId() == null) {
	        	personButton.setIcon(VaadinIcon.UNLINK.create());
	        	personButton.setText("Nicht verknüpft");
	        	personButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
	        	personButton.addClickListener(event -> {
	        		new SmConnectionDialog(personButton).open();
	        	});
	        }
	        else {
	        	personButton.setIcon(VaadinIcon.USER.create());
	            personButton.setText(DataManager.getSmSession().getStudent().get("firstname").getAsString() + " " + DataManager.getSmSession().getStudent().get("lastname").getAsString());
	            personReg = personButton.addClickListener(event -> {
	            	if (currUserID != DataManager.getUserID()) {
	            		currUserID = DataManager.getUserID();
	            		timetable.setIsOwnTimetable(true);
	            		session = DataManager.getSmSession();
	            		ActualLessonsRequest r2 = new ActualLessonsRequest(toString(start), toString(end));
	            		r2.execute(session);
	            		timetable.changeValues(r2.getLessons(), start, true, currUserID);
	            	}
	            	new TimetableSharingDialog(personButton, thisPointer, timetable, start).open();
	            	personButton.setText(DataManager.getSmSession().getStudent().get("firstname").getAsString() + " " + DataManager.getSmSession().getStudent().get("lastname").getAsString());
	            });
	        }
	        navigationContainer.add(personButton); 
	        
	        if (!bigScreen) {
	        	rightArrow = rightArrow();
		        navigationContainer.add(rightArrow);
	        }
	        
	        add(navigationContainer);
		}
		
		public void changeView(BetaTestView view) {
			if (leftReg != null)
				leftReg.remove();
			if (leftArrow != null)
				leftReg = leftArrow.addClickListener(e -> {
					view.swipeTimetable(false);
				});
			if (rightReg != null)
				rightReg.remove();
			if (rightArrow != null)
				rightReg = rightArrow.addClickListener(e -> {
					view.swipeTimetable(true);
				});
			if (personReg != null)
				personReg.remove();
			personReg = personButton.addClickListener(e -> {
				if (view.currUserID != DataManager.getUserID()) {
            		view.currUserID = DataManager.getUserID();
            		view.timetable.setIsOwnTimetable(true);
            		view.session = DataManager.getSmSession();
            		ActualLessonsRequest r2 = new ActualLessonsRequest(toString(view.start), toString(view.end));
            		r2.execute(view.session);
            		view.timetable.changeValues(r2.getLessons(), view.start, true, view.currUserID);
            	}
            	new TimetableSharingDialog(personButton, view, view.timetable, view.start).open();
            	personButton.setText(DataManager.getSmSession().getStudent().get("firstname").getAsString() + " " + DataManager.getSmSession().getStudent().get("lastname").getAsString());
			});
		}
		
	}
	
	public void setCurrUser(Integer id, Session session) {
		currUserID = id;
		this.session = session;
	}
	
	public void updateTtNavText() {
		DataManager.getMainLayout().timetableNav.personButton.setText(DataManager.getSmSession().getStudent().get("firstname").getAsString() + " " + DataManager.getSmSession().getStudent().get("lastname").getAsString());
	}


}
