/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 12 May 2019
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetUtilityUseAction;
import asWebRest.action.GetWebLinkAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.UtilityUseDAO;
import asWebRest.dao.WebLinkDAO;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import asUtilsPorts.CCImports;
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
        
        WebCommon wc = new WebCommon();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());
        GetWebLinkAction getWebLinkAction = new GetWebLinkAction(new WebLinkDAO());
        UpdateFinanceAction updateFinanceAction = new UpdateFinanceAction(new FinanceDAO());
        CCImports ccImports = new CCImports();
                        
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
                    
                case "getChecking":
                    JSONArray ckbk = getFinanceAction.getCkBk(dbc);
                    returnData += ckbk.toString();
                    break;
                    
                case "getCheckingDeep":
                    JSONArray deep = getFinanceAction.getCkBkComb(dbc);
                    returnData += deep.toString();
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
                    
                case "getUtils":
                    String month = argsInForm.getFirstValue("tMonth");
                    JSONArray uuRel = getWebLinkAction.getWebLinks(dbc, "FBook.php-UU");
                    JSONArray settingC = getFinanceAction.getSettingC(dbc);
                    JSONArray settingH = getFinanceAction.getSettingH(dbc);
                    JSONObject uuData = getUtilityUseAction.getCombinedUtilityUseByMonth(dbc, month);
                    mergedResults
                        .put("uuRel", uuRel)
                        .put("settingC", settingC)
                        .put("settingH", settingH)
                        .put("uuData", uuData);
                    returnData += mergedResults.toString();
                    break;
                    
                case "processDiscover":
                	ccImports.doDiscover();
                	break;
                	
                case "processOldNavy":
                	ccImports.doOldNavy();
                	break;
                    
                case "putAssetTrackUpdate":
                    qParams.add(argsInForm.getFirstValue("AssetValue"));
                    qParams.add(wc.basicInputFilter(argsInForm.getFirstValue("AssetNotes")));
                    qParams.add(wc.basicInputFilter(argsInForm.getFirstValue("AssetDescription")));
                    returnData += updateFinanceAction.setAssetTrackUpdate(dbc, qParams);
                    break;
                    
                case "putAutoMpgAdd":
                    String aMpgDate = "0000-00-00";
                    String aMpgMiles = "0";
                    String aMpgPrice = "0.00";
                    String aMpgGallons = "0.00";
                    if(wc.isSet(argsInForm.getFirstValue("mpgDate"))) { aMpgDate = argsInForm.getFirstValue("mpgDate"); }
                    if(wc.isSet(argsInForm.getFirstValue("mpgMiles"))) { aMpgMiles = argsInForm.getFirstValue("mpgMiles"); }
                    if(wc.isSet(argsInForm.getFirstValue("mpgPrice"))) { aMpgPrice = argsInForm.getFirstValue("mpgPrice"); }
                    if(wc.isSet(argsInForm.getFirstValue("mpgGallons"))) { aMpgGallons = argsInForm.getFirstValue("mpgGallons"); }
                    qParams.add(0, aMpgDate);
                    qParams.add(1, aMpgMiles);
                    qParams.add(2, aMpgPrice);
                    qParams.add(3, aMpgGallons);
                    returnData += updateFinanceAction.setAutoMpgAdd(dbc, qParams);
                    break;
                    
                case "putCheckbookAdd":
                    String ACkBank = "0000-00-00";
                    String ACkCredit = "0.00";
                    String ACkDebit = "0.00";
                    if(wc.isSet(argsInForm.getFirstValue("ACkBank"))) { ACkBank = argsInForm.getFirstValue("ACkBank"); }
                    if(wc.isSet(argsInForm.getFirstValue("ACkCred"))) { ACkCredit = argsInForm.getFirstValue("ACkCred"); }
                    if(wc.isSet(argsInForm.getFirstValue("ACkDebi"))) { ACkDebit = argsInForm.getFirstValue("ACkDebi"); }
                    qParams.add(0, ACkBank);
                    qParams.add(1, argsInForm.getFirstValue("ACkDate"));
                    qParams.add(2, wc.basicInputFilter(argsInForm.getFirstValue("ACkDesc")));
                    qParams.add(3, ACkDebit);
                    qParams.add(4, ACkCredit);
                    returnData += updateFinanceAction.setCheckbookAdd(dbc, qParams);
                    break;
                    
                case "putCheckbookUpdate":
                    String CkBkBank = "0000-00-00";
                    String CkBkCredit = "0.00";
                    String CkBkDebit = "0.00";
                    if(wc.isSet(argsInForm.getFirstValue("CkBkBank"))) { CkBkBank = argsInForm.getFirstValue("CkBkBank"); }
                    if(wc.isSet(argsInForm.getFirstValue("CkBkCred"))) { CkBkCredit = argsInForm.getFirstValue("CkBkCred"); }
                    if(wc.isSet(argsInForm.getFirstValue("CkBkDebi"))) { CkBkDebit = argsInForm.getFirstValue("CkBkDebi"); }
                    qParams.add(0, CkBkBank);
                    qParams.add(1, argsInForm.getFirstValue("CkBkDate"));
                    qParams.add(2, wc.basicInputFilter(argsInForm.getFirstValue("CkBkDesc")));
                    qParams.add(3, CkBkDebit);
                    qParams.add(4, CkBkCredit);
                    qParams.add(5, argsInForm.getFirstValue("CkBkID"));
                    returnData += updateFinanceAction.setCheckbookUpdate(dbc, qParams);
                    break;
                    
                case "putDecorToolsUpdate":
                    qParams.add(argsInForm.getFirstValue("DTQuantity"));
                    qParams.add(wc.basicInputFilter(argsInForm.getFirstValue("DTLocation")));
                    qParams.add(wc.basicInputFilter(argsInForm.getFirstValue("DTDescription")));
                    returnData += updateFinanceAction.setDecorToolsUpdate(dbc, qParams);
                    break;
                                        
                case "putSavingsAdd":
                    String ASvCredit = "0.00";
                    String ASvDebit = "0.00";
                    if(wc.isSet(argsInForm.getFirstValue("ASvCred"))) { ASvCredit = argsInForm.getFirstValue("ASvCred"); }
                    if(wc.isSet(argsInForm.getFirstValue("ASvDebi"))) { ASvDebit = argsInForm.getFirstValue("ASvDebi"); }
                    qParams.add(argsInForm.getFirstValue("ASvDate"));
                    qParams.add(wc.basicInputFilter(argsInForm.getFirstValue("ASvDesc")));
                    qParams.add(ASvDebit);
                    qParams.add(ASvCredit);
                    returnData += updateFinanceAction.setSavingsAdd(dbc, qParams);
                    break;
                
            }
        }
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}