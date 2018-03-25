/*
by Anthony Stump
Created: 25 Mar 2018
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebCalDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public JSONArray getLastLogId() {
        final String query_Calendar_LastLogID = "SELECT MAX(cal_entry_id) AS CEID, MAX(cal_log_id) AS CLID FROM WebCal.webcal_entry_log;";
        JSONArray tContainer = new JSONArray();
        try {            
            ResultSet resultSet = wc.q2rs(query_Calendar_LastLogID, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("CEID", resultSet.getInt("CEID"))
                    .put("CLID", resultSet.getInt("CLID"));
                tContainer.put(tObject);
            }
           
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
