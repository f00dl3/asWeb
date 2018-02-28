/*
by Anthony Stump
Created: 15 Feb 2018

Applies to tables:
    Core.WebVersion

 */

package asWebRest.model;

public class WebVersion {
    
    private String changes;
    private String date;
    private double version;
    
    public String getChanges() { return changes; }
    public String getDate() { return date; }
    public double getVersion() { return version; }
    
    public void setChanges(String changes) { this.changes = changes; }
    public void setDate(String date) { this.date = date; }
    public void setVersion(double Version) { this.version = version; }
    
}
