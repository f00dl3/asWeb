/*
by Anthony Stump
Created: 19 Nov 2019
Updated: On Creation
 */

package asWebRest.resource;

import asWebRest.hookers.CgwRipper;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CgwRipperResource extends ServerResource {
    
    @Get
    public String represent() {

        CgwRipper cgwRipper = new CgwRipper();
        String testData = cgwRipper.parserMain();
        return testData;
        
    }    
       
}
