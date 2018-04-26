/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 26 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.dao.FitnessDAO;
import asWebRest.hookers.DynChartX;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ChartResource extends ServerResource {
    
    @Post    
    public String doPost(Representation argsIn) {

        DynChartX dynChart = new DynChartX();

        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        
        String doWhat = null;
        String returnData = "";      
        
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        JSONObject jsonProps = new JSONObject();
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch (doWhat) {
                
                case "WeightRange":
                    qParams.add(argsInForm.getFirstValue("XDT1"));
                    qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jsonResultArray = getFitnessAction.getChWeightR(qParams);
                    JSONArray jsonLabelArray = new JSONArray();
                    JSONArray jsonDataArray = new JSONArray();
                    for (int i = 0; i < jsonResultArray.length(); i++) {
                        JSONObject thisObject = jsonResultArray.getJSONObject(i);
                        jsonLabelArray.put(thisObject.getString("Date"));
                        jsonDataArray.put(thisObject.getDouble("Weight"));
                    }
                    String fullChartName = "Weight Range: " + argsInForm.getFirstValue("XDT1") + " to " + argsInForm.getFirstValue("XDT2");
                    jsonProps
                        .put("chartName", fullChartName)
                        .put("chartFileName", "WeightRange")
                        .put("xLabel", "Date")
                        .put("yLabel", "Weight");
                    try { dynChart.LineChart(jsonLabelArray, jsonDataArray, jsonProps); } catch (Exception e) { e.printStackTrace(); }
                    returnData = "Line chart generated!\n";
                    returnData += jsonDataArray.toString();
                    break;
                    
            }
        }
        
        return returnData;
    
    }
        
    
}
