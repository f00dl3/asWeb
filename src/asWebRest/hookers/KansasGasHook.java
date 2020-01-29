/*
by Anthony Stump
Created: 26 Jan 2020
Updated: 29 Jan 2020
 */

package asWebRest.hookers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
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
import asWebRest.secure.KansasGasBeans;

public class KansasGasHook {
		
	private KansasGasBeans kgb = new KansasGasBeans();
	
	private String apiBase = "https://api.kansasgasservice.com/api";
	private String debugData = "";
	private String loginUrl = apiBase + "/login";
	private String usageUrl = apiBase + "/getusagehistory";
	JSONObject loginResponse = new JSONObject();
	
	public String apiCallGasUse(Map<String, String> cookies) {
		String dataBack = "apiCallGasUse()\n";
		JSONObject json = new JSONObject();
		json
			.put("billingAccountNumber", kgb.getAccount())
			.put("serviceId", 1)
			.put("culture", "en-US")
			.put("auditInfo", getAuditInfo());
		String authToken = "";
		try { authToken = loginResponse.getString("accessToken"); } catch (Exception e) { e.printStackTrace(); }
		try {
			dataBack = Jsoup.connect(usageUrl)
				.cookies(cookies)
				.ignoreContentType(true)
                .header("Content-Type", "application/json")
				.header("Access-Control-Expose-Headers", "Authorization-Token")
                .header("Authorization-Token", authToken)
                .requestBody(json.toString())
				.method(Method.POST)
				.execute()
				.body();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataBack;
	}
	
	private JSONObject getAuditInfo() {
		debugData += "called getAuditInfo\n";
		JSONObject auditInfo = new JSONObject();	
		auditInfo
			.put("csrId", JSONObject.NULL)
			.put("isCSR", false)
			.put("registeredUsername", kgb.getUser())
			.put("ldcProvider", "KGS")
			.put("isApp", false)
			.put("isMobile", true)
			.put("isWeb", false);
		return auditInfo;
	}
    
	private Response initialLogin(Connection dbc) {
		debugData += "initialLogin entered\n";
		WebUIserAuthDAO auth = new WebUIserAuthDAO();
		JSONObject json = new JSONObject();	
		Response res = null;
		json
			.put("email", kgb.getUser())
			.put("password", auth.getExternalPassword(dbc, "KansasGas"))
			.put("auditInfo", getAuditInfo())
			.put("culture", "en-US");	
		try {
			res = Jsoup.connect(loginUrl)
		.ignoreContentType(true)
                .header("Content-Type", "application/json")
                .requestBody(json.toString())
				.method(Method.POST)
				.execute();
			debugData = res.body();
			loginResponse = new JSONObject(res.body());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}	
	
	private Map<String, String> returnLogin(Connection dbc) {
		debugData += "returnLogin entered\n";
		Map<String, String> cookies = null;
		try {
			Response res = initialLogin(dbc);
			cookies = res.cookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookies;
	}
	
	public String returnGasUse(Connection dbc) {
		String returnVal = "returnGasUse()\n";
		try {
			//returnVal += initialLogin(dbc);
			returnVal = apiCallGasUse(returnLogin(dbc));
		} catch (Exception e) {
			returnVal += "\n" + ExceptionUtils.getStackTrace(e);
		}
		//returnVal += debugData;
		return returnVal;
	}
	
	public void writeToDatabase(Connection dbc) {
		JSONObject chartData = null;
		UpdateUtilityUseAction updateUtilityUseAction = new UpdateUtilityUseAction(new UtilityUseDAO());
		DateTimeFormatter formatIn = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
		DateTimeFormatter formatOut = DateTimeFormat.forPattern("yyyy-MM");		
		try {
			chartData = new JSONObject(apiCallGasUse(returnLogin(dbc)));
			JSONArray theArray = chartData.getJSONArray("chartSeries");
			for(int i = 0; i < theArray.length(); i++) {
				JSONObject thisDataset = theArray.getJSONObject(i);
				JSONArray theseItems = thisDataset.getJSONArray("chartItems");
				for(int j = 0; j < theseItems.length(); j++) {
					List<String> qParams = new ArrayList<String>();
					JSONObject thisMonth = theseItems.getJSONObject(j);
					String tMcf = Double.toString(thisMonth.getDouble("consumption"));
					String dateIn = thisMonth.getString("plotDate");
					String billedAmount = Double.toString(thisMonth.getDouble("billedAmount"));
					String billedDays = Integer.toString(thisMonth.getInt("daysOfService"));
					DateTime tDateTime = formatIn.parseDateTime(dateIn);
					String friendlyDate = formatOut.print(tDateTime);
					qParams.add(friendlyDate);
					qParams.add(tMcf);
					qParams.add(billedAmount);
					qParams.add(billedDays);
					qParams.add(tMcf);
					qParams.add(billedAmount);
					qParams.add(billedDays);
					try { updateUtilityUseAction.setGasUse(dbc, qParams); } catch (Exception e) { e.printStackTrace(); }					
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		
	}
	
}
