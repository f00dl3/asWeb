/*
by Anthony Stump
Created: 15 Nov 2018
Updated: 2 Feb 2019
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
        JSONArray this_Data6 = new JSONArray();
        JSONArray this_Data7 = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", this_Name).put("chartFileName", "ffxivQuestsByDay")
                .put("sName", "Quests").put("sColor", "Yellow")
                .put("s2Name", "Hunting").put("s2Color", "Green")
                .put("s3Name", "Crafting").put("s3Color", "Blue")
                .put("s4Name", "Dungeons").put("s4Color", "Red")
                .put("s5Name", "Achievements").put("s5Color", "White")
                .put("s6Name", "Gathering").put("s6Color", "Magenta")
                .put("s7Name", "TOTAL").put("s7Color", "Gray")
                .put("xLabel", "Date").put("yLabel", "Completed");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            int quests = thisObject.getInt("Quests");
            int hunting = thisObject.getInt("Hunting");
            int crafting = thisObject.getInt("Crafting");
            int achievements = thisObject.getInt("Achievements");
            int gathering = thisObject.getInt("Gathering");
            int dungeons = thisObject.getInt("Dungeons");
            int total = (quests + hunting + crafting + achievements + gathering + dungeons);
            this_Labels.put(thisObject.getString("OrigCompDate"));
            this_Data.put(quests);
            this_Data2.put(hunting);
            this_Data3.put(crafting);
            this_Data4.put(dungeons);
            this_Data5.put(achievements);
            this_Data6.put(gathering);
            this_Data7.put(total);
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
                .put("data6", this_Data6)
                .put("data7", this_Data7)
                .put("props", this_Props);
        return this_Glob;
    }
    
    private JSONObject gilByDate(JSONArray dataIn) {
        final String this_Name = "FFXIV Gil By Date";
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Data2 = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", this_Name).put("chartFileName", "ffxivGilByDay")
                .put("sName", "Worth").put("sColor", "Yellow")
                .put("s2Name", "Gil").put("s2Color", "Green")
                .put("xLabel", "Date").put("yLabel", "Completed");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            long gil = thisObject.getLong("Gil");
            long worth = thisObject.getLong("Worth");
            this_Labels.put(thisObject.getString("Date"));
            this_Data.put(gil);
            this_Data2.put(worth);
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("props", this_Props);
        return this_Glob;
    }

    public JSONObject getByDate(JSONArray dataIn) { return byDate(dataIn); }
    public JSONObject getGilByDate(JSONArray dataIn) { return gilByDate(dataIn); }
        
}
