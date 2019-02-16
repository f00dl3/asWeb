/*
by Anthony Stump
Created: 27 Feb 2018
For tables:
    WxObs.StationDataIndexed
    WxObs.RapidSDI
 */

package asWebRest.model;

import org.json.JSONObject;

public class StationDataIndexed {
    
    private JSONObject jsonData;
    private String getTime;
    private long obsId;
    private String timeOverride;
    
    public JSONObject getJsonData() { return jsonData; }
    public String getGetTime() { return getTime; }
    public long getObsId() { return obsId; }
    public String getTimeOverride() { return timeOverride; }
    
    public void setJsonData(JSONObject jsonData) { this.jsonData = jsonData; }
    public void setGetTime(String getTime) { this.getTime = getTime; }
    public void setObsId(long obsId) { this.obsId = obsId; }
    public void setTimeOverride(String timeOverride) { this.timeOverride = timeOverride; }
    
}
