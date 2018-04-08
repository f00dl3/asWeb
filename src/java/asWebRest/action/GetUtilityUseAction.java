/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.UtilityUseDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetUtilityUseAction {
    
    private UtilityUseDAO utilityUseDAO;
    public GetUtilityUseAction(UtilityUseDAO utilityUseDAO) { this.utilityUseDAO = utilityUseDAO; }
    
    public JSONArray getChUseElecD() { return utilityUseDAO.getChUseElecD(); }
    public JSONArray getChUseGas() { return utilityUseDAO.getChUseGas(); }
    public JSONArray getChWebData() { return utilityUseDAO.getChWebData(); }
    public JSONArray getCombinedUtilityUseByMonth(Connection dbc, String month) { return utilityUseDAO.getCombinedUtilityUseByMonth(dbc, month); }
    public JSONArray getUsePhone(Connection dbc, String month) { return utilityUseDAO.getUsePhone(dbc, month); }
    
}
