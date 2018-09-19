/*
by Anthony Stump
Created: 22 May 2018
Updated: 19 Sep 2018
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateEntertainmentAction {
    
    final private EntertainmentDAO entertainmentDAO;
    public UpdateEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public String setFfxivCraftingDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivCraftingDone(dbc, qParams); }
    public String setFfxivHuntingDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivHuntingDone(dbc, qParams); }
    public String setFfxivQuestDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivQuestDone(dbc, qParams); }
    public String setPlayedGameHours(Connection dbc, List qParams) { return entertainmentDAO.setPlayedGameHours(dbc, qParams); }

}
