/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 21 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetLogsAction;
import asWebRest.dao.LogsDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class LogsResource extends ServerResource {
    
    @Get
    public String represent() {
        List<String> qParams = new ArrayList<>();
        qParams.add("25");
        String order = "ASC";
        GetLogsAction getLogsAction = new GetLogsAction(new LogsDAO());
        JSONArray camLog = getLogsAction.getCameras(qParams, order);  
        return qParams.toString()+"\n"+camLog.toString();
    }
    
}
