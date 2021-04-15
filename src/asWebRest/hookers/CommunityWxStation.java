/*
by Anthony Stump
Split from AmbientWxStation 10 Mar 2021
Updated: 10 Mar 2021
 */

package asWebRest.hookers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.lehman.ambientweatherjava.AmbientWeather;
import com.lehman.ambientweatherjava.DataRecord;
import com.lehman.ambientweatherjava.Device;

import asWebRest.dao.WeatherDAO;
import asWebRest.secure.AmbientWeatherBeans;
import asWebRest.secure.WUndergroundBeans;
import asWebRest.shared.WebCommon;

public class CommunityWxStation {

	private String apiCall_WUnderground_C(String stations) {
		String url = "https://api.weather.com/v2/pws/observations/current";
		String dataBack = "RUN API CALL WUNDERGROUND\n";
		WUndergroundBeans wub = new WUndergroundBeans();
		try {
			dataBack = Jsoup.connect(url)
				.ignoreContentType(true)
				.data("units", "e")
				.data("format", "json")
				.data("apiKey", wub.getApiKey())
				.data("stationId", stations)
				.execute()
				.body();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataBack;
	}
	
	public String returnData() {
		
		AmbientWeatherBeans awb = new AmbientWeatherBeans();
		String dataBack = "";
		
		String awAppKey = awb.getApiKey_Application();
		String awCliKey = awb.getApiKey_Client();
		
		try { 
			AmbientWeather aw = new AmbientWeather(awAppKey, awCliKey);
			Device[] devices = aw.listUsersDevices();
			String mainMac = devices[0].getMacAddress();
			DataRecord[] records = aw.queryDeviceData(mainMac);
			dataBack += records.toString();
		} catch (Exception e) {
			dataBack += e.getMessage();
		}
		
		return dataBack;
		
	}
	
	public String returnWunder_Community(Connection dbc) {

		WebCommon wc = new WebCommon();
		WeatherDAO wxdao = new WeatherDAO();
		WUndergroundBeans wub = new WUndergroundBeans();
		String dataBack = "";
		
		JSONArray communityStations = wub.getCommunityStations();
		JSONArray base = new JSONArray();
		JSONObject encapsulator = new JSONObject();
		String stations = "";
		
		for(int ia = 0; ia < communityStations.length(); ia++) {
			stations += communityStations.getString(ia);
		}
		
		// Figure this out for merged API call?
		
		for(int i = 0; i < communityStations.length(); i++) {
			
			String station = communityStations.getString(i);
			String rawData = apiCall_WUnderground_C(station);
			
			String tTemp = ""; 
			String tDewpoint = ""; 
			String tPressureIn = ""; 
			String tRelativeHumidity = ""; 
			String tTimeString = "";
			String tWindDegrees = "";
			String tWindSpeed = "";
			String tWindGust = ""; 
			String tDailyRain = "";
			String tSunlight = "";
			String tRainRate = "";
			String tHeatIndex = "";

			JSONObject stationData = new JSONObject();			
			JSONObject baseRaw = new JSONObject(rawData);

			JSONArray secondBaseRaw = baseRaw.getJSONArray("observations");
			JSONObject homeData = (JSONObject) secondBaseRaw.get(0);
			JSONObject homeDataI = homeData.getJSONObject("imperial");

			stationData.put("Weather", "UNAVAIL");
			tTimeString = homeData.getString("obsTimeLocal"); 
			if(wc.isSet(tTimeString)) { stationData.put("TimeString", tTimeString); }
			
			try { tTemp = Integer.toString(homeDataI.getInt("temp")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tDailyRain = Double.toString(homeDataI.getDouble("precipTotal")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tDewpoint = Integer.toString(homeDataI.getInt("dewpt")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tHeatIndex = Integer.toString(homeDataI.getInt("heatIndex")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tPressureIn = Double.toString(homeDataI.getDouble("pressure")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tRainRate = Double.toString(homeDataI.getDouble("precipRate")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tRelativeHumidity = Integer.toString(homeData.getInt("humidity")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tSunlight = Double.toString(homeData.getDouble("solarRadiation")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tWindDegrees = Integer.toString(homeData.getInt("winddir")); } catch (Exception e) { dataBack += e.getMessage(); } 
			try { tWindSpeed = Integer.toString(homeDataI.getInt("windSpeed")); } catch (Exception e) { dataBack += e.getMessage(); }
			try { tWindGust = Integer.toString(homeDataI.getInt("windGust")); } catch (Exception e) { dataBack += e.getMessage(); }
			
			if(wc.isSet(tTemp)) { stationData.put("Temperature", tTemp); }
			if(wc.isSet(tDailyRain)) { stationData.put("DailyRain", tDailyRain); }
			if(wc.isSet(tDewpoint)) { stationData.put("Dewpoint", tDewpoint); }
			if(wc.isSet(tHeatIndex)) { stationData.put("HeatIndex", tHeatIndex); }
			if(wc.isSet(tPressureIn)) { stationData.put("PressureIn", tPressureIn); }
			if(wc.isSet(tRelativeHumidity)) { stationData.put("RelativeHumidity", tRelativeHumidity); }
			if(wc.isSet(tRainRate)) { stationData.put("RainRate", tRainRate); }
			if(wc.isSet(tSunlight)) { stationData.put("Sunlight", tSunlight); }
			if(wc.isSet(tWindDegrees)) { stationData.put("WindDegrees", tWindDegrees); }
			if(wc.isSet(tWindSpeed) && !tWindSpeed.equals("-999.0") && !tWindSpeed.equals("-9999.0")) { stationData.put("WindSpeed", tWindSpeed); }
			if(wc.isSet(tWindGust) && !tWindGust.equals("-999.0") && !tWindGust.equals("-9999.0")) { stationData.put("WindGust", tWindGust); }
			
			dataBack = homeData.toString();
			
			encapsulator.put(station, stationData);
			
			dataBack += rawData;
			
		}
		
	
		try {
			
			base.put(encapsulator);
			
			List<String> qParams = new ArrayList<String>();
			qParams.add(encapsulator.toString());
			
			wxdao.setRapidSDIHome(dbc, qParams);
			
			dataBack += base.toString();
			
		} catch (Exception e) {
			
			dataBack += e.getMessage();
			
		}
		
		return dataBack;
		
		
	}
	
}


