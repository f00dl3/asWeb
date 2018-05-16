/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 15 May 2018
 */

package asWebRest.action;

import asWebRest.dao.FinanceDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetFinanceAction {
    
    private FinanceDAO financeDAO;
    public GetFinanceAction(FinanceDAO financeDAO) { this.financeDAO = financeDAO; }
    
    public JSONArray get3NetWorth(Connection dbc) { return financeDAO.get3NetWorth(dbc); }
    public JSONArray getAmSch(Connection dbc) { return financeDAO.getAmSch(dbc); }
    public JSONArray getAssetTrack(Connection dbc) { return financeDAO.getAssetTrack(dbc); }
    public JSONArray getAutoBillSum(Connection dbc) { return financeDAO.getAutoBillSum(dbc); }
    public JSONArray getAutoMaint(Connection dbc) { return financeDAO.getAutoMaint(dbc); }
    public JSONArray getAutoMpg(Connection dbc) { return financeDAO.getAutoMpg(dbc); }
    public JSONArray getAutoMpgAverage(Connection dbc) { return financeDAO.getAutoMpgAverage(dbc); }
    public JSONArray getBGames(Connection dbc) { return financeDAO.getBGames(dbc); }
    public JSONArray getBills(Connection dbc) { return financeDAO.getBills(dbc); }
    public JSONArray getBooks(Connection dbc) { return financeDAO.getBooks(dbc); }
    public JSONArray getChecking(Connection dbc) { return financeDAO.getChecking(dbc); }
    public JSONArray getCkBk(Connection dbc) { return financeDAO.getCkBk(dbc); }
    public JSONArray getCkBkComb(Connection dbc) { return financeDAO.getCkBkComb(dbc); }
    public JSONArray getDecorTools(Connection dbc) { return financeDAO.getDecorTools(dbc); }
    public JSONArray getEnw(Connection dbc) { return financeDAO.getEnw(dbc); }
    public JSONArray getEnwChart(Connection dbc) { return financeDAO.getEnwChart(dbc); }
    public JSONArray getEnwt(Connection dbc) { return financeDAO.getEnwt(dbc); }
    public JSONArray getLicenses(Connection dbc) { return financeDAO.getLicenses(dbc); }
    public JSONArray getMort(Connection dbc) { return financeDAO.getMort(dbc); }
    public JSONArray getNwga(Connection dbc) { return financeDAO.getNwga(dbc); }
    public JSONArray getQMerged(Connection dbc) { return financeDAO.getQMerged(dbc); }
    public JSONArray getSaving(Connection dbc) { return financeDAO.getSaving(dbc); }
    public JSONArray getSavingChart(Connection dbc, List qParams) { return financeDAO.getSavingChart(dbc, qParams); }
    public JSONArray getSettingC(Connection dbc) { return financeDAO.getSettingC(dbc); }
    public JSONArray getSettingH(Connection dbc) { return financeDAO.getSettingH(dbc); }
    public JSONArray getSvBk(Connection dbc) { return financeDAO.getSvBk(dbc); }
    
}
