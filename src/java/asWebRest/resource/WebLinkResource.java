/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 21 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebLinkAction;
import asWebRest.dao.WebLinkDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class WebLinkResource extends ServerResource {
    
    @Get
    public String represent() {
        return "Undefined!";
    }
    
    @Options
    public String thisOptions() {
        return "Options!";
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetWebLinkAction getWebLinkAction = new GetWebLinkAction(new WebLinkDAO());
        JSONObject mergedResults = new JSONObject();   
        
        final Form argsInForm = new Form(argsIn);
        
        String master = null;
        String doWhat = null;
        String returnData = "";
         
        try {
            master = argsInForm.getFirstValue("master");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(master != null) {
            JSONArray webLinks = getWebLinkAction.getWebLinks(dbc, master);
            returnData += webLinks.toString();
        } else {
            if(doWhat != null) {
                
                switch(doWhat) {
                    
                    case "getLiveLinks":
                        JSONArray irsLinks = getWebLinkAction.getWebLinks(dbc, argsInForm.getFirstValue("master1"));
                        JSONArray df7Links = getWebLinkAction.getWebLinks(dbc, argsInForm.getFirstValue("master2"));
                        mergedResults
                            .put("irsLinks", irsLinks)
                            .put("df7Links", df7Links);
                        returnData = mergedResults.toString();
                        break;
                        
                }
                
            }
            returnData += "ERROR: NO POST DATA!";
        }        
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
        
    
}
