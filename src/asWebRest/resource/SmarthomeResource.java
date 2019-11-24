/*
by Anthony Stump
Created: 23 Nov 2019
Updated: on creation

OVERALL LOGIC FLOW: 
Have the commands execute to hub.

Set command to disable text alarming for 1 hour 
Set command to enable text alarming (arm system)

 */

package asWebRest.resource;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class SmarthomeResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
                        
        final Form argsInForm = new Form(argsIn);
        
        String[] defaultArgs = {};
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                    
            }
            
        }
        
        return returnData;
        
    }
        
}
