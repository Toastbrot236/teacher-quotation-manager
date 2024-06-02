package com.abi.quotes.views.start;

import com.abi.processing.SortingType;
import com.abi.quotes.views.MainLayout;
import com.abi.quotes.views.beta_test.BetaTestView;
import com.abi.quotes.views.favorites.FavoritesView;
import com.abi.quotes.views.profil.ProfilView;
import com.abi.quotes.views.pushtest.PushTestView;
import com.abi.quotes.views.student_quote.StudentQuoteView;
import com.abi.quotes.views.teacher.TeacherView;
import com.abi.quotes.views.users.UsersView;
import com.abi.quotes.views.zitate.ZitateView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import components.ErrorMessage;
import components.FavoriteButton;
import components.HasHelp;
import components.NewQuoteButton;
import components.NotLoggedInScreen;
import components.QuoteList;
import database.User;
import service.DataManager;

@PageTitle("Startseite")
@Route(value = "start", layout = MainLayout.class)
@JsModule("./insertatcursor.js")
@JsModule("./favoritebutton.js")
@CssImport(value = "themes/zitate-sammlung/favorite-button.css")
/**
 * Overview page for all quote-related pages, shows 3 most recent quotes. Also used as starting page.
 */
public class StartView extends VerticalLayout implements HasHelp {

	private H1 title;
	private QuoteList list;
	
