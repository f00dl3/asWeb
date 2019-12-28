/*
by Anthony Stump
Created: 22 Apr 2018
Updated: 23 Dec 2019
 */

package asWebRest.action;

import asWebRest.dao.WebCalDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateWebCalAction {
    
    private WebCalDAO webCalDAO;
    public UpdateWebCalAction(WebCalDAO webCalDAO) { this.webCalDAO = webCalDAO; }
    
    public String setAddEntry(Connection dbc, List qParams) { return webCalDAO.setAddEntry(dbc, qParams); }
    public String setAddEntryLog(Connection dbc, List qParams) { return webCalDAO.setAddEntryLog(dbc, qParams); }
    public String setAddEntryRepeats(Connection dbc, List qParams) { return webCalDAO.setAddEntryRepeats(dbc, qParams); }
    public String setAddEntryUser(Connection dbc, List qParams) { return webCalDAO.setAddEntryUser(dbc, qParams); }
    public String setDeleteEvent(Connection dbc, List qParams) { return webCalDAO.setDeleteEvent(dbc, qParams); }
        
}
