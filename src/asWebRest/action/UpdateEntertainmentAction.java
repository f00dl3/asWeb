/*
by Anthony Stump
Created: 22 May 2018
Updated: 5 Nov 2019
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateEntertainmentAction {
    
    final private EntertainmentDAO entertainmentDAO;
    public UpdateEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public String setFfxivAchievementDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivAchievementDone(dbc, qParams); }
    public String setFfxivCraftingDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivCraftingDone(dbc, qParams); }
    public String setFfxivDungeonDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivDungeonDone(dbc, qParams); }
    public String setFfxivFateDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivFateDone(dbc, qParams); }
    public String setFfxivGatheringDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivGatheringDone(dbc, qParams); }
    public String setFfxivHuntingDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivHuntingDone(dbc, qParams); }
    public String setFfxivQuestDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivQuestDone(dbc, qParams); }
    public String setPlayedGameHours(Connection dbc, List qParams) { return entertainmentDAO.setPlayedGameHours(dbc, qParams); }

}
