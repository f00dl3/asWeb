/*
by Anthony Stump
Created: 18 Feb 2018

Applies to tables:
    Core.Bills

 */

package asWebRest.model;

public class Bills {
    
    private double ele;
    private double gas;
    private String month;
    private double pho;
    private double swr;
    private double wat;
    private double web;
    
    public double getEle() { return ele; }
    public double getGas() { return gas; }
    public String getMonth() { return month; }
    public double getPho() { return pho; }
    public double getSwr() { return swr; }
    public double getWat() { return wat; }
    public double getWeb() { return web; }
    
    public void setEle(double ele) { this.ele = ele; }
    public void setGas(double gas) { this.gas = gas; }
    public void setMonth(String month) { this.month = month; }
    public void setPho(double pho) { this.pho = pho; }
    public void setSwr(double swr) { this.swr = swr; }
    public void setWat(double wat) { this.wat = wat; }
    public void setWeb(double web) { this.web = web; }
    
}
