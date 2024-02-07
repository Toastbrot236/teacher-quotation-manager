package components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;

import database.User;

public class UserEditCheckbox extends UserEdit<Boolean> {

	private Checkbox checkbox;
	
	public UserEditCheckbox(User user, String columnName) {
		super(user, columnName);
	}
	
	@Override
	public Component create() {
		checkbox = new Checkbox();
		checkbox.setValue(value);
		checkbox.addValueChangeListener(e -> {
			if (e.getValue() != value) {
				edited = true;
				checkbox.getStyle().set("outline", "4px solid var(--lumo-warning-color)");
			}
			else {
				edited = false;
				checkbox.getStyle().set("outline", null);
			}
		});
		
		if (columnName.equals("user_admin") || columnName.equals("user_connected")) {
			checkbox.setReadOnly(true);
		}
		
		return checkbox;
	}

	@Override
	public Boolean getInitialValue() {
		boolean value = user.getBoolean(columnName);
		return value;
	}
	
	public String getValue() {
		return (checkbox.getValue() == true) ? "1" : "0";
	}
	
	@Override
	public void save() {
		super.save();
		value = checkbox.getValue();
		checkbox.getStyle().set("outline", null);
	}

}
