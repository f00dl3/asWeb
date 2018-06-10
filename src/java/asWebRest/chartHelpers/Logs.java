/*
by Anthony Stump
Created: 10 Jun 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Logs {
    
    private JSONObject cmp4(JSONArray dataIn) {
        final String cmp4_Name = "Camera MP4 Size";
        JSONObject cmp4_Glob = new JSONObject();
        JSONObject cmp4_Props = new JSONObject();
        JSONArray cmp4_Labels = new JSONArray();
        JSONArray cmp4_Data = new JSONArray();
        cmp4_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cmp4_Name).put("chartFileName", "LogCamsMp4")
                .put("sName", "Size (MB)").put("sColor", "White")
                .put("xLabel", "Date").put("yLabel", "Size (MB)");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            cmp4_Labels.put(thisObject.getString("Date"));
            cmp4_Data.put(thisObject.getDouble("MP4Size")/1024);
        }
        cmp4_Glob
                .put("labels", cmp4_Labels)
                .put("data", cmp4_Data)
                .put("props", cmp4_Props);
        return cmp4_Glob;
    }
    
    public JSONObject getCmp4(JSONArray dataIn) { return cmp4(dataIn); }
        
}
