/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 30 Nov 2019
*/

package asWebRest.dao;

import java.sql.ResultSet;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsFeedDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public JSONArray getNewsFeeds(Connection dbc) {
        final String query_NewsFeeds = "SELECT Source, title, link, description, pubDate, GetTime FROM (" +
        " SELECT 'RSS' as Source, title, link, description, pubDate, GetTime FROM Feeds.RSSFeeds WHERE link LIKE '%kshb%' OR link LIKE '%kmbc%' UNION ALL" +
        " SELECT 'Gmail' as Source, Subject as title, NULL as link, Body as description, CONCAT(GetTime, ' CT') as pubDate, GetTime FROM Feeds.Messages ) as tmp" +
        " ORDER BY GetTime DESC LIMIT 25;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_NewsFeeds, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Source", resultSet.getString("Source"))
                    .put("title", resultSet.getString("title"))
                    .put("link", resultSet.getString("link"))
                    .put("description", wc.htmlStripTease(resultSet.getString("description")))
                    .put("pubDate", resultSet.getString("pubDate"))
                    .put("GetTime", resultSet.getString("GetTime"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public JSONArray getRecentEmail(Connection dbc) {
    	List<String> tParams = new ArrayList<String>();
    	final DateTime dtNow = new DateTime();
    	final DateTime dtPrevious = new DateTime().minusMinutes(15);
    	final DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY-mm-dd HH:mm");
    	final String sTimeNow = dtf.print(dtNow);
    	final String sTimePrev = dtf.print(dtPrevious);    	
    	tParams.add(0, sTimePrev);
    	tParams.add(1, sTimeNow);
    	final String query_RecentEmail = "SELECT " +
	        " MessageID, Received, FromAddress, Subject, Body, GetTime, ActionTaken" +
	        " FROM Feeds.Messages WHERE GetTime BETWEEN ? AND ? AND ActionTaken=0 ORDER BY GetTime";
	        JSONArray tContainer = new JSONArray();
	        try {
	            ResultSet resultSet = wc.q2rs1c(dbc, query_RecentEmail, tParams);
	            while (resultSet.next()) {
	                JSONObject tObject = new JSONObject();
	                tObject
	                    .put("MessageID", resultSet.getString("MessageID"))
	                    .put("Received", resultSet.getString("Received"))
	                    .put("FromAddress", resultSet.getString("FromAddress"))
	                    .put("Subject", resultSet.getString("Subject"))
	                    .put("Body", resultSet.getString("Body"))
	                    .put("GetTime", resultSet.getString("GetTime"))
	                    .put("ActionTaken", resultSet.getInt("ActionTaken"));
	                tContainer.put(tObject);
	            }
	            resultSet.close();
	        } catch (Exception e) { e.printStackTrace(); }
	        return tContainer;    	
    }

    public JSONArray getRedditFeeds(Connection dbc, List<String> qParams) {
        final String query_RedditFeeds = "SELECT id, 'Reddit' as Source, title, link, content, GetTime" +
                " FROM Feeds.RedditFeeds WHERE GetTime LIKE ? ORDER BY GetTime DESC LIMIT 255;"; // RedditDate
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_RedditFeeds, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("id", resultSet.getString("id"))
                    .put("Source", resultSet.getString("Source"))
                    .put("title", resultSet.getString("title"))
                    .put("link", resultSet.getString("link"))
                    .put("content", resultSet.getString("content"))
                    .put("GetTime", resultSet.getString("GetTime"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public String setMessageActionTaken(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_MessageActionTaken = "UPDATE Feeds.Messages SET ActionTaken=1 WHERE MessageID=?;";
        try { returnData = wc.q2do1c(dbc, query_MessageActionTaken, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

}
