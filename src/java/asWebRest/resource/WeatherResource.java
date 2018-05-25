/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 25 May 2018
 */

package asWebRest.resource;

import asWebRest.action.GetNewsFeedAction;
import asWebRest.action.GetSnmpAction;
import asWebRest.action.GetWeatherAction;
import asWebRest.action.UpdateWeatherAction;
import asWebRest.dao.NewsFeedDAO;
import asWebRest.dao.SnmpDAO;
import asWebRest.dao.WeatherDAO;
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

public class WeatherResource extends ServerResource {
    
    @Post
    public String represent(Representation argsIn) {
        
        WebCommon wc = new WebCommon();
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

        List<String> qParams = new ArrayList<>();      
        List<String> inParams = new ArrayList<>();      
        JSONObject mergedResults = new JSONObject();
        
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        UpdateWeatherAction updateWeatherAction = new UpdateWeatherAction(new WeatherDAO());
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
        String order = "DESC";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch (doWhat) {
                
                case "getCf6Data":
                    qParams.add(0, argsInForm.getFirstValue("CF6Search1"));
                    qParams.add(1, argsInForm.getFirstValue("CF6Search2"));
                    JSONArray cf6Data = getWeatherAction.getCf6Main(dbc, qParams, order);
                    returnData = cf6Data.toString();
                    break;
                
                case "getCf6Initial":
                    JSONArray alamanac = getWeatherAction.getAlmanac(dbc);
                    mergedResults
                        .put("almanac", alamanac);
                    returnData += mergedResults.toString();
                    break;
                    
                case "getEventData":
                    inParams.add(0, argsInForm.getFirstValue("eventSearchStart"));
                    inParams.add(1, argsInForm.getFirstValue("eventSearchEnd"));
                    JSONArray eventData = getWeatherAction.getStormReportsByDate(dbc, inParams);
                    returnData += eventData.toString();
                    break;
                    
                case "getHTrackLast":
                    JSONArray htLast = getWeatherAction.getHTrackLast(dbc);
                    returnData += htLast.toString();
                    break;
                    
                case "getLiveWarnings":
                    inParams.add(0, argsInForm.getFirstValue("xdt1"));
                    inParams.add(1, argsInForm.getFirstValue("xdt2"));
                    inParams.add(2, argsInForm.getFirstValue("xExp"));
                    inParams.add(3, argsInForm.getFirstValue("stationA"));
                    inParams.add(4, argsInForm.getFirstValue("idMatch"));
                    inParams.add(5, argsInForm.getFirstValue("limit"));
                    JSONArray liveWarnings = getWeatherAction.getLiveWarnings(dbc, inParams);
                    returnData += liveWarnings.toString();
                    break;
                    
                case "getMosData":
                    JSONArray lastRun = getWeatherAction.getJsonModelLast(dbc);
                    JSONObject lastRunObj = lastRun.getJSONObject(0);
                    String lastRunString = lastRunObj.getString("RunString");
                    try { if(wc.isSet(argsInForm.getFirstValue("RunStringOverride"))) {
                        lastRunString = argsInForm.getFirstValue("RunStringOverride");
                    } } catch (Exception e) { e.printStackTrace(); }
                    qParams.add(0, lastRunString);
                    JSONArray heights = getWeatherAction.getHeights(dbc);
                    JSONArray hours = getWeatherAction.getGfsFha(dbc);
                    JSONArray runs = getWeatherAction.getJsonModelRuns(dbc);
                    JSONArray jsonModelData = getWeatherAction.getJsonModelData(dbc, qParams);
                    mergedResults
                        .put("last", lastRun)
                        .put("heights", heights)
                        .put("hours", hours)
                        .put("runs", runs)
                        .put("jsonModelData", jsonModelData);
                    returnData = mergedResults.toString();
                    break;
                
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
                    
                case "setUpdateRainGauge":
                    qParams.add(0, argsInForm.getFirstValue("precip"));
                    qParams.add(1, argsInForm.getFirstValue("precip"));
                    returnData += updateWeatherAction.setUpdateRainGauge(dbc, qParams);
                    break;
                    
            }
        } else {
            returnData = "ERROR: NO POST DATA!";
        }        
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
