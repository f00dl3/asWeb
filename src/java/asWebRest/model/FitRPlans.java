/*
by Anthony Stump
Created: 18 Feb 2018

Applies to tables:
    Core.Fit_RPlans
 */

package asWebRest.model;

import org.json.JSONArray;

public class FitRPlans {
    
    private String description;
    private int done;
    private JSONArray geoJson;
    
    public String getDescription() { return description; }
    public int getDone() { return done; }
    public JSONArray getGeoJson() { return geoJson; }
    
    public void setDescription(String description) { this.description = description; }
    public void setDone(int done) { this.done = done; }
    public void setGeoJson(JSONArray geoJson) { this.geoJson = geoJson; }
    
}
