/*
Radar Lister Java Class
Migrated from MakeMP4.sh
Created: 29 Apr 2019
Updated: 28 Dec 2019
 */

package asUtilsPorts.Weather;

import asWebRest.shared.WebCommon;

import java.sql.Connection;
import java.sql.ResultSet;

public class RadarList {
    
    public static String listSites(Connection dbc) {
    	
    	WebCommon wc = new WebCommon();
    	
    	String returnData = "";            	
        final String queryRadarList = "SELECT Site FROM WxObs.RadarList WHERE Active=1 ORDER BY Site ASC;";

        try {
        	ResultSet resultSet = wc.q2rs1c(dbc, queryRadarList, null);
            while (resultSet.next()) {
                returnData += resultSet.getString("Site") + "\n";
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return returnData;
     
    }
    
}
