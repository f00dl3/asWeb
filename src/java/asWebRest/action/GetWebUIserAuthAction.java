/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 13 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.WebUIserAuthDAO;

public class GetWebUIserAuthAction {
    
    private WebUIserAuthDAO webUIserAuthDAO;
    public GetWebUIserAuthAction(WebUIserAuthDAO webUIserAuthDAO) { this.webUIserAuthDAO = webUIserAuthDAO; }
    public String getWebUIserAuth(String user) { return webUIserAuthDAO.getWebUIserAuth(user); }
    
}
