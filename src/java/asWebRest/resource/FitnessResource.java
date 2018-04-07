/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 4 Apr 2018
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
                        JSONArray autoMpg = getFinanceAction.getAutoMpgAverage();
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
                    String tempReturn = "";
                    JSONObject callbackData = new JSONObject();
                    double calsTotalSub = 0;
                    double fatTotalSub = 0;
                    double cholestTotalSub = 0;
                    double sodiumTotalSub = 0;
                    double carbsTotalSub = 0;
                    double fiberTotalSub = 0;
                    double sugarTotalSub = 0;
                    double proteinTotalSub = 0;
                    double waterTotalSub = 0;
                    double fruitVeggieTotalSub = 0;
                    double totalLength = argsInForm.size()/13;
                    for(int i=0; i<=totalLength; i++) {
                        if(wc.isSet(argsInForm.getFirstValue("Quantity["+i+"]")) && Double.parseDouble(argsInForm.getFirstValue("Quantity["+i+"]")) > 0) {
                            double thisServings = Double.parseDouble(argsInForm.getFirstValue("Quantity["+i+"]"));
                            List<String> qParamsPre = new ArrayList<>();
                            qParamsPre.add(argsInForm.getFirstValue("Quantity["+i+"]"));
                            qParamsPre.add(argsInForm.getFirstValue("FoodDescription["+i+"]"));
                            tempReturn += updateFitnessAction.setCaloriesSingle(qParamsPre) + ", ";
                            calsTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Calories["+i+"]"));
                            fatTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Fat["+i+"]"));
                            cholestTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Cholest["+i+"]"));
                            sodiumTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Sodium["+i+"]"));
                            carbsTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Carbs["+i+"]"));
                            fiberTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Fiber["+i+"]"));
                            sugarTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Sugar["+i+"]"));
                            proteinTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Protein["+i+"]"));
                            waterTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("Water["+i+"]"));
                            fruitVeggieTotalSub += thisServings * Double.parseDouble(argsInForm.getFirstValue("FruitVeggie["+i+"]"));
                        }
                    }
                    callbackData.put("totCal", calsTotalSub);
                    qParams.add(Double.toString(calsTotalSub));
                    qParams.add(Double.toString(fatTotalSub));
                    qParams.add(Double.toString(proteinTotalSub));
                    qParams.add(Double.toString(carbsTotalSub));
                    qParams.add(Double.toString(cholestTotalSub));
                    qParams.add(Double.toString(sodiumTotalSub));
                    qParams.add(Double.toString(fiberTotalSub));
                    qParams.add(Double.toString(sugarTotalSub));
                    qParams.add(Double.toString(waterTotalSub));
                    qParams.add(Double.toString(fruitVeggieTotalSub));
                    qParams.add(Double.toString(calsTotalSub));
                    qParams.add(Double.toString(fatTotalSub));
                    qParams.add(Double.toString(proteinTotalSub));
                    qParams.add(Double.toString(carbsTotalSub));
                    qParams.add(Double.toString(cholestTotalSub));
                    qParams.add(Double.toString(sodiumTotalSub));
                    qParams.add(Double.toString(fiberTotalSub));
                    qParams.add(Double.toString(sugarTotalSub));
                    qParams.add(Double.toString(waterTotalSub));
                    qParams.add(Double.toString(fruitVeggieTotalSub));
                    tempReturn += ", FINAL " + updateFitnessAction.setCalories(qParams);
                    JSONObject returnText = new JSONObject();
                    returnText.put("ReturnData", tempReturn);
                    mergedResults
                        .put("returnText", returnText)
                        .put("callbackData", callbackData);
                    returnData += mergedResults.toString();
                    break;
                    
                case "putRoute":
                    JSONArray routeDone = new JSONArray();
                    String queryFeedback = "";
                    double totalRouteLength = argsInForm.size()/4;
                    for(int i=0; i<=totalRouteLength; i++) {
                        if(wc.isSet(argsInForm.getFirstValue("RouteSetCommit["+i+"]")) && argsInForm.getFirstValue("RouteSetCommit["+i+"]").equals("Yes")) {
                            String activityType = argsInForm.getFirstValue("RouteType["+i+"]");
                            String routeDescription = argsInForm.getFirstValue("RouteDescription["+i+"]");
                            List<String> qParamA = new ArrayList<>();
                            List<String> qParamB = new ArrayList<>();
                            switch(activityType) {
                                case "CycGeoJSON":
                                    qParamA.add(routeDescription);
                                    qParamB.add(routeDescription);
                                    queryFeedback += updateFitnessAction.setPlanCyc(qParamA);
                                    queryFeedback += updateFitnessAction.setPlanMark(qParamB);
                                    break;
                                case "RunGeoJSON":
                                    qParamA.add(routeDescription);
                                    qParamB.add(routeDescription);
                                    queryFeedback += updateFitnessAction.setPlanRun(qParamA);
                                    queryFeedback += updateFitnessAction.setPlanMark(qParamB);
                                    break;
                            }
                            routeDone.put(routeDescription);
                        }
                    }
                    mergedResults
                        .put("queryFeedback", queryFeedback)
                        .put("routesDone", routeDone);
                    returnData += mergedResults.toString();
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
                    String todayX = null; String todayVomit = null;
                    if(wc.isSet(argsInForm.getFirstValue("TodayX"))) {
                        String variableField = argsInForm.getFirstValue("TodayX");
                        if(variableField.contains("VO")) {
                            todayVomit = "1";
                            todayX = variableField.replace("VO", "");
                        } else {
                            todayVomit = "0";
                            todayX = variableField;
                        }
                    }
                    qParams.add(todayX); qParams.add(todayVomit);
                    qParams.add(todayWeight); qParams.add(todayRunWalk); qParams.add(todayShoe); qParams.add(todayRSMile);
                    qParams.add(todayCycling); qParams.add(todayBkStudT); qParams.add(todayReelMow); qParams.add(todayMowNotes);
                    qParams.add(todayBicycle); qParams.add(todayCommonRoute); qParams.add(todayX); qParams.add(todayVomit);
                    returnData += updateFitnessAction.setUpdateToday(qParams);
                    break;                    
            }
        }
        
        return returnData;
        
    }
        
}
