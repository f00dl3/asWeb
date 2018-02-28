/*
by Anthony Stump
Created: 15 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebVersionAction;
import asWebRest.dao.WebVersionDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class WebVersionResource extends ServerResource {
    
    @Get
    public String represent() {
        GetWebVersionAction getWebVersionAction = new GetWebVersionAction(new WebVersionDAO());
        JSONArray webVersions = getWebVersionAction.getWebVersion();  
        return webVersions.toString();
    }
    
}
