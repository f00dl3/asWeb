/*
by Anthony Stump
Created: 7 Jun 2018
Updated: 16 Mar 2019
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Fitness {
    
    private static JSONObject calCh(JSONArray dataIn) {
        String cal_Name = "Calories";
        JSONObject cal_Glob = new JSONObject();
        JSONObject cal_Props = new JSONObject();
        JSONArray cal_Labels = new JSONArray();
        JSONArray cal_Data = new JSONArray();
        JSONArray cal_Data2 = new JSONArray();
        JSONArray cal_Data3 = new JSONArray();
        JSONArray cal_Data4 = new JSONArray();
	JSONArray cal_Data5 = new JSONArray();
        cal_Props
            .put("dateFormat", "yyyy-MM-dd")
            .put("chartName", cal_Name).put("chartFileName", "CalorieRange")
            .put("sName", "Calories").put("sColor", "White")
            .put("s2Color", "Red").put("s2Name", "Fat")
            .put("s3Color", "Green").put("s3Name", "Protein")
            .put("s4Color", "Yellow").put("s4Name", "Carbs")
		.put("s5Color", "Blue").put("s5Name", "Burned")
            .put("xLabel", "Date").put("yLabel", "Calories");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            cal_Labels.put(thisObject.getString("Date"));
            cal_Data.put(thisObject.getInt("Calories"));
            cal_Data2.put(9 * thisObject.getInt("Fat"));
            cal_Data3.put(4 * thisObject.getInt("Protein"));
            cal_Data4.put(4 * thisObject.getInt("Carbs"));
		cal_Data5.put(thisObject.getInt("CaloriesBurned"));
        }
        cal_Glob
                .put("labels", cal_Labels)
                .put("data", cal_Data)
                .put("data2", cal_Data2)
                .put("data3", cal_Data3)
                .put("data4", cal_Data4)
		.put("data5", cal_Data5)
                .put("props", cal_Props);
        return cal_Glob;
    }
    
    private static JSONObject sleepCh(JSONArray dataIn) {
        String sleep_Name = "Hours of Sleep/Gaming";
        JSONObject sleep_Glob = new JSONObject();
        JSONObject sleep_Props = new JSONObject();
        JSONArray sleep_Labels = new JSONArray();
        JSONArray sleep_Data = new JSONArray();
        JSONArray sleep_Data2 = new JSONArray();
        JSONArray sleep_Data3 = new JSONArray();
        sleep_Props
            .put("dateFormat", "yyyy-MM-dd")
            .put("chartName", sleep_Name).put("chartFileName", "SleepRange")
            .put("sName", "Sleep").put("sColor", "Yellow")
            .put("s2Name", "Gaming").put("s2Color", "Blue")
            .put("s3Name", "Exercise").put("s3Color", "Red")
            .put("xLabel", "Date").put("yLabel", "Sleep Hours");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            sleep_Labels.put(thisObject.getString("Date"));
            sleep_Data.put(thisObject.getDouble("EstHoursSleep"));
            sleep_Data2.put(thisObject.getDouble("HoursGaming"));
            sleep_Data3.put(thisObject.getDouble("HoursExercise"));
        }
        sleep_Glob
                .put("labels", sleep_Labels)
                .put("data", sleep_Data)
                .put("data2", sleep_Data2)
                .put("data3", sleep_Data3)
                .put("props", sleep_Props);
        return sleep_Glob;
    }
    
    private static JSONObject weightCh(JSONArray dataIn) {
        String wgt_Name = "Weight";
        JSONObject wgt_Glob = new JSONObject();
        JSONObject wgt_Props = new JSONObject();
        JSONArray wgt_Labels = new JSONArray();
        JSONArray wgt_Data = new JSONArray();
        wgt_Props
            .put("dateFormat", "yyyy-MM-dd")
            .put("chartName", wgt_Name).put("chartFileName", "WeightRange")
            .put("sName", "Weight").put("sColor", "Yellow")
            .put("xLabel", "Date").put("yLabel", "lbs");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            wgt_Labels.put(thisObject.getString("Date"));
            wgt_Data.put(thisObject.getDouble("Weight"));
        }
        wgt_Glob
                .put("labels", wgt_Labels)
                .put("data", wgt_Data)
                .put("props", wgt_Props);
        return wgt_Glob;
    }

    public static JSONObject getCalCh(JSONArray dataIn) { return calCh(dataIn); }
    public static JSONObject getSleepCh(JSONArray dataIn) { return sleepCh(dataIn); }
    public static JSONObject getWeightCh(JSONArray dataIn) { return weightCh(dataIn); }
    
}
