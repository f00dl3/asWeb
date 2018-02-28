/*
by Anthony Stump
Created: 18 Feb 2018

for tables:
    Core.PlainTextNotes
 */

package asWebRest.model;

public class PlainTextNotes {
 
    private String date;
    private String note;
    private String textFileName;
    private String topic;
    
    public String getDate() { return date; }
    public String getNote() { return note; }
    public String getTextFileName() { return textFileName; }
    public String getTopic() { return topic; }
    
    public void setDate(String date) { this.date = date; }
    public void setNote(String note) { this.note = note; }
    public void setTextFileName(String textFileName) { this.textFileName = textFileName; }
    public void setTopic(String topic) { this.topic = topic; }
    
}
