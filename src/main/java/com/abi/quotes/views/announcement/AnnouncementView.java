package com.abi.quotes.views.announcement;


import java.sql.SQLException;
import database.*;
import java.time.LocalDate;
import jakarta.servlet.http.Cookie;

import com.abi.quotes.views.MainLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;


@PageTitle("Ankündigungen")
@Route(value = "announcements", layout=MainLayout.class)
@RouteAlias(value = "klo", layout = MainLayout.class)
/**
 * Placeholder/Test page that will hopefully someday show certain chat messages which meet
 * configurable criterias as announcements.
 */
public class AnnouncementView extends VerticalLayout {

    private H1 title;
    private H2 subtitle;

    public AnnouncementView() {
        //Settings for the page itself
        setSpacing(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        //testbutton
        Button testButton = new Button ("Ich mach gar nichts");
        testButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
        testButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        testButton.getStyle().setMarginBottom("0");
        this.setHorizontalComponentAlignment(Alignment.START, testButton);
        testButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("zitate"));
        });
        add(testButton);
        //title number 1
        title = new H1("Neues/Ankündigungen");
        title.getStyle().set("margin-top", "10vh");
        title.getStyle().set("color", "var(--lumo-primary-text-color)");
        title.getStyle().set("font-size", "2em");
        title.getStyle().set("user-select",  "none");
        add(title);
        //title number 2
        subtitle = new H2("PlAttzhahlter. Ge wegg.");
        add(subtitle);
    }
}