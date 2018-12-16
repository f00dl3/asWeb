/*
by Anthony Stump
Created: 16 Dec 2018
 */

package asWebRest.action;

import asWebRest.dao.CrashDataDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetCrashDataAction {
    
    private CrashDataDAO crashDataDAO;
    public GetCrashDataAction(CrashDataDAO crashDataDAO) { this.crashDataDAO = crashDataDAO; }
    
    public JSONArray getCrashData(Connection dbc) { return crashDataDAO.getCrashData(dbc); }
    
}
