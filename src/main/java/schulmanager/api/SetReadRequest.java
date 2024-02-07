package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SetReadRequest extends Request {

	JsonObject message, subscription;
	
	public SetReadRequest(JsonObject message, JsonObject subscription) {
		super("set-subscription-read", "messenger");
		this.message = message;
		this.subscription = subscription;
	}

	@Override
	public JsonObject createParameters(Session session) {
		JsonObject params = new JsonObject();
		params.add("message", message);
		params.add("subscription", subscription);
		return params;
	}

}
