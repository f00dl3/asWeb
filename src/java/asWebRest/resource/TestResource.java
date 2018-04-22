/*
by Anthony Stump
Created: 22 Apr 2018
 */

package asWebRest.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TestResource extends ServerResource {
    
    @Get
    public String represent() {
        
        String test1 = getRequest().getRootRef().toString();
        String testData = test1;
        return testData;
        
    }    
    
}
