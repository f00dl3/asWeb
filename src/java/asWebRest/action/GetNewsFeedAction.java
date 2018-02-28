/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 22 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.NewsFeedDAO;
import java.util.List;
import org.json.JSONArray;

public class GetNewsFeedAction {
    
    private NewsFeedDAO newsFeedDAO;
    public GetNewsFeedAction(NewsFeedDAO newsFeedDAO) { this.newsFeedDAO = newsFeedDAO; }
    
    public JSONArray getNewsFeed() { return newsFeedDAO.getNewsFeeds(); }
    public JSONArray getRedditFeeds(List qParams) { return newsFeedDAO.getRedditFeeds(qParams); }
    
}
