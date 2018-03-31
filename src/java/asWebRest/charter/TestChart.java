/*
by Anthony Stump
Created 31 Mar 2018
https://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm

 */

package asWebRest.charter;

import asWebRest.shared.CommonBeans;
import java.io.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class TestChart {
    
    public static void TestChart() throws Exception {
        
        CommonBeans cb = new CommonBeans();
        File chartCachePath = new File(cb.getPathChartCache());
        File lineChart = new File(cb.getPathChartCache() + "/testChart.jpeg");
        
        if(!chartCachePath.exists()) {
            chartCachePath.mkdirs();
        }
        
        String axisLabel = "degrees";
        DefaultCategoryDataset testSet = new DefaultCategoryDataset();
        testSet.addValue(72, axisLabel, "2018-03-01");
        testSet.addValue(65, axisLabel, "2018-03-02");
        testSet.addValue(44, axisLabel, "2018-03-03");
        testSet.addValue(24, axisLabel, "2018-03-04");
        testSet.addValue(55, axisLabel, "2018-03-05");
        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "Temperature Graph", "Date", "Degrees F",
                testSet,PlotOrientation.VERTICAL,
                true, true, false
        );
        int width = 1920;
        int height = 1080;
        
        ChartUtils.saveChartAsJPEG(lineChart, lineChartObject, width, height);
        
    }
    
}
