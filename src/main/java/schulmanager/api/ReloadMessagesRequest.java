package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ReloadMessagesRequest extends Request {

	private String lastMessageTimestamp;
	
	public ReloadMessagesRequest(String lastMessageTimestamp) {
		super("reload-messages", "messenger");
		this.lastMessageTimestamp = lastMessageTimestamp;
	}

	@Override
	public JsonObject createParameters(Session session) {
		JsonObject params = new JsonObject();
		params.addProperty("lastMessageTimestamp", lastMessageTimestamp);
		return params;
	}
	
}