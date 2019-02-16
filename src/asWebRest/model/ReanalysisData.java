/*
by Anthony Stump
Created: 27 Feb 2018
For tables:
    WxObs.ReanalysisData
 */

package asWebRest.model;

import org.json.JSONObject;

public class ReanalysisData {
    
    private String dateTime;
    private JSONObject jsonData;
    private JSONObject jsonExtra;
    private JSONObject jsonExtra2;
    
    public String getDateTime() { return dateTime; }
    public JSONObject getJsonData() { return jsonData; }
    public JSONObject getJsonExtra() { return jsonExtra; }
    public JSONObject getJsonExtra2() { return jsonExtra2; }
    
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setJsonData(JSONObject jsonData) { this.jsonData = jsonData; }
    public void setJsonExtra(JSONObject jsonExtra) { this.jsonExtra = jsonExtra; }
    public void setJsonExtra2(JSONObject jsonExtra2) { this.jsonExtra2 = jsonExtra2; }
    
}
