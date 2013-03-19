package com.example.smsinformer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CentreonMessages {
	ArrayList<CentreonMessage> messages = new ArrayList<CentreonMessage>();
	int counter = 0;

	public boolean set(String json) {
		messages.clear();
		// cut last char
		if (json.length() > 2)
			json = json.substring(0, json.length() - 2);
		json = json.replaceAll("(\\r|\\n)", "");
		json = "{\"Messages\":[" + json + "]}";

		try {
			JSONObject jObject = new JSONObject(json);
			JSONArray jArray = jObject.getJSONArray("Messages");
			for (int i = 0; i < jArray.length(); i++) {
				CentreonMessage m = new CentreonMessage();
				JSONObject oneObject = jArray.getJSONObject(i);
				m.address = oneObject.getString("Address");
				m.host = oneObject.getString("Host");
				m.host_alias = oneObject.getString("Host alias");
				m.phone = oneObject.getString("PHONE");
				m.service_description = oneObject
						.getString("Service description");
				m.state = oneObject.getString("State");
				m.time = Integer.parseInt(oneObject.getString("TIME"));
				m.type = oneObject.getString("Notification Type");
				messages.add(m);
			}
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	CentreonMessage getNew(long time) {
		if (counter != 0 && counter + 1 < messages.size()) {
			counter++;
			return messages.get(counter);
		} else if (counter + 1 >= messages.size())
			return null;
		else if (counter == 0) {
			for (int i = 0; i < messages.size(); i++) {
				if (messages.get(i).time > time) {
					counter = i;
					return messages.get(counter);
				}
			}
		}
		return null;
	}
}
