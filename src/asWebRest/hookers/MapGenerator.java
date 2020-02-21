/*
by Anthony Stump
Created: 15 Feb 2020
Updated: 20 Feb 2020
https://developer.mapquest.com/documentation/open/static-map-api/v4/map/get/
 */

package asWebRest.hookers;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;

import org.jsoup.Jsoup;
import org.json.JSONArray;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

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
