/*
by Anthony Stump
Created: 16 May 2018
Updated: 23 May 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {
    
    private JSONObject cf6cpc(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) {       
        String cf6cpc_ChartName = "CPC Data: " + cf6ChDateStart + " to " + cf6ChDateEnd;
        JSONObject cf6cpc_Glob = new JSONObject();
        JSONObject cf6cpc_Props = new JSONObject();
        JSONArray cf6cpc_Labels = new JSONArray();
        JSONArray cf6cpc_Data = new JSONArray();
        JSONArray cf6cpc_Data2 = new JSONArray();
        JSONArray cf6cpc_Data3 = new JSONArray();
        JSONArray cf6cpc_Data4 = new JSONArray();
        cf6cpc_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cf6cpc_ChartName).put("chartFileName", "cf6cpc")
                .put("sName", "AO").put("sColor", "Red")
                .put("s2Name", "AAO").put("s2Color", "Blue")
                .put("s3Name", "NAO").put("s3Color", "Green")
                .put("s4Name", "PNA").put("s4Color", "Yellow")
                .put("xLabel", "Date").put("yLabel", "Anom");
        for(int i = 0; i < cf6Data.length(); i++) {
            JSONObject thisObject = cf6Data.getJSONObject(i);
            cf6cpc_Labels.put(thisObject.getString("Date"));
            cf6cpc_Data.put(thisObject.getDouble("AO"));
            cf6cpc_Data2.put(thisObject.getDouble("AAO"));
            cf6cpc_Data3.put(thisObject.getDouble("NAO"));
            cf6cpc_Data4.put(thisObject.getDouble("PNA"));
        }
        cf6cpc_Glob
                .put("labels", cf6cpc_Labels)
                .put("data", cf6cpc_Data)
                .put("data2", cf6cpc_Data2)
                .put("data3", cf6cpc_Data3)
                .put("data4", cf6cpc_Data4)
                .put("props", cf6cpc_Props);
        return cf6cpc_Glob;
    }
    
    private JSONObject cf6depart(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) {
        String cf6Depart_ChartName = "KMCI Departures: " + cf6ChDateStart + " to " + cf6ChDateEnd;
        JSONObject cf6Depart_Glob = new JSONObject();
        JSONObject cf6Depart_Props = new JSONObject();
        JSONArray cf6Depart_Labels = new JSONArray();
        JSONArray cf6Depart_Data = new JSONArray();   
        cf6Depart_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cf6Depart_ChartName).put("chartFileName", "cf6Depart")
                .put("sName", "Departure").put("sColor", "Yellow")
                .put("xLabel", "Date").put("yLabel", "degrees F");
        for(int i = 0; i < cf6Data.length(); i++) {
            JSONObject thisObject = cf6Data.getJSONObject(i);
            cf6Depart_Labels.put(thisObject.getString("Date"));
            cf6Depart_Data.put(thisObject.getInt("DFNorm"));
        }
        return cf6Depart_Glob;
    }
    
    private JSONObject cf6temps(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) {
        String cf6Temps_ChartName = "KMCI Temperatures: " + cf6ChDateStart + " to " + cf6ChDateEnd;
        JSONObject cf6Temps_Glob = new JSONObject();
        JSONObject cf6Temps_Props = new JSONObject();
        JSONArray cf6Temps_Labels = new JSONArray();
        JSONArray cf6Temps_Data = new JSONArray();
        JSONArray cf6Temps_Data2 = new JSONArray();
        cf6Temps_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cf6Temps_ChartName).put("chartFileName", "cf6Temps")
                .put("sName", "High").put("sColor", "Red")
                .put("s2Name", "Low").put("s2Color", "Blue")
                .put("xLabel", "Date").put("yLabel", "degress F");
        for(int i = 0; i < cf6Data.length(); i++) {
            JSONObject thisObject = cf6Data.getJSONObject(i);
            cf6Temps_Labels.put(thisObject.getString("Date"));
            cf6Temps_Data.put(thisObject.getInt("High"));
            cf6Temps_Data2.put(thisObject.getInt("Low"));
        }
        return cf6Temps_Glob;
    }
    
    private JSONObject mosTemps(JSONArray dataIn) {
        String mosTemps_ChartName = "Model Output Soundings: Temps";
        JSONObject mosTemps_Glob = new JSONObject();
        JSONObject mosTemps_Props = new JSONObject();
        JSONArray mosTemps_Labels = new JSONArray();
        JSONArray mosTemps_Data = new JSONArray();
        JSONArray mosTemps_Data1 = new JSONArray();
        JSONArray mosTemps_Data2 = new JSONArray();
        JSONArray mosTemps_Data3 = new JSONArray();
        JSONArray mosTemps_Data4 = new JSONArray();
        JSONArray mosTemps_Data5 = new JSONArray();
        JSONArray mosTemps_Data6 = new JSONArray();
        JSONArray mosTemps_Data7 = new JSONArray();
        JSONArray mosTemps_Data8 = new JSONArray();
        JSONArray mosTemps_Data9 = new JSONArray();
        JSONArray mosTemps_Data10 = new JSONArray();
        mosTemps_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", mosTemps_ChartName).put("chartFileName", "mosTemps")
                .put("sName", "Merged TF").put("sColor", "Red")
                .put("s2Name", "Merged DF").put("s2Color", "Green")
                .put("s3Name", "GFS TF").put("s3Color", "White")
                .put("s4Name", "GFS DF").put("s4Color", "Gray")
                .put("s5Name", "NAM TF").put("s5Color", "White")
                .put("s6Name", "NAM DF").put("s6Color", "Gray")
                .put("s7Name", "CMC TF").put("s7Color", "White")
                .put("s8Name", "CMC DF").put("s8Color", "Gray")
                .put("s9Name", "HRRR TF").put("s9Color", "White")
                .put("s10Name", "HRRR DF").put("s10Color", "Gray")
                .put("xLabel", "Date").put("yLabel", "degrees F");
        return mosTemps_Glob;
    }
    
    public JSONObject getCf6cpc(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6cpc(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getCf6depart(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6depart(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getCf6temps(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6temps(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getMosTemps(JSONArray dataIn) { return mosTemps(dataIn); }
       
}
    
