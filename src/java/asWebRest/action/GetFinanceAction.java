/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 4 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.FinanceDAO;
import org.json.JSONArray;

public class GetFinanceAction {
    
    private FinanceDAO financeDAO;
    public GetFinanceAction(FinanceDAO financeDAO) { this.financeDAO = financeDAO; }
    
    public JSONArray get3NetWorth() { return financeDAO.get3NetWorth(); }
    public JSONArray getAmSch() { return financeDAO.getAmSch(); }
    public JSONArray getAssetTrack() { return financeDAO.getAssetTrack(); }
    public JSONArray getAutoBillSum() { return financeDAO.getAutoBillSum(); }
    public JSONArray getAutoMaint() { return financeDAO.getAutoMaint(); }
    public JSONArray getAutoMpg() { return financeDAO.getAutoMpg(); }
    public JSONArray getAutoMpgAverage() { return financeDAO.getAutoMpgAverage(); }
    public JSONArray getBGames() { return financeDAO.getBGames(); }
    public JSONArray getBills() { return financeDAO.getBills(); }
    public JSONArray getBooks() { return financeDAO.getBooks(); }
    public JSONArray getChecking() { return financeDAO.getChecking(); }
    public JSONArray getCkBk() { return financeDAO.getCkBk(); }
    public JSONArray getCkBkRange() { return financeDAO.getCkBkRange(); }
    public JSONArray getDecorTools() { return financeDAO.getDecorTools(); }
    public JSONArray getEnw() { return financeDAO.getEnw(); }
    public JSONArray getEnwt() { return financeDAO.getEnwt(); }
    public JSONArray getLicenses() { return financeDAO.getLicenses(); }
    public JSONArray getMort() { return financeDAO.getMort(); }
    public JSONArray getNwga() { return financeDAO.getNwga(); }
    public JSONArray getQMerged() { return financeDAO.getQMerged(); }
    public JSONArray getSaving() { return financeDAO.getSaving(); }
    public JSONArray getSettingC() { return financeDAO.getSettingC(); }
    public JSONArray getSettingH() { return financeDAO.getSettingH(); }
    public JSONArray getSvBk() { return financeDAO.getSvBk(); }
    public JSONArray getUURel() { return financeDAO.getUURel(); }
    
}
