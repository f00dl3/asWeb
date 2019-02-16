/*
by Anthony Stump
Created: 2 Apr 2018
Updated: 7 Jun 2018
 */

package asWebRest.resource;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SessionResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
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
                //For later use if needed. Right now unused.
            }
        }
        
        return returnData;
        
    }
    
}