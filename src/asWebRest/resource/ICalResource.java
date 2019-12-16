/*
by Anthony Stump
Created: 14 Dec 2019
Updated: 16 Dec 2019
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

        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        
    	String iCalDump = "";
        
    	try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        
        try { iCalDump = getWebCalAction.generate_iCal(dbc); } catch (Exception e) { e.printStackTrace(); } 
                    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return iCalDump;
        
    
    }
        
}
