/*
by Anthony Stump
Created: 25 Jan 2020
Updated: 22 Jul 2020
 */

package asWebRest.hookers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import asWebRest.action.UpdateUtilityUseAction;
import asWebRest.dao.UtilityUseDAO;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.secure.EvergyBeans;

public class EvergyAPIHook {
	
	private final String evergyApiBase = "https://www.evergy.com/api";
	private final String evergyAccountLanding = "https://www.evergy.com/ma/my-account/account-summary/single-account";
	public String getEvergyApiBase() { return evergyApiBase; }
    
	private String apiCallEnergyUse(Map<String, String> cookies, String dateForUseA, String dateForUseB) {
		EvergyBeans eb = new EvergyBeans();
		String url = getEvergyApiBase() + "/report/usage/" + eb.getMeterId();
		String dataBack = "apiCallEnergyUse()\n";
		try {
			dataBack = Jsoup.connect(url)
				.cookies(cookies)
				.ignoreContentType(true)
				.referrer(evergyAccountLanding)
				.data("interval", "d")
				.data("from", dateForUseA)
				.data("to", dateForUseB)
				.execute()
				.body();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataBack += "DBG --> Fetched Evergy from " + dateForUseA + " to " + dateForUseB + "\n";
		return dataBack;
	}
    
    public String dailyJob(Connection dbc) {
    	String dataBack = "";
    	DateTime tDateTimeS = new DateTime().minusDays(0);
    	DateTime tDateTimeF = new DateTime().minusDays(31);
		DateTimeFormatter formatOut = DateTimeFormat.forPattern("yyyy-MM-dd");
		String friendlyDate = formatOut.print(tDateTimeF);
		String friendlyDateF = formatOut.print(tDateTimeS);
		try {
			dataBack += updateDatabase(dbc, friendlyDate, friendlyDateF);
		} catch (Exception e) { 
			e.printStackTrace();
			dataBack += "ERROR ON " + friendlyDate + " TO " + friendlyDate;
		}
		return dataBack;
    }
    
	private Response initialLogin(Connection dbc) {
		WebUIserAuthDAO auth = new WebUIserAuthDAO();
		EvergyBeans eb = new EvergyBeans();
		String url = "https://www.evergy.com/log-in";
		Response res = null;
		try {
			res = Jsoup.connect(url)
				.data("username", eb.getUser())
				.data("password", auth.getExternalPassword(dbc, "Evergy"))
				.method(Method.POST)
				.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public Map<String, String> returnLogin(Connection dbc) {
		Map<String, String> cookies = null;
		try {
			Response res = initialLogin(dbc);
			cookies = res.cookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookies;
	}
	
	public String returnEnergyUse(Connection dbc, String dateForUseA, String dateForUseB) {
		DateTimeFormatter formatIn = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter formatOut = DateTimeFormat.forPattern("M/d/yyyy");
		DateTime dateForUseAdt = formatIn.parseDateTime(dateForUseA);
		DateTime dateForUseBdt = formatIn.parseDateTime(dateForUseB);
		String dateForUseAf = formatOut.print(dateForUseAdt);
		String dateForUseBf = formatOut.print(dateForUseBdt);
		String returnVal = "returnEnergyUse()\n";
		try {
			returnVal = apiCallEnergyUse(returnLogin(dbc), dateForUseAf, dateForUseBf);
		} catch (Exception e) {
			returnVal += "\n" + ExceptionUtils.getStackTrace(e);
		}
		return returnVal;
	}
	
	public String updateDatabase(Connection dbc, String dateForUseA, String dateForUseB) {	
		String returnData = "";
		UpdateUtilityUseAction updateUtilityUseAction = new UpdateUtilityUseAction(new UtilityUseDAO());
		JSONObject energyData = new JSONObject();
		DateTimeFormatter formatIn = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
		DateTimeFormatter formatOut = DateTimeFormat.forPattern("yyyy-MM-dd");		
		String reu = returnEnergyUse(dbc, dateForUseA, dateForUseB);
		try {
			energyData = new JSONObject(reu);
			returnData = energyData.toString();
			JSONArray dataArray = energyData.getJSONArray("data"); 
			for(int i = 0; i < dataArray.length(); i++) {	
				List<String> qParams = new ArrayList<String>();
				JSONObject tJo = dataArray.getJSONObject(i);
				String evergyDate = tJo.getString("billDate");	
				DateTime tDateTime = formatIn.parseDateTime(evergyDate);
				String friendlyDate = formatOut.print(tDateTime);
				try {
					qParams.add(friendlyDate);
					qParams.add(Double.toString(tJo.getDouble("usage")));
					qParams.add(Double.toString(tJo.getDouble("demand")));
					qParams.add(Double.toString(tJo.getDouble("peakDemand")));
					//qParams.add(tJo.getString("peakDateTime"));
					qParams.add("Empty");
					qParams.add(Double.toString(tJo.getDouble("cost")));
					updateUtilityUseAction.setElectricityUse(dbc, qParams);
				} catch (Exception e) { e.printStackTrace(); }
			}			
		} catch (Exception e) { 
			e.printStackTrace();
			returnData += reu;
		}
		return returnData;
	}
	
}
