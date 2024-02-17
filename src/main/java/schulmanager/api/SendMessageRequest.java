package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SendMessageRequest extends Request{
	
	private String message;
	private JsonObject thread;
	
	public SendMessageRequest(String message, JsonObject thread) {
		super("send-message", "messenger");
		this.message = message;
		this.thread  = thread;
	}
	
	public JsonObject createParameters(Session session) {
		JsonObject params = new JsonObject();
		params.addProperty("text", message);
		params.add("thread", thread);
		return params;
	}
	
	public JsonObject getMessage() {
		return result.get("data").getAsJsonObject();
	}
}