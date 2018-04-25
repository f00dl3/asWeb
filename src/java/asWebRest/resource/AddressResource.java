/*
by Anthony Stump
Created: 25 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetAddressBookAction;
import asWebRest.dao.AddressBookDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class AddressResource extends ServerResource {
    
    @Get
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetAddressBookAction getAddressBookAction = new GetAddressBookAction(new AddressBookDAO());
        JSONArray addys = getAddressBookAction.getAddressBook(dbc);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return addys.toString();
        
    }
    
}
