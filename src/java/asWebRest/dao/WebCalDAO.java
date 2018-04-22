/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 22 Apr 2018
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebCalDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public JSONObject getLastLogId(Connection dbc) {
        final String query_Calendar_LastLogID = "SELECT MAX(cal_entry_id) AS CEID, MAX(cal_log_id) AS CLID FROM WebCal.webcal_entry_log;";
        JSONObject tContainer = new JSONObject();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_Calendar_LastLogID, null);
            while (resultSet.next()) {
                tContainer
                    .put("CEID", resultSet.getInt("CEID"))
                    .put("CLID", resultSet.getInt("CLID"));
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public String setAddEntry(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntry = "INSERT INTO WebCal.webcal_entry VALUES (?,Null,Null,'f00dl3',?,?,?,?,?,?,?,5,'E','P',?,Null,Null,Null,?);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntry, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setAddEntryLog(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntryLog = "INSERT INTO WebCal.webcal_entry_log VALUES (?,?,'f00dl3','f00dl3','C',?,?,Null);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntryLog, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setAddEntryUser(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntryUser = "INSERT INTO WebCal.webcal_entry_user VALUES (?,'f00dl3','A',Null,0);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntryUser, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
}
