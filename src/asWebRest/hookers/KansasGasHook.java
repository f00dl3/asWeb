/*
by Anthony Stump
Created: 26 Jan 2020
Updated: 27 Jan 2020
 */

package asWebRest.hookers;

import java.util.Map;
import java.sql.Connection;
import org.apache.commons.lang3.exception.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

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
	
}
