/*
by Anthony Stump
Created: 14 Aug 2017
Split to UVM: 16 Oct 2019
Updated: 31 Jan 2020
*/

package asUtilsPorts.UbuntuVM;

import java.io.*;
import java.sql.*;
import asWebRest.shared.MyDBConnector;
import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;
import asWebRest.shared.WebCommon;

public class Feeds {

	public static void main(String[] args) {

                JunkyBeans junkyBeans = new JunkyBeans();
		MyDBConnector mdb = new MyDBConnector();
		WebCommon wc = new WebCommon();
                
		final File ramDrive = junkyBeans.getRamDrive();
		final String mysqlShare = junkyBeans.getMySqlShare().toString();
		final String ramTemp = ramDrive.getPath()+"/rssXMLFeedsJ";
		final File ramTempF = new File(ramTemp);

		String freq = args[0];

		ramTempF.mkdirs();

		if (freq.equals("TwoMinute")) {
			
                        try {
                            System.out.println("Executing calls to asWeb API for 2 minute interval fetches:");
                            	System.out.println("DEBUG: " + junkyBeans.getApi());
				SSLHelper.getConnection(junkyBeans.getApi())
                                    .data("doWhat", "Feeds")
                                    .data("interval", "2m")
                                    .post();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
			
		}
		
		if (freq.equals("Hour")) {
			
                        final String getFeedsSQL = "SELECT Name, LinkURL FROM Feeds.RSSSources WHERE Frequency='H' AND Active=1 AND Reddit=0;";
			
			try (
				Connection conn = mdb.getMyConnection();
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
					wc.jsoupOutFile(thisLinkURL, thisFeedFile);
					wc.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "<!\\[CDATA\\[", "");
					wc.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "\\]\\]", "");
					wc.moveFile(ramTemp+"/"+thisFeedFileStr, mysqlShare+"/"+thisFeedFileStr);

					String thisFeedUpSQL = "LOAD XML LOCAL INFILE '"+mysqlShare+"/"+thisFeedFileStr+"' IGNORE INTO TABLE Feeds.RSSFeeds CHARACTER SET 'utf8' ROWS IDENTIFIED BY '<item>';";
					try ( Statement subStmt = conn.createStatement();) { subStmt.executeUpdate(thisFeedUpSQL); } catch (Exception e) { e.printStackTrace(); }

					thisFeedFile.delete();
					thisFeedDestFile.delete();
				}
			}
			catch (Exception e) { e.printStackTrace(); }
			
                        try {
                            System.out.println("Executing calls to asWeb API for 1 hour interval fetches:");
                            SSLHelper.getConnection(junkyBeans.getApi())
                                    .data("doWhat", "Feeds")
                                    .data("interval", "1h")
                                    .post();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       
                }

	}

}
