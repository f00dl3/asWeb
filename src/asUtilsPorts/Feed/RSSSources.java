/*
by Anthony Stump
Created: 14 Aug 2017
Split to RSSSources: 23 Nov 2019
Updated: 23 Nov 2019
*/

package asUtilsPorts.Feed;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import asUtils.Shares.StumpJunk;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class RSSSources {
    
    public static void getRSS(Connection dbc) {

    	String returnData = "";
    	
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();

        final String ramTemp = cb.getPathChartCache().toString();
        
        final String getFeedsSQL = "SELECT Name, LinkURL FROM Feeds.RSSSources WHERE Frequency='H' AND Active=1 AND Reddit=0;";
        
        JSONArray tContainer = new JSONArray();
        
        try {
        	ResultSet resultSet = wc.q2rs1c(dbc, getFeedsSQL, null);
        	while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
            	String thisLinkName = resultSet.getString("Name");
				String thisLinkURL = resultSet.getString("LinkURL");
				String thisFeedFileStr = "NewsFeed"+thisLinkName+".xml";
				tObject
	                .put("thisLinkName", thisLinkName)
	                .put("thisLinkURL", thisLinkURL)
	                .put("thisFeedFileStr", thisFeedFileStr);               
                tContainer.put(tObject);
        	}
        	resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        
        for(int i = 0; i < tContainer.length(); i++) {
        	JSONObject tjo = tContainer.getJSONObject(i);
        	String thisLinkName = tjo.getString("thisLinkName");
			String thisLinkURL = tjo.getString("thisLinkURL");
			String thisFeedFileStr =  tjo.getString("thisFeedFileStr");
			System.out.println("Fetching: "+thisLinkName+" ("+thisLinkURL+")");
			File thisFeedFile = new File(ramTemp+"/"+thisFeedFileStr);
			StumpJunk.jsoupOutFile(thisLinkURL, thisFeedFile);
			StumpJunk.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "<!\\[CDATA\\[", "");
			StumpJunk.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "\\]\\]", "");
			String thisFeedUpSQL = "LOAD XML LOCAL INFILE '"+ramTemp+"/"+thisFeedFileStr+"' IGNORE INTO TABLE Feeds.RSSFeeds CHARACTER SET 'utf8' ROWS IDENTIFIED BY '<item>';";
			try { returnData += wc.q2do1c(dbc, thisFeedUpSQL, null); } catch (Exception e) { e.printStackTrace(); }
			thisFeedFile.delete();
        	
        }
             
    }
    
}