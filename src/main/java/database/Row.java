package database;

import java.util.HashMap;

public class Row extends HashMap<String, Object> {
	
	public <T> T get(String key, Class<T> type) {
		Object result = get(key);
		if (result == null)
			return null;
		try {
			return (T) get(key);
		}
		catch (Exception e) {
			throw new ClassCastException();
		}
		
	}

}
