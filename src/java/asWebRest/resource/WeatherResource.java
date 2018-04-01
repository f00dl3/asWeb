/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 1 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetSnmpAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.dao.SnmpDAO;
import asWebRest.dao.WeatherDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class WeatherResource extends ServerResource {
    
    @Post
    public String represent(Representation argsIn) {

        List<String> qParams = new ArrayList<>();      
        List<String> inParams = new ArrayList<>();      
        JSONObject mergedResults = new JSONObject();
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch (doWhat) {
                
                case "getObjsJson":
                    inParams.add(0, "DESC");
                    try {
                        qParams.add(0, argsInForm.getFirstValue("startTime"));
                        qParams.add(1, argsInForm.getFirstValue("endTime"));
                        qParams.add(2, argsInForm.getFirstValue("limit"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray wxObs = getWeatherAction.getObsJson(qParams, inParams);
                    returnData = wxObs.toString();
                    break;
                    
                case "getObsJsonMerged":
                    try {
                        inParams.add(0, argsInForm.getFirstValue("startTime"));
                        inParams.add(1, argsInForm.getFirstValue("endTime"));
                        inParams.add(2, argsInForm.getFirstValue("order"));
                        inParams.add(3, argsInForm.getFirstValue("limit"));
                        inParams.add(4, argsInForm.getFirstValue("stationId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray wxObsB = getWeatherAction.getObsJsonByStation(inParams);
                    JSONArray latestObsB = getWeatherAction.getObsJsonLast();
                    JSONArray indorObs = getSnmpAction.getMergedLastTemp();
                    mergedResults
                        .put("wxObsM1H", wxObsB)
                        .put("wxObsNow", latestObsB)
                        .put("indoorObs", indorObs);
                    returnData = mergedResults.toString();
                    break;          
                    
                case "getObsJsonLast":
                    JSONArray latestObs = getWeatherAction.getObsJsonLast();
                    returnData = latestObs.toString();
                    break;
                    
            }
        } else {
            returnData = "ERROR: NO POST DATA!";
        }        
        
        return returnData;
        
    }
    
}
