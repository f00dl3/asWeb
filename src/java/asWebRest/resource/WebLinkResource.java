/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 7 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebLinkAction;
import asWebRest.dao.WebLinkDAO;
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
        
        final Form argsInForm = new Form(argsIn);
        
        String master = null;
         
        try {
            master = argsInForm.getFirstValue("master");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(master != null) {
            List<String> qParams = new ArrayList<>();
            qParams.add(0, master);
            GetWebLinkAction getWebLinkAction = new GetWebLinkAction(new WebLinkDAO());
            JSONArray webLinks = getWebLinkAction.getWebLinks(qParams);
            return webLinks.toString();
        } else {
            return "ERROR: NO POST DATA!";
        }        
    }
    
        
    
}
