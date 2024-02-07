package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MessagesRequest extends Request {

	private int subscriptionId;
	
	public MessagesRequest(int subscriptionId) {
		super("get-messages-by-subscription", "messenger");
		this.subscriptionId = subscriptionId;
	}

	@Override
	public JsonObject createParameters(Session session) {
		JsonObject params = new JsonObject();
		params.addProperty("subscriptionId", subscriptionId);
		return params;
	}
	
	public JsonObject[] getMessages() {
		JsonArray arr = result.getAsJsonObject("data").get("messages").getAsJsonArray();
		JsonObject[] messages = new JsonObject[arr.size()];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = arr.get(i).getAsJsonObject();
		}
		return messages;
	}

}