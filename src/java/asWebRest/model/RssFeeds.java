/*
by Anthony Stump
Created: 18 Feb 2018

for tables:
    Feeds.RedditFeeds
    Feeds.RSSFeeds
 */

package asWebRest.model;

public class RssFeeds {
    
    private String content;
    private String description;
    private String getTime;
    private String guid;
    private String id;
    private String link;
    private String name;
    private String pubDate;
    private String title;
    private String updated;
    private String uri;
    
    public String getContent() { return content; }
    public String getDescription() { return description; }
    public String getGetTime() { return getTime; }
    public String getGuid() { return guid; }
    public String getId() { return id; }
    public String getLink() { return link; }
    public String getName() { return name; }
    public String getPubDate() { return pubDate; }
    public String getTitle() { return title; }
    public String getUpdated() { return updated; }
    public String getUri() { return uri; }
    
    public void setContent(String content) { this.content = content; }
    public void setDescription(String description) { this.description = description; }
    public void setGetTime(String getTime) { this.getTime = getTime; }
    public void setGuid(String guid) { this.guid = guid; }
    public void setId(String id) { this.id = id; }
    public void setLink(String link) { this.link = link; }
    public void setName(String name) { this.name = name; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }
    public void setTitle(String title) { this.title = title; }
    public void setUpdated(String updated) { this.updated = updated; }
    public void setUri(String uri) { this.uri = uri; }
    
}
