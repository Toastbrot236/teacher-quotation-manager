package service;

import java.sql.SQLException;
//import java.time.Duration;
import java.util.ArrayList;

import com.abi.quotes.views.MainLayout;
import com.google.gson.JsonObject;
import com.vaadin.flow.server.VaadinService;

import schulmanager.api.Session;
import database.Row;
import database.Table;
import database.TableReceiver;
import database.User;
import jakarta.servlet.http.Cookie;

public class DataManager {

	
	public static MainLayout setMainLayout(MainLayout mainLayout) {
		SessionManager.saveValue("mainLayout", mainLayout);
		return mainLayout;
	}
	
	public static MainLayout getMainLayout() {
		return SessionManager.getValue("mainLayout", MainLayout.class);
	}
	//////////////////////
	//SETTER
	
	public static int setUserID (int id) {
		SessionManager.saveValue("userID", id);
		return id;
	}
	
	public static String setPassword (String username) {
		SessionManager.saveValue("password", username);
		return username;
	}
	
	public static String setEmail (String username) {
		SessionManager.saveValue("email", username);
		return username;
	}
	
	public static String setUsername (String username) {
		SessionManager.saveValue("username", username);
		return username;
	}
	
	public static String setFirstName (String name) {
		SessionManager.saveValue("firstName", name);
		return name;
	}
	
	public static String setLastName (String name) {
		SessionManager.saveValue("lastName", name);
		return name;
	}
	
	public static String setDisplayName (String name) {
		SessionManager.saveValue("displayName", name);
		return name;
	}
	
	public static boolean setDarkMode (boolean dark) {
		SessionManager.saveValue("darkMode", dark);
		return dark;
	}
	
	public static boolean setLoggedIn (boolean loggedIn) {
		SessionManager.saveValue("loggedIn", loggedIn);
		return loggedIn;
	}
	
	public static Permission[] setPermissions (Permission[] permissions) {
		SessionManager.saveValue("permissions", permissions);
		return permissions;
	}
	
	public static int setQuoteDetail (int id) {
		SessionManager.saveValue("quoteDetail", id);
		return id;
	}
	
	public static int setTeacherDetail (int id) {
		SessionManager.saveValue("teacherDetail", id);
		return id;
	}
	
	public static Row setTeacherDetailRow (Row row) {
		SessionManager.saveValue("teacherDetailRow", row);
		return row;
	}
	
	public static boolean setFirstLogin(boolean firstLogin) {
		SessionManager.saveValue("firstLogin", firstLogin);
		return firstLogin;
	}
	
	public static Integer setSmId(Integer smId) {
		SessionManager.saveValue("smId", smId);
		return smId;
	}
	
	public static Session setSmSession(Session session) {
		SessionManager.saveValue("smSession", session);
		return session;
	}
	
	//////////////////////
	//GETTER
	
	public static Integer getUserID () {
		Integer result = SessionManager.getValue("userID", Integer.class);
		if (result == null)
			return -1;
		return result;
	}
	
	public static String getPassword () {
		return SessionManager.getValue("password", String.class);
	}
	
	public static String getEmail () {
		return SessionManager.getValue("email", String.class);
	}
	
	public static String getUsername () {
		return SessionManager.getValue("username", String.class);
	}
	
	public static String getFirstName () {
		return SessionManager.getValue("firstName", String.class);
	}
	
	public static String getLastName () {
		return SessionManager.getValue("lastName", String.class);
	}
	
	public static String getDisplayName () {
		return SessionManager.getValue("displayName", String.class);
	}
	
	public static Boolean getDarkMode () {
		return SessionManager.getValue("darkMode", Boolean.class);
	}
	
	public static Boolean getLoggedIn () {
		return SessionManager.getValue("loggedIn", Boolean.class);
	}
	
	public static Permission[] getPermissions () {
		return SessionManager.getValue("permissions", Permission[].class);
	}
	
	public static int getQuoteDetail () {
		return SessionManager.getValue("quoteDetail", Integer.class);
	}
	
	public static int getTeacherDetail () {
		return SessionManager.getValue("teacherDetail", Integer.class);
	}
	
