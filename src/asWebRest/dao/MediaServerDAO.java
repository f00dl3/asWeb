/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 30 Apr 2019
 */

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MediaServerDAO {

    WebCommon wc = new WebCommon();
    
    public JSONArray getGeoData(Connection dbc) {
        final String query_MediaServer_GeoData = "SELECT DISTINCT GeoData FROM MediaServer WHERE GeoData IS NOT NULL;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_GeoData, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("GeoData", resultSet.getString("GeoData"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getIndexed(Connection dbc, List qParams, String isMobile) {
        final String query_MediaServer_Indexed = "SELECT" +
                " Path, Size, DurSec, File, Description, ContentDate, Artist, AlbumArt," +
                " SUBSTRING(Description,1,48) AS DescriptionLimited," +
                " LastSelected, PlayCount, Burned, BDate, Media, Working, OffDisk, Archived," +
                " BitRate, Hz, Channels, Resolution, Pages, MPAA, MPAAContent, XTags, XTagVer, GeoData, GIFVer," +
                " WarDeploy, DateIndexed, TrackListingASON, AltDisk," +
                " CASE WHEN DateIndexed BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE() THEN 1 ELSE 0 END AS NewFlag" +
                " FROM Core.MediaServer" +
                " WHERE (1=? OR Adult=?)" + // AdultTest, Adult
                " AND (1=? OR Path LIKE ?);"; // TestPath, Test
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_Indexed, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                if(isMobile.equals("yes")) {
                    tObject.put("Description", resultSet.getString("DescriptionLimited"));
                } else {
                    tObject.put("Description", resultSet.getString("Description"));
                }
                tObject
                    .put("Path", resultSet.getString("Path"))
                    .put("Size", resultSet.getLong("Size"))
                    .put("DurSec", resultSet.getInt("DurSec"))
                    .put("File", resultSet.getString("File"))
                    .put("ContentDate", resultSet.getString("ContentDate"))
                    .put("Artist", resultSet.getString("Artist"))
                    .put("AlbumArt", resultSet.getString("AlbumArt"))
                    .put("Description", resultSet.getString("Description"))
                    .put("LastSelected", resultSet.getString("LastSelected"))
                    .put("PlayCount", resultSet.getString("PlayCount"))
                    .put("Burned", resultSet.getInt("Burned"))
                    .put("BDate", resultSet.getString("BDate"))
                    .put("Media", resultSet.getString("Media"))
                    .put("Working", resultSet.getInt("Working"))
                    .put("OffDisk", resultSet.getInt("OffDisk"))
                    .put("Archived", resultSet.getString("Archived"))
                    .put("BitRate", resultSet.getInt("BitRate"))
                    .put("Hz", resultSet.getInt("Hz"))
                    .put("Channels", resultSet.getInt("Channels"))
                    .put("Resolution", resultSet.getString("Resolution"))
                    .put("Pages", resultSet.getInt("Pages"))
                    .put("MPAA", resultSet.getString("MPAA"))
                    .put("MPAAContent", resultSet.getString("MPAAContent"))
                    .put("XTags", resultSet.getString("XTags"))
                    .put("XTagVer", resultSet.getInt("XTagVer"))
                    .put("GeoData", resultSet.getString("GeoData"))
                    .put("GIFVer", resultSet.getInt("GIFVer"))
                    .put("WarDeploy", resultSet.getInt("WarDeploy"))
                    .put("DateIndexed", resultSet.getString("DateIndexed"))
                    .put("TrackListingASON",resultSet.getString("TrackListingASON"))
                    .put("AltDisk", resultSet.getInt("AltDisk"))
                    .put("NewFlag", resultSet.getInt("NewFlag"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getIndexedByDate(Connection dbc) {
        final String query_IndxedByDate = "SELECT DateIndexed, COUNT(File) AS Hits" +
        		" FROM Core.MediaServer" +
        		" WHERE DateIndexed IS NOT NULL" +
        		" AND DateIndexed BETWEEN CURDATE() - INTERVAL 365 DAY AND CURDATE()" +
        		" GROUP BY DateIndexed" +
    			" ORDER BY DateIndexed;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_IndxedByDate, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("DateIndexed"))
                    .put("Hits", resultSet.getLong("Hits"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }      
    
    public JSONArray getOverview(Connection dbc) {
        final String query_MediaServer_Overview = "SELECT COUNT(File) AS TotalRecords, SUM(PlayCount) AS TotalPlays," +
                " SUM(DurSec) AS TotalDurSec, SUM(Size) AS TotalBlocks," +
                " (SELECT COUNT(File) FROM Core.MediaServer WHERE DateIndexed = CURDATE()) AS NewToday," +
                " (SELECT COUNT(File) FROM Core.MediaServer WHERE DateIndexed BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE()) AS NewMonth," +
                " (SELECT COUNT(File) FROM Core.MediaServer WHERE DateIndexed BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE()) AS NewWeek," +
                " (SELECT COUNT(File) FROM Core.MediaServer WHERE LastSelected = CURDATE()) AS PlayedToday," +
                " (SELECT COUNT(File) FROM Core.MediaServer WHERE LastSelected BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE()) AS PlayedMonth," +
                " (SELECT COUNT(File) FROM Core.MediaServer WHERE LastSelected BETWEEN CURDATE() - INTERVAL 7 DAY AND CURDATE()) AS PlayedWeek" +
                " FROM Core.MediaServer;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_Overview, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("TotalRecords", resultSet.getInt("TotalRecords"))
                    .put("PlayCount", resultSet.getInt("TotalPlays"))
                    .put("TotalDurSec", resultSet.getLong("TotalDurSec"))
                    .put("TotalBlocks", resultSet.getLong("TotalBlocks"))
                    .put("NewToday", resultSet.getInt("NewToday"))
                    .put("NewWeek", resultSet.getInt("NewWeek"))
                    .put("NewMonth", resultSet.getInt("NewMonth"))
                    .put("PlayedToday", resultSet.getInt("PlayedToday"))
                    .put("PlayedWeek", resultSet.getInt("PlayedWeek"))
                    .put("PlayedMonth", resultSet.getInt("PlayedMonth"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getPowerRangers(Connection dbc) {
        final String query_MediaServer_PowerRangers = "SELECT Series, Season, SeasonEp, SeriesEp, ProdCode, AirDate," +
                " Title, Synopsis, MediaServer FROM Core.PowerRangers ORDER BY SeriesEp DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_PowerRangers, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Series", resultSet.getInt("Series"))
                    .put("Season", resultSet.getInt("Season"))
                    .put("SeasonEp", resultSet.getInt("SeasonEp"))
                    .put("SeriesEp", resultSet.getInt("SeriesEp"))
                    .put("ProdCode", resultSet.getString("ProdCode"))
                    .put("AirDate", resultSet.getString("AirDate"))
                    .put("Title", resultSet.getString("Title"))
                    .put("Synopsis", resultSet.getString("Synopsis"))
                    .put("MediaServer", resultSet.getInt("MediaServer"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getXFiles(Connection dbc) {
        final String query_MediaServer_XFiles = "SELECT" +
        " Path, Size, DurSec," +
        " File, Description, AlbumArt," +
        " LastSelected, PlayCount," +
        " Burned, BDate, Media" +
        " FROM Core.MediaServer WHERE Description LIKE '%XFilesTV%'" +
        " ORDER BY ContentDate DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_XFiles, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Path", resultSet.getString("Path"))
                    .put("Size", resultSet.getLong("Size"))
                    .put("DurSec", resultSet.getInt("DurSec"))
                    .put("AlbumArt", resultSet.getString("AlbumArt"))
                    .put("Description", resultSet.getString("Description"))
                    .put("File", resultSet.getString("File"))
                    .put("LastSelected", resultSet.getString("LastSelected"))
                    .put("PlayCount", resultSet.getInt("PlayCount"))
                    .put("Burned", resultSet.getInt("Burned"))
                    .put("BDate", resultSet.getString("BDate"))
                    .put("Media", resultSet.getString("Media"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public String setLastPlayed(Connection dbc, List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        final String query_MediaServer_LastPlayed = "UPDATE Core.MediaServer" +
                " SET LastSelected=CURDATE(), PlayCount=PlayCount+1 WHERE File = ?;";
        try { returnData = wc.q2do1c(dbc, query_MediaServer_LastPlayed, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
}
