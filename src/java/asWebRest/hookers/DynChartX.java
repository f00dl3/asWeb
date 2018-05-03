/*
by Anthony Stump
Created: 26 Apr 2018
Updated: 3 May 2018
 */

package asWebRest.hookers;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
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
                case "orange": colorObject = Color.ORANGE; break;
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
        
        Integer[] xData = wc.arrayIntegerFromJson(jsonLabelArray);
        double[] yData = wc.arrayDoubleOldFromJson(jsonDataArray);
        
        Color sColor = getSeriesColor(jp.getString("sColor"));
        
        chart.setTitle(jp.getString("chartName"));
        chart.setXAxisTitle(jp.getString("xLabel"));
        chart.setYAxisTitle(jp.getString("yLabel"));
        chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        chart.getStyler().setChartBackgroundColor(Color.BLACK);
        chart.getStyler().setChartFontColor(Color.WHITE);
        chart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.PLAIN, 24));
        chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
        chart.getStyler().setLegendBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));
        //chart.getStyler().setLegendTextColor(Color.WHITE);
        chart.getStyler().setLegendVisible(true);
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
        
        try {
            XYSeries series = chart.addSeries(jp.getString("sName"), null, yData); series.setMarker(SeriesMarkers.NONE).setLineColor(sColor);
            XYSeries thSeries = thChart.addSeries(jp.getString("sName"), null, yData); thSeries.setMarker(SeriesMarkers.NONE).setLineColor(sColor);
        } catch (Exception e) { e.printStackTrace(); }
        
        try { if(wc.isSet(jp.getString("s2Name")) && wc.isSet(jp.getString("s2Color"))) {
            try { 
                JSONArray jsonData2Array = jsonGlobIn.getJSONArray("data2");
                float[] yData2 = wc.arrayFloatFromJson(jsonData2Array);
                Color s2Color = getSeriesColor(jp.getString("s2Color"));
                int tArrLen = yData2.length; if(tArrLen != 0) {
                    XYSeries series2 = chart.addSeries(jp.getString("s2Name"), null, yData2); series2.setMarker(SeriesMarkers.NONE).setLineColor(s2Color);
                    XYSeries thSeries2 = thChart.addSeries(jp.getString("s2Name"), null, yData2); thSeries2.setMarker(SeriesMarkers.NONE).setLineColor(s2Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s3Name")) && wc.isSet(jp.getString("s3Color"))) {
            try {
                JSONArray jsonData3Array = jsonGlobIn.getJSONArray("data3");
                float[] yData3 = wc.arrayFloatFromJson(jsonData3Array);
                Color s3Color = getSeriesColor(jp.getString("s3Color"));
                int tArrLen = yData3.length; if(tArrLen != 0) {
                    XYSeries series3 = chart.addSeries(jp.getString("s3Name"), null, yData3); series3.setMarker(SeriesMarkers.NONE).setLineColor(s3Color);
                    XYSeries thSeries3 = thChart.addSeries(jp.getString("s3Name"), null, yData3); thSeries3.setMarker(SeriesMarkers.NONE).setLineColor(s3Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s4Name")) && wc.isSet(jp.getString("s4Color"))) {
            try {
                JSONArray jsonData4Array = jsonGlobIn.getJSONArray("data4");
                float[] yData4 = wc.arrayFloatFromJson(jsonData4Array);
                Color s4Color = getSeriesColor(jp.getString("s4Color"));
                int tArrLen = yData4.length; if(tArrLen != 0) {
                    XYSeries series4 = chart.addSeries(jp.getString("s4Name"), null, yData4); series4.setMarker(SeriesMarkers.NONE).setLineColor(s4Color);
                    XYSeries thSeries4 = thChart.addSeries(jp.getString("s4Name"), null, yData4); thSeries4.setMarker(SeriesMarkers.NONE).setLineColor(s4Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
    
        try { if(wc.isSet(jp.getString("s5Name")) && wc.isSet(jp.getString("s5Color"))) {
            try {
                JSONArray jsonData5Array = jsonGlobIn.getJSONArray("data5");
                float[] yData5 = wc.arrayFloatFromJson(jsonData5Array);
                Color s5Color = getSeriesColor(jp.getString("s5Color"));
                int tArrLen = yData5.length; if(tArrLen != 0) {
                    XYSeries series5 = chart.addSeries(jp.getString("s5Name"), null, yData5); series5.setMarker(SeriesMarkers.NONE).setLineColor(s5Color);
                    XYSeries thSeries5 = thChart.addSeries(jp.getString("s5Name"), null, yData5); thSeries5.setMarker(SeriesMarkers.NONE).setLineColor(s5Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
    
        try { if(wc.isSet(jp.getString("s6Name")) && wc.isSet(jp.getString("s6Color"))) {
            try {
                JSONArray jsonData6Array = jsonGlobIn.getJSONArray("data6");
                float[] yData6 = wc.arrayFloatFromJson(jsonData6Array);
                Color s6Color = getSeriesColor(jp.getString("s6Color"));
                int tArrLen = yData6.length; if(tArrLen != 0) {
                    XYSeries series6 = chart.addSeries(jp.getString("s6Name"), null, yData6); series6.setMarker(SeriesMarkers.NONE).setLineColor(s6Color);
                    XYSeries thSeries6 = thChart.addSeries(jp.getString("s6Name"), null, yData6); thSeries6.setMarker(SeriesMarkers.NONE).setLineColor(s6Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s7Name")) && wc.isSet(jp.getString("s7Color"))) {
            try {
                JSONArray jsonData7Array = jsonGlobIn.getJSONArray("data7");
                float[] yData7 = wc.arrayFloatFromJson(jsonData7Array);
                Color s7Color = getSeriesColor(jp.getString("s7Color"));
                int tArrLen = yData7.length; if(tArrLen != 0) {
                    XYSeries series7 = chart.addSeries(jp.getString("s7Name"), null, yData7); series7.setMarker(SeriesMarkers.NONE).setLineColor(s7Color);
                    XYSeries thSeries7 = thChart.addSeries(jp.getString("s7Name"), null, yData7); thSeries7.setMarker(SeriesMarkers.NONE).setLineColor(s7Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s8Name")) && wc.isSet(jp.getString("s8Color"))) {
            try {
                JSONArray jsonData8Array = jsonGlobIn.getJSONArray("data8");
                float[] yData8 = wc.arrayFloatFromJson(jsonData8Array);
                Color s8Color = getSeriesColor(jp.getString("s8Color"));
                int tArrLen = yData8.length; if(tArrLen != 0) {
                    XYSeries series8 = chart.addSeries(jp.getString("s8Name"), null, yData8); series8.setMarker(SeriesMarkers.NONE).setLineColor(s8Color);
                    XYSeries thSeries8 = thChart.addSeries(jp.getString("s8Name"), null, yData8); thSeries8.setMarker(SeriesMarkers.NONE).setLineColor(s8Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
        
        try { if(wc.isSet(jp.getString("s9Name")) && wc.isSet(jp.getString("s9Color"))) {
            try {
                JSONArray jsonData9Array = jsonGlobIn.getJSONArray("data9");
                float[] yData9 = wc.arrayFloatFromJson(jsonData9Array);
                Color s9Color = getSeriesColor(jp.getString("s9Color"));
                int tArrLen = yData9.length; if(tArrLen != 0) {
                    XYSeries series9 = chart.addSeries(jp.getString("s9Name"), null, yData9); series9.setMarker(SeriesMarkers.NONE).setLineColor(s9Color);
                    XYSeries thSeries9 = thChart.addSeries(jp.getString("s9Name"), null, yData9); thSeries9.setMarker(SeriesMarkers.NONE).setLineColor(s9Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
            
        try { if(wc.isSet(jp.getString("s10Name")) && wc.isSet(jp.getString("s10Color"))) {
            try {
                JSONArray jsonData10Array = jsonGlobIn.getJSONArray("data10");
                float[] yData10 = wc.arrayFloatFromJson(jsonData10Array);
                Color s10Color = getSeriesColor(jp.getString("s10Color"));
                int tArrLen = yData10.length; if(tArrLen != 0) {
                    XYSeries series10 = chart.addSeries(jp.getString("s10Name"), null, yData10); series10.setMarker(SeriesMarkers.NONE).setLineColor(s10Color);
                    XYSeries thSeries10 = thChart.addSeries(jp.getString("s10Name"), null, yData10); thSeries10.setMarker(SeriesMarkers.NONE).setLineColor(s10Color);
                }
            } catch (Exception e) { e.printStackTrace(); }
        } } catch (Exception e) { }
    
        BitmapEncoder.saveBitmap(chart, fullChart.toString(), BitmapFormat.PNG);
        BitmapEncoder.saveBitmap(thChart, thumbChart.toString(), BitmapFormat.PNG);
        
    }
    
}
