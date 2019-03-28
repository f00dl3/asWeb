/*
by Anthony Stump
Created: 28 Mar 2019
Updated: on Creation
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class MediaServer {
    
    private JSONObject byDate(JSONArray dataIn) {
        final String this_Name = "Media Server By Date";
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", this_Name).put("chartFileName", "msByDate")
                .put("sName", "Files").put("sColor", "Yellow")
                .put("xLabel", "Date").put("yLabel", "Indexed");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            int hits = thisObject.getInt("Hits");
            this_Labels.put(thisObject.getString("Date"));
            this_Data.put(hits);
        }
        /* System.out.println("Days: " + this_Labels.length());
        System.out.println("Quest days: " + this_Data.length());
        System.out.println("Hunting days: " + this_Data2.length());
        System.out.println("Crafting days: " + this_Data3.length()); */
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("props", this_Props);
        return this_Glob;
    }
    
    public JSONObject getByDate(JSONArray dataIn) { return byDate(dataIn); }
        
}
