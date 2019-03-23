/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 23 Mar 2019
 */

package asWebRest.action;

import asWebRest.dao.DatabaseInfoDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetDatabaseInfoAction {
    
    private DatabaseInfoDAO databaseInfoDAO;
    public GetDatabaseInfoAction(DatabaseInfoDAO databaseInfoDAO) { this.databaseInfoDAO = databaseInfoDAO; }
    
    public JSONArray getDatabaseInfo(Connection dbc) { return databaseInfoDAO.getDbInfo(dbc); }
    public JSONArray getDatabaseInfoByTable(Connection dbc) { return databaseInfoDAO.getDbInfoByTable(dbc); }
    public JSONArray getLiveRowCount(Connection dbc) { return databaseInfoDAO.getLiveRowCount(dbc); }
    
}
