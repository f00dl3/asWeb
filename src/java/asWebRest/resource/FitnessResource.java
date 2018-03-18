/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 18 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.dao.FitnessDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class FitnessResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
                        
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
                    if(xdt1 != null && xdt2 != null) {
                        qParams.add(xdt1);
                        qParams.add(xdt2);
                        JSONArray allRecs = getFitnessAction.getAll(qParams);  
                        returnData += allRecs.toString();
                    } else {
                        returnData += "ERROR";
                    }
                    break;
                    
                case "getBike":
                    String bike = argsInForm.getFirstValue("Bicycle");
                    if(bike != null) {
                        JSONArray bkInf = getFitnessAction.getBike(bike);
                        returnData += bkInf.toString();
                    } else {
                        returnData += "ERROR";
                    }
                    break;
                    
                case "getBkStats":
                    String bikeStats = argsInForm.getFirstValue("Bicycle");
                    if(bikeStats != null) {
                        JSONArray bkStats = getFitnessAction.getBkStats(bikeStats);
                        returnData += bkStats.toString();
                    } else {
                        returnData += "ERROR";
                    }
                    break;
                    
                case "getCrsm":
                    JSONArray crsm = getFitnessAction.getCrsm();
                    returnData += crsm.toString();
                    break;
                    
                case "getDay":
                    JSONArray today = getFitnessAction.getDay();
                    returnData += today.toString();
                    break;
                    
                case "getOverallStats":
                    JSONArray overall = getFitnessAction.getOverallStats();
                    returnData += overall.toString();
                    break;
                    
                case "getPlans":
                    JSONArray plans = getFitnessAction.getRPlans();
                    returnData += plans.toString();
                    break;
                    
                case "getRShoe":
                    JSONArray rShoe = getFitnessAction.getRShoe();
                    returnData += rShoe.toString();
                    break;
                    
                case "getTot":
                    JSONArray tot = getFitnessAction.getTot();
                    returnData += tot.toString();
                    break;
                    
                case "getYear":
                    String tYear = argsInForm.getFirstValue("year");
                    if(tYear != null) {
                        JSONArray yData = getFitnessAction.getYear(tYear);
                        returnData += yData.toString();
                    } else {
                        returnData += "ERROR";
                    }
                    break;
            }
        }
        
        return returnData;
        
    }
        
}
