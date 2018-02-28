/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 27 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.EntertainmentDAO;
import org.json.JSONArray;

public class GetEntertainmentAction {
    
    private EntertainmentDAO entertainmentDAO;
    public GetEntertainmentAction(EntertainmentDAO entertainmentDAO) { this.entertainmentDAO = entertainmentDAO; }
    
    public JSONArray getFfxivQuests() { return entertainmentDAO.getFfxivQuests(); }
    public JSONArray getGameHours() { return entertainmentDAO.getGameHours(); }
    public JSONArray getGameHoursLatest() { return entertainmentDAO.getGameHoursLatest(); }
    public JSONArray getGameHoursTotal() { return entertainmentDAO.getGameHoursTotal(); }
    public JSONArray getGameIndex() { return entertainmentDAO.getGameIndex(); }
    public JSONArray getGoosebumpsBooks() { return entertainmentDAO.getGoosebumpsBooks(); }
    public JSONArray getLego() { return entertainmentDAO.getLego(); }
    public JSONArray getStarTrek() { return entertainmentDAO.getStarTrek(); }
    public JSONArray getXTags() { return entertainmentDAO.getXTags(); }
    
}
