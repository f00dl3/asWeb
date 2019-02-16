/*
by Anthony Stump
Created: 31 Mar 2018
Updated: 1 Apr 2018
 */

package asWebRest.hookers;

import asWebRest.shared.CommonBeans;
import java.io.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;

public class DynChart {
    
    public static void LineChart(JSONArray jsonDataArray, JSONObject jsonProps) throws Exception {
        
        CommonBeans cb = new CommonBeans();
        
        String axisXLabel = jsonProps.getString("xLabel");
        String axisYLabel = jsonProps.getString("yLabel");
        String chartName = jsonProps.getString("chartName");
        String chartFileName = jsonProps.getString("chartFileName");
        int width = cb.getChartMaxWidth();
        int height = cb.getChartMaxHeight();
        int thWidth = width/6;
        int thHeight = height/6;
        
        File chartCachePath = new File(cb.getPathChartCache());
        File lineChart = new File(cb.getPathChartCache() + "/"+chartFileName+".jpeg");
        File lineChartThumb = new File(cb.getPathChartCache() + "/th_"+chartFileName+".jpeg");
        
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
        
        CategoryAxis axis = lineChartObject.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        
        ChartUtils.saveChartAsJPEG(lineChart, lineChartObject, width, height);
        
        
        JFreeChart lineChartObjectThumb = ChartFactory.createLineChart(
                chartName, axisYLabel, axisXLabel,
                testSet,PlotOrientation.VERTICAL,
                true, true, false
        );
        
        CategoryAxis axisThumb = lineChartObjectThumb.getCategoryPlot().getDomainAxis();
        axisThumb.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        
        ChartUtils.saveChartAsJPEG(lineChartThumb, lineChartObjectThumb, thWidth, thHeight);
        
    }
    
}
