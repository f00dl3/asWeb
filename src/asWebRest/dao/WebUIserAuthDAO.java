/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 28 Dec 2019
*/

package asWebRest.dao;

import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebUIserAuthDAO {
    
    
    public String getWebUIserAuth(String user) {
    	
    	MyDBConnector mdb = new MyDBConnector();
        
        final String query_WebUIserAuth = "SELECT sha256 FROM Core.WebUIsers WHERE username = ?;";
        String webUIserAuth = "";
        
        try {
            Connection connection = mdb.getMyConnection();
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
