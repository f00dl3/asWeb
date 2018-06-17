/*
by Anthony Stump
Created: 17 Jun 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class GpsData {
     
    private JSONObject gpsSpeed(JSONArray dataIn) {
        final String speed_Name = "GPS Speed";
        JSONObject speed_Glob = new JSONObject();
        JSONObject speed_Props = new JSONObject();
        JSONArray speed_Labels = new JSONArray();
        JSONArray speed_Data = new JSONArray();
        speed_Props
                .put("chartName", speed_Name).put("chartFileName", "gpsSpeed")
                .put("sName", "Speed").put("sColor", "White")
                .put("xLabel", "Interval").put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = new JSONObject(thisObject.getJSONObject("gpsLog"));
            for(int j = 0; j < tGpsLog.length(); j++) {
                JSONObject tLog = tGpsLog.getJSONObject(Integer.toString(j));
                speed_Data.put(tLog.getDouble("SpeedMPH"));
            }
        }
        speed_Glob
                .put("labels", speed_Labels)
                .put("data", speed_Data)
                .put("props", speed_Props);
        return speed_Glob;
    }
    
    public JSONObject getGpsSpeed(JSONArray dataIn) { return gpsSpeed(dataIn); }
        
}
