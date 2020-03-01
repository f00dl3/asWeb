/*
by Anthony Stump
Created: 27 Aug 2017
Updated: 24 Feb 2020
*/

package asUtilsPorts.Weather;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import asWebRest.dao.WeatherDAO;
import asWebRest.action.GetWeatherAction;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

public class RadarWorker {

	public static void fetch(String thisRound) {
        
		MyDBConnector mdb = new MyDBConnector();
		
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { }
        
		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();
		
		final String ramDrive = cb.getRamPath()+"/wxRadarJ";
		final String radPath = cb.getPersistTomcat()+"/Get/Radar";
		
		final File rdObj = new File(ramDrive);
		final File radPathObj = new File(radPath);
		
		if(!rdObj.exists()) { try { rdObj.mkdirs(); } catch (Exception e) { } }
		if(!radPathObj.exists()) { try { radPathObj.mkdirs(); } catch (Exception e) { } }

		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		String thisTimestamp = dateFormat.format(date);

		String getRadarSetSQL = "SELECT Site, DoOverlay, BoundsNSEW FROM WxObs.RadarList WHERE Active=1 AND Round='"+thisRound+"' ORDER BY Site ASC;";

        try (
        	ResultSet resultSet = wc.q2rs1c(dbc, getRadarSetSQL, null);
		) {		
			while (resultSet.next()) {
				
				final String thisRad = resultSet.getString("Site");
				File thisPathObject = new File(ramDrive+"/"+thisRad);
				File thisDestPathObject = new File(radPath+"/"+thisRad+"/Archive");
				if(!thisPathObject.exists()) { thisPathObject.mkdirs(); }
				if(!thisDestPathObject.exists()) { thisDestPathObject.mkdirs(); }
				final File radAoutFile = new File(ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".jpg");
				final File radBoutFile = new File(ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".jpg");
				final String thisOverlay = radPath+"/"+thisRad+"/_Overlay.jpg";
				
				Thread gr1a = new Thread(() -> {
                    String radarURLa = null;
                    if(thisRad.equals("XXX")) { radarURLa = "https://radar.weather.gov/ridge/Conus/RadarImg/latest_radaronly.gif"; }
                    else { radarURLa = "https://radar.weather.gov/ridge/RadarImg/N0R/"+thisRad+"_N0R_0.gif"; }
                    wc.jsoupOutBinary(radarURLa, radAoutFile, 5.0);
                });
				
				Thread gr1b = new Thread(() -> {
                    String radarURLb = null;
                    if(thisRad.equals("XXX")) { radarURLb = "https://radar.weather.gov/ridge/RadarImg/N0S/EAX_N0S_0.gif"; }
                    else { radarURLb = "https://radar.weather.gov/ridge/RadarImg/N0S/"+thisRad+"_N0S_0.gif"; }
                    wc.jsoupOutBinary(radarURLb, radBoutFile, 5.0);
                });

				Thread grListA[] = { gr1a, gr1b };
				for (Thread thread : grListA) { thread.start(); }
				for (int i = 0; i < grListA.length; i++) { try { grListA[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

				wc.copyFile(radAoutFile.toString(), radPath+"/"+thisRad+"/_BLatest_NO.gif");
				wc.copyFile(radBoutFile.toString(), radPath+"/"+thisRad+"/_VLatest_NO.gif");
				
				//final String convertArgs = "-gravity center";
				final String convertArgs = "-alpha set -channel A -evaluate set 20 -gravity center";
				
				if(resultSet.getInt("DoOverlay") == 1) {
					RadarImageProcessor rip = new RadarImageProcessor();
					final JSONArray tBounds = new JSONArray(resultSet.getString("BoundsNSEW"));
					final String appendData = rip.generateConvertStringsForStationData(dbc, tBounds);
					wc.runProcess("convert -composite "+thisOverlay+" "+radAoutFile.toString()+" "+ convertArgs + " " + appendData + " " + radAoutFile.toString());
					wc.runProcess("convert -composite "+thisOverlay+" "+radBoutFile.toString()+" "+ convertArgs + " " + radBoutFile.toString());
				}
				
				wc.copyFile(radAoutFile.toString(), radPath+"/"+thisRad+"/_BLatest.jpg");
				wc.copyFile(radAoutFile.toString(), radPath+"/"+thisRad+"/B"+thisTimestamp+".jpg");
				wc.copyFile(radBoutFile.toString(), radPath+"/"+thisRad+"/V"+thisTimestamp+".jpg");

				wc.runProcess("(ls "+radPath+"/"+thisRad+"/B*.jpg -t | head -n 16; ls "+radPath+"/"+thisRad+"/B*.jpg)|sort|uniq -u| xargs -I '{}' mv '{}' "+radPath+"/"+thisRad+"/Archive");
				wc.runProcess("(ls "+radPath+"/"+thisRad+"/V*.jpg -t | head -n 16; ls "+radPath+"/"+thisRad+"/V*.jpg)|sort|uniq -u| xargs -I '{}' mv '{}' "+radPath+"/"+thisRad+"/Archive");
				wc.runProcess("find "+radPath+"/"+thisRad+"/ -size 0 -print0 |xargs -0 rm");
				
                if(thisRad.equals("EAX")) { 
                	wc.runProcess("convert -delay 18 -loop 0 -dispose previous "+radPath+"/"+thisRad+"/B*.jpg "+radPath+"/"+thisRad+"/_BLoop.gif");
                	wc.runProcess("convert -delay 18 -loop 0 -dispose previous "+radPath+"/"+thisRad+"/V*.jpg "+radPath+"/"+thisRad+"/_VLoop.gif");
                }			


			}

		} catch (Exception e) { }

		try { dbc.close(); } catch (Exception e) { }
		
	}

	public String opacityTest(Connection dbc) {
        
		String returnData = "";
		String appendData = "";
		
		CommonBeans cb = new CommonBeans();
		GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
		RadarImageProcessor rip = new RadarImageProcessor();
		WebCommon wc = new WebCommon();
		
		final String ramDrive = cb.getRamPath()+"/wxRadarJ";
		final String radPath = cb.getPersistTomcat()+"/Get/Radar";
		
		final File rdObj = new File(ramDrive);
		final File radPathObj = new File(radPath);
		
		if(!rdObj.exists()) { try { rdObj.mkdirs(); } catch (Exception e) { } }
		if(!radPathObj.exists()) { try { radPathObj.mkdirs(); } catch (Exception e) { } }

		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		String thisTimestamp = dateFormat.format(date);

		final String thisRad = "EAX";
		File thisPathObject = new File(ramDrive+"/"+thisRad);
		File thisDestPathObject = new File(radPath+"/"+thisRad+"/Archive");
		if(!thisPathObject.exists()) { thisPathObject.mkdirs(); }
		if(!thisDestPathObject.exists()) { thisDestPathObject.mkdirs(); }

		final File radAoutFile = new File(ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".jpg");
		final String thisOverlay = radPath+"/"+thisRad+"/_Overlay.jpg";
				
		String radarURLa = "https://radar.weather.gov/ridge/RadarImg/N0R/"+thisRad+"_N0R_0.gif";
        wc.jsoupOutBinary(radarURLa, radAoutFile, 5.0);
							
		final String convertArgs = "-alpha set -channel A -evaluate set 20% -gravity center";

		String getRadarSetSQL = "SELECT Site, BoundsNSEW, DoOverlay FROM WxObs.RadarList WHERE Site='EAX';";

        	try (
        		ResultSet resultSet = wc.q2rs1c(dbc, getRadarSetSQL, null);
			) {		
			while (resultSet.next()) {
				JSONArray tBounds = new JSONArray(resultSet.getString("BoundsNSEW"));
				appendData += rip.generateConvertStringsForStationData(dbc, tBounds);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try { 
			JSONArray dataSet = getWeatherAction.getObsJsonLast(dbc);
			for(int o = 0; o < dataSet.length(); o++) {
				JSONObject tObj = dataSet.getJSONObject(o);
				JSONObject tSubObj = new JSONObject(tObj.getString("jsonSet"));
				double tTemperature = tSubObj.getDouble("Temperature");
				int tTempInt = (int) Math.round(tTemperature);
				String tStationData = Integer.toString(tTempInt);
			}
		} catch (Exception e) { e.printStackTrace(); }

		String fullProcessToRun = "convert -composite " + thisOverlay + " " + radAoutFile.toString() +
				" " + convertArgs + " " + appendData + " " + radAoutFile.toString();
		
		returnData += fullProcessToRun;
		
		wc.runProcess(fullProcessToRun);
						
		try { wc.copyFile(radAoutFile.toString(), radPath+"/"+thisRad+"/_BLatest_TESTING.jpg"); } catch (Exception e) { e.printStackTrace(); }
		
		return returnData;
				
	}
	
}
