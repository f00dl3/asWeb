/*
by Anthony Stump
Created: 20 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.HomicideDAO;
import org.json.JSONArray;

public class GetHomicideAction {
    
    private HomicideDAO homicideDAO;
    public GetHomicideAction(HomicideDAO homicideDAO) { this.homicideDAO = homicideDAO; }
    
    public JSONArray getHomicide() { return homicideDAO.getHomicide(); }
    
}
