/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 7 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWeatherAction;
import asWebRest.action.GetWebLinkAction;
import asWebRest.dao.WeatherDAO;
import asWebRest.dao.WebLinkDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class WeatherResource extends ServerResource {
    
    @Post
    public String represent(Representation argsIn) {

        List<String> qParams = new ArrayList<>();      
        List<String> inParams = new ArrayList<>();      
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
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
                case "getObjsJson": 
                    String startTime = "";
                    String endTime = "";
                    int limit = 0;      
                    inParams.add(0, "DESC");
                    try {
                        qParams.add(0, argsInForm.getFirstValue("startTime"));
                        qParams.add(1, argsInForm.getFirstValue("endTime"));
                        qParams.add(2, argsInForm.getFirstValue("limit"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JSONArray wxObs = getWeatherAction.getObsJson(qParams, inParams);
                    returnData = wxObs.toString();
                    break;
                    
                case "getObsJsonLast":
                    JSONArray latestObs = getWeatherAction.getObsJsonLast();
                    returnData = latestObs.toString();
                    break;
            }
        } else {
            returnData = "ERROR: NO POST DATA!";
        }        
        
        return returnData;
        
    }
    
}
