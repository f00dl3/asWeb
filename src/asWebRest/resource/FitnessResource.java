/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 19 Feb 2019
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetFitnessAction;
import asWebRest.action.UpdateFitnessAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.FitnessDAO;
import asWebRest.shared.JsonWorkers;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
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
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        JsonWorkers jw = new JsonWorkers();
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
                        JSONArray allRecs = getFitnessAction.getAll(dbc, qParams);
                        JSONArray calories = getFitnessAction.getCalories(dbc);
                        JSONArray plans = getFitnessAction.getRPlans(dbc);
                        JSONArray today = getFitnessAction.getDay(dbc);
                        JSONArray bkInf = getFitnessAction.getBike(dbc, bike);
                        JSONArray bkStats = getFitnessAction.getBkStats(dbc, bike);
                        JSONArray overall = getFitnessAction.getOverallStats(dbc);
                        JSONArray crsm = getFitnessAction.getCrsm(dbc);
                        JSONArray rShoe = getFitnessAction.getRShoe(dbc);
                        JSONArray tot = getFitnessAction.getTot(dbc);
                        JSONArray yData = getFitnessAction.getYear(dbc, year);
                        JSONArray autoMpg = getFinanceAction.getAutoMpgAverage(dbc);
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
                    
                case "getFitnessAsStore":
                    qParams.add(0, "2017-06-14");
                    qParams.add(1, "2018-06-14");
                    JSONArray allRecs = getFitnessAction.getAll(dbc, qParams);
                    returnData += jw.getDesiredDataType(allRecs, "dataStore", "Date");
                    break;
                    
                case "getGpsJson":
                    String actType = argsInForm.getFirstValue("activity");
                    String actDate = argsInForm.getFirstValue("logDate");
                    JSONArray gpsData = new JSONArray();
                    JSONArray oaStats = getFitnessAction.getOverallStats(dbc);
                    JSONArray oaSensors = getFitnessAction.getOverallSensors(dbc);
                    JSONArray fitToday = getFitnessAction.getDay(dbc);
                    if(actDate != null && actType != null) {
                        qParams.add(0, actDate);
                        JSONArray relatedPhotos = getFitnessAction.getRelatedPhotos(dbc, qParams);
                        switch(actType) {
                            case "Run": gpsData = getFitnessAction.getJsonLogRun(dbc, qParams); break;
                            case "Ru2": gpsData = getFitnessAction.getJsonLogRun2(dbc, qParams); break;
                            case "Ru3": gpsData = getFitnessAction.getJsonLogRun3(dbc, qParams); break;
                            case "Ru4": gpsData = getFitnessAction.getJsonLogRun4(dbc, qParams); break;
                            case "Cyc": gpsData = getFitnessAction.getJsonLogCyc(dbc, qParams); break;
                            case "Cy2": gpsData = getFitnessAction.getJsonLogCyc2(dbc, qParams); break;
                            case "Cy3": gpsData = getFitnessAction.getJsonLogCyc3(dbc, qParams); break;
                            case "Cy4": gpsData = getFitnessAction.getJsonLogCyc4(dbc, qParams); break;
                        }
                        mergedResults
                            .put("gpsLog", gpsData.toString())
                            .put("oaStats", oaStats.toString())
                            .put("oaSensors", oaSensors.toString())
                            .put("fitToday", fitToday.toString())
                            .put("relatedPhotos", relatedPhotos);
                        returnData += mergedResults;
                    } else {
                        returnData += "ERROR!";
                    }
                    break;
                    
                case "getOnlyFitnessGeoJSON": 
                    String xdt1a = argsInForm.getFirstValue("XDT1");
                    String xdt2a = argsInForm.getFirstValue("XDT2");
                    if(xdt1a != null && xdt2a != null) {
                        List<String> qParams2 = new ArrayList<>();
                        qParams.add(xdt1a);
                        qParams.add(xdt2a);
                        qParams2.add(xdt1a);
                        JSONArray relatedPhotos = getFitnessAction.getRelatedPhotos(dbc, qParams2);
                        JSONArray foj = getFitnessAction.getGeoJSON(dbc, qParams);
                        mergedResults
                            .put("foj", foj.toString())
                            .put("relatedPhotos", relatedPhotos);
                        returnData += mergedResults;
                    } else {
                        returnData += "ERROR!";
                    }
                    break;
                    
                case "getRouteHistory":
                    JSONArray routes = getFitnessAction.getAllRoutes(dbc);
                    returnData += routes;
                    break;
                    
                case "getRoutePlan":
                    qParams.add(0, argsInForm.getFirstValue("SearchString"));
                    JSONArray planned = getFitnessAction.getRPlanByDesc(dbc, qParams);
                    returnData += planned.toString();
                    break;
                    
                case "processGpsTracks":
                    returnData += "Processing GPS Tracks at /home/astump/Desktop !";
                    String[] args = {};
                    asUtilsPorts.GPSBulk.main(args);
                    returnData += "Completed!";
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
                    tempReturn += ", FINAL " + updateFitnessAction.setCalories(dbc, qParams);
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
                    String todayReelMow = null; if(wc.isSet(argsInForm.getFirstValue("TodayMowNotes"))) { todayReelMow = "1"; } qParams.add(todayReelMow);
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
                    String todaySleep = null; if(wc.isSet(argsInForm.getFirstValue("TodayEstHoursSleep"))) { todaySleep = argsInForm.getFirstValue("TodayEstHoursSleep"); } qParams.add(todaySleep);
                    String todayXO = null; if(wc.isSet(argsInForm.getFirstValue("TodayXO"))) { todayXO = argsInForm.getFirstValue("TodayXO"); } qParams.add(todayXO);
                    qParams.add(todayWeight); qParams.add(todayRunWalk); qParams.add(todayShoe); qParams.add(todayRSMile);
                    qParams.add(todayCycling); qParams.add(todayBkStudT); qParams.add(todayReelMow); qParams.add(todayMowNotes);
                    qParams.add(todayBicycle); qParams.add(todayCommonRoute); qParams.add(todayX); qParams.add(todayVomit); qParams.add(todaySleep); qParams.add(todayXO);
                    //System.out.println(qParams.toString());
                    returnData += updateFitnessAction.setUpdateToday(qParams);
                    break;                    
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
        
}
