package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Only works with single persons as recipients right now!
 */
public class CreateThreadRequest extends Request{
	
	private String topic, message;
	private JsonObject recipient;
	private boolean singlePerson;
	
	public CreateThreadRequest(JsonObject recipient, String topic, String message, boolean singlePerson) {
		super("create-thread", "messenger");
		this.message = message;
		this.topic  = topic;
		this.recipient = recipient;
		this.singlePerson = singlePerson;
	}
	
	public JsonObject createParameters(Session session) {
		if (!singlePerson) return null; //TODO
		
		JsonObject params = new JsonObject();
		
		params.addProperty("allowAnswers", true);
		params.addProperty("subject", topic);
		
		JsonObject firstMessage = new JsonObject();
		firstMessage.add("attachments", new JsonArray());
		firstMessage.addProperty("text", message);
		params.add("firstMessage", firstMessage);
		
		JsonArray recipientOptions = new JsonArray();
		JsonObject recipientOption = new JsonObject();
		recipientOption.addProperty("name", recipient.get("name").getAsString());
		recipientOption.addProperty("subtext", recipient.get("subtext").getAsString());
		recipientOption.addProperty("type", "user");
		recipientOption.add("user", recipient);
		recipientOptions.add(recipientOption);
		params.add("recipientOptions", recipientOptions);
		
		JsonArray users = new JsonArray();
		JsonObject user = new JsonObject();
		user.addProperty("id", recipient.get("id").getAsNumber());
		users.add(user);
		params.add("users", users);
		
		return params;
	}
	
	public JsonObject getMessage() {
		return result.get("data").getAsJsonObject();
	}
}