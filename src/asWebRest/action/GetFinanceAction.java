/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 26 Mar 2020
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
    public JSONArray getBGames(Connection dbc) { return financeDAO.getBGames(dbc); }
    public JSONArray getBills(Connection dbc) { return financeDAO.getBills(dbc); }
    public JSONArray getBooks(Connection dbc) { return financeDAO.getBooks(dbc); }
    public JSONArray getChecking(Connection dbc) { return financeDAO.getChecking(dbc); }
    public JSONArray getCkBk(Connection dbc) { return financeDAO.getCkBk(dbc); }
    public JSONArray getCkBkComb(Connection dbc) { return financeDAO.getCkBkComb(dbc); }
    public int getCountyHomeValue(Connection dbc) { return financeDAO.getCountyHomeValue(dbc); }
    public JSONArray getDecorTools(Connection dbc) { return financeDAO.getDecorTools(dbc); }
    public JSONArray getEnw(Connection dbc) { return financeDAO.getEnw(dbc); }
    public JSONArray getEnwChart(Connection dbc, String periodLength) { return financeDAO.getEnwChart(dbc, periodLength); }
    public JSONArray getEnwt(Connection dbc) { return financeDAO.getEnwt(dbc); }
    public JSONArray getLicenses(Connection dbc) { return financeDAO.getLicenses(dbc); }
    public JSONArray getMort(Connection dbc) { return financeDAO.getMort(dbc); }
    public JSONArray getMortDumpFund(Connection dbc) { return financeDAO.getMortDumpFund(dbc); }
    public JSONArray getNwga(Connection dbc) { return financeDAO.getNwga(dbc); }
    public JSONArray getQMerged(Connection dbc) { return financeDAO.getQMerged(dbc); }
    public JSONArray getSaving(Connection dbc) { return financeDAO.getSaving(dbc); }
    public JSONArray getSavingChart(Connection dbc, List<String> qParams) { return financeDAO.getSavingChart(dbc, qParams); }
    public JSONArray getSettingC(Connection dbc) { return financeDAO.getSettingC(dbc); }
    public JSONArray getSettingH(Connection dbc) { return financeDAO.getSettingH(dbc); }
    public JSONArray getStockList(Connection dbc) { return financeDAO.getStockList(dbc); }
    public JSONArray getSvBk(Connection dbc) { return financeDAO.getSvBk(dbc); }
	public JSONArray getZillowPIDs(Connection dbc) { return financeDAO.getZillowPIDs(dbc); }
    
}
