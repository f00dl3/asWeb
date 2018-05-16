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
    
    public JSONObject getGasMcf(JSONArray dataIn) { return gasMcf(dataIn); }
    public JSONObject getKWhU(JSONArray dataIn) { return kWhU(dataIn); }
    
}
