/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables:
    WxObs.CF6MCI

 */

package asWebRest.model;

public class Cf6Mci {

    private int archive; // bit?
    private int average;
    private int auto;
    private int cdd;
    private int clouds;
    private String date;
    private int dfNorm;
    private int hdd;
    private int high;
    private double liquid;
    private int low;
    private int pFlag;
    private int sDepth;
    private double snow;
    private double wAvg;
    private double wMax;
    private String weather;
    
    public int getArchive() { return archive; }
    public int getAverage() { return average; }
    public int getAuto() { return auto; }
    public int getCdd() { return cdd; }
    public int getClouds() { return clouds; }
    public String getDate() { return date; }
    public int getDfNorm() { return dfNorm; }
    public int getHdd() { return hdd; }
    public int getHigh() { return high; }
    public double getLiquid() { return liquid; }
    public int getLow() { return low; }
    public int getPFlag() { return pFlag; }
    public int getSDepth() { return sDepth; }
    public double getSnow() { return snow; }
    public double getWAvg() { return wAvg; }
    public double getWMax() { return wMax; }
    public String getWeather() { return weather; }
    
    public void setArchive(int archive) { this.archive = archive; }
    public void setAverage(int average) { this.average = average; }
    public void setAuto(int auto) { this.auto = auto; }
    public void setCdd(int cdd) { this.cdd = cdd; }
    public void setClouds(int clouds) { this.clouds = clouds; }
    public void setDate(String date) { this.date = date; }
    public void setDfNorm(int dfNorm) { this.dfNorm = dfNorm; }
    public void setHdd(int hdd) { this.hdd = hdd; }
    public void setHigh(int high) { this.high = high; }
    public void setLiquid(double liquid) { this.liquid = liquid; }
    public void setLow(int low) { this.low = low; }
    public void setPFlag(int pFlag) { this.pFlag = pFlag; }
    public void setSDepth(int sDepth) { this.sDepth = sDepth; }
    public void setSnow(double snow) { this.snow = snow; }
    public void setWAvg(double wAvg) { this.wAvg = wAvg; }
    public void setWMax(double wMax) { this.wMax = wMax; }
    public void setWeather(String weather) { this.weather = weather; }
    
}
