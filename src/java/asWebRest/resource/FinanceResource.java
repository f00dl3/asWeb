/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.dao.FinanceDAO;
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

public class FinanceResource extends ServerResource {
        
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
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
                    JSONArray assets = getFinanceAction.getAssetTrack(dbc);
                    JSONArray bGames = getFinanceAction.getBGames(dbc);
                    JSONArray books = getFinanceAction.getBooks(dbc);
                    JSONArray dTools = getFinanceAction.getDecorTools(dbc);
                    JSONArray licenses = getFinanceAction.getLicenses(dbc);
                    JSONArray qMerged = getFinanceAction.getQMerged(dbc);
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
                    JSONArray autoMpg = getFinanceAction.getAutoMpg(dbc);
                    JSONArray autoMpgAvg = getFinanceAction.getAutoMpgAverage(dbc);
                    JSONArray billSum = getFinanceAction.getAutoBillSum(dbc);
                    JSONArray autoMaint = getFinanceAction.getAutoMaint(dbc);
                    mergedResults
                        .put("autoMpg", autoMpg)
                        .put("autoMpgAvg", autoMpgAvg)
                        .put("billSum", billSum)
                        .put("amrData", autoMaint);
                    returnData += mergedResults.toString();
                    break;
                    
                case "getBills":
                    JSONArray bills = getFinanceAction.getBills(dbc);
                    returnData += bills.toString();
                    break;
                    
                case "getOverview":
                    JSONArray amSch = getFinanceAction.getAmSch(dbc);
                    JSONArray checking = getFinanceAction.getChecking(dbc);
                    JSONArray saving = getFinanceAction.getSaving(dbc);
                    JSONArray svBk = getFinanceAction.getSvBk(dbc);
                    JSONArray mort = getFinanceAction.getMort(dbc);
                    JSONArray enw = getFinanceAction.getEnw(dbc);
                    JSONArray x3nw = getFinanceAction.get3NetWorth(dbc);
                    JSONArray nwga = getFinanceAction.getNwga(dbc);
                    JSONArray enwt = getFinanceAction.getEnwt(dbc);
                    mergedResults
                        .put("amSch", amSch)
                        .put("checking", checking)
                        .put("saving", saving)
                        .put("svBk", svBk)
                        .put("mort", mort)
                        .put("enw", enw)
                        .put("x3nw", x3nw)
                        .put("nwga", nwga)
                        .put("enwt", enwt);
                    returnData += mergedResults.toString();
                    break;
                    
                case "putAssetTrackUpdate":
                    qParams.add(argsInForm.getFirstValue("AssetValue"));
                    qParams.add(argsInForm.getFirstValue("AssetNotes"));
                    qParams.add(argsInForm.getFirstValue("AssetDescription"));
                    returnData += updateFinanceAction.setAssetTrackUpdate(dbc, qParams);
                    break;
                    
                case "putDecorToolsUpdate":
                    qParams.add(argsInForm.getFirstValue("DTQuantity"));
                    qParams.add(argsInForm.getFirstValue("DTLocation"));
                    qParams.add(argsInForm.getFirstValue("DTDescription"));
                    returnData += updateFinanceAction.setDecorToolsUpdate(dbc, qParams);
                    break;
            }
        }
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
