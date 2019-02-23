/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 25 Jun 2018

for tables:
    Core.AO_TP
    Core.AO_TP_Images
    Core.AO_TP_Queue
 */
package asWebRest.model;

public class Tp {
    
    private String added;
    private String addedOn;
    private String categories;
    private String description;
    private int done;
    private String fetched;
    private String filename;
    private int finalCount;
    private int glob;
    private String hashPath;
    private int imageCount;
    private String imageSet;
    private String media;
    private int offDisc;
    private String tags;
    private String submittedBy;
    private int warReady;
    private String webLink;
    private String xTags;
    private int xTagVer;
    
    public String getAdded() { return added; }
    public String getAddedOn() { return addedOn; }
    public String getCategories() { return categories; }
    public String getDescription() { return description; }
    public int getDone() { return done; }
    public String getFetched() { return fetched; }
    public String getFilename() { return filename; }
    public int getFinalCount() { return finalCount; }
    public String getHashPath() { return hashPath; }
    public int getGlob() { return glob; }
    public int getImageCount() { return imageCount; }
    public String getImageSet() { return imageSet; }
    public String getMedia() { return media; }
    public int getOffDisc() { return offDisc; }
    public String getTags() { return tags; }
    public String getSubmittedBy() { return submittedBy; }
    public int getWarReady() { return warReady; }
    public String getWebLink() { return webLink; }
    public String getXTags() { return xTags; }
    public int getXTagVer() { return xTagVer; }
    
    public void setAdded(String added) { this.added = added; }
    public void setAddedOn(String addedOn) { this.addedOn = addedOn; }
    public void setCategories(String categories) { this.categories = categories; }
    public void setDescription(String description) { this.description = description; }
    public void setDone(int done) { this.done = done; }
    public void setFetched(String fetched) { this.fetched = fetched; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setFinalCount(int finalCount) { this.finalCount = finalCount; }
    public void setGlob(int glob) { this.glob = glob; }
    public void setHashPath(String hashPath) { this.hashPath = hashPath; }
    public void setImageCount(int imageCount) { this.imageCount = imageCount; }
    public void setMedia(String media) { this.media = media; }
    public void setOffDisc(int offDisc) { this.offDisc = offDisc; }
    public void setTags(String tags) { this.tags = tags; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }
    public void setWarReady(int warReady) { this.warReady = warReady; }
    public void setWebLink(String webLink) { this.webLink = webLink; }
    public void setXTags(String xTags) { this.xTags = xTags; }
    public void setXTagVer(int xTagVer) { this.xTagVer = xTagVer; }
    
}
