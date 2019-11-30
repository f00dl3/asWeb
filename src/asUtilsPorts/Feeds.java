/*
by Anthony Stump
Created: 14 Aug 2017
Reworked: 23 Nov 2019
Updated: 29 Nov 2019
*/

package asUtilsPorts;

import java.sql.Connection;

import asUtilsPorts.Feed.ANSSQuakes;
import asUtilsPorts.Feed.GetSPC;
import asUtilsPorts.Feed.KCScout;
import asUtilsPorts.Feed.NHCFetch;
import asUtilsPorts.Feed.NWSWarnings;
import asUtilsPorts.Feed.RSSSources;

public class Feeds {

    public static String do2Minute(Connection dbc) {

		String returnData = "Fetch 2 minute feeds:\n";

    	ANSSQuakes anssQuakes = new ANSSQuakes();
    	Mailer mailer = new Mailer();
        NWSWarnings nwsWarnings = new NWSWarnings();
        GetSPC getSPC = new GetSPC();
        KCScout kcScout = new KCScout();
        
        try { mailer.mailForSQL(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { anssQuakes.doAnssQuakes(dbc); } catch (Exception e) { e.printStackTrace(); }     	
    	try { returnData += kcScout.getScoutSQL(dbc); } catch (Exception e) { e.printStackTrace(); }  
    	try { nwsWarnings.doFetch(dbc); } catch (Exception e) { e.printStackTrace(); }     
    	try { getSPC.doGetSPC(dbc); } catch (Exception e) { e.printStackTrace(); } 
    	try { getSPC.doGetSPCb(dbc); } catch (Exception e) { e.printStackTrace(); } 
        
        return returnData;
            
    }
    
    public static String doHourly(Connection dbc) {
    	
    	String returnData = "Fetch hourly feeds:\n";
    	
    	RSSSources rssSources = new RSSSources();
    	NHCFetch nhcFetch = new NHCFetch();
    	GetSPC getSPC = new GetSPC();
    	
    	try { getSPC.doGetSPCHourly(dbc); } catch (Exception e) { e.printStackTrace(); }    	
    	try { nhcFetch.getNHC(dbc); } catch (Exception e) { e.printStackTrace(); } 
    	//try { rssSources.getRSS(dbc); } catch (Exception e) { e.printStackTrace(); }     	
    	
    	return returnData;
    	
    }
    
}