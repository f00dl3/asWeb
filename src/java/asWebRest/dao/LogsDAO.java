/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 7 Apr 2018
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class LogsDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getCameras(List<String> qParams, String order) {
        final String query_Logs_Cameras = "SELECT * FROM" +
        " (SELECT Date, ImgCount, MP4Size FROM Log_CamsMP4 ORDER BY Date DESC LIMIT ?) as tmp" +
        " ORDER BY Date " + order;
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Logs_Cameras, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("ImgCount", resultSet.getInt("ImgCount"))
                    .put("MP4Size", resultSet.getInt("MP4Size"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
            
    public JSONArray getPlainTextNotes(List<String> qParams) {
        final String query_PlainTextNotes = "SELECT Date, Topic, TextFileName, `Note`" +
                " FROM Core.PlainTextNotes WHERE TextFileName LIKE ?;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_PlainTextNotes, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Topic", resultSet.getString("Topic"))
                    .put("TextFileName", resultSet.getString("TextFileName"))
                    .put("Note", resultSet.getString("Note"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSdUtils() {
        final String query_Logs_SDUtils = "SELECT EventID, Notes, Date, Time, ZIPSize FROM Core.Log_SDUtils" +
                " ORDER BY EventID DESC LIMIT 5;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Logs_SDUtils, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EventID", resultSet.getInt("EventID"))
                    .put("Notes", resultSet.getString("Notes"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Time", resultSet.getString("Time"))
                    .put("ZIPSize", resultSet.getInt("ZIPSize"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSystemBackup() {
        final String query_Logs_SystemBackup = "SELECT Date, Type, Errors, Time, GB_Used, GB_Capacity, OSVersion, rsyncDiff" +
                " FROM Core.SystemBackup ORDER BY Date DESC LIMIT 5;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_Logs_SystemBackup, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Type", resultSet.getString("Type"))
                    .put("Errors", resultSet.getString("Errors"))
                    .put("Time", resultSet.getInt("Time"))
                    .put("GB_Used", resultSet.getInt("GB_Used"))
                    .put("GB_Capacity", resultSet.getInt("GB_Capacity"))
                    .put("OSVersion", resultSet.getString("OSVersion"))
                    .put("rsyncDiff", resultSet.getInt("rsyncDiff"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
