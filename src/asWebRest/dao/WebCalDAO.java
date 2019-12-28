/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 24 Dec 2019
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebCalDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public JSONArray generate_FriendlyJSON(Connection dbc) {

    	JSONArray events = getEventsBasic(dbc);     	
    	JSONArray friendlyEvents = new JSONArray();
    	
    	for(int i = 0; i < events.length(); i++) {
    		JSONObject tObject = events.getJSONObject(i);    
    		JSONObject fObject = new JSONObject();

            try { 
	            String description = "";
	            String summary = "";
            	int eFrequency = 0;
	            String formatPattern = "yyyyMMdd'T'HHmmss'Z'";
	    		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatPattern).withZoneUTC();
	    		  
	            try { description = wc.basicInputFilterICS(tObject.getString("cal_description")); } catch (Exception e) { }
	            try { summary = wc.basicInputFilterICS(tObject.getString("cal_name")); } catch (Exception e) { }
	    		try { eFrequency = tObject.getInt("cal_frequency"); } catch (Exception e) { }
	    		
	    		final String formatted_eventId = String.format("%010d", tObject.getInt("cal_id"));
	    		final String formatted_calModDate = String.format("%08d", tObject.getInt("cal_mod_date"));
	    		final String formatted_calModTime = String.format("%06d", tObject.getInt("cal_mod_time"));
	    		final String pretty_lastModified = formatted_calModDate + "T" + formatted_calModTime + "Z"; 
	    		final String formatted_calStartDate = String.format("%08d", tObject.getInt("cal_date"));
	    		final String formatted_calStartTime = String.format("%06d", tObject.getInt("cal_time"));
	    		final String pretty_eventStart = formatted_calStartDate + "T" + formatted_calStartTime + "Z"; 
	    		final DateTime eventStartDateTime = DateTime.parse(pretty_eventStart, dateTimeFormatter).toDateTime();
	    		final DateTime eventEndDateTime = eventStartDateTime.plusMinutes(tObject.getInt("cal_duration")); 
	            final String pretty_eventEnd = dateTimeFormatter.print(eventEndDateTime);
	            
	            fObject
	            	.put("eventId", formatted_eventId)
	            	.put("lastModified", pretty_lastModified)
	            	.put("summary", summary)
	            	.put("description", description)
	            	.put("eventStart", pretty_eventStart)
	            	.put("eventEnd", pretty_eventEnd)
	            	.put("frequency",  eFrequency);
	    		
	            if(eFrequency == 1) {
	            	String fDate = tObject.getString("cal_type").toUpperCase();
	            	if(fDate.contentEquals("MONTHLYBYDAY")) { fDate = "MONTHLY"; }
            		fObject.put("fDate",  fDate);
	            	if(wc.isSet(tObject.getString("cal_byday"))) {
		            	fObject.put("byDay",  tObject.getString("cal_byday"));
	            	}
	            	if(wc.isSet(Integer.toString(tObject.getInt("cal_end")))) {
	            		final String formatted_calEnd = String.format("%08d", tObject.getInt("cal_end"));
	            		final String formatted_calEndTime = String.format("%06d", tObject.getInt("cal_endtime"));
	            		final String pretty_calEnd = formatted_calEnd + "T" + formatted_calEndTime + "Z";
	            		if(!pretty_calEnd.equals("00000000T000000Z")) {
			            	fObject.put("calEnd", pretty_calEnd);
	            		}
	            	}           
	            }
	            
	    		friendlyEvents.put(fObject);

        	} catch (Exception e) { e.printStackTrace(); }   	
            
    	}
    	
    	return friendlyEvents;
    	
    }
    
    public String generate_iCal(Connection dbc) {
    	
    	JSONArray events = getEventsBasic(dbc); 
    	
    	String iCalHeader = "BEGIN:VCALENDAR\n" +
    			"X-WR-CALNAME;VALUE=TEXT:f00dl3\n" +
    			"PRODID:-//asWeb-ICS\n" +
    			"VERSION:0.1\n" +
    			"METHOD:PUBLISH\n";
    	
    	String returnData = iCalHeader;
    	
    	for(int i = 0; i < events.length(); i++) {
    		
    		JSONObject tObject = events.getJSONObject(i);

	String status = "A";

	try { status = tObject.getString("cal_status"); } catch (Exception e) { }

            try { 
				if(status.equals("A")) {
			            String description = "";
			            String summary = "";
		            	int eFrequency = 0;
			            String formatPattern = "yyyyMMdd'T'HHmmss'Z'";
			    		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatPattern).withZoneUTC();
			    		final DateTime nowTime = DateTime.now();
			    		  
			            try { description = wc.basicInputFilterICS(tObject.getString("cal_description")); } catch (Exception e) { }
			            try { summary = wc.basicInputFilterICS(tObject.getString("cal_name")); } catch (Exception e) { }
			    		try { eFrequency = tObject.getInt("cal_frequency"); } catch (Exception e) { }
			    		
			    		final String formatted_eventId = String.format("%010d", tObject.getInt("cal_id"));
			    		final String formatted_calModDate = String.format("%08d", tObject.getInt("cal_mod_date"));
			    		final String formatted_calModTime = String.format("%06d", tObject.getInt("cal_mod_time"));
			    		final String pretty_lastModified = formatted_calModDate + "T" + formatted_calModTime + "Z"; 
			    		final String formatted_calStartDate = String.format("%08d", tObject.getInt("cal_date"));
			    		final String formatted_calStartTime = String.format("%06d", tObject.getInt("cal_time"));
			    		final String pretty_eventStart = formatted_calStartDate + "T" + formatted_calStartTime + "Z"; 
			    		final DateTime eventStartDateTime = DateTime.parse(pretty_eventStart, dateTimeFormatter).toDateTime();
			    		final DateTime eventEndDateTime = eventStartDateTime.plusMinutes(tObject.getInt("cal_duration")); 
			            final String pretty_eventEnd = dateTimeFormatter.print(eventEndDateTime);
			            final String pretty_theTimeItIsNow = dateTimeFormatter.print(nowTime);	          
			            
			    		String tEventData = "BEGIN:VEVENT\n" +
			    				"UID:-LOCALHOST:8080-WCAL-F00DL3-" + formatted_eventId + "\n" +
			    				"LAST-MODIFIED:" + pretty_lastModified + "\n" +
			    				"SUMMARY:" + summary + "\n" +
			    				"DESCRIPTION:" + description + "\n" +
			    				"CLASS:PUBLIC\n" +
			    				"STATUS:CONFIRMED\n" +
			    				"ATTENDEE;ROLE=CHAIR;PARTSTAT=ACCEPTED;CN=\"Anthony \":MAILTO:youremailhere\n" +
			    				"DTSTART:" + pretty_eventStart + "\n" +
			    				"DTSTAMP:" + pretty_theTimeItIsNow + "\n" +
			    				"DTEND:" + pretty_eventEnd + "\n";
			
			            if(eFrequency == 1) {	            	
			            	String fDate = tObject.getString("cal_type").toUpperCase();
			            	if(fDate.contentEquals("MONTHLYBYDAY")) { fDate = "MONTHLY"; }
			            	tEventData += "RRULE:FREQ=" + fDate;
			            	if(wc.isSet(tObject.getString("cal_byday"))) {
			            		tEventData += ";BYDAY=" + tObject.getString("cal_byday");
			            	}
			            	if(wc.isSet(Integer.toString(tObject.getInt("cal_end")))) {
			            		final String formatted_calEnd = String.format("%08d", tObject.getInt("cal_end"));
			            		final String formatted_calEndTime = String.format("%06d", tObject.getInt("cal_endtime"));
			            		final String pretty_calEnd = formatted_calEnd + "T" + formatted_calEndTime + "Z";
			            		if(!pretty_calEnd.equals("00000000T000000Z")) {
			            			tEventData += ";UNTIL=" + pretty_calEnd;
			            		}
			            	}            	
			            	tEventData += "\n";
			            }
			            
			    		tEventData += "END:VEVENT\n";
			    		
			    		returnData += tEventData;
		
				}
        	} catch (Exception e) { e.printStackTrace(); }
    	}
    	
    	returnData += "END:VCALENDAR";
        return returnData;
    	
    }
    
    public JSONArray getEventsBasic(Connection dbc) {
    	final String query_Calendar_Events = "SELECT " +
    			" wce.cal_id, wce.cal_date, wce.cal_time, wce.cal_mod_date, wce.cal_mod_time," +
    			" wce.cal_due_date, wce.cal_due_time, wce.cal_name, wce.cal_description, wce.cal_duration," +
    			" wcu.cal_status, wcr.cal_frequency, wcr.cal_days, wcr.cal_type, wcr.cal_bymonth," +
    			" wcr.cal_bymonthday, wcr.cal_byday, wcr.cal_bysetpos, wcr.cal_byweekno, wcr.cal_byyearday," +
    			" wcr.cal_wkst, wcr.cal_count, wcr.cal_end, wcr.cal_endtime" +
    			" FROM WebCal.webcal_entry wce" +
    			" LEFT JOIN WebCal.webcal_entry_user wcu ON wce.cal_id = wcu.cal_id" +
    			" LEFT JOIN WebCal.webcal_entry_repeats wcr ON wce.cal_id = wcr.cal_id;";
    			//" WHERE wcu.cal_status = 'A'";
        JSONArray tContainer = new JSONArray();
        try {            
            ResultSet resultSet = wc.q2rs1c(dbc, query_Calendar_Events, null);
            while (resultSet.next()) {
            	JSONObject tObject = new JSONObject();
            	tObject
                    .put("cal_id", resultSet.getInt("cal_id"))
                    .put("cal_date", resultSet.getInt("cal_date"))
                    .put("cal_time", resultSet.getInt("cal_time"))
                    .put("cal_duration", resultSet.getInt("cal_duration"))
                    .put("cal_mod_date", resultSet.getInt("cal_mod_date"))
                    .put("cal_mod_time", resultSet.getInt("cal_mod_time"))
                    .put("cal_due_date", resultSet.getInt("cal_due_date"))
                    .put("cal_due_time", resultSet.getInt("cal_due_time"))
                    .put("cal_name", resultSet.getString("cal_name"))
                    .put("cal_description", resultSet.getString("cal_description"))
                    .put("cal_status", resultSet.getString("cal_status"))
                    .put("cal_frequency", resultSet.getString("cal_frequency"))
                    .put("cal_type", resultSet.getString("cal_type"))
                    .put("cal_days", resultSet.getInt("cal_days"))
                    .put("cal_bymonth", resultSet.getString("cal_bymonth"))
                    .put("cal_bymonthday", resultSet.getString("cal_bymonthday"))
                    .put("cal_byday", resultSet.getString("cal_byday"))
                    .put("cal_bysetpos", resultSet.getString("cal_bysetpos"))
                    .put("cal_byweekno", resultSet.getString("cal_byweekno"))
                    .put("cal_byyearday", resultSet.getString("cal_byyearday"))
                    .put("cal_wkst", resultSet.getString("cal_wkst"))
                    .put("cal_count", resultSet.getInt("cal_count"))
                    .put("cal_end", resultSet.getInt("cal_end"))
                    .put("cal_endtime", resultSet.getInt("cal_endtime"));
            	tContainer.put(tObject);                
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
        String query_Calendar_AddEntry = "INSERT INTO WebCal.webcal_entry " +
        		"VALUES (?,Null,Null,'f00dl3',?,?,?,?,?,?,?,5,'E','P',?,Null,Null,Null,?);";
        try { returnData = wc.q2do1c(dbc, query_Calendar_AddEntry, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    public String setAddEntryRepeats(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_AddEntry = "INSERT INTO WebCal.webcal_entry_repeats " +
        " (cal_id, cal_type, cal_frequency, cal_byday, cal_wkst) VALUES" +
		" (?,'weekly',1,?,'MO');";
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
    
    public String setDeleteEvent(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Calendar_Delete = "UPDATE WebCal.webcal_entry_user SET cal_status='D' where cal_id=?;";
        try { returnData = wc.q2do1c(dbc, query_Calendar_Delete, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
}
