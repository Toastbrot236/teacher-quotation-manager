package com.abi.quotes.views.favorites;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import components.NotLoggedInScreen;
import components.QuoteList;
import components.SettingBar;
import service.DataManager;

@PageTitle("Meine Favoriten")
@Route(value = "favoriten", layout = MainLayout.class)
/**
 * Page which shows all quotes the user has favourited.
 */
public class FavoritesView extends VerticalLayout {

    public FavoritesView() {
    	setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	add(new NotLoggedInScreen());
        } else {
        	
        	Button backButton = new Button("Zeige alle Lehrerzitate");
            backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
            backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            backButton.getStyle().setMarginBottom("0");
            this.setHorizontalComponentAlignment(Alignment.START, backButton);
            backButton.addClickListener(e -> {
    			getUI().ifPresent(ui -> ui.navigate("zitate"));
    		});
            add(backButton);
        	
        	add(new H2("Meine Favoriten"));
        	
        	QuoteList list = new QuoteList(true);
        	list.setSizeFull();
        	add(list);
        	
        	if (list.amountQuotes() == 0) {
        		add(new Span("Wie es scheint, hast du noch kein Zitat deinen Favoriten hinzugefügt. Klicke auf den Stern neben einem Zitat, wenn es dir besonders gut gefällt!"));
        	}
        }
    }

}