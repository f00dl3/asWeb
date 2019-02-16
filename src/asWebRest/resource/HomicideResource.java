/*
by Anthony Stump
Created: 20 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetHomicideAction;
import asWebRest.dao.HomicideDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class HomicideResource extends ServerResource {
    
    @Get
    public String represent() {
        GetHomicideAction getHomicideAction = new GetHomicideAction(new HomicideDAO());
        JSONArray kills = getHomicideAction.getHomicide();  
        return kills.toString();
    }
    
}
