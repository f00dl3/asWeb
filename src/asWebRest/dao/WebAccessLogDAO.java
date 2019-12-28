/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 28 Dec 2019
*/

package asWebRest.dao;

import asWebRest.shared.MyDBConnector;
import asWebRest.model.WebAccessLog;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebAccessLogDAO {
    
    WebCommon wc = new WebCommon(); 
    
    public JSONArray getWebAccessLog() {
        final String query_WebAccessLast = "SELECT LoginTime, User, RemoteIP FROM Core.WebAccessLog ORDER BY LoginTime DESC LIMIT 1;";
        JSONArray webAccessLogs = new JSONArray();
        try (
            ResultSet resultSet = wc.q2rs(query_WebAccessLast, null);
        ) {
            while (resultSet.next()) {
                JSONObject webAccessLog = new JSONObject();
                webAccessLog
                    .put("LoginTime", resultSet.getString("LoginTime"))
                    .put("RemoteIP", resultSet.getString("RemoteIP"))
                    .put("User", resultSet.getString("User"));
                webAccessLogs.put(webAccessLog);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return webAccessLogs;
    }
    
    public void updateWebAccessLog(WebAccessLog webAccessLog) {
    	MyDBConnector mdb = new MyDBConnector();
        final String query_WebAccessLog = "INSERT INTO Core.WebAccessLog (LoginTime, User, RemoteIP, Tomcat, Browser) VALUES (Null, ?, ?, 1, ?);";
        try (
            Connection connection = mdb.getMyConnection();
            PreparedStatement pStatement = connection.prepareStatement(query_WebAccessLog);
        ) {
            pStatement.setString(1, webAccessLog.getUser());
            pStatement.setString(2, webAccessLog.getRemoteIp());
            pStatement.setString(3, webAccessLog.getBrowser());
            pStatement.execute();
        } catch (SQLException sx) {
            sx.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
