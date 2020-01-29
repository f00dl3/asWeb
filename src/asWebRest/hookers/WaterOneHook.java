/*
by Anthony Stump
Created: 26 Jan 2020
Updated: 28 Jan 2020
 */

package asWebRest.hookers;

import java.sql.Connection;
import java.util.Map;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.secure.WaterOneBeans;

public class WaterOneHook {
	
	// secret question
	// ctl00%24Main%24txtAnswer=
	// ct100%24Main%24btnSubmitNew=Submit
	
	private String basePage = "https://secure8.i-doxs.net/BDX/default.aspx?BillerID=jG1dZTufJv";
	private String secretQ = "https://secure8.i-doxs.net/BDX/Secure/SecretQuestion.aspx";
	private String acctHome = "https://secure8.i-doxs.net/BDX/Secure/Home.aspx";
	private String debugData = "";
        
	private Response initialLogin(Connection dbc) {
		WaterOneBeans w1b = new WaterOneBeans();
		WebUIserAuthDAO auth = new WebUIserAuthDAO();
        JSONObject moreParams = parsePrefetch(prefetchLanding());
        //System.out.println("DEBUG: " + moreParams.toString());
        debugData += "initialLogin()";
        Response res = null;
        try { res = Jsoup.connect(basePage)
                .data("ct100$scmScriptManager", "ct100$Main$upMain|ct100$Main$LoginBox$btLogin")
                .data("ct100$Main$LoginBox$UserName", w1b.getUser())
                .data("ct100$Main$LoginBox$Password", auth.getExternalPassword(dbc, "WaterOne"))
                .data("ct100$ddlLanguages", "en")
                .data("ct100$Main$LoginBox$btLogin", "Sign In")
                .data("__VIEWSTATE", moreParams.getString("__VIEWSTATE"))
                .data("__VIEWSTATE1", moreParams.getString("__VIEWSTATE1"))
                .data("__VIEWSTATEGENERATOR", moreParams.getString("__VIEWSTATEGENERATOR"))
                .data("__EVENTVALIDATION", moreParams.getString("__EVENTVALIDATION"))
                .data("__ncforminfo", moreParams.getString("__ncforminfo"))
                .data("__ASYNCHPOST", "true")
                .data("__VIEWSTATEFIELDCOUNT", "2")
                .data("__LASTFOCUS", "")
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT", "")
                .method(Method.POST)
                .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
        
    private JSONObject parsePrefetch(Document doc) {
        Element eventValidation = doc.select("#__EVENTVALIDATION").first();
        Element viewState = doc.select("#__VIEWSTATE").first();
        Element viewStateGenerator = doc.select("#__VIEWSTATEGENERATOR").first();
        Element viewState1 = doc.select("#__VIEWSTATE1").first();
        Element ncFormInfo = doc.select("input[name=__ncforminfo]").first();
        JSONObject moreParams = new JSONObject();
        moreParams
                .put("__EVENTVALIDATION", eventValidation.attr("value"))
                .put("__VIEWSTATEGENERATOR", viewStateGenerator.attr("value"))
                .put("__VIEWSTATE", viewState.attr("value"))
                .put("__VIEWSTATE1", viewState1.attr("value"))
                .put("__ncforminfo", ncFormInfo.attr("value"));
        return moreParams;
    }
    	
    private Document prefetchLanding() {
        debugData += "preFetchLanding() entered\n";
        Document doc = null;
        try {
            doc = Jsoup.connect(basePage)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    
	private Document prefetchSecurityQuestion(Map<String, String> cookies) {
        debugData += "prefetchSecurityQuestion() entered\n";
        Document doc = null;
        try {
            doc = Jsoup.connect(secretQ)
                    .cookies(cookies)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    
    private Map<String, String> getCookies(Connection dbc) {
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
            
    public String systematic(Connection dbc) {
        String rData = "";
        Map<String, String> cookies = null;
        try { cookies = getCookies(dbc); } catch (Exception e) { e.printStackTrace(); }
        rData = prefetchSecurityQuestion(cookies).toString();
        return rData;
    }
    
    public String testPrefetch(Connection dbc) {
        return systematic(dbc);
    }
        
}
