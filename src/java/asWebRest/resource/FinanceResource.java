/*
by Anthony Stump
Created: 19 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.dao.FinanceDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class FinanceResource extends ServerResource {
    
    @Get
    public String represent() {
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        JSONArray amSch = getFinanceAction.getAmSch();  
        return amSch.toString();
    }
    
}
