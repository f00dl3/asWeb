/*
by Anthony Stump
Created: 18 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetMediaServerAction;
import asWebRest.dao.MediaServerDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class MediaServerResource extends ServerResource {
    
    @Get
    public String represent() {
        GetMediaServerAction getMediaServerAction = new GetMediaServerAction(new MediaServerDAO());
        JSONArray xFilesData = getMediaServerAction.getXFiles();  
        return xFilesData.toString();
    }
    
}
