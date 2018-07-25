/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 25 Jul 2018
 */

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherDAO {
    
    private CommonBeans wcb = new CommonBeans();
    
    private JSONArray liveWarnings(Connection dbc, List<String> inParams) {
        // 5/23/18: This fails right now. Worked when CAP data was in TEXT but now it's in JSON, REGEX does not work on JSON.
        // 7/15/18: Reason being is something changed with the published/updated/capexpires date comparative strings.
        final String xdt1 = inParams.get(0);
        final String xdt2 = inParams.get(1);
        final String xExp = inParams.get(2);
        final String stationA = inParams.get(3);
        final String idMatch = inParams.get(4);
        final int limit = Integer.parseInt(inParams.get(5));
        String query_LiveWarnings = "SELECT * FROM (" +
                "    SELECT" +
                "         lw.capVersion," +
                "         lw.id," +
                "         lw.published," +
                "         lw.updated," +
                "         lw.title," +
                "         SUBSTRING(lw.summary,1,320) AS briefSummary," +
                "         lw.summary," +
                "         lw.cappolygon," +
                "         lw.cap12polygon," +
                "         lw.capgeocode," +
                "         lw.capparameter," +
                "         lw.capevent," +
                "         lwc.ColorRGB," +
                "         lwc.ColorHEX, lwc.ExtendDisplayTime, lwc.ShowIt, lw.GetTime," +
                "         CASE WHEN lw.capgeocode IS NOT NULL " +
                "             THEN REPLACE(REPLACE(REPLACE(substring_index(substring_index(lw.capgeocode, 'FIPS6', -1), 'UGC', 1),'\r\n\t ',' '),'  ',''),' ',',')" +
                "             ELSE ''" +
                "         END as FIPSCodes," +
                "         lw.cap12same," +
                "         lw.cap12ugc," +
                "         lw.cap12vtec" +
                "     FROM WxObs.LiveWarnings lw" +
                "     LEFT JOIN WxObs.LiveWarningColors lwc ON lw.capevent = lwc.WarnType" +
                "     WHERE" +
                "         CASE WHEN lwc.ExtendDisplayTime = 0" +
                "             THEN" +
                "                  (CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.published,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.published,20,5),'-05:00') BETWEEN '" + xdt1 + "' AND '" + xdt2 + "'" +
                "                     OR CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.updated,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.updated,20,5),'-05:00') BETWEEN '" + xdt1 + "' AND '" + xdt2 + "')" +
                //"                     AND CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.capexpires,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.capexpires,20,5),'-05:00') > '" + xdt2 + "'" +
                "             ELSE" +
                "                 CONVERT_TZ(STR_TO_DATE(SUBSTRING(lw.capexpires,1,19),'%Y-%m-%dT%H:%i:%s'),SUBSTRING(lw.capexpires,20,5),'-05:00') > '" + xExp + "'" +
                "         END" +
                "         AND lw.title IS NOT NULL" +
                "         AND ( lwc.ShowIt = 1 OR lwc.ShowIt IS NULL )";
        if(stationA != "0") {
            query_LiveWarnings += "         AND ( lw.capgeocode REGEXP '" + stationA + "' OR JSON_CONTAINS(lw.cap12same, '[\"" + stationA + "\"]'))";
        }
        query_LiveWarnings += "         AND lw.id REGEXP '" + idMatch + "'" +
                "     ORDER BY lw.GetTime DESC ) as lwm" +
                " GROUP BY " +
                " CASE WHEN cap12polygon IS NOT NULL THEN CONCAT(capevent,cap12polygon) END," +
                " CASE WHEN cappolygon IS NOT NULL THEN CONCAT(capevent,cappolygon) END," +
                " CASE WHEN cappolygon IS NULL AND cap12polygon IS NULL THEN CONCAT(capevent,FIPSCodes,cap12same) END" +
                " ORDER BY published DESC" +
                " LIMIT " + limit + ";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_LiveWarnings, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                JSONArray tSameBounds = new JSONArray();
                tObject
                    .put("capVersion", resultSet.getDouble("capVersion"))
                    .put("id", resultSet.getString("id"))
                    .put("published", resultSet.getString("published"))
                    .put("updated", resultSet.getString("updated"))
                    .put("title", resultSet.getString("title"))
                    .put("briefSummary", resultSet.getString("briefSummary"))
                    .put("summary", resultSet.getString("summary"))
                    .put("cappolygon", resultSet.getString("cappolygon"))
                    .put("cap12polygon", resultSet.getString("cap12polygon"))
                    .put("capgeocode", resultSet.getString("capgeocode"))
                    .put("capparameter", resultSet.getString("capparameter"))
                    .put("capevent", resultSet.getString("capevent"))
                    .put("ColorRGB", resultSet.getString("ColorRGB"))
                    .put("ColorHEX", resultSet.getString("ColorHEX"))
                    .put("ExtendDisplayTime", resultSet.getInt("ExtendDisplayTime"))
                    .put("ShowIt", resultSet.getInt("ShowIt"))
                    .put("GetTime", resultSet.getString("GetTime"))
                    .put("FIPSCodes", resultSet.getString("FIPSCodes"))
                    .put("cap12same", resultSet.getString("cap12same"))
                    .put("cap12ugc", resultSet.getString("cap12ugc"))
                    .put("cap12vtec", resultSet.getString("cap12vtec"));
                JSONArray subIterateSameCodes = new JSONArray(tObject.getString("cap12same"));
		for (int i = 0; i < subIterateSameCodes.length(); i++) {
			List<String> tSameCode = new ArrayList<>();
                        tSameCode.add(0, subIterateSameCodes.getString(i));
                        tSameBounds.put(getLiveWarningsSameBounds(dbc, tSameCode));
                }
                tObject
                        .put("subIterateSameCodes", subIterateSameCodes)
                        .put("tSameBounds", tSameBounds);
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }/* finally {
            tContainer
                    .put(query_LiveWarnings);
        }*/
        return tContainer;
    }
    
    private String timeBetween(String xdt1, String xdt2) {
        return  " BETWEEN CONCAT(SUBSTRING('"+xdt1+"',1,4),'-',SUBSTRING('"+xdt1+"',5,2),'-',SUBSTRING('"+xdt1+"',7,2),' ',SUBSTRING('"+xdt1+"',9,2),':00')" +
            " AND CONCAT(SUBSTRING('"+xdt2+"',1,4),'-',SUBSTRING('"+xdt2+"',5,2),'-',SUBSTRING('"+xdt2+"',7,2),' ',SUBSTRING('"+xdt2+"',9,2),':00')";
    }
        
    private String timeBetweenToUTC(String xdt1, String xdt2) {
        return  " BETWEEN CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING('"+xdt1+"',1,4),'-',SUBSTRING('"+xdt1+"',5,2),'-',SUBSTRING('"+xdt1+"',7,2),' ',SUBSTRING('"+xdt1+"',9,2),':00'), '-5:00', '+0:00')" +
            " AND CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING('"+xdt2+"',1,4),'-',SUBSTRING('"+xdt2+"',5,2),'-',SUBSTRING('"+xdt2+"',7,2),' ',SUBSTRING('"+xdt2+"',9,2),':00'), '-5:00', '+0:00')";
    }
    
    private String timeFromGetTime() {
        return " SUBSTRING(GetTime, 1, 10) AS Date, CONCAT(SUBSTRING(GetTime, 12, 2),SUBSTRING(GetTime, 15, 2)) AS Time,";
    }

    private String updateRainGauge(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_UpdateRainGauge = "INSERT INTO WxObs.RainGauge VALUES (CURDATE(),?,2)" +
                " ON DUPLICATE KEY UPDATE Precip=Precip+?;";
        try { returnData = wc.q2do1c(dbc, query_UpdateRainGauge, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    // xdt1, xdt2, limit - pass orderSet string
    private String xmlBindTheRest(String orderSet) {
        return " WHERE GetTime BETWEEN ? AND ? ORDER BY GetTime "+orderSet+" LIMIT ?";
    }
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getAlmanac(Connection dbc) {
        final String query_Almanac = "SELECT" +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Low < '12') AS ColdNights," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Low < '33') AS Freezing," +
                " (SELECT SUM(Precip) FROM WxObs.RainGauge) AS HomePrecip," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE High > '94') AS HotDays," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Liquid > '0.09') AS PL01," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Liquid > '0.99') AS PL10," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Liquid > '2.49') AS PL25," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE PFlag=1) AS PDays," +
                " (SELECT SUM(Liquid) FROM WxObs.CF6MCI WHERE PFlag=1) AS LP_Total," +
                " (SELECT MAX(Liquid) FROM WxObs.CF6MCI WHERE PFlag=1) AS LP_Max," +
                " (SELECT SUM(Snow) FROM WxObs.CF6MCI WHERE PFlag=1) AS SP_Total," +
                " (SELECT MAX(Snow) FROM WxObs.CF6MCI WHERE PFlag=1) AS SP_Max," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Snow > '0.0') AS PS_Any," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Snow > '0.9') AS PS1," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Snow > '2.9') AS PS3," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Snow > '5.9') AS PS6," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Snow > '7.9') AS PS8," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Snow > '11.9') AS PS12," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Clouds BETWEEN '0' AND '3' AND Clouds IS NOT NULL) AS CL," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Clouds BETWEEN '4' AND '7') AS PC," +
                " (SELECT COUNT(Date) FROM WxObs.CF6MCI WHERE Clouds BETWEEN '8' AND '10') AS OC," +
                " CAST((AVG(Average)) AS DECIMAL(10,1)) AS TAvg_Avg," +
                " CAST((AVG(High)) AS DECIMAL(10,1)) AS High_Avg, MAX(High) AS High_Max, MIN(High) AS High_Min," +
                " CAST((AVG(Low)) AS DECIMAL(10,1)) AS Low_Avg, MAX(Low) AS Low_Max, MIN(Low) AS Low_Min," +
                " CAST((AVG(DFNorm)) AS DECIMAL(10,2)) AS DFNorm_Avg," +
                " MAX(SDepth) AS SDepth_Max, SUM(HDD) AS HDD_Total, SUM(CDD) AS CDD_Total, CAST((AVG(Clouds)*10) AS DECIMAL(10,1)) AS CC_Avg," +
                " CAST((AVG(WAvg)) AS DECIMAL(10,1)) AS WAvg_Avg, MAX(WAvg) AS WAvg_Max, MIN(WAvg) AS WAvg_Min," +
                " CAST((AVG(WMax)) AS DECIMAL(10,1)) AS WMax_Avg, MAX(WMax) AS WMax_Max, MIN(WMax) AS WMax_Min," +
                " COUNT(Date) AS Days, MIN(Date) AS FDay, MAX(Date) AS LDay" +
                " FROM WxObs.CF6MCI;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Almanac, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("ColdNights", resultSet.getInt("ColdNights"))
                    .put("Freezing", resultSet.getInt("Freezing"))
                    .put("HomePrecip", resultSet.getDouble("HomePrecip"))
                    .put("HotDays", resultSet.getInt("HotDays"))
                    .put("PL01", resultSet.getInt("PL01"))
                    .put("PL10", resultSet.getInt("PL10"))
                    .put("PL25", resultSet.getInt("PL25"))
                    .put("PDays", resultSet.getInt("PDays"))
                    .put("LP_Total", resultSet.getDouble("LP_Total"))
                    .put("LP_Max", resultSet.getDouble("LP_Max"))
                    .put("SP_Total", resultSet.getDouble("SP_Total"))
                    .put("SP_Max", resultSet.getDouble("SP_Max"))
                    .put("PS_Any", resultSet.getInt("PS_Any"))
                    .put("PS1", resultSet.getInt("PS1"))
                    .put("PS3", resultSet.getInt("PS3"))
                    .put("PS6", resultSet.getInt("PS6"))
                    .put("PS8", resultSet.getInt("PS8"))
                    .put("PS12", resultSet.getInt("PS12"))
                    .put("CL", resultSet.getInt("CL"))
                    .put("PC", resultSet.getInt("PC"))
                    .put("OC", resultSet.getInt("OC"))
                    .put("TAvg_Avg", resultSet.getDouble("TAvg_Avg"))
                    .put("High_Avg", resultSet.getDouble("High_Avg"))
                    .put("High_Max", resultSet.getInt("High_Max"))
                    .put("High_Min", resultSet.getInt("High_Min"))
                    .put("Low_Avg", resultSet.getDouble("Low_Avg"))
                    .put("Low_Max", resultSet.getInt("Low_Max"))
                    .put("Low_Min", resultSet.getInt("Low_Min"))
                    .put("DFNorm_Avg", resultSet.getDouble("DFNorm_Avg"))
                    .put("SDepth_Max", resultSet.getDouble("SDepth_Max"))
                    .put("HDD_Total", resultSet.getInt("HDD_Total"))
                    .put("CDD_Total", resultSet.getInt("CDD_Total"))
                    .put("CC_Avg", resultSet.getDouble("CC_Avg"))
                    .put("WAvg_Avg", resultSet.getDouble("WAvg_Avg"))
                    .put("WAvg_Max", resultSet.getDouble("WAvg_Max"))
                    .put("WAvg_Min", resultSet.getDouble("WAvg_Min"))
                    .put("WMax_Avg", resultSet.getDouble("WMax_Avg"))
                    .put("WMax_Max", resultSet.getDouble("WMax_Max"))
                    .put("WMax_Min", resultSet.getDouble("WMax_Min"))
                    .put("Days", resultSet.getInt("Days"))
                    .put("FDay", resultSet.getString("FDay"))
                    .put("LDay", resultSet.getString("LDay"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAlmanacHOpt(List<String> qParams) {
        final String query_Almanac_H_Opt = "SELECT COUNT(High) AS HiR FROM WxObs.CF6MCI WHERE High BETWEEN ? AND ?;"; // HRB, HRT
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Almanac_H_Opt, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("HiR", resultSet.getInt("HiR"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
         
    public JSONArray getAlmanacLOpt(List<String> qParams) {
        final String query_Almanac_L_Opt = "SELECT COUNT(Low) AS LiR FROM WxObs.CF6MCI WHERE Low BETWEEN ? AND ?;"; // LRB, LRT
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Almanac_L_Opt, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("LiR", resultSet.getInt("LiR"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer; 
    }  
    
    public JSONArray getAlmanacWxOpt(List<String> qParams) {
        final String query_Almanac_Wx_Opt = "SELECT COUNT(Date) AS ThisWx FROM WxObs.CF6MCI WHERE Weather LIKE ? OR Weather LIKE ?;"; // WxCnd, WxCNu
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Almanac_Wx_Opt, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("ThisWx", resultSet.getInt("ThisWx"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    
    
    public JSONArray getAutoStations() {
        final String query_AutoStations = "SELECT Station FROM WxObs.Stations WHERE Region IN ('AUT', 'XA1', 'XA2', 'XA3');";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_AutoStations, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Station", resultSet.getString("Station"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getCf6MciMain(Connection dbc, List<String> qParams, String order) {
        String query_CF6MCI_Main = "SELECT cf6.Date," +
            " ued.kWh, fit.RunWalk, fit.Cycling," +
            " cf6.High, cf6.Low, cf6.Average, cf6.DFNorm," +
            " cf6.HDD, cf6.CDD," +
            " cf6.Liquid, cf6.Snow, cf6.SDepth, rg.Precip as HomePrecip," +
            " cf6.WAvg, cf6.WMax, cf6.Clouds, cf6.Weather," +
            " ao.Anom AS AO, aao.Anom AS AAO," +
            " nao.Anom AS NAO, pna.Anom AS PNA" +
            " FROM WxObs.CF6MCI cf6" +
            " LEFT OUTER JOIN Core.Fitness fit ON cf6.Date = fit.Date" +
            " LEFT OUTER JOIN WxObs.RainGauge rg ON cf6.Date = rg.Date" +
            " LEFT OUTER JOIN Core.UseElecD ued ON cf6.Date = ued.Date" +
            " LEFT JOIN WxObs.CPC_AO ao ON cf6.Date = ao.ObsDate" +
            " LEFT JOIN WxObs.CPC_AAO aao ON cf6.Date = aao.ObsDate" +
            " LEFT JOIN WxObs.CPC_NAO nao ON cf6.Date = nao.ObsDate" +
            " LEFT JOIN WxObs.CPC_PNA pna ON cf6.Date = pna.ObsDate" +
            " WHERE cf6.Date BETWEEN ? AND ?" +
            " ORDER BY cf6.Date " + order +
            " LIMIT 9150;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_CF6MCI_Main, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("AAO", resultSet.getDouble("AAO"))
                    .put("AO", resultSet.getDouble("AO"))
                    .put("Average", resultSet.getDouble("Average"))
                    .put("CDD", resultSet.getInt("CDD"))
                    .put("Clouds", resultSet.getInt("Clouds"))
                    .put("Cycling", resultSet.getDouble("Cycling"))
                    .put("Date", resultSet.getString("Date"))
                    .put("DFNorm", resultSet.getDouble("DFNorm"))
                    .put("HDD", resultSet.getInt("HDD"))
                    .put("High", resultSet.getInt("High"))
                    .put("HomePrecip", resultSet.getDouble("HomePrecip"))
                    .put("kWh", resultSet.getDouble("kWh"))
                    .put("Liquid", resultSet.getDouble("Liquid"))
                    .put("Low", resultSet.getInt("Low"))
                    .put("NAO", resultSet.getDouble("NAO"))
                    .put("PNA", resultSet.getDouble("PNA"))
                    .put("RunWalk", resultSet.getDouble("RunWalk"))
                    .put("SDepth", resultSet.getDouble("SDepth"))
                    .put("Snow", resultSet.getDouble("Snow"))
                    .put("WAvg", resultSet.getDouble("WAvg"))
                    .put("Weather", resultSet.getString("Weather"))
                    .put("WMax", resultSet.getDouble("WMax"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChXmlWxObs() {
        final String query_ch_XMLWxObs = "SELECT EndRunTime, Duration FROM WxObs.Logs ORDER BY EndRunTime DESC LIMIT 2880;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_XMLWxObs, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Station", resultSet.getString("EndRunTime"))
                    .put("Duration", resultSet.getInt("Duration"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getGfsFha(Connection dbc) {
        final String query_GFSFHA = "SELECT FHour, GFS, NAM, RAP, CMC, HRRR, HRWA, HRWA as HRWN, SRFA, SRFA as SRFN FROM WxObs.GFSFHA WHERE DoGet=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GFSFHA, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("FHour", resultSet.getInt("FHour"))
                    .put("GFS", resultSet.getInt("GFS"))
                    .put("NAM", resultSet.getInt("NAM"))
                    .put("RAP", resultSet.getInt("RAP"))
                    .put("CMC", resultSet.getInt("CMC"))
                    .put("HRRR", resultSet.getInt("HRRR"))
                    .put("HRWA", resultSet.getInt("HRWA"))
                    .put("HRWN", resultSet.getInt("HRWN"))
                    .put("SRFA", resultSet.getInt("SRFA"))
                    .put("SRFN", resultSet.getInt("SRFN"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
     
    public JSONArray getHeights(Connection dbc) {
        final String query_Heights = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE GFS=1 ORDER BY HeightMb DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Heights, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("HeightMb", resultSet.getInt("HeightMb"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getHeightsAll() {
        final String query_HeightsAll = "SELECT HeightMb FROM WxObs.ModelHeightLevels ORDER BY HeightMb DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_HeightsAll, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("HeightMb", resultSet.getInt("HeightMb"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
         
    public JSONArray getHTrackLast(Connection dbc) {
        final String query_HTrack_Last = "SELECT StormID FROM WxObs.HurricaneTracks ORDER BY AddedTime DESC LIMIT 1;";
        
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_HTrack_Last, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("StormID", resultSet.getString("StormID"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getHTracks(List<String> qParams) {
        final String query_HTrack = "SELECT StormID, DateStart, DateEnd, StormName, MaxCategory, MaxWindKT, MinPresMB, ASON" +
                " FROM WxObs.HurricaneTracks WHERE StormID LIKE ? ORDER BY StormID DESC;"; // Year
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_HTrack, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("StormID", resultSet.getString("StormID"))
                    .put("DateStart", resultSet.getString("DateStart"))
                    .put("DateEnd", resultSet.getString("DateEnd"))
                    .put("StormName", resultSet.getString("StormName"))
                    .put("MaxCategory", resultSet.getString("MaxCategory"))
                    .put("MaxWindKT", resultSet.getInt("MaxWindKT"))
                    .put("MinPresMB", resultSet.getInt("MinPresMB"))
                    .put("ASON", resultSet.getString("ASON"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getJsonModelData(Connection dbc, List<String> qParams) {
        final String query_JSONModelData = "SELECT RunString, GFS, NAM, RAP, CMC, HRRR, HRWA, HRWN, SRFA, SRFN" +
                " FROM WxObs.KOJC_MFMD WHERE RunString=?;"; // RunStringMatcher
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_JSONModelData, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("RunString", resultSet.getString("RunString"))
                    .put("GFS", resultSet.getString("GFS"))
                    .put("NAM", resultSet.getString("NAM"))
                    .put("RAP", resultSet.getString("RAP"))
                    .put("CMC", resultSet.getString("CMC"))
                    .put("HRRR", resultSet.getString("HRRR"))
                    .put("HRWA", resultSet.getString("HRWA"))
                    .put("HRWN", resultSet.getString("HRWN"))
                    .put("SRFA", resultSet.getString("SRFA"))
                    .put("SRFN", resultSet.getString("SRFN"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getJsonModelLast(Connection dbc) {
        final String query_JSONModelLast = "SELECT RunString FROM WxObs.MOS_Index ORDER BY RunID DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_JSONModelLast, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("RunString", resultSet.getString("RunString"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getJsonModelRuns(Connection dbc) {
        final String query_JSONModelRuns = "SELECT RunString FROM WxObs.MOS_Index ORDER BY RunID DESC LIMIT 48;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_JSONModelRuns, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("RunString", resultSet.getString("RunString"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
        
    public JSONArray getLiveReports(Connection dbc, List<String> inParams) {
        final String xdt1 = inParams.get(0);
        final String xdt2 = inParams.get(1);
        final DateTimeFormatter theDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        final DateTime xdt1_dto_in = theDateFormat.parseDateTime(xdt1).withZone(DateTimeZone.forID("America/Chicago"));
        final DateTime xdt2_dto_in = theDateFormat.parseDateTime(xdt2).withZone(DateTimeZone.forID("America/Chicago"));
        final DateTime xdt1_dto_out = xdt1_dto_in.minusHours(24).toDateTime(DateTimeZone.UTC);
        final DateTime xdt2_dto_out = xdt2_dto_in.toDateTime(DateTimeZone.UTC);
        final String xdt1_utc = theDateFormat.print(xdt1_dto_out);
        final String xdt2_utc = theDateFormat.print(xdt2_dto_out);
        final int limit = Integer.parseInt(inParams.get(2));                
        final String query_LiveReports = "SELECT Date, Time, Type, Magnitude, Lat, Lon, Location, Comments, County, State FROM (" +
                " /* SELECT" +
                "   CASE" +
		"       WHEN Time BETWEEN '0' AND '1200' THEN" +
		"           SUBSTRING(CONVERT_TZ(CONCAT(DATE_ADD(Date, INTERVAL +0 DAY), ' ', CONCAT(SUBSTRING(Time,1,2),':',SUBSTRING(Time,3,2))), '+00:00', '-05:00'), 1, 10)" +
		"       ELSE" +
                "           SUBSTRING(CONVERT_TZ(CONCAT(Date, ' ', CONCAT(SUBSTRING(Time,1,2),':',SUBSTRING(Time,3,2))), '+00:00', '-05:00'), 1, 10)" +
                "   END as Date," +
                "   SUBSTRING(CONVERT_TZ(CONCAT(Date, ' ', CONCAT(SUBSTRING(Time,1,2),':',SUBSTRING(Time,3,2))), '+00:00', '-05:00'), 12, 5) AS Time," +
                "   Type, Magnitude, Lat, Lon, Location, Comments, County, State" +
                " FROM WxObs.SPCReportsLive" +
                " WHERE CONCAT(Date,' ',SUBSTRING(Time,1,2),':',SUBSTRING(Time,3,2)) BETWEEN CONVERT_TZ('"+xdt1+"','-05:00','+00:00') AND CONVERT_TZ('"+xdt2+"','-05:00','+00:00')" +
                " UNION ALL SELECT" +
                "   SUBSTRING(CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING(BEGIN_YEARMONTH,1,4),'-',SUBSTRING(BEGIN_YEARMONTH,5,2),'-',BEGIN_DAY,' ',BEGIN_TIME), '%Y-%m-%e %H%i'), '+00:00', '-05:00'),1,10) AS Date," +
                "   SUBSTRING(CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING(BEGIN_YEARMONTH,1,4),'-',SUBSTRING(BEGIN_YEARMONTH,5,2),'-',BEGIN_DAY,' ',BEGIN_TIME), '%Y-%m-%e %H%i'), '+00:00', '-05:00'),12,5) AS Time," +
                "   EVENT_TYPE as Type, MAGNITUDE as Magnitude, BEGIN_LAT as Lat, BEGIN_LON as Lon, CONCAT(BEGIN_LOCATION, '(', CZ_NAME, ')') AS Location," +
                "   SUBSTRING(CONCAT(EVENT_NARRATIVE,'<br/>',EPISODE_NARRATIVE),1,256) AS Comments," +
                "   CZ_NAME as County, STATE as State" +
                " FROM WxObs.NCDCStormEvents" +
                " WHERE CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING(BEGIN_YEARMONTH,1,4),'-',SUBSTRING(BEGIN_YEARMONTH,5,2),'-',BEGIN_DAY,' ',BEGIN_TIME), '%Y-%m-%e %H%i'), '+00:00', '-05:00') BETWEEN '"+xdt1+"' AND '"+xdt2+"'" +
                " UNION ALL */ SELECT" +
                "   SUBSTRING(CONVERT_TZ(STR_TO_DATE(SUBSTRING(time,1,19),'%Y-%m-%dT%H:%i:%s'), '+00:00', '-05:00'), 1, 10) AS Date," +
                "   SUBSTRING(CONVERT_TZ(STR_TO_DATE(SUBSTRING(time,1,19),'%Y-%m-%dT%H:%i:%s'), '+00:00', '-05:00'), 12, 5) AS Time," +
                "   'Q' as Type," +
                "   mag as Magnitude, latitude as Lat, longitude as Lon, place as Location, NULL as County, NULL as State, type AS Comments" +
                " FROM WxObs.ANSSQuakes" +
                " WHERE time BETWEEN '"+xdt1_utc+"' AND '"+xdt2_utc+"'" +
                " ) as tmp" +
                " ORDER BY CONCAT(Date,' ',Time) DESC LIMIT "+limit+";";
        System.out.println(query_LiveReports);
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_LiveReports, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Time", resultSet.getString("Time"))
                    .put("Type", resultSet.getString("Type"))
                    .put("Magnitude", resultSet.getString("Magnitude"))
                    .put("Lat", resultSet.getString("Lat"))
                    .put("Lon", resultSet.getString("Lon"))
                    .put("Location", resultSet.getString("Location"))
                    .put("Comments", resultSet.getString("Comments"))
                    .put("County", resultSet.getString("County"))
                    .put("State", resultSet.getString("State"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getLiveWarnings(Connection dbc, List<String> inParams) { return liveWarnings(dbc, inParams); }
    
    public JSONArray getLiveWarningsFipsBounds(Connection dbc, List<String> qParams) {
        final String query_LiveWarnings_FIPSBounds = "SELECT" +
                " fips.State, fips.Description, REPLACE(cg.coordinates, ',0 ',' ') as coords" +
                " FROM WxObs.FIPSCodes fips" +
                " LEFT JOIN WxObs.USCountyGeoBounds cg" +
                " ON cg.name = CONCAT(fips.State,'_',REPLACE(REPLACE(REPLACE(fips.Description,' County',''),' Parish',''),' Borough',''))" +
                " WHERE FIPS = SUBSTRING(?,2,5);"; //fipsCode
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_LiveWarnings_FIPSBounds, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("State", resultSet.getString("State"))
                    .put("Description", resultSet.getString("Description"))
                    .put("coords", resultSet.getString("coords"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getLiveWarningsSameBounds(Connection dbc, List<String> qParams) {
        final String query_LiveWarnings_SAMEBounds = "SELECT same.State, same.County, REPLACE(cg.coordinates, ',0 ',' ') as coords" +
                " FROM WxObs.SAMECodes same" +
                " LEFT JOIN WxObs.USCountyGeoBounds cg ON cg.name = CONCAT(same.State,'_',REPLACE(REPLACE(REPLACE(same.County,' County',''),' Parish',''),' Borough',''))" +
                " WHERE SAME = ?;"; // sameCode
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_LiveWarnings_SAMEBounds, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                if(wc.isSet(resultSet.getString("coords"))) {
                    String tCoordArray = ("[[" + resultSet.getString("coords").replace(" ", "],[") + "]]").replace("[]", "");
                    tObject
                        .put("State", resultSet.getString("State"))
                        .put("County", resultSet.getString("County"))
                        .put("coords", new JSONArray(tCoordArray));
                    tContainer.put(tObject);
                }
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getLiveWatches(Connection dbc, List<String> inParams) {
        final String xdt1 = inParams.get(0);
        final String xdt2 = inParams.get(1);
        final int limit = Integer.parseInt(inParams.get(2));                
        final String query_LiveWatches = "SELECT WatchID, WatchBox, Type, description, GetTime, ShortWID, pubDate FROM (" +
                " SELECT" +
                "   wb.WatchID as WatchID, wb.WatchBox as WatchBox, ww.description as description," +
                "   ww.GetTime as GetTime, ww.pubDate as pubDate, 'WW' as Type, SUBSTRING(wb.WatchID,6,16) as ShortWID," +
                "   wb.Timestamp as Timestamp" +
                " FROM WxObs.SPCWatchBoxes wb" +
                " LEFT JOIN WxObs.SPCWatches ww ON ww.description LIKE CONCAT('%', SUBSTRING(wb.WatchID,6,16), '%')" +
                " WHERE CONVERT_TZ(STR_TO_DATE(REPLACE(ww.pubDate,' +0000',''), '%a, %d %b %Y %H:%i:%s'), '+00:00', '-05:00') BETWEEN '"+xdt1+"' AND '"+xdt2+"'" +
                " UNION ALL" +
                " SELECT" +
                "   ms.mdID as WatchID, ms.Bounds as WatchBox, md.description as description, " +
                "   md.GetTime as GetTime, md.pubDate as pubDate, 'MD' as Type, SUBSTRING(ms.mdID,6,16) as ShortWID," +
                "   ms.GetTimestamp as Timestamp" +
                " FROM WxObs.SPCMesoscaleShape ms" +
                " LEFT JOIN WxObs.SPCMesoscale md ON md.description LIKE CONCAT('%', SUBSTRING(ms.mdID,6,16), '%')" +
                " WHERE CONVERT_TZ(STR_TO_DATE(REPLACE(md.pubDate,' +0000',''), '%a, %d %b %Y %H:%i:%s'), '+00:00', '-05:00') BETWEEN '"+xdt1+"' AND '"+xdt2+"'" +
                ") as tmp" +
                " ORDER BY STR_TO_DATE(REPLACE(pubDate,' +0000',''), '%a, %d %b %Y %H:%i%s') DESC LIMIT "+limit+";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_LiveWatches, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WatchID", resultSet.getString("WatchID"))
                    .put("WatchBox", resultSet.getString("WatchBox"))
                    .put("Type", resultSet.getString("Type"))
                    .put("description", wc.htmlStripTease(resultSet.getString("description")))
                    .put("GetTime", resultSet.getString("GetTime"))
                    .put("ShortWID", resultSet.getString("ShortWID"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getLogsXmlWxObs() {
        final String query_Logs_XMLWxObs = "SELECT EndRunTime, Duration FROM Logs ORDER BY EndRunTime DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Logs_XMLWxObs, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EndRunTime", resultSet.getInt("EndRunTime"))
                    .put("Duration", resultSet.getLong("Duration"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }   
    
    public JSONArray getObsJson(Connection dbc, List<String> qParams, List<String> inParams) {
        final String order = inParams.get(0);
        final String xmlBindTheRest = xmlBindTheRest(order);
        final String query_ObsJSON = "SELECT ObsID, GetTime, jsonData" +
                " FROM WxObs.StationDataIndexed" + xmlBindTheRest;
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsJSON, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("ObsID", resultSet.getLong("ObsID"))
                    .put("GetTime", resultSet.getString("GetTime"))
                    .put("jsonData", new JSONObject(resultSet.getString("jsonData")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
     
    public JSONArray getObsJsonRapid(Connection dbc, List<String> qParams, List<String> inParams) {
        final String order = inParams.get(0);
        final String xmlBindTheRest = xmlBindTheRest(order);
        final String query_ObsJSON = "SELECT ObsID, GetTime, jsonData" +
                " FROM WxObs.RapidSDI" + xmlBindTheRest;
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsJSON, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("ObsID", resultSet.getLong("ObsID"))
                    .put("GetTime", resultSet.getString("GetTime"))
                    .put("jsonData", new JSONObject(resultSet.getString("jsonData")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getObsJsonLast(Connection dbc) {
        final String query_ObsJSON_Last = "SELECT ObsID, JSON_EXTRACT(jsonData, '$.KOJC') as jsonSet" +
                " FROM WxObs.StationDataIndexed WHERE ObsID=(SELECT MAX(ObsID)-1 FROM WxObs.StationDataIndexed);";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsJSON_Last, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("jsonSet", resultSet.getString("jsonSet"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getObsJsonByStation(Connection dbc, List<String> inParams) {
        final String xdt1 = inParams.get(0);
        final String xdt2 = inParams.get(1);
        final String order = inParams.get(2);
        final String limit = inParams.get(3);
        final String station = "$." + inParams.get(4);
        final String query_ObsJSONbyStation = "SELECT ObsID, GetTime, jsonSet FROM (" +
		" SELECT ObsID, GetTime, JSON_EXTRACT(jsonData, '"+station+"') as jsonSet FROM WxObs.StationDataIndexed WHERE GetTime BETWEEN '"+xdt1+"' AND '"+xdt2+"' UNION ALL" +
		" SELECT ObsID, GetTime, JSON_EXTRACT(jsonData, '"+station+"') as jsonSet FROM WxObs.RapidSDI WHERE GetTime BETWEEN '"+xdt1+"' AND '"+xdt2+"'" +
                " ) as tmpSet" +
                " WHERE jsonSet IS NOT NULL" +
                " ORDER BY GetTime "+order+" LIMIT "+limit+";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsJSONbyStation, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("ObsID", resultSet.getLong("ObsID"))
                    .put("GetTime", resultSet.getString("GetTime"))
                    .put("jsonSet", resultSet.getString("jsonSet"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getObsJsonStations(Connection dbc, List<String> qParams) {
        final String query_ObsJSON_Stations = "SELECT " +
                " st.Station as Station, st.Point as Point, st.City as City, st.State as State," +
                " st.SfcMB as SfcMB, st.Priority as Priority, st.Region as Region, cs.Description as Description" +
                " FROM WxObs.Stations st" +
                " LEFT OUTER JOIN WxObs.CountryStates2 cs ON cs.Region = st.Region AND SUBSTRING(cs.CCSC,3,2) = st.State" +
                " WHERE Active=1 AND Station LIKE ?" +
                " GROUP BY st.Station;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsJSON_Stations, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Station", resultSet.getString("Station"))
                    .put("Point", resultSet.getString("Point"))
                    .put("City", resultSet.getString("City"))
                    .put("State", resultSet.getString("State"))
                    .put("SfcMB", resultSet.getInt("SfcMB"))
                    .put("Priority", resultSet.getInt("Priority"))
                    .put("Region", resultSet.getString("Region"))
                    .put("Description", resultSet.getString("Description"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
       
    public JSONArray getObsJsonStationCount(Connection dbc) {
        final String query_ObsJSON_StationCount = "SELECT COUNT(Station) as StationCount" +
                " FROM WxObs.Stations" +
                " WHERE Active=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsJSON_StationCount, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("StationCount", resultSet.getLong("StationCount"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getObsXmlGeo(Connection dbc) {
        final String query_ObsXML_Geo = "SELECT" +
                " st.Station as Station, st.Point as Point, st.City as City, st.State as State," +
                " st.SfcMB as SfcMB, st.Priority as Priority, st.Region as Region, cs.Description as Description" +
                " FROM WxObs.Stations st" +
                " LEFT OUTER JOIN WxObs.CountryStates2 cs ON cs.Region = st.Region AND SUBSTRING(cs.CCSC,3,2) = st.State" +
                " WHERE Active=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsXML_Geo, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Station", resultSet.getString("Station"))
                    .put("Point", resultSet.getString("Point"))
                    .put("City", resultSet.getString("City"))
                    .put("State", resultSet.getString("State"))
                    .put("SfcMB", resultSet.getInt("SfcMB"))
                    .put("Priority", resultSet.getInt("Priority"))
                    .put("Region", resultSet.getString("Region"))
                    .put("Description", resultSet.getString("Description"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }   
    
    public JSONArray getObsXmlReg(Connection dbc) {
        final String query_ObsXML_Reg = "SELECT Code, Description FROM WxObs.Regions ORDER BY Code ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ObsXML_Reg, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Code", resultSet.getString("Code"))
                    .put("Description", resultSet.getString("Description"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getRadarList(Connection dbc) {
        final String query_RadarList = "SELECT Site, BoundsNSEW FROM WxObs.RadarList WHERE Active=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_RadarList, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Site", resultSet.getString("Site"))
                    .put("BoundsNSEW", resultSet.getString("BoundsNSEW"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
            
    public JSONArray getReanalysis(List<String> qParams) {
        final String query_Reanalysis = "SELECT" +
                " DateTime, CONCAT(SUBSTRING(DateTime,1,4),'_',LPAD(SUBSTRING(DateTime,6,10),4,'0')) AS DateTime4D," +
                " JSON_EXTRACT(jsonData, '$.KOJC') as raKOJC, JSON_EXTRACT(jsonExtra, '$.KOJC') as raKOJCx" +
                " FROM WxObs.ReanalysisData" +
                " WHERE DateTime LIKE ?" +
                " ORDER BY CONCAT(SUBSTRING(DateTime,1,4),'_',LPAD(SUBSTRING(DateTime,6,10),4,'0'))" +
                " DESC LIMIT 1460;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Reanalysis, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("DateTime", resultSet.getString("DateTime"))
                    .put("DateTime4D", resultSet.getString("DateTime4D"))
                    .put("raKOJC", resultSet.getString("raKOJC"))
                    .put("raKOJCx", resultSet.getString("raKOJCx"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSpcLive(Connection dbc, List<String> qParams) {
        final String query_SPCLive = "SELECT GetTime, Type, title, description FROM (" +
                " SELECT GetTime, 'HN' AS Type, title, description FROM WxObs.NHCFeeds UNION ALL" +
                " SELECT GetTime, 'MD' AS Type, title, description FROM WxObs.SPCMesoscale UNION ALL" +
                " SELECT GetTime, 'WW' as Type, title, description FROM WxObs.SPCWatches UNION ALL" +
                " SELECT GetTime, 'OL' as Type, title, description FROM WxObs.SPCOutlooks " +
                ") as tmp WHERE GetTime LIKE ? AND Type LIKE ? ORDER BY GetTime DESC LIMIT 20;"; //TimeQuery, TypeQuery
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SPCLive, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Type", resultSet.getString("Type"))
                    .put("title", resultSet.getString("title"))
                    .put("description", resultSet.getString("description"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getStormReportsByDate(Connection dbc, List<String> inParams) {
        final String xdt1 = inParams.get(0);
        final String xdt2 = inParams.get(1);
        final String query_StormReportsByDate = "SELECT" +
                " Date, Time, Type, Magnitude, Lat, Lon, Location, Comments, County, State FROM (" +
                " SELECT" +
		"   DATE_SUB(SUBSTRING(CONVERT_TZ(CONCAT(Date, ' ', CONCAT(SUBSTRING(Time,1,2),':',SUBSTRING(Time,3,2))), '+00:00', '-05:00'), 1, 10), INTERVAL 1 DAY) AS Date," +
		"   SUBSTRING(CONVERT_TZ(CONCAT(Date, ' ', CONCAT(SUBSTRING(Time,1,2),':',SUBSTRING(Time,3,2))), '+00:00', '-05:00'), 12, 5) AS Time," +
                "   Type, Magnitude, Lat, Lon, Location, Comments, County, State" +
                " FROM WxObs.SPCReportsLive WHERE CONCAT(Date,' ',Time)" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" +
		"   SUBSTRING(CONVERT_TZ(time, '+00:00', '-05:00'), 1, 10) AS Date," +
		"   CONCAT(SUBSTRING(CONVERT_TZ(time, '+00:00', '-05:00'), 12, 2),SUBSTRING(CONVERT_TZ(time, '+00:00', '-05:00'), 15, 2)) AS Time," +
		"   'Earthquake' as Type, FORMAT(mag,1) as Magnitude, latitude as Lat, longitude as Lon, place as Location, status as Comments, NULL as County, NULL as State" +
                " FROM WxObs.ANSSQuakes WHERE CONVERT_TZ(time, '+00:00', '-05:00')" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   capevent as Type, NULL as Magnitude, NULL as Lat, NULL as LON, capareaDesc as Location," +
		"   CONCAT(summary, ' <a href=\"', id, '\" target=\"top\">(Link)</a>') AS Comments, NULL AS County, NULL AS State" +
                " FROM WxObs.LiveWarnings WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'SPC Product' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULL as County, NULL as State" +
                " FROM WxObs.SPCOutlooks WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'SPC Product' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULL as County, NULL as State" +
                " FROM WxObs.SPCMesoscale WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'SPC Product' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULL as County, NULL as State" +
                " FROM WxObs.SPCWatches WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'CNN News' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULL as County, NULL as State" +
                " FROM Feeds.NewsFeedCNN WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'KCTV News' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULL as County, NULL as State" +
                " FROM Feeds.NewsFeedKCTV WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'KMBC News' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULl as County, NULL as State" +
                " FROM Feeds.NewsFeedKMBC WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'KSHB News' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   description as Comments, NULL as County, NULL as State" +
                " FROM Feeds.NewsFeedKSHB WHERE GetTime" + timeBetween(xdt1, xdt2) +
                " UNION ALL SELECT" + timeFromGetTime() +
		"   'KC Scout' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, Location as Location," +
		"   Description as Comments, NULL as County, NULL as State" +
                " FROM Feeds.KCScoutIncidents WHERE GetTime" + timeBetween(xdt1, xdt2) +
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'Waze' as Type, NULL as Magnitude, latitude as Lat, longitude as Lon, CONCAT(type, ' - ', street, ' (', nearBy, ')') as Location," +
		"   CONCAT(subtype, ' ', reportDescription) as Comments, NULL as County, NULL as State" +
                " FROM Feeds.WazeFeed WHERE GetTime" + timeBetween(xdt1, xdt2) + 
		" UNION ALL SELECT" + timeFromGetTime() +
		"   'RSS News' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, title as Location," +
		"   CONCAT(description, ' <a href=\", link, \" target=\"top\">', link, '</a>') as Comments, NULL as County, NULL as State" +
                " FROM Feeds.RSSFeeds WHERE GetTime" + timeBetween(xdt1, xdt2) + 
		" UNION ALL SELECT" +
		"   SUBSTRING(CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING(BEGIN_YEARMONTH,1,4),'-',SUBSTRING(BEGIN_YEARMONTH,5,2),'-',BEGIN_DAY,' ',BEGIN_TIME), '%Y-%m-%e %H%i'), '+00:00', '-05:00'),1,10) AS Date," +
		"   SUBSTRING(CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING(BEGIN_YEARMONTH,1,4),'-',SUBSTRING(BEGIN_YEARMONTH,5,2),'-',BEGIN_DAY,' ',BEGIN_TIME), '%Y-%m-%e %H%i'), '+00:00', '-05:00'),12,5) AS Time," +
		"   EVENT_TYPE as Type, MAGNITUDE as Magnitude, BEGIN_LAT as Lat, BEGIN_LON as Lon, CONCAT(BEGIN_LOCATION, '(', CZ_NAME, ')') AS Location," +
		"   CONCAT(EVENT_NARRATIVE,'<br/>',EPISODE_NARRATIVE) AS Comments, CZ_NAME as County, STATE as State" +
                " FROM WxObs.NCDCStormEvents" +
                " WHERE CONVERT_TZ(STR_TO_DATE(CONCAT(SUBSTRING(BEGIN_YEARMONTH,1,4),'-',SUBSTRING(BEGIN_YEARMONTH,5,2),'-',BEGIN_DAY,' ',BEGIN_TIME), '%Y-%m-%e %H%i'), '+00:00', '-05:00')" +
		timeBetween(xdt1, xdt2) +
                " UNION ALL SELECT" + timeFromGetTime() +
		"   'Gmail' as Type, NULL as Magnitude, NULL as Lat, NULL as Lon, FromAddress as Location," +
		"   CONCAT(Subject, '<br/>', Body) AS Comments, NULL as County, NULL as State" +
                " FROM Feeds.Messages WHERE GetTime" + timeBetween(xdt1, xdt2) +
		") as tmp ORDER BY CONCAT(Date,Time) DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_StormReportsByDate, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Time", resultSet.getString("Time"))
                    .put("Type", resultSet.getString("Type"))
                    .put("Magnitude", resultSet.getString("Magnitude"))
                    .put("Lat", resultSet.getString("Lat"))
                    .put("Lon", resultSet.getString("Lon"))
                    .put("Location", resultSet.getString("Location"))
                    .put("Comments", wc.htmlStripTease(resultSet.getString("Comments")))
                    .put("County", resultSet.getString("County"))
                    .put("State", resultSet.getString("State"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } 
        return tContainer;
    }
    
    public JSONArray getUuT(List<String> qParams) {
        final String query_FBook_UU_T = "SELECT CAST((AVG(Average)) AS DECIMAL (10,1)) AS ATF FROM CF6MCI WHERE Date LIKE ?;"; //aGMonV
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_UU_T, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("ATF", resultSet.getDouble("ATF"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
 
    public String setUpdateRainGauge(Connection dbc, List<String> qParams) { return updateRainGauge(dbc, qParams); }
        
}