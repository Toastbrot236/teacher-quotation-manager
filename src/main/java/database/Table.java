package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Table {
	
	public Table(ResultSet result) {
		table = new HashMap<String, ArrayList<Object>>();
		try {
			int columnCount = result.getMetaData().getColumnCount();
			for (int i  = 1; i < columnCount+1; i++) {
				table.put(result.getMetaData().getColumnName(i), new ArrayList<Object>());
			}
			while (result.next()) {
				for (int i  = 1; i < columnCount+1; i++) {
					table.get(result.getMetaData().getColumnName(i)).add(result.getObject(i));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	HashMap<String, ArrayList<Object>> table;
	
	public Object get(int rowNumber, String columnName) {
		return table.get(columnName).get(rowNumber);
	}
	
	public <T> ArrayList<T> getColumn(String columnName, Class<T> type) {
		return (ArrayList<T>) table.get(columnName);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(int rowNumber, String columnName, Class<T> type) {
		try {
			Object result = table.get(columnName).get(rowNumber);
			if (result == null)
				return null;
			else 
				return (T) result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(int rowNumber, String columnName, Class<T> type, T defaultValue) {
		try {
			Object result = table.get(columnName).get(rowNumber);
			if (result == null)
				return defaultValue;
			else 
				return (T) result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean getBool(int rowNumber, String columnName) {
		try {
			Object result = table.get(columnName).get(rowNumber);
			if (result == null) 
				return false;
			else
				return (boolean) result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int columnCount() {
		return table.size();
	}
	
	public int rowCount() {
		return table.values().toArray(new ArrayList[table.values().size()])[0].size();
	}
	
	public Row getRow(int rowNo) {
		Row r = new Row();
		for (String s : table.keySet()) {
			r.put(s, get(rowNo, s));
		}
		return r;
	}
	
	public Row[] getRows() {
		Row[] rs = new Row[rowCount()];
		for (int i = 0; i < rowCount(); i++) {
			rs[i] = getRow(i);
		}
		return rs;
	}

}
