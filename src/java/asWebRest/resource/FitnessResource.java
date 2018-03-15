/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 14 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.dao.FitnessDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class FitnessResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch (doWhat) {
                case "getAll": 
                    String xdt1 = argsInForm.getFirstValue("XDT1");
                    String xdt2 = argsInForm.getFirstValue("XDT2");
                    if(xdt1 != null && xdt2 != null) {
                        qParams.add(xdt1);
                        qParams.add(xdt2);
                        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
                        JSONArray allRecs = getFitnessAction.getAll(qParams);  
                        returnData += allRecs.toString();
                    } else {
                        returnData += "ERROR";
                    }

            }
        }
        
        return returnData;
        
    }
        
}
