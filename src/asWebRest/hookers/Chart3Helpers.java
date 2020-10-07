/*
by Anthony Stump
Created: 7 Oct 2020
Updated: 7 Oct 2020
 */

package asWebRest.hookers;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.action.GetWeatherAction;
import asWebRest.dao.WeatherDAO;

public class Chart3Helpers {
    
	public JSONArray getTestJson(Connection dbc) {
		
		JSONArray dataBack = new JSONArray();
		
		GetWeatherAction gwa = new GetWeatherAction(new WeatherDAO());
		JSONArray dataToContainerize = gwa.getObsJsonHome(dbc);
		
		for(int i = 0; i < dataToContainerize.length(); i++) {
			JSONArray dataSet = new JSONArray();
			JSONObject tObject = dataToContainerize.getJSONObject(i);
			String tTimestamp = "Undefined";
			int tValue = 0;
			try { tTimestamp = tObject.getString("Timestamp"); } catch (Exception e) { e.printStackTrace(); }
			try { tValue = tObject.getInt("Temperature"); } catch (Exception e) { e.printStackTrace(); }
			dataSet
				.put(tTimestamp)
				.put(tValue);
			dataBack.put(dataSet);
		}
		
		return dataBack;
		
	}
    
}
