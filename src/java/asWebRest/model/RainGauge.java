/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables:
    WxObs.RainGauge

 */

package asWebRest.model;

public class RainGauge {
    
    private String date;
    private int gaugeNo;
    private double precip;
    
    public String getDate() { return date; }
    public int getGaugeNo() { return gaugeNo; }
    public double getPrecip() { return precip; }
    
    public void setDate(String date) { this.date = date; }
    public void setGaugeNo(int gaugeNo) { this.gaugeNo = gaugeNo; }
    public void setPrecip(double precip) { this.precip = precip; }
    
}
