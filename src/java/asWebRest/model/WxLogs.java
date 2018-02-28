/*
by Anthony Stump
Created: 27 Feb 2018
For table:
    WxObs.Logs
 */

package asWebRest.model;

public class WxLogs {
    
    private long duration;
    private String endRunTime;
    
    public long getDuration() { return duration; }
    public String getEndRunTime() { return endRunTime; }
    
    public void setDuration(long duration) { this.duration = duration; }
    public void setEndRunTime(String endRunTime) { this.endRunTime = endRunTime; }
    
}
