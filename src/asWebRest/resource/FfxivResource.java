/*
by Anthony Stump
Created: 20 Feb 2018
Split from EntertinmentResource.java on 14 Nov 2019
Updated: 16 Nov 2019
 */

package asWebRest.resource;

import asWebRest.action.GetFfxivAction;
import asWebRest.action.UpdateFfxivAction;
import asWebRest.dao.FfxivDAO;
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

public class FfxivResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetFfxivAction getFfxivAction = new GetFfxivAction(new FfxivDAO());
        UpdateFfxivAction updateFfxivAction = new UpdateFfxivAction(new FfxivDAO());
                        
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
                    JSONArray ffxivC = getFfxivAction.getFfxivCrafting(dbc);
                    returnData += ffxivC.toString();
                    break;
                    
                case "getFfxivDungeons":
                    JSONArray ffxivD = getFfxivAction.getFfxivDungeons(dbc);
                    returnData += ffxivD.toString();
                    break;
                    
                case "getFfxivItems":
                    JSONArray ffxivI = getFfxivAction.getFfxivItems(dbc);
                    returnData += ffxivI.toString();
                    break;
                    
                case "getFfxivMerged":
                    JSONArray ffxivM = getFfxivAction.getFfxivMerged(dbc, 1, 9999, "%");
                    JSONArray ffxivCountsM = getFfxivAction.getFfxivCounts(dbc);
                    JSONArray ffxivIm = getFfxivAction.getFfxivImageMaps(dbc);
                    JSONArray ffxivE = getFfxivAction.getFfxivEmotes(dbc);
                    JSONArray ffxivA = getFfxivAction.getFfxivAssets(dbc);
                    mergedResults
                        .put("ffxivMerged", ffxivM)
                        .put("ffxivCount", ffxivCountsM)
                        .put("ffxivImageMaps", ffxivIm)
                        .put("ffxivEmotes", ffxivE)
                        .put("ffxivAssets", ffxivA);
                    returnData += mergedResults.toString();
                    break;

                case "getFfxivMergedInRange":
                    int minLevelM = Integer.parseInt(argsInForm.getFirstValue("minLevel"));
                    int maxLevelM = Integer.parseInt(argsInForm.getFirstValue("maxLevel"));
                    JSONArray ffxivMr = getFfxivAction.getFfxivMerged(dbc, minLevelM, maxLevelM, "0");
                    JSONArray ffxivCounts = getFfxivAction.getFfxivCounts(dbc);
                    JSONArray ffxivImM = getFfxivAction.getFfxivImageMaps(dbc);
                    JSONArray ffxivEM = getFfxivAction.getFfxivEmotes(dbc);
                    JSONArray ffxivA2 = getFfxivAction.getFfxivAssets(dbc);
                    mergedResults
                        .put("ffxivMerged", ffxivMr)
                        .put("ffxivCount", ffxivCounts)
                        .put("ffxivImageMaps", ffxivImM)
                        .put("ffxivEmotes", ffxivEM)
                        .put("ffxivAssets", ffxivA2);
                    returnData += mergedResults.toString();
                    break;
                    
                case "getFfxivQuests":
                    JSONArray ffxivQ = getFfxivAction.getFfxivQuests(dbc, 1, 9999, "%");
                    returnData += ffxivQ.toString();
                    break;

                case "getFfxivQuestsInRange":
                    int minLevel = Integer.parseInt(argsInForm.getFirstValue("minLevel"));
                    int maxLevel = Integer.parseInt(argsInForm.getFirstValue("maxLevel"));
                    JSONArray ffxivQr = getFfxivAction.getFfxivQuests(dbc, minLevel, maxLevel, "0");
                    returnData += ffxivQr.toString();
                    break;
                    
                case "setFfxivAchievementDone":
                    qParams.add(0, argsInForm.getFirstValue("achievementCode"));
                    returnData += updateFfxivAction.setFfxivAchievementDone(dbc, qParams);
                    break;
                    
                case "setFfxivCraftingDone":
                    qParams.add(0, argsInForm.getFirstValue("recipieName"));
                    returnData += updateFfxivAction.setFfxivCraftingDone(dbc, qParams);
                    break;
                    
                case "setFfxivDungeonClear":
                    qParams.add(0, argsInForm.getFirstValue("dungeonCode"));
                    returnData += updateFfxivAction.setFfxivDungeonClear(dbc, qParams);
                    break;
                    
                case "setFfxivDungeonDone":
                    qParams.add(0, argsInForm.getFirstValue("dungeonCode"));
                    returnData += updateFfxivAction.setFfxivDungeonDone(dbc, qParams);
                    break;
                    
                case "setFfxivFateDone":
                    qParams.add(0, argsInForm.getFirstValue("fateCode"));
                    returnData += updateFfxivAction.setFfxivFateDone(dbc, qParams);
                    break;
                    
                case "setFfxivGatheringDone":
                    qParams.add(0, argsInForm.getFirstValue("gatherCode"));
                    returnData += updateFfxivAction.setFfxivGatheringDone(dbc, qParams);
                    break;
                    
                case "setFfxivGil":
                	qParams.add(0, argsInForm.getFirstValue("gil"));
                    returnData += updateFfxivAction.setFfxivGil(dbc, qParams);
                    break;     
                    
                case "setFfxivHuntingDone":
                    qParams.add(0, argsInForm.getFirstValue("huntCode"));
                    returnData += updateFfxivAction.setFfxivHuntingDone(dbc, qParams);
                    break;
                    
                case "setFfxivQuestDone":
                    qParams.add(0, argsInForm.getFirstValue("questOrder"));
                    returnData += updateFfxivAction.setFfxivQuestDone(dbc, qParams);
                    break;           	
                    
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
        
}
