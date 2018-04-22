/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 22 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.WebCalDAO;
import java.sql.Connection;
import org.json.JSONObject;

public class GetWebCalAction {
    
    private WebCalDAO webCalDAO;
    public GetWebCalAction(WebCalDAO webCalDAO) { this.webCalDAO = webCalDAO; }
    public JSONObject getLastLogId(Connection dbc) { return webCalDAO.getLastLogId(dbc); }
    
}