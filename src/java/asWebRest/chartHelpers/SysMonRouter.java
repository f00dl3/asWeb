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

public class SysMonRouter {
    
    private WebCommon wc = new WebCommon();
    
    private JSONObject mRouterCPU(JSONArray dataIn, int intLen, int step) {
        String mRouterCPU_ChartName = "Router: CPU Use";
        JSONObject mRouterCPU_Glob = new JSONObject();
        JSONObject mRouterCPU_Props = new JSONObject();
        JSONArray mRouterCPU_Labels = new JSONArray();
        JSONArray mRouterCPU_Data = new JSONArray();
        JSONArray mRouterCPU_Data2 = new JSONArray();
        JSONArray mRouterCPU_Data3 = new JSONArray();
        mRouterCPU_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mRouterCPU_ChartName).put("chartFileName", "mRouterCPU")
                .put("sName", "CPU Average").put("sColor", "Yellow")
                .put("s2Name", "Core 1").put("s2Color", "Gray")
                .put("s3Name", "Core 2").put("s3Color", "Gray")
                .put("xLabel", "WalkTime").put("yLabel", "Percentage");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject routerObject = dataIn.getJSONObject(i);
            double thisCPUAverage = (routerObject.getInt("CPULoad1") + routerObject.getInt("CPULoad2") / 2);
            mRouterCPU_Labels.put(routerObject.getString("WalkTime"));
            mRouterCPU_Data.put(thisCPUAverage);
            mRouterCPU_Data2.put(routerObject.getInt("CPULoad1"));
            mRouterCPU_Data3.put(routerObject.getInt("CPULoad2"));
        }
        mRouterCPU_Glob
                .put("labels", mRouterCPU_Labels)
                .put("data", mRouterCPU_Data)
                .put("data2", mRouterCPU_Data2)
                .put("data3", mRouterCPU_Data3)
                .put("props", mRouterCPU_Props);
        return mRouterCPU_Glob;
    }

    private JSONObject mRouterMemory(JSONArray dataIn, int intLen, int step) {
        String mRouterMemory_ChartName = "Router: Memory Use";
        JSONObject mRouterMemory_Glob = new JSONObject();
        JSONObject mRouterMemory_Props = new JSONObject();
        JSONArray mRouterMemory_Labels = new JSONArray();
        JSONArray mRouterMemory_Data = new JSONArray();
        JSONArray mRouterMemory_Data2 = new JSONArray();
        JSONArray mRouterMemory_Data3 = new JSONArray();
        JSONArray mRouterMemory_Data4 = new JSONArray();
        mRouterMemory_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mRouterMemory_ChartName).put("chartFileName", "mRouterMemory")
                .put("sName", "Overall").put("sColor", "Yellow")
                .put("s2Name", "Swap").put("s2Color", "Red")
                .put("s3Name", "Buffers").put("s3Color", "Orange")
                .put("s4Name", "Cached").put("s4Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject routerObject = dataIn.getJSONObject(i);
            float mRouterMemory_OverallUse = (
                routerObject.getFloat("KMemPhysU") - (
                routerObject.getFloat("KMemBuffU") +
                routerObject.getFloat("KMemCachedU"))
                / 1024
            );
            mRouterMemory_Labels.put(routerObject.getString("WalkTime"));
            mRouterMemory_Data.put(mRouterMemory_OverallUse);
            mRouterMemory_Data2.put(routerObject.getFloat("KSwapU")/1024);
            mRouterMemory_Data3.put(routerObject.getFloat("KMemBuffU")/1024);
            mRouterMemory_Data4.put(routerObject.getFloat("KMemCachedU")/1024);
        }
        mRouterMemory_Glob
                .put("labels", mRouterMemory_Labels)
                .put("data", mRouterMemory_Data)
                .put("data2", mRouterMemory_Data2)
                .put("data3", mRouterMemory_Data3)
                .put("data4", mRouterMemory_Data4)
                .put("props", mRouterMemory_Props);
        return mRouterMemory_Glob;
    }

    private JSONObject mRouterNet(JSONArray dataIn, int intLen, int step) {
        String mRouterNet_ChartName = "Router: Network Use";
        float mRouterNet_Cumulative = 0;
        float mRouterNet_LastOctetsTotal = 0;
        float mRouterNet_LastRouterE0Octets = 0;
        float mRouterNet_LastRouterE1Octets = 0;
        float mRouterNet_LastRouterE2Octets = 0;
        float mRouterNet_LastRouterE3Octets = 0;
        float mRouterNet_LastRouterV1Octets = 0;
        float mRouterNet_LastRouterV2Octets = 0;
        float mRouterNet_LastRouterB0Octets = 0;
        float mRouterNet_LastRouterB1Octets = 0;
        JSONObject mRouterNet_Glob = new JSONObject();
        JSONObject mRouterNet_Props = new JSONObject();
        JSONArray mRouterNet_Labels = new JSONArray();
        JSONArray mRouterNet_Data = new JSONArray();
        JSONArray mRouterNet_Data2 = new JSONArray();
        JSONArray mRouterNet_Data3 = new JSONArray();
        JSONArray mRouterNet_Data4 = new JSONArray();
        JSONArray mRouterNet_Data5 = new JSONArray();
        JSONArray mRouterNet_Data6 = new JSONArray();
        JSONArray mRouterNet_Data7 = new JSONArray();
        mRouterNet_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mRouterNet_ChartName).put("chartFileName", "mRouterNet")
                .put("sName", "eth0").put("sColor", "Yellow")
                .put("s2Name", "eth1").put("s2Color", "Orange")
                .put("s3Name", "eth2").put("s3Color", "Green")
                .put("s4Name", "eth3").put("s4Color", "Blue")
                .put("s5Name", "vlan1").put("s5Color", "Gray")
                .put("s6Name", "vlan2").put("s6Color", "Gray")
                .put("s7Name", "br0").put("s7Color", "White")
                .put("xLabel", "WalkTime").put("yLabel", "Data Mbps");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject routerObject = dataIn.getJSONObject(i);
            float mRouterNet_ThisDiffRouterE0Octets = 0;
            float mRouterNet_ThisDiffRouterE1Octets = 0;
            float mRouterNet_ThisDiffRouterE2Octets = 0;
            float mRouterNet_ThisDiffRouterE3Octets = 0;
            float mRouterNet_ThisDiffRouterV1Octets = 0;
            float mRouterNet_ThisDiffRouterV2Octets = 0;
            float mRouterNet_ThisDiffRouterB0Octets = 0;
            float mRouterNet_ThisRouterE0Octets = routerObject.getFloat("eth0Rx") + routerObject.getFloat("eth0Tx");
            float mRouterNet_ThisRouterE1Octets = routerObject.getFloat("eth1Rx") + routerObject.getFloat("eth0Tx");
            float mRouterNet_ThisRouterE2Octets = routerObject.getFloat("eth2Rx") + routerObject.getFloat("eth2Tx");
            float mRouterNet_ThisRouterE3Octets = routerObject.getFloat("eth3Rx") + routerObject.getFloat("eth3Tx");
            float mRouterNet_ThisRouterV1Octets = routerObject.getFloat("vlan1Rx") + routerObject.getFloat("vlan1Tx");
            float mRouterNet_ThisRouterV2Octets = routerObject.getFloat("vlan2Rx") + routerObject.getFloat("vlan2Tx");
            float mRouterNet_ThisRouterB0Octets = routerObject.getFloat("br0Rx") + routerObject.getFloat("br0Tx");
            float mRouterNet_ThisOctetsTotal = (
                    mRouterNet_ThisRouterE0Octets +
                    mRouterNet_ThisRouterE1Octets +
                    mRouterNet_ThisRouterE2Octets +
                    mRouterNet_ThisRouterE3Octets +
                    mRouterNet_ThisRouterV1Octets +
                    mRouterNet_ThisRouterV2Octets +
                    mRouterNet_ThisRouterB0Octets
            );
            if(mRouterNet_LastRouterE0Octets <= mRouterNet_ThisRouterE0Octets && mRouterNet_LastRouterE0Octets != 0) {
                mRouterNet_ThisDiffRouterE0Octets = ((mRouterNet_ThisRouterE0Octets - mRouterNet_LastRouterE0Octets)/1024/intLen/step);
            }
            if(mRouterNet_LastRouterE1Octets <= mRouterNet_LastRouterE1Octets && mRouterNet_LastRouterE1Octets != 0) {
                mRouterNet_ThisDiffRouterE1Octets = ((mRouterNet_ThisRouterE1Octets - mRouterNet_LastRouterE1Octets)/1024/intLen/step);
            }
            if(mRouterNet_LastRouterE2Octets <= mRouterNet_LastRouterE2Octets && mRouterNet_LastRouterE2Octets != 0) {
                mRouterNet_ThisDiffRouterE2Octets = ((mRouterNet_ThisRouterE2Octets - mRouterNet_LastRouterE2Octets)/1024/intLen/step);
            }
            if(mRouterNet_LastRouterE3Octets <= mRouterNet_LastRouterE3Octets && mRouterNet_LastRouterE3Octets != 0) {
                mRouterNet_ThisDiffRouterE3Octets = ((mRouterNet_ThisRouterE3Octets - mRouterNet_LastRouterE3Octets)/1024/intLen/step);
            }
            if(mRouterNet_LastRouterV1Octets <= mRouterNet_LastRouterV1Octets && mRouterNet_LastRouterV1Octets != 0) {
                mRouterNet_ThisDiffRouterV1Octets = ((mRouterNet_ThisRouterV1Octets - mRouterNet_LastRouterV1Octets)/1024/intLen/step);
            }
            if(mRouterNet_LastRouterV2Octets <= mRouterNet_LastRouterV2Octets && mRouterNet_LastRouterV2Octets != 0) {
                mRouterNet_ThisDiffRouterV2Octets = ((mRouterNet_ThisRouterV2Octets - mRouterNet_LastRouterV2Octets)/1024/intLen/step);
            }
            if(mRouterNet_LastRouterB0Octets <= mRouterNet_LastRouterB0Octets && mRouterNet_LastRouterB0Octets != 0) {
                mRouterNet_ThisDiffRouterB0Octets = ((mRouterNet_ThisRouterB0Octets - mRouterNet_LastRouterB0Octets)/1024/intLen/step);
            }
            mRouterNet_Labels.put(routerObject.getString("WalkTime"));
            mRouterNet_Data.put(mRouterNet_ThisDiffRouterE0Octets);
            mRouterNet_Data2.put(mRouterNet_ThisDiffRouterE1Octets);
            mRouterNet_Data3.put(mRouterNet_ThisDiffRouterE2Octets);
            mRouterNet_Data4.put(mRouterNet_ThisDiffRouterE3Octets);
            mRouterNet_Data5.put(mRouterNet_ThisDiffRouterV1Octets);
            mRouterNet_Data6.put(mRouterNet_ThisDiffRouterV2Octets);
            mRouterNet_Data7.put(mRouterNet_ThisDiffRouterB0Octets);
            mRouterNet_LastRouterE0Octets = mRouterNet_ThisRouterE0Octets;
            mRouterNet_LastRouterE1Octets = mRouterNet_ThisRouterE1Octets;
            mRouterNet_LastRouterE2Octets = mRouterNet_ThisRouterE2Octets;
            mRouterNet_LastRouterE3Octets = mRouterNet_ThisRouterE3Octets;
            mRouterNet_LastRouterV1Octets = mRouterNet_ThisRouterV1Octets;
            mRouterNet_LastRouterV2Octets = mRouterNet_ThisRouterV2Octets;
            mRouterNet_LastRouterB0Octets = mRouterNet_ThisRouterB0Octets;
            mRouterNet_LastOctetsTotal = mRouterNet_ThisOctetsTotal;
        }
        mRouterNet_Glob
                .put("labels", mRouterNet_Labels)
                .put("data", mRouterNet_Data)
                .put("data2", mRouterNet_Data2)
                .put("data3", mRouterNet_Data3)
                .put("data4", mRouterNet_Data4)
                .put("data5", mRouterNet_Data5)
                .put("data6", mRouterNet_Data6)
                .put("data7", mRouterNet_Data7)
                .put("props", mRouterNet_Props);
        return mRouterNet_Glob;
    }
    
    public JSONObject getRouterCPU(JSONArray dataIn, int intLen, int step) { return mRouterCPU(dataIn, intLen, step); }
    public JSONObject getRouterMemory(JSONArray dataIn, int intLen, int step) { return mRouterMemory(dataIn, intLen, step); }
    public JSONObject getRouterNet(JSONArray dataIn, int intLen, int step) { return mRouterNet(dataIn, intLen, step); }
    
}
