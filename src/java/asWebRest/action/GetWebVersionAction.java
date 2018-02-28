/*
by Anthony Stump
Created: 15 Feb 2018
Updated: 20 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.WebVersionDAO;
import org.json.JSONArray;

public class GetWebVersionAction {
    
    private WebVersionDAO webVersionDAO;
    public GetWebVersionAction(WebVersionDAO webVersionDAO) { this.webVersionDAO = webVersionDAO; }
    
    public JSONArray getCurrentVer() { return webVersionDAO.getCurrentVer(); }
    public JSONArray getWebVersion() { return webVersionDAO.getWebVersion(); }
    
}
