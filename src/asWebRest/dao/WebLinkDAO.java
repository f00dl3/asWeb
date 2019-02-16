/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 25 Apr 2018
*/

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebLinkDAO {
    
    WebCommon wc = new WebCommon(); 
    
    public JSONArray getWebLinks(Connection dbc, String toMatch) {
        final String query_WebLinks = "SELECT Bubble, Description," +
                " CASE WHEN TomcatProd = 1 THEN TomcatURL ELSE URL END as URL," +
                " DescriptionL, GeoPlot, TCSystem, AsOf, DesktopLink, TomcatURL FROM Core.WebLinks" +
                " WHERE Master LIKE '%"+toMatch+"%' AND Active=1 ORDER BY Description ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_WebLinks, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Bubble", resultSet.getString("Bubble"))
                    .put("Description", resultSet.getString("Description"))
                    .put("URL", resultSet.getString("URL"))
                    .put("DescriptionL", resultSet.getString("DescriptionL"))
                    .put("GeoPlot", resultSet.getString("GeoPlot"))
                    .put("TCSystem", resultSet.getString("TCSystem"))
                    .put("AsOf", resultSet.getString("AsOf"))
                    .put("DesktopLink", resultSet.getString("DesktopLink"))
                    .put("TomcatURL", resultSet.getString("TomcatURL"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
