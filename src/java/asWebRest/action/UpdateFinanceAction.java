/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 6 May 2018
 */

package asWebRest.action;

import asWebRest.dao.FinanceDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateFinanceAction {
    
    private FinanceDAO financeDAO;
    public UpdateFinanceAction(FinanceDAO financeDAO) { this.financeDAO = financeDAO; }
    
    public String setAssetTrackUpdate(Connection dbc, List qParams) { return financeDAO.setAssetTrackUpdate(dbc, qParams); }
    public String setAutoMpgAdd(Connection dbc, List qParams) { return financeDAO.setAutoMpgAdd(dbc, qParams); }
    public String setCheckbookAdd(Connection dbc, List qParams) { return financeDAO.setCheckbookAdd(dbc, qParams); }
    public String setCheckbookUpdate(Connection dbc, List qParams) { return financeDAO.setCheckbookUpdate(dbc, qParams); }
    public String setDecorToolsUpdate(Connection dbc, List qParams) { return financeDAO.setDecorToolsUpdate(dbc, qParams); }
    public String setSavingsAdd(Connection dbc, List qParams) { return financeDAO.setSavingsAdd(dbc, qParams); }

}
