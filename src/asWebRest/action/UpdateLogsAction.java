/*
by Anthony Stump
Created: 11 Jan 2020
Updated: on creation
 */

package asWebRest.action;

import asWebRest.dao.LogsDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateLogsAction {
    
    private LogsDAO logsDAO;
    public UpdateLogsAction(LogsDAO logsDAO) { this.logsDAO = logsDAO; }
    
    public String setDiscordAccess(Connection dbc, List qParams) { return logsDAO.setDiscordAccess(dbc, qParams); }
        
}
