/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 28 Sep 2018
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
                
                case "getFfxivCrafting":
                    JSONArray ffxivC = getEntertainmentAction.getFfxivCrafting(dbc);
                    returnData += ffxivC.toString();
                    break;
                    
                case "getFfxivDungeons":
                    JSONArray ffxivD = getEntertainmentAction.getFfxivDungeons(dbc);
                    returnData += ffxivD.toString();
                    break;
                    
                case "getFfxivItems":
                    JSONArray ffxivI = getEntertainmentAction.getFfxivItems(dbc);
                    returnData += ffxivI.toString();
                    break;
                    
                case "getFfxivMerged":
                    JSONArray ffxivM = getEntertainmentAction.getFfxivMerged(dbc, 1, 9999, "%");
                    JSONArray ffxivCountsM = getEntertainmentAction.getFfxivCounts(dbc);
                    JSONArray ffxivIm = getEntertainmentAction.getFfxivImageMaps(dbc);
                    JSONArray ffxivE = getEntertainmentAction.getFfxivEmotes(dbc);
                    mergedResults
                        .put("ffxivMerged", ffxivM)
                        .put("ffxivCount", ffxivCountsM)
                        .put("ffxivImageMaps", ffxivIm)
                        .put("ffxivEmotes", ffxivE);
                    returnData += mergedResults.toString();
                    break;

                case "getFfxivMergedInRange":
                    int minLevelM = Integer.parseInt(argsInForm.getFirstValue("minLevel"));
                    int maxLevelM = Integer.parseInt(argsInForm.getFirstValue("maxLevel"));
                    JSONArray ffxivMr = getEntertainmentAction.getFfxivMerged(dbc, minLevelM, maxLevelM, "0");
                    JSONArray ffxivCounts = getEntertainmentAction.getFfxivCounts(dbc);
                    JSONArray ffxivImM = getEntertainmentAction.getFfxivImageMaps(dbc);
                    JSONArray ffxivEM = getEntertainmentAction.getFfxivEmotes(dbc);
                    mergedResults
                        .put("ffxivMerged", ffxivMr)
                        .put("ffxivCount", ffxivCounts)
                        .put("ffxivImageMaps", ffxivImM)
                        .put("ffxivEmotes", ffxivEM);
                    returnData += mergedResults.toString();
                    break;
                    
                case "getFfxivQuests":
                    JSONArray ffxivQ = getEntertainmentAction.getFfxivQuests(dbc, 1, 9999, "%");
                    returnData += ffxivQ.toString();
                    break;

                case "getFfxivQuestsInRange":
                    int minLevel = Integer.parseInt(argsInForm.getFirstValue("minLevel"));
                    int maxLevel = Integer.parseInt(argsInForm.getFirstValue("maxLevel"));
                    JSONArray ffxivQr = getEntertainmentAction.getFfxivQuests(dbc, minLevel, maxLevel, "0");
                    returnData += ffxivQr.toString();
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
                    
                case "setFfxivCraftingDone":
                    qParams.add(0, argsInForm.getFirstValue("recipieName"));
                    returnData += updateEntertainmentAction.setFfxivCraftingDone(dbc, qParams);
                    break;
                    
                case "setFfxivDungeonDone":
                    qParams.add(0, argsInForm.getFirstValue("dungeonCode"));
                    returnData += updateEntertainmentAction.setFfxivDungeonDone(dbc, qParams);
                    break;
                    
                case "setFfxivGatheringDone":
                    qParams.add(0, argsInForm.getFirstValue("gatherCode"));
                    returnData += updateEntertainmentAction.setFfxivGatheringDone(dbc, qParams);
                    break;
                    
                case "setFfxivHuntingDone":
                    qParams.add(0, argsInForm.getFirstValue("huntCode"));
                    returnData += updateEntertainmentAction.setFfxivHuntingDone(dbc, qParams);
                    break;
                    
                case "setFfxivQuestDone":
                    qParams.add(0, argsInForm.getFirstValue("questOrder"));
                    returnData += updateEntertainmentAction.setFfxivQuestDone(dbc, qParams);
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
