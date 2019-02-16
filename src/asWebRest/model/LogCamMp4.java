/*
by Anthony Stump
Created: 18 Feb 2018

for tables:
    Core.Log_CamsMP4
 */
package asWebRest.model;

public class LogCamMp4 {

    private String date;
    private int imgCount;
    private int mp4Size;
    private int totSize;
    
    public String getDate() { return date; }
    public int getImgCount() { return imgCount; }
    public int getMp4Size() { return mp4Size; }
    public int getTotSize() { return totSize; }
    
    public void setDate(String date) { this.date = date; }
    public void setImgCount(int imgCount) { this.imgCount = imgCount; }
    public void setMp4Size(int mp4Size) { this.mp4Size = mp4Size; }
    public void setTotSize(int totSize) { this.totSize = totSize; }
    
}
