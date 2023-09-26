package components;

import com.vaadin.flow.component.Component;

import database.User;

public abstract class UserEdit<T> {
	
	protected boolean edited;

	protected User user;
	protected String columnName;
	protected T value;
	
	public UserEdit(User user, String columnName) {
		this.edited = false;
		
		this.user = user;
		this.columnName = columnName;
		this.value = getInitialValue();
	}
	
	public void save() {
		if (edited)
			updateDatabase(getValue());
		edited = false;
	}
	
	void updateDatabase(String newValue) {
		user.databaseUpdate(columnName, newValue);
	}
	
	public abstract T getInitialValue();
	
	protected abstract String getValue();
	
	public abstract Component create();

}
