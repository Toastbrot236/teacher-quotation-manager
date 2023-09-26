package com.abi.processing;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoIcon;

public enum SortingType {

	NEWEST("quotes_published DESC", "Neueste", VaadinIcon.CLOCK.create(), LumoIcon.ARROW_UP.create()),
	OLDEST("quotes_published", "Älteste", VaadinIcon.CLOCK.create(), LumoIcon.ARROW_DOWN.create()),
	POPULARITY_ASC("(likes - dislikes) DESC, likes DESC;", "Beliebteste", VaadinIcon.THUMBS_UP.create(), LumoIcon.ARROW_UP.create()),
    //POPULARITY_DESC("(likes - dislikes) ASC, dislikes ASC", "Unbeliebteste", VaadinIcon.THUMBS_UP.create(), LumoIcon.ARROW_DOWN.create()),
	TEACHER_NAME_ASC("teachers_name, quotes_published DESC", "Lehrer (A-Z)", VaadinIcon.SPECIALIST.create(), LumoIcon.ARROW_UP.create()),
    TEACHER_NAME_DESC("teachers_name DESC, quotes_published DESC", "Lehrer (Z-A)", VaadinIcon.SPECIALIST.create(), LumoIcon.ARROW_DOWN.create()),
    USER_NAME_ASC("user_firstName, user_lastName, quotes_published DESC", "Nutzer (A-Z)", VaadinIcon.USER.create(), LumoIcon.ARROW_UP.create()),
    USER_NAME_DESC("user_firstName DESC, user_lastName DESC, quotes_published DESC", "Nutzer (Z-A)", VaadinIcon.USER.create(), LumoIcon.ARROW_DOWN.create()),
    EDITED("quotes_lastEdited DESC, teachers_name", "Zuletzt bearbeitet", VaadinIcon.PENCIL.create(), LumoIcon.ARROW_UP.create());
    

	
	private String query, description;
	private Component iconOne, iconTwo;
	
	private SortingType(String query, String description, Component iconOne, Component iconTwo) {
		this.query = query;
		this.description = description;
		this.iconOne = iconOne;
		this.iconTwo = iconTwo;
	}

	public Component getIconOne() {
		return iconOne;
	}

	public Component getIconTwo() {
		return iconTwo;
	}

	public String getQuery() {
		return query;
	}

	public String getDescription() {
		return description;
	}
	
}
