/*
by Anthony Stump
Created: 15 Nov 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ffxiv {
    
    private JSONObject byDate(JSONArray dataIn) {
        final String this_Name = "FFXIV By Date";
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Data2 = new JSONArray();
        JSONArray this_Data3 = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", this_Name).put("chartFileName", "FfxivByDate")
                .put("sName", "Quests").put("sColor", "Yellow")
                .put("s2Name", "Hunting").put("s2Color", "Green")
                .put("s3Name", "Crafting").put("s3Color", "Blue")
                .put("xLabel", "Date").put("yLabel", "Completed");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("OrigCompDate"));
            this_Data.put(thisObject.getInt("Quests"));
            this_Data2.put(thisObject.getInt("Hunting"));
            this_Data3.put(thisObject.getInt("Crafting"));
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("data3", this_Data3)
                .put("props", this_Props);
        return this_Glob;
    }

    public JSONObject getByDate(JSONArray dataIn) { return byDate(dataIn); }
        
}
