/*
by Anthony Stump
Created: 21 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.MediaServerDAO;

public class UpdateMediaServerAction {
    
    private MediaServerDAO mediaServerDAO;
    public UpdateMediaServerAction(MediaServerDAO mediaServerDAO) { this.mediaServerDAO = mediaServerDAO; }
    
    public String setLastPlayed() { return mediaServerDAO.setLastPlayed(); }
        
}
