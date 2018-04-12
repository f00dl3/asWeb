/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebLinkAction;
import asWebRest.dao.WebLinkDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
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
        
        final Form argsInForm = new Form(argsIn);
        
        String master = null;
        String returnData = "";
         
        try {
            master = argsInForm.getFirstValue("master");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(master != null) {
            GetWebLinkAction getWebLinkAction = new GetWebLinkAction(new WebLinkDAO());
            JSONArray webLinks = getWebLinkAction.getWebLinks(dbc, master);
            returnData += webLinks.toString();
        } else {
            returnData += "ERROR: NO POST DATA!";
        }        
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
        
    
}
