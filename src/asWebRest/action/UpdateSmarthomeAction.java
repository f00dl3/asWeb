/*
by Anthony Stump
Created: 29 Nov 2019
Updated: on creation
 */

package asWebRest.action;

import asWebRest.dao.SmarthomeDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateSmarthomeAction {
	
    private SmarthomeDAO SmarthomeDAO;
    public UpdateSmarthomeAction(SmarthomeDAO SmarthomeDAO) { this.SmarthomeDAO = SmarthomeDAO; }
    
    public String setDoorEvent(Connection dbc, List qParams) { return SmarthomeDAO.setDoorEvent(dbc, qParams); }
    
}
