package components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import database.User;

public class UserEditTextField extends UserEdit<String> {

	private TextField field;
	
	public UserEditTextField(User user, String columnName) {
		super(user, columnName);
	}

	@Override
	public Component create() {
		field = new TextField();
		field.setWidthFull();
		field.setValue(value);
		field.addValueChangeListener(e -> {
			if (!e.getValue().equals(value)) {
				edited = true;
				field.getStyle().set("outline", "4px solid var(--lumo-warning-color)");
			}
			else {
				edited = false;
				field.getStyle().set("outline", null);
			}
		});
		
		return field;
	}

	@Override
	public String getInitialValue() {
		String value = user.get(columnName);
		return (value == null) ? "" : value;
	}
	
	public String getValue() {
		return field.getValue();
	}
	
	@Override
	public void save() {
		super.save();
		value = getValue();
		field.getStyle().set("outline", null);
	}
	
}
