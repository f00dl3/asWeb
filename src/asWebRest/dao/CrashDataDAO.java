/*
by Anthony Stump
Created: 16 Dec 2018
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

public class CrashDataDAO {
    
    WebCommon wc = new WebCommon();
    
    private JSONArray crashData(Connection dbc) {
        final String query_CrashData = "SELECT " +
                " Incident, Lat, Lon, Date, Time, County, Location," +
                " Vehicles, Damage, Injuries, InjuryTypes, SeatBelt, Rollover, Synopsis" +
                " FROM Core.Crash_HPMO" +
                " ORDER BY Date, Time DESC LIMIT 2500;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_CrashData, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Incident", resultSet.getLong("Incident"))
                    .put("Lat", resultSet.getDouble("Lat"))
                    .put("Lon", resultSet.getDouble("Lon"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Time", resultSet.getInt("Time"))
                    .put("County", resultSet.getString("County"))
                    .put("Location", resultSet.getString("Location"))
                    .put("Vehicles", resultSet.getInt("Vehicles"))
                    .put("Damage", resultSet.getString("Damage"))
                    .put("Injuries", resultSet.getInt("Injuries"))
                    .put("InjuryTypes", resultSet.getString("InjuryTypes"))
                    .put("SeatBelt", resultSet.getInt("SeatBelt"))
                    .put("Rollover", resultSet.getInt("Rollover"))
                    .put("Synopsis", resultSet.getString("Synopsis"));                        
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
   
    public JSONArray getCrashData(Connection dbc) { return crashData(dbc); }
    
}
