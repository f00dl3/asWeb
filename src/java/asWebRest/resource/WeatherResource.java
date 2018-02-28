/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 27 Feb 2018
 */

package asWebRest.resource;

import asWebRest.action.GetWeatherAction;
import asWebRest.dao.WeatherDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class WeatherResource extends ServerResource {
    
    @Get
    public String represent() {
        
        List<String> inParams = new ArrayList<>();
        /* inParams.add(0, "2017-12-23 10");
        inParams.add(1, "2018-01-23 15");
        inParams.add(2, "250"); */
        
        List<String> qParams = new ArrayList<>();
        qParams.add(0, "2018-01-01%");
        qParams.add(1, "2018-01-02");
        
        GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
        JSONArray tData = getWeatherAction.getStormReportsByDate(qParams); 
        return tData.toString();
        
    }
    
}
