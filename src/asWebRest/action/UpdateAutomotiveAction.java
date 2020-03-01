/*
by Anthony Stump
Created: 25 Mar 2018
Split from UpdateFinanceAction 1 Mar 2020
Updated: 1 Mar 2020
 */

package asWebRest.action;

import asWebRest.dao.AutomotiveDAO;
import asWebRest.dao.FinanceDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateAutomotiveAction {
    
    private AutomotiveDAO automotiveDAO;
    public UpdateAutomotiveAction(AutomotiveDAO automotiveDAO) { this.automotiveDAO = automotiveDAO; }
    
    public String setAutoMpgAdd(Connection dbc, List<String> qParams) { return automotiveDAO.setAutoMpgAdd(dbc, qParams); }
    public String setAutoMpgAddHondaCivic(Connection dbc, List<String> qParams) { return automotiveDAO.setAutoMpgAddHondaCivic(dbc, qParams); }

}
