/*
by Anthony Stump
Created: 5 Apr 2018
Updated: 7 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetCongressAction;
import asWebRest.dao.CongressDAO;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class CongressResource extends ServerResource {
    
    @Get @Options
    public String represent() {
        
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        File testJsonOutFile = new File(cb.getPathChartCache() + "/congressOut.json");
        GetCongressAction getCongressAction = new GetCongressAction(new CongressDAO());
        JSONArray hack = getCongressAction.getCongressHack();  
        try { wc.varToFile(hack.toString(),testJsonOutFile,false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        return hack.toString();
        
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        GetCongressAction getCongressAction = new GetCongressAction(new CongressDAO());
                        
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                
                case "getCongress":
                    JSONArray hack = getCongressAction.getCongressHack();  
                    returnData += hack.toString();
                    break;
                    
            }
        }
        
        return returnData;
    
    }
    
}
