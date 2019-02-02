/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 2 Feb 2019
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetEntertainmentAction {
    
    private EntertainmentDAO entertainmentDAO;
    public GetEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public JSONArray getChicagoSeries(Connection dbc) { return entertainmentDAO.getChicagoSeries(dbc); }
    public JSONArray getFfxivAssets(Connection dbc) { return entertainmentDAO.getFfxivAssets(dbc); }
    public JSONArray getFfxivCounts(Connection dbc) { return entertainmentDAO.getFfxivCounts(dbc); }
    public JSONArray getFfxivCrafting(Connection dbc) { return entertainmentDAO.getFfxivCrafting(dbc); }
    public JSONArray getFfxivDungeons(Connection dbc) { return entertainmentDAO.getFfxivDungeons(dbc); }
    public JSONArray getFfxivEmotes(Connection dbc) { return entertainmentDAO.getFfxivEmotes(dbc); }
    public JSONArray getFfxivGilByDate(Connection dbc) { return entertainmentDAO.getFfxivGilByDate(dbc); }
    public JSONArray getFfxivImageMaps(Connection dbc) { return entertainmentDAO.getFfxivImageMaps(dbc); }
    public JSONArray getFfxivItems(Connection dbc) { return entertainmentDAO.getFfxivItems(dbc); }
    public JSONArray getFfxivMerged(Connection dbc, int minRange, int maxRange, String completed) { return entertainmentDAO.getFfxivMerged(dbc, minRange, maxRange, completed); }
    public JSONArray getFfxivQuestsByDate(Connection dbc) { return entertainmentDAO.getFfxivQuestsByDate(dbc); }
    public JSONArray getFfxivQuests(Connection dbc, int minRange, int maxRange, String completed) { return entertainmentDAO.getFfxivQuests(dbc, minRange, maxRange, completed); }
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
