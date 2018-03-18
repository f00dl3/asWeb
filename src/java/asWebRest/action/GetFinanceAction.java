/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 18 Mar 2018
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
    public JSONArray getAutoMpg() { return financeDAO.getAutoMpg(); }
    public JSONArray getBGames() { return financeDAO.getBGames(); }
    public JSONArray getBills() { return financeDAO.getBills(); }
    public JSONArray getBooks() { return financeDAO.getBooks(); }
    public JSONArray getChecking() { return financeDAO.getChecking(); }
    public JSONArray getCkBk() { return financeDAO.getCkBk(); }
    public JSONArray getCkBkRange() { return financeDAO.getCkBkRange(); }
    public JSONArray getDecorTools() { return financeDAO.getDecorTools(); }
    public JSONArray getEnw() { return financeDAO.getEnw(); }
    public JSONArray getEnwt() { return financeDAO.getEnwt(); }
    public JSONArray getMort() { return financeDAO.getMort(); }
    public JSONArray getNwga() { return financeDAO.getNwga(); }
    public JSONArray getQBGames() { return financeDAO.getQBGames(); }
    public JSONArray getQBooks() { return financeDAO.getQBooks(); }
    public JSONArray getQDecorTools() { return financeDAO.getQDecorTools(); }
    public JSONArray getQLicenses() { return financeDAO.getQLicenses(); }
    public JSONArray getQMedia() { return financeDAO.getQMedia(); }
    public JSONArray getSaving() { return financeDAO.getSaving(); }
    public JSONArray getSettingC() { return financeDAO.getSettingC(); }
    public JSONArray getSettingH() { return financeDAO.getSettingH(); }
    public JSONArray getSvBk() { return financeDAO.getSvBk(); }
    public JSONArray getUURel() { return financeDAO.getUURel(); }
    
}
