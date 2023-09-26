package service;

import com.vaadin.flow.server.VaadinService;

class SessionManager {
	
	static void saveValue(String attribute, Object value) {
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute(attribute, value);
	}
	
	static Object getValue(String attribute) {
		return VaadinService.getCurrentRequest().getWrappedSession().getAttribute(attribute);
	}
	
	@SuppressWarnings("unchecked")
	static <T> T getValue(String attribute, Class<T> type) {
		try {
			return (T) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(attribute);
		}
		catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}

}
