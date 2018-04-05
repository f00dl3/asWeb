/*
by Anthony Stump
Created: 4 Apr 2018
*/

package asWebRest.action;

import asWebRest.dao.HomeDAO;
import java.util.List;
import org.json.JSONArray;

public class GetHomeAction {
    
    private HomeDAO homeDAO;
    public GetHomeAction(HomeDAO homeDAO) { this.homeDAO = homeDAO; }
    
    public JSONArray getMeasure(List qParams) { return homeDAO.getMeasure(qParams); }
    
}