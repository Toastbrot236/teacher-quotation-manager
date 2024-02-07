package schulmanager.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiCall {
	
	private final String CALL_URL = "https://login.schulmanager-online.de/api/calls";
	private final String BUNDLE_VERSION = "bfb8ccf0e4e0dd0a";

	private ArrayList<Request> requests;
	private Session session;
	
	private JsonObject result;
	
	public ApiCall(Session session) {
		this.requests = new ArrayList<Request>();
		this.session = session;
	}
	
	public ApiCall(Session session, Request... requests) {
		this.requests = new ArrayList<Request>();
		this.session = session;
		for (Request r : requests) {
			this.requests.add(r);
		}
	}
	
	public void addRequest(Request request) {
		requests.add(request);
	}
	
	public void addRequests(Request... requests) {
		for (Request r : requests) {
			this.requests.add(r);
		}
	}
	
	private String generateRequestBody() {
		JsonObject body = new JsonObject();
		body.addProperty("bundleVersion", BUNDLE_VERSION);
		
		JsonArray arr = new JsonArray();
		for (Request r : requests) {
			arr.add(r.toJson(session));
		}
		body.add("requests", arr);
		
		return body.toString();
	}
	
	public JsonObject execute() {
		try {
			
			HttpURLConnection con = (HttpURLConnection) new URL(CALL_URL).openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + session.getAuthToken());
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
			passResultsToRequests();

			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private void passResultsToRequests() {
		JsonArray requestResults = result.get("results").getAsJsonArray();
		for (int i = 0; i < requests.size(); i++) {
			System.out.println(i + ": " + requestResults.get(i).getAsJsonObject().toString());
			requests.get(i).setResult(requestResults.get(i).getAsJsonObject());
		}
	}

}