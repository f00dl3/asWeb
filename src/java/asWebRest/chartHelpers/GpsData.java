/*
by Anthony Stump
Created: 17 Jun 2018
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class GpsData {
    
    private WebCommon wc = new WebCommon();
     
    private JSONObject gpsSpeed(JSONArray dataIn) {
        final String speed_Name = "GPS Speed";
        JSONObject speed_Glob = new JSONObject();
        JSONObject speed_Props = new JSONObject();
        JSONArray speed_Labels = new JSONArray();
        JSONArray speed_Data = new JSONArray();
        JSONArray debug_Data = new JSONArray();
        speed_Props
                .put("dateFormat", "ss")
                .put("chartName", speed_Name).put("chartFileName", "gpsSpeed")
                .put("sName", "Speed").put("sColor", "White")
                .put("xLabel", "MPH").put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                JSONObject tLog = tGpsLog.getJSONObject(jStr);
                if(j == 0) { debug_Data.put(tLog); }
                try {
                    if(wc.isSet(Double.toString(tLog.getDouble("SpeedMPH")))) { speed_Data.put(tLog.getDouble("SpeedMPH")); } else { speed_Data.put(0.00); }
                } catch (Exception e) {
                    speed_Data.put(0.00);
                } finally { 
                    speed_Labels.put(jStr);
                }
            }
        }
        speed_Glob
                .put("labels", speed_Labels)
                .put("data", speed_Data)
                .put("props", speed_Props)
                .put("debug", debug_Data);
        return speed_Glob;
    }
    
    public JSONObject getGpsSpeed(JSONArray dataIn) { return gpsSpeed(dataIn); }
        
}
