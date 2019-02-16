/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables:
    WxObs.CPC_AO
    WxObs.CPC_AAO
    WxObs.CPC_NAO
    WxObs.CPC_PNA

 */

package asWebRest.model;

public class CpcData {
    
    private double anom;
    private String obsDate;
    
    public double getAnom() { return anom; }
    public String getObsDate() { return obsDate; }
    
    public void setAnom(double anom) { this.anom = anom; }
    public void setObsDate(String obsDate) { this.obsDate = obsDate; }
    
}
