/*
by Anthony Stump
Created: 15 Mar 2020
Updated: on creation

https://www.weather.gov/source/crh/lsr_snow.geojson

*/

package asUtilsPorts.Feed;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class SnowReports {	

	public String doSnow(Connection dbc) {

		String snow = "";
		
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
    
		final String ramTemp = cb.getPathChartCache();
		
		final String reportsUrl = "https://www.weather.gov/source/crh/lsr_snow.geojson";
		final File reportsFile = new File(ramTemp+"/lsr_snow.geojson");
		
		wc.jsoupOutBinary(reportsUrl, reportsFile, 5.0);
		
		wc.sedFileReplace(ramTemp+"/lsr_snow.json", "\\n", "");
		wc.sedFileReplace(ramTemp+"/lsr_snow.json", " null\\,", "\\\"null\\\",");
			
		String lsrData = "";
		Scanner lsrScanner = null;
		try { lsrScanner = new Scanner(reportsFile); while(lsrScanner.hasNext()) { lsrData = lsrData+lsrScanner.nextLine(); } }
		catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		
		String lsrSql = "INSERT IGNORE INTO WxObs.SnowReports (" +
				" id, timestamp, point, location, state, remarks, amount" +
				") VALUES";
		
		JSONObject lsrObj = new JSONObject(lsrData);
		JSONArray lsrFeatures = lsrObj.getJSONArray("features");
		for (int i = 0; i < lsrFeatures.length(); i++) {
			
			String thisPoint = null;
		
			JSONObject thisJObjRoot = lsrFeatures.getJSONObject(i);
			JSONObject thisJObjProperties = thisJObjRoot.getJSONObject("properties");
		
			Object thisObjGeometry = thisJObjRoot.get("geometry");		
		
			if(thisObjGeometry instanceof JSONObject) {
				thisPoint = thisJObjRoot.getJSONObject("geometry").getJSONArray("coordinates").toString();
			}
			
			long thisTimestamp = thisJObjProperties.getLong("valid_time");
			String thisAmount = thisJObjProperties.getString("amount");
			String thisRemarks = wc.jsonSanitize(thisJObjProperties.getString("remarks"));
			String thisLocation = wc.jsonSanitize(thisJObjProperties.getString("location"));
			String thisState = wc.jsonSanitize(thisJObjProperties.getString("state"));
			String thisId = thisPoint+"@"+thisTimestamp;
			lsrSql += " (" +
					"'"+thisId+"','"+thisTimestamp+"'," +
					"'"+thisPoint+"','"+thisLocation+"','"+thisState+"'," +
					"'"+thisRemarks+"','"+thisAmount+"'" +
					"),";
		
		}
		
		lsrSql = (lsrSql+";").replace(",;", ";");

		snow = lsrSql;
		
        try { wc.q2do1c(dbc, lsrSql, null); } catch (Exception e) { e.printStackTrace(); }
		
		reportsFile.delete();
		
		return snow;

	}
	
}