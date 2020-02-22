/*
by Anthony Stump
Created: 29 Aug 2017
Updated: 22 Feb 2020
*/

package asUtilsPorts.Weather;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.action.GetWeatherAction;
import asWebRest.dao.WeatherDAO;
import asWebRest.shared.CommonBeans;

public class RadarImageProcessor {
	
	public static String bindRadarImageGeocoded(Connection dbc, String station, File fileToAppendTo, JSONArray boundConstraint) {
		
		String returnData = "ENTERING CALCULATIONS";
		
		CommonBeans cb = new CommonBeans();
		GetWeatherAction getWeatherAction = new GetWeatherAction(new WeatherDAO());
		RadarBeans rb = new RadarBeans();

		final File tRadarFile = new File(cb.getPersistTomcat().toString() + "/Radar/" + station + "/_BLoop.gif");
		
		String tCoords = "";
		final double rW = (double) rb.getRadW();
		final double rH = (double) rb.getRadH();
		
		List<String> qParams = new ArrayList<>();
		qParams.add(station);		
		
		try {
			JSONArray tData = getWeatherAction.getRadarSite(dbc, qParams);
			for(int i = 0; i < tData.length(); i++) {
				JSONObject tSite = tData.getJSONObject(i);
				tCoords = tSite.getString("boundsNSEW");			
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}

		final JSONArray bounds = new JSONArray(tCoords);
		final double bN = bounds.getDouble(0);
		final double bS = bounds.getDouble(1);
		final double bE = bounds.getDouble(2);
		final double bW = bounds.getDouble(3);
		
		final double diffNS = bN - bS;
		final double diffWE = bW - bE;
		
		final double pixelsPerDegreeNS = diffNS / rH;
		final double pixelsPerDegreeWE = diffWE / rW;
		
		final double cN = boundConstraint.getDouble(0);
		final double cS = boundConstraint.getDouble(1);
		final double cE = boundConstraint.getDouble(2);
		final double cW = boundConstraint.getDouble(3);
		
		final double diffConstToMapN = bN - cN;
		final double diffConstToMapS = bS - cS;
		final double diffConstToMapE = bE - cE;
		final double diffConstToMapW = bW - cW;

		final double pixCropN = diffConstToMapN * pixelsPerDegreeNS;
		final double pixCropS = diffConstToMapS * pixelsPerDegreeNS;
		final double pixCropE = diffConstToMapE * pixelsPerDegreeWE;
		final double pixCropW = diffConstToMapW * pixelsPerDegreeWE;
		
		returnData += "\n Crop N : " + pixCropN +
				"\n Crop S : " + pixCropS +
				"\n Crop E : " + pixCropE +
				"\n Crop W : " + pixCropW;
		
		// Get image, get bounds, crop based on bounds, return fragment to the re-overly on warn image.
		// Take width/height of image minus difference in latitudes/longitudes to calculate per pixel lat diff
		
		return returnData;
		
	}

}