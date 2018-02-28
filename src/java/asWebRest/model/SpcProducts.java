/*
by Anthony Stump
Created: 27 Feb 2018

For tables:
    WxObs.NHCFeeds
    WxObs.SPCWatchBoxes
    WxObs.SPCWatches
    WxObs.SPCMesoscale
    WxObs.SPCMesoscaleShape
    WxObs.SPCOutlooks
 */

package asWebRest.model;

import org.json.JSONArray;

public class SpcProducts {
    
    private JSONArray bounds;
    private String description;
    private String getTime;
    private String getTimestamp;
    private String guid;
    private String link;
    private String mdId;
    private String pubDate;
    private String timestamp;
    private String title;
    private JSONArray watchBox;
    private String watchId;
    
    public JSONArray getBounds() { return bounds; }
    public String getDescription() { return description; }
    public String getGetTime() { return getTime; }
    public String getGetTimestamp() { return getTimestamp; }
    public String getGuid() { return guid; }
    public String getLink() { return link; }
    public String getMdId() { return mdId; }
    public String getPubDate() { return pubDate; }
    public String getTimestamp() { return timestamp; }
    public String getTitle() { return title; }
    public JSONArray getWatchBox() { return watchBox; }
    public String getWatchId() { return watchId; }    
    
    public void setBounds(JSONArray bounds) { this.bounds = bounds; }
    public void setDescription(String description) { this.description = description; }
    public void setGetTime(String getTime) { this.getTime = getTime; }
    public void setGetTimestamp(String getTimestamp) { this.getTimestamp = getTimestamp; }
    public void setGuid(String guid) { this.guid = guid; }
    public void setLink(String link) { this.link = link; }
    public void setMdId(String mdId) { this.mdId = mdId; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setTitle(String title) { this.title = title; }
    public void setWatchBox(JSONArray watchBox) { this.watchBox = watchBox; }
    public void setWatchId(String watchId) { this.watchId = watchId; }
    
}
