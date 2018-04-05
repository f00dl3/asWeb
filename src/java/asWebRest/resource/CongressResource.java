/*
by Anthony Stump
Created: 5 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetCongressAction;
import asWebRest.dao.CongressDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CongressResource extends ServerResource {
    
    @Get
    public String represent() {
        GetCongressAction getCongressAction = new GetCongressAction(new CongressDAO());
        JSONArray hack = getCongressAction.getCongressHack();  
        return hack.toString();
    }
    
}
