package schulmanager.api;

import com.google.gson.JsonObject;

public abstract class Request {
	
	private String endpointName, moduleName;
	
	protected JsonObject result;
	
	protected Request(String endpointName, String moduleName) {
		this.endpointName = endpointName;
		this.moduleName = moduleName;
	}
	
	public JsonObject toJson(Session session) {
		JsonObject request = new JsonObject();
		request.addProperty("endpointName", endpointName);
		request.addProperty("moduleName", moduleName);
		request.add("parameters", createParameters(session));
		
		return request;
	}
	
	public abstract JsonObject createParameters(Session session);
	
	void setResult(JsonObject result) {
		this.result = result;	
	}
	
	public JsonObject getResult() {
		return result;
	}
	
	public String getResultString() {
		return result.toString();
	}
	
	public JsonObject execute(Session session) {
		return new ApiCall(session, this).execute();
	}

}
