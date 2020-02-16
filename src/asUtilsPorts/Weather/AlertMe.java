/* by Anthony Stump
Created: 11 Sep 2017
Updated: 15 Feb 2020
*/

package asUtilsPorts.Weather;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import asUtilsPorts.Mailer;
import asWebRest.action.GetWeatherAction;
import asWebRest.action.UpdateWeatherAction;
import asWebRest.dao.WeatherDAO;
import asWebRest.hookers.WeatherBot;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;


public class AlertMe {

	public void capAlerts(Connection dbc) {
		
		GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
		UpdateWeatherAction updateWeatherAction = new UpdateWeatherAction(new WeatherDAO());
		Mailer mailer = new Mailer();
		WeatherBot wxBot = new WeatherBot();
		JSONArray alerts = getWeatherAction.getRecentCapAlerts(dbc);
		
		for (int i = 0; i < alerts.length(); i++) {
			int excessCounter = 0;
			List<String> qParams = new ArrayList<>();
			JSONObject tCapData = alerts.getJSONObject(i);
			JSONArray sameCodes = new JSONArray();
			qParams.add(0, tCapData.getString("id"));
			String messageToCompile = tCapData.getString("title");
			String addToMessage = " for ";
			try {
				try { 
					sameCodes = new JSONArray(tCapData.getString("cap12same")); 
				} catch (Exception e) { e.printStackTrace(); }
				for(int j = 0; j < sameCodes.length(); j++) {
					String tSAME = sameCodes.getString(j);
					if(tSAME.equals("020091")) { mailer.sendQuickText(messageToCompile); }
					if(j < 2) {
						addToMessage += getWeatherAction.getCountySAME(dbc, tSAME);
					} else {
						excessCounter++;
					}
				}
				if(excessCounter != 0) {
					addToMessage += "and " + excessCounter + " more.";
				}
			} catch (Exception e) { }					
			messageToCompile += addToMessage;	
			wxBot.botBroadcastOnly(messageToCompile);
			updateWeatherAction.setAlertSentCapAlert(dbc, qParams);
		}			
		
	}
        public static void doAlert() {
            
	        MyDBConnector mdb = new MyDBConnector();
	        WebCommon wc = new WebCommon();
	        
	        Connection dbc = null;
	        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
	                
			final DateTime dtEnd = new DateTime();
			final DateTime dtStart = new DateTime().minusDays(7);
			final DateTimeFormatter sqlTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			final String beginTime = sqlTime.print(dtStart);
			final String endTime = sqlTime.print(dtEnd);
	
		 	final String getSubsSQL = "SELECT"
	                        + " Destination, MessageLevel, Areas"
	                        + " FROM Core.AlertEndpoints;";
	                
	        try {
	            ResultSet rs = wc.q2rs1c(dbc, getSubsSQL, null);
	            while (rs.next()) { 
	                String alertScope = rs.getString("MessageLevel");
	                String sameCode = rs.getString("Areas");
	                String destinationAddress = rs.getString("Destination");
	                JSONObject params = new JSONObject();
	                params
	                    .put("beginTime", beginTime)
	                    .put("endTime", endTime)
	                    .put("alertScope", alertScope)
	                    .put("sameCode", sameCode)
	                    .put("destinationAddress", destinationAddress);
	                if(!alertScope.equals("Off")) {
	                    getAlertsForUser(dbc, params);
	                }
	            }
	            rs.close();
	        } catch (Exception e) { e.printStackTrace(); }
	        
	        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        }

		public void earthquakeAlerts(Connection dbc) {
			
			GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
			UpdateWeatherAction updateWeatherAction = new UpdateWeatherAction(new WeatherDAO());
			Mailer mailer = new Mailer();
			JSONArray recentQuakes = getWeatherAction.getRecentEarthquakes(dbc);
			
			for (int i = 0; i < recentQuakes.length(); i++) {
				List<String> qParams = new ArrayList<>();
				JSONObject tQuakeData = recentQuakes.getJSONObject(i);
				String messageToCompile = "EQ: " +
						" mag " + tQuakeData.getDouble("mag") +
						" depth " + tQuakeData.getString("depth") +
						" " + tQuakeData.getString("time") +
						" @ " + tQuakeData.getString("place") + 
						" [" + tQuakeData.getString("latitude") + "," + tQuakeData.getString("longitude") + "]";
				qParams.add(0, tQuakeData.getString("id"));
				mailer.sendMultiAlert(messageToCompile, false);		
				updateWeatherAction.setAlertSentEarthquake(dbc, qParams);
			}			
			
		}

