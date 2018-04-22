/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 22 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetNewsFeedAction;
import asWebRest.action.GetSnmpAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.dao.NewsFeedDAO;
import asWebRest.dao.SnmpDAO;
import asWebRest.dao.WeatherDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
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
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

        List<String> qParams = new ArrayList<>();      
        List<String> inParams = new ArrayList<>();      
        JSONObject mergedResults = new JSONObject();
        
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
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
                
                case "getNewsEmail":
                    JSONArray newsFeeds = getNewsFeedAction.getNewsFeed(dbc);
                    returnData = newsFeeds.toString();
                    break;
                    
                case "getObjsJson":
                    inParams.add(0, "DESC");
                    try {
                        qParams.add(0, argsInForm.getFirstValue("startTime"));
                        qParams.add(1, argsInForm.getFirstValue("endTime"));
                        qParams.add(2, argsInForm.getFirstValue("limit"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray wxObs = getWeatherAction.getObsJson(dbc, qParams, inParams);
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
                    JSONArray wxObsB = getWeatherAction.getObsJsonByStation(dbc, inParams);
                    JSONArray latestObsB = getWeatherAction.getObsJsonLast(dbc);
                    JSONArray indorObs = getSnmpAction.getMergedLastTemp(dbc);
                    mergedResults
                        .put("wxObsM1H", latestObsB)
                        .put("wxObsNow", wxObsB)
                        .put("indoorObs", indorObs);
                    returnData = mergedResults.toString();
                    break;          
                    
                case "getObsJsonLast":
                    JSONArray latestObs = getWeatherAction.getObsJsonLast(dbc);
                    returnData = latestObs.toString();
                    break;
                    
                case "getSpcFeed":
                    qParams.add(0, "%");
                    qParams.add(1, "%");
                    JSONArray spcFeed = getWeatherAction.getSpcLive(dbc, qParams);
                    returnData = spcFeed.toString();
                    break;
                    
            }
        } else {
            returnData = "ERROR: NO POST DATA!";
        }        
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
