/*
by Anthony Stump
Created: 18 Sep 2017
Updated: 26 Jul 2020
*/

package asUtilsPorts.Weather; 
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.*;

import asWebRest.hookers.AmbientWxStation;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class xsWorkerWunder {

	public void main(String xsTmp, String region) {
          
		String returnData = "";
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

		final String stationType = "Wunder";
		final File jsonOutFile = new File(xsTmp+"/rapid_"+stationType+"_"+region+".json");
		final File wunderFile = new File(xsTmp+"/wuData.json");
		final File wunderFile2 = new File(xsTmp+"/wuData2.json");
		final File wunderFile3 = new File(xsTmp+"/wuData3.json");
		final File wunderFile4 = new File(xsTmp+"/wuData4.json");
		String addStationTestSQL = "INSERT IGNORE INTO WxObs.Stations (Station, Point, City, State, Active, Priority, Region, DataSource, Frequency) VALUES";

		jsonOutFile.delete();

		int tNulls = 0;
		int tVars = 0;
		
		final String wunderURL = "http://stationdata.wunderground.com/cgi-bin/stationdata?format=json&maxstations=1000&minlat=36&minlon=-97&maxlat=41&maxlon=-91";
		final String wunderURL2 = "http://stationdata.wunderground.com/cgi-bin/stationdata?format=json&maxstations=1000&minlat=36&minlon=-103&maxlat=41&maxlon=-97";
		final String wunderURL3 = "http://stationdata.wunderground.com/cgi-bin/stationdata?format=json&maxstations=1000&minlat=41&minlon=-103&maxlat=47&maxlon=-97";
		final String wunderURL4 = "http://stationdata.wunderground.com/cgi-bin/stationdata?format=json&maxstations=1000&minlat=41&minlon=-97&maxlat=47&maxlon=-91";

		wc.jsoupOutBinary(wunderURL, wunderFile, 15.0);
		wc.jsoupOutBinary(wunderURL2, wunderFile2, 15.0);
		wc.jsoupOutBinary(wunderURL3, wunderFile3, 15.0);
		wc.jsoupOutBinary(wunderURL4, wunderFile4, 15.0);
		
		wc.sedFileReplace(wunderFile.toString(), "\\n", ""); wc.sedFileReplace(wunderFile.toString(), " null\\,", "\\\"null\\\",");
		wc.sedFileReplace(wunderFile2.toString(), "\\n", ""); wc.sedFileReplace(wunderFile2.toString(), " null\\,", "\\\"null\\\",");
		wc.sedFileReplace(wunderFile3.toString(), "\\n", ""); wc.sedFileReplace(wunderFile3.toString(), " null\\,", "\\\"null\\\",");
		wc.sedFileReplace(wunderFile4.toString(), "\\n", ""); wc.sedFileReplace(wunderFile4.toString(), " null\\,", "\\\"null\\\",");
			
		String wuLoaded = ""; Scanner wuScanner = null; try { wuScanner = new Scanner(wunderFile); while(wuScanner.hasNext()) { wuLoaded = wuLoaded+wuScanner.nextLine(); } } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		String wuLoaded2 = ""; Scanner wuScanner2 = null; try { wuScanner2 = new Scanner(wunderFile2); while(wuScanner2.hasNext()) { wuLoaded2 = wuLoaded2+wuScanner2.nextLine(); } } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		String wuLoaded3 = ""; Scanner wuScanner3 = null; try { wuScanner3 = new Scanner(wunderFile3); while(wuScanner3.hasNext()) { wuLoaded3 = wuLoaded3+wuScanner3.nextLine(); } } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		String wuLoaded4 = ""; Scanner wuScanner4 = null; try { wuScanner4 = new Scanner(wunderFile4); while(wuScanner4.hasNext()) { wuLoaded4 = wuLoaded4+wuScanner4.nextLine(); } } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }

		List<String> wxStations = new ArrayList<>();
		final String getStationListSQL = "SELECT Station FROM WxObs.Stations WHERE Active=1 AND DataSource='WU' ORDER BY Station DESC;";
	
		try ( ResultSet resultSetStations = wc.q2rs1c(dbc, getStationListSQL, null); ) { 
            while (resultSetStations.next()) { wxStations.add(resultSetStations.getString("Station")); }
        } catch (Exception e) { e.printStackTrace(); }

		JSONObject wuObj = new JSONObject(wuLoaded); JSONObject wuObjTmp = wuObj.getJSONObject("conds");
		JSONObject wuObj2 = new JSONObject(wuLoaded2); JSONObject wuObjTmp2 = wuObj2.getJSONObject("conds");
		JSONObject wuObj3 = new JSONObject(wuLoaded3); JSONObject wuObjTmp3 = wuObj3.getJSONObject("conds");
		JSONObject wuObj4 = new JSONObject(wuLoaded4); JSONObject wuObjTmp4 = wuObj4.getJSONObject("conds");

		JSONObject wuObjConcat = new JSONObject(wuObjTmp, JSONObject.getNames(wuObjTmp));
		for (String key : JSONObject.getNames(wuObjTmp2)) { wuObjConcat.put(key, wuObjTmp2.get(key)); }
		for (String key : JSONObject.getNames(wuObjTmp3)) { wuObjConcat.put(key, wuObjTmp3.get(key)); }
		for (String key : JSONObject.getNames(wuObjTmp4)) { wuObjConcat.put(key, wuObjTmp4.get(key)); }

		JSONObject wuObjConds = new JSONObject(wuObjConcat.toString().trim());
		Iterator<?> keys = wuObjConds.keys();

		JSONObject jStationData = new JSONObject();

		while(keys.hasNext()) {
				
			String key = (String)keys.next();
			System.out.println(key);

			JSONObject tJOStationData = wuObjConds.getJSONObject(key);

			String thisStation = null;
			String city = null;
			String state = null;
			String neighborhood = null;
			String latitude = null;
			String longitude = null;
			int doStationAddTest = 1;

			thisStation = tJOStationData.getString("id");

			if(tJOStationData.has("adm1")) { city = tJOStationData.getString("adm1").replace("\'","\\\'"); } else { doStationAddTest = 0; }
			if(tJOStationData.has("adm2")) { state = tJOStationData.getString("adm2"); } else { doStationAddTest = 0; }
			if(tJOStationData.has("neighborhood")) { neighborhood = tJOStationData.getString("neighborhood").replace("\'","\\\'"); } else { neighborhood = ""; }
			if(tJOStationData.has("lat")) { latitude = tJOStationData.getString("lat"); } else { doStationAddTest = 0; }
			if(tJOStationData.has("lon")) { longitude = tJOStationData.getString("lon"); } else { doStationAddTest = 0; }

			if(doStationAddTest == 1 && !wxStations.contains(thisStation)) {

				addStationTestSQL = addStationTestSQL+" ('"+thisStation+"','["+longitude+","+latitude+"]','"+city+"/"+neighborhood+"','"+state+"',1,8,'USC','WU',60),";

			}
	
			if(wxStations.contains(thisStation)) {

				String tTemp = null; tVars++;
				String tDewpoint = null; tVars++;
				String tPressureIn = null; tVars++;
				String tRelativeHumidity = null; tVars++;
				String tTimeString = null; tVars++;
				String tWeather = null; tVars++;
				String tWindDegrees = null; tVars++;
				String tWindSpeed = null; tVars++;
				String tWindGust = null; tVars++;
				String tVisibility = null; tVars++;
				String tFlightCategory = null; tVars++;
				String tDailyRain = null; tVars++;

				if(tJOStationData.has("tempf")) { tTemp = tJOStationData.getString("tempf"); if(wc.isSet(tTemp)) { jStationData.put("Temperature", tTemp); } else { tNulls++; } }
				if(tJOStationData.has("dewptf")) { tDewpoint = tJOStationData.getString("dewptf"); if(wc.isSet(tDewpoint)) { jStationData.put("Dewpoint", tDewpoint); } else { tNulls++; } }
				if(tJOStationData.has("dateutc")) { tTimeString = tJOStationData.getString("dateutc"); if(wc.isSet(tTimeString)) { jStationData.put("TimeString", tTimeString); } else { tNulls++; } }
				if(tJOStationData.has("RawP")) { tPressureIn = tJOStationData.getString("RawP"); if(wc.isSet(tPressureIn)) { jStationData.put("PressureIn", tPressureIn); } else { tNulls++; } }
				if(tJOStationData.has("humidity")) { tRelativeHumidity = tJOStationData.getString("humidity"); if(wc.isSet(tRelativeHumidity)) { jStationData.put("RelativeHumidity", tRelativeHumidity); } else { tNulls++; } }
				if(tJOStationData.has("weather")) { tWeather = tJOStationData.getString("weather"); if(wc.isSet(tWeather)) { jStationData.put("Weather", tWeather); } else { tNulls++; } }
				if(tJOStationData.has("winddir")) { tWindDegrees = tJOStationData.getString("winddir"); if(wc.isSet(tWindDegrees)) { jStationData.put("WindDegrees", tWindDegrees); } else { tNulls++; } }
				if(tJOStationData.has("windspeedmph")) { tWindSpeed = tJOStationData.getString("windspeedmph"); if(wc.isSet(tWindSpeed) && !tWindSpeed.equals("-999.0") && !tWindSpeed.equals("-9999.0")) { jStationData.put("WindSpeed", tWindSpeed); } else { tNulls++; } }
				if(tJOStationData.has("windgustmph")) { tWindGust = tJOStationData.getString("windgustmph"); if(wc.isSet(tWindGust) && !tWindGust.equals("-999.0") && !tWindGust.equals("-9999.0")) { jStationData.put("WindGust", tWindGust); } else { tNulls++; } }
				if(tJOStationData.has("visibilitysm")) { tVisibility = tJOStationData.getString("visibilitysm"); if(wc.isSet(tVisibility)) { jStationData.put("Visibility", tVisibility); } else { tNulls++; } }
				if(tJOStationData.has("flightrule")) { tVisibility = tJOStationData.getString("flightrule"); if(wc.isSet(tFlightCategory)) { jStationData.put("FlightCategory", tFlightCategory); } else { tNulls++; } }
				if(tJOStationData.has("dailyrainin")) { tVisibility = tJOStationData.getString("dailyrainin"); if(wc.isSet(tDailyRain)) { jStationData.put("DailyRain", tDailyRain); } else { tNulls++; } }
			
				if (tNulls != tVars) {
					String thisJSONstring = "\""+thisStation+"\":"+jStationData.toString()+",";
					thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
					try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
					returnData += " -> Completed: "+thisStation+" ("+stationType+" - "+region+")\n";
				} else {
					returnData += "!!! WARN: NO DATA FOR Station "+thisStation+" !\n";
				}
			
			}

		}

		addStationTestSQL = (addStationTestSQL+";").replace(",;",";");

		returnData += addStationTestSQL+"\n";

		try { wc.q2do1c(dbc, addStationTestSQL, null); } catch (Exception e) { e.printStackTrace(); }

        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        
        System.out.println(returnData);

	}
		
}
