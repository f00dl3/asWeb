/*
by Anthony Stump
Created 7 Sep 2017
Updated: 11 Jun 2020
*/

package asUtilsPorts.Feed;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.action.GetNewsFeedAction;
import asWebRest.action.UpdateNewsFeedAction;
import asWebRest.dao.NewsFeedDAO;
import asWebRest.hookers.WeatherBot;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.util.Scanner;

public class Reddit {

	public void actualClass() {

		JunkyBeans junkyBeans = new JunkyBeans();
		MyDBConnector mdb = new MyDBConnector();
		WebCommon wc = new WebCommon();

		final String ramTemp = junkyBeans.getRamDrive().toString()+"/rssXMLFeedsJ";
		final String mysqlShare = junkyBeans.getMySqlShare().toString();
		final File ramTempF = new File(ramTemp);
		DateFormat sqlCompareFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		DateFormat hackFormat = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		String sqlCompare = sqlCompareFormat.format(date);
		String hackTime = hackFormat.format(date);
		
		if(!ramTempF.exists()) { ramTempF.mkdirs(); }

		final String getRedditSQL = "SELECT Name, LinkURL FROM Feeds.RSSSources WHERE Frequency='H' AND Active=1 AND Reddit=1;";
		final String getImageLinksSQL = "SELECT id, GetTime, Content FROM Feeds.RedditFeeds WHERE GetTime LIKE '"+sqlCompare+"%';";
			
		try (
			Connection connR = mdb.getMyConnection();
			Statement stmtR = connR.createStatement();
			ResultSet resultSetR = stmtR.executeQuery(getRedditSQL);
		) {		
			while (resultSetR.next()) {
					
				String thisLinkName = resultSetR.getString("Name");
				String thisLinkURL = resultSetR.getString("LinkURL");
				String thisFeedFileStr = "Reddit"+thisLinkName+".rss";
				File thisFeedFile = new File(ramTemp+"/"+thisFeedFileStr);
				File thisFeedDestFile = new File(mysqlShare+"/"+thisFeedFileStr);
					
				System.out.println("Fetching: "+thisLinkName+" ("+thisLinkURL+")");
				wc.jsoupOutBinary(thisLinkURL, thisFeedFile, 15.0);
				wc.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "<!\\[CDATA\\[", "");
				wc.sedFileReplace(ramTemp+"/"+thisFeedFileStr, "\\]\\]", "");
				wc.runProcess("iconv -f ISO-8859-1 -t UTF-8 '"+thisFeedFile.toString()+"' > '"+thisFeedFile.toString()+"'");
				wc.moveFile(thisFeedFile.toString(), thisFeedDestFile.toString());	
				
				String thisRedditUpSQL = "LOAD XML LOCAL INFILE '"+mysqlShare+"/"+thisFeedFileStr+"' IGNORE INTO TABLE Feeds.RedditFeeds CHARACTER SET 'utf8' ROWS IDENTIFIED BY '<entry>';";
				try ( Statement subStmtR = connR.createStatement();) { subStmtR.executeUpdate(thisRedditUpSQL); }
					
				thisFeedFile.delete();
				thisFeedDestFile.delete();

			}
		} catch (Exception e) { e.printStackTrace(); }
			
