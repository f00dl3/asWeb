/* 
by Anthony Stump
Refactored: 5 Apr 2018
*/

package asWebRest.action;

import asWebRest.dao.CongressDAO;
import org.json.JSONArray;

public class GetCongressAction {
	
    private CongressDAO congressDAO;
    public GetCongressAction(CongressDAO congressDAO) { this.congressDAO = congressDAO; }

    public JSONArray getCongressHack() { return congressDAO.getCongressHack(); }
	
}
