/*
by Anthony Stump
Created: 22 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetNewsFeedAction;
import asWebRest.dao.NewsFeedDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class NewsFeedResource extends ServerResource {
    
    @Get
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        JSONArray newsFeeds = getNewsFeedAction.getNewsFeed(dbc);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return newsFeeds.toString();
        
    }
    
}
