/*
by Anthony Stump
Created: 22 Nov 2019
Updated: 13 Dec 2019

POST REQUEST VIA COMMAND LINE ala 
	wget --no-check-certificate --post-data 'doWhat=getFfxivMerged' https://localhost:8444/asWeb/r/FFXIV

 */

package asWebRest.resource;

import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import asUtilsPorts.CamController;
import asUtilsPorts.CamNightly;
import asUtilsPorts.CamPusher;
import asUtilsPorts.Feeds;
import asUtilsPorts.GetDaily;
import asUtilsPorts.Feed.MHPFetch;
import asUtilsPorts.Feed.cWazey;
import asUtilsPorts.Tests.TestStuff;

public class BackendResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
                        
        JSONObject mergedResults = new JSONObject();
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
            
            	case "_DEVTEST":
            		cWazey waze = new cWazey();
            		waze.ripShit(dbc);
            		break;
            		            		
            	case "_TEST":
            		TestStuff testStuff = new TestStuff();
            		returnData = testStuff.stupidTomcatSandboxing();
            		break;
            		
            	case "CamController":
            		CamController cc = new CamController();
            		cc.initCams();
            		break;
            		
            	case "CamPusher":
            		CamPusher camPusher = new CamPusher();
            		camPusher.pushIt(dbc);
            		break;
            		
            	case "CamNightly":
            		CamNightly camNightly = new CamNightly();
            		camNightly.doJob(dbc);
            		break;
            		
            	case "Feeds":
            		String interval = "2m";
            		Feeds feeds = new Feeds();
            		try { interval = argsInForm.getFirstValue("interval"); } catch (Exception e) { }
            		switch(interval) {
            			case "1h": returnData = feeds.doHourly(dbc); break;
            			case "2m": default: returnData = feeds.do2Minute(dbc); break;
            		}
            		break;
            
            	case "GetDaily":
            		int daysBack = 1;
            		GetDaily gd = new GetDaily();
            		try { daysBack = Integer.parseInt(argsInForm.getFirstValue("days")); } catch (Exception e) { }
            		returnData = gd.getDaily(dbc, daysBack);
            		break;
            		
	            case "MHPFetch":
	                MHPFetch mhpFetch = new MHPFetch();
	                String mhpArg1 = "A";
	                try { mhpArg1 = argsInForm.getFirstValue("troop"); } catch (Exception e) { }
	                mhpFetch.doMHP(dbc, mhpArg1);                    
	                break;
                
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
        
}
