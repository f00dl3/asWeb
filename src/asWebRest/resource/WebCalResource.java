/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 18 Dec 2019
 */

package asWebRest.resource;

import asWebRest.action.GetWebCalAction;
import asWebRest.action.UpdateWebCalAction;
import asWebRest.dao.WebCalDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class WebCalResource extends ServerResource {
    
    @Get
    
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        JSONObject llid = getWebCalAction.getLastLogId(dbc);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return llid.toString();
    
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
    
        String doWhat = null;
        String returnData = "";
        List<String> qParams = new ArrayList<>();
        JSONObject mergedResults = new JSONObject();
        
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        UpdateWebCalAction updateWebCalAction = new UpdateWebCalAction(new WebCalDAO());
        
        final Form argsInForm = new Form(argsIn);
        
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
	
	            case "getEventsBasic":
	                JSONArray calEvents = getWebCalAction.generate_FriendlyJSON(dbc);
	                returnData += calEvents.toString();
	                break;
                
                case "setQuickCalEntry":
                    List<String> qParams2 = new ArrayList<>();
                    List<String> qParams3 = new ArrayList<>();
                    JSONObject log = getWebCalAction.getLastLogId(dbc);
                    String dateInString = argsInForm.getFirstValue("QuickStart");
                    String timeInString = argsInForm.getFirstValue("QuickStartTime");
                    String eventTitle = argsInForm.getFirstValue("QuickTitle");
                    String dateTimeFromInput = dateInString + " " + timeInString;
                    DateTimeFormatter inputFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
                    DateTimeFormatter formatDate = DateTimeFormat.forPattern("yyyyMMdd");
                    DateTimeFormatter formatTime = DateTimeFormat.forPattern("HHmmss");
                    DateTime inputtedTime = inputFormat.parseDateTime(dateTimeFromInput).withZone(DateTimeZone.UTC);                    
                    LocalDateTime currentDateTime = new LocalDateTime();
                    int nextCEID = (log.getInt("CEID")) + 1;
                    int nextCLID = (log.getInt("CLID")) + 1;
                    int duration = 0;
                    String date_Ymd = formatDate.print(currentDateTime);
                    String date_His = formatTime.print(currentDateTime);
                    String start_Ymd = formatDate.print(inputtedTime);
                    String start_His = formatTime.print(inputtedTime);
                    qParams.add(0, Integer.toString(nextCEID));
                    qParams2.add(0, Integer.toString(nextCLID));
                    qParams2.add(1, Integer.toString(nextCEID));
                    qParams2.add(2, date_Ymd);
                    qParams2.add(3, date_His);
                    qParams3.add(0, Integer.toString(nextCEID));
                    qParams3.add(1, start_Ymd);
                    qParams3.add(2, start_His);
                    qParams3.add(3, date_Ymd);
                    qParams3.add(4, date_His);
                    qParams3.add(5, Integer.toString(duration));
                    qParams3.add(6, start_Ymd);
                    qParams3.add(7, start_His);
                    qParams3.add(8, eventTitle);
                    qParams3.add(9, eventTitle);
                    String errorData = "Attempting entry " + nextCEID + "\n" +
                            "DEBUG INFO:\n" +
                            "ENTRY LOG TIME = " + date_Ymd + " " + date_His + "\n" +
                            "START TIME = " + start_Ymd + " " + start_His + "\n" +
                            updateWebCalAction.setAddEntryUser(dbc, qParams) + "\n" +
                            updateWebCalAction.setAddEntryLog(dbc, qParams2) + "\n" +
                            updateWebCalAction.setAddEntry(dbc, qParams3);
                    JSONObject returnObject = new JSONObject();
                    returnObject
                        .put("EntryID", nextCEID)
                        .put("ErrorLog", errorData);
                    returnData = returnObject.toString();
                    break;
            
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
