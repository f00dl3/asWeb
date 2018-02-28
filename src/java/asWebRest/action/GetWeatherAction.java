/*
by Anthony Stump
Created: 25 Feb 2018
Updated: 27 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.WeatherDAO;
import java.util.List;
import org.json.JSONArray;

public class GetWeatherAction {
    
    private WeatherDAO weatherDAO;
    public GetWeatherAction(WeatherDAO weatherDAO) { this.weatherDAO = weatherDAO; }
    
    public JSONArray getAlmanac() { return weatherDAO.getAlmanac(); }
    public JSONArray getAlmanacHOpt(List qParams) { return weatherDAO.getAlmanacHOpt(qParams); }
    public JSONArray getAlmanacLOpt(List qParams) { return weatherDAO.getAlmanacLOpt(qParams); }
    public JSONArray getAlmanacWxOpt(List qParams) { return weatherDAO.getAlmanacWxOpt(qParams); }
    public JSONArray getAutoStations() { return weatherDAO.getAutoStations(); }
    public JSONArray getChXmlWxObs() { return weatherDAO.getChXmlWxObs(); }
    public JSONArray getGfsFha() { return weatherDAO.getGfsFha(); }
    public JSONArray getHeights() { return weatherDAO.getHeights(); }
    public JSONArray getHeightsAll() { return weatherDAO.getHeightsAll(); }
    public JSONArray getHTrackLast() { return weatherDAO.getHTrackLast(); }
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
    public JSONArray getObsJson(List qParams, List inParams) { return weatherDAO.getObsJson(qParams, inParams); }
    public JSONArray getObsJsonRapid(List qParams, List inParams) { return weatherDAO.getObsJsonRapid(qParams, inParams); }
    public JSONArray getObsJsonLast() { return weatherDAO.getObsJsonLast(); }
    public JSONArray getObsJsonByStation(List inParams) { return weatherDAO.getObsJsonByStation(inParams); }
    public JSONArray getObsJsonStations(List qParams) { return weatherDAO.getObsJsonStations(qParams); }
    public JSONArray getObsXmlGeo() { return weatherDAO.getObsXmlGeo(); }
    public JSONArray getObsXmlReg() { return weatherDAO.getObsXmlReg(); }
    public JSONArray getRadarList() { return weatherDAO.getRadarList(); }
    public JSONArray getReanalysis(List qParams) { return weatherDAO.getReanalysis(qParams); }
    public JSONArray getSpcLive(List qParams) { return weatherDAO.getSpcLive(qParams); }
    public JSONArray getStormReportsByDate(List inParams) { return weatherDAO.getStormReportsByDate(inParams); }
    public JSONArray getUuT(List qParams) { return weatherDAO.getUuT(qParams); }
    
}
