/*
by Anthony Stump
Created: 13 May 2018
Updated: 15 May 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Finance {

    private static JSONObject finEnw(JSONArray dataIn) {
        String enw_Name = "Estimated Net Worth";
        JSONObject enw_Glob = new JSONObject();
        JSONObject enw_Props = new JSONObject();
        JSONArray enw_Labels = new JSONArray();
        JSONArray enw_Data = new JSONArray();
        JSONArray enw_Data2 = new JSONArray();
        JSONArray enw_Data3 = new JSONArray();
        JSONArray enw_Data4 = new JSONArray();
        JSONArray enw_Data5 = new JSONArray();
        JSONArray enw_Data6 = new JSONArray();
        enw_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", enw_Name).put("chartFileName", "FinENW")
                .put("sName", "Worth").put("sColor", "White")
                .put("s2Name", "Liquid").put("s2Color", "Green")
                .put("s3Name", "Fixed").put("s3Color", "Blue")
                .put("s4Name", "Insurance").put("s4Color", "Gray")
                .put("s5Name", "Credits").put("s5Color", "Yellow")
                .put("s6Name", "Debt").put("s6Color", "Red")
                .put("xLabel", "Date").put("yLabel", "K USD");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            enw_Labels.put(thisObject.getString("AsOf"));
            enw_Data.put(thisObject.getDouble("Worth"));
            enw_Data2.put(thisObject.getDouble("AsLiq"));
            enw_Data3.put(thisObject.getDouble("AsFix"));
            enw_Data4.put(thisObject.getDouble("Life"));
            enw_Data5.put(thisObject.getDouble("Credits"));
            enw_Data6.put(thisObject.getDouble("Debts"));
        }
        enw_Glob
                .put("labels", enw_Labels)
                .put("data", enw_Data)
                .put("data2", enw_Data2)
                .put("data3", enw_Data3)
                .put("data4", enw_Data4)
                .put("data5", enw_Data5)
                .put("data6", enw_Data6)
                .put("props", enw_Props);
        return enw_Glob;
    }
    
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
    
    public static JSONObject getFinEnw(JSONArray dataIn) { return finEnw(dataIn); }
    public static JSONObject getSavingsOpt(JSONArray dataIn) { return savingsOpt(dataIn); }
    
}
