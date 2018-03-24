/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 24 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetFitnessAction;
import asWebRest.action.UpdateFitnessAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.FitnessDAO;
import asWebRest.shared.WebCommon;
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
        
        WebCommon wc = new WebCommon();
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        UpdateFitnessAction updateFitnessAction = new UpdateFitnessAction(new FitnessDAO());
                        
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
                    
                case "putCalories":
                    // gonna be a bitch
                    String calAdd = null; if(wc.isSet(argsInForm.getFirstValue("Calories"))) { calAdd = argsInForm.getFirstValue("Calories"); } qParams.add(calAdd);
                    String fatAdd = null; if(wc.isSet(argsInForm.getFirstValue("Fat"))) { fatAdd = argsInForm.getFirstValue("Fat"); } qParams.add(fatAdd);
                    String protAdd = null; if(wc.isSet(argsInForm.getFirstValue("Protein"))) { protAdd = argsInForm.getFirstValue("Protein"); } qParams.add(protAdd);
                    String carbsAdd = null; if(wc.isSet(argsInForm.getFirstValue("Carbs"))) { carbsAdd = argsInForm.getFirstValue("Carbs"); } qParams.add(carbsAdd);
                    String cholestAdd = null; if(wc.isSet(argsInForm.getFirstValue("Cholest"))) { cholestAdd = argsInForm.getFirstValue("Cholest"); } qParams.add(cholestAdd);
                    String sodiumAdd = null; if(wc.isSet(argsInForm.getFirstValue("Sodium"))) { sodiumAdd = argsInForm.getFirstValue("Sodium"); } qParams.add(sodiumAdd);
                    String fiberAdd = null; if(wc.isSet(argsInForm.getFirstValue("Fiber"))) { fiberAdd = argsInForm.getFirstValue("Fiber"); } qParams.add(fiberAdd);
                    String sugarAdd = null; if(wc.isSet(argsInForm.getFirstValue("Sugar"))) { sugarAdd = argsInForm.getFirstValue("Sugar"); } qParams.add(sugarAdd);
                    String waterAdd = null; if(wc.isSet(argsInForm.getFirstValue("Water"))) { waterAdd = argsInForm.getFirstValue("Water"); } qParams.add(waterAdd);
                    String fruitsVeggiesAdd = null; if(wc.isSet(argsInForm.getFirstValue("FruitVeggie"))) { fruitsVeggiesAdd = argsInForm.getFirstValue("FruitVeggie"); } qParams.add(fruitsVeggiesAdd);
                    qParams.add(calAdd); qParams.add(fatAdd); qParams.add(protAdd); qParams.add(carbsAdd); qParams.add(cholestAdd);
                    qParams.add(sodiumAdd); qParams.add(fiberAdd); qParams.add(sugarAdd); qParams.add(waterAdd); qParams.add(fruitsVeggiesAdd);
                    returnData += updateFitnessAction.setCalories(qParams);
                    break;
                    
                case "putToday":
                    String todayWeight = null; if(wc.isSet(argsInForm.getFirstValue("TodayWeight"))) { todayWeight = argsInForm.getFirstValue("TodayWeight"); } qParams.add(todayWeight); 
                    String todayRunWalk = null; if(wc.isSet(argsInForm.getFirstValue("TodayRunWalk"))) { todayRunWalk = argsInForm.getFirstValue("TodayRunWalk"); } qParams.add(todayRunWalk);
                    String todayShoe = null; if(wc.isSet(argsInForm.getFirstValue("TodayShoe"))) { todayShoe = argsInForm.getFirstValue("TodayShoe"); } qParams.add(todayShoe);
                    String todayRSMile = null; if(wc.isSet(argsInForm.getFirstValue("TodayRSMile"))) { todayRSMile = argsInForm.getFirstValue("TodayRSMile"); } qParams.add(todayRSMile);
                    String todayCycling = null; if(wc.isSet(argsInForm.getFirstValue("TodayCycling"))) { todayCycling = argsInForm.getFirstValue("TodayCycling"); } qParams.add(todayCycling);
                    String todayBkStudT = null; if(wc.isSet(argsInForm.getFirstValue("TodayBkStudT"))) { todayBkStudT = argsInForm.getFirstValue("TodayBkStudT"); } qParams.add(todayBkStudT);
                    String todayReelMow = null; if(wc.isSet(argsInForm.getFirstValue("TodayReelMow"))) { todayReelMow = argsInForm.getFirstValue("TodayReelMow"); } qParams.add(todayReelMow);
                    String todayMowNotes = null; if(wc.isSet(argsInForm.getFirstValue("TodayMowNotes"))) { todayMowNotes = argsInForm.getFirstValue("TodayMowNotes"); } qParams.add(todayMowNotes);
                    String todayBicycle = null; if(wc.isSet(argsInForm.getFirstValue("TodayBicycle"))) { todayBicycle = argsInForm.getFirstValue("TodayBicycle"); } qParams.add(todayBicycle);
                    String todayCommonRoute = null; if(wc.isSet(argsInForm.getFirstValue("TodayCommonRoute"))) { todayCommonRoute = argsInForm.getFirstValue("TodayCommonRoute"); } qParams.add(todayCommonRoute);
                    String todayX = null; if(wc.isSet(argsInForm.getFirstValue("TodayX"))) { todayX = argsInForm.getFirstValue("TodayX"); } qParams.add(todayX);
                    qParams.add(todayWeight); qParams.add(todayRunWalk); qParams.add(todayShoe); qParams.add(todayRSMile);
                    qParams.add(todayCycling); qParams.add(todayBkStudT); qParams.add(todayReelMow); qParams.add(todayMowNotes);
                    qParams.add(todayBicycle); qParams.add(todayCommonRoute); qParams.add(todayX);
                    returnData += updateFitnessAction.setUpdateToday(qParams);
                    break;                    
            }
        }
        
        return returnData;
        
    }
        
}
