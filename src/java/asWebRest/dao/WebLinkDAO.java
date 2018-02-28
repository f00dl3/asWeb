/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 26 Feb 2018
*/

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebLinkDAO {
    
    WebCommon wc = new WebCommon(); 
    
    public JSONArray getWebLinks(List<String> qParams) {
        final String query_WebLinks = "SELECT Bubble, Description, URL, DescriptionL," +
                "GeoPlot, TCSystem, AsOf, DesktopLink, TomcatURL FROM Core.WebLinks" +
                " WHERE Master LIKE ? AND Active=1 ORDER BY Description ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_WebLinks, qParams);
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
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
