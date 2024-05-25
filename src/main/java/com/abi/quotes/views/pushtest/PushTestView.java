package com.abi.quotes.views.pushtest;

import com.abi.quotes.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Push-Benachrichtigungs-Test")
@Route(value="push-test", layout= MainLayout.class)
/**
 * Test page where you can subscribe to push notifications (through the browser) and post funny notifications
 */
public class PushTestView extends VerticalLayout {
	
	public PushTestView(WebPushService webPushService) {

        var toggle = new WebPushToggle(webPushService.getPublicKey());
        var messageInput = new TextField("Message:");
        var sendButton = new Button("Notify all users!");
        var messageLayout = new HorizontalLayout(messageInput, sendButton);
        messageLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        add(
            new H1("Web Push Notification Demo"),
            toggle,
            messageLayout
        );

        toggle.addSubscribeListener(e -> {
            webPushService.subscribe(e.getSubscription());
        });
        toggle.addUnsubscribeListener(e -> {
            webPushService.unsubscribe(e.getSubscription());
        });

        sendButton.addClickListener(e -> webPushService.notifyAll("Message from user", messageInput.getValue()));
    }

}
