/*
by Anthony Stump
Created: 29 Nov 2019
Updated: 4 Dec 2019
*/

package asWebRest.dao;

import java.sql.ResultSet;

import asWebRest.hookers.AlarmSystem;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SmarthomeDAO {

	AlarmSystem als = new AlarmSystem();
    WebCommon wc = new WebCommon();
    
    public JSONArray getArmDisarm(Connection dbc) {
        final String query_ArmDisarm = "SELECT RequestID, ArmTime, ArmType FROM net_snmp.Home_ArmDisarm ORDER BY ArmTime DESC LIMIT 25;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ArmDisarm, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("RequestID", resultSet.getLong("RequestID"))
                    .put("ArmTime", resultSet.getString("ArmTime"))
                    .put("ArmType", resultSet.getString("ArmType"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDoorEvents(Connection dbc) {
        final String query_DoorEvents = "SELECT EventID, ReceivedTimesetamp, OriginalTimestamp, DoorLocation FROM net_snmp.Home_DoorEvents ORDER BY ReceivedTimesetamp DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_DoorEvents, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EventID", resultSet.getLong("EventID"))
                    .put("ReceivedTimestamp", resultSet.getString("ReceivedTimesetamp"))
                    .put("OriginalTimestamp", resultSet.getString("OriginalTimestamp"))
                    .put("DoorLocation", resultSet.getString("DoorLocation"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public String setArmDisarm(Connection dbc, List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_ArmDisarm = "INSERT INTO net_snmp.Home_ArmDisarm (ArmType) values (?);";
        try { returnData = wc.q2do1c(dbc, query_ArmDisarm, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }    
    
    public String setDoorEvent(Connection dbc, List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_AddDoorEvent = "INSERT INTO net_snmp.Home_DoorEvents (OriginalTimestamp, DoorLocation) values (?, ?);";
        try { returnData = wc.q2do1c(dbc, query_AddDoorEvent, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { als.notifyOfEvent(dbc, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
}
