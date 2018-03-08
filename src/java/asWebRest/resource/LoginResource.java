/*
by Anthony Stump
Created: 10 Feb 2018
Udpated: 4 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebAccessLogAction;
import asWebRest.action.GetWebUIserAuthAction;
import asWebRest.action.UpdateWebAccessLogAction;
import asWebRest.dao.WebAccessLogDAO;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.model.WebAccessLog;
import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class LoginResource extends ServerResource {
    
    WebCommon wc = new WebCommon(); 
    
    @Get @Options
    public String represent() {
        
        GetWebAccessLogAction getWebAccessLogAction = new GetWebAccessLogAction(new WebAccessLogDAO());
        JSONArray webAccessLogs = getWebAccessLogAction.getWebAccessLogs();  
        return webAccessLogs.toString();
        
    }
    
    @Post
    public String loginCheck(Representation loginInput) {
        
        final Form loginInfo = new Form(loginInput);
       
        String loginCheck = "false";
        String userName = "";
        String hashWord = "";
        
        try {
            userName = loginInfo.getFirstValue("asUser");
            hashWord = wc.cryptIt(loginInfo.getFirstValue("asPass"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        GetWebUIserAuthAction getWebUIserAuthAction = new GetWebUIserAuthAction(new WebUIserAuthDAO());
        String webUIserAuth = getWebUIserAuthAction.getWebUIserAuth(userName);
        if(webUIserAuth.equals(hashWord)) {
            loginCheck = "true";
            WebAccessLog webAccessLog = new WebAccessLog();
            webAccessLog.setUser(userName);
            webAccessLog.setRemoteIp("NA-Yet"); // To work on
            UpdateWebAccessLogAction updateWebAccessLogAction = new UpdateWebAccessLogAction(new WebAccessLogDAO());
            updateWebAccessLogAction.updateWebAccessLog(webAccessLog);
        }
        
        JSONObject returnData = new JSONObject();
        returnData.put("isValidLogin", loginCheck);
        
        return returnData.toString();
        
    }
    
}
