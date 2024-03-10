package database;

import java.sql.SQLException;
import java.sql.Timestamp;

import components.QuoteBox;

public class Quote {
    private int id;
    private String category;
    private String teachersGender;
    private String teachersName;
    private Timestamp published;
    private String userFirstName;
    private String userLastName;
    private String userDisplayName;
    private String text;
    private Timestamp lastEdited;
    private Integer lastEditedBy;
    private long likes;
    private long dislikes;
    private long comments;
    private boolean isStar;
    private boolean isLiked, isDisliked;
    private int teachersId;

    public Quote(int id, String category, String teachersGender, String teachersName, Timestamp published,
                 String userFirstName, String userLastName, String userDisplayName, String text, Timestamp lastEdited,
                 Integer lastEditedBy, long likes, long dislikes, boolean isStar, boolean isLiked, boolean isDisliked, 
                 long comments, int teachersId) {
        this.id = id;
        this.category = category;
        this.teachersGender = teachersGender;
        this.teachersName = teachersName;
        this.published = published;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userDisplayName = userDisplayName;
        this.text = text;
        this.lastEdited = lastEdited;
        this.lastEditedBy = lastEditedBy;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isStar = isStar;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.comments = comments;
        this.teachersId = teachersId;
    }

    private void databaseUpdate(String columnName, String newValue) {
        try {
            new TableReceiver().runUpdate("UPDATE quotes SET " + columnName + " = " + newValue + " WHERE quotes_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        databaseUpdate("quotes_id", String.valueOf(id));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        databaseUpdate("quotes_category", "'" + category + "'");
    }

    public String getTeachersGender() {
        return teachersGender;
    }

    public void setTeachersGender(String teachersGender) {
        this.teachersGender = teachersGender;
        databaseUpdate("teachers_gender", "'" + teachersGender + "'");
    }

    public String getTeachersName() {
        return teachersName;
    }

    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
        databaseUpdate("teachers_name", "'" + teachersName + "'");
    }

    public Timestamp getPublished() {
        return published;
    }

    public void setPublished(Timestamp published) {
        this.published = published;
        databaseUpdate("quotes_published", "'" + published.toString() + "'");
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
        databaseUpdate("user_firstName", "'" + userFirstName + "'");
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
        databaseUpdate("user_lastName", "'" + userLastName + "'");
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
        databaseUpdate("user_displayName", "'" + userDisplayName + "'");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        databaseUpdate("quotes_text", "'" + text + "'");
    }

    public Timestamp getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(String lastEdited) {
        //this.lastEdited = Timestamp.valueOf(lastEdited);
        databaseUpdate("quotes_lastEdited", lastEdited);
    }

    public Integer getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(Integer lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
        databaseUpdate("quotes_lastEditedBy", "'" + lastEditedBy + "'");
    }

    public long getLikes() {
        return likes;
    }

    public long getDislikes() {
        return dislikes;
    }
    
    public long getComments() {
        return comments;
    }

    public boolean isStar() {
        return isStar;
    }
    
    public boolean isLiked() {
    	return isLiked;
    }
    
    public boolean isDisliked() {
    	return isDisliked;
    }
    
    public int getTeachersId() {
    	return teachersId;
    }

    public static Quote fromRow(Row row) {
        int id = row.get("quotes_id", Integer.class);
        String category = row.get("quotes_category", String.class);
        String teachersGender = row.get("teachers_gender", String.class);
        String teachersName = row.get("teachers_name", String.class);
        Timestamp published = row.get("quotes_published", Timestamp.class);
        String userFirstName = row.get("user_firstName", String.class);
        String userLastName = row.get("user_lastName", String.class);
        String userDisplayName = row.get("user_displayName", String.class);
        String text = row.get("quotes_text", String.class);
        Timestamp lastEdited = row.get("quotes_lastEdited", Timestamp.class);
        Integer lastEditedBy = row.get("quotes_lastEditedBy", Integer.class);
        long likes = row.get("likes", Long.class);
        long dislikes = row.get("dislikes", Long.class);
        long comments = row.get("comments", Long.class);
        boolean isStar = false;
        int teachersId = row.get("teachers_id", Integer.class);
        try {
        	isStar = row.get("isStar", Long.class) > 0;
        }
        catch (Exception e) {
        	isStar = row.get("isStar", Integer.class) > 0;
        }
        
        boolean isLiked = false;
        try {
        	isLiked = row.get("isLiked", Long.class) > 0;
        }
        catch (Exception e) {
        	isLiked = row.get("isLiked", Integer.class) > 0;
        }
        
        boolean isDisliked = false;
        try {
        	isDisliked = row.get("isDisliked", Long.class) > 0;
        }
        catch (Exception e) {
        	isDisliked = row.get("isDisliked", Integer.class) > 0;
        }

        return new Quote(id, category, teachersGender, teachersName, published, userFirstName, userLastName,
                userDisplayName, text, lastEdited, lastEditedBy, likes, dislikes, isStar, isLiked, isDisliked, 
                comments, teachersId);
    }
    
}