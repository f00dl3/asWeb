/*
by Anthony Stump
Created: 22 Nov 2019
Updated: 5 Jan 2020

POST REQUEST VIA COMMAND LINE ala 
	wget --no-check-certificate --post-data 'doWhat=getFfxivMerged' https://localhost:8444/asWeb/r/FFXIV

 */

package asWebRest.resource;

import asWebRest.hookers.EvergyAPIHook;
import asWebRest.hookers.KansasGasHook;
import asWebRest.hookers.WeatherBot;
import asWebRest.hookers.ZillowAPIHook;
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
import asUtilsPorts.CodexImport;
import asUtilsPorts.Feeds;
import asUtilsPorts.GetDaily;
import asUtilsPorts.Mailer;
import asUtilsPorts.Radar;
import asUtilsPorts.Cams.KilaeuaCam;
import asUtilsPorts.Feed.CF6Daily;
import asUtilsPorts.Feed.MHPFetch;
import asUtilsPorts.UbuntuVM.BackThatAssUp;
import asUtilsPorts.Weather.RadarList;
import asUtilsPorts.Weather.RadarNightly;

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
            
            	case "Backup":
        			BackThatAssUp btau = new BackThatAssUp();
        			btau.reminder();
        			break;
            		
            	case "Broadcast":
            		Mailer mailer = new Mailer();
            		String message = "(NO MESSAGE TO BROADCAST)";
            		try { message = argsInForm.getFirstValue("message"); } catch (Exception e) { }
            		mailer.sendMultiAlert(message, false);
            		break;
            		
            	case "CamController":
            		CamController.initCams();
            		break;
            		
            	case "CamPusher":
            		CamPusher cp = new CamPusher();
            		cp.pushIt(dbc);
            		break;
            		
            	case "CamNightly":
            		CamNightly cn = new CamNightly();
            		cn.doJob(dbc);
            		break;
            		
            	case "CF6":
            		CF6Daily cf6 = new CF6Daily();
            		int daysBackB = 1;
            		try { daysBackB = Integer.parseInt(argsInForm.getFirstValue("days")); } catch (Exception e) { }
            		returnData += cf6.getCf6(dbc, daysBackB);
            		break;
            		
            	case "Codex":
            		String argsIn2 = "";
            		try { argsIn2 = argsInForm.getFirstValue("interval"); } catch (Exception e) { }
            		CodexImport.wrapper(argsIn2);
            		break;

	        	case "Evergy":
	        		String dateStart = "2020-01-01";
	        		String dateEnd = "2020-01-01";;
	        		try { dateStart = argsInForm.getFirstValue("start"); } catch (Exception e) { e.printStackTrace(); }
	        		try { dateEnd = argsInForm.getFirstValue("end"); } catch (Exception e) { e.printStackTrace(); }
	        		EvergyAPIHook evergy = new EvergyAPIHook();
	        		returnData += evergy.updateDatabase(dbc, dateStart, dateEnd);
	        		break;
	        		
            	case "Feeds":
            		String interval = "2m";
            		try { interval = argsInForm.getFirstValue("interval"); } catch (Exception e) { }
            		switch(interval) {
            			case "1h": returnData = Feeds.doHourly(dbc); break;
            			case "5m": returnData = Feeds.do5Minute(dbc); break;
            			case "2m": default: returnData = Feeds.do2Minute(dbc); break;
            		}
            		break;
            
            	case "GetDaily":
            		int daysBack = 1;
            		try { daysBack = Integer.parseInt(argsInForm.getFirstValue("days")); } catch (Exception e) { }
            		returnData = GetDaily.getDaily(dbc, daysBack);
            		break;
            		
            	case "KansasGasService":
        			KansasGasHook kgs = new KansasGasHook();
        			kgs.writeToDatabase(dbc);
        			break;
            		
            	case "Kilauea":
            		KilaeuaCam kc = new KilaeuaCam();
            		kc.doKilaeua();
            		break;
            		
            	case "Radar":
            		Radar radar = new Radar();
            		radar.fetchRadars();
            		break;
            		
            	case "RadarList":
            		returnData = RadarList.listSites(dbc);
            		break;
            		
            	case "RadarNightly":
            		RadarNightly.process(dbc);
            		break;
            		
	            case "MHPFetch":
	                String mhpArg1 = "A";
	                try { mhpArg1 = argsInForm.getFirstValue("troop"); } catch (Exception e) { }
	                MHPFetch.doMHP(dbc, mhpArg1);                    
	                break;

				case "Zillow":
					ZillowAPIHook zapi = new ZillowAPIHook();
					zapi.autoZestimates(dbc);
					break;
					
	            case "WxBot":
	        		WeatherBot wxb = new WeatherBot();
	            	wxb.startBot();
	            	break;
                
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
        
}
