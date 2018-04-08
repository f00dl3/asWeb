/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 7 Apr 2018
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsFeedDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getNewsFeeds() {
        final String query_NewsFeeds = "SELECT Source, title, link, description, pubDate, GetTime FROM (" +
        " SELECT 'RSS' as Source, title, link, description, pubDate, GetTime FROM Feeds.RSSFeeds WHERE link LIKE '%kshb%' OR link LIKE '%kmbc%' UNION ALL" +
        " SELECT 'Gmail' as Source, Subject as title, NULL as link, Body as description, CONCAT(GetTime, ' CT') as pubDate, GetTime FROM Feeds.Messages ) as tmp" +
        " ORDER BY GetTime DESC LIMIT 25;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_NewsFeeds, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Source", resultSet.getString("Source"))
                    .put("title", resultSet.getString("title"))
                    .put("link", resultSet.getString("link"))
                    .put("description", resultSet.getString("description"))
                    .put("pubDate", resultSet.getString("pubDate"))
                    .put("GetTime", resultSet.getString("GetTime"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getRedditFeeds(List<String> qParams) {
        final String query_RedditFeeds = "SELECT 'Reddit' as Source, title, link, content, GetTime" +
                " FROM Feeds.RedditFeeds WHERE GetTime LIKE ? ORDER BY GetTime DESC LIMIT 255;"; // RedditDate
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_RedditFeeds, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
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
    
    
}
