/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 24 Feb 2019
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FitnessDAO {
    
    WebCommon wc = new WebCommon();
       
    private JSONArray geoJson(Connection dbc, List<String> qParams) {
        final String query_Fitness_GeoJSON = "SELECT" +
                " Date, RunGeoJSON, CycGeoJSON, AltGeoJSON FROM Core.Fitness" +
                " WHERE Date BETWEEN ? AND ? " +
                " ORDER BY Date DESC LIMIT 0, 365;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_GeoJSON, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("RunGeoJSON", resultSet.getString("RunGeoJSON"))
                    .put("CycGeoJSON", resultSet.getString("CycGeoJSON"))
                    .put("AltGeoJSON", resultSet.getString("AltGeoJSON"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray rPlanByDesc(Connection dbc, List<String> qParams) {
        final String query_Fitness_RPlans = "SELECT Description, GeoJSON, Done, DistKm from Core.Fit_RPlans WHERE Description = ?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_RPlans, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("GeoJSON", resultSet.getString("GeoJSON"))
                    .put("DistKm", resultSet.getDouble("DistKm"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAll(Connection dbc, List<String> qParams) {
        final String query_Fitness_All = "SELECT" +
                " f.Date, f.Weight, f.RunWalk, f.Shoe, f.RSMile," +
                " f.Bicycle, f.Cycling, f.BkStudT," +
                " f.ReelMow, f.MowNotes, f.Calories," +
                " CASE WHEN f.gpsLogRun IS NULL THEN f.RunGeoJSON ELSE null END AS RunGeoJSON," +
                " CASE WHEN f.gpsLogCyc IS NULL THEN f.CycGeoJSON ELSE null END AS CycGeoJSON," +
                " CASE WHEN" +
                "   (f.gpsLogRun2 IS NULL AND f.gpsLogRun3 IS NULL AND f.gpsLogRun4 IS NULL AND" +
                "   f.gpsLogCyc2 IS NULL AND f.gpsLogCyc3 IS NULL AND f.gpsLogCyc4 IS NULL)" +
                "   THEN f.AltGeoJSON ELSE null END AS AltGeoJSON," +
                " f.Fat, f.Protein, f.Carbs, f.Sugar, f.Fiber, f.Cholest, f.Sodium, f.Water, f.FruitsVeggies," +
                " f.TrackedTime, f.TrackedDist," +
                " f.CycSpeedAvg, f.CycSpeedMax, f.CycCadAvg, f.CycCadMax, f.CycPowerAvg, f.CycPowerMax, f.CycHeartAvg, f.CycHeartMax," +
                " f.RunSpeedAvg, f.RunSpeedMax, f.RunHeartAvg, f.RunHeartMax," +
                " f.Gym, f.GymWorkout, f.CommonRoute, f.xTags, f.Orgs," +
                " f.Vomit, f.EstHoursSleep, f.Swimming, f.HoursGaming," +
                " f.CaloriesBurned, f.Steps, f.IntensityMinutes," +
                " cf6.High, cf6.Low, cf6.Average," +
                " CASE WHEN f.gpsLogCyc IS NOT NULL THEN true ELSE false END AS isGPSCycJSON," +
                " CASE WHEN f.gpsLogRun IS NOT NULL THEN true ELSE false END AS isGPSRunJSON," +
                " CASE WHEN f.gpsLogCyc2 IS NOT NULL THEN true ELSE false END AS isGPSCyc2JSON," +
                " CASE WHEN f.gpsLogRun2 IS NOT NULL THEN true ELSE false END AS isGPSRun2JSON," +
                " CASE WHEN f.gpsLogCyc3 IS NOT NULL THEN true ELSE false END AS isGPSCyc3JSON," +
                " CASE WHEN f.gpsLogRun3 IS NOT NULL THEN true ELSE false END AS isGPSRun3JSON," +
                " CASE WHEN f.gpsLogCyc4 IS NOT NULL THEN true ELSE false END AS isGPSCyc4JSON," +
                " CASE WHEN f.gpsLogRun4 IS NOT NULL THEN true ELSE false END AS isGPSRun4JSON" +
                " FROM Core.Fitness f" +
                " LEFT OUTER JOIN WxObs.CF6MCI cf6 ON f.Date = cf6.Date" +
                " WHERE f.Date BETWEEN ? AND ? " +
                " ORDER BY f.Date DESC LIMIT 0, 365;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_All, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("RunWalk", resultSet.getDouble("RunWalk"))
                    .put("Shoe", resultSet.getString("Shoe"))
                    .put("RSMile", resultSet.getString("RSMile"))
                    .put("RunGeoJSON", resultSet.getString("RunGeoJSON"))
                    .put("CycGeoJSON", resultSet.getString("CycGeoJSON"))
                    .put("AltGeoJSON", resultSet.getString("AltGeoJSON"))
                    .put("Bicycle", resultSet.getString("Bicycle"))
                    .put("Cycling", resultSet.getDouble("Cycling"))
                    .put("BkStudT", resultSet.getInt("BkStudT"))
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
					.put("Orgs", resultSet.getInt("Orgs"))
                    .put("Vomit", resultSet.getInt("Vomit"))
                    .put("EstHoursSleep", resultSet.getDouble("EstHoursSleep"))
                    .put("Swimming", resultSet.getInt("Swimming"))
                    .put("High", resultSet.getInt("High"))
                    .put("Low", resultSet.getInt("Low"))
                    .put("Average", resultSet.getInt("Average"))
                    .put("isGPSCycJSON", resultSet.getBoolean("isGPSCycJSON"))
                    .put("isGPSCyc2JSON", resultSet.getBoolean("isGPSCyc2JSON"))
                    .put("isGPSCyc3JSON", resultSet.getBoolean("isGPSCyc3JSON"))
                    .put("isGPSCyc4JSON", resultSet.getBoolean("isGPSCyc4JSON"))
                    .put("isGPSRunJSON", resultSet.getBoolean("isGPSRunJSON"))
                    .put("isGPSRun2JSON", resultSet.getBoolean("isGPSRun2JSON"))
                    .put("isGPSRun3JSON", resultSet.getBoolean("isGPSRun3JSON"))
                    .put("isGPSRun4JSON", resultSet.getBoolean("isGPSRun4JSON"))
                    .put("HoursGaming", resultSet.getDouble("HoursGaming"))
                    .put("CaloriesBurned", resultSet.getInt("CaloriesBurned"))
                    .put("Steps", resultSet.getInt("Steps"))
                    .put("IntensityMinutes", resultSet.getInt("IntensityMinutes"));
                tContainer.put(tObject);
            }
            resultSet.close();
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public JSONArray getAllRoutes(Connection dbc) {
        final String commonRouteCheck = "(CommonRoute = 0 OR CommonRoute IS NULL)";
        final String query_Fitness_AllRoutes = "SELECT" +
                " Date, Type, GeoJSON FROM (" +
                " SELECT Date, 'R' AS Type, RunGeoJSON as GeoJSON FROM Core.Fitness WHERE RunGeoJSON IS NOT NULL AND " + commonRouteCheck + " UNION ALL" +
                " SELECT Date, 'C' AS Type, CycGeoJSON as GeoJSON FROM Core.Fitness WHERE CycGeoJSON IS NOT NULL AND " + commonRouteCheck + " UNION ALL" +
                " SELECT Date, 'A' AS Type, AltGeoJSON as GeoJSON FROM Core.Fitness WHERE AltGeoJSON IS NOT NULL AND " + commonRouteCheck + ") as tmp" +
                " ORDER BY Date DESC";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_AllRoutes, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Type", resultSet.getString("Type"))
                    .put("GeoJSON", resultSet.getString("GeoJSON"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getBike(Connection dbc, String bike) {
        
        final String query_Fitness_Bike = "SELECT Description, Who, Purchased, PurchPrice FROM Core.Fit_Bike WHERE Code='" + bike +"'";
        
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_Bike, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("Who", resultSet.getString("Who"))
                    .put("Purchased", resultSet.getString("Purchased"))
                    .put("PurchPrice", resultSet.getDouble("PurchPrice"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
        
    }
    
    public JSONArray getBkStats(Connection dbc, String bike) {
        final String query_Fitness_BkStats = "SELECT" +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNChain=1 AND Bicycle='"+bike+"') AS LastChain," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkCln=1 AND Bicycle='"+bike+"') AS LastCleaned," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkFlt=1 AND Bicycle='"+bike+"') AS LastFlat," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireF=1 AND Bicycle='"+bike+"') AS LastTireFront," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireFS=1) AS LastTireFrontStudded," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireR=1 AND Bicycle='"+bike+"') AS LastTireRear," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireRS=1) AS LastTireRearStudded," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelF=1 AND Bicycle='"+bike+"') AS LastWheelFront," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelR=1 AND Bicycle='"+bike+"') AS LastWheelRear," +
                " (SELECT MAX(Date) FROM Core.Fitness WHERE BkOH=1 AND Bicycle='"+bike+"') AS LastOverhaul," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNChain = 1) AND Bicycle='"+bike+"') AS MilesChain," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Bicycle='"+bike+"') AS MilesBike," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkOH = 1) AND Bicycle='"+bike+"') AS MilesOverhaul," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireF = 1 AND BkStudT = 0 AND Bicycle='"+bike+"')) AS MilesTireFront," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireFS = 1) AND BkStudT = 1) AS MilesTireFrontStudded," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireR = 1 AND BkStudT = 0 AND Bicycle='"+bike+"')) AS MilesTireRear," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNTireRS = 1) AND BkStudT = 1) AS MilesTireRearStudded," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelF = 1) AND Bicycle='"+bike+"') AS MilesWheelFront," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date > (SELECT MAX(Date) FROM Core.Fitness WHERE BkNWheelR = 1) AND Bicycle='"+bike+"') AS MilesWheelRear" +
                " FROM Core.Fitness LIMIT 1;";
        //System.out.println(query_Fitness_BkStats);
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_BkStats, null);
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getCalories(Connection dbc) {
        final String query_Calories_Main = "SELECT c.Food, c.Serving, c.Calories, c.Carbs, c.Protein, c.Sugar," +
        " c.Sodium, c.Fat, c.Cholest, c.Fiber, c.Water, c.FruitVeggie, c.Last, c.ServingsLast, c.ServingsLastE, c.LastE," +
        " sla.ServingsLast as ThisServingsLast," +
        " sle.ServingsLastE as ThisServingsLastE" +
        " FROM Core.Fit_Calories c" +
        " LEFT JOIN (SELECT ServingsLast, Last, Food FROM Core.Fit_Calories WHERE Last=CURDATE()) AS sla ON sla.Last = c.Last AND sla.Food = c.Food" +
        " LEFT JOIN (SELECT ServingsLastE, LastE, Food FROM Core.Fit_Calories WHERE LastE=CURDATE()) AS sle ON sle.LastE = c.LastE AND sla.Food = c.Food;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Calories_Main, null);
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
                    .put("LastE", resultSet.getString("LastE"))
                    .put("ThisServingsLast", resultSet.getDouble("ThisServingsLast"))
                    .put("ThisServingsLastE", resultSet.getDouble("ThisServingsLastE"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChCaloriesR(Connection dbc, List<String> qParams) {
        final String query_ch_CaloriesR = "SELECT Date, Calories, Fat, Protein, Carbs" +
                " FROM Core.Fitness WHERE Date BETWEEN ? AND ? LIMIT 366;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_CaloriesR, qParams);
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public JSONArray getChWeightA() {
        final String query_ch_WeightA = "SELECT Date, Weight, EstHoursSleep FROM Core.Fitness ORDER BY Date ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_WeightA, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("EstHoursSleep", resultSet.getDouble("EstHoursSleep"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChWeightR(Connection dbc, List<String> qParams) {
        final String query_ch_WeightR = "SELECT Date, Weight, EstHoursSleep, HoursGaming FROM Core.Fitness WHERE Date BETWEEN ? AND ? ORDER BY Date ASC LIMIT 366;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_WeightR, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("HoursGaming", resultSet.getDouble("HoursGaming"))
                    .put("EstHoursSleep", resultSet.getDouble("EstHoursSleep"));
                tContainer.put(tObject);
            }
            resultSet.close();
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public JSONArray getCrsm(Connection dbc) {
        final String query_Fitness_CRSM = "SELECT MAX(RSMile) AS CRSM FROM Core.Fitness WHERE Shoe = (SELECT MAX(Shoe) FROM Core.Fitness WHERE Shoe LIKE 'R%');";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_CRSM, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("CRSM", resultSet.getDouble("CRSM"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDay(Connection dbc) {
        final String query_Fitness_Day = "SELECT " +
                " Weight, RunWalk, Shoe, RSMile, Cycling, Gym, GymWorkout, TrackedTime, TrackedDist," +
                " BkStudT, ReelMow, MowNotes, CommonRoute, xTags, Vomit, EstHoursSleep, Orgs," +
                " CaloriesBurned, IntensityMinutes, Steps" +
                " FROM Core.Fitness WHERE Date=CURDATE();";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_Day, null);
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
                    .put("xTags", resultSet.getString("xTags"))
                    .put("Vomit", resultSet.getInt("Vomit"))
                    .put("Orgs", resultSet.getInt("Orgs"))
                    .put("Steps", resultSet.getInt("Steps"))
                    .put("IntensityMinutes", resultSet.getInt("IntensityMinutes"))
                    .put("CaloriesBurned", resultSet.getInt("CaloriesBurned"))
                    .put("EstHoursSleep", resultSet.getDouble("EstHoursSleep"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDayE(Connection dbc) {
        final String query_Fitness_DayE = "SELECT Weight, ExMin FROM Core.Fit_Em WHERE Date=CURDATE();";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_DayE, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Weight", resultSet.getDouble("Weight"))
                    .put("ExMin", resultSet.getInt("ExMin"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDayY(Connection dbc) {
        final String query_Fitness_DayY = "SELECT CaloriesBurned, Steps, IntensityMinutes," +
        		" Calories, XTags, Orgs" +
    			" FROM Core.Fitness WHERE Date=CURDATE() - INTERVAL 1 DAY;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_DayY, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("CaloriesBurned", resultSet.getInt("CaloriesBurned"))
                    .put("Steps", resultSet.getInt("Steps"))
                    .put("IntensityMinutes", resultSet.getInt("IntensityMinutes"))
                    .put("Calories", resultSet.getInt("Calories"))
                    .put("XTags", resultSet.getString("XTags"))
                    .put("Orgs", resultSet.getInt("Orgs"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getGeoJSON(Connection dbc, List<String> qParams) { return geoJson(dbc, qParams); }
    
    public JSONArray getJsonLogCyc(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogCyc = "SELECT gpsLogCyc as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogCyc, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogCyc2(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogCyc2 = "SELECT gpsLogCyc2 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogCyc2, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogCyc3(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogCyc3 = "SELECT gpsLogCyc3 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogCyc3, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogCyc4(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogCyc4 = "SELECT gpsLogCyc4 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogCyc4, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
       
    public JSONArray getJsonLogRun(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogRun = "SELECT gpsLogRun as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogRun, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogRun2(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogRun2 = "SELECT gpsLogRun2 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogRun2, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    
    public JSONArray getJsonLogRun3(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogRun3 = "SELECT gpsLogRun3 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogRun3, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getJsonLogRun4(Connection dbc, List<String> qParams) {
        final String query_Fitness_jsonLogRun4 = "SELECT gpsLogRun4 as gpsLog FROM Fitness WHERE Date=?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_jsonLogRun4, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("gpsLog", new JSONObject(resultSet.getString("gpsLog")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
       
    public JSONArray getOverallStats(Connection dbc) {
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
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_OverallStats, null);
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getOverallSensors(Connection dbc) {
        final String query_Fitness_OverallSensors = "SELECT" +
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
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_OverallSensors, null);
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getRelatedPhotos(Connection dbc, List<String> qParams) {
        final String query_Fitness_RelatedPhotos = "SELECT Path, File, Description, GeoData, Resolution, WarDeploy" +
                " FROM Core.MediaServer WHERE (Description LIKE '%run%' OR Description LIKE '%cycling%' OR Description LIKE '%bike%' OR Description LIKE '%hiking%')" +
                " AND ContentDate=? AND GeoData IS NOT NULL;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_RelatedPhotos, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Path", resultSet.getString("Path"))
                    .put("File", resultSet.getString("File"))
                    .put("Description", resultSet.getString("Description"))
                    .put("GeoData", resultSet.getString("GeoData"))
                    .put("Resolution", resultSet.getString("Resolution"))
                    .put("WarDeploy", resultSet.getInt("WarDeploy"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
      
    public JSONArray getRPlanByDesc(Connection dbc, List<String> qParams) { return rPlanByDesc(dbc, qParams); }
    
    public JSONArray getRPlans(Connection dbc) {
        final String query_Fitness_RPlans = "SELECT Description, GeoJSON, Done, DistKm from Core.Fit_RPlans ORDER BY Description DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_RPlans, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("GeoJSON", resultSet.getString("GeoJSON"))
                    .put("Done", resultSet.getInt("Done"))
                    .put("DistKm", resultSet.getDouble("DistKm"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getRShoe(Connection dbc) {
        final String query_Fitness_RShoe = "SELECT MAX(Shoe) AS Pair FROM Core.Fitness WHERE Shoe LIKE 'R%';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_RShoe, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Pair", resultSet.getString("Pair"));
                tContainer.put(tObject);
            }
            resultSet.close();
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
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getTot(Connection dbc) {
        final String query_Fitness_TOT = "SELECT" +
                " COUNT(Date) AS TOTDY," +
                " SUM(RunWalk) AS TOTRW," +
                " SUM(Cycling) AS TOTCY," +
                " (SUM(Cycling) + SUM(RunWalk)) AS TOTOA" +
                " FROM Core.Fitness";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_TOT, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("TOTDY", resultSet.getInt("TOTDY"))
                    .put("TOTRW", resultSet.getDouble("TOTRW"))
                    .put("TOTCY", resultSet.getDouble("TOTCY"))
                    .put("TOTOA", resultSet.getDouble("TOTOA"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }  
    
    public JSONArray getYear(Connection dbc, String yearIn) {
        int yearInt = Integer.parseInt(yearIn);
        int yb1 = yearInt-1;
        int yb2 = yearInt-2;
        int yb3 = yearInt-3;
        int yb4 = yearInt-4;
        final String query_Fitness_Year = "SELECT" +
                " (SELECT SUM(RunWalk) FROM Core.Fitness WHERE Date LIKE '"+yearInt+"-%' AND RunWalk IS NOT NULL) AS yb0rw," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yearInt+"-%' AND Cycling IS NOT NULL) AS yb0cy," +
                " (SELECT SUM(RunWalk)+SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yearInt+"-%' AND (RunWalk IS NOT NULL OR Cycling IS NOT NULL)) AS yb0oa," +
                " (SELECT SUM(RunWalk) FROM Core.Fitness WHERE Date LIKE '"+yb1+"-%' AND RunWalk IS NOT NULL) AS yb1rw," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb1+"-%' AND Cycling IS NOT NULL) AS yb1cy," +
                " (SELECT SUM(RunWalk)+SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb1+"-%' AND (RunWalk IS NOT NULL OR Cycling IS NOT NULL)) AS yb1oa," +
                " (SELECT SUM(RunWalk) FROM Core.Fitness WHERE Date LIKE '"+yb2+"-%' AND RunWalk IS NOT NULL) AS yb2rw," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb2+"-%' AND Cycling IS NOT NULL) AS yb2cy," +
                " (SELECT SUM(RunWalk)+SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb2+"-%' AND RunWalk IS NOT NULL) AS yb2oa," +
                " (SELECT SUM(RunWalk) FROM Core.Fitness WHERE Date LIKE '"+yb3+"-%' AND RunWalk IS NOT NULL) AS yb3rw," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb3+"-%' AND Cycling IS NOT NULL) AS yb3cy," +
                " (SELECT SUM(RunWalk)+SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb3+"-%' AND (RunWalk IS NOT NULL OR Cycling IS NOT NULL)) AS yb3oa," +
                " (SELECT SUM(RunWalk) FROM Core.Fitness WHERE Date LIKE '"+yb4+"-%' AND RunWalk IS NOT NULL) AS yb4rw," +
                " (SELECT SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb4+"-%' AND Cycling IS NOT NULL) AS yb4cy," +
                " (SELECT SUM(RunWalk)+SUM(Cycling) FROM Core.Fitness WHERE Date LIKE '"+yb4+"-%' AND (RunWalk IS NOT NULL OR Cycling IS NOT NULL)) AS yb4oa" +
                " FROM Core.Fitness LIMIT 1";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Fitness_Year, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("yb0rw", resultSet.getDouble("yb0rw"))
                    .put("yb0cy", resultSet.getDouble("yb0cy"))
                    .put("yb0oa", resultSet.getDouble("yb0oa"))
                    .put("yb1rw", resultSet.getDouble("yb1rw"))
                    .put("yb1cy", resultSet.getDouble("yb1cy"))
                    .put("yb1oa", resultSet.getDouble("yb1oa"))
                    .put("yb2rw", resultSet.getDouble("yb2rw"))
                    .put("yb2cy", resultSet.getDouble("yb2cy"))
                    .put("yb2oa", resultSet.getDouble("yb2oa"))
                    .put("yb3rw", resultSet.getDouble("yb3rw"))
                    .put("yb3cy", resultSet.getDouble("yb3cy"))
                    .put("yb3oa", resultSet.getDouble("yb3oa"))
                    .put("yb4rw", resultSet.getDouble("yb4rw"))
                    .put("yb4cy", resultSet.getDouble("yb4cy"))
                    .put("yb4oa", resultSet.getDouble("yb4oa"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public String setPlanCyc(List<String> qParams) {
        String returnData = "";
        String query_Fitness_RPlanDoC = "UPDATE Core.Fitness SET CycGeoJSON=(SELECT GeoJSON FROM Core.Fit_RPlans WHERE Description=?) WHERE Date=CURDATE();";
        try { returnData = wc.q2do(query_Fitness_RPlanDoC, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setPlanMark(List<String> qParams) {
        String returnData = "";
        String query_Fitness_RPlanDoMC = "UPDATE Core.Fit_RPlans SET Done=1 WHERE Description=?;";
        try { returnData = wc.q2do(query_Fitness_RPlanDoMC, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setPlanRun(List<String> qParams) {
        String returnData = "";
        String query_Fitness_RPlanDoR = "UPDATE Core.Fitness SET RunGeoJSON=(SELECT GeoJSON FROM Core.Fit_RPlans WHERE Description=?) WHERE Date=CURDATE();";
        try { returnData = wc.q2do(query_Fitness_RPlanDoR, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setCalories(Connection dbc, List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_Calories_Update = "INSERT INTO Core.Fitness " +
                " (Date,Calories,Fat,Protein,Carbs,Cholest,Sodium,Fiber,Sugar,Water,FruitsVeggies) VALUES" +
                " (CURDATE(),?,?,?,?,?,?,?,?,?,?)" +
                " ON DUPLICATE KEY UPDATE Calories=?, Fat=?, Protein=?," +
                " Carbs=?, Cholest=?, Sodium=?, Fiber=?," +
                " Sugar=?, Water=?, FruitsVeggies=?;";
        try { returnData = wc.q2do1c(dbc, query_Calories_Update, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setCaloriesSingle(List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_Calories_SingleUpdate = "UPDATE Core.Fit_Calories SET ServingsLast=?, Last=CURDATE() WHERE Food=?;";
        try { returnData = wc.q2do(query_Calories_SingleUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setCaloriesSingleE(List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_Calories_SingleUpdate = "UPDATE Core.Fit_Calories SET ServingsLastE=?, Last=CURDATE() WHERE Food=?;";
        try { returnData = wc.q2do(query_Calories_SingleUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setUpdateToday(List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String tRShoe = qParams.get(2);
        double tRShoeMaxMiles = 0.0;
        final String query_Fitness_GetLastRsMileTotal = "SELECT MAX(RSMile) AS MaxRSMiles FROM Core.Fitness WHERE Shoe='" + tRShoe + "' AND Date != CURDATE();";
        try {
            ResultSet resultSet = wc.q2rs(query_Fitness_GetLastRsMileTotal, null);
            while (resultSet.next()) {
                tRShoeMaxMiles = resultSet.getDouble("MaxRSMiles");
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        String query_Fitness_DayIU = "INSERT INTO Core.Fitness" +
	            " (Date,Weight,RunWalk,Shoe,RSMile,Cycling,BkStudT,ReelMow,MowNotes,Bicycle," +
        		"   CommonRoute,xTags,Vomit,EstHoursSleep,Orgs,IntensityMinutes) VALUES" +
	            " (CURDATE(),?,?,?,(?+" + tRShoeMaxMiles + "),?,?,?,?,?," +
        		"   ?,?,?,?,?,?)" +
	            " ON DUPLICATE KEY UPDATE" +
	            " Weight=?, RunWalk=?, Shoe=?, RSMile=(?+" + tRShoeMaxMiles + ")," +
				" Cycling=?, BkStudT=?, ReelMow=?, MowNotes=?," +
				" Bicycle=?, CommonRoute=?, xTags=?, Vomit=?, EstHoursSleep=?, Orgs=?," +
				" IntensityMinutes=?";
        try { returnData = wc.q2do(query_Fitness_DayIU, qParams); } catch (Exception e) { e.printStackTrace(); }
        final String query_Fitness_DayIUSteps = "UPDATE Core.Fitness SET Steps=(2000*IFNULL(1508.57*RunWalk,0)) WHERE Date=CURDATE();";
        final String query_Fitness_DayIUCals = "UPDATE Core.Fitness SET CaloriesBurned=(1600+IFNULL(175*RunWalk,0)+IFNULL(72*Cycling,0)) WHERE Date=CURDATE();";
        try { returnData = wc.q2do(query_Fitness_DayIUSteps, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData = wc.q2do(query_Fitness_DayIUCals, null); } catch (Exception e) { e.printStackTrace(); }
        returnData += "\n ***DEBUG: \n part1 : " + query_Fitness_GetLastRsMileTotal + "\n part2 : " + query_Fitness_DayIU;
        return returnData;
    }
    
    public String setUpdateTodayEm(List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_Fitness_DayIUE = "INSERT INTO Core.Fit_Em (Date, Weight, ExMin) VALUES (CURDATE(),?,?) ON DUPLICATE KEY UPDATE Weight=?, ExMin=?;";
        try { returnData = wc.q2do(query_Fitness_DayIUE, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setUpdateYesterday(List<String> qParams) {
    	String returnData = "Query has not ran yet or has failed.";
    	String query_Fitness_DayYU = "UPDATE Core.Fitness SET" + 
    			" IntensityMinutes=?, Calories=?, XTags=?, Orgs=?" +
    			" WHERE Date=CURDATE() - INTERVAL 1 DAY;";
    	try { returnData = wc.q2do(query_Fitness_DayYU, qParams); } catch (Exception e) { e.printStackTrace(); }
    	return returnData;
    }
    
}
