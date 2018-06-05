/*
by Anthony Stump
Created: 9 May 2018
 */

package asWebRest.resource;

import asWebRest.shared.JsonWorkers;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ToolsResource extends ServerResource {
       
    @Post
    public String doPost(Representation argsIn) {
        
        JsonWorkers jw = new JsonWorkers();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
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
                
                case "JsonToDataStore":
                    JSONArray dataInput = new JSONArray();
                    dataInput.put(argsInForm.getFirstValue("jsonToConvert"));
                    returnData = jw.getDesiredDataType(
                            dataInput,
                            "dataStore",
                            argsInForm.getFirstValue("identifier")
                    );
                    break;
                
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }

}
