/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 30 Mar 2018
 */

package asWebRest.action;

import asWebRest.dao.DatabaseInfoDAO;
import org.json.JSONArray;

public class GetDatabaseInfoAction {
    
    private DatabaseInfoDAO databaseInfoDAO;
    public GetDatabaseInfoAction(DatabaseInfoDAO databaseInfoDAO) { this.databaseInfoDAO = databaseInfoDAO; }
    
    public JSONArray getDatabaseInfo() { return databaseInfoDAO.getDbInfo(); }
    public JSONArray getDatabaseInfoByTable() { return databaseInfoDAO.getDbInfoByTable(); }
    
}
