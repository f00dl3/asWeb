/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 23 May 2018

Applies to tables:
    Core.FFXIV_Quests
 */

package asWebRest.model;

public class FfxivQuests {

    private String classes;
    private int completed;
    private int coordX;
    private int coordY;
    private int exp;
    private int gil;
    private String givingNpc;
    private int minLevel;
    private String name;
    private String origCompDate;
    private String questOrder;
    private int seals;
    private double version;
    private String zone;
    
    public String getClasses() { return classes; }
    public int getCompleted() { return completed; }
    public int getCoordX() { return coordX; }
    public int getCoordY() { return coordY; }
    public int getExp() { return exp; }
    public int getGil() { return gil; }
    public String getGivingNpc() { return givingNpc; }
    public int getMinLevel() { return minLevel; }
    public String getName() { return name; }
    public String getOrigCompDate() { return origCompDate; }
    public String getQuestOrder() { return questOrder; }
    public int getSeals() { return seals; }
    public double getVersion() { return version; }
    public String getZone() { return zone; }
    
    public void setClasses(String classes ) { this.classes = classes; }
    public void setCompleted(int completed) { this.completed = completed; }
    public void setCoordX(int coordX) { this.coordX = coordX; }
    public void setCoordY(int coordY) { this.coordY = coordY; }
    public void setExp(int exp) { this.exp = exp; }
    public void setGil(int gil) { this.gil = gil; }
    public void setGivingNpc(String givingNpc) { this.givingNpc = givingNpc; }
    public void setMinLevel(int minLevel) { this.minLevel = minLevel; }
    public void setName(String name) { this.name = name; }
    public void setOrigCompDate(String origCompDate) { this.origCompDate = origCompDate; }
    public void setQuestOrder(String questOrder) { this.questOrder = questOrder; }
    public void setSeals(int seals) { this.seals = seals; }
    public void setVersion(double version) { this.version = version; }
    public void setZone(String zone) { this.zone = zone; }
    
}
