/*
by Anthony Stump
Created: 25 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebCalAction;
import asWebRest.dao.WebCalDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class WebCalResource extends ServerResource {
    
    @Get
    public String represent() {
        GetWebCalAction getWebCalAction = new GetWebCalAction(new WebCalDAO());
        JSONArray llid = getWebCalAction.getLastLogId();  
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
        
        final Form argsInForm = new Form(argsIn);
        
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            
            switch(doWhat) {
                
                case "QuickCalAdd":
                    //qParams.add();
                    break;
            
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
