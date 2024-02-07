package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import elemental.json.Json;
import elemental.json.JsonBoolean;

public class SubscriptionsRequest extends Request {

	public SubscriptionsRequest() {
		super("get-subscriptions", "messenger");
	}

	@Override
	public JsonObject createParameters(Session session) {
		JsonObject params = new JsonObject();
		params.addProperty("all", false);
		params.addProperty("reason", "whenLoadedSubscriptions");
		return params;
	}
	
	public JsonObject[] getSubscriptions() {
		JsonArray arr = result.get("data").getAsJsonArray();
		JsonObject[] subscriptions = new JsonObject[arr.size()];
		for (int i = 0; i < subscriptions.length; i++) {
			subscriptions[i] = arr.get(i).getAsJsonObject();
		}
		return subscriptions;
	}

}
