/*
by Anthony Stump
Created: 18 Feb 2018

Applies to tables:
    Core.GameHours
 */

package asWebRest.model;

public class GameHours {
    
    private int active;
    private double hours;
    private String last;
    private String name;
    private String notes;
    
    public int getActive() { return active; }
    public double getHours() { return hours; }
    public String getLast() { return last; }
    public String getName() { return name; }
    public String getNotes() { return notes; }
    
    public void setActive(int active) { this.active = active; }
    public void setHours(double hours) { this.hours = hours; }
    public void setLast(String last) { this.last = last; }
    public void setName(String name) { this.name = name; }
    public void setNotes(String notes) { this.notes = notes; }
    
}
