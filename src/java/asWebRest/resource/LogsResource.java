/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 30 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetDatabaseInfoAction;
import asWebRest.action.GetLogsAction;
import asWebRest.action.GetWebVersionAction;
import asWebRest.dao.DatabaseInfoDAO;
import asWebRest.dao.LogsDAO;
import asWebRest.dao.WebVersionDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
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
    
    @Post
    public String doPost(Representation argsIn) {
        
        GetDatabaseInfoAction getDatabaseInfoAction = new GetDatabaseInfoAction(new DatabaseInfoDAO());
        GetLogsAction getLogsAction = new GetLogsAction(new LogsDAO());
        GetWebVersionAction getWebVersionAction = new GetWebVersionAction(new WebVersionDAO());
                        
        JSONObject mergedResults = new JSONObject();
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                
                case "AnthonyOverviewData":
                    String camLogOrder = "DESC";
                    qParams.add(0, "5");
                    JSONArray dbInfo = getDatabaseInfoAction.getDatabaseInfo();
                    JSONArray webVersion = getWebVersionAction.getWebVersion();
                    JSONArray sduLogs = getLogsAction.getSdUtils();
                    JSONArray camLogs = getLogsAction.getCameras(qParams, camLogOrder);
                    JSONArray backupLogs = getLogsAction.getSystemBackup();
                    mergedResults
                        .put("dbInfo", dbInfo)
                        .put("webVersion", webVersion)
                        .put("sduLogs", sduLogs)
                        .put("camLogs", camLogs)
                        .put("backupLogs", backupLogs);
                    returnData += mergedResults.toString();
                    break;
                    
            }
        }
       
        return returnData;
        
    }
    
    
}
