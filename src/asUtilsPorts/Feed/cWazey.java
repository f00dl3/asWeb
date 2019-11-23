/* 
by Anthony Stump
Created: 10 Sep 2017
Updated: 23 Nov 2019
*/

package asUtilsPorts.Feed;

import asUtils.Shares.StumpJunk;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.Scanner;

import org.json.*;


public class cWazey {

	public static void ripShit(Connection dbc) {

		// WAZE security increased in 2018 - this no longer works. Need to hack past it.
		
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
                
		final File ramDrive = new File(cb.getPathChartCache().toString());
		final String wazeFeedURL_170910 = "https://www.waze.com/rtserver/web/TGeoRSS?ma=600&mj=100&mu=100&left=-95.57315826416016&right=-93.53519439697266&bottom=38.73627888925287&top=39.15666429656342";
        final String wazeFeedURL_180210 = "https://www.waze.com/rtserver/web/TGeoRSS?ma=600&mj=100&mu=100&left=-98.71600341796874&right=-90.59490966796874&bottom=38.466362223416645&top=39.68088395222562";
        final String wazeFeedURL_180226 = "https://www.waze.com/rtserver/web/TGeoRSS?ma=600&mj=100&mu=100&left=-96.677490234375&right=-92.616943359375&bottom=38.70954432169585&top=39.27822087837867";
        final String wazeFeedURL_190503 = "https://www.waze.com/rtserver/web/TGeoRSS?bottom=38.70242345243809&left=-96.11141967773439&ma=200&mj=100&mu=20&right=-93.47140502929688&top=39.6058851619054&types=alerts,traffic,users";
        final String wazeFeedURL = wazeFeedURL_190503;
		final Path wazeJSON = Paths.get(ramDrive.toString()+"/Waze.json");

		StumpJunk.jsoupOutBinary(wazeFeedURL, wazeJSON.toFile(), 5.0);

		StumpJunk.sedFileReplace(wazeJSON.toString(), "\\n", "");
		StumpJunk.sedFileReplace(wazeJSON.toString(), " null\\,", "\\\"null\\\",");
			
		String wazeLoaded = "";
		Scanner wazeScanner = null;
		try { wazeScanner = new Scanner(wazeJSON.toFile()); while(wazeScanner.hasNext()) { wazeLoaded = wazeLoaded+wazeScanner.nextLine(); } }
		catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		
		String wazeSQL = "INSERT IGNORE INTO Feeds.WazeFeed ("
			+ "id, uuid, country, nThumbsUp,"
			+ "reportRating, reliability, type, speed,"
			+ "reportMood, subType, street, additionalInfo,"
			+ "nComments, reportBy, reportDescription, wazeData,"
			+ "nearBy, pubMillis, longitude, latitude"
			+ ") VALUES";
		
		JSONObject wazeObj = new JSONObject(wazeLoaded);
		JSONArray alerts = wazeObj.getJSONArray("alerts");

		for (int i = 0; i < alerts.length(); i++) {

			JSONObject tJOAlert = alerts.getJSONObject(i);

			String tId = null;
			String tUuid = null;
			String tCountry = null;
			int tNThumbsUp = 0;
			int tReportRating = 0;
			int tReliability = 0;
			String tType = null;
			int tSpeed = 0;
			int tReportMood = 0;
			String tSubType = null;
			String tStreet = null;
			String tAdditionalInfo = null;
			int tNComments = 0;
			String tReportBy = null;
			String tReportDescription = null;
			String tWazeData = null;
			String tNearBy = null;
			long tPubMillis = 0;
			double tLatitude = 0.000000;
			double tLongitude = 0.000000;

			Object tObjLoc = tJOAlert.get("location");
			if(tObjLoc instanceof JSONObject) {
				JSONObject tJOLocation = tJOAlert.getJSONObject("location");
				tLongitude = tJOLocation.getDouble("x");
				tLatitude = tJOLocation.getDouble("y");
			}

			tId = tJOAlert.getString("id");
			tUuid = tJOAlert.getString("uuid");
			tCountry = StumpJunk.jsonSanitize(tJOAlert.getString("country"));
			tNThumbsUp = tJOAlert.getInt("nThumbsUp");
			tReportRating = tJOAlert.getInt("reportRating");
			tReliability = tJOAlert.getInt("reliability");
			tType = StumpJunk.jsonSanitize(tJOAlert.getString("type"));
			tSpeed = tJOAlert.getInt("speed");
			tReportMood = tJOAlert.getInt("reportMood");
			tSubType = StumpJunk.jsonSanitize(tJOAlert.getString("subtype"));
			if(tJOAlert.has("street")) { tStreet = StumpJunk.jsonSanitize(tJOAlert.getString("street")); }
			if(tJOAlert.has("additionalInfo")) { tAdditionalInfo = StumpJunk.jsonSanitize(tJOAlert.getString("additionalInfo")); }
			tNComments = tJOAlert.getInt("nComments");
			if(tJOAlert.has("reportBy")) { tReportBy = StumpJunk.jsonSanitize(tJOAlert.getString("reportBy")); }
			if(tJOAlert.has("reportDescription")) { tReportDescription = StumpJunk.jsonSanitize(tJOAlert.getString("reportDescription")); }
			tWazeData = tJOAlert.getString("wazeData");
			if(tJOAlert.has("nearBy")) { tNearBy = StumpJunk.jsonSanitize(tJOAlert.getString("nearBy")); }
			tPubMillis = tJOAlert.getLong("pubMillis");

			wazeSQL = wazeSQL+"("
				+ "'"+tId+"','"+tUuid+"','"+tCountry+"',"+tNThumbsUp+","
				+ tReportRating+","+tReliability+",'"+tType+"',"+tSpeed+","
				+ tReportMood+",'"+tSubType+"','"+tStreet+"','"+tAdditionalInfo+"',"
				+ tNComments+",'"+tReportBy+"','"+tReportDescription+"','"+tWazeData+"',"
				+ "'"+tNearBy+"',"+tPubMillis+","+tLatitude+","+tLongitude
				+ "),"; 

		}

		wazeSQL = (wazeSQL+";").replace(",;", ";");
		
		try { wc.q2do1c(dbc, wazeSQL, null); } catch (Exception e) { e.printStackTrace(); }       

		try { Files.delete(wazeJSON); } catch (IOException ix) { ix.printStackTrace(); }

	}

}
