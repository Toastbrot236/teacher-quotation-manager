package com.abi.quotes;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "zitate-sammlung")
@PWA(name = "SchulHub", shortName = "SchulHub", offlinePath="offline.html", offlineResources = { "schulmanager.css" })
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void configurePage(AppShellSettings settings) {
      settings.addFavIcon("icon", "icons/apple-touch-icon.png", "32x32");
      settings.addFavIcon("icon", "icons/favicon-32x32.png", "32x32");
      settings.addFavIcon("icon", "icons/favicon-16x16.png", "16x16");
      settings.addLink("manifest", "icons/site.webmanifest");
      settings.addLink("mask-icon", "icons/safari-pinned-tab.svg");
      settings.addLink("shortcut icon", "icons/favicon.ico");
      settings.addMetaTag("msapplication-TileColor", "#da532bc");
      settings.addMetaTag("msapplication-config", "icons/browserconfig.xml");
      settings.addMetaTag("theme-color", "#3ba2be");
    }

}
