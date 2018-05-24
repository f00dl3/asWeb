/*
by Anthony Stump
Created: 22 May 2018
Updated: 24 May 2018
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateEntertainmentAction {
    
    final private EntertainmentDAO entertainmentDAO;
    public UpdateEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public String setFfxivQuestDone(Connection dbc, List qParams) { return entertainmentDAO.setFfxivQuestDone(dbc, qParams); }
    public String setPlayedGameHours(Connection dbc, List qParams) { return entertainmentDAO.setPlayedGameHours(dbc, qParams); }

}
