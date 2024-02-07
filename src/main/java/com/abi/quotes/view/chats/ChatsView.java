package com.abi.quotes.view.chats;

import com.abi.quotes.views.MainLayout;
import com.abi.quotes.views.beta_test.SmView;
import com.google.gson.JsonObject;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import schulmanager.api.ApiCall;
import schulmanager.api.Request;
import schulmanager.api.SubscriptionsRequest;
import schulmanager.components.ChatOverviewBox;
import service.DataManager;

@PageTitle("Chats")
@Route(value = "chats", layout = MainLayout.class)
public class ChatsView extends SmView {

	public ChatsView() {
		super();
	}

	@Override
	protected void initialise() {
		this.setHorizontalComponentAlignment(Alignment.START);
		
		ApiCall call = new ApiCall(DataManager.getSmSession());
		SubscriptionsRequest subscriptionsRequest = new SubscriptionsRequest();
		call.addRequest(subscriptionsRequest);
		call.execute();
		
		VerticalLayout innerLayout = new VerticalLayout();
		innerLayout.setPadding(false);
		innerLayout.setSpacing(false);
		innerLayout.setWidth("100%");
		innerLayout.setMaxWidth("800px");
		innerLayout.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
		
		JsonObject[] subs = subscriptionsRequest.getSubscriptions();
		for (int i = 0; i < subs.length; i++) {
			ChatOverviewBox box = new ChatOverviewBox(subs[i]);
			if (box.getUnreadCount() > 0)
				innerLayout.addComponentAsFirst(box);
			else
				innerLayout.add(box);
			if (i == 0) 
				box.getStyle().set("border-top-left-radius", "15px").set("border-top-right-radius", "15px");
			if ( i == subs.length-1)
				box.getStyle().set("border-bottom-left-radius", "15px").set("border-bottom-right-radius", "15px");
		}
		
		add(innerLayout);
	}
	
}
