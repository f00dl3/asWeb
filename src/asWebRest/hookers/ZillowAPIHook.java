/*
by Anthony Stump
Created: 24 Jan 2020
Updated: 25 Jan 2020
 */

package asWebRest.hookers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.secure.ZillowBeans;

public class ZillowAPIHook {

	private String zillowApi = "https://www.zillow.com/webservice";
	public String getZillowApi() { return zillowApi; }
	
	
	public Document apiCallZestimate(String zpid) {
		ZillowBeans zb = new ZillowBeans();
		String url = getZillowApi() + "/GetZestimate.htm";
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
				.data("zws-id", zb.getApiKey())
				.data("zpid", zpid)
				.post();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public void autoZestimates(Connection dbc) {
		GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
		UpdateFinanceAction updateFinanceAction = new UpdateFinanceAction(new FinanceDAO());
		JSONArray dailyValues = new JSONArray();
		JSONArray properties = getFinanceAction.getZillowPIDs(dbc);
		for(int i = 0; i < properties.length(); i++) {
			JSONObject tJo = properties.getJSONObject(i);
			String propertyId = tJo.getString("ZPID");
			String zestimate = "0";
			JSONObject dataSubset = new JSONObject();
			try { zestimate = returnZestimate(propertyId); } catch (Exception e) { e.printStackTrace(); }
			dataSubset
				.put("PropertyID",  propertyId)
				.put("Description", tJo.getString("Who"))
				.put("Address",  tJo.getString("Address"))
				.put("Zestimate", zestimate);
			dailyValues.put(dataSubset);
			if(tJo.getInt("MyAsset") == 1) {
				try { updateFinanceAction.setZillowHomeValue(dbc, zestimate); } catch (Exception e) { e.printStackTrace(); }
			}
		}
		try {			
			List<String> qParams = new ArrayList<String>();
			qParams.add(0, dailyValues.toString());
			updateFinanceAction.setZillowDailyUpdate(dbc, qParams);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public String returnZestimate(String zpid) {
		String returnVal = "ERROR";
		try {
			Document doc = apiCallZestimate(zpid);
			Element tElement = doc.select("amount").first();
			returnVal = tElement.text();
		} catch (Exception e) {
			// e.printStackTrace();
			returnVal += "\n" + ExceptionUtils.getStackTrace(e);
		}
		return returnVal;
	}
	
}
