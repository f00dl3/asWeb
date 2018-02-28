/*
by Anthony Stump
Created: 18 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetCookingAction;
import asWebRest.dao.CookingDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CookingResource extends ServerResource {
    
    @Get
    public String represent() {
        GetCookingAction getCookingAction = new GetCookingAction(new CookingDAO());
        JSONArray recipies = getCookingAction.getRecipies();  
        return recipies.toString();
    }
    
}
