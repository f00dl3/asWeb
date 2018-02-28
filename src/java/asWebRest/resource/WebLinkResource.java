/*
by Anthony Stump
Created: 21 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWebLinkAction;
import asWebRest.dao.WebLinkDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
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
    public String doPost(HttpServletRequest request, HttpServletResponse response) {
        String master = null;
        try { master = request.getParameter("Master"); } catch (Exception e) { e.printStackTrace(); }
        if(master != null) {
            List<String> qParams = new ArrayList<>();
            qParams.add(1, master);
            GetWebLinkAction getWebLinkAction = new GetWebLinkAction(new WebLinkDAO());
            JSONArray webLinks = getWebLinkAction.getWebLinks(qParams);
            return webLinks.toString();
        } else {
            return "ERROR: NO POST DATA!";
        }        
    }
    
        
    
}
