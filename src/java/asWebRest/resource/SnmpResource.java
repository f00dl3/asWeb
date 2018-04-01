/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 1 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetSnmpAction;
import asWebRest.dao.SnmpDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
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
    
    @Post
    public String represent(Representation argsIn) {

        List<String> qParams = new ArrayList<>();      
        List<String> inParams = new ArrayList<>();      
        JSONObject mergedResults = new JSONObject();
        final Form argsInForm = new Form(argsIn);
        
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        if(doWhat != null) {
            switch (doWhat) {
                case "rapidSnmpUpdates":
                    break;
            }
        }
        
        return returnData;
    }
}
