/*
by Anthony Stump
Created: 31 Mar 2018
 */

package asWebRest.resource;

import asWebRest.action.GetFitnessAction;
import asWebRest.charter.DynChart;
import asWebRest.charter.TestChart;
import asWebRest.dao.FitnessDAO;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ChartResource extends ServerResource {
    
    TestChart testChart = new TestChart();
    DynChart dynChart = new DynChart();
    
    @Get
    public String represent() {
        
        GetFitnessAction getFitnessAction = new GetFitnessAction(new FitnessDAO());
        List<String> qParams = new ArrayList<>();
        
        String xdt1 = "2017-03-30";
        String xdt2 = "2018-03-31";
        
        qParams.add(xdt1);
        qParams.add(xdt2);
        
        JSONArray fullResultDataset = getFitnessAction.getAll(qParams);
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
        
        JSONObject jsonProps = new JSONObject();
        
        jsonProps
            .put("chartName", "Weight1Yr")
            .put("xLabel", "lbs")
            .put("yLabel", "date")
            .put("width", 1920)
            .put("height", 1080);
        
        try { dynChart.LineChart(jsonDataArray, jsonProps); } catch (Exception e) { e.printStackTrace(); }
        String returnString = "Line chart generated!";
        return returnString;
    }
    
    
}
