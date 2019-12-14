/*
by Anthony Stump
Created: 14 Dec 2019
Updated: on creation
 */

package asWebRest.resource;

import asWebRest.action.GetWebCalAction;
import asWebRest.dao.WebCalDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ICalResource extends ServerResource {
    
    @Get
    
    public String represent() {
        
    	String iCalDump = "";
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return iCalDump;
        
    
    }
        
}
