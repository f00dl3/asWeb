/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 12 Feb 2020
 */

package asWebRest.action;

import asWebRest.dao.NewsFeedDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetNewsFeedAction {
    
    private NewsFeedDAO newsFeedDAO;
    public GetNewsFeedAction(NewsFeedDAO newsFeedDAO) { this.newsFeedDAO = newsFeedDAO; }
    
    public JSONArray getNewsFeed(Connection dbc) { return newsFeedDAO.getNewsFeeds(dbc); }
    public JSONArray getRedditFeeds(Connection dbc, List<String> qParams) { return newsFeedDAO.getRedditFeeds(dbc, qParams); }
    public JSONArray getRedditKcregionalwxInventory(Connection dbc) { return newsFeedDAO.getRedditKcregionalwxInventory(dbc); }
    public int getRedditFeedSent(Connection dbc, List<String> qParams) { return newsFeedDAO.getRedditFeedSent(dbc, qParams); }
    
}
