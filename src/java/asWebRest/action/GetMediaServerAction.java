/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 22 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.MediaServerDAO;
import java.util.List;
import org.json.JSONArray;

public class GetMediaServerAction {
    
    private MediaServerDAO mediaServerDAO;
    public GetMediaServerAction(MediaServerDAO mediaServerDAO) { this.mediaServerDAO = mediaServerDAO; }
    
    public JSONArray getGeoData() { return mediaServerDAO.getGeoData(); }
    public JSONArray getIndexed(List qParams) { return mediaServerDAO.getIndexed(qParams); }
    public JSONArray getOverview() { return mediaServerDAO.getOverview(); }
    public JSONArray getPowerRangers() { return mediaServerDAO.getPowerRangers(); }
    public JSONArray getXFiles() { return mediaServerDAO.getXFiles(); }
        
}
