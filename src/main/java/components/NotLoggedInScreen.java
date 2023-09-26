package components;

import com.abi.quotes.views.login.LoginView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class NotLoggedInScreen extends VerticalLayout {
	
	public NotLoggedInScreen() {
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		H1 heading = new H1("Login erforderlich");
		Span infotext = new Span("Du musst dich erst anmelden, um diese Funktion nutzen zu können.");
		RouterLink link = new RouterLink("Zum Login", LoginView.class);
		
		add(heading, infotext, link);
	}
	
}
