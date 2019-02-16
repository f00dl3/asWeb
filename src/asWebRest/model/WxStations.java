/*
by Anthony Stump
Created: 26 Feb 2018
Updated: 27 Feb 2018

Tables:
    WxObs.Regions
    WxObs.Stations
 */

package asWebRest.model;

public class WxStations {
    
    private int active;
    private String city;
    private String code;
    private String dataSource;
    private String description;
    private int eleM;
    private int frequency;
    private int gfsData;
    private String ghcndId;
    private String indexedOn;
    private String point;
    private int priority;
    private String region;
    private int sfcMb;
    private String state;
    private String station;
    
    public int getActive() { return active; }
    public String getCity() { return city; }
    public String getCode() { return code; }
    public String getDataSource() { return dataSource; }
    public String getDescription() { return description; }
    public int getEleM() { return eleM; }
    public int getFrequency() { return frequency; }
    public int getGfsData() { return gfsData; }
    public String getGhcndId() { return ghcndId; }
    public String getIndexedOn() { return indexedOn; }
    public String getPoint() { return point; }
    public int getPriority() { return priority; }
    public String getRegion() { return region; }
    public int getSfcMb() { return sfcMb; }
    public String getState() { return state; }
    public String getStation() { return station; }
    
    public void setActive(int active) { this.active = active; }
    public void setCity(String city) { this.city = city; }
    public void setCode(String code) { this.code = code; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public void setDescription(String description) { this.description = description; }
    public void setEleM(int eleM) { this.eleM = eleM; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    public void setGfsData(int gfsData) { this.gfsData = gfsData; }
    public void setGhcndId(String ghcndId) { this.ghcndId = ghcndId; }
    public void setIndexedOn(String indexedOn) { this.indexedOn = indexedOn; }
    public void setPoint(String point) { this.point = point; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setRegion(String region) { this.region = region; }
    public void setSfcMb(int sfcMb) { this.sfcMb = sfcMb; }
    public void setState(String state) { this.state = state; }
    public void setStation(String station) { this.station = station; }
    
}
