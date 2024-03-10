package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import service.DataManager;

public class ChatUsersRequest extends Request {
	
	public ChatUsersRequest() {
		super("get-chat-users", "messenger");
		hasParameters = false;
	}
	
	public JsonObject[] getUsers() {
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
	
	/*public static void main(String[] args) {
		Session s = new Session("haijo1018@gmail.com", "Avalon1810!");
		ApiCall call = new ApiCall(s);
		ChatUsersRequest r1 = new ChatUsersRequest();
		DistributionListsRequest r2 = new DistributionListsRequest();
		call.addRequests(r1, r2);
		call.execute();
		System.out.println(r1.getUsers()[3]);
		System.out.println(r2.getLists()[0]);
	}*/
	
}
