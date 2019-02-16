/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.PtoDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetPtoAction {
    
    private PtoDAO ptoDAO;
    public GetPtoAction(PtoDAO ptoDAO) { this.ptoDAO = ptoDAO; }
    public JSONArray getPto(Connection dbc) { return ptoDAO.getPto(dbc); }
    
}
