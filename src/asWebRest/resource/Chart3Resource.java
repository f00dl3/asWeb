/*
by Anthony Stump
Created: 7 Oct 2020
Updated: 18 Nov 2020
 */

package asWebRest.resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import asWebRest.action.GetFinanceAction;
import asWebRest.chartHelpers.Finance;
import asWebRest.dao.FinanceDAO;
import asWebRest.hookers.Chart3Helpers;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class Chart3Resource extends ServerResource {
    
    @Post    
    public String doPost(Representation argsIn) {

        WebCommon wc = new WebCommon();
        Chart3Helpers c3h = new Chart3Helpers();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        String doWhat = null;
        String returnData = "";      
        List<String> inParams = new ArrayList<>();              
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
        	
            switch (doWhat) {    
            
    		 	case "rapidWorth": case "testData": default:
    				Finance fin = new Finance();
    				GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
                    JSONArray enw_RawR = getFinanceAction.getEnwChartRapid(dbc);
        			returnData = fin.getFinEnw(enw_RawR, "All", "R").toString();
        			break;
                      
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    
    }
    
}