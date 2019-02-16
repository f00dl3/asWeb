/*
by Anthony Stump
Created: 15 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.WebAccessLogDAO;
import asWebRest.model.WebAccessLog;

public class UpdateWebAccessLogAction {

    private WebAccessLogDAO webAccessLogDAO;
    public UpdateWebAccessLogAction(WebAccessLogDAO webAccessLogDAO) { this.webAccessLogDAO = webAccessLogDAO; }
    public void updateWebAccessLog(WebAccessLog webAccessLog) { webAccessLogDAO.updateWebAccessLog(webAccessLog); }
    
}
