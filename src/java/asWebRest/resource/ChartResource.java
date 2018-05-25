/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 25 May 2018
 */

package asWebRest.resource;

import asWebRest.action.GetEntertainmentAction;
import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetFitnessAction;
import asWebRest.action.GetSnmpAction;
import asWebRest.action.GetUtilityUseAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.chartHelpers.Finance;
import asWebRest.chartHelpers.SysMonNote3;
import asWebRest.chartHelpers.SysMonDesktop;
import asWebRest.chartHelpers.SysMonPi;
import asWebRest.chartHelpers.SysMonPi2;
import asWebRest.chartHelpers.SysMonRouter;
import asWebRest.chartHelpers.Utilities;
import asWebRest.chartHelpers.Weather;
import asWebRest.dao.EntertainmentDAO;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.FitnessDAO;
import asWebRest.dao.SnmpDAO;
import asWebRest.dao.UtilityUseDAO;
import asWebRest.dao.WeatherDAO;
import asWebRest.hookers.DynChartX;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        GetEntertainmentAction getEntertainmentAction = new GetEntertainmentAction(new EntertainmentDAO());
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
        
        Finance fin = new Finance();
        Weather wx = new Weather();
                    
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
                                                   
                case "EntertainmentFfxivQuestsByDate":
                    genericCharts = true;
                    final String fullChartNameQBD = "FFXIV Quests By Date";
                    JSONArray jsonResultArrayQBD = getEntertainmentAction.getFfxivQuestsByDate(dbc);
                    for (int i = 0; i < jsonResultArrayQBD.length(); i++) {
                        JSONObject thisObject = jsonResultArrayQBD.getJSONObject(i);
                        labels.put(thisObject.getString("OrigCompDate"));
                        data.put(thisObject.getInt("OnThisDate"));
                    }
                    props
                        .put("dateFormat", "yyyy-MM-dd")
                        .put("chartName", fullChartNameQBD).put("chartFileName", "ffxivQuestsByDay")
                        .put("sName", "Quests").put("sColor", "Yellow")
                        .put("xLabel", "Date").put("yLabel", "Quests");
                    break;
                    
                case "FinanceBills":
                    genericCharts = false;
                    JSONArray bill_Raw = getFinanceAction.getBills(dbc);
                    JSONObject bill_Glob = fin.getBillCh(bill_Raw);
                    try { dynChart.LineChart(bill_Glob); returnData += "Chart generated - Bills!\n"; } catch (Exception e) { e.printStackTrace(); }
                    break;
                
                case "FinanceOverviewCharts":
                    genericCharts = false;
                    JSONArray enw_Raw = getFinanceAction.getEnwChart(dbc);
                    JSONArray svChart_Raw = getFinanceAction.getSavingChart(dbc, null);
                    JSONObject enw_Glob = fin.getFinEnw(enw_Raw);
                    JSONObject svChart_Glob = fin.getSavingsOpt(svChart_Raw);
                    try { dynChart.LineChart(enw_Glob); returnData += "Chart generated - Est Net Worth!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(svChart_Glob); returnData += "Chart generated - Savings!\n"; } catch (Exception e) { e.printStackTrace(); }
                    break;
                 
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
                        .put("dateFormat", "yyyy-MM-dd")
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
                        .put("dateFormat", "yyyy-MM-dd")
                        .put("chartName", fullChartName).put("chartFileName", "WeightRange")
                        .put("sName", "lbs").put("sColor", "Yellow")
                        .put("xLabel", "Date").put("yLabel", "Weight");
                    break;
                    
                case "SysMonCharts":
                    
                    final DateFormat wtf = new SimpleDateFormat("yyyyMMdd");
                    final Date nowTimestamp = new Date();
                    final String wtfDate = wtf.format(nowTimestamp);
                    
                    SysMonNote3 smCell = new SysMonNote3();
                    SysMonDesktop smDesktop = new SysMonDesktop();
                    SysMonPi smPi = new SysMonPi();
                    SysMonPi2 smPi2 = new SysMonPi2();
                    SysMonRouter smRouter = new SysMonRouter();
                    
                    String stepIn = null;
                    String stepTest = "1";
                    String dateIn = null;
                    String dateTest = "1";
                    
                    try { 
                        stepIn = argsInForm.getFirstValue("step");
                        dateIn = argsInForm.getFirstValue("date").replace("-", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    if(wc.isSet(stepIn) && !stepIn.equals("1")) { stepTest = "0"; }
                    if(wc.isSet(dateIn) && !dateIn.equals(wtfDate)) { dateTest = "0"; }
                    
                    qParams.add(0, stepTest); //Test
                    qParams.add(1, stepIn);                   
                    qParams.add(2, dateTest); //DateTest
                    qParams.add(3, dateIn);
                    
                    returnData += "SQL Query paramaters: " + qParams.toString() + "\n";
                    
                    genericCharts = false;
                    int step = Integer.valueOf(stepIn);
                    int intLen = 60 * 2;
                    
                    JSONArray mainGlob = getSnmpAction.getMain(dbc, qParams);
                    JSONArray note3Glob = getSnmpAction.getNote3(dbc, qParams);
                    JSONArray emS4Glob = getSnmpAction.getEmS4(dbc, qParams);
                    JSONArray piGlob = getSnmpAction.getPi(dbc, qParams);
                    JSONArray pi2Glob = getSnmpAction.getPi2(dbc, qParams);
                    JSONArray routerGlob = getSnmpAction.getRouter(dbc, qParams);
                    
                    JSONObject mCellBattCPU_Glob = smCell.getCellBattCPU(note3Glob, intLen, step);
                    JSONObject mCellNet_Glob = smCell.getCellNet(note3Glob, intLen, step);
                    JSONObject mCellSig_Glob = smCell.getCellSig(note3Glob, intLen, step);
                    JSONObject mCellTemp_Glob = smCell.getCellTemp(note3Glob, intLen, step);
                    JSONObject mCellTempRapid_Glob = smCell.getCellTempRapid(note3Glob, intLen, step);
                    JSONObject mJavaCodeLines_Glob = smDesktop.getJavaCodeLines(mainGlob, intLen, step);
                    JSONObject mPiAmb_Glob = smPi.getPiAmb(piGlob, intLen, step);
                    JSONObject mPiCPU_Glob = smPi.getPiCPU(piGlob, intLen, step);
                    JSONObject mPiLoad_Glob = smPi.getPiLoad(piGlob, intLen, step);
                    JSONObject mPiMemory_Glob = smPi.getPiMemory(piGlob, intLen, step);
                    JSONObject mPiTemp_Glob = smPi.getPiTemp(piGlob, intLen, step);
                    JSONObject mPi2CPU_Glob = smPi2.getPi2CPU(pi2Glob, intLen, step);
                    JSONObject mPi2GPSSpeed_Glob = smPi2.getPi2GPSSpeed(pi2Glob, intLen, step);
                    JSONObject mPi2Light_Glob = smPi2.getPi2Light(pi2Glob, intLen, step);
                    JSONObject mPi2Load_Glob = smPi2.getPi2Load(pi2Glob, intLen, step);
                    JSONObject mPi2Memory_Glob = smPi2.getPi2Memory(pi2Glob, intLen, step);
                    JSONObject mPi2Temp_Glob = smPi2.getPi2Temp(pi2Glob, intLen, step);
                    JSONObject mRouterCPU_Glob = smRouter.getRouterCPU(routerGlob, intLen, step);
                    JSONObject mRouterMemory_Glob = smRouter.getRouterMemory(routerGlob, intLen, step);
                    JSONObject mRouterNet_Glob = smRouter.getRouterNet(routerGlob, intLen, step);
                    JSONObject mSysFans_Glob = smDesktop.getSysFans(mainGlob, intLen, step);
                    JSONObject mSysLoad_Glob = smDesktop.getSysLoad(mainGlob, intLen, step);
                    JSONObject mSysCams_Glob = smDesktop.getSysCams(mainGlob, intLen, step);
                    JSONObject mSysCPU_Glob = smDesktop.getSysCPU(mainGlob, intLen, step);
                    JSONObject mSysDiskIO_Glob = smDesktop.getSysDiskIO(mainGlob, intLen, step);
                    JSONObject mSysMemory_Glob = smDesktop.getSysMemory(mainGlob, intLen, step);
                    JSONObject mSysMySQLSize_Glob = smDesktop.getSysMySQLSize(mainGlob, intLen, step);
                    JSONObject mSysNet_Glob = smDesktop.getSysNet(mainGlob, intLen, step);
                    JSONObject mSysNumUsers_Glob = smDesktop.getSysNumUsers(mainGlob, intLen, step);
                    JSONObject mSysStorage_Glob = smDesktop.getSysStorage(mainGlob, intLen, step);
                    JSONObject mSysTemp_Glob = smDesktop.getSysTemp(mainGlob, intLen, step);
                    JSONObject mSysTomcatDeploy_Glob = smDesktop.getSysTomcatDeploy(mainGlob, intLen, step);
                    JSONObject mSysUPSLoad_Glob = smDesktop.getSysUPSLoad(mainGlob, intLen, step);
                    JSONObject mSysUPSTimeLeft_Glob = smDesktop.getSysUPSTimeLeft(mainGlob, intLen, step);
                    JSONObject mSysVolt_Glob = smDesktop.getSysVolt(mainGlob, intLen, step);
                    
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
 
                    break;
                    
                case "Utilities":
                    genericCharts = false;
                    Utilities util = new Utilities();
                    JSONArray gasMcf_Raw = getUtilityUseAction.getChUseGas(dbc);
                    JSONArray kWhU_Raw = getUtilityUseAction.getChUseElecD(dbc);
                    JSONArray ph_Raw = getUtilityUseAction.getChCellUse(dbc);
                    JSONArray webData_Raw = getUtilityUseAction.getChWebData(dbc);
                    JSONObject gasMcf_Glob = util.getGasMcf(gasMcf_Raw);
                    JSONObject kWhU_Glob = util.getKWhU(kWhU_Raw);
                    JSONObject phData_Glob = util.getPhData(ph_Raw);
                    JSONObject phMin_Glob = util.getPhMin(ph_Raw);
                    JSONObject phMms_Glob = util.getPhMms(ph_Raw);
                    JSONObject phText_Glob = util.getPhText(ph_Raw);
                    JSONObject webData_Glob = util.getWebData(webData_Raw);
                    try { dynChart.LineChart(gasMcf_Glob); returnData += "Chart generated - Gas!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(kWhU_Glob); returnData += "Chart generated - Electricity!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(phData_Glob); returnData += "Chart generated - Phone Data Use!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(phMin_Glob); returnData += "Chart generated - Phone Minutes!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(phMms_Glob); returnData += "Chart generated - Phone MMS Messages!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(phText_Glob); returnData += "Chart generated - Phone Text Messages!\n"; } catch (Exception e) { e.printStackTrace(); }
                    try { dynChart.LineChart(webData_Glob); returnData += "Chart generated - Web Data Use!\n"; } catch (Exception e) { e.printStackTrace(); }
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
                    JSONObject cf6cpc_Glob = wx.getCf6cpc(cf6Data, cf6ChDateStart, cf6ChDateEnd);
                    JSONObject cf6Depart_Glob = wx.getCf6depart(cf6Data, cf6ChDateStart, cf6ChDateEnd);
                    JSONObject cf6Temps_Glob = wx.getCf6temps(cf6Data, cf6ChDateStart, cf6ChDateEnd);
                    try { dynChart.LineChart(cf6cpc_Glob); returnData += "Chart generated - cf6cpc!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(cf6Depart_Glob); returnData += "Chart generated - cf6Depart!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    try { dynChart.LineChart(cf6Temps_Glob); returnData += "Chart generated - cf6Temps!\n"; } catch (Exception e) { e.printStackTrace(); } 
                    break;
                    
                case "WeatherModelCharts":
                    genericCharts = false;
                    JSONArray lastRun = getWeatherAction.getJsonModelLast(dbc);
                    JSONArray hourSet = getWeatherAction.getGfsFha(dbc);
                    JSONObject lastRunObj = lastRun.getJSONObject(0);
                    String lastRunString = lastRunObj.getString("RunString");
                    if(wc.isSet(argsInForm.getFirstValue("modelRunString"))) { lastRunString = argsInForm.getFirstValue("modelRunString"); }
                    try {
                        qParams.add(0, lastRunString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray modelData = getWeatherAction.getJsonModelData(dbc, qParams);
                    JSONObject mosTemps_Glob = wx.getMosTemps(modelData, hourSet);
                    try {
                        dynChart.LineChart(mosTemps_Glob);
                        returnData += "DEBUG: " + mosTemps_Glob.getJSONArray("debug").toString() +
                            "\nChart generated - mosTemps!\n";
                    } catch (Exception e) { e.printStackTrace(); } 
                    // Troubleshoot 5/23/18
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