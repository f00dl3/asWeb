/*
by Anthony Stump
Created: 14 Aug 2017
Split to RSSSources: 23 Nov 2019
Updated: 8 Feb 2020
*/

package asUtilsPorts.Feed;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class RSSSources {
    
    public void getRSS(Connection dbc) {

    	String returnData = "";
    	
    	MyDBConnector mdb = new MyDBConnector();
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
				String thisFeedFileStr = "NewsFeed"+thisLinkName+".xml";
				tObject
	                .put("thisLinkName", thisLinkName)
	                .put("thisLinkURL", resultSet.getString("LinkURL"))
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
			File thisFeedFile = new File(ramTemp+"/"+thisFeedFileStr);
			wc.jsoupOutFile(thisLinkURL, thisFeedFile);
			wc.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "<!\\[CDATA\\[", "");
			wc.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "\\]\\]", "");
			String thisFeedUpSQL = "LOAD XML LOCAL INFILE '"+ramTemp+"/"+thisFeedFileStr+"' IGNORE INTO TABLE Feeds.RSSFeeds CHARACTER SET 'utf8' ROWS IDENTIFIED BY '<item>';";
			try ( 
					Connection subConn = mdb.getMyConnection();
					Statement subStmt = subConn.createStatement();
				) { 
					System.out.println("Loading infile for: " +thisLinkName+" ("+thisLinkURL+")");
					subStmt.executeUpdate(thisFeedUpSQL);
					subConn.close();
				} catch (Exception e) { 
					e.printStackTrace();
			}
			thisFeedFile.delete();
			try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }
        }
             
    }
    
}