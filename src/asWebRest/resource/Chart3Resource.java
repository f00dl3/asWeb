/*
by Anthony Stump
Created: 7 Oct 2020
Updated: 7 Oct 2020
 */

package asWebRest.resource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

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
            
        		case "TestData":
        			returnData = c3h.getTestJson(dbc).toString();
        			break;
                      
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    
    }
    
}