package schulmanager.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import schulmanager.api.ApiCall;
import schulmanager.api.ChatUsersRequest;
import schulmanager.api.CreateThreadRequest;
import schulmanager.api.DistributionListsRequest;
import service.DataManager;

public class NewChatButton extends Button {

	public NewChatButton() {
		super("Neuer Chat");
		//setEnabled(false);
		
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
			//JsonObject[] arr1 = r1.getLists(); //TODO Use once CreateThreadRequest allows for messages towards distribution lists
			JsonObject[] arr2 = r2.getUsers();
			//for (JsonObject jo : arr1) recipients.add(jo); //TODO Use once CreateThreadRequest allows for messages towards distribution lists
			for (JsonObject jo : arr2) recipients.add(jo);
			recipientBox.setItems(recipients);
			
			recipientBox.setItemLabelGenerator(
			        json -> json.get("name").getAsString());
			
			recipientBox.setRenderer(createRenderer());
			
			TextField topicField = new TextField("Betreff");
			topicField.setWidthFull();
			
			TextArea messageField = new TextArea("Nachricht");
			messageField.setWidthFull();
			
			Button cancelButton = new Button("Abbruch");
			cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
			cancelButton.addClickListener(event -> dialog.close());
			
			Button submitButton = new Button("Erstellen & Senden");
			submitButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
			submitButton.addClickListener(event -> {
				if (createChat(recipientBox.getValue(), topicField.getValue(), messageField.getValue())) {
					dialog.close();
					UI.getCurrent().getPage().reload();
				}
				else {
					Notification errorNot = new Notification();
					errorNot.addThemeVariants(NotificationVariant.LUMO_ERROR);
					errorNot.setText("Stelle sicher, dass Empfänger, Titel und Nachricht vorhanden sind!");
					errorNot.setDuration(3500);
					errorNot.open();
				}
			});
			
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setJustifyContentMode(JustifyContentMode.END);
			buttonLayout.add(cancelButton, submitButton);
			
			layout.add(recipientBox, topicField, messageField, buttonLayout);
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
	
	/**
	 * 
	 * @param recipient
	 * @param topic
	 * @param message
	 * @return true, if the chat was created succesfully; otherwise false
	 */
	private boolean createChat(JsonObject recipient, String topic, String message) {
		if (recipient == null || topic == null || message == null || topic.equals("") || message.equals("")) {
			return false;
		}
		else {
			new CreateThreadRequest(recipient, topic, message, true).execute(DataManager.getSmSession());
			return true;
		}
	}
	
}
