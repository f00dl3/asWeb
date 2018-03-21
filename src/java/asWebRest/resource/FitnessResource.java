/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 21 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetFitnessAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.FitnessDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class FitnessResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
                        
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
            switch (doWhat) {
                
                case "getAll": 
                    String xdt1 = argsInForm.getFirstValue("XDT1");
                    String xdt2 = argsInForm.getFirstValue("XDT2");
                    String bike = argsInForm.getFirstValue("Bicycle");
                    String year = argsInForm.getFirstValue("year");
                    if(xdt1 != null && xdt2 != null && bike != null && year != null) {
                        qParams.add(xdt1);
                        qParams.add(xdt2);
                        JSONArray allRecs = getFitnessAction.getAll(qParams);
                        JSONArray calories = getFitnessAction.getCalories();
                        JSONArray plans = getFitnessAction.getRPlans();
                        JSONArray today = getFitnessAction.getDay();
                        JSONArray bkInf = getFitnessAction.getBike(bike);
                        JSONArray bkStats = getFitnessAction.getBkStats(bike);
                        JSONArray overall = getFitnessAction.getOverallStats();
                        JSONArray crsm = getFitnessAction.getCrsm();
                        JSONArray rShoe = getFitnessAction.getRShoe();
                        JSONArray tot = getFitnessAction.getTot();
                        JSONArray yData = getFitnessAction.getYear(year);
                        JSONArray autoMpg = getFinanceAction.getAutoMpg();
                        mergedResults
                            .put("allRecs", allRecs)
                            .put("calories", calories)
                            .put("plans", plans)
                            .put("today", today)
                            .put("bkInf", bkInf)
                            .put("bkStats", bkStats)
                            .put("overall", overall)
                            .put("crsm", crsm)
                            .put("rShoe", rShoe)
                            .put("tot", tot)
                            .put("yData", yData)
                            .put("autoMpg", autoMpg);
                    } else {
                        returnData += "ERROR";
                    }
                    returnData += mergedResults.toString();
                    break;
                    
            }
        }
        
        return returnData;
        
    }
        
}
