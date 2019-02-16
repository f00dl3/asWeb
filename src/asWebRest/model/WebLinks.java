/*
by Anthony Stump
Created: 17 Feb 2018
Updated: 25 Mar 2018
 */

package asWebRest.model;

public class WebLinks {
    
    private int active;
    private String asOf;
    private String bubble;
    private String description;
    private String descriptionL;
    private String desktopLink;
    private String geoPlot;
    private int legacyTc;
    private String master;
    private String tcSystem;
    private int tomcatProd;
    private String tomcatUrl;
    private String url;
    
    public int getActive() { return active; }
    public String getAsOf() { return asOf; }
    public String getBubble() { return bubble; }
    public String getDescription() { return description; }
    public String getDescriptionL() { return descriptionL; }
    public String getDesktopLink() { return desktopLink; }
    public String getGeoPlot() { return geoPlot; }
    public int getLegacyTc() { return legacyTc; }
    public String getMaster() { return master; }
    public String getTcSystem() { return tcSystem; }
    public int getTomcatProd() { return tomcatProd; }
    public String getTomcatUrl() { return tomcatUrl; }
    public String getUrl() { return url; }
    
    public void setActive(int active) { this.active = active; }
    public void setAsOf(String asOf) { this.asOf = asOf; }
    public void setBubble(String bubble) { this.bubble = bubble; }
    public void setDescription(String description) { this.description = description; }
    public void setDescriptionL(String descriptionL) { this.descriptionL = descriptionL; }
    public void setDesktopLink(String desktopLink) { this.desktopLink = desktopLink; }
    public void setGeoPlot(String geoPlot) { this.geoPlot = geoPlot; }
    public void setLegacyTc(int legacyTc) { this.legacyTc = legacyTc; }
    public void setMaster(String master) { this.master = master; }
    public void setTcSystem(String tcSystem) { this.tcSystem = tcSystem; }
    public void setTomcatProd(int tomcatProd) { this.tomcatProd = tomcatProd; }
    public void setTomcatUrl(String tomcatUrl) { this.tomcatUrl = tomcatUrl; }
    public void setUrl(String url) { this.url = url; }
    
}
