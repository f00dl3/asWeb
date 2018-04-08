/*
by Anthony Stump
Created: 15 Feb 2018
Updated: 7 Apr 2018
*/

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebVersionDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getCurrentVer() {
        final String query_index_CurrentVersion = "SELECT MAX(Version) AS Ver, MAX(Date) AS Date FROM Core.WebVersion;";
        JSONArray webVersions = new JSONArray();
        try (
            ResultSet resultSet = wc.q2rs(query_index_CurrentVersion, null);
        ) {
            while (resultSet.next()) {
                JSONObject webVersion = new JSONObject();
                webVersion
                    .put("Version", resultSet.getString("Ver"))
                    .put("Date", resultSet.getString("Date"));
                webVersions.put(webVersion);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return webVersions;
    }
    
    public JSONArray getWebVersion() {
        final String query_WebVersion = "SELECT Version, Date, Changes FROM Core.WebVersion ORDER BY Date DESC LIMIT 5;";
        JSONArray webVersions = new JSONArray();
        try (
            ResultSet resultSet = wc.q2rs(query_WebVersion, null);
        ) {
            while (resultSet.next()) {
                JSONObject webVersion = new JSONObject();
                webVersion
                    .put("Version", resultSet.getString("Version"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Changes", resultSet.getString("Changes"));
                webVersions.put(webVersion);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return webVersions;
    }
    
   
}
