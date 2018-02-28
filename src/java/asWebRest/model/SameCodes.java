/*
by Anthony Stump
Created: 27 Feb 2018
For table: 
    WxObs.SAMECodes
 */

package asWebRest.model;

public class SameCodes {

    private String county;
    private String same;
    private String state;
    
    public String getCounty() { return county; }
    public String getSame() { return same; }
    public String getState() { return state; }
    
    public void setCounty(String county) { this.county = county; }
    public void setSame(String same) { this.same = same; }
    public void setState(String state) { this.state = state; }
    
}
