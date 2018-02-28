/*
by Anthony Stump
Created: 20 Feb 2018
 */

package asWebRest.model;

public class SdUtils {
    
    private String date;
    private int eventId;
    private String notes;
    private String time;
    private int zipSize;
    
    public String getDate() { return date; }
    public int getEventId() { return eventId; }
    public String getNotes() { return notes; }
    public String getTime() { return time; }
    public int getZipSize() { return zipSize; }
    
    public void setDate(String date) { this.date = date; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setTime(String time) { this.time = time; }
    public void setZipSize(int zipSize) { this.zipSize = zipSize; }
    
}
