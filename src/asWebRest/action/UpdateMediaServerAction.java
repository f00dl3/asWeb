/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 15 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.MediaServerDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateMediaServerAction {
    
    private MediaServerDAO mediaServerDAO;
    public UpdateMediaServerAction(MediaServerDAO mediaServerDAO) { this.mediaServerDAO = mediaServerDAO; }
    
    public String setLastPlayed(Connection dbc, List qParams) { return mediaServerDAO.setLastPlayed(dbc, qParams); }
        
}
