/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 4 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.dao.FinanceDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class FinanceResource extends ServerResource {
    
    @Get
    public String represent() {
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        JSONArray amSch = getFinanceAction.getAmSch();  
        return amSch.toString();
    }
    
        
    @Post
    public String doPost(Representation argsIn) {
        
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        UpdateFinanceAction updateFinanceAction = new UpdateFinanceAction(new FinanceDAO());
                        
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
                
                case "getAssetData":
                    JSONArray assets = getFinanceAction.getAssetTrack();
                    JSONArray bGames = getFinanceAction.getBGames();
                    JSONArray books = getFinanceAction.getBooks();
                    JSONArray dTools = getFinanceAction.getDecorTools();
                    JSONArray licenses = getFinanceAction.getLicenses();
                    JSONArray qMerged = getFinanceAction.getQMerged();
                    mergedResults
                        .put("assets", assets)
                        .put("bGames", bGames)
                        .put("books", books)
                        .put("dTools", dTools)
                        .put("licenses", licenses)
                        .put("qMerged", qMerged);
                    returnData += mergedResults.toString();
                    break;
                
                case "getAuto":
                    JSONArray autoMpg = getFinanceAction.getAutoMpg();
                    JSONArray autoMpgAvg = getFinanceAction.getAutoMpgAverage();
                    JSONArray billSum = getFinanceAction.getAutoBillSum();
                    JSONArray autoMaint = getFinanceAction.getAutoMaint();
                    mergedResults
                        .put("autoMpg", autoMpg)
                        .put("autoMpgAvg", autoMpgAvg)
                        .put("billSum", billSum)
                        .put("amrData", autoMaint);
                    returnData += mergedResults.toString();
                    break;
                    
                case "getBills":
                    JSONArray bills = getFinanceAction.getBills();
                    returnData += bills.toString();
                    break;
                    
                case "putAssetTrackUpdate":
                    qParams.add(argsInForm.getFirstValue("AssetValue"));
                    qParams.add(argsInForm.getFirstValue("AssetNotes"));
                    qParams.add(argsInForm.getFirstValue("AssetDescription"));
                    returnData += updateFinanceAction.setAssetTrackUpdate(qParams);
                    break;
                    
                case "putDecorToolsUpdate":
                    qParams.add(argsInForm.getFirstValue("DTQuantity"));
                    qParams.add(argsInForm.getFirstValue("DTLocation"));
                    qParams.add(argsInForm.getFirstValue("DTDescription"));
                    returnData += updateFinanceAction.setDecorToolsUpdate(qParams);
                    break;
            }
        }
    
        return returnData;
        
    }
    
}
