/*
by Anthony Stump
Created: 23 Nov 2019
Updated: 2 Jan 2020
 */

package asWebRest.resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import asWebRest.action.GetSmarthomeAction;
import asWebRest.action.UpdateSmarthomeAction;
import asWebRest.dao.SmarthomeDAO;
import asWebRest.hookers.SmartplugInterface;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;


public class SmarthomeResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {

        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
    	WebCommon wc = new WebCommon();

        List<String> qParams = new ArrayList<>();
        List<String> inParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String[] defaultArgs = {};
        String doWhat = null;
        String returnData = "";
        
        GetSmarthomeAction getSmarthomeAction = new GetSmarthomeAction(new SmarthomeDAO());
        UpdateSmarthomeAction updateSmarthomeAction = new UpdateSmarthomeAction(new SmarthomeDAO());
        SmartplugInterface smartplugInterface = new SmartplugInterface();
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
            
            	case "doSmartplug":
            		String device = "router";
            		String command = "status";
            		if(wc.isSet(argsInForm.getFirstValue("target"))) { device = argsInForm.getFirstValue("target"); }
            		if(wc.isSet(argsInForm.getFirstValue("command"))) { command = argsInForm.getFirstValue("command"); }
            		smartplugInterface.setPlug(command, device);
            		break;
            		
            	case "getArmDisarm":
                    JSONArray armDisarm = getSmarthomeAction.getArmDisarm(dbc);
                    returnData = armDisarm.toString();
            		break;       
            
            	case "getDoorEvents":
                    JSONArray doorEvents = getSmarthomeAction.getDoorEvents(dbc);
                    returnData = doorEvents.toString();
            		break;             	
            
	            case "setArmDisarm":
            		String armType = "";
            		if(wc.isSet(argsInForm.getFirstValue("ArmType"))) { armType = argsInForm.getFirstValue("ArmType"); }
            		qParams.add(0, armType);
                	returnData = updateSmarthomeAction.setArmDisarm(dbc, qParams).toString();
                	break;       	
                	
	            case "setDoorEvent":
            		String originalTimestamp = "";
            		String doorLocation = "0";
            		if(wc.isSet(argsInForm.getFirstValue("OriginalTimestamp"))) { originalTimestamp = argsInForm.getFirstValue("OriginalTimestamp"); }
            		if(wc.isSet(argsInForm.getFirstValue("DoorLocation"))) { doorLocation = argsInForm.getFirstValue("DoorLocation"); }
            		qParams.add(0, originalTimestamp);
            		qParams.add(1, doorLocation);
                	returnData = updateSmarthomeAction.setDoorEvent(dbc, qParams).toString();
                	break; 

	            case "setEnvironmentalEvent":
            		String originalTimestampE = "";
            		String eventAndLocation = "0";
            		if(wc.isSet(argsInForm.getFirstValue("OriginalTimestamp"))) { originalTimestampE = argsInForm.getFirstValue("OriginalTimestamp"); }
            		if(wc.isSet(argsInForm.getFirstValue("EventLocation"))) { eventAndLocation = argsInForm.getFirstValue("EventLocation"); }
            		qParams.add(0, originalTimestampE);
            		qParams.add(1, eventAndLocation);
                	returnData = updateSmarthomeAction.setCo2FireEvent(dbc, qParams).toString();
                	break;                  

            }
            
        }        
               
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }

        return returnData;
        
    }
        
}
