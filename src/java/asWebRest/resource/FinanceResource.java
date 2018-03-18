/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 18 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFinanceAction;
import asWebRest.dao.FinanceDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class FinanceResource extends ServerResource {
    
    @Get
    public String represent() {
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        JSONArray amSch = getFinanceAction.getAmSch();  
        return amSch.toString();
    }
    
        
    @Post
    public String doPost(Representation argsIn) {
        
        GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
                        
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
                
                case "getAutoMpg":
                    JSONArray autoMpg = getFinanceAction.getAutoMpg();
                    returnData += autoMpg.toString();
                    break;
                    
            }
        }
    
        return returnData;
        
    }
    
}
