/*
by Anthony Stump
Created: 4 Apr 2018
Updated: 29 Dec 2018
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    private JSONArray alarmBatteries(Connection dbc) {
        final String query_HomeDeepClean = "SELECT Description, Type, Replaced" +
                " FROM Core.HM_AlarmBatteries;";
        JSONArray tContainer = new JSONArray();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_HomeDeepClean, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("Type", resultSet.getString("Type"))
                    .put("Replaced", resultSet.getString("Replaced"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray houseDeepCleaning(Connection dbc) {
        final String query_HomeDeepClean = "SELECT Room, DateCompleted, DateStarted, TimeSpent" +
                " FROM Core.HM_DeepClean;";
        JSONArray tContainer = new JSONArray();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_HomeDeepClean, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Room", resultSet.getString("Room"))
                    .put("DateCompleted", resultSet.getString("DateCompleted"))
                    .put("DateStarted", resultSet.getString("DateStarted"))
                    .put("TimeSpent", resultSet.getDouble("TimeSpent"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAlarmBatteries(Connection dbc) { return alarmBatteries(dbc); } 
    public JSONArray getHouseDeepCleaning(Connection dbc) { return houseDeepCleaning(dbc); }
    
    public JSONArray getMeasure(Connection dbc, List<String> qParams) {
        final String query_Blueprint_HMM_xL = "SELECT x1, y1, x2, y2, Height, Width," +
                " Level, Description, Type, OffFloor" +
                " FROM Core.HM_Measure WHERE Level=?;";
        JSONArray tContainer = new JSONArray();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_Blueprint_HMM_xL, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("x1", resultSet.getInt("x1"))
                    .put("x2", resultSet.getInt("x2"))
                    .put("y1", resultSet.getInt("y1"))
                    .put("y2", resultSet.getInt("y2"))
                    .put("Width", resultSet.getInt("Width"))
                    .put("Type", resultSet.getString("Type"))
                    .put("Height", resultSet.getInt("Height"))
                    .put("OffFloor", resultSet.getInt("OffFloor"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Level", resultSet.getString("Level"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
