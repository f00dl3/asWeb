/*
by Anthony Stump
Created: 21 Feb 2018
 */

package asWebRest.model;

public class Pto {
    
    private double carryOver;
    private String month;
    private double newHrs; //new is reserved for actions in Java!
    private String notes;
    private double save;
    private double taken;
    
    public double getCarryOver() { return carryOver; }
    public String getMonth() { return month; }
    public double getNew() { return newHrs; }
    public String getNotes() { return notes; }
    public double getSave() { return save; }
    public double getTaken() { return taken; }
    
    public void setCarryOver(double carryOver) { this.carryOver = carryOver; }
    public void setMonth(String month) { this.month = month; }
    public void setNew(double newHrs) { this.newHrs = newHrs; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setSave(double save) { this.save = save; }
    public void setTaken(double taken) { this.taken = taken; }    
    
}
