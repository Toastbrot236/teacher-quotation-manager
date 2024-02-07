package schulmanager.api;

import com.google.gson.JsonObject;

public class Session {
	
	private String authToken;
	private JsonObject student;
	
	private String username;
	private String password;
	private int id;
	
	public Session(String username, String password) {
		this.username = username;
		this.password = password;
		LoginCall login = new LoginCall(username, password);
		JsonObject result = login.execute();
		if (result == null || result.get("jwt") == null)
			throw new InvalidCredentialsException();
		authToken = result.get("jwt").getAsString();
		id = result.getAsJsonObject("user").get("id").getAsInt();
		student = result
				.get("user").getAsJsonObject()
				.get("associatedStudent").getAsJsonObject();
	}
	
	public void setAuthToken(String token) {
		authToken = token;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
	public JsonObject getStudent() {
		return student;
	}
	
	public int getStudentId() {
		return student.get("id").getAsInt();
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public int getId() {
		return id;
	}

}