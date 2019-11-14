/*
by Anthony Stump
Created: 22 May 2018
Updated: 14 Nov 2019
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateEntertainmentAction {
    
    final private EntertainmentDAO entertainmentDAO;
    public UpdateEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public String setPlayedGameHours(Connection dbc, List qParams) { return entertainmentDAO.setPlayedGameHours(dbc, qParams); }

}
