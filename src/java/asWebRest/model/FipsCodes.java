/*
by Anthony Stump
Created: 27 Feb 2018
For table:
    WxObs.FIPSCodes
 */

package asWebRest.model;

public class FipsCodes {
    
    private String description;
    private String fips;
    private String state;
    
    public String getDescription() { return description; }
    public String getFips() { return fips; }
    public String getState() { return state; }
    
    public void setDescription(String description) { this.description = description; }
    public void setFips(String fips) { this.fips = fips; }
    public void setState(String state) { this.state = state; }
    
}
