package database;

import java.sql.SQLException;
import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String displayName;
    private Timestamp lastLogin;
    private boolean admin;
    private boolean read;
    private boolean write;
    private boolean rate;
    //private boolean darkMode;
    private int theme;
    private String email;
    private boolean edit;
    private boolean delete;
    private String password;
    private boolean test;
    private Integer smId;

    // without theme parameter, theme gets set depending on darkMode parameter's value
    public User(int userId, String username, String firstName, String lastName, String displayName, Timestamp lastLogin,
            boolean admin, boolean read, boolean write, boolean rate, boolean darkMode, String email,
            boolean edit, boolean delete, String password, boolean test, Integer smId) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.lastLogin = lastLogin;
        this.admin = admin;
        this.read = read;
        this.write = write;
        this.rate = rate;
        //this.darkMode = darkMode;
        this.theme = darkMode ? 1 : 0 ;
        this.email = email;
        this.edit = edit;
        this.delete = delete;
        this.password = password;
        this.test = test;
        this.smId = smId;
    }

    // with theme parameter
    public User(int userId, String username, String firstName, String lastName, String displayName, Timestamp lastLogin,
                boolean admin, boolean read, boolean write, boolean rate, int theme, String email,
                boolean edit, boolean delete, String password, boolean test, Integer smId) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.lastLogin = lastLogin;
        this.admin = admin;
        this.read = read;
        this.write = write;
        this.rate = rate;
        this.theme = theme;
        this.email = email;
        this.edit = edit;
        this.delete = delete;
        this.password = password;
        this.test = test;
        this.smId = smId;
    }

    public User() {
	}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        databaseUpdate("user_id", String.valueOf(userId));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        databaseUpdate("user_username", "'" + username + "'");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        databaseUpdate("user_firstName", "'" + firstName + "'");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        databaseUpdate("user_lastName", "'" + lastName + "'");
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        databaseUpdate("user_displayName", "'" + displayName + "'");
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
        databaseUpdate("user_lastLogin", "'" + lastLogin.toString());
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        databaseUpdate("user_admin", admin ? "1" : "0");
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
        databaseUpdate("user_read", read ? "1" : "0");
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
        databaseUpdate("user_write", write ? "1" : "0");
    }

    public boolean isRate() {
        return rate;
    }

    public void setRate(boolean rate) {
        this.rate = rate;
        databaseUpdate("user_rate", rate ? "1" : "0");
    }

    public boolean isDarkMode() {
        //return darkMode;
        return this.theme == 1;
    }

    public void setDarkMode(boolean darkMode) {
        this.setTheme( darkMode ? 1 : 0 );
        // databaseUpdate("user_darkMode", darkMode ? "1" : "0");
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
        databaseUpdate("user_darkMode", String.valueOf(theme));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        databaseUpdate("user_email", "'" + email + "'");
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        databaseUpdate("user_edit", edit ? "1" : "0");
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
        databaseUpdate("user_delete", delete ? "1" : "0");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        databaseUpdate("user_password", password);
    }
    
    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
        databaseUpdate("user_test", test ? "1" : "0");
    }
    
    public Integer getSmId() {
    	return smId;
    }
    
    public void setSmId(Integer smId) {
    	this.smId = smId;
    	databaseUpdate("user_smId", smId.toString());
    }
    
    public static User fromRow(Row row) {
        int userId = row.get("user_id", Integer.class);
        String username = row.get("user_username", String.class);
        String firstName = row.get("user_firstName", String.class);
        String lastName = row.get("user_lastName", String.class);
        String displayName = row.get("user_displayName", String.class);
        Timestamp lastLogin = row.get("user_lastLogin", Timestamp.class);
        boolean admin = row.get("user_admin", Boolean.class);
        boolean read = row.get("user_read", Boolean.class);
        boolean write = row.get("user_write", Boolean.class);
        boolean rate = row.get("user_rate", Boolean.class);
        boolean darkMode = row.get("user_darkMode", Boolean.class);
        // transition from darkMode to theme by setting theme to 1 if darkMode is true, leaving theme
        // at 0 if darkMode is false, moving over whether the user has been using darkmode before or not,
        // then setting darkMode to false so the if block doesnt overwrite the theme setting over and over again
        int theme = 0;
        if (darkMode) {
            theme = 1;
            //this.setDarkMode(false);
        }
        String email = row.get("user_email", String.class);
        boolean edit = row.get("user_edit", Boolean.class);
        boolean delete = row.get("user_delete", Boolean.class);
        String password = row.get("user_password", String.class);
        boolean test = row.get("user_test", Boolean.class);
        Integer smId = row.get("user_smId", Integer.class);

        return new User(userId, username, firstName, lastName, displayName, lastLogin, admin, read, write, rate,
                        theme, email, edit, delete, password, test, smId);
    }
    
    public void databaseUpdate(String columnName, String newValue) {
    	try {
			new TableReceiver().runUpdate("UPDATE user SET " + columnName + " = '" + newValue + "' WHERE user_id = " + userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public String get(String columnName) {
    	String value = "";

    	switch (columnName) {
        case "user_id":
            value = String.valueOf(getUserId());
            break;
        case "user_username":
            value = getUsername();
            break;
        case "user_firstName":
            value = getFirstName();
            break;
        case "user_lastName":
            value = getLastName();
            break;
        case "user_displayName":
            value = getDisplayName();
            break;
        case "user_email":
            value = getEmail();
            break;
        case "user_password":
            value = getPassword();
            break;
    };
    	
    	return value;
    }
    
    public boolean getBoolean(String columnName) {
        boolean value = false;

        switch (columnName) {
            case "user_admin":
                value = isAdmin();
                break;
            case "user_read":
                value = isRead();
                break;
            case "user_write":
                value = isWrite();
                break;
            case "user_rate":
                value = isRate();
                break;
            case "user_darkMode":
                value = isDarkMode();
                break;
            case "user_edit":
                value = isEdit();
                break;
            case "user_delete":
                value = isDelete();
                break;
            case "user_connected":
            	value = smId != null;
        };

        return value;
    }
    
    public static String nameFormat(String firstName, String lastName, String displayName) {
    	if (displayName == null)
    		return firstName + " " + lastName;
    	return displayName + " (" + firstName + " " + lastName.charAt(0) + ".)";
    }
    
    public static void updateNumber(int userId, String columnName, String newValue) {
    	try {
			new TableReceiver().runUpdate("UPDATE user SET " + columnName + " = " + newValue + " WHERE user_id = " + userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param userId
     * @return String array of length 2, where index 0 contains username and index 1 contains password
     */
    public static String[] smCredentials(int userId) {
    	try {
			Table result = new TableReceiver().
					runQueryAndGet(
							"SELECT smCredentials_username, smCredentials_password FROM smCredentials JOIN user ON user_smId = smCredentials_id WHERE user_id = " + userId);
			if (result.rowCount() == 0)
				return null;
			return new String[] {
					result.get(0, "smCredentials_username", String.class),
					result.get(0, "smCredentials_password", String.class)
			};
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }

}
