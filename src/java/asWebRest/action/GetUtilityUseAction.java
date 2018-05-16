/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 16 May 2018
 */

package asWebRest.action;

import asWebRest.dao.UtilityUseDAO;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetUtilityUseAction {
    
    private UtilityUseDAO utilityUseDAO;
    public GetUtilityUseAction(UtilityUseDAO utilityUseDAO) { this.utilityUseDAO = utilityUseDAO; }
    
    public JSONArray getChCellUse(Connection dbc) { return utilityUseDAO.getChCellUse(dbc); }
    public JSONArray getChUseElecD(Connection dbc) { return utilityUseDAO.getChUseElecD(dbc); }
    public JSONArray getChUseGas(Connection dbc) { return utilityUseDAO.getChUseGas(dbc); }
    public JSONArray getChWebData(Connection dbc) { return utilityUseDAO.getChWebData(dbc); }
    public JSONObject getCombinedUtilityUseByMonth(Connection dbc, String month) { return utilityUseDAO.getCombinedUtilityUseByMonth(dbc, month); }
    public JSONArray getUsePhone(Connection dbc, String month) { return utilityUseDAO.getUsePhone(dbc, month); }
    
}
