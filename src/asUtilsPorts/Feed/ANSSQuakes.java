/*
by Anthony Stump
Split off from Feeds.java 3 May 2019
Updated: 23 Nov 2019
*/

package asUtilsPorts.Feed;

import java.io.File;
import java.sql.Connection;

import asUtils.Shares.StumpJunk;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class ANSSQuakes {

    public static void doAnssQuakes(Connection dbc) {

			CommonBeans cb = new CommonBeans();
    		WebCommon wc = new WebCommon();
    		StumpJunk sj = new StumpJunk();

            final String eqFeedURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.csv";
            final File eqcsvFile = new File(cb.getPathChartCache().toString()+"/ANSSQuakes.csv");
            
            sj.jsoupOutFile(eqFeedURL, eqcsvFile);

            final String infileQuery = "LOAD DATA LOCAL INFILE '"+eqcsvFile.toString()+"'" + 
                    " INTO TABLE WxObs.ANSSQuakes" +
                    " FIELDS TERMINATED BY ','" + 
                    " ENCLOSED BY '\"'" +
                    " LINES TERMINATED BY '\\n';";

            String cleanQuakeSQL = "DELETE FROM WxObs.ANSSQuakes WHERE time='time';";
            
            try { wc.q2do1c(dbc, infileQuery, null); } catch (Exception e) { e.printStackTrace(); }
            try { wc.q2do1c(dbc, cleanQuakeSQL, null); } catch (Exception e) { e.printStackTrace(); }
			
            eqcsvFile.delete();
            
    }
    
}