/*
by Anthony Stump
Created: 4 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetHomeAction;
import asWebRest.dao.HomeDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class HomeResource extends ServerResource {
        
    @Post
    public String doPost(Representation argsIn) {
        
        GetHomeAction getHomeAction = new GetHomeAction(new HomeDAO());
                        
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
                
                case "getMeasure":
                    qParams.add(argsInForm.getFirstValue("level"));
                    JSONArray measure = getHomeAction.getMeasure(qParams);
                    returnData += measure.toString();
                    break;
                
            }
        }
    
        return returnData;
        
    }
    
}
