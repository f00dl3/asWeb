/*
by Anthony Stump
Created: 4 Apr 2018
Updated: 29 Dec 2018
 */

package asWebRest.resource;

import asWebRest.action.GetHomeAction;
import asWebRest.dao.HomeDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class HomeResource extends ServerResource {
        
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetHomeAction getHomeAction = new GetHomeAction(new HomeDAO());
                        
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
            switch(doWhat) {
                
                case "getAlarmBatteries":
                    JSONArray alarmBatt = getHomeAction.getAlarmBatteries(dbc);
                    returnData += alarmBatt.toString();
                    break;
                
                case "getDeepClean":
                    JSONArray deepClean = getHomeAction.getHouseDeepCleaning(dbc);
                    returnData += deepClean.toString();
                    break;
                
                case "getMeasure":
                    qParams.add(argsInForm.getFirstValue("level"));
                    JSONArray measure = getHomeAction.getMeasure(dbc, qParams);
                    returnData += measure.toString();
                    break;
                
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
