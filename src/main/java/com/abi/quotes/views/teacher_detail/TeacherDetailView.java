package com.abi.quotes.views.teacher_detail;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import components.NewQuoteButton;
import components.QuoteList;
import components.TeacherBox;
import service.DataManager;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Lehreransicht")
@Route(value = "lehrer-detail", layout = MainLayout.class)
public class TeacherDetailView extends VerticalLayout {
	
	private int teacherId;
	
	public TeacherDetailView() {
		teacherId = DataManager.getTeacherDetail();
		
		setHeightFull(); 
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
       
        getStyle().set("text-align", "center");
        
        add(new TeacherBox(teacherId));

        QuoteList list = new QuoteList(-1, true, teacherId, false);
        list.setHeightFull();
        if (list.amountQuotes() == 0) {
        	setSpacing(true);
        	add (new H2("Keine Zitate gefunden"));
        }
        else {
        	setSpacing(false);
        	add(list);
        }
        
        HorizontalLayout addLayout = new HorizontalLayout();
        addLayout.setAlignItems(Alignment.CENTER);
        addLayout.add(new Span("Zitat hinzufügen"));
        NewQuoteButton addButton = new NewQuoteButton(teacherId);
        addButton.setQuoteCreationListener(() -> list.update());
        add(addLayout, addButton);
	}

}
