/*
by Anthony Stump
Created: 15 Sep 2017
Updated: 8 Feb 2020
*/

package asUtilsPorts.Weather;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import org.json.*;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class xsWorkerBouy {

	public void main(String xsTmp, String region) {

        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
		final String stationType = "Bouy";
		final File jsonOutFile = new File(xsTmp+"/output_"+stationType+"_"+region+".json");
		final File badStationFile = new File(xsTmp+"/badStations_"+stationType+".txt");

		int thisNullCounter = 0;
		int tVars = 0;
		
		List<String> wxStations = new ArrayList<>();
		final String getStationListSQL = "SELECT SUBSTR(Station,2) AS Station FROM WxObs.Stations WHERE Active=1 AND Priority = 6 ORDER BY Priority, Station DESC;";
		try ( ResultSet resultSetStations = wc.q2rs1c(dbc, getStationListSQL, null); ) { 
            while (resultSetStations.next()) { wxStations.add(resultSetStations.getString("Station")); }
        } catch (Exception e) { e.printStackTrace(); }

		for (String thisStation : wxStations) {
		
			File xmlOut = new File(xsTmp+"/"+thisStation+".xml");
		
			JSONObject jStationObj = new JSONObject();
			JSONObject jStationData = new JSONObject();
			jStationObj.put(thisStation, jStationData);

			String tDewpointF = null; tVars++;
			String tPressureIn = null; tVars++;
			String tPressureMb = null; tVars++;
			String tRelativeHumidity = null; tVars++;
			String tTempF = null; tVars++;
			String tTimeString = null; tVars++;
			String tWaterTempF = null; tVars++;
			String tWaterColumn = null; tVars++;
			String tWaveDegrees = null; tVars++;
			String tWaveDirection = null; tVars++;
			String tWaveHeight = null; tVars++;
			String tWavePeriodAvg = null; tVars++;
			String tWavePeriodDom = null; tVars++;
			String tWeather = null; tVars++;
			String tWindDegrees = null; tVars++;
			String tWindDirection = null; tVars++;
			String tWindSpeed = null; tVars++;
			String tWindGust = null; tVars++;
			String tVisibility = null; tVars++;

			Scanner xmlScanner = null; try {		
				xmlScanner = new Scanner(xmlOut);
				while(xmlScanner.hasNext()) {
					String line = xmlScanner.nextLine();
					if(line.contains("<dewpoint_f>")) { Pattern p = Pattern.compile("<dewpoint_f>(.*)</dewpoint_f>"); Matcher m = p.matcher(line); if (m.find()) { tDewpointF = m.group(1); } }
					if(line.contains("<observation_time>")) { Pattern p = Pattern.compile("<observation_time>(.*)</observation_time>"); Matcher m = p.matcher(line); if (m.find()) { tTimeString = m.group(1); } }
					if(line.contains("<pressure_in>")) { Pattern p = Pattern.compile("<pressure_in>(.*)</pressure_in>"); Matcher m = p.matcher(line); if (m.find()) { tPressureIn = m.group(1); } }
					if(line.contains("<pressure_mb>")) { Pattern p = Pattern.compile("<pressure_mb>(.*)</pressure_mb>"); Matcher m = p.matcher(line); if (m.find()) { tPressureMb = m.group(1); } }
					if(line.contains("<relative_humidity>")) { Pattern p = Pattern.compile("<relative_humidity>(.*)</relative_humidity>"); Matcher m = p.matcher(line); if (m.find()) { tRelativeHumidity = m.group(1); } }
					if(line.contains("<temp_f>")) { Pattern p = Pattern.compile("<temp_f>(.*)</temp_f>"); Matcher m = p.matcher(line); if (m.find()) { tTempF = m.group(1); } }
					if(line.contains("<weather>")) { Pattern p = Pattern.compile("<weather>(.*)</weather>"); Matcher m = p.matcher(line); if (m.find()) { tWeather = m.group(1); } }
					if(line.contains("<water_temp_f>")) { Pattern p = Pattern.compile("<water_temp_f>(.*)</water_temp_f>"); Matcher m = p.matcher(line); if (m.find()) { tWaterTempF = m.group(1); } }
					if(line.contains("<mean_wave_degrees>")) { Pattern p = Pattern.compile("<mean_wave_degrees>(.*)</mean_wave_degrees>"); Matcher m = p.matcher(line); if (m.find()) { tWaveDegrees = m.group(1); } }
					if(line.contains("<mean_wave_dir>")) { Pattern p = Pattern.compile("<mean_wave_dir>(.*)</mean_wave_dir>"); Matcher m = p.matcher(line); if (m.find()) { tWaveDirection = m.group(1); } }
					if(line.contains("<water_column_height>")) { Pattern p = Pattern.compile("<water_column_height>(.*)</water_column_height>"); Matcher m = p.matcher(line); if (m.find()) { tWaterColumn = m.group(1); } }
					if(line.contains("<wave_height_m>")) { Pattern p = Pattern.compile("<wave_height_m>(.*)</wave_height_m>"); Matcher m = p.matcher(line); if (m.find()) { tWaveHeight = m.group(1); } }
					if(line.contains("<average_period_sec>")) { Pattern p = Pattern.compile("<average_period_sec>(.*)</average_period_sec>"); Matcher m = p.matcher(line); if (m.find()) { tWavePeriodAvg = m.group(1); } }
					if(line.contains("<dominant_period_sec>")) { Pattern p = Pattern.compile("<dominant_period_sec>(.*)</dominant_period_sec>"); Matcher m = p.matcher(line); if (m.find()) { tWavePeriodDom = m.group(1); } }
					if(line.contains("<wind_degrees>")) { Pattern p = Pattern.compile("<wind_degrees>(.*)</wind_degrees>"); Matcher m = p.matcher(line); if (m.find()) { tWindDegrees = m.group(1); } }
					if(line.contains("<wind_dir>")) { Pattern p = Pattern.compile("<wind_dir>(.*)</wind_dir>"); Matcher m = p.matcher(line); if (m.find()) { tWindDirection = m.group(1); } }
					if(line.contains("<wind_mph>")) { Pattern p = Pattern.compile("<wind_mph>(.*)</wind_mph>"); Matcher m = p.matcher(line); if (m.find()) { tWindSpeed = m.group(1); } }
					if(line.contains("<wind_gust_mph>")) { Pattern p = Pattern.compile("<wind_gust_mph>(.*)</wind_gust_mph>"); Matcher m = p.matcher(line); if (m.find()) { tWindGust = m.group(1); } }
					if(line.contains("<visibility_mi>")) { Pattern p = Pattern.compile("<visibility_mi>(.*)</visibility_mi>"); Matcher m = p.matcher(line); if (m.find()) { tVisibility = m.group(1); } }
				}
			}
			catch (FileNotFoundException fnf) { }
		
			if (wc.isSet(tTempF)) { jStationData.put("Temperature", tTempF); } else { thisNullCounter++; }
			if (wc.isSet(tDewpointF)) { jStationData.put("Dewpoint", tDewpointF); } else { thisNullCounter++; }
			if (wc.isSet(tRelativeHumidity)) { jStationData.put("RelativeHumidity", tRelativeHumidity); } else { thisNullCounter++; }
			if (wc.isSet(tPressureMb)) { jStationData.put("Pressure", tPressureMb); } else { thisNullCounter++; }
			if (wc.isSet(tPressureIn)) { jStationData.put("PressureIn", tPressureIn); } else { thisNullCounter++; }
			if (wc.isSet(tTimeString)) { jStationData.put("TimeString", tTimeString); } else { thisNullCounter++; }
			if (wc.isSet(tVisibility)) { jStationData.put("Visibility", tVisibility); } else { thisNullCounter++; }
			if (wc.isSet(tWaterTempF)) { jStationData.put("WaterTemp", tWaterTempF); } else { thisNullCounter++; }
			if (wc.isSet(tWaterColumn)) { jStationData.put("WaterColumn", tWaterColumn); } else { thisNullCounter++; }
			if (wc.isSet(tWaveDegrees)) { jStationData.put("WaveDegrees", tWaveDegrees); } else { thisNullCounter++; }
			if (wc.isSet(tWaveDirection)) { jStationData.put("WaveDirection", tWaveDirection); } else { thisNullCounter++; }
			if (wc.isSet(tWaveHeight)) { jStationData.put("WaveHeight", tWaveHeight); } else { thisNullCounter++; }
			if (wc.isSet(tWavePeriodAvg)) { jStationData.put("WavePeriodAverage", tWavePeriodAvg); } else { thisNullCounter++; }
			if (wc.isSet(tWavePeriodDom)) { jStationData.put("WavePeriodDominant", tWavePeriodDom); } else { thisNullCounter++; }
			if (wc.isSet(tWeather)) { jStationData.put("Weather", tWeather); } else { thisNullCounter++; }
			if (wc.isSet(tWindDegrees)) { jStationData.put("WindDegrees", tWindDegrees); } else { thisNullCounter++; }
			if (wc.isSet(tWindDirection)) { jStationData.put("WindDirection", tWindDirection); } else { thisNullCounter++; }
			if (wc.isSet(tWindGust)) { jStationData.put("WindGust", tWindGust); } else { thisNullCounter++; }
			if (wc.isSet(tWindSpeed)) { jStationData.put("WindSpeed", tWindSpeed); } else { thisNullCounter++; }

			if (thisNullCounter != tVars) {
				String thisJSONstring = jStationObj.toString().substring(1);
				thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
				try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
				System.out.println(" -> Completed: "+thisStation+" ("+stationType+" - "+region+")");
			} else {
				System.out.println("!!! WARN: NO DATA FOR Station "+thisStation+" !");
				String thisBadStation = thisStation+", ";
				try { wc.varToFile(thisBadStation, badStationFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			}				
			xmlOut.delete();			
		}
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}		
}
