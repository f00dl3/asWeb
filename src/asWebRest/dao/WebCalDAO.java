/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 22 Apr 2018
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebCalDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public String generate_iCal(JSONObject events) {
    	
    	String iCalHeader = "BEGIN:VCALENDAR\n" +
    			"X-WR-CALNAME;VALUE=TEXT:f00dl3\n" +
    			"PRODID:-//asWeb-ICS\n" +
    			"VERSION:0.1\n" +
    			"METHOD:PUBLISH\n";
    	
    	/* ICAL FORMAT :
    	 
				BEGIN:VEVENT
				UID:-LOCALHOST:8080-WCAL-F00DL3-0000000220
				LAST-MODIFIED:20150326T003958Z
				SUMMARY:Red Lobster parents
				DESCRIPTION:Red Lobster parents
				CLASS:PUBLIC
				STATUS:CONFIRMED
				ATTENDEE;ROLE=CHAIR;PARTSTAT=ACCEPTED;CN="Anthony ":MAILTO:youremailhere
				DTSTART:20150329T230000Z
				DTSTAMP:20191214T201601Z
				DTEND:20150330T010000Z
				END:VEVENT

    	 */
    	
    	String returnData = iCalHeader;
    	return returnData;
    	
    }
    
    public JSONObject getEventsBasic(Connection dbc) {
    	final String query_Calendar_Events = "SELECT " +
    			" cal_id, cal_date, cal_time, cal_mod_date, cal_mod_time," +
    			" cal_due_date, cal_due_time, cal_name, cal_description, cal_duration" +
    			" FROM WebCal.webcal_entry";
        JSONObject tContainer = new JSONObject();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_Calendar_Events, null);
            while (resultSet.next()) {
                tContainer
                    .put("cal_id", resultSet.getInt("cal_id"))
                    .put("cal_date", resultSet.getInt("cal_date"))
                    .put("cal_time", resultSet.getInt("cal_time"))
                    .put("cal_duration", resultSet.getInt("cal_duration"))
                    .put("cal_mod_date", resultSet.getInt("cal_mod_date"))
                    .put("cal_mod_time", resultSet.getInt("cal_mod_time"))
                    .put("cal_due_date", resultSet.getInt("cal_due_date"))
                    .put("cal_due_time", resultSet.getInt("cal_due_time"))
                    .put("cal_name", resultSet.getString("cal_name"))
                    .put("cal_description", resultSet.getString("cal_description"));
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONObject getLastLogId(Connection dbc) {
        final String query_Calendar_LastLogID = "SELECT MAX(cal_entry_id) AS CEID, MAX(cal_log_id) AS CLID FROM WebCal.webcal_entry_log;";
        JSONObject tContainer = new JSONObject();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_Calendar_LastLogID, null);
            while (resultSet.next()) {
                tContainer
                    .put("CEID", resultSet.getInt("CEID"))
                    .put("CLID", resultSet.getInt("CLID"));
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public String setAddEntry(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntry = "INSERT INTO WebCal.webcal_entry VALUES (?,Null,Null,'f00dl3',?,?,?,?,?,?,?,5,'E','P',?,Null,Null,Null,?);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntry, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setAddEntryLog(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntryLog = "INSERT INTO WebCal.webcal_entry_log VALUES (?,?,'f00dl3','f00dl3','C',?,?,Null);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntryLog, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setAddEntryUser(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntryUser = "INSERT INTO WebCal.webcal_entry_user VALUES (?,'f00dl3','A',Null,0);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntryUser, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
}
