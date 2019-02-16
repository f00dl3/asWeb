/*
by Anthony Stump
Created: 16 Dec 2018
 */

package asWebRest.resource;

import asWebRest.action.GetCrashDataAction;
import asWebRest.dao.CrashDataDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CrashDataResource extends ServerResource {
    
        
        
    @Get
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetCrashDataAction getCrashDataAction = new GetCrashDataAction(new CrashDataDAO());
        JSONArray crashData = getCrashDataAction.getCrashData(dbc);  
        return crashData.toString();
        
    }
    
}
