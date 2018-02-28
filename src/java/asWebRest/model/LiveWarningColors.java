/*
by Anthony Stump
Created: 26 Feb 2018
For table:
    WxObs.LiveWarningColors
 */

package asWebRest.model;

public class LiveWarningColors {
    
    private String alertScope;
    private String colorHex;
    private String colorName;
    private String colorRgb;
    private int extendedDisplayTime;
    private int showIt;
    private String warnType;
    
    public String getAlertScope() { return alertScope; }
    public String getColorHex() { return colorHex; }
    public String getColorName() { return colorName; }
    public String getColorRgb() { return colorRgb; }
    public int getExtendedDisplayTime() { return extendedDisplayTime; }
    public int getShowIt() { return showIt; }
    public String getWarnType() { return warnType; }
    
    public void setAlertScope(String alertScope) { this.alertScope = alertScope; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public void setColorName(String colorName) { this.colorName = colorName; }
    public void setColorRgb(String colorRgb) { this.colorRgb = colorRgb; }
    public void setExtendedDisplayTime(int extendedDisplayTime) { this.extendedDisplayTime = extendedDisplayTime; }
    public void setShowIt(int showIt) { this.showIt = showIt; }
    public void setWarnType(String warnType) { this.warnType = warnType; }
    
}
