/*
by Anthony Stump
Created: 15 Sep 2017
Updated: 2 Jan 2020
*/

package asUtilsPorts.Weather;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import org.json.*;

public class xsWorkerFull {

	public void stations(boolean debugMode, String xsTmp, String region) {

        ModelBeans wmb = new ModelBeans();
        ModelShare wms = new ModelShare();
        MyDBConnector mdb = new MyDBConnector();
        Grib2Iterators gi = new Grib2Iterators();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        ArrayList<Integer> tHts = gi.getHeights("H");
        if(debugMode) { System.out.println("tHts: " + tHts.toString()); }
        
        final double ddv = wmb.getDefaultDataValue();
		final String stationType = "Full";
		final File jsonOutFile = new File(xsTmp+"/output_"+stationType+"_"+region+".json");
        int priorityLock = 4;
        
        if(debugMode) { priorityLock = 2; }
		List<String> wxStations = new ArrayList<>();
		List<String> wxStationPoints = new ArrayList<>();

		String pointInputString = "";

		final File pointInputDump = new File(xsTmp+"/pointDump"+region+".txt");	
		final String getStationListSQL = "SELECT Station FROM WxObs.Stations WHERE Active=1 AND Region='"+region+"' AND Priority < "+priorityLock+" ORDER BY Priority, Station DESC;";
		final String getStationPointListSQL = "SELECT SUBSTRING(Point, 2, CHAR_LENGTH(Point)-2) as fixedPoint FROM WxObs.Stations WHERE Active=1 AND Region='"+region+"' AND Priority < 4 ORDER BY Priority, Station DESC;";
		final File hrrrSounding = new File(xsTmp+"/grib2/outSounding_HRRR_"+region+".csv");
		final File rapSounding = new File(xsTmp+"/grib2/outSounding_RAP_"+region+".csv");
		final String hrrrMatch = "TMP|RH|UGRD|VGRD|CAPE|CIN|4LFTX|HGT|PWAT|TMP|RH|HLCY";
		final String rapMatch = hrrrMatch;

		int gribSpot = 0;
		int iterk = 0;

		try ( ResultSet resultSetStations = wc.q2rs1c(dbc, getStationListSQL, null); ) { 
            while (resultSetStations.next()) { wxStations.add(resultSetStations.getString("Station")); }
        } catch (Exception e) { e.printStackTrace(); }

		try ( ResultSet resultSetStationPoints = wc.q2rs1c(dbc, getStationPointListSQL, null); ) { 
            while (resultSetStationPoints.next()) { wxStationPoints.add(resultSetStationPoints.getString("fixedPoint")); }
        } catch (Exception e) { e.printStackTrace(); }

		for (String thisPoint : wxStationPoints) {
			String thisGeo = thisPoint.replace(",", " ");
			pointInputString = pointInputString+"-lon "+thisGeo+" ";
		}

		try { wc.varToFile(pointInputString, pointInputDump, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }

		System.out.println(" --> Parsing GRIB2 data HRRR for region "+region);
		try { wc.runProcessOutFile("\""+wmb.getWgrib2Path()+"/wgrib2\" "+xsTmp+"/grib2/HRRR "+pointInputString+" -match \":("+hrrrMatch+"):\"", hrrrSounding, false); } catch (Exception fnf) { fnf.printStackTrace(); }

		System.out.println(" --> Parsing GRIB2 data RAP for region "+region);
		try { wc.runProcessOutFile("\""+wmb.getWgrib2Path()+"/wgrib2\" "+xsTmp+"/grib2/RAP "+pointInputString+" -match \":("+rapMatch+"):\"", rapSounding, false); } catch (Exception fnf) { fnf.printStackTrace(); }
		
		wc.sedFileReplace(hrrrSounding.getPath(), ":lon", ",lon");
		wc.sedFileReplace(rapSounding.getPath(), ":lon", ",lon");

		for (String thisStation : wxStations) {

			gribSpot = (gribSpot + 3);

			File xmlOut = new File(xsTmp+"/"+thisStation+".xml");
			String thisGeo = wxStationPoints.get(iterk);

			System.out.println(" --> Processing "+thisStation+" - GRIB2 spot is "+gribSpot+", coords "+thisGeo);

			JSONObject jStationObj = new JSONObject();
			JSONObject jsd = new JSONObject();
			jStationObj.put(thisStation, jsd);

			String tDewpointF = null;
			String tPressureMb = null;
			String tPressureIn = null;
			String tRelativeHumidity = null;
			String tTempF = null;
			String tTimeString = null;
			String tWeather = null;
			String tWindDegrees = null;
			String tWindDirection = null;
			String tWindSpeed = null;
			String tWindGust = null;
			String tVisibility = null;

			Scanner xmlScanner = null; try {		
				xmlScanner = new Scanner(xmlOut);
				while(xmlScanner.hasNext()) {
					String line = xmlScanner.nextLine();
					if(line.contains("<dewpoint_f>")) { Pattern p = Pattern.compile("<dewpoint_f>(.*)</dewpoint_f>"); Matcher m = p.matcher(line); if (m.find()) { tDewpointF = m.group(1); } }
					if(line.contains("<observation_time>")) { Pattern p = Pattern.compile("<observation_time>(.*)</observation_time>"); Matcher m = p.matcher(line); if (m.find()) { tTimeString = m.group(1); } }
					if(line.contains("<pressure_mb>")) { Pattern p = Pattern.compile("<pressure_mb>(.*)</pressure_mb>"); Matcher m = p.matcher(line); if (m.find()) { tPressureMb = m.group(1); } }
					if(line.contains("<pressure_in>")) { Pattern p = Pattern.compile("<pressure_in>(.*)</pressure_in>"); Matcher m = p.matcher(line); if (m.find()) { tPressureIn = m.group(1); } }
					if(line.contains("<relative_humidity>")) { Pattern p = Pattern.compile("<relative_humidity>(.*)</relative_humidity>"); Matcher m = p.matcher(line); if (m.find()) { tRelativeHumidity = m.group(1); } }
					if(line.contains("<temp_f>")) { Pattern p = Pattern.compile("<temp_f>(.*)</temp_f>"); Matcher m = p.matcher(line); if (m.find()) { tTempF = m.group(1); } }
					if(line.contains("<weather>")) { Pattern p = Pattern.compile("<weather>(.*)</weather>"); Matcher m = p.matcher(line); if (m.find()) { tWeather = m.group(1); } }
					if(line.contains("<wind_degrees>")) { Pattern p = Pattern.compile("<wind_degrees>(.*)</wind_degrees>"); Matcher m = p.matcher(line); if (m.find()) { tWindDegrees = m.group(1); } }
					if(line.contains("<wind_dir>")) { Pattern p = Pattern.compile("<wind_dir>(.*)</wind_dir>"); Matcher m = p.matcher(line); if (m.find()) { tWindDirection = m.group(1); } }
					if(line.contains("<wind_mph>")) { Pattern p = Pattern.compile("<wind_mph>(.*)</wind_mph>"); Matcher m = p.matcher(line); if (m.find()) { tWindSpeed = m.group(1); } }
					if(line.contains("<wind_gust_mph>")) { Pattern p = Pattern.compile("<wind_gust_mph>(.*)</wind_gust_mph>"); Matcher m = p.matcher(line); if (m.find()) { tWindGust = m.group(1); } }
					if(line.contains("<visibility_mi>")) { Pattern p = Pattern.compile("<visibility_mi>(.*)</visibility_mi>"); Matcher m = p.matcher(line); if (m.find()) { tVisibility = m.group(1); } }
				}
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		
			if (wc.isSet(tTempF)) { jsd.put("Temperature", tTempF); } 
			if (wc.isSet(tDewpointF)) { jsd.put("Dewpoint", tDewpointF); } 
			if (wc.isSet(tRelativeHumidity)) { jsd.put("RelativeHumidity", tRelativeHumidity); } 
			if (wc.isSet(tPressureMb)) { jsd.put("Pressure", tPressureMb); } 
			if (wc.isSet(tPressureIn)) { jsd.put("PressureIn", tPressureIn); } 
			if (wc.isSet(tTimeString)) { jsd.put("TimeString", tTimeString); } 
			if (wc.isSet(tVisibility)) { jsd.put("Visibility", tVisibility); } 
			if (wc.isSet(tWeather)) { jsd.put("Weather", tWeather); } 
			if (wc.isSet(tWindDegrees)) { jsd.put("WindDegrees", tWindDegrees); } 
			if (wc.isSet(tWindDirection)) { jsd.put("WindDirection", tWindDirection); } 
			if (wc.isSet(tWindGust)) { jsd.put("WindGust", tWindGust); } 
			if (wc.isSet(tWindSpeed)) { jsd.put("WindSpeed", tWindSpeed); } 

			Scanner hrrrScanner = null; try {		
				hrrrScanner = new Scanner(hrrrSounding);
				while(hrrrScanner.hasNext()) {
					String line = hrrrScanner.nextLine();
                    for(int tHt : tHts) {
                        if(debugMode) { System.out.println("Looping through height: [ " + tHt + " ]"); }
                        if(line.startsWith(gi.hrrr("RH"+tHt)+":")) { try { jsd.put("RH"+tHt, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("RH"+tHt, ddv); } }
                        if(line.startsWith(gi.hrrr("TC"+tHt)+":")) { try { jsd.put("T"+tHt, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); jsd.put("T"+tHt, ddv); } }
                        if(line.startsWith(gi.hrrr("WU"+tHt)+":")) { try { jsd.put("WU"+tHt, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("WU"+tHt, ddv); } }
                        if(line.startsWith(gi.hrrr("WV"+tHt)+":")) { try { jsd.put("WV"+tHt, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("WV"+tHt, ddv); } }
                    }
                    if(line.startsWith(gi.hrrr("CAPE")+":")) { try { jsd.put("CAPE", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("CAPE", ddv); } }
                    if(line.startsWith(gi.hrrr("CIN")+":")) { try { jsd.put("CIN", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("CIN", ddv); } }
                    if(line.startsWith(gi.hrrr("LI")+":")) { try { jsd.put("LI", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("LI", ddv); } }
                    if(line.startsWith(gi.hrrr("PWAT")+":")) { try { jsd.put("PWAT", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("PWAT", ddv); } }
                    if(line.startsWith(gi.hrrr("HGT500")+":")) { try { jsd.put("HGT500", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("HGT500", ddv); } }                              
				}
			} 
            catch (Exception fnf) { fnf.printStackTrace(); }

			Scanner rapScanner = null; try {		
				rapScanner = new Scanner(rapSounding);
				while(rapScanner.hasNext()) {
					String line = rapScanner.nextLine();
                    if(line.startsWith(gi.rap("CCL")+":")) { try { jsd.put("CCL", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("RAP", ddv); } }
                    if(line.startsWith(gi.rap("FZLV")+":")) { try { jsd.put("FZLV", (gi.g2d(line, gribSpot, false)*3.28084)); } catch (Exception e) { e.printStackTrace(); jsd.put("FZLV", ddv); } }
                    if(line.startsWith(gi.rap("WZLV")+":")) { try { jsd.put("RAP", gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); jsd.put("WZLV", ddv); } }                                                                      
				}
			}
			catch (Exception fnf) { fnf.printStackTrace(); }
                        
            for (int tHt : tHts) {
                try { 
                    if(jsd.has("T"+tHt) && jsd.has("RH"+tHt)) { jsd.put("D"+tHt, wms.calcDwpt(jsd.getDouble("T"+tHt), jsd.getDouble("RH"+tHt))); } else { jsd.put("D"+tHt, ddv); }
                    if(jsd.has("WU"+tHt) && jsd.has("WV"+tHt)) { jsd.put("WD"+tHt, wms.windDirCalc(jsd.getDouble("WU"+tHt), jsd.getDouble("WV"+tHt))); } else { jsd.put("WD"+tHt, ddv); }
                    if(jsd.has("WU"+tHt) && jsd.has("WV"+tHt)) { jsd.put("WS"+tHt, wms.windSpdCalc(jsd.getDouble("WU"+tHt), jsd.getDouble("WV"+tHt))); } else { jsd.put("WS"+tHt, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                        
			try { jsd.put("SLCL", wms.calcSLCL(jsd.getDouble("T0"), jsd.getDouble("RH0"))); } catch (Exception e) { e.printStackTrace(); jsd.put("SLCL", ddv); }
                        
            if(debugMode) { System.out.println(jsd.toString()); }
                        
			if (jsd.length() != 0) {
				String thisJSONstring = jStationObj.toString().substring(1);
				thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
				try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
				System.out.println(" -> Completed: "+thisStation+" ("+stationType+" - "+region+")");
			} else {
				System.out.println("!!! WARN: NO DATA FOR Station "+thisStation+" !");
			}

			iterk++;
			xmlOut.delete();

		}
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}

}
