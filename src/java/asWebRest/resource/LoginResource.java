/*
by Anthony Stump
Created: 10 Feb 2018
Udpated: 16 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebAccessLogAction;
import asWebRest.action.GetWebUIserAuthAction;
import asWebRest.dao.WebAccessLogDAO;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.shared.WebCommon;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class LoginResource extends ServerResource {
    
    @Get
    public String represent() {
        GetWebAccessLogAction getWebAccessLogAction = new GetWebAccessLogAction(new WebAccessLogDAO());
        JSONArray webAccessLogs = getWebAccessLogAction.getWebAccessLogs();  
        return webAccessLogs.toString();
    }
    
    @Options
    public String thisOptions() {
        return "Options!";
    }
    
    @Post
    public String doAuth(HttpServletRequest request, HttpServletResponse response) {       
        boolean loginCheck = false;
        String hashWord = "";
        String userName = "";
        try { userName = request.getParameter("asUser"); } catch (Exception e) { e.printStackTrace(); }
        try { hashWord = WebCommon.cryptIt(request.getParameter("asPass")); } catch (Exception e) { e.printStackTrace(); }
        GetWebUIserAuthAction getWebUIserAuthAction = new GetWebUIserAuthAction(new WebUIserAuthDAO());
        String webUIserAuth = getWebUIserAuthAction.getWebUIserAuth(userName);
        if(webUIserAuth.equals(hashWord)) { loginCheck = true; }
        JSONObject returnData = new JSONObject();
        returnData.put("isValidLogin", loginCheck);
        return returnData.toString();
    }
    
        
    
}
