/*
by Anthony Stump
Created: 13 May 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Finance {

    private static JSONObject savingsOpt(JSONArray dataIn) {
        String svChart_Name = "Savings Balance";
        JSONObject svChart_Glob = new JSONObject();
        JSONObject svChart_Props = new JSONObject();
        JSONArray svChart_Labels = new JSONArray();
        JSONArray svChart_Data = new JSONArray();
        svChart_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", svChart_Name).put("chartFileName", "FinSavings")
                .put("sName", "Savings").put("sColor", "Red")
                .put("xLabel", "Date").put("yLabel", "USD");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            svChart_Labels.put(thisObject.getString("Date"));
            svChart_Data.put(thisObject.getDouble("Value"));
        }
        svChart_Glob
                .put("labels", svChart_Labels)
                .put("data", svChart_Data)
                .put("props", svChart_Props);
        return svChart_Glob;
    }
    
    public static JSONObject getSavingsOpt(JSONArray dataIn) { return savingsOpt(dataIn); }
    
}
