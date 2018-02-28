/*
by Anthony Stump
Created: 18 Feb 2018

For tables:
    Core.MapOverlays
 */

package asWebRest.model;

public class MapOverlays {
    
    private String boundsE;
    private String boundsN;
    private String boundsS;
    private String boundsW;
    private String description;
    private String path;
    
    public String getBoundsE() { return boundsE; }
    public String getBoundsN() { return boundsN; }
    public String getBoundsS() { return boundsS; }
    public String getBoundsW() { return boundsW; }
    public String getDescription() { return description; }
    public String getPath() { return path; }
    
    public void setBoundsE(String boundsE) { this.boundsE = boundsE; }
    public void setBoundsN(String boundsN) { this.boundsN = boundsN; }
    public void setBoundsS(String boundsS) { this.boundsS = boundsS; }
    public void setBoundsW(String boundsW) { this.boundsW = boundsW; }
    public void setDescription(String description) { this.description = description; }
    public void setPath(String path) { this.path = path; }
    
}
