/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 22 Feb 2018
 */

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MediaServerDAO {

    WebCommon wc = new WebCommon();
    
    public JSONArray getGeoData() {
        final String query_MediaServer_GeoData = "SELECT DISTINCT GeoData FROM MediaServer WHERE GeoData IS NOT NULL;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_MediaServer_GeoData, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("GeoData", resultSet.getString("GeoData"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getIndexed(List qParams) {
        final String query_MediaServer_Indexed = "SELECT" +
                " Path, Size, DurSec, File, Description, ContentDate, Artist, AlbumArt," +
                " SUBSTRING(Description,1,128) AS DescriptionLimited," +
                " LastSelected, PlayCount, Burned, BDate, Media, Working, OffDisk, Archived," +
                " BitRate, Hz, Channels, Resolution, Pages, MPAA, MPAAContent, XTags, XTagVer, GeoData, GIFVer," +
                " WarDeploy, DateIndexed, TrackListingASON" +
                " FROM Core.MediaServer" +
                " WHERE (1=? OR Adult=?)" + // AdultTest, Adult
                " AND (1=? OR Path LIKE ?);"; // TestPath, Test
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_MediaServer_Indexed, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Path", resultSet.getString("Path"))
                    .put("Size", resultSet.getLong("Size"))
                    .put("DurSec", resultSet.getInt("DurSec"))
                    .put("File", resultSet.getString("File"))
                    .put("ContentDate", resultSet.getString("ContentDate"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Artist", resultSet.getString("Artist"))
                    .put("AlbumArt", resultSet.getString("AlbumArt"))
                    .put("DescriptionLimited", resultSet.getString("DescriptionLimited"))
                    .put("LastSelected", resultSet.getString("LastSelected"))
                    .put("PlayCount", resultSet.getString("PlayCount"))
                    .put("Burned", resultSet.getInt("Burned"))
                    .put("BDate", resultSet.getString("BDate"))
                    .put("Media", resultSet.getString("Media"))
                    .put("Working", resultSet.getInt("Working"))
                    .put("OffDisk", resultSet.getInt("OffDisk"))
                    .put("Archived", resultSet.getInt("Archived"))
                    .put("WarDeploy", resultSet.getInt("WarDeploy"))
                    .put("DateIndexed", resultSet.getString("DateIndexed"))
                    .put("TrackListingASON",resultSet.getString("TrackListingASON"));                    
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
      
    public JSONArray getOverview() {
        final String query_MediaServer_Overview = "SELECT COUNT(File) AS TotalRecords, SUM(PlayCount) AS TotalPlays," +
                " SUM(DurSec) AS TotalDurSec, SUM(Size) AS TotalBlocks FROM Core.MediaServer;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_MediaServer_Overview, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("TotalRecords", resultSet.getInt("TotalRecords"))
                    .put("PlayCount", resultSet.getInt("TotalPlays"))
                    .put("TotalDurSec", resultSet.getLong("TotalDurSec"))
                    .put("TotalBlocks", resultSet.getLong("TotalBlocks"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getPowerRangers() {
        final String query_MediaServer_PowerRangers = "SELECT Series, Season, SeasonEp, SeriesEp, ProdCode, AirDate," +
                " Title, Synopsis, MediaServer FROM Core.PowerRangers ORDER BY SeriesEp DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_MediaServer_PowerRangers, null);
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
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getXFiles() {
        final String query_MediaServer_XFiles = "SELECT" +
        " Path, Size, DurSec," +
        " File, Description, AlbumArt," +
        " LastSelected, PlayCount," +
        " Burned, BDate, Media" +
        " FROM Core.MediaServer WHERE Description LIKE '%XFilesTV%'" +
        " ORDER BY ContentDate DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_MediaServer_XFiles, null);
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
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public String setLastPlayed() {
        final String query_MediaServer_LastPlayed = "UPDATE Core.MediaServer" +
                " SET LastSelected=CURDATE(), PlayCount=PlayCount+1 WHERE File LIKE ?;";
        /* try {
            ResultSet resultSet = wc.q2rs(query_MediaServer_Overview, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("TotalRecords", resultSet.getInt("TotalRecords"))
                    .put("PlayCount", resultSet.getInt("TotalPlays"))
                    .put("TotalDurSec", resultSet.getLong("TotalDurSec"))
                    .put("TotalBlocks", resultSet.getLong("TotalBlocks"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); } */
        return "placeholder for query" + query_MediaServer_LastPlayed;
    }
    
}
