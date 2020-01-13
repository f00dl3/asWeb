/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 28 Dec 2019
 */

package asWebRest.resource;

import asWebRest.action.GetDatabaseInfoAction;
import asWebRest.action.GetLogsAction;
import asWebRest.action.GetWebVersionAction;
import asWebRest.action.UpdateLogsAction;
import asWebRest.action.UpdateWebAccessLogAction;
import asWebRest.dao.DatabaseInfoDAO;
import asWebRest.dao.LogsDAO;
import asWebRest.dao.WebAccessLogDAO;
import asWebRest.dao.WebVersionDAO;
import asWebRest.model.WebAccessLog;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
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
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        List<String> qParams = new ArrayList<>();
        qParams.add("25");
        String order = "ASC";
        GetLogsAction getLogsAction = new GetLogsAction(new LogsDAO());
        JSONArray camLog = getLogsAction.getCameras(dbc, qParams, order);
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return qParams.toString()+"\n"+camLog.toString();
        
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetDatabaseInfoAction getDatabaseInfoAction = new GetDatabaseInfoAction(new DatabaseInfoDAO());
        GetLogsAction getLogsAction = new GetLogsAction(new LogsDAO());
        GetWebVersionAction getWebVersionAction = new GetWebVersionAction(new WebVersionDAO());
        UpdateLogsAction updateLogsAction = new UpdateLogsAction(new LogsDAO());
        UpdateWebAccessLogAction updateWebAccessLogAction = new UpdateWebAccessLogAction(new WebAccessLogDAO());
                        
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
                    JSONArray dbInfo = getDatabaseInfoAction.getDatabaseInfo(dbc);
                    JSONArray webVersion = getWebVersionAction.getWebVersion(dbc);
                    JSONArray sduLogs = getLogsAction.getSdUtils(dbc);
                    JSONArray camLogs = getLogsAction.getCameras(dbc, qParams, camLogOrder);
                    JSONArray backupLogs = getLogsAction.getSystemBackup(dbc);
                    mergedResults
                        .put("dbInfo", dbInfo)
                        .put("webVersion", webVersion)
                        .put("sduLogs", sduLogs)
                        .put("camLogs", camLogs)
                        .put("backupLogs", backupLogs);
                    returnData += mergedResults.toString();
                    break;

                case "LogLogin":
                    WebAccessLog webAccessLog = new WebAccessLog();
                    webAccessLog.setUser(argsInForm.getFirstValue("UserName"));
                    webAccessLog.setRemoteIp(argsInForm.getFirstValue("RemoteIP"));
                    webAccessLog.setBrowser(argsInForm.getFirstValue("UserAgent"));
                    updateWebAccessLogAction.updateWebAccessLog(webAccessLog);
                    returnData += "QUERY RAN SUCESSFULLY!";
                    break;
                    
                case "Notes":
                    String nss = "%";
                    try { nss = argsInForm.getFirstValue("noteSearchString"); } catch (Exception e) { e.printStackTrace(); }
                    qParams.add(0, nss);
                    JSONArray notes = getLogsAction.getPlainTextNotes(dbc, qParams);
                    returnData += notes.toString();
                    break;

                case "setDiscordAccess":
                    String access = "";
                    try { access = argsInForm.getFirstValue("attempt"); } catch (Exception e) { e.printStackTrace(); }
                    qParams.add(0, access);
                    updateLogsAction.setDiscordAccess(dbc, qParams);
                    break;
                                        
            }
        }
       
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
    
}
