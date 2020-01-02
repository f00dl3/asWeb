/*
by Anthony Stump
Created: 29 Nov 2019
Updated: 2 Jan 2020
 */

package asWebRest.action;

import asWebRest.dao.SmarthomeDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateSmarthomeAction {
	
    private SmarthomeDAO SmarthomeDAO;
    public UpdateSmarthomeAction(SmarthomeDAO SmarthomeDAO) { this.SmarthomeDAO = SmarthomeDAO; }

    public String setArmDisarm(Connection dbc, List qParams) { return SmarthomeDAO.setArmDisarm(dbc, qParams); }
    public String setCo2FireEvent(Connection dbc, List qParams) { return SmarthomeDAO.setCo2FireEvent(dbc, qParams); }
    public String setDoorEvent(Connection dbc, List qParams) { return SmarthomeDAO.setDoorEvent(dbc, qParams); }
    
}
