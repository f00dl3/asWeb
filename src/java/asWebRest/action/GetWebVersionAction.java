/*
by Anthony Stump
Created: 15 Feb 2018
Updated: 23 Jun 2018
 */

package asWebRest.action;

import asWebRest.dao.WebVersionDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetWebVersionAction {
    
    private WebVersionDAO webVersionDAO;
    public GetWebVersionAction(WebVersionDAO webVersionDAO) { this.webVersionDAO = webVersionDAO; }
    
    public JSONArray getCurrentVer(Connection dbc) { return webVersionDAO.getCurrentVer(dbc); }
    public JSONArray getWebVersion(Connection dbc) { return webVersionDAO.getWebVersion(dbc); }
    public JSONArray getWebVersionAuto(Connection dbc) throws Exception { return webVersionDAO.getWebVersionAuto(dbc); }
    
}
