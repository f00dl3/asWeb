/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 3 Jun 2018
 */

package asWebRest.action;

import asWebRest.dao.LogsDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetLogsAction {
    
    private LogsDAO logsDAO;
    public GetLogsAction(LogsDAO logsDAO) { this.logsDAO = logsDAO; }
    
    public JSONArray getCameras(Connection dbc, List qParams, String order) { return logsDAO.getCameras(dbc, qParams, order); }
    public JSONArray getPlainTextNotes(Connection dbc, List qParams) { return logsDAO.getPlainTextNotes(dbc, qParams); }
    public JSONArray getSdUtils(Connection dbc) { return logsDAO.getSdUtils(dbc); }
    public JSONArray getSystemBackup(Connection dbc) { return logsDAO.getSystemBackup(dbc); }
    
}
