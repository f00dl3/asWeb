/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 26 Jan 2020
*/

package asWebRest.dao;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WebUIserAuthDAO {

	WebCommon wc = new WebCommon();
    
	public String getExternalPassword(Connection dbc, String account) {
		List<String> internalBinding = new ArrayList<String>();
		internalBinding.add(account);
		String passBack = "";
        final String query_Auth = "SELECT Value FROM Core.JavaSex WHERE Item=?;";
        
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Auth, internalBinding);
            while (resultSet.next()) { 
                passBack = resultSet.getString("Value");
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
    	return passBack;		
	}
	
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
