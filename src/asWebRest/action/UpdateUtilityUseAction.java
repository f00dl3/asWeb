/*
by Anthony Stump
Created: 25 Jan 2020
Updated: 29 Jan 2020
 */

package asWebRest.action;

import asWebRest.dao.UtilityUseDAO;

import java.sql.Connection;
import java.util.List;

public class UpdateUtilityUseAction {
    
	private UtilityUseDAO utilityUseDAO;
	public UpdateUtilityUseAction(UtilityUseDAO utilityUseDAO) { this.utilityUseDAO = utilityUseDAO; }
    
	public String setElectricityUse(Connection dbc, List<String> qParams) { return utilityUseDAO.setElectricityUse(dbc, qParams); }
	public String setGasUse(Connection dbc, List<String> qParams) { return utilityUseDAO.setGasUse(dbc, qParams); }

}
