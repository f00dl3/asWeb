/*
by Anthony Stump
Created: 19 Feb 2018
Split from FinanceResource 1 Mar 2020
Updated: 1 Mar 2020
 */

package asWebRest.resource;

import asWebRest.action.GetAutomotiveAction;
import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetUtilityUseAction;
import asWebRest.action.GetWebLinkAction;
import asWebRest.action.UpdateAutomotiveAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.dao.AutomotiveDAO;
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


public class AutomotiveResource extends ServerResource {
        
    @Post
    public String doPost(Representation argsIn) {

        GetAutomotiveAction getAutomotiveAction = new GetAutomotiveAction(new AutomotiveDAO());
        UpdateAutomotiveAction updateAutomotiveAction = new UpdateAutomotiveAction(new AutomotiveDAO());
        
        WebCommon wc = new WebCommon();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
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
                
                case "getAuto":
                    JSONArray autoMpg = getAutomotiveAction.getAutoMpg(dbc);
                    JSONArray autoMpgAvg = getAutomotiveAction.getAutoMpgAverage(dbc);
                    JSONArray billSum = getAutomotiveAction.getAutoBillSum(dbc);
                    JSONArray autoMaint = getAutomotiveAction.getAutoMaint(dbc);
                    mergedResults
                        .put("autoMpg", autoMpg)
                        .put("autoMpgAvg", autoMpgAvg)
                        .put("billSum", billSum)
                        .put("amrData", autoMaint);
                    returnData += mergedResults.toString();
                    break;

                case "getAutoHondaCivic":
                    JSONArray autoMpgHC = getAutomotiveAction.getAutoMpgHondaCivic(dbc);
                    JSONArray autoMpgAvgHC = getAutomotiveAction.getAutoMpgAverageHondaCivic(dbc);
                    JSONArray billSumHC = getAutomotiveAction.getAutoBillSumHondaCivic(dbc);
                    JSONArray autoMaintHC = getAutomotiveAction.getAutoMaintHondaCivic(dbc);
                    mergedResults
                        .put("autoMpg", autoMpgHC)
                        .put("autoMpgAvg", autoMpgAvgHC)
                        .put("billSum", billSumHC)
                        .put("amrData", autoMaintHC);
                    returnData += mergedResults.toString();
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
                    returnData += updateAutomotiveAction.setAutoMpgAdd(dbc, qParams);
                    break;

                case "putAutoMpgAddHondaCivic":
                    String aMpgDateHC = "0000-00-00";
                    String aMpgMilesHC = "0";
                    String aMpgPriceHC = "0.00";
                    String aMpgGallonsHC = "0.00";
                    if(wc.isSet(argsInForm.getFirstValue("mpgDate"))) { aMpgDateHC = argsInForm.getFirstValue("mpgDate"); }
                    if(wc.isSet(argsInForm.getFirstValue("mpgMiles"))) { aMpgMilesHC = argsInForm.getFirstValue("mpgMiles"); }
                    if(wc.isSet(argsInForm.getFirstValue("mpgPrice"))) { aMpgPriceHC = argsInForm.getFirstValue("mpgPrice"); }
                    if(wc.isSet(argsInForm.getFirstValue("mpgGallons"))) { aMpgGallonsHC = argsInForm.getFirstValue("mpgGallons"); }
                    qParams.add(0, aMpgDateHC);
                    qParams.add(1, aMpgMilesHC);
                    qParams.add(2, aMpgPriceHC);
                    qParams.add(3, aMpgGallonsHC);
                    returnData += updateAutomotiveAction.setAutoMpgAddHondaCivic(dbc, qParams);
                    break;
                    
                case "zestimateUpdate":
                	break;
                
            }
        }
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}