/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 29 Mar 2018
 */

package asWebRest.action;

import asWebRest.dao.FinanceDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateFinanceAction {
    
    private FinanceDAO financeDAO;
    public UpdateFinanceAction(FinanceDAO financeDAO) { this.financeDAO = financeDAO; }
    
    public String setAssetTrackUpdate(Connection dbc, List qParams) { return financeDAO.setAssetTrackUpdate(dbc, qParams); }
    public String setDecorToolsUpdate(Connection dbc, List qParams) { return financeDAO.setDecorToolsUpdate(dbc, qParams); }

}
