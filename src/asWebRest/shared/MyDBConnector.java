/*
by Anthony Stump
Created: 14 Aug 2017
Updated: 28 Dec 2019
*/

package asWebRest.shared;

import asWebRest.secure.DatabaseProps;
import java.sql.*;

public class MyDBConnector {

	public Connection getMyConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		DatabaseProps DBProperties = new DatabaseProps();
		return DriverManager.getConnection(DBProperties.getDbUrl(), DBProperties.getDbUser(), DBProperties.getDbPass());
	}

	public void main(String[] args) {
		Connection conn = null;
		try {
			conn = getMyConnection();
			System.out.println("MySQL (Core) Connected!");
		}
		catch (SQLException err) { System.out.println(err.getMessage()); }
		catch (Exception e) { System.out.println(e); }

	}
	
}
