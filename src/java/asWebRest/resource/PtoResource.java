/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 29 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetPtoAction;
import asWebRest.dao.PtoDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class PtoResource extends ServerResource {
    
    @Get
    public String represent() {
        GetPtoAction getPtoAction = new GetPtoAction(new PtoDAO());
        JSONArray pto = getPtoAction.getPto();  
        return pto.toString();
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        GetPtoAction getPtoAction = new GetPtoAction(new PtoDAO());
                        
        List<String> qParams = new ArrayList<>();
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
                    JSONArray workPTO = getPtoAction.getPto();
                    returnData += workPTO.toString();
                    break;
                    
            }
        }
       
        return returnData;
        
    }
    
}
