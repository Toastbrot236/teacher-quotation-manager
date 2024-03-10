package schulmanager.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import schulmanager.api.ApiCall;
import schulmanager.api.ChatUsersRequest;
import schulmanager.api.DistributionListsRequest;
import service.DataManager;

public class NewChatButton extends Button {

	public NewChatButton() {
		super("Neuer Chat");
		setEnabled(false);
		
		setIcon(VaadinIcon.PLUS.create());
		addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
		addClickListener(e -> {
			Dialog dialog = new Dialog("Neuer Chat");
			VerticalLayout layout = new VerticalLayout();
			
			ApiCall call = new ApiCall(DataManager.getSmSession());
			DistributionListsRequest r1 = new DistributionListsRequest();
			ChatUsersRequest r2 = new ChatUsersRequest();
			call.addRequests(r1, r2);
			call.execute();
			
			ComboBox<JsonObject> recipientBox = new ComboBox<JsonObject>("Empfänger");
			recipientBox.getStyle().set("--vaadin-combo-box-overlay-width", "400px");
			
			ArrayList<JsonObject> recipients = new ArrayList<JsonObject>();
			JsonObject[] arr1 = r1.getLists();
			JsonObject[] arr2 = r2.getUsers();
			for (JsonObject jo : arr1) recipients.add(jo);
			for (JsonObject jo : arr2) recipients.add(jo);
			recipientBox.setItems(recipients);
			
			recipientBox.setItemLabelGenerator(
			        json -> json.get("name").getAsString());
			
			recipientBox.setRenderer(createRenderer());
			
			layout.add(recipientBox);
			dialog.add(layout);
			
			dialog.open();
		});
	}
	
	private Renderer<JsonObject> createRenderer() {
		return new ComponentRenderer<Component, JsonObject>(json -> createListItem(json));
	}
	
	private HorizontalLayout createListItem(JsonObject json) {
		HorizontalLayout layout = new HorizontalLayout();
		StringBuilder sb = new StringBuilder(
				"<div><span>" + json.get("name").getAsString() + "</span>"
			);
		if (json.get("subtext") != null)
			sb.append("<br><span style=\"font-size: 10px\">" + json.get("subtext").getAsString() + "</span>");
		sb.append("</div>");
		
		layout.add(new Html(sb.toString()));
		return layout;
	}
	
}
