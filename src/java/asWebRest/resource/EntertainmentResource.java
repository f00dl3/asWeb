/*
by Anthony Stump
Created: 20 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetEntertainmentAction;
import asWebRest.dao.EntertainmentDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class EntertainmentResource extends ServerResource {
    
    @Get
    public String represent() {
        GetEntertainmentAction getEntertanimentAction = new GetEntertainmentAction(new EntertainmentDAO());
        JSONArray gameHours = getEntertanimentAction.getGameHours();  
        return gameHours.toString();
    }
    
}
