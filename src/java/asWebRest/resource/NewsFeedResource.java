/*
by Anthony Stump
Created: 18 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetNewsFeedAction;
import asWebRest.dao.NewsFeedDAO;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class NewsFeedResource extends ServerResource {
    
    @Get
    public String represent() {
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        JSONArray newsFeeds = getNewsFeedAction.getNewsFeed();  
        return newsFeeds.toString();
    }
    
}
