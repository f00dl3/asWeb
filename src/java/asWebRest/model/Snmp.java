/*
by Anthony Stump
Created: 23 Feb 2018
Updated: 26 Feb 2018

Applies to:
    net_snmp.Main
    net_snmp.Note3

CONCEPT
 */

package asWebRest.model;

import org.json.JSONObject;

public class Snmp {
    
    private int activeConn;
    private int cpuLoad;
    private long diskMemUse;
    private JSONObject expandedJsonData;
    private double loadIndex;
    private int numUsers;
    private long netInterface;
    private String walkTime;
    
    public int getActiveConn() { return activeConn; }
    public int getCpuLoad() { return cpuLoad; }
    public long getDiskMemUse() { return diskMemUse; }
    public JSONObject getExpandedJsonData() { return expandedJsonData; }
    public double getLoadIndex() { return loadIndex; }
    public long getNetInterface() { return netInterface; }
    public int getNumUsers() { return numUsers; }
    public String getWalkTime() { return walkTime; }
    
    public void setActiveConn(int activeConn) { this.activeConn = activeConn; }
    public void setCpuLoad(int cpuLoad) { this.cpuLoad = cpuLoad; }
    public void setDiskMemUse(long diskMemUse) { this.diskMemUse = diskMemUse; }
    public void setExpandedJsonData(JSONObject expandedJsonData) { this.expandedJsonData = expandedJsonData; }
    public void setLoadIndex(double loadIndex) { this.loadIndex = loadIndex; }
    public void setRmnet0Rx(long netInterface) { this.netInterface = netInterface; }
    public void setNumUsers(int numUsers) { this.numUsers = numUsers; }
    public void setWalkTime(String walkTime) { this.walkTime = walkTime; }
    
}
