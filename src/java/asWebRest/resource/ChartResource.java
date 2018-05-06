/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 6 May 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.action.GetSnmpAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.dao.FitnessDAO;
import asWebRest.dao.SnmpDAO;
import asWebRest.dao.WeatherDAO;
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
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        String doWhat = null;
        String order = "DESC";
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
                    
                    String mCellBattCPU_ChartName = "Note 3: Battery / CPU Use";
                    JSONObject mCellBattCPU_Glob = new JSONObject();
                    JSONObject mCellBattCPU_Props = new JSONObject();
                    JSONArray mCellBattCPU_Labels = new JSONArray();
                    JSONArray mCellBattCPU_Data = new JSONArray();
                    JSONArray mCellBattCPU_Data2 = new JSONArray();
                    JSONArray mCellBattCPU_Data3 = new JSONArray();
                    
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
                    
                    String mCellSig_ChartName = "Note 3: Signal Strength";
                    JSONObject mCellSig_Glob = new JSONObject();
                    JSONObject mCellSig_Props = new JSONObject();
                    JSONArray mCellSig_Labels = new JSONArray();
                    JSONArray mCellSig_Data = new JSONArray();
                    JSONArray mCellSig_Data2 = new JSONArray();
                    JSONArray mCellSig_Data3 = new JSONArray();
                    JSONArray mCellSig_Data4 = new JSONArray();
                    
                    String mCellTemp_ChartName = "Note 3: Temperatures";
                    JSONObject mCellTemp_Glob = new JSONObject();
                    JSONObject mCellTemp_Props = new JSONObject();
                    JSONArray mCellTemp_Labels = new JSONArray();
                    JSONArray mCellTemp_Data = new JSONArray();
                    
                    String mCellTempRapid_ChartName = "Note 3: Temperatures (Rapid Polling)";
                    JSONObject mCellTempRapid_Glob = new JSONObject();
                    JSONObject mCellTempRapid_Props = new JSONObject();
                    JSONArray mCellTempRapid_Labels = new JSONArray();
                    JSONArray mCellTempRapid_Data = new JSONArray();
                    
                    String mJavaCodeLines_ChartName = "Lines of Code - Java Projects";
                    JSONObject mJavaCodeLines_Glob = new JSONObject();
                    JSONObject mJavaCodeLines_Props = new JSONObject();
                    JSONArray mJavaCodeLines_Labels = new JSONArray();
                    JSONArray mJavaCodeLines_Data = new JSONArray();
                    JSONArray mJavaCodeLines_Data2 = new JSONArray();
                    
                    String mPiAmb_ChartName = "Raspberry Pi: Ambient Sensors";
                    JSONObject mPiAmb_Glob = new JSONObject();
                    JSONObject mPiAmb_Props = new JSONObject();
                    JSONArray mPiAmb_Labels = new JSONArray();
                    JSONArray mPiAmb_Data = new JSONArray();
                    JSONArray mPiAmb_Data2 = new JSONArray();
                    
                    String mPiCPU_ChartName = "Raspberry Pi: CPU Use";
                    JSONObject mPiCPU_Glob = new JSONObject();
                    JSONObject mPiCPU_Props = new JSONObject();
                    JSONArray mPiCPU_Labels = new JSONArray();
                    JSONArray mPiCPU_Data = new JSONArray();
                    JSONArray mPiCPU_Data2 = new JSONArray();
                    JSONArray mPiCPU_Data3 = new JSONArray();
                    JSONArray mPiCPU_Data4 = new JSONArray();
                    JSONArray mPiCPU_Data5 = new JSONArray();
                    
                    String mPiLoad_ChartName = "Raspberry Pi: System Load";
                    JSONObject mPiLoad_Glob = new JSONObject();
                    JSONObject mPiLoad_Props = new JSONObject();
                    JSONArray mPiLoad_Labels = new JSONArray();
                    JSONArray mPiLoad_Data = new JSONArray();
                    JSONArray mPiLoad_Data2 = new JSONArray();
                    JSONArray mPiLoad_Data3 = new JSONArray();
                    
                    String mPiMemory_ChartName = "Raspberry Pi: Memory Use";
                    JSONObject mPiMemory_Glob = new JSONObject();
                    JSONObject mPiMemory_Props = new JSONObject();
                    JSONArray mPiMemory_Labels = new JSONArray();
                    JSONArray mPiMemory_Data = new JSONArray();
                    JSONArray mPiMemory_Data2 = new JSONArray();
                    JSONArray mPiMemory_Data3 = new JSONArray();
                    JSONArray mPiMemory_Data4 = new JSONArray();
                    
                    String mPiTemp_ChartName = "Raspberry Pi: Temperature";
                    JSONObject mPiTemp_Glob = new JSONObject();
                    JSONObject mPiTemp_Props = new JSONObject();
                    JSONArray mPiTemp_Labels = new JSONArray();
                    JSONArray mPiTemp_Data = new JSONArray();
                    
                    String mPi2CPU_ChartName = "Raspberry Pi 2: CPU Use";
                    JSONObject mPi2CPU_Glob = new JSONObject();
                    JSONObject mPi2CPU_Props = new JSONObject();
                    JSONArray mPi2CPU_Labels = new JSONArray();
                    JSONArray mPi2CPU_Data = new JSONArray();
                    JSONArray mPi2CPU_Data2 = new JSONArray();
                    JSONArray mPi2CPU_Data3 = new JSONArray();
                    JSONArray mPi2CPU_Data4 = new JSONArray();
                    JSONArray mPi2CPU_Data5 = new JSONArray();
                    
                    String mPi2GPSSpeed_ChartName = "Raspberry Pi 2: GPS Statistics";
                    JSONObject mPi2GPSSpeed_Glob = new JSONObject();
                    JSONObject mPi2GPSSpeed_Props = new JSONObject();
                    JSONArray mPi2GPSSpeed_Labels = new JSONArray();
                    JSONArray mPi2GPSSpeed_Data = new JSONArray();
                    JSONArray mPi2GPSSpeed_Data2 = new JSONArray();
                    
                    String mPi2Light_ChartName = "Raspberry Pi 2: Light Level";
                    JSONObject mPi2Light_Glob = new JSONObject();
                    JSONObject mPi2Light_Props = new JSONObject();
                    JSONArray mPi2Light_Labels = new JSONArray();
                    JSONArray mPi2Light_Data = new JSONArray();
                    
                    String mPi2Load_ChartName = "Raspberry Pi 2: System Load";
                    JSONObject mPi2Load_Glob = new JSONObject();
                    JSONObject mPi2Load_Props = new JSONObject();
                    JSONArray mPi2Load_Labels = new JSONArray();
                    JSONArray mPi2Load_Data = new JSONArray();
                    JSONArray mPi2Load_Data2 = new JSONArray();
                    JSONArray mPi2Load_Data3 = new JSONArray();
                    
                    String mPi2Memory_ChartName = "Raspberry Pi 2: Memory Use";
                    JSONObject mPi2Memory_Glob = new JSONObject();
                    JSONObject mPi2Memory_Props = new JSONObject();
                    JSONArray mPi2Memory_Labels = new JSONArray();
                    JSONArray mPi2Memory_Data = new JSONArray();
                    JSONArray mPi2Memory_Data2 = new JSONArray();
                    JSONArray mPi2Memory_Data3 = new JSONArray();
                    JSONArray mPi2Memory_Data4 = new JSONArray();
                    
                    String mPi2Temp_ChartName = "Raspberry Pi 2: Temperature";
                    JSONObject mPi2Temp_Glob = new JSONObject();
                    JSONObject mPi2Temp_Props = new JSONObject();
                    JSONArray mPi2Temp_Labels = new JSONArray();
                    JSONArray mPi2Temp_Data = new JSONArray();
                    
                    String mRouterCPU_ChartName = "Router: CPU Use";
                    JSONObject mRouterCPU_Glob = new JSONObject();
                    JSONObject mRouterCPU_Props = new JSONObject();
                    JSONArray mRouterCPU_Labels = new JSONArray();
                    JSONArray mRouterCPU_Data = new JSONArray();
                    JSONArray mRouterCPU_Data2 = new JSONArray();
                    JSONArray mRouterCPU_Data3 = new JSONArray();
                    
                    String mRouterMemory_ChartName = "Router: Memory Use";
                    JSONObject mRouterMemory_Glob = new JSONObject();
                    JSONObject mRouterMemory_Props = new JSONObject();
                    JSONArray mRouterMemory_Labels = new JSONArray();
                    JSONArray mRouterMemory_Data = new JSONArray();
                    JSONArray mRouterMemory_Data2 = new JSONArray();
                    JSONArray mRouterMemory_Data3 = new JSONArray();
                    JSONArray mRouterMemory_Data4 = new JSONArray();
                    
                    String mRouterNet_ChartName = "Router: Network Use";
                    long mRouterNet_Cumulative = 0;
                    long mRouterNet_LastOctetsTotal = 0;
                    long mRouterNet_LastRouterE0Octets = 0;
                    long mRouterNet_LastRouterE1Octets = 0;
                    long mRouterNet_LastRouterE2Octets = 0;
                    long mRouterNet_LastRouterE3Octets = 0;
                    long mRouterNet_LastRouterV1Octets = 0;
                    long mRouterNet_LastRouterV2Octets = 0;
                    long mRouterNet_LastRouterB0Octets = 0;
                    long mRouterNet_LastRouterB1Octets = 0;
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
                    
                    String mSysFans_ChartName = "Desktop: Fan Speeds";
                    JSONObject mSysFans_Glob = new JSONObject();
                    JSONObject mSysFans_Props = new JSONObject();
                    JSONArray mSysFans_Labels = new JSONArray();
                    JSONArray mSysFans_Data = new JSONArray();
                    JSONArray mSysFans_Data2 = new JSONArray();
                    JSONArray mSysFans_Data3 = new JSONArray();
                    
                    String mSysLoad_ChartName = "Desktop: System Load";
                    JSONObject mSysLoad_Glob = new JSONObject();
                    JSONObject mSysLoad_Props = new JSONObject();
                    JSONArray mSysLoad_Labels = new JSONArray();
                    JSONArray mSysLoad_Data = new JSONArray();
                    JSONArray mSysLoad_Data2 = new JSONArray();
                    JSONArray mSysLoad_Data3 = new JSONArray();
                    
                    String mSysMemory_ChartName = "Desktop: Memory Use";
                    JSONObject mSysMemory_Glob = new JSONObject();
                    JSONObject mSysMemory_Props = new JSONObject();
                    JSONArray mSysMemory_Labels = new JSONArray();
                    JSONArray mSysMemory_Data = new JSONArray();
                    JSONArray mSysMemory_Data2 = new JSONArray();
                    JSONArray mSysMemory_Data3 = new JSONArray();
                    JSONArray mSysMemory_Data4 = new JSONArray();
                    
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
                    
                    String mSysStorage_ChartName = "Desktop: Storage";
                    JSONObject mSysStorage_Glob = new JSONObject();
                    JSONObject mSysStorage_Props = new JSONObject();
                    JSONArray mSysStorage_Labels = new JSONArray();
                    JSONArray mSysStorage_Data = new JSONArray();
                    JSONArray mSysStorage_Data2 = new JSONArray();
                    
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
                    
                    String mSysTomcatDeploy_ChartName = "Desktop: Tomcat WAR Deployments";
                    JSONObject mSysTomcatDeploy_Glob = new JSONObject();
                    JSONObject mSysTomcatDeploy_Props = new JSONObject();
                    JSONArray mSysTomcatDeploy_Labels = new JSONArray();
                    JSONArray mSysTomcatDeploy_Data = new JSONArray();
                    JSONArray mSysTomcatDeploy_Data2 = new JSONArray();
                    
                    String mSysUPSLoad_ChartName = "UPS: Load";
                    JSONObject mSysUPSLoad_Glob = new JSONObject();
                    JSONObject mSysUPSLoad_Props = new JSONObject();
                    JSONArray mSysUPSLoad_Labels = new JSONArray();
                    JSONArray mSysUPSLoad_Data = new JSONArray();
                    
                    String mSysUPSTimeLeft_ChartName = "UPS: Time Left";
                    JSONObject mSysUPSTimeLeft_Glob = new JSONObject();
                    JSONObject mSysUPSTimeLeft_Props = new JSONObject();
                    JSONArray mSysUPSTimeLeft_Labels = new JSONArray();
                    JSONArray mSysUPSTimeLeft_Data = new JSONArray();
                    
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
                    
                    mCellBattCPU_Props
                            .put("chartName", mCellBattCPU_ChartName).put("chartFileName", "mCellBattCPU")
                            .put("sName", "CPU").put("sColor", "Yellow")
                            .put("s2Name", "Battery").put("s2Color", "Red")
                            .put("s3Name", "Memory").put("s3Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mCellNet_Props
                            .put("chartName", mCellNet_ChartName).put("chartFileName", "mCellNet")
                            .put("sName", "rmnet0").put("sColor", "Yellow")
                            .put("s2Name", "wlan0").put("s2Color", "Red")
                            .put("s3Name", "Connections").put("s3Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "Data Kbps");
                    
                    mCellSig_Props
                            .put("chartName", mCellSig_ChartName).put("chartFileName", "mCellSig")
                            .put("sName", "LTE").put("sColor", "Orange")
                            .put("s2Name", "CDMA").put("s2Color", "Yellow")
                            .put("s3Name", "EVDO").put("s3Color", "Blue")
                            .put("s4Name", "GSM").put("s4Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "DBz");
                    
                    mCellTemp_Props
                            .put("chartName", mCellTemp_ChartName).put("chartFileName", "mCellTemp")
                            .put("sName", "Temperature").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "deg. F");
                    
                    mCellTempRapid_Props
                            .put("chartName", mCellTempRapid_ChartName).put("chartFileName", "mCellTempRapid")
                            .put("sName", "Temperature").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "deg. F");
                    
                    mJavaCodeLines_Props
                            .put("chartName", mJavaCodeLines_ChartName).put("chartFileName", "mJavaCodeLines")
                            .put("sName", "asUtils/java").put("sColor", "Blue")
                            .put("s2Name", "asWeb/TOTAL").put("s2Color", "Red")
                            .put("xLabel", "WalkTime").put("yLabel", "Lines");
                    
                    mPiAmb_Props
                            .put("chartName", mPiAmb_ChartName).put("chartFileName", "mPiAmb")
                            .put("sName", "Light").put("sColor", "Yellow")
                            .put("s2Name", "Sound").put("s2Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "Units");
                    
                    mPiCPU_Props
                            .put("chartName", mPiCPU_ChartName).put("chartFileName", "mPiCPU")
                            .put("sName", "Average").put("sColor", "Yellow")
                            .put("s2Name", "Core 1").put("s2Color", "Gray")
                            .put("s3Name", "Core 2").put("s3Color", "Gray")
                            .put("s4Name", "Core 3").put("s4Color", "Gray")
                            .put("s5Name", "Core 4").put("s5Color", "Gray")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mPiLoad_Props
                            .put("chartName", mPiLoad_ChartName).put("chartFileName", "mPiLoad")
                            .put("sName", "Load Index").put("sColor", "Red")
                            .put("s2Name", "5 min Avg").put("s2Color", "Green")
                            .put("s3Name", "15 min Avg").put("s3Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Load Index");
                    
                    mPiMemory_Props
                            .put("chartName", mPiMemory_ChartName).put("chartFileName", "mPiMemory")
                            .put("sName", "Overall").put("sColor", "Yellow")
                            .put("s2Name", "Swap").put("s2Color", "Red")
                            .put("s3Name", "Buffers").put("s3Color", "Orange")
                            .put("s4Name", "Cached").put("s4Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mPiTemp_Props
                            .put("chartName", mPiTemp_ChartName).put("chartFileName", "mPiTemp")
                            .put("sName", "Temperature").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "degrees F");
                                    
                    mPi2CPU_Props
                            .put("chartName", mPi2CPU_ChartName).put("chartFileName", "mPi2CPU")
                            .put("sName", "Average").put("sColor", "Yellow")
                            .put("s2Name", "Core 1").put("s2Color", "Gray")
                            .put("s3Name", "Core 2").put("s3Color", "Gray")
                            .put("s4Name", "Core 3").put("s4Color", "Gray")
                            .put("s5Name", "Core 4").put("s5Color", "Gray")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mPi2GPSSpeed_Props
                            .put("chartName", mPi2GPSSpeed_ChartName).put("chartFileName", "mPi2GPSSpeed")
                            .put("sName", "Speed MPH").put("sColor", "White")
                            .put("s2Name", "Fix Age MS").put("s2Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "MPH (MS)");
                    
                    mPi2Light_Props
                            .put("chartName", mPi2Light_ChartName).put("chartFileName", "mPi2Light")
                            .put("sName", "Light Level").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "Units");
                    
                    mPi2Load_Props
                            .put("chartName", mPi2Load_ChartName).put("chartFileName", "mPi2Load")
                            .put("sName", "Load Index").put("sColor", "Red")
                            .put("s2Name", "5 min Avg").put("s2Color", "Green")
                            .put("s3Name", "15 min Avg").put("s3Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Load Index");
                    
                    mPi2Memory_Props
                            .put("chartName", mPi2Memory_ChartName).put("chartFileName", "mPi2Memory")
                            .put("sName", "Overall").put("sColor", "Yellow")
                            .put("s2Name", "Swap").put("s2Color", "Red")
                            .put("s3Name", "Buffers").put("s3Color", "Orange")
                            .put("s4Name", "Cached").put("s4Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mPi2Temp_Props
                            .put("chartName", mPi2Temp_ChartName).put("chartFileName", "mPi2Temp")
                            .put("sName", "Temperature").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "degrees F");
                    
                    mRouterCPU_Props
                            .put("chartName", mRouterCPU_ChartName).put("chartFileName", "mRouterCPU")
                            .put("sName", "CPU Average").put("sColor", "Yellow")
                            .put("s2Name", "Core 1").put("s2Color", "Gray")
                            .put("s3Name", "Core 2").put("s3Color", "Gray")
                            .put("xLabel", "WalkTime").put("yLabel", "Percentage");
                    
                    mRouterMemory_Props
                            .put("chartName", mRouterMemory_ChartName).put("chartFileName", "mRouterMemory")
                            .put("sName", "Overall").put("sColor", "Yellow")
                            .put("s2Name", "Swap").put("s2Color", "Red")
                            .put("s3Name", "Buffers").put("s3Color", "Orange")
                            .put("s4Name", "Cached").put("s4Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mRouterNet_Props
                            .put("chartName", mRouterNet_ChartName).put("chartFileName", "mRouterNet")
                            .put("sName", "eth0").put("sColor", "Yellow")
                            .put("s2Name", "eth1").put("s2Color", "Orange")
                            .put("s3Name", "eth2").put("s3Color", "Green")
                            .put("s4Name", "eth3").put("s4Color", "Blue")
                            .put("s5Name", "vlan1").put("s5Color", "Gray")
                            .put("s6Name", "vlan2").put("s6Color", "Gray")
                            .put("s7Name", "br0").put("s7Color", "White")
                            .put("xLabel", "WalkTime").put("yLabel", "Data Mbps");
                    
                    mSysCams_Props
                            .put("chartName", mSysCams_ChartName).put("chartFileName", "mSysCams")
                            .put("sName", "Cam 1 (USB)").put("sColor", "Yellow")
                            .put("s2Name", "Cam 2 (Back)").put("s2Color", "Blue")
                            .put("s3Name", "Cam 3 (Drive)").put("s3Color", "Green")
                            .put("s4Name", "Cam 4 (Pi)").put("s4Color", "Orange")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent Available");
                    
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
                    
                    mSysDiskIO_Props
                            .put("chartName", mSysDiskIO_ChartName).put("chartFileName", "mSysDiskIO")
                            .put("sName", "Total").put("sColor", "White")
                            .put("s2Name", "Reads").put("s2Color", "Green")
                            .put("s3Name", "Writes").put("s3Color", "Red")
                            .put("xLabel", "WalkTime").put("yLabel", "x1000 Blocks");
                    
                    mSysFans_Props
                            .put("chartName", mSysFans_ChartName).put("chartFileName", "mSysFans")
                            .put("sName", "Fan 1").put("sColor", "Red")
                            .put("s2Name", "Fan 2").put("s2Color", "Blue")
                            .put("s3Name", "Fan 3").put("s3Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "RPM");
                    
                    mSysLoad_Props
                            .put("chartName", mSysLoad_ChartName).put("chartFileName", "mSysLoad")
                            .put("sName", "LoadIndex").put("sColor", "Red")
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
                            .put("sName", "CPU Average").put("sColor", "Yellow")
                            .put("s2Name", "Case").put("s2Color", "Green")
                            .put("s3Name", "Core 1/2").put("s3Color", "Gray")
                            .put("s4Name", "Core 3/4").put("s4Color", "Gray")
                            .put("s5Name", "Core 5/6").put("s5Color", "Gray")
                            .put("s6Name", "Core 7/8").put("s6Color", "Gray")
                            .put("xLabel", "WalkTime").put("yLabel", "Temp F");
                    
                    mSysTomcatDeploy_Props
                            .put("chartName", mSysTomcatDeploy_ChartName).put("chartFileName", "mSysTomcatDeploy")
                            .put("sName", "WAR Files").put("sColor", "Green")
                            .put("s2Name", "Deployed").put("s2Color", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "Files");
                    
                    mSysUPSLoad_Props
                            .put("chartName", mSysUPSLoad_ChartName).put("chartFileName", "mSysUPSLoad")
                            .put("sName", "UPS Load %").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "Percent");
                    
                    mSysUPSTimeLeft_Props
                            .put("chartName", mSysUPSTimeLeft_ChartName).put("chartFileName", "mSysUPSTimeLeft")
                            .put("sName", "Time Left").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "Minutes");
                    
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
                        
                        mSysCams_Labels.put(thisObject.getString("WalkTime"));
                        
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
                        
                        mSysFans_Labels.put(thisObject.getString("WalkTime"));
                        mSysFans_Data.put(thisObject.getInt("dtFan1"));
                        mSysFans_Data2.put(thisObject.getInt("dtFan2"));
                        mSysFans_Data3.put(thisObject.getInt("dtFan3"));
                        
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
                        mSysMySQLSize_Data2.put(thisObject.getLong("duMySQLCore")/100);
                        mSysMySQLSize_Data3.put(thisObject.getLong("duMySQLFeeds")/100);
                        mSysMySQLSize_Data4.put(thisObject.getLong("duMySQLNetSNMP")/100);
                        mSysMySQLSize_Data5.put(thisObject.getLong("duMySQLWxObs")/100);
                        mSysMySQLSize_Data6.put(thisObject.getLong("duMySQLTotal")/100);
                        
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
                        
                        mSysTomcatDeploy_Labels.put(thisObject.getString("WalkTime"));
                        mSysTomcatDeploy_Data.put(thisObject.getInt("dtTomcatWars"));
                        mSysTomcatDeploy_Data2.put(thisObject.getInt("dtTomcatDeploy"));
                        
                        mSysUPSLoad_Labels.put(thisObject.getString("WalkTime"));
                        mSysUPSLoad_Data.put(thisObject.getDouble("dtUPSLoad"));
                        
                        mSysUPSTimeLeft_Labels.put(thisObject.getString("WalkTime"));
                        mSysUPSTimeLeft_Data.put(thisObject.getDouble("dtUPSTimeLeft"));
                        
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
                    
                    for(int i = 0; i < note3Glob.length(); i++) {
                        
                        JSONObject note3Object = note3Glob.getJSONObject(i);
                        //JSONObject note3RapidSensors = note3Glob.getJSONObject("SensorsRapid");
                        
                        float mCellBattCPU_MemUse = ((note3Object.getLong("MemoryUse")/1024/1024)/3*100);
                        mCellBattCPU_Labels.put(note3Object.getString("WalkTime"));
                        mCellBattCPU_Data.put(note3Object.getDouble("CPUUse"));
                        mCellBattCPU_Data2.put(note3Object.getInt("BattLevel"));
                        mCellBattCPU_Data3.put(mCellBattCPU_MemUse);
                        
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
                        
                        mCellTemp_Labels.put(note3Object.getString("WalkTime"));
                        mCellTemp_Data.put(wc.tempC2F(note3Object.getInt("BattTemp")/10));
                        
                        mCellTempRapid_Labels.put(note3Object.getString("WalkTime"));
                        mCellTempRapid_Data.put(0);
                        /* for (JSONObject thisIteratedJson : note3RapidSensors) {
                            double mCellTempRapid_ThisTemp = thisIteratedJson.getDouble("AmbientTemperature");
                            if(mCellTempRapid_ThisTemp != 0) {
                                mCellTempRapid_Data.put(thisIteratedJson.getDouble("AmbientTemperatureF"));
                            }
                        } */
                        
                    }
                    
                    for(int i = 0; i < piGlob.length(); i++) {
                        
                        JSONObject piObject = piGlob.getJSONObject(i);
                        
                        mPiAmb_Labels.put(piObject.getString("WalkTime"));
                        mPiAmb_Data.put(piObject.getInt("ExtAmbLight"));
                        mPiAmb_Data2.put(piObject.getInt("ExtNoise"));
                        
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
                        
                        mPiLoad_Labels.put(piObject.getString("WalkTime"));
                        mPiLoad_Data.put(piObject.getDouble("LoadIndex1"));
                        mPiLoad_Data2.put(piObject.getDouble("LoadIndex5"));
                        mPiLoad_Data3.put(piObject.getDouble("LoadIndex15"));
                        
                        long mPiMemory_OverallUse = (
                                piObject.getLong("KMemPhysU") - (
                                piObject.getLong("KMemBuffU") +
                                piObject.getLong("KMemCachedU"))
                                / 1024
                        );
                        mPiMemory_Labels.put(piObject.getString("WalkTime"));
                        mPiMemory_Data.put(mPiMemory_OverallUse);
                        mPiMemory_Data2.put(piObject.getLong("KSwapU")/1024);
                        mPiMemory_Data3.put(piObject.getLong("KMemBuffU")/1024);
                        mPiMemory_Data4.put(piObject.getLong("KMemCachedU")/1024);
                        
                        mPiTemp_Labels.put(piObject.getString("WalkTime"));
                        mPiTemp_Data.put(piObject.getDouble("ExtTemp"));
                        
                    }
                    
                    
                    for(int i = 0; i < pi2Glob.length(); i++) {
                        
                        JSONObject pi2Object = pi2Glob.getJSONObject(i);
                        
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
                        
                        mPi2GPSSpeed_Labels.put(pi2Object.getString("WalkTime"));
                        mPi2GPSSpeed_Data.put(pi2Object.getDouble("GPSSpeedMPH"));
                        mPi2GPSSpeed_Data2.put((double) pi2Object.getInt("GPSAgeMS") / 100);
                        
                        mPi2Light_Labels.put(pi2Object.getString("WalkTime"));
                        mPi2Light_Data.put(pi2Object.getInt("LightLevel"));
                        
                        mPi2Load_Labels.put(pi2Object.getString("WalkTime"));
                        mPi2Load_Data.put(pi2Object.getDouble("LoadIndex1"));
                        mPi2Load_Data2.put(pi2Object.getDouble("LoadIndex5"));
                        mPi2Load_Data3.put(pi2Object.getDouble("LoadIndex15"));
                        
                        long mPi2Memory_OverallUse = (
                                pi2Object.getLong("KMemPhysU") - (
                                pi2Object.getLong("KMemBuffU") +
                                pi2Object.getLong("KMemCachedU"))
                                / 1024
                        );
                        mPi2Memory_Labels.put(pi2Object.getString("WalkTime"));
                        mPi2Memory_Data.put(mPi2Memory_OverallUse);
                        mPi2Memory_Data2.put(pi2Object.getLong("KSwapU")/1024);
                        mPi2Memory_Data3.put(pi2Object.getLong("KMemBuffU")/1024);
                        mPi2Memory_Data4.put(pi2Object.getLong("KMemCachedU")/1024);
                        
                        mPi2Temp_Labels.put(pi2Object.getString("WalkTime"));
                        mPi2Temp_Data.put(pi2Object.getDouble("ExtTemp"));
                        
                    }
                    
                    for(int i = 0; i < routerGlob.length(); i++) {
                        
                        JSONObject routerObject = routerGlob.getJSONObject(i);
                        
                        double thisCPUAverage = (routerObject.getInt("CPULoad1") + routerObject.getInt("CPULoad2") / 2);
                        mRouterCPU_Labels.put(routerObject.getString("WalkTime"));
                        mRouterCPU_Data.put(thisCPUAverage);
                        mRouterCPU_Data2.put(routerObject.getInt("CPULoad1"));
                        mRouterCPU_Data3.put(routerObject.getInt("CPULoad2"));
                        
                        long mRouterMemory_OverallUse = (
                                routerObject.getLong("KMemPhysU") - (
                                routerObject.getLong("KMemBuffU") +
                                routerObject.getLong("KMemCachedU"))
                                / 1024
                        );
                        mRouterMemory_Labels.put(routerObject.getString("WalkTime"));
                        mRouterMemory_Data.put(mRouterMemory_OverallUse);
                        mRouterMemory_Data2.put(routerObject.getLong("KSwapU")/1024);
                        mRouterMemory_Data3.put(routerObject.getLong("KMemBuffU")/1024);
                        mRouterMemory_Data4.put(routerObject.getLong("KMemCachedU")/1024);
                        
                        long mRouterNet_ThisDiffRouterE0Octets = 0;
                        long mRouterNet_ThisDiffRouterE1Octets = 0;
                        long mRouterNet_ThisDiffRouterE2Octets = 0;
                        long mRouterNet_ThisDiffRouterE3Octets = 0;
                        long mRouterNet_ThisDiffRouterV1Octets = 0;
                        long mRouterNet_ThisDiffRouterV2Octets = 0;
                        long mRouterNet_ThisDiffRouterB0Octets = 0;
                        long mRouterNet_ThisRouterE0Octets = routerObject.getLong("eth0Rx") + routerObject.getLong("eth0Tx");
                        long mRouterNet_ThisRouterE1Octets = routerObject.getLong("eth1Rx") + routerObject.getLong("eth0Tx");
                        long mRouterNet_ThisRouterE2Octets = routerObject.getLong("eth2Rx") + routerObject.getLong("eth2Tx");
                        long mRouterNet_ThisRouterE3Octets = routerObject.getLong("eth3Rx") + routerObject.getLong("eth3Tx");
                        long mRouterNet_ThisRouterV1Octets = routerObject.getLong("vlan1Rx") + routerObject.getLong("vlan1Tx");
                        long mRouterNet_ThisRouterV2Octets = routerObject.getLong("vlan2Rx") + routerObject.getLong("vlan2Tx");
                        long mRouterNet_ThisRouterB0Octets = routerObject.getLong("br0Rx") + routerObject.getLong("br0Tx");
                        long mRouterNet_ThisOctetsTotal = (
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
                    
                    
                    mCellBattCPU_Glob.put("labels", mCellBattCPU_Labels).put("data", mCellBattCPU_Data).put("data2", mCellBattCPU_Data2).put("data3", mCellBattCPU_Data3).put("props", mCellBattCPU_Props);
                    mCellNet_Glob.put("labels", mCellNet_Labels).put("data", mCellNet_Data).put("data2", mCellNet_Data2).put("data3", mCellNet_Data3).put("props", mCellNet_Props);
                    mCellSig_Glob.put("labels", mCellSig_Labels).put("data", mCellSig_Data).put("data2", mCellSig_Data2).put("data3", mCellSig_Data3).put("data4", mCellSig_Data4).put("props", mCellSig_Props);
                    mCellTemp_Glob.put("labels", mCellTemp_Labels).put("data", mCellTemp_Data).put("props", mCellTemp_Props);
                    mCellTempRapid_Glob.put("labels", mCellTempRapid_Labels).put("data", mCellTempRapid_Data).put("props", mCellTempRapid_Props);
                    mJavaCodeLines_Glob.put("labels", mJavaCodeLines_Labels).put("data", mJavaCodeLines_Data).put("data2", mJavaCodeLines_Data2).put("props", mJavaCodeLines_Props);
                    mPiAmb_Glob.put("labels", mPiAmb_Labels).put("data", mPiAmb_Data).put("data2", mPiAmb_Data2).put("props", mPiAmb_Props);
                    mPiCPU_Glob.put("labels", mPiCPU_Labels).put("data", mPiCPU_Data).put("data2", mPiCPU_Data2).put("data3", mPiCPU_Data3).put("data4", mPiCPU_Data4).put("data5", mPiCPU_Data5).put("props", mPiCPU_Props);
                    mPiLoad_Glob.put("labels", mPiLoad_Labels).put("data", mPiLoad_Data).put("data2", mPiLoad_Data2).put("data3", mPiLoad_Data3).put("props", mPiLoad_Props);
                    mPiMemory_Glob.put("labels", mPiMemory_Labels).put("data", mPiMemory_Data).put("data2", mPiMemory_Data2).put("data3", mPiMemory_Data3).put("data4", mPiMemory_Data4).put("props", mPiMemory_Props);
                    mPiTemp_Glob.put("labels", mPiTemp_Labels).put("data", mPiTemp_Data).put("props", mPiTemp_Props);
                    mPi2CPU_Glob.put("labels", mPi2CPU_Labels).put("data", mPi2CPU_Data).put("data2", mPi2CPU_Data2).put("data3", mPi2CPU_Data3).put("data4", mPi2CPU_Data4).put("data5", mPi2CPU_Data5).put("props", mPi2CPU_Props);
                    mPi2GPSSpeed_Glob.put("labels", mPi2GPSSpeed_Labels).put("data", mPi2GPSSpeed_Data).put("data2", mPi2GPSSpeed_Data2).put("props", mPi2GPSSpeed_Props);
                    mPi2Light_Glob.put("labels", mPi2Light_Labels).put("data", mPi2Light_Data).put("props", mPi2Light_Props);
                    mPi2Load_Glob.put("labels", mPi2Load_Labels).put("data", mPi2Load_Data).put("data2", mPi2Load_Data2).put("data3", mPi2Load_Data3).put("props", mPi2Load_Props);
                    mPi2Memory_Glob.put("labels", mPi2Memory_Labels).put("data", mPi2Memory_Data).put("data2", mPi2Memory_Data2).put("data3", mPi2Memory_Data3).put("data4", mPi2Memory_Data4).put("props", mPi2Memory_Props);
                    mPi2Temp_Glob.put("labels", mPi2Temp_Labels).put("data", mPi2Temp_Data).put("props", mPi2Temp_Props);
                    mRouterCPU_Glob.put("labels", mRouterCPU_Labels).put("data", mRouterCPU_Data).put("data2", mRouterCPU_Data2).put("data3", mRouterCPU_Data3).put("props", mRouterCPU_Props);
                    mRouterMemory_Glob.put("labels", mRouterMemory_Labels).put("data", mRouterMemory_Data).put("data2", mRouterMemory_Data2).put("data3", mRouterMemory_Data3).put("data4", mRouterMemory_Data4).put("props", mRouterMemory_Props);
                    mRouterNet_Glob.put("labels", mRouterNet_Labels).put("data", mRouterNet_Data).put("data2", mRouterNet_Data2).put("data3", mRouterNet_Data3).put("data4", mRouterNet_Data4).put("data5", mRouterNet_Data5).put("data6", mRouterNet_Data6).put("data7", mRouterNet_Data7).put("props", mRouterNet_Props);
                    mSysCams_Glob.put("labels", mSysCams_Labels).put("data", mSysCams_Data).put("data2", mSysCams_Data2).put("data3", mSysCams_Data3).put("data4", mSysCams_Data4).put("props", mSysCams_Props);
                    mSysCPU_Glob.put("labels", mSysCPU_Labels).put("data", mSysCPU_Data).put("data2", mSysCPU_Data2).put("data3", mSysCPU_Data3).put("data4", mSysCPU_Data4).put("data5", mSysCPU_Data5).put("data6", mSysCPU_Data6).put("data7", mSysCPU_Data7).put("data8", mSysCPU_Data8).put("data9", mSysCPU_Data9).put("props", mSysCPU_Props);
                    mSysFans_Glob.put("labels", mSysFans_Labels).put("data", mSysFans_Data).put("data2", mSysFans_Data2).put("data3", mSysFans_Data3).put("props", mSysFans_Props);
                    mSysLoad_Glob.put("labels", mSysLoad_Labels).put("data", mSysLoad_Data).put("data2", mSysLoad_Data2).put("data3", mSysLoad_Data3).put("props", mSysLoad_Props);
                    mSysMemory_Glob.put("labels", mSysMemory_Labels).put("data", mSysMemory_Data).put("data2", mSysMemory_Data2).put("data3", mSysMemory_Data3).put("data4", mSysMemory_Data4).put("props", mSysMemory_Props);
                    mSysMySQLSize_Glob.put("labels", mSysMySQLSize_Labels).put("data", mSysMySQLSize_Data).put("data2", mSysMySQLSize_Data2).put("data3", mSysMySQLSize_Data3).put("data4", mSysMySQLSize_Data4).put("data5", mSysMySQLSize_Data5).put("data6", mSysMySQLSize_Data6).put("props", mSysMySQLSize_Props);
                    mSysNet_Glob.put("labels", mSysNet_Labels).put("data", mSysNet_Data).put("data2", mSysNet_Data2).put("data3", mSysNet_Data3).put("data4", mSysNet_Data4).put("data5", mSysNet_Data5).put("data6", mSysNet_Data6).put("data7", mSysNet_Data7).put("data8", mSysNet_Data8).put("props", mSysNet_Props);
                    mSysNumUsers_Glob.put("labels", mSysNumUsers_Labels).put("data", mSysNumUsers_Data).put("data2", mSysNumUsers_Data2).put("data3", mSysNumUsers_Data3).put("data4", mSysNumUsers_Data4).put("data5", mSysNumUsers_Data5).put("data6", mSysNumUsers_Data6).put("data7", mSysNumUsers_Data7).put("props", mSysNumUsers_Props);
                    mSysStorage_Glob.put("labels", mSysStorage_Labels).put("data", mSysStorage_Data).put("data2", mSysStorage_Data2).put("props", mSysStorage_Props);
                    mSysDiskIO_Glob.put("labels", mSysDiskIO_Labels).put("data", mSysDiskIO_Data).put("data2", mSysDiskIO_Data2).put("data3", mSysDiskIO_Data3).put("props", mSysDiskIO_Props);
                    mSysTemp_Glob.put("labels", mSysTemp_Labels).put("data", mSysTemp_Data).put("data2", mSysTemp_Data2).put("data3", mSysTemp_Data3).put("data4", mSysTemp_Data4).put("data5", mSysTemp_Data5).put("data6", mSysTemp_Data6).put("props", mSysTemp_Props);
                    mSysTomcatDeploy_Glob.put("labels", mSysTomcatDeploy_Labels).put("data", mSysTomcatDeploy_Data).put("data2", mSysTomcatDeploy_Data2).put("props", mSysTomcatDeploy_Props);
                    mSysUPSLoad_Glob.put("labels", mSysUPSLoad_Labels).put("data", mSysUPSLoad_Data).put("props", mSysUPSLoad_Props);
                    mSysUPSTimeLeft_Glob.put("labels", mSysUPSTimeLeft_Labels).put("data", mSysUPSTimeLeft_Data).put("props", mSysUPSTimeLeft_Props);
                    mSysVolt_Glob.put("labels", mSysVolt_Labels).put("data", mSysVolt_Data).put("data2", mSysVolt_Data2).put("data3", mSysVolt_Data3).put("data4", mSysVolt_Data4).put("data5", mSysVolt_Data5).put("data6", mSysVolt_Data6).put("data7", mSysVolt_Data7).put("data8", mSysVolt_Data8).put("data9", mSysVolt_Data9).put("props", mSysVolt_Props);
                    
                    try { dynChart.LineChart(mCellBattCPU_Glob); returnData += "Chart generated - mCellBattCPU!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mCellNet_Glob); returnData += "Chart generated - mCellNet!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mCellSig_Glob); returnData += "Chart generated - mCellSig!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mCellTemp_Glob); returnData += "Chart generated - mCellTemp!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mCellTempRapid_Glob); returnData += "Chart generated - mCellTempRapid!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mJavaCodeLines_Glob); returnData += "Chart generated - mJavaCodeLines!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPiAmb_Glob); returnData += "Chart generated - mPiAmb!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPiCPU_Glob); returnData += "Chart generated - mPiCPU!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPiLoad_Glob); returnData += "Chart generated - mPiLoad!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPiMemory_Glob); returnData += "Chart generated - mPiMemory!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPiTemp_Glob); returnData += "Chart generated - mPiTemp!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPi2CPU_Glob); returnData += "Chart generated - mPi2CPU!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPi2GPSSpeed_Glob); returnData += "Chart generated - mPi2GPSSpeed!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPi2Light_Glob); returnData += "Chart generated - mPi2Light!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPi2Load_Glob); returnData += "Chart generated - mPi2Load!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPi2Memory_Glob); returnData += "Chart generated - mPi2Memory!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mPi2Temp_Glob); returnData += "Chart generated - mPi2Temp!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mRouterCPU_Glob); returnData += "Chart generated - mRouterCPU!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mRouterMemory_Glob); returnData += "Chart generated - mRouterMemory!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mRouterNet_Glob); returnData += "Chart generated - mRouterNet!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysFans_Glob); returnData += "Chart generated - mSysFans!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysLoad_Glob); returnData += "Chart generated - mSysLoad!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysCams_Glob); returnData += "Chart generated - mSysCams!\n"; } catch (Exception e) { e.printStackTrace(); }  
                    try { dynChart.LineChart(mSysCPU_Glob); returnData += "Chart generated - mSysCPU!\n"; } catch (Exception e) { e.printStackTrace(); }  
                    try { dynChart.LineChart(mSysDiskIO_Glob); returnData += "Chart generated - mSysDiskIO!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysMemory_Glob); returnData += "Chart generated - mSysMemory!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysMySQLSize_Glob); returnData += "Chart generated - mSysMySQLSize!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysNet_Glob); returnData += "Chart generated - mSysNet!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysNumUsers_Glob); returnData += "Chart generated - mSysNumUsers!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(mSysStorage_Glob); returnData += "Chart generated - mSysStorage!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysTemp_Glob); returnData += "Chart generated - mSysTemp!\n"; } catch (Exception e) { e.printStackTrace(); }  
                    try { dynChart.LineChart(mSysTomcatDeploy_Glob); returnData += "Chart generated - mSysTomcatDeploy!\n"; } catch (Exception e) { e.printStackTrace(); }  
                    try { dynChart.LineChart(mSysUPSLoad_Glob); returnData += "Chart generated - mSysUPSLoad!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysUPSTimeLeft_Glob); returnData += "Chart generated - mSysUPSTimeLeft!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysVolt_Glob); returnData += "Chart generated - mSysVolt!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    
                    returnData += mCellBattCPU_Data3.toString();
                    
                    break;
                    
                case "WeatherCf6OverviewCharts":
                    
                    genericCharts = false;
                    order = "ASC";
                    
                    String cf6ChDateStart = "";
                    String cf6ChDateEnd = "";
                    
                    try {
                        cf6ChDateStart = argsInForm.getFirstValue("dateStart");
                        cf6ChDateEnd = argsInForm.getFirstValue("dateEnd");
                        qParams.add(0, cf6ChDateStart);
                        qParams.add(1, cf6ChDateEnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    JSONArray cf6Data = getWeatherAction.getCf6Main(dbc, qParams, order);
                    
                    String cf6cpc_ChartName = "CPC Data: " + cf6ChDateStart + " to " + cf6ChDateEnd;
                    JSONObject cf6cpc_Glob = new JSONObject();
                    JSONObject cf6cpc_Props = new JSONObject();
                    JSONArray cf6cpc_Labels = new JSONArray();
                    JSONArray cf6cpc_Data = new JSONArray();
                    JSONArray cf6cpc_Data2 = new JSONArray();
                    JSONArray cf6cpc_Data3 = new JSONArray();
                    JSONArray cf6cpc_Data4 = new JSONArray();
                    
                    String cf6Depart_ChartName = "KMCI Departures: " + cf6ChDateStart + " to " + cf6ChDateEnd;
                    JSONObject cf6Depart_Glob = new JSONObject();
                    JSONObject cf6Depart_Props = new JSONObject();
                    JSONArray cf6Depart_Labels = new JSONArray();
                    JSONArray cf6Depart_Data = new JSONArray();
                    
                    String cf6Temps_ChartName = "KMCI Temperatures: " + cf6ChDateStart + " to " + cf6ChDateEnd;
                    JSONObject cf6Temps_Glob = new JSONObject();
                    JSONObject cf6Temps_Props = new JSONObject();
                    JSONArray cf6Temps_Labels = new JSONArray();
                    JSONArray cf6Temps_Data = new JSONArray();
                    JSONArray cf6Temps_Data2 = new JSONArray();
                    
                    cf6cpc_Props
                            .put("chartName", cf6cpc_ChartName).put("chartFileName", "cf6cpc")
                            .put("sName", "AO").put("sColor", "Red")
                            .put("s2Name", "AAO").put("s2Color", "Blue")
                            .put("s3Name", "NAO").put("s3Color", "Green")
                            .put("s4Name", "PNA").put("s4Color", "Yellow")
                            .put("xLabel", "Date").put("yLabel", "Anom");
                    
                    cf6Depart_Props
                            .put("chartName", cf6Depart_ChartName).put("chartFileName", "cf6Depart")
                            .put("sName", "Departure").put("sColor", "Yellow")
                            .put("xLabel", "Date").put("yLabel", "degrees F");
                    
                    cf6Temps_Props
                            .put("chartName", cf6Temps_ChartName).put("chartFileName", "cf6Temps")
                            .put("sName", "High").put("sColor", "Red")
                            .put("s2Name", "Low").put("s2Color", "Blue")
                            .put("xLabel", "Date").put("yLabel", "degress F");
                    
                    for(int i = 0; i < cf6Data.length(); i++) {
                        
                        JSONObject thisObject = cf6Data.getJSONObject(i);
                        
                        cf6cpc_Labels.put(thisObject.getString("Date"));
                        cf6cpc_Data.put(thisObject.getDouble("AO"));
                        cf6cpc_Data2.put(thisObject.getDouble("AAO"));
                        cf6cpc_Data3.put(thisObject.getDouble("NAO"));
                        cf6cpc_Data4.put(thisObject.getDouble("PNA"));
                        
                        cf6Depart_Labels.put(thisObject.getString("Date"));
                        cf6Depart_Data.put(thisObject.getInt("DFNorm"));
                        
                        cf6Temps_Labels.put(thisObject.getString("Date"));
                        cf6Temps_Data.put(thisObject.getInt("High"));
                        cf6Temps_Data2.put(thisObject.getInt("Low"));
                        
                    }
                    
                    cf6cpc_Glob.put("labels", cf6cpc_Labels).put("data", cf6cpc_Data).put("data2", cf6cpc_Data2).put("data3", cf6cpc_Data3).put("data4", cf6cpc_Data4).put("props", cf6cpc_Props);
                    cf6Depart_Glob.put("labels", cf6Depart_Labels).put("data", cf6Depart_Data).put("props", cf6Depart_Props);
                    cf6Temps_Glob.put("labels", cf6Temps_Labels).put("data", cf6Temps_Data).put("data2", cf6Temps_Data2).put("props", cf6Temps_Props);
                    
                    try { dynChart.LineChart(cf6cpc_Glob); returnData += "Chart generated - cf6cpc!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(cf6Depart_Glob); returnData += "Chart generated - cf6Depart!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(cf6Temps_Glob); returnData += "Chart generated - cf6Temps!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    
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