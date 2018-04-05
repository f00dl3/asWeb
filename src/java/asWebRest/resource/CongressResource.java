/*
by Anthony Stump
Created: 5 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetCongressAction;
import asWebRest.dao.CongressDAO;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
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
    
}