/*
by Anthony Stump
Created: 25 May 2018
 */

package asWebRest.action;

import asWebRest.dao.WeatherDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateWeatherAction {
    
    final private WeatherDAO weatherDAO;
    public UpdateWeatherAction(WeatherDAO weatherDAO) { this.weatherDAO = weatherDAO; }
    
    public String setUpdateRainGauge(Connection dbc, List qParams) { return weatherDAO.setUpdateRainGauge(dbc, qParams); }

}
