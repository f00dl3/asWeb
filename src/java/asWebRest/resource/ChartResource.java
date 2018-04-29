/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 29 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.action.GetSnmpAction;
import asWebRest.dao.FitnessDAO;
import asWebRest.dao.SnmpDAO;
import asWebRest.hookers.DynChartX;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ChartResource extends ServerResource {
    
    @Post    
    public String doPost(Representation argsIn) {

        WebCommon wc = new WebCommon();
        boolean genericCharts = false;
        
        DynChartX dynChart = new DynChartX();
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        String doWhat = null;
        String returnData = "";      
        
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        JSONObject mergedData = new JSONObject();
        JSONObject jsonGlob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        JSONArray data2 = new JSONArray();
        JSONArray data3 = new JSONArray();
        JSONArray data4 = new JSONArray();
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch (doWhat) {
                 
                case "FitnessCalorieRange":
                    genericCharts = true;
                    String fcnCalories = "Calorie Range: " + argsInForm.getFirstValue("XDT1") + " to " + argsInForm.getFirstValue("XDT2");
                    qParams.add(argsInForm.getFirstValue("XDT1"));
                    qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jraCalorieRange = getFitnessAction.getChCaloriesR(qParams);
                    for (int i = 0; i < jraCalorieRange.length(); i++) {
                        JSONObject thisObject = jraCalorieRange.getJSONObject(i);
                        labels.put(thisObject.getString("Date"));
                        data.put(thisObject.getInt("Calories"));
                        data2.put(9 * thisObject.getInt("Fat"));
                        data3.put(4 * thisObject.getInt("Protein"));
                        data4.put(4 * thisObject.getInt("Carbs"));
                    }
                    props
                        .put("chartName", fcnCalories).put("chartFileName", "CalorieRange")
                        .put("sName", "Calories").put("sColor", "White")
                        .put("s2Color", "Red").put("s2Name", "Fat")
                        .put("s3Color", "Green").put("s3Name", "Protein")
                        .put("s4Color", "Yellow").put("s4Name", "Carbs")
                        .put("xLabel", "Date").put("yLabel", "Calories");
                    break;
                                   
                case "FitnessWeightRange":
                    genericCharts = true;
                    String fullChartName = "Weight Range: " + argsInForm.getFirstValue("XDT1") + " to " + argsInForm.getFirstValue("XDT2");
                    qParams.add(argsInForm.getFirstValue("XDT1"));
                    qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jsonResultArray = getFitnessAction.getChWeightR(qParams);
                    for (int i = 0; i < jsonResultArray.length(); i++) {
                        JSONObject thisObject = jsonResultArray.getJSONObject(i);
                        labels.put(thisObject.getString("Date"));
                        data.put(thisObject.getDouble("Weight"));
                    }
                    props
                        .put("chartName", fullChartName).put("chartFileName", "WeightRange")
                        .put("sName", "Calories").put("sColor", "Yellow")
                        .put("xLabel", "Date").put("yLabel", "Weight");
                    break;
                    
                case "SysMonCharts":
                    
                    genericCharts = false;
                    
                    qParams.add(0, "1"); //Test
                    qParams.add(1, doWhat = argsInForm.getFirstValue("step"));; //Step
                    qParams.add(2, "1"); //DateTest
                    qParams.add(3, doWhat = argsInForm.getFirstValue("date")); //Date
                    
                    int step = Integer.valueOf(doWhat = argsInForm.getFirstValue("step"));
                    int intLen = 60 * 2;
                    
                    JSONArray mainGlob = getSnmpAction.getMain(dbc, qParams);
                    JSONArray note3Glob = getSnmpAction.getNote3(dbc, qParams);
                    JSONArray emS4Glob = getSnmpAction.getEmS4(dbc, qParams);
                    JSONArray piGlob = getSnmpAction.getPi(dbc, qParams);
                    JSONArray pi2Glob = getSnmpAction.getPi2(dbc, qParams);
                    JSONArray routerGlob = getSnmpAction.getRouter(dbc, qParams);
                    
                    String mJavaCodeLines_ChartName = "Lines of Code - Java Projects";
                    JSONObject mJavaCodeLines_Glob = new JSONObject();
                    JSONObject mJavaCodeLines_Props = new JSONObject();
                    JSONArray mJavaCodeLines_Labels = new JSONArray();
                    JSONArray mJavaCodeLines_Data = new JSONArray();
                    JSONArray mJavaCodeLines_Data2 = new JSONArray();
                    
                    String mSysCPU_ChartName = "Desktop CPU Load";
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
                    
                    String mSysDiskIO_ChartName = "Disk I/O Use";
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
                    
                    String mSysLoad_ChartName = "Desktop System Load";
                    JSONObject mSysLoad_Glob = new JSONObject();
                    JSONObject mSysLoad_Props = new JSONObject();
                    JSONArray mSysLoad_Labels = new JSONArray();
                    JSONArray mSysLoad_Data = new JSONArray();
                    JSONArray mSysLoad_Data2 = new JSONArray();
                    JSONArray mSysLoad_Data3 = new JSONArray();
                    
                    String mSysMemory_ChartName = "Desktop Memory Use";
                    JSONObject mSysMemory_Glob = new JSONObject();
                    JSONObject mSysMemory_Props = new JSONObject();
                    JSONArray mSysMemory_Labels = new JSONArray();
                    JSONArray mSysMemory_Data = new JSONArray();
                    JSONArray mSysMemory_Data2 = new JSONArray();
                    JSONArray mSysMemory_Data3 = new JSONArray();
                    JSONArray mSysMemory_Data4 = new JSONArray();
                    
                    String mSysMySQLSize_ChartName = "MySQL Database Sizes";
                    JSONObject mSysMySQLSize_Glob = new JSONObject();
                    JSONObject mSysMySQLSize_Props = new JSONObject();
                    JSONArray mSysMySQLSize_Labels = new JSONArray();
                    JSONArray mSysMySQLSize_Data = new JSONArray();
                    JSONArray mSysMySQLSize_Data2 = new JSONArray();
                    JSONArray mSysMySQLSize_Data3 = new JSONArray();
                    JSONArray mSysMySQLSize_Data4 = new JSONArray();
                    JSONArray mSysMySQLSize_Data5 = new JSONArray();
                    JSONArray mSysMySQLSize_Data6 = new JSONArray();
                    
                    String mSysNet_ChartName = "Network Use";
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
                    
                    String mSysNumUsers_ChartName = "Number of Users/Processes";
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
                    
                    String mSysStorage_ChartName = "Desktop Storage";
                    JSONObject mSysStorage_Glob = new JSONObject();
                    JSONObject mSysStorage_Props = new JSONObject();
                    JSONArray mSysStorage_Labels = new JSONArray();
                    JSONArray mSysStorage_Data = new JSONArray();
                    JSONArray mSysStorage_Data2 = new JSONArray();
                    
                    String mSysTemp_ChartName = "Desktop Temperatures";
                    JSONObject mSysTemp_Glob = new JSONObject();
                    JSONObject mSysTemp_Props = new JSONObject();
                    JSONArray mSysTemp_Labels = new JSONArray();
                    JSONArray mSysTemp_Data = new JSONArray();
                    JSONArray mSysTemp_Data2 = new JSONArray();
                    JSONArray mSysTemp_Data3 = new JSONArray();
                    JSONArray mSysTemp_Data4 = new JSONArray();
                    JSONArray mSysTemp_Data5 = new JSONArray();
                    JSONArray mSysTemp_Data6 = new JSONArray();
                    
                    mJavaCodeLines_Props
                            .put("chartName", mJavaCodeLines_ChartName).put("chartFileName", "mJavaCodeLines")
                            .put("sName", "asUtils/java").put("sColor", "Blue")
                            .put("s2Name", "asWeb/TOTAL").put("s2Color", "Red")
                            .put("xLabel", "WalkTime").put("yLabel", "Lines");
                    
                    mSysCPU_Props
                            .put("chartName", mSysCPU_ChartName).put("chartFileName", "mSysCPU")
                            .put("sName", "Avg CPU").put("sColor", "White")
                            .put("s2Name", "Core 1").put("s2Color", "Gray")
                            .put("s3Name", "Core 2").put("s3Color", "Gray")
                            .put("s4Name", "Core 3").put("s4Color", "Gray")
                            .put("s5Name", "Core 4").put("s5Color", "Gray")
                            .put("s6Name", "Core 5").put("s6Color", "Gray")
                            .put("s7Name", "Core 6").put("s7Color", "Gray")
                            .put("s8Name", "Core 7").put("s8Color", "Gray")
                            .put("s9Name", "Core 8").put("s9Color", "Gray")
                            .put("xLabel", "WalkTime").put("yLabel", "% Use");
                    
                    mSysDiskIO_Props
                            .put("chartName", mSysDiskIO_ChartName).put("chartFileName", "mSysDiskIO")
                            .put("sName", "Total").put("sColor", "White")
                            .put("s2Name", "Reads").put("s2Color", "Green")
                            .put("s3Name", "Writes").put("s3Color", "Red")
                            .put("xLabel", "WalkTime").put("yLabel", "x1000 Blocks");
                    
                    mSysLoad_Props
                            .put("chartName", mSysLoad_ChartName).put("chartFileName", "mSysLoad")
                            .put("sName", "LoadIndex").put("sColor", "Yellow")
                            .put("s2Name", "Load 5").put("s2Color", "Green")
                            .put("s3Name", "Load 15").put("s3Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Load Index");
                    
                    mSysMemory_Props
                            .put("chartName", mSysMemory_ChartName).put("chartFileName", "mSysMemory")
                            .put("sName", "Used").put("sColor", "Yellow")
                            .put("s2Name", "Swap").put("s2Color", "Red")
                            .put("s3Name", "Buffers").put("s3Color", "Green")
                            .put("s4Name", "Cached").put("s4Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Use in MBs");
                    
                    mSysMySQLSize_Props
                            .put("chartName", mSysMySQLSize_ChartName).put("chartFileName", "mSysMySQLSize")
                            .put("sName", "Rows").put("sColor", "Blue")
                            .put("s2Name", "Size:Core").put("s2Color", "Red")
                            .put("s3Name", "Size:Feeds").put("s3Color", "Orange")
                            .put("s4Name", "Size:NetSNMP").put("s4Color", "Yellow")
                            .put("s5Name", "Size:WxObs").put("s5Color", "Green")
                            .put("s6Name", "Size:TOTAL").put("s6Color", "White")
                            .put("xLabel", "WalkTime").put("yLabel", "Size Mbit");
                    
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
                    
                    mSysStorage_Props
                            .put("chartName", mSysStorage_ChartName).put("chartFileName", "mSysStorage")
                            .put("sName", "sda1").put("sColor", "Red")
                            .put("s2Name", "sdb1").put("s2Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "% Disk Use");                            
                            
                    mSysTemp_Props
                            .put("chartName", mSysTemp_ChartName).put("chartFileName", "mSysTemp")
                            .put("sName", "CPU Average").put("sColor", "Red")
                            .put("s2Name", "Case").put("s2Color", "Blue")
                            .put("s3Name", "Core 1/2").put("s3Color", "Gray")
                            .put("s4Name", "Core 3/4").put("s4Color", "Gray")
                            .put("s5Name", "Core 5/6").put("s5Color", "Gray")
                            .put("s6Name", "Core 7/8").put("s6Color", "Gray")
                            .put("xLabel", "WalkTime").put("yLabel", "Temp F");
                    
                    for(int i = 0; i < mainGlob.length(); i++) {
                        JSONObject thisObject = mainGlob.getJSONObject(i);
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
                        
                        mSysLoad_Labels.put(thisObject.getString("WalkTime"));
                        mSysLoad_Data.put(thisObject.getDouble("dtLoadIndex1"));
                        mSysLoad_Data2.put(thisObject.getDouble("dtLoadIndex5"));
                        mSysLoad_Data3.put(thisObject.getDouble("dtLoadIndex15"));
                        
                        float mSysMemory_Used = 0.00f;
                        try { mSysMemory_Used = ((thisObject.getLong("dtKMemPhysU")-(thisObject.getLong("dtKMemBuffU")+thisObject.getLong("dtKMemCachedU")))/1024); } catch (Exception e) { e.printStackTrace(); }
                        mSysMemory_Labels.put(thisObject.getString("WalkTime"));
                        mSysMemory_Data.put(mSysMemory_Used);
                        mSysMemory_Data2.put(thisObject.getLong("dtKSwapU")/1024);
                        mSysMemory_Data3.put(thisObject.getLong("dtKMemBuffU")/1024);
                        mSysMemory_Data4.put(thisObject.getLong("dtKMemCachedU")/1024);
                        
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
                        mSysMySQLSize_Data2.put(thisObject.getLong("duMySQLCore")/1000);
                        mSysMySQLSize_Data3.put(thisObject.getLong("duMySQLFeeds")/1000);
                        mSysMySQLSize_Data4.put(thisObject.getLong("duMySQLNetSNMP")/1000);
                        mSysMySQLSize_Data5.put(thisObject.getLong("duMySQLWxObs")/1000);
                        mSysMySQLSize_Data6.put(thisObject.getLong("duMySQLTotal")/1000);
                        
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
                        }
                        mSysNet_LastOctetsTotal = mSysNet_ThisOctetsTotal;
                        mSysNet_LastOctetsIn = thisObject.getLong("dtOctetsIn");
                        mSysNet_LastOctetsOut = thisObject.getLong("dtOctetsOut");
                        mSysNet_LastLapOctets = mSysNet_ThisLapOctets;
                        mSysNet_LastVirtualOctets = mSysNet_ThisVirtualOctets;
                        mSysNet_LastPiOctets = mSysNet_ThisPiOctets;
                        mSysNet_LastPi2Octets = mSysNet_ThisPi2Octets;
                        
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
                        
                        double mSysStorage_sda1Used = 0.00;
                        double mSysStorage_sdb1Used = 0.00;
                        try { mSysStorage_sda1Used = (double) 100.0 * ((double) thisObject.getLong("dtK4RootU")/(double) thisObject.getLong("dtK4Root")); } catch (Exception e) { e.printStackTrace(); }
                        try { mSysStorage_sdb1Used = (double) 100.0 * ((double) thisExpanded.getLong("k4Extra1U")/(double) thisExpanded.getLong("k4Extra1")); } catch (Exception e) { e.printStackTrace(); }
                        mSysStorage_Labels.put(thisObject.getString("WalkTime"));
                        mSysStorage_Data.put(mSysStorage_sda1Used);
                        mSysStorage_Data2.put(mSysStorage_sdb1Used);
                        
                        mSysTemp_Labels.put(thisObject.getString("WalkTime"));
                        mSysTemp_Data.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCPU")/1000)));
                        mSysTemp_Data2.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCase")/1000)));
                        mSysTemp_Data3.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore1")/1000)));
                        mSysTemp_Data4.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore2")/1000)));
                        mSysTemp_Data5.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore3")/1000)));
                        mSysTemp_Data6.put(0.93 * (wc.tempC2F(thisObject.getInt("dtTempCore4")/1000)));
                        
                    }
                    
                    mJavaCodeLines_Glob.put("labels", mJavaCodeLines_Labels).put("data", mJavaCodeLines_Data).put("data2", mJavaCodeLines_Data2).put("props", mJavaCodeLines_Props);
                    mSysCPU_Glob.put("labels", mSysCPU_Labels).put("data", mSysCPU_Data).put("data2", mSysCPU_Data2).put("data3", mSysCPU_Data3).put("data4", mSysCPU_Data4).put("data5", mSysCPU_Data5).put("data6", mSysCPU_Data6).put("data7", mSysCPU_Data7).put("data8", mSysCPU_Data8).put("data9", mSysCPU_Data9).put("props", mSysCPU_Props);
                    mSysLoad_Glob.put("labels", mSysLoad_Labels).put("data", mSysLoad_Data).put("data2", mSysLoad_Data2).put("data3", mSysLoad_Data3).put("props", mSysLoad_Props);
                    mSysMemory_Glob.put("labels", mSysMemory_Labels).put("data", mSysMemory_Data).put("data2", mSysMemory_Data2).put("data3", mSysMemory_Data3).put("data4", mSysMemory_Data4).put("props", mSysMemory_Props);
                    mSysMySQLSize_Glob.put("labels", mSysMySQLSize_Labels).put("data", mSysMySQLSize_Data).put("data2", mSysMySQLSize_Data2).put("data3", mSysMySQLSize_Data3).put("data4", mSysMySQLSize_Data4).put("data5", mSysMySQLSize_Data5).put("data6", mSysMySQLSize_Data6).put("props", mSysMySQLSize_Props);
                    mSysNet_Glob.put("labels", mSysNet_Labels).put("data", mSysNet_Data).put("data2", mSysNet_Data2).put("data3", mSysNet_Data3).put("data4", mSysNet_Data4).put("data5", mSysNet_Data5).put("data6", mSysNet_Data6).put("data7", mSysNet_Data7).put("data8", mSysNet_Data8).put("props", mSysNet_Props);
                    mSysNumUsers_Glob.put("labels", mSysNumUsers_Labels).put("data", mSysNumUsers_Data).put("data2", mSysNumUsers_Data2).put("data3", mSysNumUsers_Data3).put("data4", mSysNumUsers_Data4).put("data5", mSysNumUsers_Data5).put("data6", mSysNumUsers_Data6).put("data7", mSysNumUsers_Data7).put("props", mSysNumUsers_Props);
                    mSysStorage_Glob.put("labels", mSysStorage_Labels).put("data", mSysStorage_Data).put("data2", mSysStorage_Data2).put("props", mSysStorage_Props);
                    mSysDiskIO_Glob.put("labels", mSysDiskIO_Labels).put("data", mSysDiskIO_Data).put("data2", mSysDiskIO_Data2).put("data3", mSysDiskIO_Data3).put("props", mSysDiskIO_Props);
                    mSysTemp_Glob.put("labels", mSysTemp_Labels).put("data", mSysTemp_Data).put("data2", mSysTemp_Data2).put("data3", mSysTemp_Data3).put("data4", mSysTemp_Data4).put("data5", mSysTemp_Data5).put("data6", mSysTemp_Data6).put("props", mSysTemp_Props);
                    
                    try { dynChart.LineChart(mJavaCodeLines_Glob); returnData += "Chart generated - mJavaCodeLines!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysLoad_Glob); returnData += "Chart generated - mSysLoad!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysCPU_Glob); returnData += "Chart generated - mSysCPU!\n"; } catch (Exception e) { e.printStackTrace(); }  
                    try { dynChart.LineChart(mSysDiskIO_Glob); returnData += "Chart generated - mSysDiskIO!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysMemory_Glob); returnData += "Chart generated - mSysMemory!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysMySQLSize_Glob); returnData += "Chart generated - mSysMySQLSize!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysNet_Glob); returnData += "Chart generated - mSysNet!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysNumUsers_Glob); returnData += "Chart generated - mSysNumUsers!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysStorage_Glob); returnData += "Chart generated - mSysStorage!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysTemp_Glob); returnData += "Chart generated - mSysTemp!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    
                    break;
                    
            }
            
            if(genericCharts) {
                jsonGlob.put("labels", labels).put("data", data).put("props", props);
                if(data2.length() != 0) { jsonGlob.put("data2", data2); }
                if(data3.length() != 0) { jsonGlob.put("data3", data3); }
                if(data4.length() != 0) { jsonGlob.put("data4", data4); }
                try { dynChart.LineChart(jsonGlob); returnData = "Line chart generated!\n"; } catch (Exception e) { e.printStackTrace(); }
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    
    }
    
}