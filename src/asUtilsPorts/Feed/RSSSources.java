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
import java.sql.Statement;

import asUtils.Shares.MyDBConnector;
import asUtils.Shares.StumpJunk;
import asWebRest.shared.CommonBeans;

public class RSSSources {
    
    public static void getRSS() {

        CommonBeans cb = new CommonBeans();

        final String ramTemp = cb.getPathChartCache().toString();
        final String mysqlShare = ramTemp;
        
        final String getFeedsSQL = "SELECT Name, LinkURL FROM Feeds.RSSSources WHERE Frequency='H' AND Active=1 AND Reddit=0;";

		try (
		Connection conn = MyDBConnector.getMyConnection();
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery(getFeedsSQL);
		) {		
			while (resultSet.next()) {
				
				String thisLinkName = resultSet.getString("Name");
				String thisLinkURL = resultSet.getString("LinkURL");
				String thisFeedFileStr = "NewsFeed"+thisLinkName+".xml";
				File thisFeedFile = new File(ramTemp+"/"+thisFeedFileStr);
				File thisFeedDestFile = new File(mysqlShare+"/"+thisFeedFileStr);
			
				System.out.println("Fetching: "+thisLinkName+" ("+thisLinkURL+")");
				StumpJunk.jsoupOutFile(thisLinkURL, thisFeedFile);
				StumpJunk.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "<!\\[CDATA\\[", "");
				StumpJunk.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "\\]\\]", "");
				StumpJunk.moveFile(ramTemp+"/"+thisFeedFileStr, mysqlShare+"/"+thisFeedFileStr);
			
				String thisFeedUpSQL = "LOAD XML LOCAL INFILE '"+mysqlShare+"/"+thisFeedFileStr+"' IGNORE INTO TABLE Feeds.RSSFeeds CHARACTER SET 'utf8' ROWS IDENTIFIED BY '<item>';";
				try ( Statement subStmt = conn.createStatement();) { subStmt.executeUpdate(thisFeedUpSQL); } catch (Exception e) { e.printStackTrace(); }
			
				thisFeedFile.delete();
				thisFeedDestFile.delete();
			}
			
		} catch (Exception e) { e.printStackTrace(); }
		
		
    }
    
}