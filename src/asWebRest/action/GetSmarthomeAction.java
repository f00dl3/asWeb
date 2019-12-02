/*
by Anthony Stump
Created: 29 Nov 2019
Updated: 1 Dec 2019
 */

package asWebRest.action;

import asWebRest.dao.SmarthomeDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetSmarthomeAction {
    
    private SmarthomeDAO SmarthomeDAO;
    public GetSmarthomeAction(SmarthomeDAO SmarthomeDAO) { this.SmarthomeDAO = SmarthomeDAO; }

    public JSONArray getArmDisarm(Connection dbc) { return SmarthomeDAO.getArmDisarm(dbc); }
    public JSONArray getDoorEvents(Connection dbc) { return SmarthomeDAO.getDoorEvents(dbc); }
    
}
