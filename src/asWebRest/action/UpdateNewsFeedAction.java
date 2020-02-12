/*
by Anthony Stump
Created: 12 Feb 2020
Updated: on creation
 */

package asWebRest.action;

import asWebRest.dao.NewsFeedDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateNewsFeedAction {
    
    private NewsFeedDAO newsFeedDAO;
    public UpdateNewsFeedAction(NewsFeedDAO newsFeedDAO) { this.newsFeedDAO = newsFeedDAO; }
    
    public String setRedditSentNotice(Connection dbc, List<String> qParams) { return newsFeedDAO.setRedditSentNotice(dbc, qParams); }
        
}
