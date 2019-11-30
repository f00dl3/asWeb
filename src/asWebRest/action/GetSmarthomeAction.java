/*
by Anthony Stump
Created: 29 Nov 2019
Updated: on creation
 */

package asWebRest.action;

import asWebRest.dao.SmarthomeDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetSmarthomeAction {
    
    private SmarthomeDAO SmarthomeDAO;
    public GetSmarthomeAction(SmarthomeDAO SmarthomeDAO) { this.SmarthomeDAO = SmarthomeDAO; }

    public JSONArray getDoorEvents(Connection dbc) { return SmarthomeDAO.getDoorEvents(dbc); }
    
}
