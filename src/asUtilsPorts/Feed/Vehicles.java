/*
by Anthony Stump
Created: 4 Nov 2020
Updated: on creation
*/

package asUtilsPorts.Feed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import asUtilsPorts.Mailer;
import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetStockAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.action.UpdateStockAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.StockDAO;
import asWebRest.secure.FinnHubBeans;
import asWebRest.secure.VinAuditBeans;

public class Vehicles {	
		 
	private String apiCallVehicle(String vin, String mileage) {
		VinAuditBeans vab = new VinAuditBeans();
		String url = "https://marketvalue.vinaudit.com/getmarketvalue.php";
		String dataBack = "apiCallVehicle()\n";
		try {
			dataBack = Jsoup.connect(url)
				.ignoreContentType(true)
				.data("vin", vin)
				.data("key", vab.getApiKey())
				.data("format", "json")
				.data("mileage", mileage)
				.data("period", "180")
				.execute()
				.body();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataBack;
	}
	
	public String getVehicleValue(Connection dbc) {
		
		String dataBack = "Daily vehicle value report\n\n";
		
		GetFinanceAction gfa = new GetFinanceAction(new FinanceDAO());
		UpdateFinanceAction ufa = new UpdateFinanceAction(new FinanceDAO());
		JSONArray assets = gfa.getAssetTrack(dbc);
		JSONObject vehicleIndex = new JSONObject();
		
		for(int i = 0; i < assets.length(); i++) {
			try {
				JSONObject tVeh = assets.getJSONObject(i);
				if(tVeh.getString("Related").equals("VehicleValue")) { 
					String tVehicle = tVeh.getString("Description");
					String tVIN = tVeh.getString("Serial");
					String tSubdata = tVeh.getString("Notes");
					String[] tSubdataArray = tSubdata.split(" ");
					String tMileage = tSubdataArray[0];
					JSONObject tApiCallData = new JSONObject(apiCallVehicle(tVIN, tMileage));
					JSONObject tApiCallPrices = tApiCallData.getJSONObject("prices");
					Double tValueDbl = tApiCallPrices.getDouble("average");
					int tValueInt = (int) Math.round(tValueDbl);
					String tValue = String.valueOf(tValueInt);
					List<String> qParams = new ArrayList<>();
					qParams.add(0, tValue);
					qParams.add(1, tSubdata);
					qParams.add(2, tVehicle);
					ufa.setAssetTrackUpdate(dbc, qParams);
					JSONObject tVIObj = new JSONObject();
					tVIObj
						.put("vin", tVIN)
						.put("mileage", tMileage)
						.put("value", tValue);
					vehicleIndex.put(tVehicle, tVIObj);					
				}
			} catch (Exception e) {
			}
		}
		
		dataBack += vehicleIndex.toString();
		
		return dataBack;
		
	}
	
}