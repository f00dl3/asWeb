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
                .put("sName", "HRRR TF").put("sColor", "Red")
                .put("s2Name", "HRRR DF").put("s2Color", "Green")
                .put("s3Name", "GFS TF").put("s3Color", "Red")
                .put("s4Name", "GFS DF").put("s4Color", "Green")
                .put("s5Name", "NAM TF").put("s5Color", "Red")
                .put("s6Name", "NAM DF").put("s6Color", "Green")
                .put("s7Name", "CMC TF").put("s7Color", "Red")
                .put("s8Name", "CMC DF").put("s8Color", "Green")
                .put("s9Name", "SREF TF").put("s9Color", "Red")
                .put("s10Name", "SREF DF").put("s10Color", "Green")
                .put("xLabel", "Date").put("yLabel", "degrees F");
        List<Integer> hourList = new ArrayList<>();
        for(int i = 0; i < hourSet.length(); i++) {
            JSONObject thisObject = hourSet.getJSONObject(i);
            int thisHour = thisObject.getInt("FHour");
            hourList.add(thisHour);
        }
        JSONObject thisObject = dataIn.getJSONObject(0);
        JSONObject thisCMC = new JSONObject();
        JSONObject thisGFS = new JSONObject();
        JSONObject thisHRRR = new JSONObject();
        JSONObject thisNAM = new JSONObject();
        JSONObject thisSREF = new JSONObject();
        try { thisCMC = new JSONObject(thisObject.getString("CMC")); } catch (Exception e) {}
        try { thisGFS = new JSONObject(thisObject.getString("GFS")); } catch (Exception e) {}
        try { thisHRRR = new JSONObject(thisObject.getString("HRRR")); } catch (Exception e) {}
        try { thisNAM = new JSONObject(thisObject.getString("NAM")); } catch (Exception e) {}
        try { thisSREF = new JSONObject(thisObject.getString("SREF")); } catch (Exception e) {}
        String thisRunString = thisObject.getString("RunString");
        thisRunString = thisRunString.replace("_", "").replace("Z", "");
        final DateTimeFormatter theDateFormat = DateTimeFormat.forPattern("yyyyMMddHH");
        final DateTime trsDateTime = theDateFormat.parseDateTime(thisRunString);
        for(int hour : hourList) {
            DateTime this_trsDateTime = trsDateTime.plusHours(hour);
            String this_ValidTime = theDateFormat.print(this_trsDateTime);
            double cmcTf = -999.9;
            double cmcDf = -999.9;
            double gfsTf = -999.9;
            double gfsDf = -999.9;
            double hrrrTf = -999.9;
            double hrrrDf = -999.9;
            double namTf = -999.9;
            double namDf = -999.9;
            double srefTf = -999.9;
            double srefDf = -999.9;
            try { hrrrTf = wc.tempC2F(thisHRRR.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { hrrrDf = wc.tempC2F(thisHRRR.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { cmcTf = wc.tempC2F(thisCMC.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { cmcDf = wc.tempC2F(thisCMC.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { gfsTf = wc.tempC2F(thisGFS.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { gfsDf = wc.tempC2F(thisGFS.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { namTf = wc.tempC2F(thisNAM.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { namDf = wc.tempC2F(thisNAM.getDouble("D0_"+hour)); } catch (Exception e) {}
            try { srefTf = wc.tempC2F(thisSREF.getDouble("T0_"+hour)); } catch (Exception e) {}
            try { srefDf = wc.tempC2F(thisSREF.getDouble("D0_"+hour)); } catch (Exception e) {}
            if(hrrrTf == -999.9) { hrrrTf = 0.0; }
            if(hrrrDf == -999.9) { hrrrDf = 0.0; } 
            if(cmcTf == -999.9) { cmcTf = hrrrTf; } 
            if(cmcDf == -999.9) { cmcDf = hrrrDf; } 
            if(gfsTf == -999.9) { gfsTf = hrrrTf; } 
            if(gfsDf == -999.9) { gfsDf = hrrrDf; } 
            if(namTf == -999.9) { namTf = hrrrTf; }
            if(namDf == -999.9) { namDf = hrrrDf; } 
            if(srefTf == -999.9) { srefTf = hrrrTf; }
            if(srefDf == -999.9) { srefDf = hrrrDf; } 
            if((hrrrTf + cmcTf + gfsTf + namTf) != 0.0) {
                mosTemps_Labels.put(this_ValidTime);
                mosTemps_Data.put(hrrrTf);
                mosTemps_Data2.put(hrrrDf);
                mosTemps_Data3.put(gfsTf);
                mosTemps_Data4.put(gfsDf);
                mosTemps_Data5.put(namTf);
                mosTemps_Data6.put(namDf);
                mosTemps_Data7.put(cmcTf);
                mosTemps_Data8.put(cmcDf);
                mosTemps_Data9.put(srefTf);
                mosTemps_Data10.put(srefDf);
            }
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
        return mosTemps_Glob;
    }
    
    private JSONObject mosWind(JSONArray dataIn, JSONArray hourSet) {
        String this_ChartName = "Model Output Soundings: Winds";
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Data2 = new JSONArray();
        JSONArray this_Data3 = new JSONArray();
        JSONArray this_Data4 = new JSONArray();
        JSONArray this_Data5 = new JSONArray();
        JSONArray this_Debug = new JSONArray();
        this_Props
                .put("dateFormat", "yyyyMMddHH")
                .put("chartName", this_ChartName).put("chartFileName", "WxMOSWind")
                .put("sName", "HRRR").put("sColor", "Yellow")
                .put("s2Name", "GFS").put("s2Color", "Yellow")
                .put("s3Name", "NAM").put("s3Color", "Yellow")
                .put("s4Name", "CMC").put("s4Color", "Yellow")
                .put("s5Name", "SREF").put("s5Color", "Yellow")
                .put("xLabel", "Date").put("yLabel", "Wind Speed");
        List<Integer> hourList = new ArrayList<>();
        for(int i = 0; i < hourSet.length(); i++) {
            JSONObject thisObject = hourSet.getJSONObject(i);
            int thisHour = thisObject.getInt("FHour");
            hourList.add(thisHour);
        }
        JSONObject thisObject = dataIn.getJSONObject(0);
        JSONObject thisCMC = new JSONObject();
        JSONObject thisGFS = new JSONObject();
        JSONObject thisHRRR = new JSONObject();
        JSONObject thisNAM = new JSONObject();
        JSONObject thisSREF = new JSONObject();
        try { thisCMC = new JSONObject(thisObject.getString("CMC")); } catch (Exception e) {}
        try { thisGFS = new JSONObject(thisObject.getString("GFS")); } catch (Exception e) {}
        try { thisHRRR = new JSONObject(thisObject.getString("HRRR")); } catch (Exception e) {}
        try { thisNAM = new JSONObject(thisObject.getString("NAM")); } catch (Exception e) {}
        try { thisSREF = new JSONObject(thisObject.getString("SREF")); } catch (Exception e) {}
        String thisRunString = thisObject.getString("RunString");
        thisRunString = thisRunString.replace("_", "").replace("Z", "");
        final DateTimeFormatter theDateFormat = DateTimeFormat.forPattern("yyyyMMddHH");
        final DateTime trsDateTime = theDateFormat.parseDateTime(thisRunString);
        for(int hour : hourList) {
            DateTime this_trsDateTime = trsDateTime.plusHours(hour);
            String this_ValidTime = theDateFormat.print(this_trsDateTime);
            double cmcW = 0.0;
            double gfsW = 0.0;
            double hrrrW = 0.0;
            double namW = 0.0;
            double srefW = 0.0;
            try { hrrrW  = thisHRRR.getDouble("WS0_"+hour); } catch (Exception e) {}
            try { cmcW = thisCMC.getDouble("WS0_"+hour); } catch (Exception e) {}
            try { gfsW = thisGFS.getDouble("WS0_"+hour); } catch (Exception e) {}
            try { namW = thisNAM.getDouble("WS0_"+hour); } catch (Exception e) {}
            try { srefW = thisSREF.getDouble("WS0_"+hour); } catch (Exception e) {}
            if((hrrrW + cmcW + gfsW + namW + srefW) != 0.0) {
                if(cmcW == 0) { cmcW = hrrrW; }
                if(gfsW == 0) { gfsW = hrrrW; }
                if(namW == 0) { namW = hrrrW; } 
                if(srefW == 0) { srefW = hrrrW; } 
                this_Labels.put(this_ValidTime);
                this_Data.put(hrrrW);
                this_Data2.put(gfsW);
                this_Data3.put(namW);
                this_Data4.put(cmcW);
            }
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("data3", this_Data3)
                .put("data4", this_Data4)
                .put("data5", this_Data5)
                .put("props", this_Props)
                .put("debug", this_Debug);
        return this_Glob;  
    } 
    
    private JSONObject obsJsonCapeCin(JSONArray dataIn, String stationId) {
        String this_ChartName = "ObsJSON CAPE/CIN for " + stationId;
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Data2 = new JSONArray();
        JSONArray this_Debug = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .put("chartName", this_ChartName).put("chartFileName", "ObsJSONCapeCin")
                .put("sName", "CAPE").put("sColor", "Red")
                .put("s2Name", "CINH").put("s2Color", "Blue")
                .put("xLabel", "Date").put("yLabel", "j/KG @ Height");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("GetTime"));
            JSONObject thisSet = new JSONObject(thisObject.getString("jsonSet"));
            try { this_Data.put(thisSet.getDouble("CAPE")); } catch (Exception e) { }
            try { this_Data2.put(thisSet.getDouble("CIN")); } catch (Exception e) { }
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("props", this_Props)
                .put("debug", this_Debug);
        return this_Glob;
    }
    
    private JSONObject obsJsonHumidity(JSONArray dataIn, String stationId) {
        String this_ChartName = "ObsJSON Humidity for " + stationId;
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Debug = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .put("chartName", this_ChartName).put("chartFileName", "ObsJSONHumi")
                .put("sName", "Humidity").put("sColor", "White")
                .put("xLabel", "Date").put("yLabel", "Percent");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("GetTime"));
            JSONObject thisSet = new JSONObject(thisObject.getString("jsonSet"));
            try { this_Data.put(thisSet.getDouble("RelativeHumidity")); } catch (Exception e) { }
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("props", this_Props)
                .put("debug", this_Debug);
        return this_Glob;
    }
    
    private JSONObject obsJsonLevel(JSONArray dataIn, String stationId) {
        String this_ChartName = "ObsJSON Levels for " + stationId;
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Data2 = new JSONArray();
        JSONArray this_Data3 = new JSONArray();
        JSONArray this_Data4 = new JSONArray();
        JSONArray this_Debug = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .put("chartName", this_ChartName).put("chartFileName", "ObsJSONLevel")
                .put("sName", "CCL").put("sColor", "Yellow")
                .put("s2Name", "SLCL").put("s2Color", "Green")
                .put("s3Name", "FZLV").put("s3Color", "Red")
                .put("s4Name", "WZLV").put("s4Color", "Blue")
                .put("xLabel", "Date").put("yLabel", "Feet");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("GetTime"));
            JSONObject thisSet = new JSONObject(thisObject.getString("jsonSet"));
            double ccl = 0.0;
            double slcl = 0.0;
            double fzlv = 0.0;
            double wzlv = 0.0;
            try { ccl = thisSet.getDouble("CCL"); } catch (Exception e) { }
            try { slcl = thisSet.getDouble("SLCL"); } catch (Exception e) { }
            try { fzlv = thisSet.getDouble("FZLV"); } catch (Exception e) { }
            try { wzlv = thisSet.getDouble("WZLV"); } catch (Exception e) { }
            this_Data.put(ccl);
            this_Data2.put(slcl);
            this_Data3.put(fzlv);
            this_Data4.put(wzlv);
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("data3", this_Data3)
                .put("data4", this_Data4)
                .put("props", this_Props)
                .put("debug", this_Debug);
        return this_Glob;
    }
    
    private JSONObject obsJsonTemps(JSONArray dataIn, String stationId) {
        String this_ChartName = "ObsJSON Temps for " + stationId;
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Data2 = new JSONArray();
        JSONArray this_Debug = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .put("chartName", this_ChartName).put("chartFileName", "ObsJSONTemp")
                .put("sName", "Temperature").put("sColor", "Red")
                .put("s2Name", "Dewpoint").put("s2Color", "Blue")
                .put("xLabel", "Date").put("yLabel", "Degrees");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("GetTime"));
            JSONObject thisSet = new JSONObject(thisObject.getString("jsonSet"));
            try { this_Data.put(thisSet.getDouble("Temperature")); } catch (Exception e) { }
            try { this_Data2.put(thisSet.getDouble("Dewpoint")); } catch (Exception e) { }
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("data2", this_Data2)
                .put("props", this_Props)
                .put("debug", this_Debug);
        return this_Glob;
    }
    
    private JSONObject obsJsonWind(JSONArray dataIn, String stationId) {
        String this_ChartName = "ObsJSON Wind for " + stationId;
        JSONObject this_Glob = new JSONObject();
        JSONObject this_Props = new JSONObject();
        JSONArray this_Labels = new JSONArray();
        JSONArray this_Data = new JSONArray();
        JSONArray this_Debug = new JSONArray();
        this_Props
                .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .put("chartName", this_ChartName).put("chartFileName", "ObsJSONWind")
                .put("sName", "Wind").put("sColor", "Yellow")
                .put("xLabel", "Date").put("yLabel", "MPH");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            this_Labels.put(thisObject.getString("GetTime"));
            JSONObject thisSet = new JSONObject(thisObject.getString("jsonSet"));
            try { this_Data.put(thisSet.getDouble("WindSpeed")); } catch (Exception e) { }
        }
        this_Glob
                .put("labels", this_Labels)
                .put("data", this_Data)
                .put("props", this_Props)
                .put("debug", this_Debug);
        return this_Glob;
    }
    
   
    public JSONObject getCf6cpc(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6cpc(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getCf6depart(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6depart(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getCf6temps(JSONArray cf6Data, String cf6ChDateStart, String cf6ChDateEnd) { return cf6temps(cf6Data, cf6ChDateStart, cf6ChDateEnd); }
    public JSONObject getMosTemps(JSONArray dataIn, JSONArray hourSet) { return mosTemps(dataIn, hourSet); }
    public JSONObject getMosWind(JSONArray dataIn, JSONArray hourSet) { return mosWind(dataIn, hourSet); }
    public JSONObject getObsJsonCapeCin(JSONArray dataIn, String stationId) { return obsJsonCapeCin(dataIn, stationId); }
    public JSONObject getObsJsonHumidity(JSONArray dataIn, String stationId) { return obsJsonHumidity(dataIn, stationId); }
    public JSONObject getObsJsonLevel(JSONArray dataIn, String stationId) { return obsJsonLevel(dataIn, stationId); }
    public JSONObject getObsJsonTemps(JSONArray dataIn, String stationId) { return obsJsonTemps(dataIn, stationId); }
    public JSONObject getObsJsonWind(JSONArray dataIn, String stationId) { return obsJsonWind(dataIn, stationId); }
       
}
    
