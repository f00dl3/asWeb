/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.FinanceDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetFinanceAction {
    
    private FinanceDAO financeDAO;
    public GetFinanceAction(FinanceDAO financeDAO) { this.financeDAO = financeDAO; }
    
    public JSONArray get3NetWorth() { return financeDAO.get3NetWorth(); }
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
    public JSONArray getCkBk() { return financeDAO.getCkBk(); }
    public JSONArray getCkBkRange() { return financeDAO.getCkBkRange(); }
    public JSONArray getDecorTools(Connection dbc) { return financeDAO.getDecorTools(dbc); }
    public JSONArray getEnw() { return financeDAO.getEnw(); }
    public JSONArray getEnwt() { return financeDAO.getEnwt(); }
    public JSONArray getLicenses(Connection dbc) { return financeDAO.getLicenses(dbc); }
    public JSONArray getMort(Connection dbc) { return financeDAO.getMort(dbc); }
    public JSONArray getNwga() { return financeDAO.getNwga(); }
    public JSONArray getQMerged(Connection dbc) { return financeDAO.getQMerged(dbc); }
    public JSONArray getSaving(Connection dbc) { return financeDAO.getSaving(dbc); }
    public JSONArray getSettingC() { return financeDAO.getSettingC(); }
    public JSONArray getSettingH() { return financeDAO.getSettingH(); }
    public JSONArray getSvBk(Connection dbc) { return financeDAO.getSvBk(dbc); }
    public JSONArray getUURel() { return financeDAO.getUURel(); }
    
}
