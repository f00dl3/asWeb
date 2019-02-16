/*
by Anthony Stump
Created: 26 Feb 2018

Tables:
    WxObs.LiveWarnings
 */

package asWebRest.model;

import org.json.JSONArray;

public class LiveWarnings {
    
    private JSONArray cap12polygon;
    private JSONArray cap12same;
    private JSONArray cap12ugc;
    private JSONArray cap12vtec;
    private String capareaDesc;
    private String capcategory;
    private String capcertainty;
    private String capeffective;
    private String capevent;
    private String capexpires;
    private String capgeocode;
    private String capmsgType;
    private String capparameter;
    private String cappolygon;
    private String capseverity;
    private String capstatus;
    private String capurgency;
    private double capVersion;
    private String getTime;
    private String id;
    private String published;
    private String summary;
    private String title;
    private String updated;
    
    public JSONArray getCap12polygon() { return cap12polygon; }
    public JSONArray getCap12same() { return cap12same; }
    public JSONArray getCap12ugc() { return cap12ugc; }
    public JSONArray getCap12vtec() { return cap12vtec; }
    public String getCapareaDesc() { return capareaDesc; }
    public String getCapcategory() { return capcategory; }
    public String getCapcertainty() { return capcertainty; }
    public String getCapeffective() { return capeffective; }
    public String getCapevent() { return capevent; }
    public String getCapexpires() { return capexpires; }
    public String getCapgeocode() { return capgeocode; }
    public String getCapmsgType() { return capmsgType; }
    public String getCapparameter() { return capparameter; }
    public String getCappolygon() { return cappolygon; }
    public String getCapseverity() { return capseverity; }
    public String getCapstatus() { return capstatus; }
    public String getCapurgency() { return capurgency; }
    public double getCapVersion() { return capVersion; }
    public String getGetTime() { return getTime; }
    public String getId() { return id; }
    public String getPublished() { return published; }
    public String getSummary() { return summary; }
    public String getTitle() { return title; }
    public String getUpdated() { return updated; }
    
    public void setCap12polygon(JSONArray cap12polygon) { this.cap12polygon = cap12polygon; }
    public void setCap12same(JSONArray cap12same) { this.cap12same = cap12same; }
    public void setCap12ugc(JSONArray cap12ugc) { this.cap12ugc = cap12ugc; }
    public void setCap12vtec(JSONArray cap12vtec) { this.cap12vtec = cap12vtec; }
    public void setCapareaDesc(String capareaDesc) { this.capareaDesc = capareaDesc; }
    public void setCapcategory(String capcategory) { this.capcategory = capcategory; }
    public void setCapcertainty(String capcertainty) { this.capcertainty = capcertainty; }
    public void setCapeffective(String capeffective) { this.capeffective = capeffective; }
    public void setCapevent(String capevent) { this.capevent = capevent; }
    public void setCapexpires(String capexpires) { this.capexpires = capexpires; }
    public void setCapgeocode(String capgeocode) { this.capgeocode = capgeocode; }
    public void setCapmsgType(String capmsgType) { this.capmsgType = capmsgType; }
    public void setCapparameter(String capparameter) { this.capparameter = capparameter; }
    public void setCappolygon(String cappolygon) { this.cappolygon = cappolygon; }
    public void setCapseverity(String capseverity) { this.capseverity = capseverity; }
    public void setCapstatus(String capstatus) { this.capstatus = capstatus; }
    public void setCapuregency(String capurgency) { this.capurgency = capurgency; }
    public void setCapVersion(double capVersion) { this.capVersion = capVersion; }
    public void setGetTime(String getTime) { this.getTime = getTime; }
    public void setId(String id) { this.id = id; }
    public void setPublished(String published) { this.published = published; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setTitle(String title) { this.title = title; }
    public void setUpdated(String updated) { this.updated = updated; }
    
}
