/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 26 Jul 2020
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
    public JSONArray getAlmanacHOpt(List<String> qParams) { return weatherDAO.getAlmanacHOpt(qParams); }
    public JSONArray getAlmanacLOpt(List<String> qParams) { return weatherDAO.getAlmanacLOpt(qParams); }
    public JSONArray getAlmanacWxOpt(List<String> qParams) { return weatherDAO.getAlmanacWxOpt(qParams); }
    public JSONArray getAutoStations(Connection dbc) { return weatherDAO.getAutoStations(dbc); }
    public JSONArray getCf6Main(Connection dbc, List<String> qParams, String order) { return weatherDAO.getCf6MciMain(dbc, qParams, order); }
    public JSONArray getChXmlWxObs() { return weatherDAO.getChXmlWxObs(); }
    public String getCountySAME(Connection dbc, String sameCode) { return weatherDAO.getCountySAME(dbc, sameCode); }
    public JSONArray getGfsFha(Connection dbc) { return weatherDAO.getGfsFha(dbc); }
    public JSONArray getHeights(Connection dbc) { return weatherDAO.getHeights(dbc); }
    public JSONArray getHeightsAll() { return weatherDAO.getHeightsAll(); }
    public JSONArray getHTrackLast(Connection dbc) { return weatherDAO.getHTrackLast(dbc); }
    public JSONArray getHTracks(List<String> qParams) { return weatherDAO.getHTracks(qParams); }
    public JSONArray getJsonModelData(Connection dbc, List<String> qParams) { return weatherDAO.getJsonModelData(dbc, qParams); }
    public JSONArray getJsonModelLast(Connection dbc) { return weatherDAO.getJsonModelLast(dbc); }
    public JSONArray getJsonModelRuns(Connection dbc) { return weatherDAO.getJsonModelRuns(dbc); }
    public JSONArray getLastCapAlertForTesting(Connection dbc) { return weatherDAO.getLastCapAlertForTesting(dbc); }
    public JSONArray getLiveReports(Connection dbc, List<String> inParams) { return weatherDAO.getLiveReports(dbc, inParams); }
    public JSONArray getLiveWarnings(Connection dbc, List<String> inParams) { return weatherDAO.getLiveWarnings(dbc, inParams); }
    public JSONArray getLiveWarningsFipsBounds(Connection dbc, List<String> qParams) { return weatherDAO.getLiveWarningsFipsBounds(dbc, qParams); }
    public JSONArray getLiveWarningsSameBounds(Connection dbc, List<String> qParams) { return weatherDAO.getLiveWarningsSameBounds(dbc, qParams); }
    public JSONArray getLiveWatches(Connection dbc, List<String> inParams) { return weatherDAO.getLiveWatches(dbc, inParams); }
    public JSONArray getLogsXmlWxObs(Connection dbc) { return weatherDAO.getLogsXmlWxObs(dbc); }
    public JSONArray getObsJson(Connection dbc, List<String> qParams, List<String> inParams) { return weatherDAO.getObsJson(dbc, qParams, inParams); }
    public JSONArray getObsJsonRapid(Connection dbc, List<String> qParams, List<String> inParams) { return weatherDAO.getObsJsonRapid(dbc, qParams, inParams); }
    public JSONArray getObsJsonLast(Connection dbc) { return weatherDAO.getObsJsonLast(dbc); }
    public JSONArray getObsJsonLastByStation(Connection dbc, List<String> inParams) { return weatherDAO.getObsJsonLastByStation(dbc, inParams); }
    public JSONArray getObsJsonByStation(Connection dbc, List<String> inParams) { return weatherDAO.getObsJsonByStation(dbc, inParams); }
    public JSONArray getObsJsonHome(Connection dbc) { return weatherDAO.getObsJsonHome(dbc); }
    public JSONArray getObsJsonStations(Connection dbc, List<String> qParams) { return weatherDAO.getObsJsonStations(dbc, qParams); }
    public JSONArray getObsJsonStationCount(Connection dbc) { return weatherDAO.getObsJsonStationCount(dbc); }
    public JSONArray getObsXmlGeo(Connection dbc) { return weatherDAO.getObsXmlGeo(dbc); }
    public JSONArray getObsXmlReg(Connection dbc) { return weatherDAO.getObsXmlReg(dbc); }
    public JSONArray getRadarList(Connection dbc) { return weatherDAO.getRadarList(dbc); }
    public JSONArray getRadarSite(Connection dbc, List<String> qParams) { return weatherDAO.getRadarSite(dbc, qParams); }
    public JSONArray getRecentCapAlerts(Connection dbc) { return weatherDAO.getRecentCapAlerts(dbc); }
    public JSONArray getRecentEarthquakes(Connection dbc) { return weatherDAO.getRecentEarthquakes(dbc); }
    public JSONArray getReanalysis(List<String> qParams) { return weatherDAO.getReanalysis(qParams); }
    public JSONArray getSnowReports(Connection dbc) { return weatherDAO.getSnowReports(dbc); }
    public JSONArray getSpcLive(Connection dbc, List<String> qParams) { return weatherDAO.getSpcLive(dbc, qParams); }
    public JSONArray getSpcMesoSent(Connection dbc) { return weatherDAO.getSpcMesoSent(dbc); }
    public JSONArray getSpcOutlookSent(Connection dbc) { return weatherDAO.getSpcOutlookSent(dbc); }
    public JSONArray getSpcWatchSent(Connection dbc) { return weatherDAO.getSpcWatchSent(dbc); }
    public JSONArray getStormReportsByDate(Connection dbc, List<String> inParams) { return weatherDAO.getStormReportsByDate(dbc, inParams); }
    public JSONArray getUuT(List<String> qParams) { return weatherDAO.getUuT(qParams); }
    
}