    public StartView() {
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
                
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	setJustifyContentMode(JustifyContentMode.CENTER);
        	add(new NotLoggedInScreen());
        } else {
        	
        	Html effect = new Html("<div class=\"area\" >\n"
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
        			+ "    </div >");
        	effect.getStyle().set("z-index",  "-1000");
        	add(effect);
        	
        	title = new H1("Willkommen, " + DataManager.getFirstName());
        	title.getStyle().set("margin-top", "10vh");
            title.getStyle().set("color", "var(--lumo-primary-text-color)");
            title.getStyle().set("font-size", "2em");
            title.getStyle().set("user-select",  "none");
            
            VerticalLayout buttonsLayout = createNavigationButtons();

            add(title, buttonsLayout);
            
            H2 newQuotesHeadline = new H2("Neueste Zitate");
            newQuotesHeadline.setId("newQuotesHeadline");
            newQuotesHeadline.getElement().setAttribute("onclick", "document.getElementById('newQuotesHeadline').scrollIntoView()");
            newQuotesHeadline.addClickListener(event -> {UI.getCurrent().getPage().executeJs("document.getElementById('newQuotesHeadline').scrollIntoView()");});
            newQuotesHeadline.getStyle().set("padding-left", "18px");
            newQuotesHeadline.getStyle().set("padding-top",  "90px");
            newQuotesHeadline.getStyle().set("user-select",  "none");
            setHorizontalComponentAlignment(Alignment.START, newQuotesHeadline);
            add(newQuotesHeadline);
            
            add(createQuoteList());
        	
            //If the user logged in for the first time, an introductory dialog is displayed and the cookie banner is shown
            if(DataManager.getFirstLogin()) {
            	Notification cookieNotification = new Notification();
                cookieNotification.setPosition(Position.BOTTOM_STRETCH);
                cookieNotification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                HorizontalLayout dialogContent = new HorizontalLayout();
                dialogContent.setWidthFull();
                Image cookieImg = new Image("images/cookie.png", "Cookies");
                cookieImg.setHeight("4em");
                Html cookieText = new Html("<span style=\"font-size: 8pt\">Diese Website verwendet Cookies. Durch die Nutzung der Website stimmst du der Verwendung von Cookies zu.<span>");
                cookieText.getStyle().setWidth("100%");
                Button agreeButton = new Button("Ok");
                agreeButton.addClickListener(event -> {
                    cookieNotification.close();
                });
                agreeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
                dialogContent.add(cookieImg, cookieText, agreeButton);
                cookieNotification.add(dialogContent);
                cookieNotification.open();   
                
            	openHelp().addDialogCloseActionListener(e -> {DataManager.setFirstLogin(false); e.getSource().close();});
            }   
        }
    }
    
    private VerticalLayout createNavigationButtons() {
        Button alleZitateButton = createNavigationButton("Alle Lehrerzitate", ZitateView.class);
        alleZitateButton.setIcon(VaadinIcon.LIST.create());
        alleZitateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        alleZitateButton.setWidth("80%");
        alleZitateButton.setMaxWidth("400px");
        
        Button favoritenButton = createNavigationButton("★", FavoritesView.class);
        favoritenButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
        
        HorizontalLayout firstRow = new HorizontalLayout(alleZitateButton, favoritenButton);
        firstRow.setWidth("85%");
        firstRow.setMaxWidth("450px");
        firstRow.setSpacing(false);
        firstRow.getThemeList().set("spacing-xs", true);
        
        Button zitateNachLehrerButton = createNavigationButton("Zitate nach Lehrer", TeacherView.class);
        Button schuelerZitateButton = createNavigationButton("Schülerzitate", StudentQuoteView.class);
        
        NewQuoteButton newQuoteButton = new NewQuoteButton();
        newQuoteButton.setText("Neues Zitat einreichen");
        newQuoteButton.setIcon(null);
        newQuoteButton.getStyle().set("background-color", "var(--lumo-success-color)");
        newQuoteButton.getStyle().set("color", "white");
        newQuoteButton.getStyle().set("font-weight", "bold");
        newQuoteButton.setQuoteCreationListener(() -> list.update());
        
        Button nutzerverwaltungButton = createNavigationButton("Nutzerverwaltung", UsersView.class);
        nutzerverwaltungButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        
        Button testsButton = createNavigationButton("Neues Zeug testen", PushTestView.class);
        testsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        VerticalLayout buttonsLayout = new VerticalLayout(firstRow, new HorizontalLayout(zitateNachLehrerButton, schuelerZitateButton), newQuoteButton);
        buttonsLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        
        if (DataManager.isAdmin())
        	buttonsLayout.add(nutzerverwaltungButton);
        if (DataManager.canTest())
        	buttonsLayout.add(testsButton);
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
    	list = new QuoteList(3, true, -1, true);
    	list.setSortingType(SortingType.NEWEST);
    	list.setHeightFull();
    	list.setHeight("110%");
    	list.getList().setHeightFull();
    	return list;
    }
    
    private void openUserMenu(Component target) {
    	ContextMenu menu = new ContextMenu();
    	
    	boolean darkMode = DataManager.getDarkMode();
    	setDarkMode(darkMode);
    	Button darkModeButton = new Button((darkMode) ? "Light Mode" : "Dark Mode");
    	darkModeButton.setIcon((darkMode) ? VaadinIcon.SUN_O.create() : VaadinIcon.MOON_O.create());
    	darkModeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
    	darkModeButton.addClickListener(e -> {
    		setDarkMode(!DataManager.getDarkMode());
    		darkModeButton.setText((DataManager.getDarkMode()) ? "Light Mode" : "Dark Mode");
    	});
    	menu.addItem(darkModeButton);
    	
    	Button profileButton = new Button("Dein Profil");
    	profileButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    	profileButton.addClickListener(e -> {
    		getUI().ifPresent(ui -> ui.navigate("profil"));
    	});
    	menu.addItem(profileButton);
    	
    	if (DataManager.isAdmin()) {
    		Button usersButton = new Button("Nutzerverwaltung");
    		usersButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    		usersButton.addClickListener(e -> {
                UI.getCurrent().navigate(UsersView.class);
            });
    		menu.addItem(usersButton);
    	}
    	
    	menu.setOpenOnClick(true);
    	menu.setTarget(target);
    }
    
    private void setDarkMode(boolean dark) {
    	User.updateNumber(DataManager.getUserID(), "user_darkMode", (dark) ? "1" : "0");
    	DataManager.setDarkMode(dark);
    	getElement().executeJs("document.documentElement.setAttribute('theme', $0)", (dark) ? Lumo.DARK : Lumo.LIGHT);
    }

	@Override
	public Component[] getPages() {
		boolean darkMode = DataManager.getDarkMode();
		VerticalLayout comp1 = new VerticalLayout();
		comp1.setPadding(false);
		comp1.add(new Paragraph("Herzlich willkommen, " + DataManager.getFirstName()));
		comp1.add(new Html("<p>Schön, dass du hierher gefunden hast. "
				+ "(<i>Scrolle herunter, um alles zu lesen.</i>) Diese Website dient dazu, all die <b>lustigen und denkwürdigen Sätze, Weisheiten und Sprüche</b>, "
				+ "denen wir im Laufe unserer Oberstufe begegnen, festzuhalten.</p>"));
		comp1.add(new Html("<p>So soll eine digitale Sammlung der besten Lehrer- und Schülerzitate entstehen. Alles hier im Web. Anstelle "
				+ "kleiner privater Sammlungen und Notizzettel gibt es eine <b>zentrale Sammelstelle</b>, an der jeder teilhaben "
				+ "und in vielerlei Erinnerungen an die kleinen Momente schwelgen kann."));
		comp1.add(new Html("<p>Wenn du erfahren möchtest, wie du mitmachen kannst und wie die Website bedient werden kann, "
				+ "klicke dich durch die nächsten Seiten. Vielen Dank für dein Interesse!"));
		
		VerticalLayout comp2 = new VerticalLayout();
		comp2.setPadding(false);
		Image img0 = new Image(darkMode ? "images/help0-dark.png" : "images/help0.png", "Start-Bildschirm");
		img0.setWidth("90%");
		img0.setMaxWidth("570px");
		comp2.add(img0);
		comp2.add(new Html("<p>Auf dem Startbildschirm hast du neben einer Schnellansicht für die neuesten 3 Zitate direkt die Möglichkeit, "
				+ "verschiedene Knöpfe zu betätigen.</p>"));
		comp2.add(new Html("<p><font color=\"#117FFF\">Alle Lehrerzitate</font>: Zeigt dir eine Auflistung sämtlicher bisher eingetragener "
				+ "Lehrerzitate, die du sortieren und durchsuchen "
				+ "kannst. Ein Klick auf das Zitat bringt dich auf eine neue Seite, "
				+ "auf der auch das Kommentieren und Bearbeiten möglich ist."));
				comp2.add(new Html("<p><font color=\"#117FFF\">Schülerzitate</font>: Tut dasselbe wie Alle Lehrerzitate, aber für Schülerzitate. "
				+ "Von Schülern, für Schüler."));
		comp2.add(new Html("<p><font color=\"#117FFF\">Zitate nach Lehrer</font>: Eine Übersicht aller Lehrer der Schule. Ein Klick auf den "
				+ "jeweiligen Lehrer zeigt dir dessen Zitate an."));
		comp2.add(new Html("<p><font color=\"#158443\">Neues Zitat einreichen</font>: Das Herzstück dieser Webseite! Trage bitte möglichst viele "
				+ "neue Zitate ein. Hierzu musst du bloß einen Lehrer aus dem Dropdown-Menü auswählen und das Zitat eintippen. <i>Tipp: "
				+ "Überprüfe am besten vorher mit der Suchfunktion bei</i> Alle Zitate<i>, ob das Zitat bereits vorhanden ist.</i>"));
		
		VerticalLayout comp3 = new VerticalLayout();
		HorizontalLayout comp3Inner = new HorizontalLayout();
		comp3Inner.setHeight("100%");
		comp3Inner.setPadding(false);
		Image img1 = new Image(darkMode ? "images/help1-dark.png" : "images/help1.png", "Drei-Punkte-Menü");
		img1.setWidth("90%");
		img1.setMaxWidth("210px");
		img1.setHeight("90%");
		comp3Inner.add(img1);
		comp3.add(comp3Inner);
		comp3Inner.add(new Html("<p>Klickst du von irgendeiner Seite auf die drei Punkte in der unteren rechten Bildschirmecke, öffnet sich ein "
				+ "kleines Menü.</p>"));
		comp3.add(new Html("<p>Hier kannst du dich abmelden (Du wirst beim nächsten Besuch mit dem selben Gerät automatisch wieder eingeloggt.), "
				+ "zwischen Dark Mode und Light Mode wechseln, diese Hilfe jederzeit wieder öffnen oder auf dein Profil zugreifen. Am besten "
				+ "änderst du dein Passwort und gibst eine E-Mail-Adresse an. Diese wird benötigt, solltest du einmal deine Anmeldedaten vergessen.</p>"
				));
		comp3.add(new Html("<p><font color=\"red\"><u>ACHTUNG</u>: Verwende <b>auf keinen Fall</b> ein Passwort, das du auch auf anderen Websites benutzt! "
				+ "Deine Daten sind nicht geschützt.</font></p>"));
		
		VerticalLayout comp4 = new VerticalLayout();
		comp4.setPadding(false);
		comp4.add(new H3("Fragen, Feedback, Fehler?"));
		comp4.add(new Html("<p>Nur her damit! Gib gerne all deine Verbesserungsvorschläge an mich, Jona, weiter. Entweder persönlich oder per "
				+ "Mail an <a href=\"mailto:illusioquest@gmail.com\">illusioquest@gmail.com</a>.</p>"));
		comp4.add(new Html("<p>Sollte dir einmal ein Bug auffallen, melde ihn bitte sofort!"));
		
		VerticalLayout comp5 = new VerticalLayout();
		comp5.setPadding(false);
		comp5.setSpacing(false);
		Html html0 = new Html("<p>Entwickler: <b>Jona Richartz</b>  <small>(Ja, das hab ich gemacht)</small></p>");
		html0.getElement().setProperty("margin", "0px");
		Html html1 = new Html("<p>Unter Verwendung von <a href=\"https://vaadin.com/\"><b>Vaadin 24</b></a></p>");
		html1.getElement().setProperty("margin", "0px");
		comp5.add(html0, html1);
		comp5.add(new Html("<p>GitHub-Repo: <a href = \"https://github.com/IllusioQuest/teacher-quotation-manager\">teacher-quotation-manager</a></p>"));
		comp5.add(new Html("<p>Helfende Hände:</p>"));
		comp5.add(new Html("<ul>\r\n"
				+ "  <li><a href=\"https://schulmanager-online.de/\">Schulmanager</a> (Gesamter Stundenplan- und Chats-Sektor; nicht bloß eine helfende Hand, sondern dort essentiell für die Bereitstellung der Daten)</li>"
				+ "  <li><a href=\"https://www.eclipse.org/\">Eclipse</a> (IDE)</li>"
				+ "  <li><a href=\"https://www.jetbrains.com/idea/\">IntelliJ IDEA (die Community Variante)</a> (IDE)</li>"
				+ "  <li><a href=\"https://cloud.google.com/run/\">Google Cloud Run</a> (Hosting)</li>"
				+ "  <li><a href=\"https://www.docker.com/\">Docker</a> (Containerisierung)</li>"
				+ "  <li><a href=\"https://aiven.io/\">Aiven</a> (Datenbank)</li>"
				+ "  <li><a href=\"https://maven.apache.org/\">Maven</a> (Projekt- / Dependencymanager)</li>"
				+ "  <li><a href=\"https://spring.io/\">Spring</a> (? Ist halt bei Vaadin dabei...)</li>"
				+ "  <li><a href=\"https://github.com/\">GitHub</a> (Versionsmanagement / Veröffentlichung des Quellcodes)</li>"
				+ "  <li><a href=\"https://ngrok.com/\">ngrok</a> (Zu Testzwecken während der Entwicklung)</li>"
				+ "  <li><a href=\"https://www.veryicon.com/icons/file-type/color-file-icon-collection/\">veryicon</a> (Dateityp-Icons)</li>"
				+ "</ul>"));
		comp5.add(new Html("<script src=\"https://zitate.webmart.de/zdt.js\" async></script>"));
		comp5.add(new Html("<span style=\"font-family: monospace\"><small>Version 2.2.0</small></span>"));
		comp5.add(new Html("<p>Du hast das alles gelesen? Dafür hast du dir einen Keks verdient. 🍪</p>"));
		comp5.add(new Html("<span>Übrigens, zu manchen Keksen passt auch prima Kaffee:"));
		comp5.add(new Html("<a href=\"https://www.buymeacoffee.com/illusioquest?l=de\" target=\"_blank\"><img src=\"https://cdn.buymeacoffee.com/buttons/v2/default-blue.png\" alt=\"Buy Me A Coffee\" style=\"height: 60px !important;width: 217px !important;\" ></a>"));
		
		return new Component[] {comp1, comp2, comp3, comp4, comp5};
	}

}
