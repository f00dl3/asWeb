/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 7 Apr 2018
 */

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class CookingDAO {

    WebCommon wc = new WebCommon();
    
    public JSONArray getRecipies() {
        final String query_Recipies_Cooking = "SELECT Description, Ingredients, Instructions, CookTemp, CookTime" +
                " FROM Core.Cooking ORDER BY Description ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Recipies_Cooking, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("Ingredients", resultSet.getString("Ingredients"))
                    .put("Instructions", resultSet.getString("Instructions"))
                    .put("CookTemp", resultSet.getInt("CookTemp"))
                    .put("CookTime", resultSet.getInt("CookTime"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
