/*
by Anthony Stump
Created: 22 Nov 2019
Updated: on Creation

POST REQUEST VIA COMMAND LINE ala 
	wget --no-check-certificate --post-data 'doWhat=getFfxivMerged' https://localhost:8444/asWeb/r/FFXIV

 */

package asWebRest.resource;

import asWebRest.hookers.CgwRipper;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import asUtilsPorts.KCScout;
import asUtilsPorts.MHPFetch;
import asUtilsPorts.NWSWarnings;

public class BackendResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
                        
        JSONObject mergedResults = new JSONObject();
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String[] defaultArgs = {};
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
            
	            case "KCScout":
	                KCScout kcScout = new KCScout();
	                returnData = kcScout.getScoutSQL();                  
	                break;
            
	            case "MHPFetch":
	                MHPFetch mhpFetch = new MHPFetch();
	                mhpFetch.main(defaultArgs);                    
	                break;
                 
                case "NWSWarnings":
                    NWSWarnings nwsWarnings = new NWSWarnings();
                    nwsWarnings.main(defaultArgs);                    
                    break;
                    
            }
            
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
        
}
