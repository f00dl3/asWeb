/*
by Anthony Stump
Created: 2 Sep 2017
Updated: 30 Dec 2019
*/

package asUtilsPorts.Feed;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class NWSWarnings {
	

	public void doFetch(Connection dbc) {

        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
    
		final String ramTemp = cb.getPathChartCache();
		
		final String warn2URL = "https://api.weather.gov/alerts/active";
		final File warn2File = new File(ramTemp+"/Warn2All.json");
		
		wc.jsoupOutBinary(warn2URL, warn2File, 5.0);
		
		wc.sedFileReplace(ramTemp+"/Warn2All.json", "\\n", "");
		wc.sedFileReplace(ramTemp+"/Warn2All.json", " null\\,", "\\\"null\\\",");
			
		String warn2data = "";
		Scanner warn2Scanner = null;
		try { warn2Scanner = new Scanner(warn2File); while(warn2Scanner.hasNext()) { warn2data = warn2data+warn2Scanner.nextLine(); } }
		catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		
		String w2jsonSQL = "INSERT IGNORE INTO WxObs.LiveWarnings ("
			+ " capVersion, id, updated, published, title, summary,"
			+ " cap12polygon, capevent, capeffective, capexpires, capstatus,"
			+ " capmsgType, capcategory, capurgency, capseverity, capcertainty,"
			+ " capareaDesc, cap12same, cap12ugc, cap12vtec"
			+ ") VALUES";
		
		JSONObject warn2obj = new JSONObject(warn2data);
		JSONArray w2features = warn2obj.getJSONArray("features");
		for (int i = 0; i < w2features.length(); i++) {
		
			String thisPolygon = null;
			String thisSAME = null;
			String thisUGC = null;
			String thisVTEC = null;
		
			JSONObject thisJObjRoot = w2features.getJSONObject(i);
			JSONObject thisJObjProperties = thisJObjRoot.getJSONObject("properties");
		
			Object thisObjGeocode = thisJObjProperties.get("geocode");
			Object thisObjGeometry = thisJObjRoot.get("geometry");
			Object thisObjParameters = thisJObjProperties.get("parameters");
		
			if(thisObjGeocode instanceof JSONObject) {
				thisSAME = thisJObjProperties.getJSONObject("geocode").getJSONArray("SAME").toString();
				thisSAME = thisSAME.replace("[[[", "[[").replace("]]]","]]");
				thisSAME = "'"+thisSAME+"'";
				thisUGC = thisJObjProperties.getJSONObject("geocode").getJSONArray("UGC").toString();
				thisUGC = thisUGC.replace("[[[", "[[").replace("]]]","]]");
				thisUGC = "'"+thisUGC+"'";
			}
		
			if(thisObjGeometry instanceof JSONObject) {
				thisPolygon = thisJObjRoot.getJSONObject("geometry").getJSONArray("coordinates").toString();
				thisPolygon = thisPolygon.replace("[[[", "[[").replace("]]]","]]");
				thisPolygon = "'"+thisPolygon+"'";
			}
		
			if(thisObjParameters instanceof JSONObject) {
				if (thisJObjProperties.getJSONObject("parameters").has("VTEC")) {
					JSONArray vtecJSON = thisJObjProperties.getJSONObject("parameters").getJSONArray("VTEC");
					thisVTEC = vtecJSON.toString();
					thisVTEC = thisVTEC.replace("[[[", "[[").replace("]]]","]]");
					thisVTEC = "'"+thisVTEC+"'";
				}
			}
		
			String thisId = thisJObjRoot.getString("id");
			String thisUpdated = thisJObjProperties.getString("sent");
			String thisTitle = thisJObjProperties.getString("headline");
			String thisSummary1 = wc.jsonSanitize(thisJObjProperties.getString("description"));
			String thisSummary2 = wc.jsonSanitize(thisJObjProperties.getString("instruction"));
			String thisEvent = thisJObjProperties.getString("event");
			String thisEffective = thisJObjProperties.getString("effective");
			String thisExpires = thisJObjProperties.getString("expires");
			String thisStatus = thisJObjProperties.getString("status");
			String thisMsgType = thisJObjProperties.getString("messageType");
			String thisCategory = thisJObjProperties.getString("category");
			String thisUrgency = thisJObjProperties.getString("urgency");
			String thisSeverity = thisJObjProperties.getString("severity");
			String thisCertainty = thisJObjProperties.getString("certainty");
			String thisAreaDesc = wc.jsonSanitize(thisJObjProperties.getString("areaDesc"));
		
			w2jsonSQL = w2jsonSQL+" (1.20,'"+thisId+"','"+thisUpdated+"','"+thisUpdated+"','"+thisTitle+"','"+thisSummary1+thisSummary2+"',"
				+ thisPolygon+",'"+thisEvent+"','"+thisEffective+"','"+thisExpires+"','"+thisStatus+"',"
				+ "'"+thisMsgType+"','"+thisCategory+"','"+thisUrgency+"','"+thisSeverity+"','"+thisCertainty+"',"
				+ "'"+thisAreaDesc+"',"+thisSAME+","+thisUGC+","+thisVTEC
				+ "),";
		
		}
		
		w2jsonSQL = (w2jsonSQL+";").replace(",;", ";");

        try { wc.q2do1c(dbc, w2jsonSQL, null); } catch (Exception e) { e.printStackTrace(); }
		
		warn2File.delete();

	}

}