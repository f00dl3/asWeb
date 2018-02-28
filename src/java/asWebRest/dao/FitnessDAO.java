/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 26 Feb 2018
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FitnessDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getAll(List<String> qParams) {
        final String query_Fitness_All = "SELECT" +
                " f.Date, f.Weight, f.RunWalk, f.Shoe, f.RSMile, f.RunGeoJSON," +
                " f.Bicycle, f.Cycling, f.BkStudT, f.CycGeoJSON," +
                " f.AltGeoJSON, f.ReelMow, f.MowNotes, f.Calories," +
                " f.Fat, f.Protein, f.Carbs, f.Sugar, f.Fiber, f.Cholest, f.Sodium, f.Water, f.FruitsVeggies," +
                " f.TrackedTime, f.TrackedDist," +
                " f.CycSpeedAvg, f.CycSpeedMax, f.CycCadAvg, f.CycCadMax, f.CycPowerAvg, f.CycPowerMax, f.CycHeartAvg, f.CycHeartMax," +
                " f.RunSpeedAvg, f.RunSpeedMax, f.RunHeartAvg, f.RunHeartMax," +
                " f.Gym, f.GymWorkout, f.CommonRoute, f.xTags," +
                " cf6.High, cf6.Low, cf6.Average," +
                " CASE WHEN f.gpsLogCyc IS NOT NULL THEN true ELSE false END AS isGPSCycJSON," +
                " CASE WHEN f.gpsLogRun IS NOT NULL THEN true ELSE false END AS isGPSRunJSON," +
                " CASE WHEN f.gpsLogCyc2 IS NOT NULL THEN true ELSE false END AS isGPSCyc2JSON," +
                " CASE WHEN f.gpsLogRun2 IS NOT NULL THEN true ELSE false END AS isGPSRun2JSON" +
                " FROM Core.Fitness f" +
                " LEFT OUTER JOIN WxObs.CF6MCI cf6 ON f.Date = cf6.Date" +
                " WHERE f.Date BETWEEN ? AND ? " +
                " ORDER BY f.Date DESC LIMIT 0, 365;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_All, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("RunWalk", resultSet.getDouble("RunWalk"))
                    .put("Shoe", resultSet.getString("Shoe"))
                    .put("RSMile", resultSet.getString("RSMile"))
                    .put("RunGeoJSON", resultSet.getString("RunGeoJSON"))
                    .put("Bicycle", resultSet.getString("Bicycle"))
                    .put("Cycling", resultSet.getDouble("Cycling"))
                    .put("BkStudT", resultSet.getInt("BkStudT"))
                    .put("CycGeoJSON", resultSet.getString("CycGeoJSON"))
                    .put("AltGeoJSON", resultSet.getString("AltGeoJSON"))
                    .put("ReelMow", resultSet.getInt("ReelMow"))
                    .put("MowNotes", resultSet.getString("MowNotes"))
                    .put("Calories", resultSet.getInt("Calories"))
                    .put("Fat", resultSet.getInt("Fat"))
                    .put("Protein", resultSet.getInt("Protein"))
                    .put("Sugar", resultSet.getInt("Sugar"))
                    .put("Carbs", resultSet.getInt("Carbs"))
                    .put("Fiber", resultSet.getInt("Fiber"))
                    .put("Cholest", resultSet.getInt("Cholest"))
                    .put("Sodium", resultSet.getInt("Sodium"))
                    .put("Water", resultSet.getInt("Water"))
                    .put("FruitsVeggies", resultSet.getDouble("FruitsVeggies"))
                    .put("TrackedTime", resultSet.getInt("TrackedTime"))
                    .put("TrackedDist", resultSet.getDouble("TrackedDist"))
                    .put("CycSpeedAvg", resultSet.getDouble("CycSpeedAvg"))
                    .put("CycSpeedMax", resultSet.getDouble("CycSpeedMax"))
                    .put("CycCadAvg", resultSet.getDouble("CycCadAvg"))
                    .put("CycCadMax", resultSet.getDouble("CycCadMax"))
                    .put("CycPowerAvg", resultSet.getDouble("CycPowerAvg"))
                    .put("CycPowerMax", resultSet.getDouble("CycPowerMax"))
                    .put("CycHeartAvg", resultSet.getDouble("CycHeartAvg"))
                    .put("CycHeartMax", resultSet.getDouble("CycHeartMax"))
                    .put("RunSpeedAvg", resultSet.getDouble("RunSpeedAvg"))
                    .put("RunSpeedMax", resultSet.getDouble("RunSpeedMax"))
                    .put("RunHeartAvg", resultSet.getDouble("RunHeartAvg"))
                    .put("RunHeartMax", resultSet.getDouble("RunHeartMax"))
                    .put("Gym", resultSet.getInt("Gym"))
                    .put("GymWorkout", resultSet.getString("GymWorkout"))
                    .put("CommonRoute", resultSet.getInt("CommonRoute"))
                    .put("xTags", resultSet.getString("xTags"))
                    .put("High", resultSet.getInt("High"))
                    .put("Low", resultSet.getInt("Low"))
                    .put("Average", resultSet.getInt("Average"))
                    .put("isGpsCycJSON", resultSet.getString("isGpsCycJSON"))
                    .put("isGpsCyc2JSON", resultSet.getString("isGpsCyc2JSON"))
                    .put("isGpsRunJSON", resultSet.getString("isGpsRunJSON"))
                    .put("isGpsRun2JSON", resultSet.getString("isGpsRun2JSON"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAllE() {
        final String query_Fitness_AllE = "SELECT" +
                " Date, Weight, ExMin, Calories, Fat, Protein, Sugar, Carbs" +
                " Fiber, Cholest, Sodium, Water, FruitsVeggies" +
                " FROM Core.Fit_Em" +
                " ORDER BY Date DESC LIMIT 365;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_AllE, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("ExMin", resultSet.getInt("ExMin"))
                    .put("Calories", resultSet.getInt("Calories"))
                    .put("Fat", resultSet.getInt("Fat"))
                    .put("Protein", resultSet.getInt("Protein"))
                    .put("Sugar", resultSet.getInt("Sugar"))
                    .put("Carbs", resultSet.getInt("Carbs"))
                    .put("Fiber", resultSet.getInt("Fiber"))
                    .put("Cholest", resultSet.getInt("Cholest"))
                    .put("Sodium", resultSet.getInt("Sodium"))
                    .put("Water", resultSet.getInt("Water"))
                    .put("FruitsVeggies", resultSet.getDouble("FruitsVeggies"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public JSONArray getAllRoutes() {
        final String query_Fitness_AllRoutes = "SELECT" +
                " Date, Type, GeoJSON FROM (" +
                " SELECT Date, 'R' AS Type, RunGeoJSON as GeoJSON FROM Core.Fitness WHERE RunGeoJSON IS NOT NULL AND CommonRoute=0 UNION ALL" +
                " SELECT Date, 'C' AS Type, CycGeoJSON as GeoJSON FROM Core.Fitness WHERE CycGeoJSON IS NOT NULL AND CommonRoute=0 UNION ALL" +
                " SELECT Date, 'A' AS Type, AltGeoJSON as GeoJSON FROM Core.Fitness WHERE AltGeoJSON IS NOT NULL AND CommonRoute=0) as tmp;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_AllRoutes, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Type", resultSet.getString("Type"))
                    .put("GeoJSON", resultSet.getString("GeoJSON"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getBkStats(String bike) {
        final String query_Fitness_BkStats = "SELECT" +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNChain=1 AND Bicycle="+bike+") AS LastChain," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkCln=1 AND Bicycle="+bike+") AS LastCleaned," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkFlt=1 AND Bicycle="+bike+") AS LastFlat," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireF=1 AND Bicycle="+bike+") AS LastTireFront," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireFS=1) AS LastTireFrontStudded," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireR=1 AND Bicycle="+bike+") AS LastTireRear," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireRS=1) AS LastTireRearStudded," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelF=1 AND Bicycle="+bike+") AS LastWheelFront," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelR=1 AND Bicycle="+bike+") AS LastWheelRear," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkOH=1 AND Bicycle="+bike+") AS LastOverhaul," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNChain = 1) AND Bicycle="+bike+") AS MilesChain," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Bicycle="+bike+") AS MilesBike," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkOH = 1) AND Bicycle="+bike+") AS MilesOverhaul," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireF = 1) AND BkStudT = 0 AND Bicycle="+bike+") AS MilesTireFront," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireFS = 1) AND BkStudT = 1) AS MilesTireFrontStudded," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireR = 1) AND BkStudT = 0 AND Bicycle="+bike+") AS MilesTireRear," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireRS = 1) AND BkStudT = 1) AS MilesTireRearStudded," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelF = 1) AND Bicycle="+bike+") AS MilesWheelFront," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelR = 1) AND Bicycle="+bike+") AS MilesWheelRear" +
                " FROM Core.Fitness LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_BkStats, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("LastChain", resultSet.getString("LastChain"))
                    .put("LastCleaned", resultSet.getString("LastCleaned"))
                    .put("LastFlat", resultSet.getString("LastFlat"))
                    .put("LastTireFront", resultSet.getString("LastTireFront"))
                    .put("LastTireFrontStudded", resultSet.getString("LastTireFrontStudded"))
                    .put("LastTireRear", resultSet.getString("LastTireRear"))
                    .put("LastTireRearStudded", resultSet.getString("LastTireRearStudded"))
                    .put("LastWheelFront", resultSet.getString("LastWheelFront"))
                    .put("LastWheelRear", resultSet.getString("LastWheelRear"))
                    .put("LastOverhaul", resultSet.getString("LastOverhaul"))
                    .put("MilesChain", resultSet.getDouble("MilesChain"))
                    .put("MilesBike", resultSet.getDouble("MilesBike"))
                    .put("MilesOverhaul", resultSet.getDouble("MilesOverhaul"))
                    .put("MilesTireFront", resultSet.getDouble("MilesTireFront"))
                    .put("MilesTireFrontStudded", resultSet.getDouble("MilesTireFrontStudded"))
                    .put("MilesTireRear", resultSet.getDouble("MilesTireRear"))
                    .put("MilesTireRearStudded", resultSet.getDouble("MilesTireRearStudded"))
                    .put("MilesWheelFront", resultSet.getDouble("MilesWheelFront"))
                    .put("MilesWheelRear", resultSet.getDouble("MilesWheelRear"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getCalories() {
        final String query_Calories_Main = "SELECT Food, Serving, Calories, Carbs, Protein, Sugar," +
                " Sodium, Fat, Cholest, Fiber, Water, FruitVeggie, Last, ServingsLast, ServingsLastE, LastE" +
                " FROM Core.Fit_Calories;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Calories_Main, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Food", resultSet.getString("Food"))
                    .put("Serving", resultSet.getString("Serving"))
                    .put("Calories", resultSet.getInt("Calories"))
                    .put("Carbs", resultSet.getInt("Carbs"))
                    .put("Protein", resultSet.getInt("Protein"))
                    .put("Sugar", resultSet.getInt("Sugar"))
                    .put("Sodium", resultSet.getInt("Sugar"))
                    .put("Fat", resultSet.getInt("Fat"))
                    .put("Cholest", resultSet.getInt("Cholest"))
                    .put("Fiber", resultSet.getInt("Fiber"))
                    .put("Water", resultSet.getInt("Water"))
                    .put("FruitVeggie", resultSet.getDouble("FruitVeggie"))
                    .put("Last", resultSet.getString("Last"))
                    .put("ServingsLast", resultSet.getDouble("ServingsLast"))
                    .put("ServingsLastE", resultSet.getDouble("ServingsLastE"))
                    .put("LastE", resultSet.getString("LastE"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getCaloriesServingsLast(List<String> qParams) {
        final String query_Calories_ServingsLast = "SELECT ServingsLast, ServingsLastE FROM Core.Fit_Calories WHERE Last=CURDATE() AND Food=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Calories_ServingsLast, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("ServingsLast", resultSet.getDouble("ServingsLast"))
                    .put("ServingsLastE", resultSet.getDouble("ServingsLastE"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChCaloriesR(List<String> qParams) {
        final String query_ch_CaloriesR = "SELECT Date, Calories, Fat, Protein, Carbs" +
                " FROM Core.Fitness WHERE Date BETWEEN ? AND ? LIMIT 366;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_CaloriesR, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Calories", resultSet.getInt("Calories"))
                    .put("Fat", resultSet.getInt("Fat"))
                    .put("Protein", resultSet.getInt("Protein"))
                    .put("Carbs", resultSet.getInt("Carbs"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public JSONArray getChWeightA() {
        final String query_ch_WeightA = "SELECT Date, Weight FROM Core.Fitness ORDER BY Date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_WeightA, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChWeightR(List<String> qParams) {
        final String query_ch_WeightR = "SELECT Date, Weight FROM Fitness WHERE Date BETWEEN ? AND ? LIMIT 366;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_WeightR, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChWeightRE(List<String> qParams) {
        final String query_ch_WeightRE = "SELECT Date, Weight FROM Fit_Em WHERE Date BETWEEN ? AND ? LIMIT 366;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_WeightRE, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public JSONArray getCrsm() {
        final String query_Fitness_CRSM = "SELECT MAX(RSMile) AS CRSM FROM Core.Fitness WHERE Shoe = (SELECT MAX(Shoe) FROM Core.Fitness WHERE SHoe LIKE 'R%';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_CRSM, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("CRSM", resultSet.getDouble("CRSM"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDay() {
        final String query_Fitness_Day = "SELECT Weight, RunWalk, Shoe, RSMile, Cycling, Gym, GymWorkout, TrackedTime, TrackedDist, BkStudT, ReelMow, MowNotes, CommonRoute, xTags FROM Core.Fitness WHERE Date=CURDATE();";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_Day, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("RunWalk", resultSet.getDouble("RunWalk"))
                    .put("Shoe", resultSet.getString("Shoe"))
                    .put("RSMile", resultSet.getDouble("RSMile"))
                    .put("Cycling", resultSet.getDouble("Cycling"))
                    .put("Gym", resultSet.getInt("Gym"))
                    .put("GymWorkout", resultSet.getString("GymWorkout"))
                    .put("TrackedTime", resultSet.getInt("TrackedTime"))
                    .put("TrackedDist", resultSet.getDouble("TrackedDist"))
                    .put("BkStudT", resultSet.getInt("BkStudT"))
                    .put("ReelMow", resultSet.getInt("ReelMow"))
                    .put("MowNotes", resultSet.getString("MowNotes"))
                    .put("CommonRoute", resultSet.getInt("CommonRoute"))
                    .put("xTags", resultSet.getString("xTags"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDayE() {
        final String query_Fitness_DayE = "SELECT Weight, ExMin FROM Core.Fit_Em WHERE Date=CURDATE();";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_DayE, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("ExMin", resultSet.getInt("ExMin"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogCyc(List<String> qParams) {
        final String query_Fitness_jsonLogCyc = "SELECT gpsLogCyc as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_jsonLogCyc, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", resultSet.getString("gpsLog"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogCyc2(List<String> qParams) {
        final String query_Fitness_jsonLogCyc2 = "SELECT gpsLogCyc2 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_jsonLogCyc2, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", resultSet.getString("gpsLog"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
       
    public JSONArray getJsonLogRun(List<String> qParams) {
        final String query_Fitness_jsonLogRun = "SELECT gpsLogRun as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_jsonLogRun, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", resultSet.getString("gpsLog"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogRun2(List<String> qParams) {
        final String query_Fitness_jsonLogRun2 = "SELECT gpsLogRun2 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_jsonLogRun2, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", resultSet.getString("gpsLog"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getOverallStats() {
        final String query_Fitness_OverallStats = "SELECT" +
                " SUM(TrackedTime/60) AS TT," +
                " SUM(TrackedDist) AS TD," + 
                " AVG(CycSpeedAvg) AS AvgCycSpeedAvg," +
                " AVG(CycSpeedMax) AS AvgCycSpeedMax," +
                " MAX(CycSpeedAvg) AS MaxCycSpeedAvg," +
                " MAX(CycSpeedMax) AS MaxCycSpeedMax" +
                " FROM Core.Fitness" +
                " WHERE TrackedDist IS NOT NULL AND TrackedDist != 0";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_OverallStats, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("TT", resultSet.getDouble("TT"))
                    .put("TD", resultSet.getDouble("TD"))
                    .put("AvgCycSpeedAvg", resultSet.getDouble("AvgCycSpeedAvg"))
                    .put("AvgCycSpeedMax", resultSet.getDouble("AvgCycSpeedMax"))
                    .put("MaxCycSpeedAvg", resultSet.getDouble("MaxCycSpeedAvg"))
                    .put("MaxCycSpeedMax", resultSet.getDouble("MaxCycSpeedMax"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getOverallSensors() {
        final String query_Fitness_OverallStats = "SELECT" +
            " AVG(CycCadAvg) AS AvgCycCadAvg," +
            " MAX(CycCadAvg) AS MaxCycCadAvg," +
            " AVG(CycCadMax) AS AvgCycCadMax," +
            " MAX(CycCadMax) AS MaxCycCadMax," +
            " AVG(CycPowerAvg) AS AvgCycPowerAvg," +
            " MAX(CycPowerAvg) AS MaxCycPowerAvg," +
            " AVG(CycPowerMax) AS AvgCycPowerMax," +
            " MAX(CycPowerMax) AS MaxCycPowerMax," +
            " AVG(RunHeartAvg) AS AvgRunHeartAvg," +
            " MAX(RunHeartAvg) AS MaxRunHeartAvg" +
            " FROM Core.Fitness" +
            " WHERE CycCadAvg IS NOT NULL AND CycCadAvg != 0;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_OverallStats, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("AvgCycCadAvg", resultSet.getDouble("AvgCycCadAvg"))
                    .put("MaxCycCadAvg", resultSet.getDouble("MaxCycCadAvg"))
                    .put("AvgCycCadMax", resultSet.getDouble("AvgCycCadMax"))
                    .put("MaxCycCadMax", resultSet.getDouble("MaxCycCadMax"))
                    .put("AvgCycPowerAvg", resultSet.getDouble("AvgCycPowerAvg"))
                    .put("MaxCycPowerAvg", resultSet.getDouble("MaxCycPowerAvg"))
                    .put("AvgCycPowerMax", resultSet.getDouble("AvgCycPowerMax"))
                    .put("MaxCycPowerMax", resultSet.getDouble("MaxCycPowerMax"))
                    .put("AvgRunHeartAvg", resultSet.getDouble("AvgRunHeartAvg"))
                    .put("MaxRunHeartAvg", resultSet.getDouble("MaxRunHeartAvg"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getRelatedPhotos(List<String> qParams) {
        final String query_Fitness_RelatedPhotos = "SELECT Path, File, GeoData, Resolution, WarDeploy" +
                " FROM Core.MediaServer WHERE (Description LIKE '%run%' OR Description LIKE '%cycling%' OR Description LIKE '%bike%' OR Description LIKE '%hiking%')" +
                " AND ContentDate=? AND GeoData IS NOT NULL;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_RelatedPhotos, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Path", resultSet.getString("Path"))
                    .put("File", resultSet.getString("File"))
                    .put("GeoData", resultSet.getString("GeoData"))
                    .put("Resolution", resultSet.getString("Resolution"))
                    .put("WarDeploy", resultSet.getInt("WarDeploy"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
      
    public JSONArray getRPlans() {
        final String query_Fitness_RPlans = "SELECT Description, GeoJSON, Done from Core.Fit_RPlans ORDER BY Description DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_RPlans, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("GeoJSON", resultSet.getString("GeoJSON"))
                    .put("Done", resultSet.getInt("Done"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getRShoe() {
        final String query_Fitness_RShoe = "SELECT MAX(Shoe) AS Pair FROM Core.Fitness WHERE Shoe LIKE 'R%';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_RShoe, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Pair", resultSet.getString("Pair"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getRSMileMax(List<String> qParams) {
        final String query_Fitness_RShoe = "SELECT MAX(RSMile) AS LastMax FROM Core.Fitness WHERE Shoe=? AND Date < CURDATE();";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_RShoe, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("LastMax", resultSet.getDouble("LastMax"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getTot() {
        final String query_Fitness_TOT = "SELECT" +
                " COUNT(Date) AS TOTDY," +
                " SUM(RunWalk) AS TOTRW," +
                " SUM(Cycling) AS TOTCY," +
                " (SUM(Cycling) + SUM(RunWalk)) AS TOTOA" +
                " FROM Core.Fitness";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_TOT, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("TOTDY", resultSet.getInt("TOTDY"))
                    .put("TOTRW", resultSet.getDouble("TOTRW"))
                    .put("TOTCY", resultSet.getDouble("TOTCY"))
                    .put("TOTOA", resultSet.getDouble("TOTOA"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }  
    
    public JSONArray getYear(List<String> qParams) {
        final String query_Fitness_Year = "SELECT SUM(RunWalk) AS YearRW, SUM(Cycling) AS YearCY," +
                "(SUM(Cycling)+SUM(RunWalk)) AS YearOA FROM Core.Fitness WHERE Date LIKE ?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_Year, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("YearRW", resultSet.getDouble("YearRW"))
                    .put("YearCY", resultSet.getDouble("YearCY"))
                    .put("YearOA", resultSet.getDouble("YearOA"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
