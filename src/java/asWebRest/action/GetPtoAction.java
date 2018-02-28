/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 27 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.PtoDAO;
import org.json.JSONArray;

public class GetPtoAction {
    
    private PtoDAO ptoDAO;
    public GetPtoAction(PtoDAO ptoDAO) { this.ptoDAO = ptoDAO; }
    public JSONArray getPto() { return ptoDAO.getPto(); }
    
}
