/*
by Anthony Stump
Created: 22 Apr 2018
 */

package asWebRest.resource;

import asWebRest.hookers.SnmpWalk;
import java.io.IOException;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TestResource extends ServerResource {
    
    @Get
    public String represent() {
        
        //snmpWalk [ -c commName -p portNum -v snmpVer] targetAddr oid
        String snmpArgs[] = {
            "-c", "public",
            "-p", "161",
            "-v", "3",
            "127.0.0.1",
            "1.3.6.1.4.1.7950.2.10.31.3"
        };
        
        String snmpTest = "UNEXEC!";
        
        try {
            SnmpWalk snmpWalk = new SnmpWalk();
            snmpTest = snmpWalk.getSnmpWalk(snmpArgs);
        } catch (IOException io) { io.printStackTrace(); }
        
        String test1 = getRequest().getRootRef().toString();
        String test2 = System.getProperty("catalina.base");
        
        String testData = "test1: " + test1 + "\n" +
                "test 2: " + test2 + "\n\n" +
                "snmpTest: " + snmpTest;
        return testData;
        
    }    
    
}
