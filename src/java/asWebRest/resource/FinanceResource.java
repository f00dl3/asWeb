/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 28 Mar 2018
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
                
                case "getFinanceData":
                    JSONArray assets = getFinanceAction.getAssetTrack();
                    JSONArray bGames = getFinanceAction.getBGames();
                    JSONArray books = getFinanceAction.getBooks();
                    JSONArray dTools = getFinanceAction.getDecorTools();
                    JSONArray licenses = getFinanceAction.getLicenses();
                    JSONArray qBGames = getFinanceAction.getQBGames();
                    JSONArray qBooks = getFinanceAction.getQBooks();
                    JSONArray qdTools = getFinanceAction.getQDecorTools();
                    JSONArray qLicenses = getFinanceAction.getQLicenses();
                    JSONArray qMedia = getFinanceAction.getQMedia();
                    mergedResults
                        .put("assets", assets)
                        .put("bGames", bGames)
                        .put("books", books)
                        .put("dTools", dTools)
                        .put("licenses", licenses)
                        .put("qBGames", qBGames)
                        .put("qBooks", qBooks)
                        .put("qdTools", qdTools)
                        .put("qLicenses", qLicenses)
                        .put("qMedia", qMedia);
                    returnData += mergedResults.toString();
                    break;
                
                case "getAutoMpg":
                    JSONArray autoMpg = getFinanceAction.getAutoMpg();
                    returnData += autoMpg.toString();
                    break;
                    
                case "putAssetTrackUpdate":
                    qParams.add(argsInForm.getFirstValue("AssetValue"));
                    qParams.add(argsInForm.getFirstValue("AssetNotes"));
                    qParams.add(argsInForm.getFirstValue("AssetDescription"));
                    returnData += updateFinanceAction.setAssetTrackUpdate(qParams);
                    break;
                    
            }
        }
    
        return returnData;
        
    }
    
}
