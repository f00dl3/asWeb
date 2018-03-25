/*
by Anthony Stump
Created: 25 Mar 2018
 */

package asWebRest.action;

import asWebRest.dao.WebCalDAO;
import org.json.JSONArray;

public class GetWebCalAction {
    
    private WebCalDAO webCalDAO;
    public GetWebCalAction(WebCalDAO webCalDAO) { this.webCalDAO = webCalDAO; }
    public JSONArray getLastLogId() { return webCalDAO.getLastLogId(); }
    
}