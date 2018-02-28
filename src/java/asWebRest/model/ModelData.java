/*
by Anthony Stump
Created: 26 Feb 2018

For tables:
    WxObs.KOJC_MFMD
*/

package asWebRest.model;

import org.json.JSONObject;

public class ModelData {
    
    private int obsId;
    private String runString;
    private String gfs;
    private String nam;
    private String rap;
    private String cmc;
    private JSONObject hrrr;
    private JSONObject hrwa;
    private JSONObject hrwn;
    private JSONObject srfa;
    private JSONObject srfn;
    
    public int getObsId() { return obsId; }
    public String getRunString() { return runString; }
    public String getGfs() { return gfs; }
    public String getNam() { return nam; }
    public String getRap() { return rap; }
    public String getCmc() { return cmc; }
    public JSONObject getHrrr() { return hrrr; }
    public JSONObject getHrwa() { return hrwa; }
    public JSONObject getHrwn() { return hrwn; }
    public JSONObject getSrfa() { return srfa; }
    public JSONObject getSrfn() { return srfn; }
    
    public void setObsId(int obsId) { this.obsId = obsId; }
    public void setRunString(String runString) { this.runString = runString; }
    public void setGfs(String gfs) { this.gfs = gfs; }
    public void setNam(String nam) { this.nam = nam; }
    public void setRap(String rap) { this.rap = rap; }
    public void setCmc(String cmc) { this.cmc = cmc; }
    public void setHrrr(JSONObject hrrr) { this.hrrr = hrrr; }
    public void setHrwa(JSONObject hrwa) { this.hrwa = hrwa; }
    public void setHrwn(JSONObject hrwn) { this.hrwn = hrwn; }
    public void setSrfa(JSONObject srfa) { this.srfa = srfa; }
    public void setSrfn(JSONObject srfn) { this.srfn = srfn; }
    
}
