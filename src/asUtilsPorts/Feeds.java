/*
by Anthony Stump
Created: 14 Aug 2017
Reworked: 23 Nov 2019
Updated: 23 Nov 2019
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
    	anssQuakes.doAnssQuakes(dbc);
    	
        KCScout kcScout = new KCScout();
        returnData += kcScout.getScoutSQL(dbc);     
   
        NWSWarnings nwsWarnings = new NWSWarnings();
        nwsWarnings.doFetch(dbc);    
        
        GetSPC getSPC = new GetSPC();
        getSPC.doGetSPC(dbc);
        getSPC.doGetSPCb(dbc);
        
        return returnData;
        
            
    }
    
    public static String doHourly(Connection dbc) {
    	
    	String returnData = "Fetch hourly feeds:\n";
    	
    	RSSSources rssSources = new RSSSources();
    	rssSources.getRSS();
    	
    	GetSPC getSPC = new GetSPC();
    	getSPC.doGetSPCHourly(dbc);
    	
    	NHCFetch nhcFetch = new NHCFetch();
    	nhcFetch.getNHC(dbc);
    	
    	return returnData;
    	
    }
    
}