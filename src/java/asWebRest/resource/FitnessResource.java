/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 22 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.dao.FitnessDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class FitnessResource extends ServerResource {
    
    @Get
    public String represent() {
        List<String> qParams = new ArrayList<>();
        qParams.add("2018-02-21");
        qParams.add("2018-02-22");
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        JSONArray allRecs = getFitnessAction.getAll(qParams);  
        return allRecs.toString();
    }
    
}