		private static void getAlertsForUser(Connection dbc, JSONObject params) {
            
        	Mailer mailer = new Mailer();
            MyDBConnector mdb = new MyDBConnector();
            WebCommon wc = new WebCommon();
            
            String beginTime = params.getString("beginTime");
            String endTime = params.getString("endTime");
            String alertScope = params.getString("alertScope");
            String sameCode = params.getString("sameCode");
            String destinationAddress = params.getString("destinationAddress");

            String getActWarnSQL = "SELECT id, GetTime, title, summary, testCond FROM ("
                    + "	SELECT"
                    + "		lw.capVersion, lw.id, lw.published, lw.updated, lw.title, lw.summary,"
                    + "		lw.cappolygon, lw.cap12polygon, lw.capgeocode, lw.capparameter, lw.capevent,"
                    + "		lwc.ColorRGB, lwc.ColorHEX, lwc.ExtendDisplayTime, lwc.ShowIt, lw.GetTime,"
                    + "		CASE"
                    + "			WHEN lw.capgeocode IS NOT NULL THEN REPLACE(REPLACE(REPLACE(substring_index(substring_index(lw.capgeocode, 'FIPS6', -1), 'UGC', 1),'\r\n\t ',' '),'  ',''),' ',',')"
                    + "			ELSE ''"
                    + "		END as FIPSCodes,"
                    + "		lw.cap12same, lw.cap12ugc, lw.cap12vtec, CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.capexpires,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.capexpires,20,5),'-05:00') as testCond"
                    + "	FROM WxObs.LiveWarnings lw"
                    + "	LEFT JOIN WxObs.LiveWarningColors lwc ON lw.capevent = lwc.WarnType"
                    + "	WHERE "
                    + "		CASE WHEN lwc.ExtendDisplayTime = 0 THEN"
                    + "			(CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.published,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.published,20,5),'-05:00') BETWEEN '"+beginTime+"' AND '"+endTime+"'"
                    + "			OR CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.updated,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.updated,20,5),'-05:00') BETWEEN '"+beginTime+"' AND '"+endTime+"')"
                    + "			AND CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.capexpires,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.capexpires,20,5),'-05:00') > '"+beginTime+"'"
                    + "		ELSE"
                    + "			CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.capexpires,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.capexpires,20,5),'-05:00') >= '"+beginTime+"'"
                    + "		END"
                    + "		AND lw.title IS NOT NULL"
                    + "		AND ( lwc.AlertScope LIKE '%"+alertScope+"%' OR lwc.AlertScope IS NULL)"
                    + "		AND ( lw.capgeocode REGEXP '"+sameCode+"' OR lw.cap12same REGEXP '"+sameCode+"' )"
                    + "	ORDER BY lw.published DESC"
                    + ") as lwm"
                    + " GROUP BY "
                    + "	CASE WHEN cap12polygon IS NOT NULL THEN CONCAT(capevent,cap12polygon) END,"
                    + "	CASE WHEN cappolygon IS NOT NULL THEN CONCAT(capevent,cappolygon) END,"
                    + "	CASE WHEN cappolygon IS NULL AND cap12polygon IS NULL THEN CONCAT(capevent,FIPSCodes,cap12same) END"
                    + " ORDER BY GetTime DESC"
                    + " LIMIT 1;";

            try {
                ResultSet rs = wc.q2rs1c(dbc, getActWarnSQL, null);
                while (rs.next()) { 
                    String tId = rs.getString("id");
                    String checkIfSent = "SELECT Sent FROM WxObs.LiveWarnings_Sent WHERE id='" + tId + "';";
                    int rowCount = 0;
                    int sendAlert = 0;
                    try {
                        ResultSet srs = wc.q2rs1c(dbc, checkIfSent, null);
                        while (srs.next()) { rowCount++; }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(rowCount == 0) {
                        System.out.println("Will send alert --> " + tId);
                        sendAlert = 1;
                    } else {
                        System.out.println("Alert already sent --> " + tId);
                    }
                    System.out.println("rowCount: " + rowCount);
                    if(sendAlert == 1) {
                        String tExpireTime = rs.getString("testCond");
                        String tTitle = rs.getString("title");
                        String tSummary = rs.getString("summary");
                        tSummary = tId; // for mobile compliance
                        System.out.println("Alert!\nID: "+tId+"\nTitle: "+tTitle+"\nExpires: "+tExpireTime);
                        mailer.sendMail(destinationAddress, "asUtils.AlertMe: "+tTitle, tSummary, null);
                        String flagSent = "INSERT INTO WxObs.LiveWarnings_Sent VALUES ('"+tId+"',1);";
                        System.out.println(flagSent);
                        try { wc.q2do1c(dbc, flagSent, null); } catch (Exception e) { e.printStackTrace(); }
                    }
                }
                rs.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    
		public static void main(String[] args) {
	            doAlert();
		}

}