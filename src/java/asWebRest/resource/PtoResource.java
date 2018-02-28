/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 27 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetPtoAction;
import asWebRest.dao.PtoDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class PtoResource extends ServerResource {
    @Get
    public String represent() {
        GetPtoAction getPtoAction = new GetPtoAction(new PtoDAO());
        JSONArray pto = getPtoAction.getPto();  
        return pto.toString();
    }
    
}
