/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 20 Feb 2018
*/

package asWebRest.dao;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebUIserAuthDAO {
    
    WebCommon wc = new WebCommon(); 
    
    public String getWebUIserAuth(String user) {
        final String query_WebUIserAuth = "SELECT sha256 FROM Core.WebUIsers WHERE username = ?;";
        String webUIserAuth = "";
        try {
            Connection connection = MyDBConnector.getMyConnection();
            PreparedStatement pStatement = connection.prepareStatement(query_WebUIserAuth);
            pStatement.setString(1, user);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                webUIserAuth = resultSet.getString("sha256");
            }
        } catch (SQLException sx) {
            sx.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webUIserAuth;
    }
    
}
