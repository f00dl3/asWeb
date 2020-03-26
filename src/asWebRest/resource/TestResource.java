/*
by Anthony Stump
Created: 22 Apr 2018
Updated: 26 Mar 2020
 */

package asWebRest.resource;

import asWebRest.action.GetNewsFeedAction;
import asWebRest.dao.NewsFeedDAO;
import asWebRest.hookers.MapGenerator;
import asWebRest.hookers.MediaTools;
import asWebRest.hookers.SnmpWalk;
import asWebRest.hookers.WaterOneHook;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.JsonWorkers;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import asUtilsPorts.Feeds;
import asUtilsPorts.Stations;
import asUtilsPorts.Cams.CamBeans;
import asUtilsPorts.Cams.CamSensors;
import asUtilsPorts.Cams.CamWorkerURL;
import asUtilsPorts.Feed.GetSPC;
import asUtilsPorts.Feed.RSSSources;
import asUtilsPorts.Feed.Reddit;
import asUtilsPorts.Feed.SnowReports;
import asUtilsPorts.Feed.Stocks;
import asUtilsPorts.Feed.cWazey;
import asUtilsPorts.Jobs.UbuntuVM.Crontabs_UVM;
import asUtilsPorts.Tests.TestStuff;
import asUtilsPorts.Weather.AlertMe;
import asUtilsPorts.Weather.RadarWorker;
import asUtilsPorts.Weather.SPCMapDownloader;

public class TestResource extends ServerResource {
    
    @Get
    public String represent() {
        
        JsonWorkers jw = new JsonWorkers();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        CommonBeans cb = new CommonBeans();
        
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        
        //snmpWalk [ -c commName -p portNum -v snmpVer] targetAddr oid
        String snmpArgs[] = {
            "-c", "public",
            "-p", "161",
            "-v", "3",
            "127.0.0.1",
            "1.3.6.1.4.1.7950.2.10.31.3"
        };
        
        String snmpTest1 = "";
        String snmpTest2 = "";
        
        try {
            SnmpWalk snmpWalk = new SnmpWalk();
            //snmpTest1 = snmpWalk.get("HOST-RESOURCES-MIB::hrSystemUptime.0");
            //snmpTest2 = snmpWalk.get("HOST-RESOURCES-MIB::hrSystemProcesses.0");
        } catch (IOException io) { io.printStackTrace(); }
        
        String test1 = getRequest().getRootRef().toString();
        String test2 = System.getProperty("catalina.base");
        
        String testData = "test1: " + test1 + "\n" +
                "test 2: " + test2 + "\n\n" +
                "snmpTest1: " + snmpTest1 + "\n" +
                "snmpTest2: " + snmpTest2 + "\n";
        
        List<String> qParams = new ArrayList<>();
        qParams.add(0, "2018-05-09%");
        testData += "\n" + jw.getDesiredDataType(
                getNewsFeedAction.getRedditFeeds(dbc, qParams),
                "jsonFromCsv",
                null
        );
        
        String testDate = "2018-05-13";
        LocalDate convertedDate = LocalDate.parse(testDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
        testData += "\nTest date end of month: " + convertedDate + "\n\n";
        
        testData += cb.getCatalinaHome();
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                    
        return testData;
        
    }    
       
    @Post
    public String doPost(Representation argsIn) {
        
        JsonWorkers jw = new JsonWorkers();
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        
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
	        		cWazey cw = new cWazey();
	        		cw.ripShit(dbc);
	        		break;
	        		            		
	        	case "_TEST":
	        		returnData = TestStuff.stupidTomcatSandboxing();
	        		break;

            	case "AlertMe":
            		AlertMe.doAlert();
            		break;
	        		
	        	case "AudioTest":
	        		MediaTools mTools = new MediaTools();
	        		String mediaPath = "/extra1/MediaServer/Games/U-Z/Unreal Tournament - Phantom.mp3";
                    if(wc.isSet(argsInForm.getFirstValue("file"))) { mediaPath = argsInForm.getFirstValue("file"); }
	        		mTools.doPlayMediaRemotely(mediaPath);
	        		break;	        		
            		
	        	case "CamTemp":
	        		CamSensors cSense = new CamSensors();
	        		cSense.logTemperature(dbc);
	        		break;
	        		
	        	case "CamURLTest":
	        		CamBeans camBeans = new CamBeans();
	        		CamWorkerURL cwURL = new CamWorkerURL();
	        		cwURL.doJob(dbc, camBeans.getCamPath().getPath());
	        		break;
	        		
                case "dojoDataStoreTest":
                    qParams.add(0, argsInForm.getFirstValue("searchDate"));
                    returnData = jw.getDesiredDataType(
                            getNewsFeedAction.getRedditFeeds(dbc, qParams),
                            "dataStore",
                            null
                    );
                    break;
	        		
	        	case "JodaHour":
	        		Feeds feeds = new Feeds();
	        		returnData += feeds.testHourOfDay();
	        		break;
	        		
	        	case "Map": 
	        		AlertMe alertMe = new AlertMe();
	        		returnData += alertMe.testAlertMap(dbc);
        			break;
	        		
	        	case "quartz":
	        		Crontabs_UVM cUVM = new Crontabs_UVM();
	        		cUVM.scheduler();
	        		break;
	        		
	        	case "RadarOverlay":
	        		RadarWorker rw = new RadarWorker();
	        		returnData += rw.opacityTest(dbc);
	        		break;

			 	case "Reddit":
	               	Reddit rd = new Reddit();
					returnData += rd.checkIfSent(dbc);
					break;

	        	case "Snow": 
	        		SnowReports sr = new SnowReports();
	        		returnData += sr.doSnow(dbc);
        			break;
        			
            	case "ThreadTest":
            		int testsToRun = 1;
                    testsToRun = Integer.parseInt(argsInForm.getFirstValue("count"));
            		ThreadRipper tr = new ThreadRipper();
            		returnData += tr.selfTest(testsToRun, false);
            		break;
            		
            	case "SPCMapsHistorical":
            		SPCMapDownloader spcMd = new SPCMapDownloader();
            		returnData += spcMd.goBackXdays("7750");
            		break;
	
	            case "Stations":
	            	Stations stations = new Stations();
	            	String wxTest = "Wunder";
                    if(wc.isSet(argsInForm.getFirstValue("action"))) { 
                    	wxTest = argsInForm.getFirstValue("action");
                    }
	            	returnData += stations.fetch(false, wxTest);
	            	break;
	            	
	            case "Stock":
	            	Stocks stocks = new Stocks();
	            	returnData += stocks.getStockQuote(dbc);
	            	break;

                case "Watch":
            		GetSPC getSPC = new GetSPC();
                	returnData += getSPC.checkSentWatch(dbc);
                	returnData += getSPC.checkSentOutlook(dbc);
                	break;
                	
				case "WaterOne":
					WaterOneHook w1h = new WaterOneHook();
					returnData = w1h.testPrefetch(dbc);
					break;
	            	
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }

}
