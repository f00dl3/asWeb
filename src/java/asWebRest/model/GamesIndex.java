/*
by Anthony Stump
Created: 18 Feb 2018

Applies to tables:
    Core.GamesIndex
 */

package asWebRest.model;

public class GamesIndex {

    private String burnDate;
    private String fof;
    private String gameName;
    private String lastUpdate;
    private String linuxTested;
    private double sizeG;
    private int pendingBurn;
    private String volume;
    
    public String getBurnDate() { return burnDate; }
    public String getFof() { return fof; }
    public String getGameName() { return gameName; }
    public String getLastUpdate() { return lastUpdate; }
    public String getLinuxTested() { return linuxTested; }
    public double getSizeG() { return sizeG; }
    public int getPendingBurn() { return pendingBurn; }
    public String getVolume() { return volume; }
    
    public void setBurnDate(String burnDate) { this.burnDate = burnDate; }
    public void setFof(String fof) { this.fof = fof; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }
    public void setLinuxTested(String linuxTested) { this.linuxTested = linuxTested; }
    public void setSizeG(double sizeG) { this.sizeG = sizeG; }
    public void setPendigBurn(int pendingBurn) { this.pendingBurn = pendingBurn; }
    public void setVolume(String volume) { this.volume = volume; }
    
}
