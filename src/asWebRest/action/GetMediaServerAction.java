/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 28 Mar 2019
 */

package asWebRest.action;

import asWebRest.dao.MediaServerDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetMediaServerAction {
    
    private MediaServerDAO mediaServerDAO;
    public GetMediaServerAction(MediaServerDAO mediaServerDAO) { this.mediaServerDAO = mediaServerDAO; }
    
    public JSONArray getGeoData(Connection dbc) { return mediaServerDAO.getGeoData(dbc); }
    public JSONArray getIndexed(Connection dbc, List qParams, String isMobile) { return mediaServerDAO.getIndexed(dbc, qParams, isMobile); }
    public JSONArray getIndexedByDate(Connection dbc) { return mediaServerDAO.getIndexedByDate(dbc); }
    public JSONArray getOverview(Connection dbc) { return mediaServerDAO.getOverview(dbc); }
    public JSONArray getPowerRangers(Connection dbc) { return mediaServerDAO.getPowerRangers(dbc); }
    public JSONArray getXFiles(Connection dbc) { return mediaServerDAO.getXFiles(dbc); }
        
}
