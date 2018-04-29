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
                    
                    JSONArray mainGlob = getSnmpAction.getMain(dbc, qParams);
                    JSONArray note3Glob = getSnmpAction.getNote3(dbc, qParams);
                    JSONArray emS4Glob = getSnmpAction.getEmS4(dbc, qParams);
                    JSONArray piGlob = getSnmpAction.getPi(dbc, qParams);
                    JSONArray pi2Glob = getSnmpAction.getPi2(dbc, qParams);
                    JSONArray routerGlob = getSnmpAction.getRouter(dbc, qParams);
                    
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
                    
                    String mSysLoad_ChartName = "Desktop System Load";
                    JSONObject mSysLoad_Glob = new JSONObject();
                    JSONObject mSysLoad_Props = new JSONObject();
                    JSONArray mSysLoad_Labels = new JSONArray();
                    JSONArray mSysLoad_Data = new JSONArray();
                    
                    String mSysMemory_ChartName = "Desktop Memory Use";
                    JSONObject mSysMemory_Glob = new JSONObject();
                    JSONObject mSysMemory_Props = new JSONObject();
                    JSONArray mSysMemory_Labels = new JSONArray();
                    JSONArray mSysMemory_Data = new JSONArray();
                    JSONArray mSysMemory_Data2 = new JSONArray();
                    JSONArray mSysMemory_Data3 = new JSONArray();
                    JSONArray mSysMemory_Data4 = new JSONArray();
                    
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
                    
                    mSysCPU_Props
                            .put("chartName", mSysCPU_ChartName).put("chartFileName", "mSysCPU")
                            .put("sName", "Avg CPU").put("sColor", "White")
                            .put("s2Name", "Core 1").put("s2Color", "Green")
                            .put("s3Name", "Core 2").put("s3Color", "Green")
                            .put("s4Name", "Core 3").put("s4Color", "Green")
                            .put("s5Name", "Core 4").put("s5Color", "Green")
                            .put("s6Name", "Core 5").put("s6Color", "Green")
                            .put("s7Name", "Core 6").put("s7Color", "Green")
                            .put("s8Name", "Core 7").put("s8Color", "Green")
                            .put("s9Name", "Core 8").put("s9Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "% Use");
                    
                    mSysLoad_Props
                            .put("chartName", mSysLoad_ChartName).put("chartFileName", "mSysLoad")
                            .put("sName", "LoadIndex").put("sColor", "Yellow")
                            .put("xLabel", "WalkTime").put("yLabel", "Load");
                    
                    mSysMemory_Props
                            .put("chartName", mSysMemory_ChartName).put("chartFileName", "mSysMemory")
                            .put("sName", "Used").put("sColor", "Yellow")
                            .put("s2Name", "Swap").put("s2Color", "Red")
                            .put("s3Name", "Buffers").put("s3Color", "Green")
                            .put("s4Name", "Cached").put("s4Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "Use in MBs");
                    
                    mSysStorage_Props
                            .put("chartName", mSysStorage_ChartName).put("chartFileName", "mSysStorage")
                            .put("sName", "sda1").put("sColor", "Red")
                            .put("s2Name", "sdb1").put("s2Color", "Blue")
                            .put("xLabel", "WalkTime").put("yLabel", "% Disk Use");                            
                            
                    mSysTemp_Props
                            .put("chartName", mSysTemp_ChartName).put("chartFileName", "mSysTemp")
                            .put("sName", "CPU Average").put("sColor", "Red")
                            .put("s2Name", "Case").put("s2Color", "Blue")
                            .put("s3Name", "Core 1/2").put("s3Color", "Green")
                            .put("s4Name", "Core 3/4").put("s4Color", "Green")
                            .put("s5Name", "Core 5/6").put("s5Color", "Green")
                            .put("s6Name", "Core 7/8").put("s6Color", "Green")
                            .put("xLabel", "WalkTime").put("yLabel", "Temp F");
                    
                    for(int i = 0; i < mainGlob.length(); i++) {
                        JSONObject thisObject = mainGlob.getJSONObject(i);
                        JSONObject thisExpanded = thisObject.getJSONObject("dtExpandedJSONData");
                        
                        double mSysCPU_LoadAverage = 0.00;
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
                        
                        mSysLoad_Labels.put(thisObject.getString("WalkTime"));
                        mSysLoad_Data.put(thisObject.getDouble("dtLoadIndex1"));
                        
                        double mSysMemory_Used = 0.00;
                        try { mSysMemory_Used = ((thisObject.getLong("dtKMemPhysU")-(thisObject.getLong("dtKMemBuffU")+thisObject.getLong("dtKMemCachedU")))/1024); } catch (Exception e) { e.printStackTrace(); }
                        mSysMemory_Labels.put(thisObject.getString("WalkTime"));
                        mSysMemory_Data.put(mSysMemory_Used);
                        mSysMemory_Data2.put(thisObject.getLong("dtKSwapU")/1024);
                        mSysMemory_Data3.put(thisObject.getLong("dtKMemBuffU")/1024);
                        mSysMemory_Data4.put(thisObject.getLong("dtKMemCachedU")/1024);
                        
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
                    
                    mSysCPU_Glob.put("labels", mSysCPU_Labels).put("data", mSysCPU_Data).put("data2", mSysCPU_Data2).put("data3", mSysCPU_Data3).put("data4", mSysCPU_Data4).put("data5", mSysCPU_Data5).put("data6", mSysCPU_Data6).put("data7", mSysCPU_Data7).put("data8", mSysCPU_Data8).put("data9", mSysCPU_Data9).put("props", mSysCPU_Props);
                    mSysLoad_Glob.put("labels", mSysLoad_Labels).put("data", mSysLoad_Data).put("props", mSysLoad_Props);
                    mSysMemory_Glob.put("labels", mSysMemory_Labels).put("data", mSysMemory_Data).put("data2", mSysMemory_Data2).put("data3", mSysMemory_Data3).put("data4", mSysMemory_Data4).put("props", mSysMemory_Props);
                    mSysStorage_Glob.put("labels", mSysStorage_Labels).put("data", mSysStorage_Data).put("data2", mSysStorage_Data2).put("props", mSysStorage_Props);
                    mSysTemp_Glob.put("labels", mSysTemp_Labels).put("data", mSysTemp_Data).put("data2", mSysTemp_Data2).put("data3", mSysTemp_Data3).put("data4", mSysTemp_Data4).put("data5", mSysTemp_Data5).put("data6", mSysTemp_Data6).put("props", mSysTemp_Props);
                    
                    try { dynChart.LineChart(mSysLoad_Glob); returnData += "Chart generated - mSysLoad!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysCPU_Glob); returnData += "Chart generated - mSysCPU!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysMemory_Glob); returnData += "Chart generated - mSysMemory!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysStorage_Glob); returnData += "Chart generated - mSysStorage!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(mSysTemp_Glob); returnData += "Chart generated - mSysTemp!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    
                    returnData += mSysStorage_Data.toString();
                    
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