/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 29 Aug 2018
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetEntertainmentAction {
    
    private EntertainmentDAO entertainmentDAO;
    public GetEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public JSONArray getFfxivDungeons(Connection dbc) { return entertainmentDAO.getFfxivDungeons(dbc); }
    public JSONArray getFfxivItems(Connection dbc) { return entertainmentDAO.getFfxivItems(dbc); }
    public JSONArray getFfxivQuests(Connection dbc) { return entertainmentDAO.getFfxivQuests(dbc); }
    public JSONArray getFfxivQuestsByDate(Connection dbc) { return entertainmentDAO.getFfxivQuestsByDate(dbc); }
    public JSONArray getGameHours(Connection dbc) { return entertainmentDAO.getGameHours(dbc); }
    public JSONArray getGameHoursLatest(Connection dbc) { return entertainmentDAO.getGameHoursLatest(dbc); }
    public JSONArray getGameHoursTotal(Connection dbc) { return entertainmentDAO.getGameHoursTotal(dbc); }
    public JSONArray getGameIndex(Connection dbc) { return entertainmentDAO.getGameIndex(dbc); }
    public JSONArray getGoosebumpsBooks(Connection dbc) { return entertainmentDAO.getGoosebumpsBooks(dbc); }
    public JSONArray getLego(Connection dbc) { return entertainmentDAO.getLego(dbc); }
    public JSONArray getPowerRangers(Connection dbc) { return entertainmentDAO.getPowerRangers(dbc); }
    public JSONArray getStarTrek(Connection dbc) { return entertainmentDAO.getStarTrek(dbc); }
    public JSONArray getTrueBlood(Connection dbc) { return entertainmentDAO.getTrueBlood(dbc); }
    public JSONArray getXFiles(Connection dbc) { return entertainmentDAO.getXFiles(dbc); }
    public JSONArray getXTags(Connection dbc) { return entertainmentDAO.getXTags(dbc); }
    
}
