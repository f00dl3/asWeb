/*
by Anthony Stump
Base code created: 30 Mar 2018
Split off: 7 May 2018
Updated: 13 May 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;
import asWebRest.shared.WebCommon;

public class SysMonPi {
    
    private WebCommon wc = new WebCommon();
    
    private JSONObject mPiAmb(JSONArray dataIn, int intLen, int step) {
        String mPiAmb_ChartName = "Raspberry Pi: Ambient Sensors";
        JSONObject mPiAmb_Glob = new JSONObject();
        JSONObject mPiAmb_Props = new JSONObject();
        JSONArray mPiAmb_Labels = new JSONArray();
        JSONArray mPiAmb_Data = new JSONArray();
        JSONArray mPiAmb_Data2 = new JSONArray();
        mPiAmb_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPiAmb_ChartName).put("chartFileName", "mPiAmb")
                .put("sName", "Light").put("sColor", "Yellow")
                .put("s2Name", "Sound").put("s2Color", "Green")
                .put("xLabel", "WalkTime").put("yLabel", "Units");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject piObject = dataIn.getJSONObject(i);
            mPiAmb_Labels.put(piObject.getString("WalkTime"));
            mPiAmb_Data.put(piObject.getInt("ExtAmbLight"));
            mPiAmb_Data2.put(piObject.getInt("ExtNoise"));
        }
        mPiAmb_Glob
                .put("labels", mPiAmb_Labels)
                .put("data", mPiAmb_Data)
                .put("data2", mPiAmb_Data2)
                .put("props", mPiAmb_Props);                 
        return mPiAmb_Glob;
    }
    
    private JSONObject mPiCPU(JSONArray dataIn, int intLen, int step) {
        String mPiCPU_ChartName = "Raspberry Pi: CPU Use";
        JSONObject mPiCPU_Glob = new JSONObject();
        JSONObject mPiCPU_Props = new JSONObject();
        JSONArray mPiCPU_Labels = new JSONArray();
        JSONArray mPiCPU_Data = new JSONArray();
        JSONArray mPiCPU_Data2 = new JSONArray();
        JSONArray mPiCPU_Data3 = new JSONArray();
        JSONArray mPiCPU_Data4 = new JSONArray();
        JSONArray mPiCPU_Data5 = new JSONArray();
        mPiCPU_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPiCPU_ChartName).put("chartFileName", "mPiCPU")
                .put("sName", "Average").put("sColor", "Yellow")
                .put("s2Name", "Core 1").put("s2Color", "Gray")
                .put("s3Name", "Core 2").put("s3Color", "Gray")
                .put("s4Name", "Core 3").put("s4Color", "Gray")
                .put("s5Name", "Core 4").put("s5Color", "Gray")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject piObject = dataIn.getJSONObject(i);
            double mPiCPU_Average = ((
                piObject.getInt("CPULoad") +
                piObject.getInt("CPULoad2") +
                piObject.getInt("CPULoad3") +
                piObject.getInt("CPULoad4")) / 4
            );
            mPiCPU_Labels.put(piObject.getString("WalkTime"));
            mPiCPU_Data.put(mPiCPU_Average);
            mPiCPU_Data2.put(piObject.getInt("CPULoad"));
            mPiCPU_Data3.put(piObject.getInt("CPULoad2"));
            mPiCPU_Data4.put(piObject.getInt("CPULoad3"));
            mPiCPU_Data5.put(piObject.getInt("CPULoad4"));
        }
        mPiCPU_Glob
                .put("labels", mPiCPU_Labels)
                .put("data", mPiCPU_Data)
                .put("data2", mPiCPU_Data2)
                .put("data3", mPiCPU_Data3)
                .put("data4", mPiCPU_Data4)
                .put("data5", mPiCPU_Data5)
                .put("props", mPiCPU_Props);
        return mPiCPU_Glob;
    }
    
    private JSONObject mPiLoad(JSONArray dataIn, int intLen, int step) {
        String mPiLoad_ChartName = "Raspberry Pi: System Load";
        JSONObject mPiLoad_Glob = new JSONObject();
        JSONObject mPiLoad_Props = new JSONObject();
        JSONArray mPiLoad_Labels = new JSONArray();
        JSONArray mPiLoad_Data = new JSONArray();
        JSONArray mPiLoad_Data2 = new JSONArray();
        JSONArray mPiLoad_Data3 = new JSONArray();
        mPiLoad_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPiLoad_ChartName).put("chartFileName", "mPiLoad")
                .put("sName", "Load Index").put("sColor", "Red")
                .put("s2Name", "5 min Avg").put("s2Color", "Green")
                .put("s3Name", "15 min Avg").put("s3Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Load Index");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject piObject = dataIn.getJSONObject(i);
            mPiLoad_Labels.put(piObject.getString("WalkTime"));
            mPiLoad_Data.put(piObject.getDouble("LoadIndex1"));
            mPiLoad_Data2.put(piObject.getDouble("LoadIndex5"));
            mPiLoad_Data3.put(piObject.getDouble("LoadIndex15"));
        }
        mPiLoad_Glob
                .put("labels", mPiLoad_Labels)
                .put("data", mPiLoad_Data)
                .put("data2", mPiLoad_Data2)
                .put("data3", mPiLoad_Data3)
                .put("props", mPiLoad_Props);
        return mPiLoad_Glob;
    }
    
    private JSONObject mPiMemory(JSONArray dataIn, int intLen, int step) {
        String mPiMemory_ChartName = "Raspberry Pi: Memory Use";
        JSONObject mPiMemory_Glob = new JSONObject();
        JSONObject mPiMemory_Props = new JSONObject();
        JSONArray mPiMemory_Labels = new JSONArray();
        JSONArray mPiMemory_Data = new JSONArray();
        JSONArray mPiMemory_Data2 = new JSONArray();
        JSONArray mPiMemory_Data3 = new JSONArray();
        JSONArray mPiMemory_Data4 = new JSONArray();
        mPiMemory_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPiMemory_ChartName).put("chartFileName", "mPiMemory")
                .put("sName", "Overall").put("sColor", "Yellow")
                .put("s2Name", "Swap").put("s2Color", "Red")
                .put("s3Name", "Buffers").put("s3Color", "Orange")
                .put("s4Name", "Cached").put("s4Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject piObject = dataIn.getJSONObject(i);
            float mPiMemory_OverallUse = (
                piObject.getFloat("KMemPhysU") - (
                piObject.getFloat("KMemBuffU") +
                piObject.getFloat("KMemCachedU"))
                / 1024
            );
            mPiMemory_Labels.put(piObject.getString("WalkTime"));
            mPiMemory_Data.put(mPiMemory_OverallUse);
            mPiMemory_Data2.put(piObject.getFloat("KSwapU")/1024);
            mPiMemory_Data3.put(piObject.getFloat("KMemBuffU")/1024);
            mPiMemory_Data4.put(piObject.getFloat("KMemCachedU")/1024);
        }
        mPiMemory_Glob
                .put("labels", mPiMemory_Labels)
                .put("data", mPiMemory_Data)
                .put("data2", mPiMemory_Data2)
                .put("data3", mPiMemory_Data3)
                .put("data4", mPiMemory_Data4)
                .put("props", mPiMemory_Props);
        return mPiMemory_Glob;
    }
    
    private JSONObject mPiTemp(JSONArray dataIn, int intLen, int step) {
        String mPiTemp_ChartName = "Raspberry Pi: Temperature";
        JSONObject mPiTemp_Glob = new JSONObject();
        JSONObject mPiTemp_Props = new JSONObject();
        JSONArray mPiTemp_Labels = new JSONArray();
        JSONArray mPiTemp_Data = new JSONArray();
        mPiTemp_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPiTemp_ChartName).put("chartFileName", "mPiTemp")
                .put("sName", "Temperature").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "degrees F");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject piObject = dataIn.getJSONObject(i);
            mPiTemp_Labels.put(piObject.getString("WalkTime"));
            mPiTemp_Data.put(piObject.getDouble("ExtTemp"));
        }
        mPiTemp_Glob
                .put("labels", mPiTemp_Labels)
                .put("data", mPiTemp_Data)
                .put("props", mPiTemp_Props);
        return mPiTemp_Glob;
    }
    
    public JSONObject getPiAmb(JSONArray dataIn, int intLen, int step) { return mPiAmb(dataIn, intLen, step); }
    public JSONObject getPiCPU(JSONArray dataIn, int intLen, int step) { return mPiCPU(dataIn, intLen, step); }
    public JSONObject getPiLoad(JSONArray dataIn, int intLen, int step) { return mPiLoad(dataIn, intLen, step); }
    public JSONObject getPiMemory(JSONArray dataIn, int intLen, int step) { return mPiMemory(dataIn, intLen, step); }
    public JSONObject getPiTemp(JSONArray dataIn, int intLen, int step) { return mPiTemp(dataIn, intLen, step); }
    
}
