/*
by Anthony Stump
Created: 25 Mar 2018
Updated: 22 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebCalAction;
import asWebRest.action.UpdateWebCalAction;
import asWebRest.dao.WebCalDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class WebCalResource extends ServerResource {
    
    @Get
    
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        JSONArray llid = getWebCalAction.getLastLogId(dbc);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return llid.toString();
    
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
    
        String doWhat = null;
        String returnData = "";
        List<String> qParams = new ArrayList<>();
        JSONObject mergedResults = new JSONObject();
        
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        UpdateWebCalAction updateWebCalAction = new UpdateWebCalAction(new WebCalDAO());
        
        final Form argsInForm = new Form(argsIn);
        
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            
            switch(doWhat) {
                
                case "QuickCalEntry":
                    JSONArray llid = getWebCalAction.getLastLogId(dbc);
                    returnData += "Kicked off!\n";
                    returnData += llid.toString();
                    break;
            
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
