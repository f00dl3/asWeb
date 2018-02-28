/*
by Anthony Stump
Created: 26 Feb 2018
 */

package asWebRest.model;

public class AnssQuakes {
    
    private double depthError;
    private String dmin;
    private double gap;
    private double horizontalError;
    private String id;
    private String latitude;
    private String locationSource;
    private String longitude;
    private double mag;
    private double magError;
    private int magNst;
    private String magSource;
    private String magType;
    private String net;
    private int nst;
    private String place;
    private String rms;
    private String status;
    private String time;
    private String type;
    private String updated;
    
    public double getDepthError() { return depthError; }
    public String getDmin() { return dmin; }
    public double getGap() { return gap; }
    public double getHorizontalError() { return horizontalError; }
    public String getId() { return id; }
    public String getLatitude() { return latitude; }
    public String getLocationSource() { return locationSource; }
    public String getLongitude() { return longitude; }
    public double getMag() { return mag; }
    public double getMagError() { return magError; }
    public int getMagNst() { return magNst; }
    public String getMagSource() { return magSource; }
    public String getMagType() { return magType; }
    public String getNet() { return net; }
    public int getNst() { return nst; }
    public String getPlace() { return place; }
    public String getRms() { return rms; }
    public String getStatus() { return status; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public String getUpdated() { return updated; }
    
    public void setDepthError(double depthError) { this.depthError = depthError; }
    public void setDmin(String dmin) { this.dmin = dmin; }
    public void setGap(double gap) { this.gap = gap; }
    public void setHorizontalError(double horizontalError) { this.horizontalError = horizontalError; }
    public void setId(String id) { this.id = id; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public void setLocationSource(String locationSource) { this.locationSource = locationSource; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public void setMag(double mag) { this.mag = mag; }
    public void setMagError(double magError) { this.magError = magError; }
    public void setMagNst(int magNst) { this.magNst = magNst; }
    public void setMagSource(String magSource) { this.magSource = magSource; }
    public void setMagType(String magType) { this.magType = magType; }
    public void setNet(String net) { this.net = net; }
    public void setNst(int nst) { this.nst = nst; }
    public void setPlace(String place) { this.place = place; }
    public void setRms(String rms) { this.rms = rms; }
    public void setStatus(String status) { this.status = status; }
    public void setTime(String time) { this.time = time; }
    public void setType(String type) { this.type = type; }
    public void setUpdated(String updated) { this.updated = updated; }
    
}
