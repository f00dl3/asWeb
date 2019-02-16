/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetPtoAction;
import asWebRest.dao.PtoDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class PtoResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetPtoAction getPtoAction = new GetPtoAction(new PtoDAO());
                        
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                
                case "getWorkPTO":
                    JSONArray workPTO = getPtoAction.getPto(dbc);
                    returnData += workPTO.toString();
                    break;
                    
            }
        }
       
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
