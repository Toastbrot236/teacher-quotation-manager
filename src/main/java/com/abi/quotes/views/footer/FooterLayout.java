package com.abi.quotes.views.footer;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@CssImport(value = "themes/zitate-sammlung/footer.css")
public class FooterLayout extends AppLayout {

    public FooterLayout() {
        Footer f = new Footer();
        f.add(new Button("Test"));
        //Never ever let  this   be true!!! Css is already configured in such a way, 
        //that the navbar  |     is always displayed at the bottom! As empirically 
        //proven, a        V     "true" here breaks the optimal padding
        this.addToNavbar(false, f);
    }
}