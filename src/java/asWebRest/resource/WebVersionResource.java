/*
by Anthony Stump
Created: 15 Feb 2018
Updated: 3 Jun 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebVersionAction;
import asWebRest.dao.WebVersionDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;

public class WebVersionResource extends ServerResource {
    
    @Get @Options
    public String represent() {
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        GetWebVersionAction getWebVersionAction = new GetWebVersionAction(new WebVersionDAO());
        JSONArray webVersions = getWebVersionAction.getWebVersion(dbc);  
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        return webVersions.toString();
    }
    
}
