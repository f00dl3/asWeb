/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 22 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.UtilityUseDAO;
import org.json.JSONArray;

public class GetUtilityUseAction {
    
    private UtilityUseDAO utilityUseDAO;
    public GetUtilityUseAction(UtilityUseDAO utilityUseDAO) { this.utilityUseDAO = utilityUseDAO; }
    
    public JSONArray getChUseElecD() { return utilityUseDAO.getChUseElecD(); }
    public JSONArray getChUseGas() { return utilityUseDAO.getChUseGas(); }
    public JSONArray getChWebData() { return utilityUseDAO.getChWebData(); }
    public JSONArray getUseElectricity(String month) { return utilityUseDAO.getUseElectricity(month); }
    public JSONArray getUseGas(String month) { return utilityUseDAO.getUseGas(month); }
    public JSONArray getUseInternet(String month) { return utilityUseDAO.getUseInternet(month); }
    public JSONArray getUsePhone(String month) { return utilityUseDAO.getUsePhone(month); }
    
}
