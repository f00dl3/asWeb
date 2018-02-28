/*
by Anthony Stump
Created: 18 Feb 2018

Applies to tables:
    Core.Fit_Bike

 */
package asWebRest.model;

public class FitBike {
    
    private String code;
    private String description;
    private String purchased;
    private double purchPrice;
    private String who;
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public String getPurchased() { return purchased; }
    public double getPurchPrice() { return purchPrice; }
    public String getWho() { return who; }
    
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setPurchased(String purchased) { this.purchased = purchased; }
    public void setPurchPrice(double purchPrice) { this.purchPrice = purchPrice; }
    public void setWho(String who) { this.who = who; }
    
}
