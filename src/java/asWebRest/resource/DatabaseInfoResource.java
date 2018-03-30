/*
by Anthony Stump
Created: 18 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetDatabaseInfoAction;
import asWebRest.dao.DatabaseInfoDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class DatabaseInfoResource extends ServerResource {
    
    @Get
    public String represent() {
        GetDatabaseInfoAction getDatabaseInfoAction = new GetDatabaseInfoAction(new DatabaseInfoDAO());
        JSONArray dbInfo2 = getDatabaseInfoAction.getDatabaseInfoByTable();  
        return dbInfo2.toString();
    }
    
}
