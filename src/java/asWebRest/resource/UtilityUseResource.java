/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 4 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetUtilityUseAction;
import asWebRest.dao.UtilityUseDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class UtilityUseResource extends ServerResource {
    
    @Get
    public String represent() {
        final String agMonV = "2018-01";
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());
        JSONArray callMe = getUtilityUseAction.getUsePhone(agMonV);  
        return callMe.toString();
    }
    
    // figure out agMonV w/o billions of queries!
    
}