	public static Row getTeacherDetailRow () {
		return SessionManager.getValue("teacherDetailRow", Row.class);
	}
	
	public static boolean getFirstLogin() {
		return SessionManager.getValue("firstLogin", Boolean.class);
	}
	
	/**
	 * Returns the Schulmanager's student ID of the user. If accounts are not connected, this will always return null, whereas every other return value proves a connection.
	 * @return
	 */
	public static Integer getSmId() {
		return SessionManager.getValue("smId", Integer.class);
	}
	
	public static boolean accountsConnected() {
		return getSmId() != null;
	}
	
	public static Session getSmSession() {
		return SessionManager.getValue("smSession", Session.class);
	}

	////////////////////////
	//Other stuff
	
	/*private void databaseUpdate(String param, String value) {
		try {
			new TableReceiver().runUpdate("UPDATE user SET " + param + " = '" + value + "' WHERE user_id = " + getUserID());
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}*/
	
	public static void login(int userId) {
		try {
			setUserID(userId);
			setLoggedIn(true);
			Table results = new TableReceiver().runQueryAndGet("SELECT * FROM user WHERE user_id = " + userId);
			new TableReceiver().runUpdate("UPDATE user SET user_lastLogin = now() WHERE user_id = " + userId);
			Row r = results.getRow(0);
			
			User user = User.fromRow(r);
			
	        setUsername(user.getUsername());
	        setPassword(user.getPassword());
	        setEmail(user.getEmail());
	        setFirstName(user.getFirstName());
	        setLastName(user.getLastName());
	        setDisplayName(user.getDisplayName());
	        setDarkMode(user.isDarkMode());
	        setFirstLogin(user.getLastLogin() == null);
	        setSmId(user.getSmId());
	        
	        ArrayList<Permission> permissions = new ArrayList<Permission>();
	        if (user.isAdmin()) {
	        	permissions.add(Permission.ADMIN);
	        }
	        if (user.isRead()) {
	            permissions.add(Permission.READ);
	        }
	        if (user.isWrite()) {
	            permissions.add(Permission.WRITE);
	        }
	        if (user.isRate()) {
	            permissions.add(Permission.RATE);
	        }
	        if (user.isEdit()) {
	            permissions.add(Permission.EDIT);
	        }
	        if (user.isDelete()) {
	            permissions.add(Permission.DELETE);
	        }
	        if (user.isTest()) {
	        	permissions.add(Permission.TEST);
	        }
	        setPermissions(permissions.toArray(new Permission[permissions.size()]));
	        
	        if (getMainLayout() != null) {
	        	getMainLayout().updateUserSpan();
	        	getMainLayout().updateMoreButton();
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void logout() {
		setLoggedIn(false);
	}
	
	public static void setLoginCookies(String name, String password) {
		Cookie nameCookie = new Cookie("name", name);
		nameCookie.setMaxAge(90000000);
		nameCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		VaadinService.getCurrentResponse().addCookie(nameCookie);
		
		Cookie passwordCookie = new Cookie("password", password);
		passwordCookie.setMaxAge(90000000);
		passwordCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		VaadinService.getCurrentResponse().addCookie(passwordCookie);
	}
	
	public static boolean canRead() {
		return Permission.canRead(getPermissions());
	}
	
	public static boolean canWrite() {
		return Permission.canWrite(getPermissions());
	}
	
	public static boolean canRate() {
		return Permission.canRate(getPermissions());
	}
	
	public static boolean canEdit() {
		return Permission.canEdit(getPermissions());
	}
	
	public static boolean canDelete() {
		return Permission.canDelete(getPermissions());
	}
	
	public static boolean isAdmin() {
		return Permission.isAdmin(getPermissions());
	}
	
	public static boolean canTest() {
		return Permission.canTest(getPermissions());
	}
	
	//IGNORE, BAD STYLE FOLLOWING:
	
	public static void setSubscription(JsonObject sub) {
		SessionManager.saveValue("subscription", sub);
	}
	
	public static JsonObject getSubscription() {
		return SessionManager.getValue("subscription", JsonObject.class);
	}

}
