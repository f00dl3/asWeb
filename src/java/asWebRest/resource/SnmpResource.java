/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 25 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetSnmpAction;
import asWebRest.dao.SnmpDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class SnmpResource extends ServerResource {
    
    @Get
    public String represent() {
        List<String> qParams = new ArrayList<>();
        qParams.add(0, "1"); //Test
        qParams.add(1, "1"); //Step
        qParams.add(2, "1"); //DateTest
        qParams.add(3, "20180224"); //Date
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        JSONArray snmpData = getSnmpAction.getRouter(qParams);  
        return snmpData.toString();
    }
    
}
