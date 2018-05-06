/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 6 May 2018
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
    public JSONArray getGfsFha() { return weatherDAO.getGfsFha(); }
    public JSONArray getHeights() { return weatherDAO.getHeights(); }
    public JSONArray getHeightsAll() { return weatherDAO.getHeightsAll(); }
    public JSONArray getHTrackLast(Connection dbc) { return weatherDAO.getHTrackLast(dbc); }
    public JSONArray getHTracks(List qParams) { return weatherDAO.getHTracks(qParams); }
    public JSONArray getJsonModelData(List qParams) { return weatherDAO.getJsonModelData(qParams); }
    public JSONArray getJsonModelLast() { return weatherDAO.getJsonModelLast(); }
    public JSONArray getJsonModelRuns() { return weatherDAO.getJsonModelRuns(); }
    public JSONArray getLiveReports(List inParams) { return weatherDAO.getLiveReports(inParams); }
    public JSONArray getLiveWarnings(List inParams) { return weatherDAO.getLiveWarnings(inParams); }
    public JSONArray getLiveWarningsFipsBounds(List qParams) { return weatherDAO.getLiveWarningsFipsBounds(qParams); }
    public JSONArray getLiveWarningsSameBounds(List qParams) { return weatherDAO.getLiveWarningsSameBounds(qParams); }
    public JSONArray getLiveWatches(List inParams) { return weatherDAO.getLiveWatches(inParams); }
    public JSONArray getLogsXmlWxObs() { return weatherDAO.getLogsXmlWxObs(); }
    public JSONArray getObsJson(Connection dbc, List qParams, List inParams) { return weatherDAO.getObsJson(dbc, qParams, inParams); }
    public JSONArray getObsJsonRapid(Connection dbc, List qParams, List inParams) { return weatherDAO.getObsJsonRapid(dbc, qParams, inParams); }
    public JSONArray getObsJsonLast(Connection dbc) { return weatherDAO.getObsJsonLast(dbc); }
    public JSONArray getObsJsonByStation(Connection dbc, List inParams) { return weatherDAO.getObsJsonByStation(dbc, inParams); }
    public JSONArray getObsJsonStations(Connection dbc, List qParams) { return weatherDAO.getObsJsonStations(dbc, qParams); }
    public JSONArray getObsXmlGeo(Connection dbc) { return weatherDAO.getObsXmlGeo(dbc); }
    public JSONArray getObsXmlReg(Connection dbc) { return weatherDAO.getObsXmlReg(dbc); }
    public JSONArray getRadarList() { return weatherDAO.getRadarList(); }
    public JSONArray getReanalysis(List qParams) { return weatherDAO.getReanalysis(qParams); }
    public JSONArray getSpcLive(Connection dbc, List qParams) { return weatherDAO.getSpcLive(dbc, qParams); }
    public JSONArray getStormReportsByDate(Connection dbc, List inParams) { return weatherDAO.getStormReportsByDate(dbc, inParams); }
    public JSONArray getUuT(List qParams) { return weatherDAO.getUuT(qParams); }
    
}
