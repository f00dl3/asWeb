/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 16 Dec 2019
 */

package asWebRest.action;

import asWebRest.dao.WebCalDAO;
import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetWebCalAction {
    
    private WebCalDAO webCalDAO;
    public GetWebCalAction(WebCalDAO webCalDAO) { this.webCalDAO = webCalDAO; }
    public String generate_iCal(Connection dbc) { return webCalDAO.generate_iCal(dbc); }
    public JSONArray getEventsBasic(Connection dbc) { return webCalDAO.getEventsBasic(dbc); }
    public JSONObject getLastLogId(Connection dbc) { return webCalDAO.getLastLogId(dbc); }
    
}