/*
by Anthony Stump
Base code created: 30 Mar 2018
Split off: 7 May 2018
Updated: 13 May 2018
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class SysMonNote3 {
    
    private WebCommon wc = new WebCommon();
    
    private JSONObject mCellBattCPU(JSONArray dataIn, int intLen, int step) {
        String mCellBattCPU_ChartName = "Note 3: Battery / CPU Use";
        JSONObject mCellBattCPU_Glob = new JSONObject();
        JSONObject mCellBattCPU_Props = new JSONObject();
        JSONArray mCellBattCPU_Labels = new JSONArray();
        JSONArray mCellBattCPU_Data = new JSONArray();
        JSONArray mCellBattCPU_Data2 = new JSONArray();
        JSONArray mCellBattCPU_Data3 = new JSONArray();
        mCellBattCPU_Props
                .put("chartName", mCellBattCPU_ChartName).put("chartFileName", "mCellBattCPU")
                .put("sName", "CPU").put("sColor", "Yellow")
                .put("s2Name", "Battery").put("s2Color", "Red")
                .put("s3Name", "Memory").put("s3Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject tObj = dataIn.getJSONObject(i);
            float mCellBattCPU_MemUse = ((tObj.getFloat("MemoryUse")/1024/1024)/3*100);
            mCellBattCPU_Labels.put(tObj.getString("WalkTime"));
            mCellBattCPU_Data.put(tObj.getDouble("CPUUse"));
            mCellBattCPU_Data2.put(tObj.getInt("BattLevel"));
            mCellBattCPU_Data3.put(mCellBattCPU_MemUse);
        }
        mCellBattCPU_Glob
                .put("labels", mCellBattCPU_Labels)
                .put("data", mCellBattCPU_Data)
                .put("data2", mCellBattCPU_Data2)
                .put("data3", mCellBattCPU_Data3)
                .put("props", mCellBattCPU_Props);
        return mCellBattCPU_Glob;
    }
    
    private JSONObject mCellNet(JSONArray dataIn, int intLen, int step) {
        String mCellNet_ChartName = "Note 3: Data Network Use";
        long mCellNet_Cumulative = 0;
        long mCellNet_LastPhoneCellOctets = 0;
        long mCellNet_LastPhoneWiFiOctets = 0;
        long mCellNet_LastOctetsTotal = 0;
        JSONObject mCellNet_Glob = new JSONObject();
        JSONObject mCellNet_Props = new JSONObject();
        JSONArray mCellNet_Labels = new JSONArray();
        JSONArray mCellNet_Data = new JSONArray();
        JSONArray mCellNet_Data2 = new JSONArray();
        JSONArray mCellNet_Data3 = new JSONArray();
        mCellNet_Props
                .put("chartName", mCellNet_ChartName).put("chartFileName", "mCellNet")
                .put("sName", "rmnet0").put("sColor", "Yellow")
                .put("s2Name", "wlan0").put("s2Color", "Red")
                .put("s3Name", "Connections").put("s3Color", "Green")
                .put("xLabel", "WalkTime").put("yLabel", "Data Kbps");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject note3Object = dataIn.getJSONObject(i);
            long mCellNet_ThisDiffPhoneCell = 0;
            long mCellNet_ThisDiffPhoneWiFi = 0;
            long mCellNet_ThisPhoneCellOctets = note3Object.getLong("rmnet0Rx") + note3Object.getLong("rmnet0Tx");
            long mCellNet_ThisPhoneWiFiOctets = note3Object.getLong("wlan0Rx") + note3Object.getLong("wlan0Tx");
            long mCellNet_ThisOctetsTotal = mCellNet_ThisPhoneCellOctets + mCellNet_ThisPhoneWiFiOctets;
            mCellNet_Labels.put(note3Object.getString("WalkTime"));
            if(mCellNet_LastPhoneCellOctets <= mCellNet_ThisPhoneCellOctets && mCellNet_LastPhoneCellOctets != 0) {
                mCellNet_ThisDiffPhoneCell = ((mCellNet_ThisPhoneCellOctets - mCellNet_LastPhoneCellOctets)/1024/intLen/step);
            }
            if(mCellNet_LastPhoneWiFiOctets <= mCellNet_ThisPhoneWiFiOctets && mCellNet_LastPhoneWiFiOctets != 0) {
                mCellNet_ThisDiffPhoneWiFi = ((mCellNet_ThisPhoneWiFiOctets - mCellNet_LastPhoneWiFiOctets)/1024/intLen/step);
            }
            mCellNet_Cumulative = mCellNet_Cumulative + (mCellNet_ThisOctetsTotal - mCellNet_LastOctetsTotal);
            mCellNet_Data.put(mCellNet_ThisDiffPhoneCell);
            mCellNet_Data2.put(mCellNet_ThisDiffPhoneWiFi);
            mCellNet_Data3.put(note3Object.getInt("ActiveConn"));
            mCellNet_LastPhoneCellOctets = mCellNet_ThisPhoneCellOctets;
            mCellNet_LastPhoneWiFiOctets = mCellNet_ThisPhoneWiFiOctets;
            mCellNet_LastOctetsTotal = mCellNet_ThisOctetsTotal;
        }
        mCellNet_Glob
                .put("labels", mCellNet_Labels)
                .put("data", mCellNet_Data)
                .put("data2", mCellNet_Data2)
                .put("data3", mCellNet_Data3)
                .put("props", mCellNet_Props);
        return mCellNet_Glob;
    }
    
    private JSONObject mCellSig(JSONArray dataIn, int intLen, int step) {
        String mCellSig_ChartName = "Note 3: Signal Strength";
        JSONObject mCellSig_Glob = new JSONObject();
        JSONObject mCellSig_Props = new JSONObject();
        JSONArray mCellSig_Labels = new JSONArray();
        JSONArray mCellSig_Data = new JSONArray();
        JSONArray mCellSig_Data2 = new JSONArray();
        JSONArray mCellSig_Data3 = new JSONArray();
        JSONArray mCellSig_Data4 = new JSONArray();                    
        mCellSig_Props
                .put("chartName", mCellSig_ChartName).put("chartFileName", "mCellSig")
                .put("sName", "LTE").put("sColor", "Orange")
                .put("s2Name", "CDMA").put("s2Color", "Yellow")
                .put("s3Name", "EVDO").put("s3Color", "Blue")
                .put("s4Name", "GSM").put("s4Color", "Green")
                .put("xLabel", "WalkTime").put("yLabel", "DBz");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject note3Object = dataIn.getJSONObject(i);
            mCellSig_Labels.put(note3Object.getString("WalkTime"));
            int mCellSig_LTE = 0;
            int mCellSig_CDMA = 0;
            int mCellSig_EVDO = 0;
            int mCellSig_GSM = 0;
            if(note3Object.getInt("SigStrLTE") > 0) { mCellSig_LTE = 0; } else { mCellSig_LTE = note3Object.getInt("SigStrLTE"); }
            if(note3Object.getInt("SigStrCDMA") > 0) { mCellSig_CDMA = 0; } else { mCellSig_CDMA = note3Object.getInt("SigStrCDMA"); }
            if(note3Object.getInt("SigStrEVDO") > 0) { mCellSig_EVDO = 0; } else { mCellSig_EVDO = note3Object.getInt("SigStrEVDO"); }
            if(note3Object.getInt("SigStrGSM") > 0) { mCellSig_GSM = 0; } else { mCellSig_GSM = note3Object.getInt("SigStrGSM"); }
            mCellSig_Data.put(mCellSig_LTE);
            mCellSig_Data2.put(mCellSig_CDMA);
            mCellSig_Data3.put(mCellSig_EVDO);
            mCellSig_Data4.put(mCellSig_GSM);
        }
        mCellSig_Glob
                .put("labels", mCellSig_Labels)
                .put("data", mCellSig_Data)
                .put("data2", mCellSig_Data2)
                .put("data3", mCellSig_Data3)
                .put("data4", mCellSig_Data4)
                .put("props", mCellSig_Props);
        return mCellSig_Glob;
    }
    
    private JSONObject mCellTemp(JSONArray dataIn, int intLen, int step) {
        String mCellTemp_ChartName = "Note 3: Temperatures";
        JSONObject mCellTemp_Glob = new JSONObject();
        JSONObject mCellTemp_Props = new JSONObject();
        JSONArray mCellTemp_Labels = new JSONArray();
        JSONArray mCellTemp_Data = new JSONArray();           
        mCellTemp_Props
                .put("chartName", mCellTemp_ChartName).put("chartFileName", "mCellTemp")
                .put("sName", "Temperature").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "deg. F");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject note3Object = dataIn.getJSONObject(i);
            mCellTemp_Labels.put(note3Object.getString("WalkTime"));
            mCellTemp_Data.put(wc.tempC2F(note3Object.getInt("BattTemp")/10));
        }
        mCellTemp_Glob
                .put("labels", mCellTemp_Labels)
                .put("data", mCellTemp_Data)
                .put("props", mCellTemp_Props);
        return mCellTemp_Glob;
    }
    
    private JSONObject mCellTempRapid(JSONArray dataIn, int intLen, int step) {
        String mCellTempRapid_ChartName = "Note 3: Temperatures (Rapid Polling)";
        JSONObject mCellTempRapid_Glob = new JSONObject();
        JSONObject mCellTempRapid_Props = new JSONObject();
        JSONArray mCellTempRapid_Labels = new JSONArray();
        JSONArray mCellTempRapid_Data = new JSONArray();
        mCellTempRapid_Props
                .put("chartName", mCellTempRapid_ChartName).put("chartFileName", "mCellTempRapid")
                .put("sName", "Temperature").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "deg. F");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject note3Object = dataIn.getJSONObject(i);
            //JSONObject note3RapidSensors = note3Glob.getJSONObject("SensorsRapid");
            mCellTempRapid_Labels.put(note3Object.getString("WalkTime"));
            mCellTempRapid_Data.put(0);
            /* for (JSONObject thisIteratedJson : note3RapidSensors) {
                double mCellTempRapid_ThisTemp = thisIteratedJson.getDouble("AmbientTemperature");
                if(mCellTempRapid_ThisTemp != 0) {
                    mCellTempRapid_Data.put(thisIteratedJson.getDouble("AmbientTemperatureF"));
                }
            } */
        }
        mCellTempRapid_Glob
                .put("labels", mCellTempRapid_Labels)
                .put("data", mCellTempRapid_Data)
                .put("props", mCellTempRapid_Props);    
        return mCellTempRapid_Glob;
    }
    
    public JSONObject getCellBattCPU(JSONArray dataIn, int intLen, int step) { return mCellBattCPU(dataIn, intLen, step); }
    public JSONObject getCellNet(JSONArray dataIn, int intLen, int step) { return mCellNet(dataIn, intLen, step); }
    public JSONObject getCellSig(JSONArray dataIn, int intLen, int step) { return mCellSig(dataIn, intLen, step); }
    public JSONObject getCellTemp(JSONArray dataIn, int intLen, int step) { return mCellTemp(dataIn, intLen, step); }
    public JSONObject getCellTempRapid(JSONArray dataIn, int intLen, int step) { return mCellTempRapid(dataIn, intLen, step); }
    
}
