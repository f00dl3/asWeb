/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetUtilityUseAction;
import asWebRest.dao.UtilityUseDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class UtilityUseResource extends ServerResource {
    
    @Get
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        final String agMonV = "2018-01";
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());
        JSONArray callMe = getUtilityUseAction.getUsePhone(dbc, agMonV);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return callMe.toString();
    }
    
}
