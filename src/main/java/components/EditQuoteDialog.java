package components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextArea;

import database.Quote;
import service.DataManager;

public class EditQuoteDialog extends Dialog {

	private Quote quote;
	
	private TextArea textArea;
	
	public EditQuoteDialog(Quote quote) {
		super();
		
		this.quote = quote;
		
		setHeaderTitle("Zitat bearbeiten");
		
		createBody();
		createFooter();
	}
	
	private void createBody() {
		textArea = new TextArea();
		textArea.setWidthFull();
		textArea.setLabel("Zitat");
		textArea.setValue(quote.getText().replace("<br/>", "\r\n"));
		add(textArea);
	}
	
	private void createFooter() {
		Button saveButton = new Button("Übernehmen");
		saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickListener(event ->{
			saveChanges();
		});
		Button cancelButton = new Button("Abbruch", event -> close());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		getFooter().add(cancelButton);
		getFooter().add(saveButton);
	}
	
	private void saveChanges() {

		if (!textArea.getValue().equals("")) {
			quote.setText(textArea.getValue().replace("\n", "<br/>"));
			quote.setLastEditedBy(DataManager.getUserID());
			quote.setLastEdited("now()");
			close();
			UI.getCurrent().getPage().reload();
		}
		
	}

}
