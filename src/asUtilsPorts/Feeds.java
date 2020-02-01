/*
by Anthony Stump
Created: 14 Aug 2017
Updated: 1 Feb 2020
*/

package asUtilsPorts;

import java.io.File;
import java.sql.Connection;

import org.joda.time.DateTime;

import asUtilsPorts.CamPusher;
import asUtilsPorts.Cams.CamBeans;
import asUtilsPorts.Cams.CamSensors;
import asUtilsPorts.Cams.CamWorkerURL;
import asUtilsPorts.Feed.ANSSQuakes;
import asUtilsPorts.Feed.GetSPC;
import asUtilsPorts.Feed.KCScout;
import asUtilsPorts.Feed.NHCFetch;
import asUtilsPorts.Feed.NWSWarnings;
import asUtilsPorts.SNMP.Router;
import asUtilsPorts.SNMP.UbuntuVM;
import asUtilsPorts.Weather.AlertMe;
//import asUtilsPorts.Feed.RSSSources;

public class Feeds {

    public static String do2Minute(Connection dbc) {

		String returnData = "Fetch 2 minute feeds:\n";

    	ANSSQuakes anssQuakes = new ANSSQuakes();
    	AlertMe alertMe = new AlertMe();
        CamBeans camBeans = new CamBeans();
    	CamPusher camPusher = new CamPusher();
    	CamWorkerURL cwURL = new CamWorkerURL();
        GetSPC getSPC = new GetSPC();
    	Mailer mailer = new Mailer();
        NWSWarnings nwsWarnings = new NWSWarnings();
        Router routerSnmp = new Router();
        UbuntuVM uvmSnmp = new UbuntuVM();
        
		final File camPath = camBeans.getCamPath();

		try { camPusher.pushIt(dbc); } catch (Exception e) { e.printStackTrace(); }
        try { cwURL.doJob(dbc, camPath.getPath()); } catch (Exception e) { e.printStackTrace(); }        
        try { mailer.mailForSQL(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { anssQuakes.doAnssQuakes(dbc); } catch (Exception e) { e.printStackTrace(); }     	
    	try { nwsWarnings.doFetch(dbc); } catch (Exception e) { e.printStackTrace(); }     
    	try { getSPC.doGetSPC(dbc); } catch (Exception e) { e.printStackTrace(); } 
    	try { getSPC.doGetSPCb(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { alertMe.earthquakeAlerts(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { alertMe.capAlerts(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { uvmSnmp.snmpUbuntuVM(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { routerSnmp.snmpRouter(dbc); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
            
    }
    
    public static String do5Minute(Connection dbc) {
    	
    	String returnData = "Fetch 5 minute feeds:\n";

        CamSensors camSensors = new CamSensors();
        KCScout kcScout = new KCScout();
        Radar radar = new Radar();
        Stations stations = new Stations();
        
    	try { radar.fetchRadars(); } catch (Exception e) { e.printStackTrace(); }
    	try { returnData += kcScout.getScoutSQL(dbc); } catch (Exception e) { e.printStackTrace(); }  
    	try { stations.fetch(false, "Wunder"); } catch (Exception e) { e.printStackTrace(); }
    	try { camSensors.logTemperature(dbc); } catch (Exception e) { e.printStackTrace(); }
    	
    	return returnData;
    	
    }
    
    public static String doHourly(Connection dbc) {
    	
    	DateTime rightNow = new DateTime();
    	String returnData = "Fetch hourly feeds:\n";
    	
    	//RSSSources rssSources = new RSSSources();
    	NHCFetch nhcFetch = new NHCFetch();
    	GetSPC getSPC = new GetSPC();
    	
    	try { getSPC.doGetSPCHourly(dbc); } catch (Exception e) { e.printStackTrace(); }    	
    	try { nhcFetch.getNHC(dbc); } catch (Exception e) { e.printStackTrace(); } 
    	//try { rssSources.getRSS(dbc); } catch (Exception e) { e.printStackTrace(); }     	
    	
    	if(rightNow.getHourOfDay() == 6) {
    		returnData += GetDaily.getDaily(dbc, 1);
    	}
    	
    	return returnData;
    	
    }
    
}
