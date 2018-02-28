/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables:
    Core.Auto_MPG

 */

package asWebRest.model;

public class AutoMpg {
    
    private double costPG;
    private String date;
    private double gallons;
    private int estimated;
    private int seaFoam;
    private int totMiles;
    
    public double getCostPG() { return costPG; }
    public String getDate() { return date; }
    public double getGallons() { return gallons; }
    public int getEstimated() { return estimated; }
    public int getSeaFoam() { return seaFoam; }
    public int getTotMiles() { return totMiles; }
    
    public void setCostPG(double costPG) { this.costPG = costPG; }
    public void setDate(String date) { this.date = date; }
    public void setGallons(double gallons) { this.gallons = gallons; }
    public void setEstimated(int estimated) { this.estimated = estimated; }
    public void setSeaFoam(int seaFoam) { this.seaFoam = seaFoam; }
    public void setTotMiles(int totMiles) { this.totMiles = totMiles; }
    
}
