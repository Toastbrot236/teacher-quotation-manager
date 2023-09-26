package database;

import java.sql.Timestamp;

public class Comment {
    private int commentId;
    private String text;
    private Timestamp published;
    private int authorId;
    private String userFirstName;
    private String userLastName;
    private String userDisplayName;

    public Comment(int commentId, String text, Timestamp published, int authorId, String userFirstName, String userLastName, String userDisplayName) {
        this.commentId = commentId;
        this.text = text;
        this.published = published;
        this.authorId = authorId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userDisplayName = userDisplayName;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getPublished() {
        return published;
    }

    public void setPublished(Timestamp published) {
        this.published = published;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public static Comment fromRow(Row row) {
        int commentId = row.get("comments_id", Integer.class);
        String text = row.get("comments_text", String.class);
        Timestamp published = row.get("comments_published", Timestamp.class);
        int authorId = row.get("comments_author", Integer.class);
        String userFirstName = row.get("user_firstName", String.class);
        String userLastName = row.get("user_lastName", String.class);
        String userDisplayName = row.get("user_displayName", String.class);

        return new Comment(commentId, text, published, authorId, userFirstName, userLastName, userDisplayName);
    }
}