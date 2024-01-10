package com.abi.quotes.views.student_quote;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import components.NewQuoteButton;
import components.NotLoggedInScreen;
import components.QuoteList;
import components.SettingBar;
import service.DataManager;
import service.Permission;

@PageTitle("Schülerzitate")
@Route(value = "schuelerzitate", layout = MainLayout.class)
public class StudentQuoteView extends VerticalLayout {

    public StudentQuoteView() {
    	setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        
        if (DataManager.getLoggedIn() == null || !DataManager.getLoggedIn()) {
        	add(new NotLoggedInScreen());
        } else {
        	QuoteList list = new QuoteList(-1, false, 79, true);
        	
        	add(list);
        	
        	list.setSizeFull();
        	add(list);
        }
    }

}
