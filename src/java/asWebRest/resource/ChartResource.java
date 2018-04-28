/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 27 Apr 2018
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
        JSONObject jsonGlob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch (doWhat) {
                 
                case "CalorieRange":
                    String fcnCalories = "Calorie Range: " + argsInForm.getFirstValue("XDT1") + " to " + argsInForm.getFirstValue("XDT2");
                    qParams.add(argsInForm.getFirstValue("XDT1"));
                    qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jraCalorieRange = getFitnessAction.getChCaloriesR(qParams);
                    for (int i = 0; i < jraCalorieRange.length(); i++) {
                        JSONObject thisObject = jraCalorieRange.getJSONObject(i);
                        labels.put(thisObject.getString("Date"));
                        data.put(thisObject.getDouble("Calories"));
                    }
                    props
                        .put("chartName", fcnCalories)
                        .put("chartFileName", "CalorieRange")
                        .put("xLabel", "Date")
                        .put("yLabel", "Calories");
                    break;
                                   
                case "WeightRange":
                    String fullChartName = "Weight Range: " + argsInForm.getFirstValue("XDT1") + " to " + argsInForm.getFirstValue("XDT2");
                    qParams.add(argsInForm.getFirstValue("XDT1"));
                    qParams.add(argsInForm.getFirstValue("XDT2"));
                    JSONArray jsonResultArray = getFitnessAction.getChWeightR(qParams);
                    for (int i = 0; i < jsonResultArray.length(); i++) {
                        JSONObject thisObject = jsonResultArray.getJSONObject(i);
                        labels.put(thisObject.getString("Date"));
                        data.put(thisObject.getDouble("Weight"));
                    }
                    props
                        .put("chartName", fullChartName)
                        .put("chartFileName", "WeightRange")
                        .put("xLabel", "Date")
                        .put("yLabel", "Weight");
                    break;
                    
            }
            
            jsonGlob
                .put("labels", labels)
                .put("data", data)
                .put("props", props);
            try { dynChart.LineChart(jsonGlob); } catch (Exception e) { e.printStackTrace(); }
            returnData = "Line chart generated!\n";
            returnData += data.toString();
            
        }
        
        return returnData;
    
    }
        
    
}
