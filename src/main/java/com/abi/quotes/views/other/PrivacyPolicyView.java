package com.abi.quotes.views.other;


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
import com.vaadin.flow.component.Html;
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
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;


@PageTitle("Datenschutzerklärung")
@Route(value = "privacypolicy", layout=MainLayout.class)
@RouteAlias(value = "privacy", layout = MainLayout.class)
/**
 * Page which shows the privacy policy in a hopefully less annoying and more efficient way.
 */
public class PrivacyPolicyView extends VerticalLayout {

    private Html text;

    public PrivacyPolicyView() {
        try {
            InputStream stream = getClass().getResourceAsStream("/META-INF/resources/texts/Datenschutz.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String html = "";
            String line = reader.readLine();
            while(line != null) {
                html += line;
                line = reader.readLine();
            }
            text = new Html("<div>" + html + "</div>");
            add(text);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}