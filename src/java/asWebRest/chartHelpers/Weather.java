/*
by Anthony Stump
Created: 16 May 2018
Updated: 4 Nov 2018
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {
    
    WebCommon wc = new WebCommon();
    
    private JSONObject cf6cpc(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) {       
        String cf6cpc_ChartName = "CPC Data: " + cf6ChDateStart + " to " + cf6ChDateEnd;
        JSONObject cf6cpc_Glob = new JSONObject();
        JSONObject cf6cpc_Props = new JSONObject();
        JSONArray cf6cpc_Labels = new JSONArray();
        JSONArray cf6cpc_Data = new JSONArray();
        JSONArray cf6cpc_Data2 = new JSONArray();
        JSONArray cf6cpc_Data3 = new JSONArray();
        JSONArray cf6cpc_Data4 = new JSONArray();
        cf6cpc_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cf6cpc_ChartName).put("chartFileName", "cf6cpc")
                .put("sName", "AO").put("sColor", "Red")
                .put("s2Name", "AAO").put("s2Color", "Blue")
                .put("s3Name", "NAO").put("s3Color", "Green")
                .put("s4Name", "PNA").put("s4Color", "Yellow")
                .put("xLabel", "Date").put("yLabel", "Anom");
        for(int i = 0; i < cf6Data.length(); i++) {
            JSONObject thisObject = cf6Data.getJSONObject(i);
            cf6cpc_Labels.put(thisObject.getString("Date"));
            cf6cpc_Data.put(thisObject.getDouble("AO"));
            cf6cpc_Data2.put(thisObject.getDouble("AAO"));
            cf6cpc_Data3.put(thisObject.getDouble("NAO"));
            cf6cpc_Data4.put(thisObject.getDouble("PNA"));
        }
        cf6cpc_Glob
                .put("labels", cf6cpc_Labels)
                .put("data", cf6cpc_Data)
                .put("data2", cf6cpc_Data2)
                .put("data3", cf6cpc_Data3)
                .put("data4", cf6cpc_Data4)
                .put("props", cf6cpc_Props);
        return cf6cpc_Glob;
    }
    
    private JSONObject cf6depart(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) {
        String cf6Depart_ChartName = "KMCI Departures: " + cf6ChDateStart + " to " + cf6ChDateEnd;
        JSONObject cf6Depart_Glob = new JSONObject();
        JSONObject cf6Depart_Props = new JSONObject();
        JSONArray cf6Depart_Labels = new JSONArray();
        JSONArray cf6Depart_Data = new JSONArray();   
        cf6Depart_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cf6Depart_ChartName).put("chartFileName", "cf6Depart")
                .put("sName", "Departure").put("sColor", "Yellow")
                .put("xLabel", "Date").put("yLabel", "degrees F");
        for(int i = 0; i < cf6Data.length(); i++) {
            JSONObject thisObject = cf6Data.getJSONObject(i);
            cf6Depart_Labels.put(thisObject.getString("Date"));
            cf6Depart_Data.put(thisObject.getInt("DFNorm"));
        }
        cf6Depart_Glob
                .put("labels", cf6Depart_Labels)
                .put("data", cf6Depart_Data)
                .put("props", cf6Depart_Props);
        return cf6Depart_Glob;
    }
    
    private JSONObject cf6temps(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) {
        String cf6Temps_ChartName = "KMCI Temperatures: " + cf6ChDateStart + " to " + cf6ChDateEnd;
        JSONObject cf6Temps_Glob = new JSONObject();
        JSONObject cf6Temps_Props = new JSONObject();
        JSONArray cf6Temps_Labels = new JSONArray();
        JSONArray cf6Temps_Data = new JSONArray();
        JSONArray cf6Temps_Data2 = new JSONArray();
        cf6Temps_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", cf6Temps_ChartName).put("chartFileName", "cf6Temps")
                .put("sName", "High").put("sColor", "Red")
                .put("s2Name", "Low").put("s2Color", "Blue")
                .put("xLabel", "Date").put("yLabel", "degress F");
        for(int i = 0; i < cf6Data.length(); i++) {
            JSONObject thisObject = cf6Data.getJSONObject(i);
            cf6Temps_Labels.put(thisObject.getString("Date"));
            cf6Temps_Data.put(thisObject.getInt("High"));
            cf6Temps_Data2.put(thisObject.getInt("Low"));
        }
        cf6Temps_Glob
                .put("labels", cf6Temps_Labels)
                .put("data", cf6Temps_Data)
                .put("data2", cf6Temps_Data2)
                .put("props", cf6Temps_Props);
        return cf6Temps_Glob;
    }
    
    private JSONObject mosTemps(JSONArray dataIn, JSONArray hourSet) {
        String mosTemps_ChartName = "Model Output Soundings: Temps";
        JSONObject mosTemps_Glob = new JSONObject();
        JSONObject mosTemps_Props = new JSONObject();
        JSONArray mosTemps_Labels = new JSONArray();
        JSONArray mosTemps_Data = new JSONArray();
        JSONArray mosTemps_Data2 = new JSONArray();
        JSONArray mosTemps_Data3 = new JSONArray();
        JSONArray mosTemps_Data4 = new JSONArray();
        JSONArray mosTemps_Data5 = new JSONArray();
        JSONArray mosTemps_Data6 = new JSONArray();
        JSONArray mosTemps_Data7 = new JSONArray();
        JSONArray mosTemps_Data8 = new JSONArray();
        JSONArray mosTemps_Data9 = new JSONArray();
        JSONArray mosTemps_Data10 = new JSONArray();
        JSONArray mosTemps_Debug = new JSONArray();
        mosTemps_Props
                .put("dateFormat", "yyyyMMddHH")
                .put("chartName", mosTemps_ChartName).put("chartFileName", "WxMOSTemp")
                .put("sName", "Merged TF").put("sColor", "Red")
                .put("s2Name", "Merged DF").put("s2Color", "Green")
                .put("s3Name", "GFS TF").put("s3Color", "White")
                .put("s4Name", "GFS DF").put("s4Color", "Gray")
                .put("s5Name", "NAM TF").put("s5Color", "White")
                .put("s6Name", "NAM DF").put("s6Color", "Gray")
                .put("s7Name", "CMC TF").put("s7Color", "White")
                .put("s8Name", "CMC DF").put("s8Color", "Gray")
                .put("s9Name", "HRRR TF").put("s9Color", "White")
                .put("s10Name", "HRRR DF").put("s10Color", "Gray")
                .put("xLabel", "Date").put("yLabel", "degrees F");
        List<Integer> hourList = new ArrayList<>();
        for(int i = 0; i < hourSet.length(); i++) {
            JSONObject thisObject = hourSet.getJSONObject(i);
            int thisHour = thisObject.getInt("FHour");
            hourList.add(thisHour);
        }
        //Good here! --> System.out.println(hourList.toString());
        JSONObject thisObject = dataIn.getJSONObject(0);
        JSONObject thisCMC = new JSONObject();
        JSONObject thisGFS = new JSONObject();
        JSONObject thisHRRR = new JSONObject();
        JSONObject thisNAM = new JSONObject();
        try { thisCMC = new JSONObject(thisObject.getString("CMC")); } catch (Exception e) {}
        try { thisGFS = new JSONObject(thisObject.getString("GFS")); } catch (Exception e) {}
        try { thisHRRR = new JSONObject(thisObject.getString("HRRR")); } catch (Exception e) {}
        try { thisNAM = new JSONObject(thisObject.getString("NAM")); } catch (Exception e) {}
        String thisRunString = thisObject.getString("RunString");
        thisRunString = thisRunString.replace("_", "").replace("Z", "");
        final DateTimeFormatter theDateFormat = DateTimeFormat.forPattern("yyyyMMddHH");
        final DateTime trsDateTime = theDateFormat.parseDateTime(thisRunString);
        for(int hour : hourList) {
            DateTime this_trsDateTime = trsDateTime.plusHours(hour);
            String this_ValidTime = theDateFormat.print(this_trsDateTime);
            mosTemps_Labels.put(this_ValidTime);
            double cmcTf = -999.9;
            double cmcDf = -999.9;
            double gfsTf = -999.9;
            double gfsDf = -999.9;
            double hrrrTf = -999.9;
            double hrrrDf = -999.9;
            double namTf = -999.9;
            double namDf = -999.9;
            try { hrrrTf = wc.tempC2F(thisHRRR.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { hrrrDf = wc.tempC2F(thisHRRR.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { cmcTf = wc.tempC2F(thisCMC.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { cmcDf = wc.tempC2F(thisCMC.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { gfsTf = wc.tempC2F(thisGFS.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { gfsDf = wc.tempC2F(thisGFS.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { namTf = wc.tempC2F(thisNAM.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { namDf = wc.tempC2F(thisNAM.getDouble("D0_"+hour)); } catch (Exception e) {}
            double mergedCountTf = 0.0;
            double mergedCountDf = 0.0;
            double mergedTf = 0.0;
            double mergedDf = 0.0;
            if(hrrrTf != -999.9) { mergedCountTf++; mergedTf = mergedTf + hrrrTf; } else { hrrrTf = 0.0; } mosTemps_Data9.put(hrrrTf);
            if(hrrrDf != -999.9) { mergedCountTf++; mergedDf = mergedDf + hrrrTf; } else { hrrrDf = 0.0; } mosTemps_Data10.put(hrrrDf);
            if(cmcTf != -999.9) { mergedCountTf++; mergedTf = mergedTf + cmcTf; } else { cmcTf = 0.0; } mosTemps_Data7.put(cmcTf);
            if(cmcDf != -999.9) { mergedCountDf++; mergedDf = mergedDf + cmcDf; } else { cmcDf = 0.0; } mosTemps_Data8.put(cmcDf);
            if(gfsTf != -999.9) { mergedCountTf++; mergedTf = mergedTf + gfsTf; } else { gfsTf = 0.0; } mosTemps_Data3.put(gfsTf);
            if(gfsDf != -999.9) { mergedCountDf++; mergedDf = mergedDf + gfsDf; } else { gfsDf = 0.0; } mosTemps_Data4.put(gfsDf);
            if(namTf != -999.9) { mergedCountTf++; mergedTf = mergedTf + namTf; } else { namTf = 0.0; } mosTemps_Data5.put(namTf);
            if(namDf != -999.9) { mergedCountTf++; mergedDf = mergedDf + namTf; } else { namDf = 0.0; } mosTemps_Data6.put(namDf);
            mergedTf = mergedTf / mergedCountTf;
            mergedDf = mergedDf / mergedCountDf;
            mosTemps_Data.put(hrrrTf);
            mosTemps_Data2.put(hrrrDf);
        }
        mosTemps_Glob
                .put("labels", mosTemps_Labels)
                .put("data", mosTemps_Data)
                .put("data2", mosTemps_Data2)
                .put("data3", mosTemps_Data3)
                .put("data4", mosTemps_Data4)
                .put("data5", mosTemps_Data5)
                .put("data6", mosTemps_Data6)
                .put("data7", mosTemps_Data7)
                .put("data8", mosTemps_Data8)
                .put("data9", mosTemps_Data9)
                .put("data10", mosTemps_Data10)
                .put("props", mosTemps_Props)
                .put("debug", mosTemps_Debug);
        System.out.println(mosTemps_Glob.toString());
        return mosTemps_Glob;
    }
    
    public JSONObject getCf6cpc(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6cpc(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getCf6depart(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6depart(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getCf6temps(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6temps(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getMosTemps(JSONArray dataIn, JSONArray hourSet) { return mosTemps(dataIn, hourSet); }
       
}
    
