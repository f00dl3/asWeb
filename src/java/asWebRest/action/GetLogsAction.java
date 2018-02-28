/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 22 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.LogsDAO;
import java.util.List;
import org.json.JSONArray;

public class GetLogsAction {
    
    private LogsDAO logsDAO;
    public GetLogsAction(LogsDAO logsDAO) { this.logsDAO = logsDAO; }
    
    public JSONArray getCameras(List qParams, String order) { return logsDAO.getCameras(qParams, order); }
    public JSONArray getPlainTextNotes(List qParams) { return logsDAO.getPlainTextNotes(qParams); }
    public JSONArray getSdUtils() { return logsDAO.getSdUtils(); }
    public JSONArray getSystemBackup() { return logsDAO.getSystemBackup(); }
    
}
