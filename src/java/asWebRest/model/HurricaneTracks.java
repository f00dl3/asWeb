/*
by Anthony Stump
Created: 26 Feb 2018

Applies to:
    WxObs.HurricaneTracks
 */

package asWebRest.model;

public class HurricaneTracks {
    
    private String addedTime;
    private String ason;
    private String dateEnd;
    private String dateStart;
    private String maxCategory;
    private int maxWindKt;
    private int minPresMb;
    private String stormId;
    private String stormName;
    
    public String getAddedTime() { return addedTime; }
    public String getAson() { return ason; }
    public String getDateEnd() { return dateEnd; }
    public String getDateStart() { return dateStart; }
    public String getMaxCategory() { return maxCategory; }
    public int getMaxWindKt() { return maxWindKt; }
    public int getMinPresMb() { return minPresMb; }
    public String getStormId() { return stormId; }
    public String getStormName() { return stormName; }
    
    public void setAddedTime(String addedTime) { this.addedTime = addedTime; }
    public void setAson(String ason) { this.ason = ason; }
    public void setDateEnd(String dateEnd) { this.dateEnd = dateEnd; }
    public void setDateStart(String dateStart) { this.dateStart = dateStart; }
    public void setMaxCategory(String maxCategory) { this.maxCategory = maxCategory; }
    public void setMaxWindKt(int maxWindKt) { this.maxWindKt = maxWindKt; }
    public void setMinPresMb(int minPresMb) { this.minPresMb = minPresMb; }
    public void setStormId(String stormId) { this.stormId = stormId; }
    public void setStormName(String stormName) { this.stormName = stormName; }
    
}
