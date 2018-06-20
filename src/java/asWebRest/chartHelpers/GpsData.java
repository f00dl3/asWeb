/*
by Anthony Stump
Created: 17 Jun 2018
Updated: 19 Jun 2018
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class GpsData {
    
    private WebCommon wc = new WebCommon();
          
    private JSONObject gpsElement(JSONArray dataIn, String whatToDo, String xUnits, String dataPoint) {
        final String name = "GPS " + whatToDo;
        final String fileName = "gps" + whatToDo;
        JSONObject glob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        props
                .put("dateFormat", "ss")
                .put("chartName", name).put("chartFileName", fileName)
                .put("sName", whatToDo).put("sColor", "White")
                .put("xLabel", xUnits).put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                JSONObject tLog = tGpsLog.getJSONObject(jStr);
                try {
                    if(wc.isSet(Double.toString(tLog.getDouble(dataPoint)))) { data.put(tLog.getDouble(dataPoint)); } else { data.put(0.00); }
                } catch (Exception e) {
                    data.put(0.00);
                } finally { 
                    labels.put(jStr);
                }
            }
        }
        glob
                .put("labels", labels)
                .put("data", data)
                .put("props", props)
                .put("debug", data);
        return glob;
    }
        
    public JSONObject getGpsElement(JSONArray dataIn, String whatToDo, String xUnits, String dataPoint) { return gpsElement(dataIn, whatToDo, xUnits, dataPoint); }
        
}
