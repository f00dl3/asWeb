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
import asUtilsPorts.Feed.NWSWarnings;

public class Feeds {

    public static String do2Minute(Connection dbc) {

		String returnData = "";

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
    	
    	String returnData = "";
    	return returnData;
    	
    }
    
}