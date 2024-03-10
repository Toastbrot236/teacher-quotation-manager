package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import service.DataManager;

public class DistributionListsRequest extends Request {
	
	public DistributionListsRequest() {
		super("get-distribution-lists", "messenger");
		hasParameters = false;
	}
	
	public JsonObject[] getLists() {
		JsonArray arr = result.getAsJsonArray("data");
		JsonObject[] lists = new JsonObject[arr.size()];
		for (int i = 0; i < lists.length; i++) {
			lists[i] = arr.get(i).getAsJsonObject();
		}
		return lists;
	}

	@Override
	public JsonObject createParameters(Session session) {
		return null;
	}
	
}
