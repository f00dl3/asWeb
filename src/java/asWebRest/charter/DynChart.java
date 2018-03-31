/*
by Anthony Stump
Created 31 Mar 2018

 */

package asWebRest.charter;

import asWebRest.shared.CommonBeans;
import java.io.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;

public class DynChart {
    
    public static void LineChart(JSONArray jsonDataArray, JSONObject jsonProps) throws Exception {
        
        String axisXLabel = jsonProps.getString("xLabel");
        String axisYLabel = jsonProps.getString("yLabel");
        String chartName = jsonProps.getString("chartName");
        int width = jsonProps.getInt("width");
        int height = jsonProps.getInt("height");
        
        CommonBeans cb = new CommonBeans();
        File chartCachePath = new File(cb.getPathChartCache());
        File lineChart = new File(cb.getPathChartCache() + "/"+chartName+".jpeg");
        
        if(!chartCachePath.exists()) {
            chartCachePath.mkdirs();
        }
        
        DefaultCategoryDataset testSet = new DefaultCategoryDataset();
        
        JSONArray fullResults = jsonDataArray;
        for (int i = 0; i < fullResults.length(); i++) {
            JSONObject thisRow = fullResults.getJSONObject(i);
            double yData = thisRow.getDouble("Weight");
            String xData = thisRow.getString("Date");
            testSet.addValue(yData, "", xData);
        }
        JFreeChart lineChartObject = ChartFactory.createLineChart(
                chartName, axisYLabel, axisXLabel,
                testSet,PlotOrientation.VERTICAL,
                true, true, false
        );
        
        ChartUtils.saveChartAsJPEG(lineChart, lineChartObject, width, height);
        
    }
    
}
