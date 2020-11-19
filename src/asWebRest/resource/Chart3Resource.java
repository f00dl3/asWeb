/*
by Anthony Stump
Created: 7 Oct 2020
Updated: 18 Nov 2020
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

import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.chartHelpers.Finance;
import asWebRest.chartHelpers.Weather;
import asWebRest.dao.FinanceDAO;
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
        
		Finance fin = new Finance();
        Weather wx = new Weather();
        
		GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());

        JSONArray enw_RawY = getFinanceAction.getEnwChart(dbc, "Year");
        JSONArray enw_RawR = getFinanceAction.getEnwChartRapid(dbc);
        
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
        	
            switch (doWhat) {    

		 		case "ObsJSONTempH":
                	WUndergroundBeans wub = new WUndergroundBeans();
                	String stationIdHome = wub.getStation_Home();
                    JSONArray wxObsBa2 = getWeatherAction.getObsJsonHome(dbc);
                    returnData = wx.getObsJsonTemps(wxObsBa2, stationIdHome).toString();
	    			break;
    			
		 		case "wFixed":
	    			returnData = fin.getFinEnw(enw_RawY, "Year", "F").toString();
	    			break;
    			
		 		case "wLiquid":
	    			returnData = fin.getFinEnw(enw_RawY, "Year", "L").toString();
	    			break;

		 		case "wTotal":
	    			returnData = fin.getFinEnw(enw_RawY, "Year", "T").toString();
	    			break;
	    			
    		 	case "wRapid": case "testData": default:
        			returnData = fin.getFinEnw(enw_RawR, "All", "R").toString();
        			break;
                      
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    
    }
    
}