/*
by Anthony Stump
Created: 25 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebCalAction;
import asWebRest.dao.WebCalDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class WebCalResource extends ServerResource {
    
    @Get
    public String represent() {
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        JSONArray llid = getWebCalAction.getLastLogId();  
        return llid.toString();
    }
    
}
