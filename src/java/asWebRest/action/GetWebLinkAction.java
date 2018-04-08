/*
by Anthony Stump
Created: 21 Feb 2018
Updated: 8 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.WebLinkDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetWebLinkAction {
    
    private WebLinkDAO webLinkDAO;
    public GetWebLinkAction(WebLinkDAO webLinkDAO) { this.webLinkDAO = webLinkDAO; }
    
    public JSONArray getWebLinks(Connection dbc, List qParams) { return webLinkDAO.getWebLinks(dbc, qParams); }
    
}
