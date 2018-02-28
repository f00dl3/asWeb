/*
by Anthony Stump
Created: 18 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.CookingDAO;
import org.json.JSONArray;

public class GetCookingAction {
    
    private CookingDAO cookingDAO;
    public GetCookingAction(CookingDAO cookingDAO) { this.cookingDAO = cookingDAO; }
    
    public JSONArray getRecipies() { return cookingDAO.getRecipies(); }
    
}
