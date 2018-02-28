/*
by Anthony Stump
Created: 20 Feb 2018
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomicideDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getHomicide() {
        final String query_Homicide = "SELECT HomID, Date, Point, Age, Description, Victim" +
                " FROM Core.Homicide WHERE Region='KCY' ORDER BY Date DESC LIMIT 2500;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Homicide, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("HomID", resultSet.getString("HomID"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Point", resultSet.getString("Point"))
                    .put("Age", resultSet.getInt("Age"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Victim", resultSet.getString("Victim"));                        
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
   
    
}
