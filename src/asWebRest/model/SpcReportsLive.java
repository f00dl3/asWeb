/*
by Anthony Stump
Created: 26 Feb 2018

Tables:
    WxObs.SPCLiveReports
 */

package asWebRest.model;

public class SpcReportsLive {
    
    private String assocId;
    private String comments;
    private String county;
    private String date;
    private String lat;
    private String location;
    private String lon;
    private String magnitude;
    private String state;
    private String time;
    private String type;
    
    public String getAssocId() { return assocId; }
    public String getComments() { return comments; }
    public String getCounty() { return county; }
    public String getDate() { return date; }
    public String getLat() { return lat; }
    public String getLocation() { return location; }
    public String getLon() { return lon; }
    public String getMagnitude() { return magnitude; }
    public String getState() { return state; }
    public String getTime() { return time; }
    public String getType() { return type; }
    
    public void setAssocId(String assocId) { this.assocId = assocId; }
    public void setComments(String comments) { this.comments = comments; }
    public void setCountry(String county) { this.county = county; }
    public void setDate(String date) { this.date = date; }
    public void setLat(String lat) { this.lat = lat; }
    public void setLocation(String location) { this.location = location; }
    public void setLon(String lon) { this.lon = lon; }
    public void setMagnitude(String magnitude) { this.magnitude = magnitude; }
    public void setState(String state) { this.state = state; }
    public void setTime(String time) { this.time = time; }
    public void setType(String type) { this.type = type; }
    
}
