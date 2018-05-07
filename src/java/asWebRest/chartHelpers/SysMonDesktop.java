/*
by Anthony Stump
Base code created: 30 Mar 2018
Split off: 7 May 2018
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class SysMonDesktop {
    
    private WebCommon wc = new WebCommon();
    
    private JSONObject mJavaCodeLines(JSONArray dataIn, int intLen, int step) {
        String mJavaCodeLines_ChartName = "Lines of Code - Java Projects";
        JSONObject mJavaCodeLines_Glob = new JSONObject();
        JSONObject mJavaCodeLines_Props = new JSONObject();
        JSONArray mJavaCodeLines_Labels = new JSONArray();
        JSONArray mJavaCodeLines_Data = new JSONArray();
        JSONArray mJavaCodeLines_Data2 = new JSONArray();
        mJavaCodeLines_Props
                .put("chartName", mJavaCodeLines_ChartName).put("chartFileName", "mJavaCodeLines")
                .put("sName", "asUtils/java").put("sColor", "Blue")
                .put("s2Name", "asWeb/TOTAL").put("s2Color", "Red")
                .put("xLabel", "WalkTime").put("yLabel", "Lines");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject thisExpanded = thisObject.getJSONObject("dtExpandedJSONData");
            long mJavaCodeLines_asWebTotal = (
                    thisExpanded.getLong("LOC_aswjJs") +
                    thisExpanded.getLong("LOC_aswjJava") +
                    thisExpanded.getLong("LOC_aswjCss") +
                    thisExpanded.getLong("LOC_aswjJsp")
            );
            mJavaCodeLines_Labels.put(thisObject.getString("WalkTime"));
            mJavaCodeLines_Data.put(thisExpanded.getLong("LOC_asUtilsJava"));
            mJavaCodeLines_Data2.put(mJavaCodeLines_asWebTotal);
        }
        mJavaCodeLines_Glob
                .put("labels", mJavaCodeLines_Labels)
                .put("data", mJavaCodeLines_Data)
                .put("data2", mJavaCodeLines_Data2)
                .put("props", mJavaCodeLines_Props);
        return mJavaCodeLines_Glob;
    }
    
    private JSONObject mSysCams(JSONArray dataIn, int intLen, int step) {
        long mSysCams_LastTotal = 0;
        long mSysCams_Last1 = 0;
        long mSysCams_Last2 = 0;
        long mSysCams_Last3 = 0;
        long mSysCams_Last4 = 0;
        String mSysCams_ChartName = "IP Camera Availability";
        JSONObject mSysCams_Glob = new JSONObject();
        JSONObject mSysCams_Props = new JSONObject();
        JSONArray mSysCams_Labels = new JSONArray();
        JSONArray mSysCams_Data = new JSONArray();
        JSONArray mSysCams_Data2 = new JSONArray();
        JSONArray mSysCams_Data3 = new JSONArray();
        JSONArray mSysCams_Data4 = new JSONArray();
        mSysCams_Props
                .put("chartName", mSysCams_ChartName).put("chartFileName", "mSysCams")
                .put("sName", "Cam 1 (USB)").put("sColor", "Yellow")
                .put("s2Name", "Cam 2 (Back)").put("s2Color", "Blue")
                .put("s3Name", "Cam 3 (Drive)").put("s3Color", "Green")
                .put("s4Name", "Cam 4 (Pi)").put("s4Color", "Orange")
                .put("xLabel", "WalkTime").put("yLabel", "Percent Available");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            long mSysCams_ThisTotal = 0;
            long mSysCams_This1 = 0;
            long mSysCams_This2 = 0;
            long mSysCams_This3 = 0;
            long mSysCams_This4 = 0;
            float mSysCams_Cam1Available = 0.0f;
            float mSysCams_Cam2Available = 0.0f;
            float mSysCams_Cam3Available = 0.0f;
            float mSysCams_Cam4Available = 0.0f;
            if(thisObject.getLong("dtLogSecCam1Down") > 0) { mSysCams_This1 = thisObject.getLong("dtLogSecCam1Down"); }
            if(thisObject.getLong("dtLogSecCam2Down") > 0) { mSysCams_This2 = thisObject.getLong("dtLogSecCam2Down"); }
            if(thisObject.getLong("dtLogSecCam3Down") > 0) { mSysCams_This3 = thisObject.getLong("dtLogSecCam3Down"); }
            if(thisObject.getLong("dtLogSecCam4Down") > 0) { mSysCams_This4 = thisObject.getLong("dtLogSecCam4Down"); }
            mSysCams_Labels.put(thisObject.getString("WalkTime"));
            mSysCams_ThisTotal = ( mSysCams_This1 + mSysCams_This2 + mSysCams_This3 + mSysCams_This4);
            if(mSysCams_LastTotal <= mSysCams_ThisTotal && mSysCams_LastTotal != 0) {
                mSysCams_Cam1Available = 100 - (((mSysCams_This1 - mSysCams_Last1)/intLen)*100/step);
                mSysCams_Cam2Available = 100 - (((mSysCams_This2 - mSysCams_Last2)/intLen)*100/step);
                mSysCams_Cam3Available = 100 - (((mSysCams_This3 - mSysCams_Last3)/intLen)*100/step);
                mSysCams_Cam4Available = 100 - (((mSysCams_This4 - mSysCams_Last4)/intLen)*100/step);
            }
            if(mSysCams_Cam1Available < 0) { mSysCams_Cam1Available = 0; }
            if(mSysCams_Cam2Available < 0) { mSysCams_Cam2Available = 0; }
            if(mSysCams_Cam3Available < 0) { mSysCams_Cam3Available = 0; }
            if(mSysCams_Cam4Available < 0) { mSysCams_Cam4Available = 0; }
            mSysCams_Data.put(mSysCams_Cam1Available);
            mSysCams_Data2.put(mSysCams_Cam2Available);
            mSysCams_Data3.put(mSysCams_Cam3Available);
            mSysCams_Data4.put(mSysCams_Cam4Available);
            mSysCams_LastTotal = mSysCams_ThisTotal;
            mSysCams_Last1 = mSysCams_This1;
            mSysCams_Last2 = mSysCams_This2;
            mSysCams_Last3 = mSysCams_This3;
            mSysCams_Last4 = mSysCams_This4;
        }
        mSysCams_Glob
                .put("labels", mSysCams_Labels)
                .put("data", mSysCams_Data)
                .put("data2", mSysCams_Data2)
                .put("data3", mSysCams_Data3)
                .put("data4", mSysCams_Data4)
                .put("props", mSysCams_Props);
        return mSysCams_Glob;
    }
       
    private JSONObject mSysCPU(JSONArray dataIn, int intLen, int step) {
        String mSysCPU_ChartName = "Desktop: CPU Load";
        JSONObject mSysCPU_Glob = new JSONObject();
        JSONObject mSysCPU_Props = new JSONObject();
        JSONArray mSysCPU_Labels = new JSONArray();
        JSONArray mSysCPU_Data = new JSONArray();
        JSONArray mSysCPU_Data2 = new JSONArray();
        JSONArray mSysCPU_Data3 = new JSONArray();
        JSONArray mSysCPU_Data4 = new JSONArray();
        JSONArray mSysCPU_Data5 = new JSONArray();
        JSONArray mSysCPU_Data6 = new JSONArray();
        JSONArray mSysCPU_Data7 = new JSONArray();
        JSONArray mSysCPU_Data8 = new JSONArray();
        JSONArray mSysCPU_Data9 = new JSONArray();
        mSysCPU_Props
                .put("chartName", mSysCPU_ChartName).put("chartFileName", "mSysCPU")
                .put("sName", "Avg CPU").put("sColor", "Yellow")
                .put("s2Name", "Core 1").put("s2Color", "Gray")
                .put("s3Name", "Core 2").put("s3Color", "Gray")
                .put("s4Name", "Core 3").put("s4Color", "Gray")
                .put("s5Name", "Core 4").put("s5Color", "Gray")
                .put("s6Name", "Core 5").put("s6Color", "Gray")
                .put("s7Name", "Core 6").put("s7Color", "Gray")
                .put("s8Name", "Core 7").put("s8Color", "Gray")
                .put("s9Name", "Core 8").put("s9Color", "Gray")
                .put("xLabel", "WalkTime").put("yLabel", "% Use");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            float mSysCPU_LoadAverage = 0.00f;
            try {
                mSysCPU_LoadAverage = (
                        (thisObject.getInt("dtCPULoad1") +
                        thisObject.getInt("dtCPULoad2") +
                        thisObject.getInt("dtCPULoad3") +
                        thisObject.getInt("dtCPULoad4") +
                        thisObject.getInt("dtCPULoad5") +
                        thisObject.getInt("dtCPULoad6") +
                        thisObject.getInt("dtCPULoad7") +
                        thisObject.getInt("dtCPULoad8")) / 8
                );
            } catch (Exception e) { e.printStackTrace(); }
            mSysCPU_Labels.put(thisObject.getString("WalkTime"));
            mSysCPU_Data.put(mSysCPU_LoadAverage);
            mSysCPU_Data2.put(thisObject.getInt("dtCPULoad1"));
            mSysCPU_Data3.put(thisObject.getInt("dtCPULoad2"));
            mSysCPU_Data4.put(thisObject.getInt("dtCPULoad3"));
            mSysCPU_Data5.put(thisObject.getInt("dtCPULoad4"));
            mSysCPU_Data6.put(thisObject.getInt("dtCPULoad5"));
            mSysCPU_Data7.put(thisObject.getInt("dtCPULoad6"));
            mSysCPU_Data8.put(thisObject.getInt("dtCPULoad7"));
            mSysCPU_Data9.put(thisObject.getInt("dtCPULoad8"));
        }
        mSysCPU_Glob
                .put("labels", mSysCPU_Labels)
                .put("data", mSysCPU_Data)
                .put("data2", mSysCPU_Data2)
                .put("data3", mSysCPU_Data3)
                .put("data4", mSysCPU_Data4)
                .put("data5", mSysCPU_Data5)
                .put("data6", mSysCPU_Data6)
                .put("data7", mSysCPU_Data7)
                .put("data8", mSysCPU_Data8)
                .put("data9", mSysCPU_Data9)
                .put("props", mSysCPU_Props);
        return mSysCPU_Glob;
    }

    private JSONObject mSysDiskIO(JSONArray dataIn, int intLen, int step) {
        String mSysDiskIO_ChartName = "Desktop: Disk I/O Use";
        long mSysDiskIO_Cumulative = 0;
        long mSysDiskIO_LastTotal = 0;
        long mSysDiskIO_LastRead = 0;
        long mSysDiskIO_LastWrite = 0;
        JSONObject mSysDiskIO_Glob = new JSONObject();
        JSONObject mSysDiskIO_Props = new JSONObject();
        JSONArray mSysDiskIO_Labels = new JSONArray();
        JSONArray mSysDiskIO_Data = new JSONArray();
        JSONArray mSysDiskIO_Data2 = new JSONArray();
        JSONArray mSysDiskIO_Data3 = new JSONArray();
        mSysDiskIO_Props
                .put("chartName", mSysDiskIO_ChartName).put("chartFileName", "mSysDiskIO")
                .put("sName", "Total").put("sColor", "White")
                .put("s2Name", "Reads").put("s2Color", "Green")
                .put("s3Name", "Writes").put("s3Color", "Red")
                .put("xLabel", "WalkTime").put("yLabel", "x1000 Blocks");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            long mSysDiskIO_ThisRead = 0;
            long mSysDiskIO_ThisWrite = 0;
            if(thisObject.getLong("dtDiskIOSysRead") != 0) { mSysDiskIO_ThisRead = thisObject.getLong("dtDiskIOSysRead"); }
            if(thisObject.getLong("dtDiskIOSysWrite") != 0) { mSysDiskIO_ThisWrite = thisObject.getLong("dtDiskIOSysWrite"); }
            long mSysDiskIO_ThisTotal = mSysDiskIO_ThisRead + mSysDiskIO_ThisWrite;
            if(mSysDiskIO_LastTotal <= mSysDiskIO_ThisTotal && mSysDiskIO_LastTotal != 0) {
                if(mSysDiskIO_Cumulative != 0) { mSysDiskIO_Cumulative = mSysDiskIO_Cumulative + (mSysDiskIO_ThisTotal - mSysDiskIO_LastTotal); }
                mSysDiskIO_Data.put((mSysDiskIO_ThisTotal - mSysDiskIO_LastTotal)/1000/intLen/step);
                if(mSysDiskIO_LastRead <= mSysDiskIO_ThisRead) { mSysDiskIO_Data2.put((mSysDiskIO_ThisRead - mSysDiskIO_LastRead)/1000/intLen/step); }
                if(mSysDiskIO_LastWrite <= mSysDiskIO_ThisWrite) { mSysDiskIO_Data3.put((mSysDiskIO_ThisWrite - mSysDiskIO_LastWrite)/1000/intLen/step); }
            }
            mSysDiskIO_LastTotal = mSysDiskIO_ThisTotal;
            mSysDiskIO_LastRead = mSysDiskIO_ThisRead;
            mSysDiskIO_LastWrite = mSysDiskIO_ThisWrite;
        }
        mSysDiskIO_Glob
                .put("labels", mSysDiskIO_Labels)
                .put("data", mSysDiskIO_Data)
                .put("data2", mSysDiskIO_Data2)
                .put("data3", mSysDiskIO_Data3)
                .put("props", mSysDiskIO_Props);
        return mSysDiskIO_Glob;
    }
                    
    private JSONObject mSysFans(JSONArray dataIn, int intLen, int step) {
        String mSysFans_ChartName = "Desktop: Fan Speeds";
        JSONObject mSysFans_Glob = new JSONObject();
        JSONObject mSysFans_Props = new JSONObject();
        JSONArray mSysFans_Labels = new JSONArray();
        JSONArray mSysFans_Data = new JSONArray();
        JSONArray mSysFans_Data2 = new JSONArray();
        JSONArray mSysFans_Data3 = new JSONArray();
        mSysFans_Props
                .put("chartName", mSysFans_ChartName).put("chartFileName", "mSysFans")
                .put("sName", "Fan 1").put("sColor", "Red")
                .put("s2Name", "Fan 2").put("s2Color", "Blue")
                .put("s3Name", "Fan 3").put("s3Color", "Green")
                .put("xLabel", "WalkTime").put("yLabel", "RPM");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysFans_Labels.put(thisObject.getString("WalkTime"));
            mSysFans_Data.put(thisObject.getInt("dtFan1"));
            mSysFans_Data2.put(thisObject.getInt("dtFan2"));
            mSysFans_Data3.put(thisObject.getInt("dtFan3"));
        }
        mSysFans_Glob
                .put("labels", mSysFans_Labels)
                .put("data", mSysFans_Data)
                .put("data2", mSysFans_Data2)
                .put("data3", mSysFans_Data3)
                .put("props", mSysFans_Props);
        return mSysFans_Glob;
    }
                   
    private JSONObject mSysLoad(JSONArray dataIn, int intLen, int step) {
        String mSysLoad_ChartName = "Desktop: System Load";
        JSONObject mSysLoad_Glob = new JSONObject();
        JSONObject mSysLoad_Props = new JSONObject();
        JSONArray mSysLoad_Labels = new JSONArray();
        JSONArray mSysLoad_Data = new JSONArray();
        JSONArray mSysLoad_Data2 = new JSONArray();
        JSONArray mSysLoad_Data3 = new JSONArray();           
        mSysLoad_Props
                .put("chartName", mSysLoad_ChartName).put("chartFileName", "mSysLoad")
                .put("sName", "LoadIndex").put("sColor", "Red")
                .put("s2Name", "Load 5").put("s2Color", "Green")
                .put("s3Name", "Load 15").put("s3Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Load Index");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysLoad_Labels.put(thisObject.getString("WalkTime"));
            mSysLoad_Data.put(thisObject.getDouble("dtLoadIndex1"));
            mSysLoad_Data2.put(thisObject.getDouble("dtLoadIndex5"));
            mSysLoad_Data3.put(thisObject.getDouble("dtLoadIndex15"));
        }
        mSysLoad_Glob
                .put("labels", mSysLoad_Labels)
                .put("data", mSysLoad_Data)
                .put("data2", mSysLoad_Data2)
                .put("data3", mSysLoad_Data3)
                .put("props", mSysLoad_Props);
        return mSysLoad_Glob;
    }
     
    private JSONObject mSysMemory(JSONArray dataIn, int intLen, int step) {
        String mSysMemory_ChartName = "Desktop: Memory Use";
        JSONObject mSysMemory_Glob = new JSONObject();
        JSONObject mSysMemory_Props = new JSONObject();
        JSONArray mSysMemory_Labels = new JSONArray();
        JSONArray mSysMemory_Data = new JSONArray();
        JSONArray mSysMemory_Data2 = new JSONArray();
        JSONArray mSysMemory_Data3 = new JSONArray();
        JSONArray mSysMemory_Data4 = new JSONArray();
        mSysMemory_Props
                .put("chartName", mSysMemory_ChartName).put("chartFileName", "mSysMemory")
                .put("sName", "Used").put("sColor", "Yellow")
                .put("s2Name", "Swap").put("s2Color", "Red")
                .put("s3Name", "Buffers").put("s3Color", "Green")
                .put("s4Name", "Cached").put("s4Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Use in MBs");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            float mSysMemory_Used = 0.00f;
            try { mSysMemory_Used = ((thisObject.getLong("dtKMemPhysU")-(thisObject.getLong("dtKMemBuffU")+thisObject.getLong("dtKMemCachedU")))/1024); } catch (Exception e) { e.printStackTrace(); }
            mSysMemory_Labels.put(thisObject.getString("WalkTime"));
            mSysMemory_Data.put(mSysMemory_Used);
            mSysMemory_Data2.put(thisObject.getLong("dtKSwapU")/1024);
            mSysMemory_Data3.put(thisObject.getLong("dtKMemBuffU")/1024);
            mSysMemory_Data4.put(thisObject.getLong("dtKMemCachedU")/1024);
        }
        mSysMemory_Glob
                .put("labels", mSysMemory_Labels)
                .put("data", mSysMemory_Data)
                .put("data2", mSysMemory_Data2)
                .put("data3", mSysMemory_Data3)
                .put("data4", mSysMemory_Data4)
                .put("props", mSysMemory_Props);
        return mSysMemory_Glob;
    }
                    
    private JSONObject mSysMySQLSize(JSONArray dataIn, int intLen, int step) {
        String mSysMySQLSize_ChartName = "Desktop: MySQL Database Sizes";
        JSONObject mSysMySQLSize_Glob = new JSONObject();
        JSONObject mSysMySQLSize_Props = new JSONObject();
        JSONArray mSysMySQLSize_Labels = new JSONArray();
        JSONArray mSysMySQLSize_Data = new JSONArray();
        JSONArray mSysMySQLSize_Data2 = new JSONArray();
        JSONArray mSysMySQLSize_Data3 = new JSONArray();
        JSONArray mSysMySQLSize_Data4 = new JSONArray();
        JSONArray mSysMySQLSize_Data5 = new JSONArray();
        JSONArray mSysMySQLSize_Data6 = new JSONArray();
        mSysMySQLSize_Props
                .put("chartName", mSysMySQLSize_ChartName).put("chartFileName", "mSysMySQLSize")
                .put("sName", "Rows").put("sColor", "Blue")
                .put("s2Name", "Size:Core").put("s2Color", "Red")
                .put("s3Name", "Size:Feeds").put("s3Color", "Orange")
                .put("s4Name", "Size:NetSNMP").put("s4Color", "Yellow")
                .put("s5Name", "Size:WxObs").put("s5Color", "Green")
                .put("s6Name", "Size:TOTAL").put("s6Color", "White")
                .put("xLabel", "WalkTime").put("yLabel", "Size Mbit");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            long mSysMySQLSize_TotalRows = ((
                    thisObject.getLong("dtMySQLRowsCore") +
                    thisObject.getLong("dtMySQLRowsFeeds") +
                    thisObject.getLong("dtMySQLRowsWebCal") +
                    thisObject.getLong("dtMySQLRowsNetSNMP") +
                    thisObject.getLong("dtMySQLRowsWxObs"))
                    / 1000
            );
            mSysMySQLSize_Labels.put(thisObject.getString("WalkTime"));
            mSysMySQLSize_Data.put(mSysMySQLSize_TotalRows);
            mSysMySQLSize_Data2.put(thisObject.getLong("duMySQLCore")/100);
            mSysMySQLSize_Data3.put(thisObject.getLong("duMySQLFeeds")/100);
            mSysMySQLSize_Data4.put(thisObject.getLong("duMySQLNetSNMP")/100);
            mSysMySQLSize_Data5.put(thisObject.getLong("duMySQLWxObs")/100);
            mSysMySQLSize_Data6.put(thisObject.getLong("duMySQLTotal")/100);
        }
        mSysMySQLSize_Glob
                .put("labels", mSysMySQLSize_Labels)
                .put("data", mSysMySQLSize_Data)
                .put("data2", mSysMySQLSize_Data2)
                .put("data3", mSysMySQLSize_Data3)
                .put("data4", mSysMySQLSize_Data4)
                .put("data5", mSysMySQLSize_Data5)
                .put("data6", mSysMySQLSize_Data6)
                .put("props", mSysMySQLSize_Props);
        return mSysMySQLSize_Glob;
    }
     
    private JSONObject mSysNet(JSONArray dataIn, int intLen, int step) {
        String returnData = "";
        String mSysNet_ChartName = "Desktop: Network Use";
        long mSysNet_Cumulative = 0;
        long mSysNet_LastOctetsTotal = 0;
        long mSysNet_LastOctetsIn = 0;
        long mSysNet_LastOctetsOut = 0;
        long mSysNet_LastLapOctets = 0;
        long mSysNet_LastVirtualOctets = 0;
        long mSysNet_LastPiOctets = 0;
        long mSysNet_LastPi2Octets = 0;
        JSONObject mSysNet_Glob = new JSONObject();
        JSONObject mSysNet_Props = new JSONObject();
        JSONArray mSysNet_Labels = new JSONArray();
        JSONArray mSysNet_Data = new JSONArray();
        JSONArray mSysNet_Data2 = new JSONArray();
        JSONArray mSysNet_Data3 = new JSONArray();
        JSONArray mSysNet_Data4 = new JSONArray();
        JSONArray mSysNet_Data5 = new JSONArray();
        JSONArray mSysNet_Data6 = new JSONArray();
        JSONArray mSysNet_Data7 = new JSONArray();
        JSONArray mSysNet_Data8 = new JSONArray();
        mSysNet_Props
                .put("chartName", mSysNet_ChartName).put("chartFileName", "mSysNet")
                .put("sName", "Total").put("sColor", "White")
                .put("s2Name", "Router").put("s2Color", "Yellow")
                .put("s3Name", "Desktop RX").put("s3Color", "Green")
                .put("s4Name", "Desktop TX").put("s4Color", "Red")
                .put("s5Name", "Laptop").put("s5Color", "Gray")
                .put("s6Name", "VMs").put("s6Color", "Gray")
                .put("s7Name", "Pi").put("s7Color", "Gray")
                .put("s8Name", "Pi 2").put("s8Color", "Gray")
                .put("xLabel", "WalkTime").put("yLabel", "Mbps");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            long mSysNet_ThisLapOctets = thisObject.getLong("lapOctetsIn") + thisObject.getLong("lapOctetsOut");
            long mSysNet_ThisPiOctets = thisObject.getLong("piOctetsIn") + thisObject.getLong("piOctetsOut");
            long mSysNet_ThisPi2Octets = thisObject.getLong("pi2OctetsIn") + thisObject.getLong("pi2OctetsOut");
            long mSysNet_ThisVirtualOctets = (
                    thisObject.getLong("cmvmOctetsIn") +
                    thisObject.getLong("cmvmOctetsOut") +
                    thisObject.getLong("uvmOctetsIn") +
                    thisObject.getLong("uvmOctetsOut") +
                    thisObject.getLong("w12OctetsIn") +
                    thisObject.getLong("w12OctetsOut") +
                    thisObject.getLong("w16OctetsIn") +
                    thisObject.getLong("w16OctetsOut") +
                    thisObject.getLong("wxpOctetsIn") +
                    thisObject.getLong("wxpOctetsOut")
            );
            long mSysNet_ThisOctetsTotal = thisObject.getLong("dtOctetsIn") + thisObject.getLong("dtOctetsOut");
            if(mSysNet_LastOctetsTotal <= mSysNet_ThisOctetsTotal && mSysNet_LastOctetsTotal != 0) {
                mSysNet_Labels.put(thisObject.getString("WalkTime"));
                mSysNet_Cumulative = mSysNet_Cumulative + (mSysNet_ThisOctetsTotal - mSysNet_LastOctetsTotal);
                mSysNet_Data.put((mSysNet_ThisOctetsTotal - mSysNet_LastOctetsTotal)/1024/1024/intLen/step);
                mSysNet_Data2.put((thisObject.getLong("rtrWANtx") + thisObject.getLong("rtrWANrx"))/1024/1024/intLen/step);
                if(mSysNet_LastOctetsIn <= thisObject.getLong("dtOctetsIn") && mSysNet_LastOctetsIn != 0) { mSysNet_Data3.put((thisObject.getLong("dtOctetsIn") - mSysNet_LastOctetsIn)/1024/1024/intLen/step); }
                if(mSysNet_LastOctetsOut <= thisObject.getLong("dtOctetsOut") && mSysNet_LastOctetsOut != 0) { mSysNet_Data4.put((thisObject.getLong("dtOctetsOut") - mSysNet_LastOctetsOut)/1024/1024/intLen/step); }
                if(mSysNet_LastLapOctets <= mSysNet_ThisLapOctets && mSysNet_LastLapOctets != 0) { mSysNet_Data5.put((mSysNet_ThisLapOctets - mSysNet_LastLapOctets)/1024/1024/intLen/step); }
                if(mSysNet_LastVirtualOctets <= mSysNet_ThisVirtualOctets && mSysNet_LastVirtualOctets != 0) { mSysNet_Data6.put((mSysNet_ThisVirtualOctets - mSysNet_LastVirtualOctets)/1024/1024/intLen/step); }
                if(mSysNet_LastPiOctets <= mSysNet_ThisPiOctets && mSysNet_LastPiOctets != 0) { mSysNet_Data7.put((mSysNet_ThisPiOctets - mSysNet_LastPiOctets)/1024/1024/intLen/step); }
                if(mSysNet_LastPi2Octets <= mSysNet_ThisPi2Octets && mSysNet_LastPi2Octets != 0) { mSysNet_Data8.put((mSysNet_ThisPiOctets - mSysNet_LastPiOctets)/1024/1024/intLen/step); }
            } else {
                returnData += "No data! - " + thisObject.getString("WalkTime");
            }
            mSysNet_LastOctetsTotal = mSysNet_ThisOctetsTotal;
            mSysNet_LastOctetsIn = thisObject.getLong("dtOctetsIn");
            mSysNet_LastOctetsOut = thisObject.getLong("dtOctetsOut");
            mSysNet_LastLapOctets = mSysNet_ThisLapOctets;
            mSysNet_LastVirtualOctets = mSysNet_ThisVirtualOctets;
            mSysNet_LastPiOctets = mSysNet_ThisPiOctets;
            mSysNet_LastPi2Octets = mSysNet_ThisPi2Octets;
        }
        mSysNet_Glob
                .put("labels", mSysNet_Labels)
                .put("data", mSysNet_Data)
                .put("data2", mSysNet_Data2)
                .put("data3", mSysNet_Data3)
                .put("data4", mSysNet_Data4)
                .put("data5", mSysNet_Data5)
                .put("data6", mSysNet_Data6)
                .put("data7", mSysNet_Data7)
                .put("data8", mSysNet_Data8)
                .put("props", mSysNet_Props);
        return mSysNet_Glob;
    }
     
    private JSONObject mSysNumUsers(JSONArray dataIn, int intLen, int step) {
        String mSysNumUsers_ChartName = "Desktop: Users/Processes";
        JSONObject mSysNumUsers_Glob = new JSONObject();
        JSONObject mSysNumUsers_Props = new JSONObject();
        JSONArray mSysNumUsers_Labels = new JSONArray();
        JSONArray mSysNumUsers_Data = new JSONArray();
        JSONArray mSysNumUsers_Data2 = new JSONArray();
        JSONArray mSysNumUsers_Data3 = new JSONArray();
        JSONArray mSysNumUsers_Data4 = new JSONArray();
        JSONArray mSysNumUsers_Data5 = new JSONArray();
        JSONArray mSysNumUsers_Data6 = new JSONArray();
        JSONArray mSysNumUsers_Data7 = new JSONArray();
        mSysNumUsers_Props
                .put("chartName", mSysNumUsers_ChartName).put("chartFileName", "mSysNumUsers")
                .put("sName", "All Sessions").put("sColor", "White")
                .put("s2Name", "Desktop Users").put("s2Color", "Red")
                .put("s3Name", "SSH Desktop Users").put("s3Color", "Orange")
                .put("s4Name", "Laptop Users").put("s4Color", "Blue")
                .put("s5Name", "VM Desktop Users").put("s5Color", "Gray")
                .put("s6Name", "IPCon/10").put("s6Color", "Yellow")
                .put("s7Name", "Proc/100").put("s7Color", "Green")
                .put("xLabel", "WalkTime").put("yLabel", "Count");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            int mSysNumUsers_VmUsers = (
                    thisObject.getInt("wxpNumUsers") +
                    thisObject.getInt("w12NumUsers") +
                    thisObject.getInt("w16NumUsers") +
                    thisObject.getInt("uvmNumUsers") +
                    thisObject.getInt("cmvmNumUsers")
            );
            int mSysNumUsers_TotalSessions = (
                    thisObject.getInt("dtNumUsers") +
                    thisObject.getInt("lapNumUsers") +
                    mSysNumUsers_VmUsers
            );
            mSysNumUsers_Labels.put(thisObject.getString("WalkTime"));
            mSysNumUsers_Data.put(mSysNumUsers_TotalSessions);
            mSysNumUsers_Data2.put(thisObject.getInt("dtNumUsers"));
            mSysNumUsers_Data3.put(thisObject.getInt("dtNS5ActiveSSH"));
            mSysNumUsers_Data4.put(thisObject.getInt("lapNumUsers"));
            mSysNumUsers_Data5.put(mSysNumUsers_VmUsers);
            mSysNumUsers_Data6.put((float) (thisObject.getInt("dtNS5Active")/10));
            mSysNumUsers_Data7.put((float) (thisObject.getInt("dtProcesses")/100));
        }
        mSysNumUsers_Glob
                .put("labels", mSysNumUsers_Labels)
                .put("data", mSysNumUsers_Data)
                .put("data2", mSysNumUsers_Data2)
                .put("data3", mSysNumUsers_Data3)
                .put("data4", mSysNumUsers_Data4)
                .put("data5", mSysNumUsers_Data5)
                .put("data6", mSysNumUsers_Data6)
                .put("data7", mSysNumUsers_Data7)
                .put("props", mSysNumUsers_Props);
        return mSysNumUsers_Glob;
    }

    private JSONObject mSysStorage(JSONArray dataIn, int intLen, int step) {
        String mSysStorage_ChartName = "Desktop: Storage";
        JSONObject mSysStorage_Glob = new JSONObject();
        JSONObject mSysStorage_Props = new JSONObject();
        JSONArray mSysStorage_Labels = new JSONArray();
        JSONArray mSysStorage_Data = new JSONArray();
        JSONArray mSysStorage_Data2 = new JSONArray();
        mSysStorage_Props
                .put("chartName", mSysStorage_ChartName).put("chartFileName", "mSysStorage")
                .put("sName", "sda1").put("sColor", "Red")
                .put("s2Name", "sdb1").put("s2Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "% Disk Use");    
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject thisExpanded = thisObject.getJSONObject("dtExpandedJSONData");
            double mSysStorage_sda1Used = 0.00;
            double mSysStorage_sdb1Used = 0.00;
            try { mSysStorage_sda1Used = (double) 100.0 * ((double) thisObject.getLong("dtK4RootU")/(double) thisObject.getLong("dtK4Root")); } catch (Exception e) { e.printStackTrace(); }
            try { mSysStorage_sdb1Used = (double) 100.0 * ((double) thisExpanded.getLong("k4Extra1U")/(double) thisExpanded.getLong("k4Extra1")); } catch (Exception e) { e.printStackTrace(); }
            mSysStorage_Labels.put(thisObject.getString("WalkTime"));
            mSysStorage_Data.put(mSysStorage_sda1Used);
            mSysStorage_Data2.put(mSysStorage_sdb1Used);
        } 
        mSysStorage_Glob
                .put("labels", mSysStorage_Labels)
                .put("data", mSysStorage_Data)
                .put("data2", mSysStorage_Data2)
                .put("props", mSysStorage_Props);
        return mSysStorage_Glob;
    }

    private JSONObject mSysTemp(JSONArray dataIn, int intLen, int step) {
        String mSysTemp_ChartName = "Desktop: Temperatures";
        JSONObject mSysTemp_Glob = new JSONObject();
        JSONObject mSysTemp_Props = new JSONObject();
        JSONArray mSysTemp_Labels = new JSONArray();
        JSONArray mSysTemp_Data = new JSONArray();
        JSONArray mSysTemp_Data2 = new JSONArray();
        JSONArray mSysTemp_Data3 = new JSONArray();
        JSONArray mSysTemp_Data4 = new JSONArray();
        JSONArray mSysTemp_Data5 = new JSONArray();
        JSONArray mSysTemp_Data6 = new JSONArray(); 
        mSysTemp_Props
                .put("chartName", mSysTemp_ChartName).put("chartFileName", "mSysTemp")
                .put("sName", "CPU Average").put("sColor", "Yellow")
                .put("s2Name", "Case").put("s2Color", "Green")
                .put("s3Name", "Core 1/2").put("s3Color", "Gray")
                .put("s4Name", "Core 3/4").put("s4Color", "Gray")
                .put("s5Name", "Core 5/6").put("s5Color", "Gray")
                .put("s6Name", "Core 7/8").put("s6Color", "Gray")
                .put("xLabel", "WalkTime").put("yLabel", "Temp F");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysTemp_Labels.put(thisObject.getString("WalkTime"));
            mSysTemp_Data.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCPU")/1000)));
            mSysTemp_Data2.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCase")/1000)));
            mSysTemp_Data3.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore1")/1000)));
            mSysTemp_Data4.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore2")/1000)));
            mSysTemp_Data5.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore3")/1000)));
            mSysTemp_Data6.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore4")/1000)));
        }
        mSysTemp_Glob
                .put("labels", mSysTemp_Labels)
                .put("data", mSysTemp_Data)
                .put("data2", mSysTemp_Data2)
                .put("data3", mSysTemp_Data3)
                .put("data4", mSysTemp_Data4)
                .put("data5", mSysTemp_Data5)
                .put("data6", mSysTemp_Data6)
                .put("props", mSysTemp_Props);
        return mSysTemp_Glob;
    }
        
    private JSONObject mSysTomcatDeploy(JSONArray dataIn, int intLen, int step) {
        String mSysTomcatDeploy_ChartName = "Desktop: Tomcat WAR Deployments";
        JSONObject mSysTomcatDeploy_Glob = new JSONObject();
        JSONObject mSysTomcatDeploy_Props = new JSONObject();
        JSONArray mSysTomcatDeploy_Labels = new JSONArray();
        JSONArray mSysTomcatDeploy_Data = new JSONArray();
        JSONArray mSysTomcatDeploy_Data2 = new JSONArray();
        mSysTomcatDeploy_Props
                .put("chartName", mSysTomcatDeploy_ChartName).put("chartFileName", "mSysTomcatDeploy")
                .put("sName", "WAR Files").put("sColor", "Green")
                .put("s2Name", "Deployed").put("s2Color", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "Files");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysTomcatDeploy_Labels.put(thisObject.getString("WalkTime"));
            mSysTomcatDeploy_Data.put(thisObject.getInt("dtTomcatWars"));
            mSysTomcatDeploy_Data2.put(thisObject.getInt("dtTomcatDeploy"));
        }
        mSysTomcatDeploy_Glob
                .put("labels", mSysTomcatDeploy_Labels)
                .put("data", mSysTomcatDeploy_Data)
                .put("data2", mSysTomcatDeploy_Data2)
                .put("props", mSysTomcatDeploy_Props);
        return mSysTomcatDeploy_Glob;
    }
     
    private JSONObject mSysUPSLoad(JSONArray dataIn, int intLen, int step) {
        String mSysUPSLoad_ChartName = "UPS: Load";
        JSONObject mSysUPSLoad_Glob = new JSONObject();
        JSONObject mSysUPSLoad_Props = new JSONObject();
        JSONArray mSysUPSLoad_Labels = new JSONArray();
        JSONArray mSysUPSLoad_Data = new JSONArray();
        mSysUPSLoad_Props
                .put("chartName", mSysUPSLoad_ChartName).put("chartFileName", "mSysUPSLoad")
                .put("sName", "UPS Load %").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysUPSLoad_Labels.put(thisObject.getString("WalkTime"));
            mSysUPSLoad_Data.put(thisObject.getDouble("dtUPSLoad"));
        }
        mSysUPSLoad_Glob
                .put("labels", mSysUPSLoad_Labels)
                .put("data", mSysUPSLoad_Data)
                .put("props", mSysUPSLoad_Props);
        return mSysUPSLoad_Glob;
    }
                    
    private JSONObject mSysUPSTimeLeft(JSONArray dataIn, int intLen, int step) {
        String mSysUPSTimeLeft_ChartName = "UPS: Time Left";
        JSONObject mSysUPSTimeLeft_Glob = new JSONObject();
        JSONObject mSysUPSTimeLeft_Props = new JSONObject();
        JSONArray mSysUPSTimeLeft_Labels = new JSONArray();
        JSONArray mSysUPSTimeLeft_Data = new JSONArray();
        mSysUPSTimeLeft_Props
                .put("chartName", mSysUPSTimeLeft_ChartName).put("chartFileName", "mSysUPSTimeLeft")
                .put("sName", "Time Left").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "Minutes");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysUPSTimeLeft_Labels.put(thisObject.getString("WalkTime"));
            mSysUPSTimeLeft_Data.put(thisObject.getDouble("dtUPSTimeLeft"));
        }
        mSysUPSTimeLeft_Glob
                .put("labels", mSysUPSTimeLeft_Labels)
                .put("data", mSysUPSTimeLeft_Data)
                .put("props", mSysUPSTimeLeft_Props);
        return mSysUPSTimeLeft_Glob;
    }
     
    private JSONObject mSysVolt(JSONArray dataIn, int intLen, int step) {
        String mSysVolt_ChartName = "Desktop: Voltages";
        JSONObject mSysVolt_Glob = new JSONObject();
        JSONObject mSysVolt_Props = new JSONObject();
        JSONArray mSysVolt_Labels = new JSONArray();
        JSONArray mSysVolt_Data = new JSONArray();
        JSONArray mSysVolt_Data2 = new JSONArray();
        JSONArray mSysVolt_Data3 = new JSONArray();
        JSONArray mSysVolt_Data4 = new JSONArray();
        JSONArray mSysVolt_Data5 = new JSONArray();
        JSONArray mSysVolt_Data6 = new JSONArray();
        JSONArray mSysVolt_Data7 = new JSONArray();
        JSONArray mSysVolt_Data8 = new JSONArray();
        JSONArray mSysVolt_Data9 = new JSONArray();
        mSysVolt_Props
                .put("chartName", mSysVolt_ChartName).put("chartFileName", "mSysVolt")
                .put("sName", "CPU").put("sColor", "Red")
                .put("s2Name", "Core 1").put("s2Color", "Gray")
                .put("s3Name", "Core 2").put("s3Color", "Gray")
                .put("s4Name", "Core 3").put("s4Color", "Gray")
                .put("s5Name", "Core 4").put("s5Color", "Gray")
                .put("s6Name", "+3.3V").put("s6Color", "Yellow")
                .put("s7Name", "+5V").put("s7Color", "Green")
                .put("s8Name", "+12V").put("s8Color", "Orange")
                .put("s9Name", "CMOS Battery").put("s9Color", "White")
                .put("xLabel", "WalkTime").put("yLabel", "Voltage");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysVolt_Labels.put(thisObject.getString("WalkTime"));
            mSysVolt_Data.put(thisObject.getDouble("dtVoltCPU"));
            mSysVolt_Data2.put(thisObject.getDouble("dtVoltCore1"));
            mSysVolt_Data3.put(thisObject.getDouble("dtVoltCore2"));
            mSysVolt_Data4.put(thisObject.getDouble("dtVoltCore3"));
            mSysVolt_Data5.put(thisObject.getDouble("dtVoltCore4"));
            mSysVolt_Data6.put(thisObject.getDouble("dtVoltPlus33"));
            mSysVolt_Data7.put(thisObject.getDouble("dtVoltPlus5"));
            mSysVolt_Data8.put(thisObject.getDouble("dtVoltPlus12"));
            mSysVolt_Data9.put(thisObject.getDouble("dtVoltBatt"));
        }
        mSysVolt_Glob
                .put("labels", mSysVolt_Labels)
                .put("data", mSysVolt_Data)
                .put("data2", mSysVolt_Data2)
                .put("data3", mSysVolt_Data3)
                .put("data4", mSysVolt_Data4)
                .put("data5", mSysVolt_Data5)
                .put("data6", mSysVolt_Data6)
                .put("data7", mSysVolt_Data7)
                .put("data8", mSysVolt_Data8)
                .put("data9", mSysVolt_Data9)
                .put("props", mSysVolt_Props);
        return mSysVolt_Glob;
    }
                    
    public JSONObject getJavaCodeLines(JSONArray dataIn, int intLen, int step) { return mJavaCodeLines(dataIn, intLen, step); }
    public JSONObject getSysCams(JSONArray dataIn, int intLen, int step) { return mSysCams(dataIn, intLen, step); }
    public JSONObject getSysCPU(JSONArray dataIn, int intLen, int step) { return mSysCPU(dataIn, intLen, step); }
    public JSONObject getSysDiskIO(JSONArray dataIn, int intLen, int step) { return mSysDiskIO(dataIn, intLen, step); }
    public JSONObject getSysFans(JSONArray dataIn, int intLen, int step) { return mSysFans(dataIn, intLen, step); }
    public JSONObject getSysLoad(JSONArray dataIn, int intLen, int step) { return mSysLoad(dataIn, intLen, step); }
    public JSONObject getSysMemory(JSONArray dataIn, int intLen, int step) { return mSysMemory(dataIn, intLen, step); }
    public JSONObject getSysMySQLSize(JSONArray dataIn, int intLen, int step) { return mSysMySQLSize(dataIn, intLen, step); }
    public JSONObject getSysNet(JSONArray dataIn, int intLen, int step) { return mSysNet(dataIn, intLen, step); }
    public JSONObject getSysNumUsers(JSONArray dataIn, int intLen, int step) { return mSysNumUsers(dataIn, intLen, step); }
    public JSONObject getSysStorage(JSONArray dataIn, int intLen, int step) { return mSysStorage(dataIn, intLen, step); }
    public JSONObject getSysTemp(JSONArray dataIn, int intLen, int step) { return mSysTemp(dataIn, intLen, step); }
    public JSONObject getSysTomcatDeploy(JSONArray dataIn, int intLen, int step) { return mSysTomcatDeploy(dataIn, intLen, step); }
    public JSONObject getSysUPSLoad(JSONArray dataIn, int intLen, int step) { return mSysUPSLoad(dataIn, intLen, step); }
    public JSONObject getSysUPSTimeLeft(JSONArray dataIn, int intLen, int step) { return mSysUPSTimeLeft(dataIn, intLen, step); }
    public JSONObject getSysVolt(JSONArray dataIn, int intLen, int step) { return mSysVolt(dataIn, intLen, step); }
    
}
