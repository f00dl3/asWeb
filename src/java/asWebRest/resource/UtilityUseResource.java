/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 4 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetUtilityUseAction;
import asWebRest.dao.UtilityUseDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class UtilityUseResource extends ServerResource {
    
    @Get
    public String represent() {
        final String agMonV = "2018-01";
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());
        JSONArray callMe = getUtilityUseAction.getUsePhone(agMonV);  
        return callMe.toString();
    }
        @Post
    public String doPost(Representation argsIn) {
        
        GetUtilityUseAction getUtilityUseAction = new GetUtilityUseAction(new UtilityUseDAO());
                        
        JSONObject mergedResults = new JSONObject();
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
                case "getUtils":
                    break;
                // figure out agMonV w/o billions of queries!
            }
        }
        
        return returnData;
        
    }
    
}
