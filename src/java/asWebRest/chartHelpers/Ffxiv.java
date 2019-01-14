/*
by Anthony Stump
Created: 15 Nov 2018
Updated: 13 Jan 2019
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
        JSONArray this_Data4 = new JSONArray();
        JSONArray this_Data5 = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", this_Name).put("chartFileName", "ffxivQuestsByDay")
                .put("sName", "Quests").put("sColor", "Yellow")
                .put("s2Name", "Hunting").put("s2Color", "Green")
                .put("s3Name", "Crafting").put("s3Color", "Blue")
                .put("s4Name", "Dungeons").put("s4Color", "Red")
                .put("s5Name", "Achievements").put("s4Color", "White")
                .put("xLabel", "Date").put("yLabel", "Completed");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("OrigCompDate"));
            this_Data.put(thisObject.getInt("Quests"));
            this_Data2.put(thisObject.getInt("Hunting"));
            this_Data3.put(thisObject.getInt("Crafting"));
            this_Data4.put(thisObject.getInt("Dungeons"));
            this_Data5.put(thisObject.getInt("Achievements"));
        }
        /* System.out.println("Days: " + this_Labels.length());
        System.out.println("Quest days: " + this_Data.length());
        System.out.println("Hunting days: " + this_Data2.length());
        System.out.println("Crafting days: " + this_Data3.length()); */
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("data3", this_Data3)
                .put("data4", this_Data4)
                .put("data5", this_Data5)
                .put("props", this_Props);
        return this_Glob;
    }

    public JSONObject getByDate(JSONArray dataIn) { return byDate(dataIn); }
        
}
