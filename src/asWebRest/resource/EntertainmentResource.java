/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 14 Nov 2019
 */

package asWebRest.resource;

import asWebRest.action.GetEntertainmentAction;
import asWebRest.action.UpdateEntertainmentAction;
import asWebRest.dao.EntertainmentDAO;
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

public class EntertainmentResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetEntertainmentAction getEntertainmentAction = new GetEntertainmentAction(new EntertainmentDAO());
        UpdateEntertainmentAction updateEntertainmentAction = new UpdateEntertainmentAction(new EntertainmentDAO());
                        
        JSONObject mergedResults = new JSONObject();
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                
                case "getChicagoSeries":
                    JSONArray chi = getEntertainmentAction.getChicagoSeries(dbc);
                    returnData += chi.toString();
                    break;

                case "getDOSBoxMapping":
                    JSONArray dbm = getEntertainmentAction.getDOSBoxMapping(dbc);
                    returnData += dbm.toString();
                    break;
                    
                case "getGameData":
                    JSONArray ghTotal = getEntertainmentAction.getGameHoursTotal(dbc);
                    JSONArray ghLatest = getEntertainmentAction.getGameHoursLatest(dbc);
                    JSONArray gh = getEntertainmentAction.getGameHours(dbc);
                    mergedResults
                        .put("gameHoursTotal", ghTotal)
                        .put("gameHoursLatest", ghLatest)
                        .put("gameHours", gh);
                    returnData += mergedResults.toString();
                    break;
                    
                case "getGameIndex":
                    JSONArray gInd = getEntertainmentAction.getGameIndex(dbc);
                    returnData += gInd.toString();
                    break;
                    
                case "getGoosebumps":
                    JSONArray goose = getEntertainmentAction.getGoosebumpsBooks(dbc);
                    returnData += goose.toString();
                    break;
                    
                case "getLego":
                    JSONArray lego = getEntertainmentAction.getLego(dbc);
                    returnData += lego.toString();
                    break;
                    
                case "getPowerRangers":
                    JSONArray pr = getEntertainmentAction.getPowerRangers(dbc);
                    returnData += pr.toString();
                    break;
                    
                case "getRandomQuotes":
                    JSONArray rq = getEntertainmentAction.getRandomQuotes(dbc);
                    returnData += rq.toString();
                    break;
                    
                case "getStarTrek":
                    JSONArray st = getEntertainmentAction.getStarTrek(dbc);
                    returnData += st.toString();
                    break;
                    
                case "getTrueBlood":
                    JSONArray tb = getEntertainmentAction.getTrueBlood(dbc);
                    returnData += tb.toString();
                    break;
                    
                case "getXFiles":
                    JSONArray xf = getEntertainmentAction.getXFiles(dbc);
                    returnData += xf.toString();
                    break;
                    
                case "setPlayedGameHours":
                    qParams.add(0, argsInForm.getFirstValue("minutes"));
                    qParams.add(1, argsInForm.getFirstValue("game"));
                    returnData += updateEntertainmentAction.setPlayedGameHours(dbc, qParams);
                    break;
                    
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
        
}
