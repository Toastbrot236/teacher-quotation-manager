package components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.theme.lumo.LumoIcon;

public class OptionField extends HorizontalLayout {
	
	private TextFieldBase<?, String> field;
	private String value, helpText; 
	private Button editButton, saveButton, cancelButton;
	private ValueSavedListener listener;
	private EditButtonClickedListener editListener;
	
	public OptionField (String label, String initialValue, String helpText) {
		this.helpText = helpText;
		
		value = initialValue;
		
		setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		
		field = new TextField(label);
		if (value == null)
			field.setValue("");
		else
			field.setValue(value);
		field.setReadOnly(true);
		add(field);
		
		if (helpText != null) {
			Button helpButton = new Button(VaadinIcon.INFO_CIRCLE.create());
			helpButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,
					ButtonVariant.LUMO_ICON);
			field.setSuffixComponent(helpButton);
			field.setTooltipText(helpText);
			Tooltip tooltip = field.getTooltip().withManual(true);
			helpButton.addClickListener(event -> {
				tooltip.setOpened(!tooltip.isOpened());
			});
		}
		
		
		editButton = new Button(new Icon(VaadinIcon.PENCIL));
        editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        editButton.setAriaLabel("Ändern");
        editButton.addClickListener(e -> {
        	if (editListener != null)
        		editListener.onClick();
        	startEdit();
        });
        
        saveButton = new Button(LumoIcon.CHECKMARK.create());
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON);
        saveButton.setVisible(false);
        saveButton.addClickListener(e -> {
        	endEdit();
        	save();
        });
        
        cancelButton = new Button(LumoIcon.CROSS.create());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        cancelButton.setVisible(false);
        cancelButton.addClickListener(e -> {
        	if (value == null)
    			field.setValue("");
    		else
    			field.setValue(value);
        	endEdit();
        });
        
        add(editButton, saveButton, cancelButton);
        
	}
	
	public OptionField (String label) {
		this(label, "", null);
	}
	
	public void setValueSavedListener(ValueSavedListener listener) {
		this.listener = listener;
	}
	
	public void setEditButtonClickedListener(EditButtonClickedListener editListener) {
		this.editListener = editListener;
	}
	
	public void setValue(String value) {
		this.value = value;
		field.setValue(value);
	}
	
	public void showError(String errorMessage) {
		Notification notification = new Notification(errorMessage, 3000, Position.MIDDLE);
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		notification.open();
	}
	
	public void reset() {
		field.setValue(value);
	}
	
	public void setTextField(TextFieldBase<?, String> field) {
		remove(this.field);
		this.field = field;
		field.setValue(value);
		field.setReadOnly(true);
		this.addComponentAsFirst(field);
	}
	
	private void startEdit() {
		editButton.setVisible(false);
		saveButton.setVisible(true);
		cancelButton.setVisible(true);
		field.setReadOnly(false);
	}
	
	private void endEdit() {
		editButton.setVisible(true);
		saveButton.setVisible(false);
		cancelButton.setVisible(false);
		field.setReadOnly(true);
	}
	
	private void save() {
		ValueSavedEvent event = new ValueSavedEvent(value, field.getValue(), this);
		if (listener != null) {
			listener.onSave(event);
		}
		value = field.getValue();
	}
	
	public TextFieldBase<?, String> getTextField() {
		return field;
	}
	
	public class ValueSavedEvent {
		
		private String oldValue, newValue;
		private OptionField source;
		
		ValueSavedEvent (String oldValue, String newValue, OptionField source) {
			this.oldValue = oldValue;
			this.newValue = newValue;
			this.source = source;
		}
		
		public String getOldValue() {
			return oldValue;
		}
		
		public String getNewValue() {
			return newValue;
		}

		public OptionField getSource() {
			return source;
		}
		
	}
	
	public interface ValueSavedListener {
		public void onSave(ValueSavedEvent event);
	}
	
	public interface EditButtonClickedListener {
		public void onClick();
	}
	
	public void setSaveButtonEnabled(boolean newStatus) {
		saveButton.setEnabled(newStatus);
	}

}

