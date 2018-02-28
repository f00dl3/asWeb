/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables;
    Fit.HM_Measure

 */

package asWebRest.model;

public class HomeMeasure {
    
    private String description;
    private int height;
    private String level;
    private int offFloor;
    private String type;
    private int width;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    
    public String getDescription() { return description; }
    public int getHeight() { return height; }
    public String getLevel() { return level; }
    public int getOffFloor() { return offFloor; }
    public String getType() { return type; }
    public int getWidth() { return width; }
    public int getX1() { return x1; }
    public int getX2() { return x2; }
    public int getY1() { return y1; }
    public int getY2() { return y2; }
    
    public void setDescription(String description) { this.description = description; }
    public void setHeight(int height) { this.height = height; }
    public void setLevel(String level) { this.level = level; }
    public void setOffFloor(int offFloor) { this.offFloor = offFloor; }
    public void setType(String type) { this.type = type; }
    public void setWidth(int width) { this.width = width; }
    public void setX1(int x1) { this.x1 = x1; }
    public void setX2(int x2) { this.x2 = x2; }
    public void setY1(int y1) { this.y1 = y1; }
    public void setY2(int y2) { this.y2 = y2; }
    
}
