/*
by Anthony Stump
Created: 10 Jun 2018
Updated: 9 Oct 2019
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
            try {
            	cmp4_Labels.put(thisObject.getString("Date"));
                cmp4_Data.put(thisObject.getDouble("MP4Size")/1024);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        cmp4_Glob
                .put("labels", cmp4_Labels)
                .put("data", cmp4_Data)
                .put("props", cmp4_Props);
        return cmp4_Glob;
    }

    private JSONObject redditStatsKCRW(JSONArray dataIn) {
        final String tName = "Reddit Stats for KCRegionalWx";
        JSONObject tGlob = new JSONObject();
        JSONObject tProps = new JSONObject();
        JSONArray tLabels = new JSONArray();
        JSONArray tData = new JSONArray();
        JSONArray tData2 = new JSONArray();
        JSONArray tData3 = new JSONArray();
        tProps
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", tName).put("chartFileName", "RedditStats_kcregionalwx")
                .put("sName", "Views").put("sColor", "White")
                .put("s2Name", "Unique").put("s2Color", "Green")
                .put("s3Name", "Subs").put("s3Color", "Red")
                .put("xLabel", "Date").put("yLabel", "Count");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            try {
            	tLabels.put(thisObject.getString("Date"));
                tData.put(thisObject.getInt("PageViews"));
                tData2.put(thisObject.getInt("UniqueViews"));
                tData3.put(thisObject.getInt("Subscriptions"));
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        tGlob
                .put("labels", tLabels)
                .put("data", tData)
                .put("data2", tData2)
                .put("data3", tData3)
                .put("props", tProps);
        return tGlob;
    }
    
    private JSONObject wxXml(JSONArray dataIn) {
        final String tName = "Weather XML/JSON Parsing";
        JSONObject tGlob = new JSONObject();
        JSONObject tProps = new JSONObject();
        JSONArray tLabels = new JSONArray();
        JSONArray tData = new JSONArray();
        tProps
                .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .put("chartName", tName).put("chartFileName", "WxXml")
                .put("sName", "Elapsed").put("sColor", "White")
                .put("xLabel", "Timestamp").put("yLabel", "Seconds");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            tLabels.put(thisObject.getString("EndRunTime"));
            tData.put(thisObject.getDouble("Duration")/1024);
        }
        tGlob
                .put("labels", tLabels)
                .put("data", tData)
                .put("props", tProps);
        return tGlob;
    }
    
    public JSONObject getCmp4(JSONArray dataIn) { return cmp4(dataIn); }
    public JSONObject getRedditStatsKCRW(JSONArray dataIn) { return redditStatsKCRW(dataIn); }
    public JSONObject getWxXml(JSONArray dataIn) { return wxXml(dataIn); }
        
}
