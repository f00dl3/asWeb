/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 15 Mar 2018

Applies to tables:
    Core.Fit_RPlans
 */

package asWebRest.model;

import org.json.JSONArray;

public class FitRPlans {
    
    private String description;
    private double distKm;
    private int done;
    private JSONArray geoJson;
    
    public String getDescription() { return description; }
    public double getDistKm() { return distKm; }
    public int getDone() { return done; }
    public JSONArray getGeoJson() { return geoJson; }
    
    public void setDescription(String description) { this.description = description; }
    public void setDistKm(double distKm) { this.distKm = distKm; }
    public void setDone(int done) { this.done = done; }
    public void setGeoJson(JSONArray geoJson) { this.geoJson = geoJson; }
    
}
