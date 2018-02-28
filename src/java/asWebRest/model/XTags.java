/*
by Anthony Stump
Created: 17 Feb 2018

Applies to tables:
    Core.AO_Tags

 */

package asWebRest.model;

public class XTags {
    
    private String description;
    private String detail;
    private String tag;
    private int tagVer;
    
    public String getDescription() { return description; }
    public String getDetail() { return detail; }
    public String getTag() { return tag; }
    public int getTagVer() { return tagVer; }
    
    public void setDescription(String description) { this.description = description; }
    public void setDetail(String detail) { this.detail = detail; }
    public void setTag(String tag) { this.tag = tag; }
    public void setTagVer(int tagVer) { this.tagVer = tagVer; }
    
}
