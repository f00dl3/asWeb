/*
by Anthony Stump
Created: 15 Feb 2020
Updated: 22 Feb 2020
https://developer.mapquest.com/documentation/open/static-map-api/v4/map/get/
 */

package asWebRest.hookers;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.sql.Connection;

import org.jsoup.Jsoup;

import asUtilsPorts.Weather.RadarBeans;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import asWebRest.action.GetWeatherAction;
import asWebRest.dao.WeatherDAO;
import asWebRest.secure.MapQuestBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class MapGenerator {
	
	private static String apiBaseUri = "https://www.mapquestapi.com";
	
	public String cap12toImage(File outFile, JSONArray cap12in, boolean countyBounds) {
		return imageWithBox(outFile, parseCap12Json(cap12in, countyBounds));
	}
	
	public String parseCap12Json(JSONArray cap12in, boolean countyBounds) {
		
		String dataBack = "";
		
		if(countyBounds) {
			for(int i = 0; i < cap12in.length(); i++) {
				JSONArray tCba = cap12in.getJSONArray(i);
				for(int k = 0; k < tCba.length(); k++) {
					JSONArray tCoords = tCba.getJSONArray(k);
					double tLon = tCoords.getDouble(0);
					double tLat = tCoords.getDouble(1);
					dataBack += tLat + "," + tLon + "|";
				}
				try { dataBack = dataBack.substring(0, dataBack.length() - 1); } catch (Exception e) { } 
				dataBack += dataBack + "&shape=|";
			}
		} else {
			for(int i = 0; i < cap12in.length(); i++) {
				JSONArray tCoords = cap12in.getJSONArray(i);
				double tLon = tCoords.getDouble(0);
				double tLat = tCoords.getDouble(1);
				dataBack += tLat + "," + tLon + "|";
			}
		}
		
		try { dataBack = dataBack.substring(0, dataBack.length() - 1); } catch (Exception e) { }
		return dataBack;
		
	}
	
	public String imageWithBox(File outFile, String shapeData) {

		String rData = "";
		MapQuestBeans mqb = new MapQuestBeans();
		WebCommon wc = new WebCommon();
		
		final int toS = 30;		
		final String genPoint = apiBaseUri + "/staticmap/v5/map";
		
		String stripPath = outFile.getPath();
		int toLength = (int) (1000.0*toS*60);
		File cacheFile = new File(stripPath+".tmp");
        rData += " --> MapQuest API call [ "+genPoint+" ] \n" + shapeData;        
		try {
			FileOutputStream out = new FileOutputStream(cacheFile);
			Response binaryResult = Jsoup.connect(genPoint)
				.ignoreContentType(true)
				.maxBodySize(1024*1024*1024*100)
				.timeout(toLength)
				.data("key", mqb.getApiKey())
				.data("shape", shapeData)
				.data("size", "512,386@2x")
				.data("zoom", "10")
				.method(Method.GET)
				.execute();
			out.write(binaryResult.bodyAsBytes());
			out.close();
		}
        catch (SocketTimeoutException stx) { stx.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		if (cacheFile.length() > 0) {
			wc.moveFileSilently(cacheFile.getPath(), outFile.getPath());
		} else { System.out.println("0 byte download!"); }
		cacheFile.delete();
		System.out.flush();		
		
		return rData;
		
	}
	
	public String doRadarOverlay(Connection dbc, String radarSite) {

		String dataBack = "";
		
		CommonBeans cb = new CommonBeans();
		GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
		MapQuestBeans mqb = new MapQuestBeans();
		RadarBeans rb = new RadarBeans();
		
		final int toS = 30;		
		int toLength = (int) (1000.0*toS*60);
		final String genPoint = apiBaseUri + "/staticmap/v5/map";
		final int rW = rb.getRadW();
		final int rH = rb.getRadH();
		
		try {
			JSONArray radarList = getWeatherAction.getRadarList(dbc);
			for(int i = 0; i < radarList.length(); i++) {
				JSONObject tRad = radarList.getJSONObject(i);
				String tSite = tRad.getString("Site");
				if(tSite.equals(radarSite) || radarSite.equals("_ALL")) {
					JSONArray bounds = new JSONArray(tRad.getString("BoundsNSEW"));
					double bN = bounds.getDouble(0);
					double bS = bounds.getDouble(1);
					double bE = bounds.getDouble(2);
					double bW = bounds.getDouble(3);
					String bbString = bN + "," + bW + "," + bS + "," + bE;
					dataBack += "Doing overlay for " + tSite + " - bounds = " + bbString;
					File overlayFile = new File(cb.getPersistTomcat()+"/Get/Radar/" + tSite + "/_Overlay.jpg");
					FileOutputStream out = new FileOutputStream(overlayFile);
					Response binaryResult = Jsoup.connect(genPoint)
						.ignoreContentType(true)
						.maxBodySize(1024*1024*1024*100)
						.timeout(toLength)
						.data("key", mqb.getApiKey())
						.data("boundingBox", bbString)
						.data("size", rW+","+rH)
						.data("margin", "-250")
						.method(Method.GET)
						.execute();
					out.write(binaryResult.bodyAsBytes());
					out.close();
				}				
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return dataBack;
		
	}

	public String testMapCreation() {
		
		WeatherBot wxBot = new WeatherBot();
		
		String returnData = "";
		
		CommonBeans cb = new CommonBeans();
				
		File outFile = new File(cb.getPathChartCache() + "/mapTester.jpg");
		String points = "39.011,-94.702|39.023,-94.661|39.012,-94.591|39.011,-94.639";
		
		try { returnData += imageWithBox(outFile, points); } catch (Exception e) { e.printStackTrace(); }
		try { wxBot.botBroadcastImage(outFile, returnData); } catch (Exception e) { e.printStackTrace(); }
		
		return returnData;
				
	}
	
	
}
