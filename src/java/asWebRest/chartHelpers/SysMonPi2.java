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

public class SysMonPi2 {
    
    private WebCommon wc = new WebCommon();
    
    private JSONObject mPi2CPU(JSONArray dataIn, int intLen, int step) {
        String mPi2CPU_ChartName = "Raspberry Pi 2: CPU Use";
        JSONObject mPi2CPU_Glob = new JSONObject();
        JSONObject mPi2CPU_Props = new JSONObject();
        JSONArray mPi2CPU_Labels = new JSONArray();
        JSONArray mPi2CPU_Data = new JSONArray();
        JSONArray mPi2CPU_Data2 = new JSONArray();
        JSONArray mPi2CPU_Data3 = new JSONArray();
        JSONArray mPi2CPU_Data4 = new JSONArray();
        JSONArray mPi2CPU_Data5 = new JSONArray();
        mPi2CPU_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPi2CPU_ChartName).put("chartFileName", "mPi2CPU")
                .put("sName", "Average").put("sColor", "Yellow")
                .put("s2Name", "Core 1").put("s2Color", "Gray")
                .put("s3Name", "Core 2").put("s3Color", "Gray")
                .put("s4Name", "Core 3").put("s4Color", "Gray")
                .put("s5Name", "Core 4").put("s5Color", "Gray")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject pi2Object = dataIn.getJSONObject(i);
            double mPi2CPU_Average = ((
                    pi2Object.getInt("CPULoad") +
                    pi2Object.getInt("CPULoad2") +
                    pi2Object.getInt("CPULoad3") +
                    pi2Object.getInt("CPULoad4")) / 4
            );
            mPi2CPU_Labels.put(pi2Object.getString("WalkTime"));
            mPi2CPU_Data.put(mPi2CPU_Average);
            mPi2CPU_Data2.put(pi2Object.getInt("CPULoad"));
            mPi2CPU_Data3.put(pi2Object.getInt("CPULoad2"));
            mPi2CPU_Data4.put(pi2Object.getInt("CPULoad3"));
            mPi2CPU_Data5.put(pi2Object.getInt("CPULoad4"));
        }
        mPi2CPU_Glob
                .put("labels", mPi2CPU_Labels)
                .put("data", mPi2CPU_Data)
                .put("data2", mPi2CPU_Data2)
                .put("data3", mPi2CPU_Data3)
                .put("data4", mPi2CPU_Data4)
                .put("data5", mPi2CPU_Data5)
                .put("props", mPi2CPU_Props);
        return mPi2CPU_Glob;
    }
    
    public JSONObject mPi2GPSSpeed(JSONArray dataIn, int intLen, int step) {
        String mPi2GPSSpeed_ChartName = "Raspberry Pi 2: GPS Statistics";
        JSONObject mPi2GPSSpeed_Glob = new JSONObject();
        JSONObject mPi2GPSSpeed_Props = new JSONObject();
        JSONArray mPi2GPSSpeed_Labels = new JSONArray();
        JSONArray mPi2GPSSpeed_Data = new JSONArray();
        JSONArray mPi2GPSSpeed_Data2 = new JSONArray();
        mPi2GPSSpeed_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPi2GPSSpeed_ChartName).put("chartFileName", "mPi2GPSSpeed")
                .put("sName", "Speed MPH").put("sColor", "White")
                .put("s2Name", "Fix Age MS").put("s2Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "MPH (MS)");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject pi2Object = dataIn.getJSONObject(i);
            mPi2GPSSpeed_Labels.put(pi2Object.getString("WalkTime"));
            mPi2GPSSpeed_Data.put(pi2Object.getDouble("GPSSpeedMPH"));
            mPi2GPSSpeed_Data2.put((double) pi2Object.getInt("GPSAgeMS") / 100);
        }
        mPi2GPSSpeed_Glob
                .put("labels", mPi2GPSSpeed_Labels)
                .put("data", mPi2GPSSpeed_Data)
                .put("data2", mPi2GPSSpeed_Data2)
                .put("props", mPi2GPSSpeed_Props);
        return mPi2GPSSpeed_Glob;
    }
    
    public JSONObject mPi2Light(JSONArray dataIn, int intLen, int step) {
        String mPi2Light_ChartName = "Raspberry Pi 2: Light Level";
        JSONObject mPi2Light_Glob = new JSONObject();
        JSONObject mPi2Light_Props = new JSONObject();
        JSONArray mPi2Light_Labels = new JSONArray();
        JSONArray mPi2Light_Data = new JSONArray();
        mPi2Light_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPi2Light_ChartName).put("chartFileName", "mPi2Light")
                .put("sName", "Light Level").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "Units");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject pi2Object = dataIn.getJSONObject(i);
            mPi2Light_Labels.put(pi2Object.getString("WalkTime"));
            mPi2Light_Data.put(pi2Object.getInt("LightLevel"));
        }
        mPi2Light_Glob
                .put("labels", mPi2Light_Labels)
                .put("data", mPi2Light_Data)
                .put("props", mPi2Light_Props);
        return mPi2Light_Glob;
    }

    public JSONObject mPi2Load(JSONArray dataIn, int intLen, int step) {
        String mPi2Load_ChartName = "Raspberry Pi 2: System Load";
        JSONObject mPi2Load_Glob = new JSONObject();
        JSONObject mPi2Load_Props = new JSONObject();
        JSONArray mPi2Load_Labels = new JSONArray();
        JSONArray mPi2Load_Data = new JSONArray();
        JSONArray mPi2Load_Data2 = new JSONArray();
        JSONArray mPi2Load_Data3 = new JSONArray();
        mPi2Load_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPi2Load_ChartName).put("chartFileName", "mPi2Load")
                .put("sName", "Load Index").put("sColor", "Red")
                .put("s2Name", "5 min Avg").put("s2Color", "Green")
                .put("s3Name", "15 min Avg").put("s3Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Load Index");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject pi2Object = dataIn.getJSONObject(i);
            mPi2Load_Labels.put(pi2Object.getString("WalkTime"));
            mPi2Load_Data.put(pi2Object.getDouble("LoadIndex1"));
            mPi2Load_Data2.put(pi2Object.getDouble("LoadIndex5"));
            mPi2Load_Data3.put(pi2Object.getDouble("LoadIndex15"));
        }
        mPi2Load_Glob
                .put("labels", mPi2Load_Labels)
                .put("data", mPi2Load_Data)
                .put("data2", mPi2Load_Data2)
                .put("data3", mPi2Load_Data3)
                .put("props", mPi2Load_Props);
        return mPi2Load_Glob;
    }
                    
    public JSONObject mPi2Memory(JSONArray dataIn, int intLen, int step) {
        String mPi2Memory_ChartName = "Raspberry Pi 2: Memory Use";
        JSONObject mPi2Memory_Glob = new JSONObject();
        JSONObject mPi2Memory_Props = new JSONObject();
        JSONArray mPi2Memory_Labels = new JSONArray();
        JSONArray mPi2Memory_Data = new JSONArray();
        JSONArray mPi2Memory_Data2 = new JSONArray();
        JSONArray mPi2Memory_Data3 = new JSONArray();
        JSONArray mPi2Memory_Data4 = new JSONArray();
        mPi2Memory_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPi2Memory_ChartName).put("chartFileName", "mPi2Memory")
                .put("sName", "Overall").put("sColor", "Yellow")
                .put("s2Name", "Swap").put("s2Color", "Red")
                .put("s3Name", "Buffers").put("s3Color", "Orange")
                .put("s4Name", "Cached").put("s4Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject pi2Object = dataIn.getJSONObject(i);
            float mPi2Memory_OverallUse = (
                pi2Object.getFloat("KMemPhysU") - (
                pi2Object.getFloat("KMemBuffU") +
                pi2Object.getFloat("KMemCachedU"))
                / 1024
            );
            mPi2Memory_Labels.put(pi2Object.getString("WalkTime"));
            mPi2Memory_Data.put(mPi2Memory_OverallUse);
            mPi2Memory_Data2.put(pi2Object.getFloat("KSwapU")/1024);
            mPi2Memory_Data3.put(pi2Object.getFloat("KMemBuffU")/1024);
            mPi2Memory_Data4.put(pi2Object.getFloat("KMemCachedU")/1024);
        }
        mPi2Memory_Glob
                .put("labels", mPi2Memory_Labels)
                .put("data", mPi2Memory_Data)
                .put("data2", mPi2Memory_Data2)
                .put("data3", mPi2Memory_Data3)
                .put("data4", mPi2Memory_Data4)
                .put("props", mPi2Memory_Props);    
        return mPi2Memory_Glob;
    }
     
    public JSONObject mPi2Temp(JSONArray dataIn, int intLen, int step) {
        String mPi2Temp_ChartName = "Raspberry Pi 2: Temperature";
        JSONObject mPi2Temp_Glob = new JSONObject();
        JSONObject mPi2Temp_Props = new JSONObject();
        JSONArray mPi2Temp_Labels = new JSONArray();
        JSONArray mPi2Temp_Data = new JSONArray();
        mPi2Temp_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mPi2Temp_ChartName).put("chartFileName", "mPi2Temp")
                .put("sName", "Temperature").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "degrees F");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject pi2Object = dataIn.getJSONObject(i);
            mPi2Temp_Labels.put(pi2Object.getString("WalkTime"));
            mPi2Temp_Data.put(pi2Object.getDouble("ExtTemp"));
        }
        mPi2Temp_Glob
                .put("labels", mPi2Temp_Labels)
                .put("data", mPi2Temp_Data)
                .put("props", mPi2Temp_Props);    
        return mPi2Temp_Glob;
    }
    
    public JSONObject getPi2CPU(JSONArray dataIn, int intLen, int step) { return mPi2CPU(dataIn, intLen, step); }
    public JSONObject getPi2GPSSpeed(JSONArray dataIn, int intLen, int step) { return mPi2GPSSpeed(dataIn, intLen, step); }
    public JSONObject getPi2Light(JSONArray dataIn, int intLen, int step) { return mPi2Light(dataIn, intLen, step); }
    public JSONObject getPi2Load(JSONArray dataIn, int intLen, int step) { return mPi2Load(dataIn, intLen, step); }
    public JSONObject getPi2Memory(JSONArray dataIn, int intLen, int step) { return mPi2Memory(dataIn, intLen, step); }
    public JSONObject getPi2Temp(JSONArray dataIn, int intLen, int step) { return mPi2Temp(dataIn, intLen, step); }
    
}