		try (
			Connection connR = mdb.getMyConnection();
			Statement stmtR = connR.createStatement();
			ResultSet resultSetR = stmtR.executeQuery(getImageLinksSQL);
		) {		
			while (resultSetR.next()) {
				String thumbURL = null;
				String fullURL = null;
				String keyLookup = resultSetR.getString("id");
				System.out.println("DEBUG: found a match on " + keyLookup);
				String line = resultSetR.getString("content");
				if(line.contains("<img src=")) { Pattern p = Pattern.compile("<img src=\\\"(.*)\\\" alt"); Matcher m = p.matcher(line); if (m.find()) { thumbURL = m.group(1); } }
				if(line.contains("[link]")) { Pattern p = Pattern.compile("<span><a href=\\\"(.*)\\\">\\[link\\]"); Matcher m = p.matcher(line); if (m.find()) { fullURL = m.group(1); } }
				if (thumbURL != null && thumbURL.toLowerCase().contains("jpg")) {
					File thisTmpFolder = new File(ramTemp+"/Reddit");
					File thisThumbFile = new File(ramTemp+"/Reddit/"+keyLookup+"#"+thumbURL.substring(thumbURL.lastIndexOf("/")+1));
					thisTmpFolder.mkdirs();
					File thisFullFile = new File(ramTemp+"/Reddit/"+keyLookup+"#"+fullURL.substring(fullURL.lastIndexOf("/")+1));
					if (fullURL != null && (fullURL.toLowerCase().contains("jpg") || fullURL.toLowerCase().contains("png"))) {
						wc.jsoupOutBinary(fullURL, thisFullFile, 15.0);
					} else { 
						wc.jsoupOutBinary(fullURL, thisFullFile, 15.0);
						wc.jsoupOutBinary(thumbURL, thisThumbFile, 15.0);
						String nextDown = null;
						Scanner subScanner = null; try {		
							subScanner = new Scanner(thisFullFile);
							while(subScanner.hasNext()) {				
								String subLine = subScanner.nextLine();
								if(subLine.contains("secure_url")) {
									Pattern p = Pattern.compile("content=\\\"(.*)\\\""); Matcher m = p.matcher(subLine); if (m.find()) { nextDown = m.group(1); }
									System.out.println(nextDown);
									File subFile = new File(ramTemp+"/Reddit/"+keyLookup+"#"+nextDown.substring(nextDown.lastIndexOf("/")+1));
									wc.jsoupOutBinary(nextDown, subFile, 15.0);
									thisThumbFile.delete();
								}
								if(subLine.contains("mp4Url")) {
									nextDown = "https://giant.gfycat.com/"+fullURL.substring(fullURL.lastIndexOf("/")+1)+".mp4";
									System.out.println(nextDown);
									File subFile = new File(ramTemp+"/Reddit/"+keyLookup+"#"+nextDown.substring(nextDown.lastIndexOf("/")+1));
									wc.jsoupOutBinary(nextDown, subFile, 15.0);
									thisThumbFile.delete();
								}
							}
						} catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
						thisFullFile.delete();
					}
				} else {
					File thisFullFile = new File(ramTemp+"/Reddit/"+keyLookup+"#"+fullURL.substring(fullURL.lastIndexOf("/")+1));
					wc.jsoupOutBinary(fullURL, thisFullFile, 15.0);
					String nextDown = null;
					Scanner subScanner = null; try {		
						subScanner = new Scanner(thisFullFile);
						while(subScanner.hasNext()) {
							String subLine = subScanner.nextLine();
							if(subLine.contains("m4a: ")) {
								Pattern p = Pattern.compile("m4a: \\\"(.*)\\\""); Matcher m = p.matcher(subLine); if (m.find()) { nextDown = m.group(1); }
								System.out.println(nextDown);
								File subFile = new File(ramTemp+"/Reddit/"+keyLookup+"#"+nextDown.substring(nextDown.lastIndexOf("/")+1));
								wc.jsoupOutBinary(nextDown, subFile, 15.0);
								thisFullFile.delete();
							}
						}
					} catch (FileNotFoundException fnf) { fnf.printStackTrace(); }	
				}
			}
		
		} catch (Exception e) { e.printStackTrace(); }

		File redditFolder = new File(ramTemp+"/Reddit");
		if (redditFolder.isDirectory()) {
			File webOutFolder = new File(junkyBeans.getWebRoot().toString()+"/rOut");
                        File outZip = new File(webOutFolder+"/Reddit#"+hackTime+".zip");
			webOutFolder.mkdirs();
			wc.zipThisFolder(redditFolder, outZip);
			wc.deleteDir(redditFolder);
		}
	
	}
	
	public String checkIfSent(Connection dbc) {
		
		String returnData = "";
		
		GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
		UpdateNewsFeedAction updateNewsFeedAction = new UpdateNewsFeedAction(new NewsFeedDAO());		
		WeatherBot wxBot = new WeatherBot();
		WebCommon wc = new WebCommon();
		
		JSONArray thisFeedStore = new JSONArray();
		
		try { 
			
			thisFeedStore = getNewsFeedAction.getRedditKcregionalwxInventory(dbc);
			
			for(int i = 0; i < thisFeedStore.length(); i++) {
				
				JSONObject tFeed = thisFeedStore.getJSONObject(i);
				String id = tFeed.getString("id");
				String title = wc.basicInputFilter(tFeed.getString("title").replaceAll("\'",""));
				
				List<String> qParams = new ArrayList<>();
				qParams.add(id);
				
				returnData += "\nDEBUG: Checking if we sent notification on " + id + " (" + title + ")";
				
				int wasItSent = getNewsFeedAction.getRedditFeedSent(dbc, qParams);
				if(wasItSent == 1) {
					returnData += "\nFEED " + id + " WAS ALREADY SENT!";
				} else {
					returnData += "\nFEED " + id + " HAS NOT BEEN SENT YET!";
					String messageToSend = "Someone be typin on Reddit! An article titled \"" + title +
							"\" was just published at https://reddit.com/r/kcregionalwx !";
					try {
						wxBot.botBroadcastOnly(messageToSend);
						updateNewsFeedAction.setRedditSentNotice(dbc, qParams); 
					} catch (Exception e) { 
						e.printStackTrace(); 
					}
				}
			}
			
		} catch (Exception e) { e.printStackTrace(); }
		
		return returnData;
		
	}		
	
	public static void main(String[] args) {
		Reddit reddit = new Reddit();
		reddit.actualClass();
	}

}
