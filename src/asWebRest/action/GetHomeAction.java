/*
by Anthony Stump
Created: 4 Apr 2018
Updated: 29 Dec 2018
*/

package asWebRest.action;

import asWebRest.dao.HomeDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetHomeAction {
    
    private HomeDAO homeDAO;
    public GetHomeAction(HomeDAO homeDAO) { this.homeDAO = homeDAO; }
    
    public JSONArray getAlarmBatteries(Connection dbc) { return homeDAO.getAlarmBatteries(dbc); }
    public JSONArray getHouseDeepCleaning(Connection dbc) { return homeDAO.getHouseDeepCleaning(dbc); }
    public JSONArray getMeasure(Connection dbc, List qParams) { return homeDAO.getMeasure(dbc, qParams); }
    
}
