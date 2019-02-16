/*
by Anthony Stump
Created: 18 Feb 2018

Applies to multiple tables:
    Core.UseElecD
    Core.UseGas
    Core.UseSprintA
    Core.UseSprintE

 */

package asWebRest.model;

public class UtilityUse {

    private String bill;
    private String date;
    private int estimated;
    private double kwh;
    private double mbData;
    private double mbDown;
    private double mbUpload;
    private double minutes;
    private double minutesFree;
    private int minutesL500;
    private double mms;
    private String month;
    private double texts;
    private double totalMcf;
    
    public String getBill() { return bill; }
    public String getDate() { return date; }
    public int getEstimated() { return estimated; }
    public double getKwh() { return kwh; }
    public double getMbData() { return mbData; }
    public double getMbDown() { return mbDown; }
    public double getMbUpload() { return mbUpload; }
    public double getMinutes() { return minutes; }
    public double getMinutesFree() { return minutesFree; }
    public int getMinutesL500() { return minutesL500; }
    public double getMms() { return mms; }
    public String getMonth() { return month; }
    public double getTexts() { return texts; }
    public double getTotalMcf() { return totalMcf; }
    
    public void setBill(String bill) { this.bill = bill; }
    public void setDate(String date) { this.date = date; }
    public void setEstimated(int estimated) { this.estimated = estimated; }
    public void setKwh(double kwh) { this.kwh = kwh; }
    public void setMbData(double mbData) { this.mbData = mbData; }
    public void setMbDown(double mbDown) { this.mbDown = mbDown; }
    public void setMbUpload(double mbUpload) { this.mbUpload = mbUpload; }
    public void setMintues(double minutes) { this.minutes = minutes; }
    public void setMinutesFree(double minutesFree) { this.minutesFree = minutesFree; }
    public void setMinutesL500(int minutesL500) { this.minutesL500 = minutesL500; }
    public void setMms(double mms) { this.mms = mms; }
    public void setMonth(String month) { this.month = month; }
    public void setTexts(double texts) { this.texts = texts; }
    public void setTotalMcf(double totalMcf) { this.totalMcf = totalMcf; }
    
}
