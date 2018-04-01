/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 1 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.charter.DynChart;
import asWebRest.dao.FitnessDAO;
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

        DynChart dynChart = new DynChart();

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
                    JSONArray fullResultDataset = getFitnessAction.getAll(qParams, "ASC");
                    JSONArray jsonDataArray = new JSONArray();
                    for (int i = 0; i < fullResultDataset.length(); i++) {
                        JSONObject thisDatasetObject = fullResultDataset.getJSONObject(i);
                        String thisDate = thisDatasetObject.getString("Date");
                        double thisWeight = thisDatasetObject.getDouble("Weight");
                        JSONObject thisObj = new JSONObject();
                        thisObj.put("Date", thisDate);
                        thisObj.put("Weight", thisWeight);
                        jsonDataArray.put(thisObj);
                    }
                    String fullChartName = "Weight Range: " + argsInForm.getFirstValue("XDT1") + " to " + argsInForm.getFirstValue("XDT2");
                    jsonProps
                        .put("chartName", fullChartName)
                        .put("chartFileName", "WeightRange")
                        .put("xLabel", "Weight")
                        .put("yLabel", "Date")
                        .put("width", 1920)
                        .put("height", 1080);
                    try { dynChart.LineChart(jsonDataArray, jsonProps); } catch (Exception e) { e.printStackTrace(); }
                    returnData = "Line chart generated!";
                    break;
                    
            }
        }
        
        return returnData;
    
    }
        
    
}
