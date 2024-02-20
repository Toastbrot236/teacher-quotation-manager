package schulmanager.api;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;

public class LoginCall {
	
	private final String LOGIN_URL = "https://login.schulmanager-online.de/api/login";

	private String username, password;
	private JsonObject result;
	
	public LoginCall(String username, String password) {
		this.username = username;
		this.password = password;
		System.out.println("LoginCall instantiated with emailOrUsername = " + username);
	}
	
	private String generateRequestBody() {
		JsonObject body = new JsonObject();
		body.addProperty("emailOrUsername", username);
		body.addProperty("password", password);
		body.addProperty("mobileApp", false);
		body.add("institutionId", JsonNull.INSTANCE);
		
		return body.toString();
	}
	
	public JsonObject execute() {
		try {
			
			HttpURLConnection con = (HttpURLConnection) new URL(LOGIN_URL).openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.connect();
			
			//Write to server
			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
			writer.write(generateRequestBody());
			writer.close();
			
			//Get response from server
			InputStream is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
			reader.close();
			
			result = response;
			System.out.println(response.toString());
			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JsonObject getResult() {
		return result;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}