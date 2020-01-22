/*
by Anthony Stump
Created: 25 May 2018
Updated: 22 Jan 2020
 */

package asWebRest.action;

import asWebRest.dao.WeatherDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateWeatherAction {
    
    final private WeatherDAO weatherDAO;
    public UpdateWeatherAction(WeatherDAO weatherDAO) { this.weatherDAO = weatherDAO; }

    public String setAlertSentCapAlert(Connection dbc, List qParams) { return weatherDAO.setAlertSentCapAlert(dbc, qParams); }
    public String setAlertSentEarthquake(Connection dbc, List qParams) { return weatherDAO.setAlertSentEarthquake(dbc, qParams); }
    public String setUpdateRainGauge(Connection dbc, List qParams) { return weatherDAO.setUpdateRainGauge(dbc, qParams); }

}
