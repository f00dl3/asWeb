/*
by Anthony Stump
Created: 7 Oct 2020
Updated: 23 Dec 2020
 */

package asWebRest.resource;

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

import asWebRest.action.GetFfxivAction;
import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetFitnessAction;
import asWebRest.action.GetMediaServerAction;
import asWebRest.action.GetSnmpAction;
import asWebRest.action.GetUtilityUseAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.chartHelpers.Ffxiv;
import asWebRest.chartHelpers.Finance;
import asWebRest.chartHelpers.Fitness;
import asWebRest.chartHelpers.MediaServer;
import asWebRest.chartHelpers.SysMonDesktop;
import asWebRest.chartHelpers.Utilities;
import asWebRest.chartHelpers.Weather;
import asWebRest.dao.FfxivDAO;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.FitnessDAO;
import asWebRest.dao.MediaServerDAO;
import asWebRest.dao.SnmpDAO;
import asWebRest.dao.UtilityUseDAO;
import asWebRest.dao.WeatherDAO;
import asWebRest.hookers.Chart3Helpers;
import asWebRest.secure.WUndergroundBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class Chart3Resource extends ServerResource {
    
    @Post    
    public String doPost(Representation argsIn) {

        WebCommon wc = new WebCommon();
        Chart3Helpers c3h = new Chart3Helpers();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        String doWhat = null;
        String returnData = "";      
        List<String> inParams = new ArrayList<>();              
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);

        Ffxiv ffxiv = new Ffxiv();
		Finance fin = new Finance();
		Fitness fitness = new Fitness();
		MediaServer ms = new MediaServer();
        Utilities util = new Utilities();
        Weather wx = new Weather();

        GetFfxivAction getFfxivAction = new GetFfxivAction(new FfxivDAO());
		GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        GetMediaServerAction getMediaServerAction = new GetMediaServerAction(new MediaServerDAO());
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());

        JSONArray enw_RawA = getFinanceAction.getEnwChart(dbc, "All");
        JSONArray enw_RawY = getFinanceAction.getEnwChart(dbc, "Year");
        JSONArray enw_RawR = getFinanceAction.getEnwChartRapid(dbc);
        JSONArray ph_Raw = getUtilityUseAction.getChCellUse(dbc);
        JSONArray wxObsBa2 = getWeatherAction.getObsJsonHome(dbc);

    	WUndergroundBeans wub = new WUndergroundBeans();
    	String stationIdHome = wub.getStation_Home();
        
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
        	
            switch (doWhat) {    

	            case "Bills":
                    JSONArray bill_Raw = getFinanceAction.getBills(dbc);
                    returnData = fin.getBillCh(bill_Raw).toString();
	            	break;
	            	
	 			case "CalorieRange": 
	                qParams.add(argsInForm.getFirstValue("XDT1"));
	                qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jraCalorieRange = getFitnessAction.getChCaloriesR(dbc, qParams);
	                returnData = fitness.getCalCh(jraCalorieRange).toString();
	                break;
	                
	 			case "CellData": returnData = util.getPhData(ph_Raw).toString(); break;	                
	 			case "CellMin": returnData = util.getPhMin(ph_Raw).toString(); break;   
	 			case "CellMMS": returnData = util.getPhMms(ph_Raw).toString(); break;
	 			case "CellText": returnData = util.getPhText(ph_Raw).toString(); break;
                
	 			case "ffxivGilWorthByDay":
	                JSONArray gbd_Raw = getFfxivAction.getFfxivGilByDate(dbc);        
	 				returnData = ffxiv.getGilWorthByDate(gbd_Raw).toString();
	 				break;
	 				
	 			case "ffxivQuestsByDay":
	                JSONArray qbd_Raw = getFfxivAction.getFfxivQuestsByDate(dbc);        
	 				returnData = ffxiv.getByDate(qbd_Raw).toString();
	 				break;
				
	 			case "FinENW_All_A":
	 				returnData = fin.getFinEnw(enw_RawA, "All", "A").toString();
	 				break;
	 				
		 		case "FinENW_Year_A": 
		 			returnData = fin.getFinEnw(enw_RawY, "Year", "A").toString();
		 			break;
	    			
		 		case "msByDate":
                    JSONArray msbd = getMediaServerAction.getIndexedByDate(dbc);
                    returnData = ms.getByDate(msbd).toString();
		 			break;

		 		case "ObsJSONHumidityH": returnData = wx.getObsJsonHumidity(wxObsBa2, stationIdHome).toString(); break;	    			
		 		case "ObsJSONPrecipRateH": returnData = wx.getObsJsonPrecipRate(wxObsBa2, stationIdHome).toString(); break;			
		 		case "ObsJSONPressureH": returnData = wx.getObsJsonPressure(wxObsBa2, stationIdHome).toString(); break;
		 		case "ObsJSONTempH": returnData = wx.getObsJsonTemps(wxObsBa2, stationIdHome).toString(); break;
		 		case "ObsJSONWindH": returnData = wx.getObsJsonWind(wxObsBa2, stationIdHome).toString(); break;

	 			case "SleepRange": 
	                qParams.add(argsInForm.getFirstValue("XDT1"));
	                qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jraSleepRange = getFitnessAction.getChWeightR(dbc, qParams);
	                returnData = fitness.getSleepCh(jraSleepRange).toString();
	                break;
	                
	 			case "SysMonCharts": 
                    int intLen = 60 * 2;
	 		        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
                    SysMonDesktop smDesktop = new SysMonDesktop();
                    JSONArray mainGlob = new JSONArray();
                    final DateFormat wtf = new SimpleDateFormat("yyyyMMdd");
                    final Date nowTimestamp = new Date();
                    final String wtfDate = wtf.format(nowTimestamp);                    
                    int step = 1;
                    String stepIn = null;
                    String stepTest = "1";
                    String dateIn = null;
                    String dateTest = "1";        
                    String smcType = "";
                    try { 
                    	smcType = argsInForm.getFirstValue("type");
                        stepIn = argsInForm.getFirstValue("step");
                        dateIn = argsInForm.getFirstValue("date").replace("-", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(wc.isSet(stepIn) && !stepIn.equals("1")) { stepTest = "0"; }
                    if(wc.isSet(dateIn) && !dateIn.equals(wtfDate)) { dateTest = "0"; }                                       
                    qParams.add(0, dateTest); //DateTest
                    qParams.add(1, dateIn);      
                    try { mainGlob = getSnmpAction.getMain(dbc, qParams, step); } catch (Exception e) { e.printStackTrace(); }
                    switch(smcType) {
                    	case "mSysCPU": returnData = smDesktop.getSysCPU(mainGlob, intLen, step).toString(); break;
                    	case "mSysFans": returnData = smDesktop.getSysFans(mainGlob, intLen, step).toString(); break;
                    	case "mSysMemory": returnData = smDesktop.getSysMemory(mainGlob, intLen, step).toString(); break;
                    	case "mSysMySQLSize": returnData = smDesktop.getSysMySQLSize(mainGlob, intLen, step).toString(); break;
                    	case "mSysNet": returnData = smDesktop.getSysNet(mainGlob, intLen, step).toString(); break;
                    	case "mSysNumUsers": returnData = smDesktop.getSysNumUsers(mainGlob, intLen, step).toString(); break;
                    	case "mSysStorage": returnData = smDesktop.getSysStorage(mainGlob, intLen, step).toString(); break;
                    	case "mSysTemp": returnData = smDesktop.getSysTemp(mainGlob, intLen, step).toString(); break;
                    	case "mSysLoad": default: returnData = smDesktop.getSysLoad(mainGlob, intLen, step).toString(); break;
                    }
	 				break;
	                
	 			case "UseElecD":
                    JSONArray kWhU_Raw = getUtilityUseAction.getChUseElecD(dbc);
                    returnData = util.getKWhU(kWhU_Raw).toString();
	 				break;
	 				
	 			case "UseGas":
                    JSONArray gasMcf_Raw = getUtilityUseAction.getChUseGas(dbc);
                    returnData += util.getGasMcf(gasMcf_Raw).toString();
	 				break;

	 			case "WebData":
                    JSONArray webData_Raw = getUtilityUseAction.getChWebData(dbc);
                    returnData = util.getWebData(webData_Raw).toString(); 
                    break;	    
	 			
		 		case "WeightRange": 
                    qParams.add(argsInForm.getFirstValue("XDT1"));
                    qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jsonResultArray = getFitnessAction.getChWeightR(dbc, qParams);
                    returnData = fitness.getWeightCh(jsonResultArray).toString();
                    break;
                    
    		 	case "wRapid": case "testData": default: returnData = fin.getFinEnw(enw_RawR, "All", "R").toString(); break;

                case "WxObsCharts":
                    String stationId = "KOJC";
                    String wxocType = "";
                    try {
                    	wxocType = argsInForm.getFirstValue("type");
                        stationId = argsInForm.getFirstValue("stationId");
                        inParams.add(0, argsInForm.getFirstValue("startTime"));
                        inParams.add(1, argsInForm.getFirstValue("endTime"));
                        inParams.add(2, argsInForm.getFirstValue("order"));
                        inParams.add(3, argsInForm.getFirstValue("limit"));
                        inParams.add(4, argsInForm.getFirstValue("stationId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(wc.isSet(stationId)) {
                        JSONArray wxObsBS = getWeatherAction.getObsJsonByStation(dbc, inParams);
	                    switch(wxocType) {
	                    	case "humidity": returnData = wx.getObsJsonHumidity(wxObsBS, stationId).toString(); break;
	                    	case "pressure": returnData = wx.getObsJsonPressure(wxObsBS, stationId).toString(); break;
	                    	case "wind": returnData = wx.getObsJsonWind(wxObsBS, stationId).toString(); break;
	                    	case "temp": default: returnData = wx.getObsJsonTemps(wxObsBS, stationId).toString(); break;
	                    }
                    }
                    break;
                    
                case "WxObsChartsCF6":
                    String cf6ChDateStart = "";
                    String cf6ChDateEnd = "";
                    String cf6Type = "";
                    try {
                        cf6ChDateStart = argsInForm.getFirstValue("dateStart");
                        cf6ChDateEnd = argsInForm.getFirstValue("dateEnd");
                        cf6Type = argsInForm.getFirstValue("type"); 
                        qParams.add(0, cf6ChDateStart);
                        qParams.add(1, cf6ChDateEnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(wc.isSet(cf6ChDateStart) && wc.isSet(cf6ChDateEnd)) {
                    	JSONArray cf6Data = getWeatherAction.getCf6Main(dbc, qParams, "ASC");
                    	switch(cf6Type) {
                    		case "depart": returnData = wx.getCf6depart(cf6Data, cf6ChDateStart, cf6ChDateEnd).toString(); break;
                    		case "temps": default: returnData = wx.getCf6temps(cf6Data, cf6ChDateStart, cf6ChDateEnd).toString(); break;
                    	}
                    }
                    break;
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    
    }
    
}