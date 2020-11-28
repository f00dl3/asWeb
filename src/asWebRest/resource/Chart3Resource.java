/*
by Anthony Stump
Created: 7 Oct 2020
Updated: 28 Nov 2020
 */

package asWebRest.resource;

import java.sql.Connection;
import java.util.ArrayList;
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
import asWebRest.action.GetUtilityUseAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.chartHelpers.Ffxiv;
import asWebRest.chartHelpers.Finance;
import asWebRest.chartHelpers.Fitness;
import asWebRest.chartHelpers.MediaServer;
import asWebRest.chartHelpers.Utilities;
import asWebRest.chartHelpers.Weather;
import asWebRest.dao.FfxivDAO;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.FitnessDAO;
import asWebRest.dao.MediaServerDAO;
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
                      
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    
    }
    
}