/*
by Anthony Stump
Created: 26 Apr 2018
Updated: 27 Apr 2018
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
import org.knowm.xchart.style.markers.SeriesMarkers;

public class DynChartX {
    
    private static Color getSeriesColor(String colorIn) {
        Color colorObject = Color.WHITE;
        if(colorIn.substring(0, 1).equals("#")) {
            colorObject = Color.decode(colorIn);
        } else {
            switch(colorIn.toLowerCase()) {
                case "blue": colorObject = Color.BLUE; break;
                case "gray": colorObject = Color.GRAY; break;
                case "green": colorObject = Color.GREEN; break;
                case "magenta": colorObject = Color.MAGENTA; break;
                case "red": colorObject = Color.RED; break;
                case "yellow": colorObject = Color.YELLOW; break;
                case "white": break;
            }
        }
        return colorObject;
    }
    
    public static void LineChart(JSONObject jsonGlobIn) throws Exception {
        
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        JSONObject jp = jsonGlobIn.getJSONObject("props");
        JSONArray jsonLabelArray = jsonGlobIn.getJSONArray("labels");
        JSONArray jsonDataArray = jsonGlobIn.getJSONArray("data");
        
        File chartCachePath = new File(cb.getPathChartCache());
        if(!chartCachePath.exists()) { chartCachePath.mkdirs(); }
        String chartFileName = jp.getString("chartFileName");
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
        
        Color sColor = getSeriesColor(jp.getString("sColor"));
        
        chart.setTitle(jp.getString("chartName"));
        chart.setXAxisTitle(jp.getString("xLabel"));
        chart.setYAxisTitle(jp.getString("yLabel"));
        chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        chart.getStyler().setChartBackgroundColor(Color.BLACK);
        chart.getStyler().setChartFontColor(Color.WHITE);
        chart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.PLAIN, 24));
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setPlotGridLinesColor(getSeriesColor("#5E5E5E"));
        
        thChart.setTitle(jp.getString("chartName"));
        thChart.setXAxisTitle(jp.getString("xLabel"));
        thChart.setYAxisTitle(jp.getString("yLabel"));
        thChart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        thChart.getStyler().setAxisTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 8));
        thChart.getStyler().setChartBackgroundColor(Color.BLACK);
        thChart.getStyler().setChartFontColor(Color.WHITE);
        thChart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.PLAIN, 10));
        thChart.getStyler().setLegendVisible(false);
        thChart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));
        thChart.getStyler().setPlotGridLinesVisible(true);
        thChart.getStyler().setPlotGridLinesColor(getSeriesColor("#5E5E5E"));
        
        XYSeries series = chart.addSeries(jp.getString("sName"), null, yData); series.setMarker(SeriesMarkers.NONE).setLineColor(sColor);
        XYSeries thSeries = thChart.addSeries(jp.getString("sName"), null, yData); thSeries.setMarker(SeriesMarkers.NONE).setLineColor(sColor);
        
        try { if(wc.isSet(jp.getString("s2Name")) && wc.isSet(jp.getString("s2Color"))) {
            JSONArray jsonData2Array = jsonGlobIn.getJSONArray("data2");
            double[] yData2 = wc.arrayDoubleFromJson(jsonData2Array);
            Color s2Color = getSeriesColor(jp.getString("s2Color"));
            XYSeries series2 = chart.addSeries(jp.getString("s2Name"), null, yData2); series2.setMarker(SeriesMarkers.NONE).setLineColor(s2Color);
            XYSeries thSeries2 = thChart.addSeries(jp.getString("s2Name"), null, yData2); thSeries2.setMarker(SeriesMarkers.NONE).setLineColor(s2Color);
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s3Name")) && wc.isSet(jp.getString("s3Color"))) {
            JSONArray jsonData3Array = jsonGlobIn.getJSONArray("data3");
            double[] yData3 = wc.arrayDoubleFromJson(jsonData3Array);
            Color s3Color = getSeriesColor(jp.getString("s3Color"));
            XYSeries series3 = chart.addSeries(jp.getString("s3Name"), null, yData3); series3.setMarker(SeriesMarkers.NONE).setLineColor(s3Color);
            XYSeries thSeries3 = thChart.addSeries(jp.getString("s3Name"), null, yData3); thSeries3.setMarker(SeriesMarkers.NONE).setLineColor(s3Color);
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s4Name")) && wc.isSet(jp.getString("s4Color"))) {
            JSONArray jsonData4Array = jsonGlobIn.getJSONArray("data4");
            double[] yData4 = wc.arrayDoubleFromJson(jsonData4Array);
            Color s4Color = getSeriesColor(jp.getString("s4Color"));
            XYSeries series4 = chart.addSeries(jp.getString("s4Name"), null, yData4); series4.setMarker(SeriesMarkers.NONE).setLineColor(s4Color);
            XYSeries thSeries4 = thChart.addSeries(jp.getString("s4Name"), null, yData4); thSeries4.setMarker(SeriesMarkers.NONE).setLineColor(s4Color);
        } } catch (Exception e) { }
    
    
        BitmapEncoder.saveBitmap(chart, fullChart.toString(), BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(thChart, thumbChart.toString(), BitmapFormat.PNG);
        
    }
    
}
