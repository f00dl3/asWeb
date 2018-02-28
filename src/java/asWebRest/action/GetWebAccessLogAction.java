/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 13 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.WebAccessLogDAO;
import org.json.JSONArray;

public class GetWebAccessLogAction {
    
    private WebAccessLogDAO webAccessLogDAO;
    public GetWebAccessLogAction(WebAccessLogDAO webAccessLogDAO) { this.webAccessLogDAO = webAccessLogDAO; }
    public JSONArray getWebAccessLogs() { return webAccessLogDAO.getWebAccessLog(); }
    
}
