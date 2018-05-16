/*
by Anthony Stump
Created: 16 May 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utilities {
    
    private JSONObject gasMcf(JSONArray dataIn) {
        final String gasMcf_Name = "Gas use MCF";
        JSONObject gasMcf_Glob = new JSONObject();
        JSONObject gasMcf_Props = new JSONObject();
        JSONArray gasMcf_Labels = new JSONArray();
        JSONArray gasMcf_Data = new JSONArray();
        gasMcf_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", gasMcf_Name).put("chartFileName", "UseGas")
                .put("sName", "MCF").put("sColor", "White")
                .put("xLabel", "Date").put("yLabel", "Million Cubic Feet");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            gasMcf_Labels.put(thisObject.getString("Month"));
            gasMcf_Data.put(thisObject.getDouble("TotalMCF"));
        }
        gasMcf_Glob
                .put("labels", gasMcf_Labels)
                .put("data", gasMcf_Data)
                .put("props", gasMcf_Props);
        return gasMcf_Glob;
    }
    
    private JSONObject kWhU(JSONArray dataIn) {
        final String kWhU_Name = "Electricity Use (kWh)";
        JSONObject kWhU_Glob = new JSONObject();
        JSONObject kWhU_Props = new JSONObject();
        JSONArray kWhU_Labels = new JSONArray();
        JSONArray kWhU_Data = new JSONArray();
        kWhU_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", kWhU_Name).put("chartFileName", "UseElecD")
                .put("sName", "kWh").put("sColor", "White")
                .put("xLabel", "Date").put("yLabel", "kWh");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            kWhU_Labels.put(thisObject.getString("Date"));
            kWhU_Data.put(thisObject.getDouble("kWh"));
        }
        kWhU_Glob
                .put("labels", kWhU_Labels)
                .put("data", kWhU_Data)
                .put("props", kWhU_Props);
        return kWhU_Glob;
    }
    
    private JSONObject phData(JSONArray dataIn) {
        final String phData_Name = "Phone use - Data in MB";
        JSONObject phData_Glob = new JSONObject();
        JSONObject phData_Props = new JSONObject();
        JSONArray phData_Labels = new JSONArray();
        JSONArray phData_Data = new JSONArray();
        phData_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", phData_Name).put("chartFileName", "CellData")
                .put("sName", "Data").put("sColor", "White")
                .put("xLabel", "Bill").put("yLabel", "MB Data");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            phData_Labels.put(thisObject.getString("Bill"));
            phData_Data.put(thisObject.getInt("MBData"));
        }
        phData_Glob
                .put("labels", phData_Labels)
                .put("data", phData_Data)
                .put("props", phData_Props);
        return phData_Glob;
    }
    
    private JSONObject phMin(JSONArray dataIn) {
        final String phMin_Name = "Phone use - Minutes";
        JSONObject phMin_Glob = new JSONObject();
        JSONObject phMin_Props = new JSONObject();
        JSONArray phMin_Labels = new JSONArray();
        JSONArray phMin_Data = new JSONArray();
        phMin_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", phMin_Name).put("chartFileName", "CellMin")
                .put("sName", "Minutes").put("sColor", "White")
                .put("xLabel", "Bill").put("yLabel", "Minutes");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            phMin_Labels.put(thisObject.getString("Bill"));
            phMin_Data.put(thisObject.getInt("Minutes"));
        }
        phMin_Glob
                .put("labels", phMin_Labels)
                .put("data", phMin_Data)
                .put("props", phMin_Props);
        return phMin_Glob;
    }
        
    private JSONObject phMms(JSONArray dataIn) {
        final String phMms_Name = "Phone use - Multimedia Messages/MMS";
        JSONObject phMms_Glob = new JSONObject();
        JSONObject phMms_Props = new JSONObject();
        JSONArray phMms_Labels = new JSONArray();
        JSONArray phMms_Data = new JSONArray();
        phMms_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", phMms_Name).put("chartFileName", "CellMMS")
                .put("sName", "Messages").put("sColor", "White")
                .put("xLabel", "Bill").put("yLabel", "Messages");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            phMms_Labels.put(thisObject.getString("Bill"));
            phMms_Data.put(thisObject.getInt("MMS"));
        }
        phMms_Glob
                .put("labels", phMms_Labels)
                .put("data", phMms_Data)
                .put("props", phMms_Props);
        return phMms_Glob;
    }    
    
    private JSONObject phText(JSONArray dataIn) {
        final String phText_Name = "Phone use - Text Messages";
        JSONObject phText_Glob = new JSONObject();
        JSONObject phText_Props = new JSONObject();
        JSONArray phText_Labels = new JSONArray();
        JSONArray phText_Data = new JSONArray();
        phText_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", phText_Name).put("chartFileName", "CellMin")
                .put("sName", "Minutes").put("sColor", "White")
                .put("xLabel", "Date").put("yLabel", "Minutes");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            phText_Labels.put(thisObject.getString("Bill"));
            phText_Data.put(thisObject.getInt("Minutes"));
        }
        phText_Glob
                .put("labels", phText_Labels)
                .put("data", phText_Data)
                .put("props", phText_Props);
        return phText_Glob;
    }
    
    public JSONObject getGasMcf(JSONArray dataIn) { return gasMcf(dataIn); }
    public JSONObject getKWhU(JSONArray dataIn) { return kWhU(dataIn); }
    public JSONObject getPhData(JSONArray dataIn) { return phData(dataIn); }
    public JSONObject getPhMin(JSONArray dataIn) { return phMin(dataIn); }
    public JSONObject getPhMms(JSONArray dataIn) { return phMms(dataIn); }
    public JSONObject getPhText(JSONArray dataIn) { return phText(dataIn); }
    
}
