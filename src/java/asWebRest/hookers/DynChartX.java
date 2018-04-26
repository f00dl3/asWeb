/*
by Anthony Stump
Created: 26 Apr 2018
 */

package asWebRest.hookers;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class DynChartX {
    
    public static void LineChart(JSONArray jsonLabelArray, JSONArray jsonDataArray, JSONObject jsonProps) throws Exception {
        
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        File chartCachePath = new File(cb.getPathChartCache());
        if(!chartCachePath.exists()) { chartCachePath.mkdirs(); }
        String chartFileName = jsonProps.getString("chartFileName");
        File fullChart = new File(cb.getPathChartCache() + "/"+chartFileName+".png");
        File thumbChart = new File(cb.getPathChartCache() + "/th_"+chartFileName+".png");
        
        int width = cb.getChartMaxWidth();
        int height = cb.getChartMaxHeight();
        int thWidth = width/6;
        int thHeight = height/6;
        
        XYChart chart = new XYChart(width, height);
        XYChart thChart = new XYChart(thWidth, thHeight);
        
        String[] xData = wc.arrayStringFromJson(jsonLabelArray);
        double[] yData = wc.arrayDoubleFromJson(jsonDataArray);
        
        // Create full resolution chart first!
        chart.setTitle(jsonProps.getString("chartName"));
        chart.setXAxisTitle(jsonProps.getString("xLabel"));
        chart.setYAxisTitle(jsonProps.getString("yLabel"));
        chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        chart.getStyler().setChartBackgroundColor(Color.BLACK);
        chart.getStyler().setChartFontColor(Color.WHITE);
        chart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.PLAIN, 24));
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));
        chart.getStyler().setPlotGridLinesVisible(false);
        XYSeries series = chart.addSeries("y(x)", null, yData);
        series.setMarker(SeriesMarkers.NONE);
        series.setLineColor(XChartSeriesColors.RED);
        BitmapEncoder.saveBitmap(chart, fullChart.toString(), BitmapFormat.PNG);
        
        // Then the thumbnail chart!
        thChart.setTitle(jsonProps.getString("chartName"));
        thChart.setXAxisTitle(jsonProps.getString("xLabel"));
        thChart.setYAxisTitle(jsonProps.getString("yLabel"));
        thChart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        thChart.getStyler().setChartBackgroundColor(Color.BLACK);
        thChart.getStyler().setChartFontColor(Color.WHITE);
        thChart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.PLAIN, 10));
        thChart.getStyler().setLegendVisible(false);
        thChart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));
        thChart.getStyler().setPlotGridLinesVisible(false);
        XYSeries thSeries = thChart.addSeries("y(x)", null, yData);
        thSeries.setMarker(SeriesMarkers.NONE);
        thSeries.setLineColor(XChartSeriesColors.RED);
        BitmapEncoder.saveBitmap(thChart, thumbChart.toString(), BitmapFormat.PNG);
        
    }
    
}
