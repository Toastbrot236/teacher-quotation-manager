package schulmanager.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ActualLessonsRequest extends Request{
	
	private String startDate;
	private String endDate;
	
	public ActualLessonsRequest(String startDate, String endDate) {
		super("get-actual-lessons", "schedules");
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public JsonObject createParameters(Session session) {
		JsonObject params = new JsonObject();
		params.addProperty("start", startDate);
		params.addProperty("end", endDate);
		params.add("student", session.getStudent());
		return params;
	}
	
	public JsonObject[] getLessons() {
		JsonArray arr = result.get("data").getAsJsonArray();
		JsonObject[] lessons = new JsonObject[arr.size()];
		for (int i = 0; i < lessons.length; i++) {
			lessons[i] = arr.get(i).getAsJsonObject();
		}
		return lessons;
	}

}
