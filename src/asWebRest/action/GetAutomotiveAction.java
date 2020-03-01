/*
by Anthony Stump
Created: 19 Feb 2018
Split from GetFinanceAction 1 Mar 2020
Updated: 1 Mar 2020
 */

package asWebRest.action;

import asWebRest.dao.AutomotiveDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetAutomotiveAction {
    
    private AutomotiveDAO automotiveDAO;
    public GetAutomotiveAction(AutomotiveDAO automotiveDAO) { this.automotiveDAO = automotiveDAO; }
    
    public JSONArray getAutoBillSum(Connection dbc) { return automotiveDAO.getAutoBillSum(dbc); }
    public JSONArray getAutoBillSumHondaCivic(Connection dbc) { return automotiveDAO.getAutoBillSumHondaCivic(dbc); }
    public JSONArray getAutoMaint(Connection dbc) { return automotiveDAO.getAutoMaint(dbc); }
    public JSONArray getAutoMaintHondaCivic(Connection dbc) { return automotiveDAO.getAutoMaintHondaCivic(dbc); }
    public JSONArray getAutoMpg(Connection dbc) { return automotiveDAO.getAutoMpg(dbc); }
    public JSONArray getAutoMpgHondaCivic(Connection dbc) { return automotiveDAO.getAutoMpgHondaCivic(dbc); }
    public JSONArray getAutoMpgAverage(Connection dbc) { return automotiveDAO.getAutoMpgAverage(dbc); }
    public JSONArray getAutoMpgAverageHondaCivic(Connection dbc) { return automotiveDAO.getAutoMpgAverageHondaCivic(dbc); }
    
}
