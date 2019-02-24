/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 24 Feb 2019
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class PtoDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public JSONArray getPto(Connection dbc) {
        final String query_PTO = "SELECT Month, New, Taken, Save," + 
                " (@runtot := @runtot + CarryOver + New - Taken - Save) AS Balance, Notes" +
                " FROM Core.SprintPTO WHERE Month BETWEEN '2018-04' AND '2020-03';";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_PTO, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Month", resultSet.getString("Month"))
                    .put("New", resultSet.getDouble("New"))
                    .put("Taken", resultSet.getDouble("Taken"))
                    .put("Save", resultSet.getDouble("Save"))
                    .put("Balance", resultSet.getDouble("Balance"))
                    .put("Notes", resultSet.getString("Notes"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
