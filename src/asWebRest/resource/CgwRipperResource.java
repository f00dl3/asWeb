/*
by Anthony Stump
Created: 19 Nov 2019
Updated: 28 Dec 2019
 */

package asWebRest.resource;

import asWebRest.hookers.CgwRipper;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CgwRipperResource extends ServerResource {
    
    @Get
    public String represent() {

        String testData = CgwRipper.parserMain();
        return testData;
        
    }    
       
}
