/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 11 Jul 2018
 */

package asWebRest.action;

import asWebRest.dao.WeatherDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetWeatherAction {
    
    private WeatherDAO weatherDAO;
    public GetWeatherAction(WeatherDAO weatherDAO) { this.weatherDAO = weatherDAO; }
    
    public JSONArray getAlmanac(Connection dbc) { return weatherDAO.getAlmanac(dbc); }
    public JSONArray getAlmanacHOpt(List qParams) { return weatherDAO.getAlmanacHOpt(qParams); }
    public JSONArray getAlmanacLOpt(List qParams) { return weatherDAO.getAlmanacLOpt(qParams); }
    public JSONArray getAlmanacWxOpt(List qParams) { return weatherDAO.getAlmanacWxOpt(qParams); }
    public JSONArray getAutoStations() { return weatherDAO.getAutoStations(); }
    public JSONArray getCf6Main(Connection dbc, List qParams, String order) { return weatherDAO.getCf6MciMain(dbc, qParams, order); }
    public JSONArray getChXmlWxObs() { return weatherDAO.getChXmlWxObs(); }
    public JSONArray getGfsFha(Connection dbc) { return weatherDAO.getGfsFha(dbc); }
    public JSONArray getHeights(Connection dbc) { return weatherDAO.getHeights(dbc); }
    public JSONArray getHeightsAll() { return weatherDAO.getHeightsAll(); }
    public JSONArray getHTrackLast(Connection dbc) { return weatherDAO.getHTrackLast(dbc); }
    public JSONArray getHTracks(List qParams) { return weatherDAO.getHTracks(qParams); }
    public JSONArray getJsonModelData(Connection dbc, List qParams) { return weatherDAO.getJsonModelData(dbc, qParams); }
    public JSONArray getJsonModelLast(Connection dbc) { return weatherDAO.getJsonModelLast(dbc); }
    public JSONArray getJsonModelRuns(Connection dbc) { return weatherDAO.getJsonModelRuns(dbc); }
    public JSONArray getLiveReports(List inParams) { return weatherDAO.getLiveReports(inParams); }
    public JSONArray getLiveWarnings(Connection dbc, List inParams) { return weatherDAO.getLiveWarnings(dbc, inParams); }
    public JSONArray getLiveWarningsFipsBounds(List qParams) { return weatherDAO.getLiveWarningsFipsBounds(qParams); }
    public JSONArray getLiveWarningsSameBounds(List qParams) { return weatherDAO.getLiveWarningsSameBounds(qParams); }
    public JSONArray getLiveWatches(List inParams) { return weatherDAO.getLiveWatches(inParams); }
    public JSONArray getLogsXmlWxObs() { return weatherDAO.getLogsXmlWxObs(); }
    public JSONArray getObsJson(Connection dbc, List qParams, List inParams) { return weatherDAO.getObsJson(dbc, qParams, inParams); }
    public JSONArray getObsJsonRapid(Connection dbc, List qParams, List inParams) { return weatherDAO.getObsJsonRapid(dbc, qParams, inParams); }
    public JSONArray getObsJsonLast(Connection dbc) { return weatherDAO.getObsJsonLast(dbc); }
    public JSONArray getObsJsonByStation(Connection dbc, List inParams) { return weatherDAO.getObsJsonByStation(dbc, inParams); }
    public JSONArray getObsJsonStations(Connection dbc, List qParams) { return weatherDAO.getObsJsonStations(dbc, qParams); }
    public JSONArray getObsJsonStationCount(Connection dbc) { return weatherDAO.getObsJsonStationCount(dbc); }
    public JSONArray getObsXmlGeo(Connection dbc) { return weatherDAO.getObsXmlGeo(dbc); }
    public JSONArray getObsXmlReg(Connection dbc) { return weatherDAO.getObsXmlReg(dbc); }
    public JSONArray getRadarList(Connection dbc) { return weatherDAO.getRadarList(dbc); }
    public JSONArray getReanalysis(List qParams) { return weatherDAO.getReanalysis(qParams); }
    public JSONArray getSpcLive(Connection dbc, List qParams) { return weatherDAO.getSpcLive(dbc, qParams); }
    public JSONArray getStormReportsByDate(Connection dbc, List inParams) { return weatherDAO.getStormReportsByDate(dbc, inParams); }
    public JSONArray getUuT(List qParams) { return weatherDAO.getUuT(qParams); }
    
}
