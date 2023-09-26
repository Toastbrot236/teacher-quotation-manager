package com.abi.quotes.views.start;

import com.abi.processing.SortingType;
import com.abi.quotes.views.MainLayout;
import com.abi.quotes.views.profil.ProfilView;
import com.abi.quotes.views.teacher.TeacherView;
import com.abi.quotes.views.users.UsersView;
import com.abi.quotes.views.zitate.ZitateView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import components.ErrorMessage;
import components.NewQuoteButton;
import components.NotLoggedInScreen;
import components.QuoteList;
import service.DataManager;

@PageTitle("Start")
@Route(value = "start", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class StartView extends VerticalLayout {

    public StartView() {
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	setJustifyContentMode(JustifyContentMode.CENTER);
        	add(new NotLoggedInScreen());
        } else {
        	
        	add(new Html("<div class=\"area\" >\n"
        			+ "            <ul class=\"circles\" style=\"width:90%; height:90%\">\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "                    <li></li>\n"
        			+ "            </ul>\n"
        			+ "    </div >"));
        	
        	H1 title = new H1("Willkommen, " + DataManager.getFirstName());
        	title.getStyle().set("margin-top", "10vh");
            title.getStyle().set("color", "var(--lumo-primary-text-color)");
            title.getStyle().set("font-size", "2em");
            
            Span infoText = new Span("Übrigens, du kannst auf deinen Namen klicken, um dein Profil aufzurufen und dort dein Passwort zu ändern.");

            VerticalLayout buttonsLayout = createNavigationButtons();

            add(title, infoText, buttonsLayout);
            
            H2 newQuotesHeadline = new H2("Neueste Zitate");
            newQuotesHeadline.getStyle().set("padding-left", "18px");
            newQuotesHeadline.getStyle().set("padding-top",  "90px");
            setHorizontalComponentAlignment(Alignment.START, newQuotesHeadline);
            add(newQuotesHeadline);
            
            add(createQuoteList());
        	
        }
    }
    
    private VerticalLayout createNavigationButtons() {
        Button alleZitateButton = createNavigationButton("Alle Zitate", ZitateView.class);
        alleZitateButton.setIcon(VaadinIcon.LIST.create());
        alleZitateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        alleZitateButton.setWidth("80%");
        alleZitateButton.setMaxWidth("400px");
        
        Button zitateNachLehrerButton = createNavigationButton("Zitate nach Lehrer", TeacherView.class);
        
        NewQuoteButton newQuoteButton = new NewQuoteButton();
        newQuoteButton.setText("Neues Zitat einreichen");
        newQuoteButton.setIcon(null);
        newQuoteButton.getStyle().set("background-color", "var(--lumo-success-color)");
        newQuoteButton.getStyle().set("color", "white");
        newQuoteButton.getStyle().set("font-weight", "bold");
        
        Button nutzerverwaltungButton = createNavigationButton("Nutzerverwaltung", UsersView.class);
        nutzerverwaltungButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        VerticalLayout buttonsLayout = new VerticalLayout(alleZitateButton, zitateNachLehrerButton, newQuoteButton);
        buttonsLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        
        if (DataManager.isAdmin())
        	buttonsLayout.add(nutzerverwaltungButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.getStyle().set("margin-top", "20px");

        return buttonsLayout;
    }

    private Button createNavigationButton(String caption, Class<? extends Component> navigationTarget) {
        Button button = new Button(caption);
        button.addClickListener(e -> {
            UI.getCurrent().navigate(navigationTarget);
        });
        return button;
    }
    
    private QuoteList createQuoteList() {
    	QuoteList list = new QuoteList(3, true);
    	list.setSortingType(SortingType.NEWEST);
    	list.setHeightFull();
    	list.setHeight("110%");
    	list.getList().setHeightFull();
    	return list;
    }

}
