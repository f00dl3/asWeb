/*
by Anthony Stump
Created: 27 Feb 2018
Tables:
    WxObs.RadarList
 */

package asWebRest.model;

import org.json.JSONArray;

public class RadarList {
    
    private int active;
    private JSONArray boundsNsew;
    private String description;
    private String site;
    private int round;
    
    public int getActive() { return active; }
    public JSONArray getBoundsNsew() { return boundsNsew; }
    public String getDescription() { return description; }
    public String getSite() { return site; }
    public int getRound() { return round; }
    
    public void setActive(int active) { this.active = active; }
    public void setBoundsNsew(JSONArray boundsNsew) { this.boundsNsew = boundsNsew; }
    public void setDescription(String description) { this.description = description; }
    public void setSite(String site) { this.site = site; }
    public void setRound(int round) { this.round = round; }
    
}
