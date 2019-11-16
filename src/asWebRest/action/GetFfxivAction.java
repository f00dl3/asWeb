/*
by Anthony Stump
Created: 20 Feb 2018
Split from GetEntertinmentAction.java on 14 Nov 2019
Updated: 16 Nov 2019
 */

package asWebRest.action;

import asWebRest.dao.FfxivDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetFfxivAction {
    
    private FfxivDAO ffxivDAO;
    public GetFfxivAction(FfxivDAO ffxivDAO) { this.ffxivDAO = ffxivDAO; }

    public JSONArray getFfxivAssets(Connection dbc) { return ffxivDAO.getFfxivAssets(dbc); }
    public JSONArray getFfxivCounts(Connection dbc) { return ffxivDAO.getFfxivCounts(dbc); }
    public JSONArray getFfxivCrafting(Connection dbc) { return ffxivDAO.getFfxivCrafting(dbc); }
    public JSONArray getFfxivDungeons(Connection dbc) { return ffxivDAO.getFfxivDungeons(dbc); }
    public JSONArray getFfxivEmotes(Connection dbc) { return ffxivDAO.getFfxivEmotes(dbc); }
    public JSONArray getFfxivGilByDate(Connection dbc) { return ffxivDAO.getFfxivGilByDate(dbc); }
    public JSONArray getFfxivImageMaps(Connection dbc) { return ffxivDAO.getFfxivImageMaps(dbc); }
    public JSONArray getFfxivItems(Connection dbc) { return ffxivDAO.getFfxivItems(dbc); }
    public JSONArray getFfxivLevelsCurrent(Connection dbc) { return ffxivDAO.getFfxivLevelsCurrent(dbc); }
    public JSONArray getFfxivMerged(Connection dbc, int minRange, int maxRange, String completed) { return ffxivDAO.getFfxivMerged(dbc, minRange, maxRange, completed); }
    public JSONArray getFfxivQuestsByDate(Connection dbc) { return ffxivDAO.getFfxivQuestsByDate(dbc); }
    public JSONArray getFfxivQuests(Connection dbc, int minRange, int maxRange, String completed) { return ffxivDAO.getFfxivQuests(dbc, minRange, maxRange, completed); }
    
}
