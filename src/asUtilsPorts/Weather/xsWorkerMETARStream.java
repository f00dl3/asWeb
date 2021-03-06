/*
by Anthony Stump
Created: 24 Sep 2017
Updated: 2 Jan 2020
*/

package asUtilsPorts.Weather;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.json.*;


public class xsWorkerMETARStream {

	public void main(String xsTmp) {

        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

		final String stationType = "METAR";
		final String region = "None";
		final File jsonOutFile = new File(xsTmp+"/output_"+stationType+"_"+region+".json");
		final File xmlMetarsIn = new File(xsTmp+"/metars.xml");

		List<String> wxStations = new ArrayList<>();
		final String getStationListSQL = "SELECT Station FROM WxObs.Stations WHERE Active=1 AND Priority = 5 ORDER BY Priority, Station DESC;";
		try ( ResultSet resultSetStations = wc.q2rs1c(dbc, getStationListSQL, null); ) { 
            while (resultSetStations.next()) { wxStations.add(resultSetStations.getString("Station")); }
        } catch (Exception e) { e.printStackTrace(); }

		boolean bRawText = false;
		boolean bStationId = false;
		boolean bObservationTime = false;
		boolean bLatitude = false;
		boolean bLongitude = false;
		boolean bTempC = false;
		boolean bDewpointC = false;
		boolean bWindDirDegrees = false;
		boolean bWindSpeedKt = false;
		boolean bVisibilityStatuteMi = false;
		boolean bAltimInHg = false;
		boolean bSeaLevelPressureMb = false;
		boolean bWxString = false;
		boolean bFlightCategory = false;
		boolean bPrecipIn = false;
		int xmlIter = 0;
	
		String thisStation = null;
		String tDewpointC = null;
		String tPressureIn = null;
		String tPressureMb = null;
		String tTempC = null;
		String tTimeString = null;
		String tWeather = null;
		String tWindDegrees = null;
		String tWindSpeed = null;
		String tVisibility = null;
		String tPrecipIn = null;
		String tRawMETAR = null;
		String tFlightCat = null;

		jsonOutFile.delete();

		try {

			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(xmlMetarsIn));

			while(eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();
				switch(event.getEventType()) {

					case XMLStreamConstants.START_ELEMENT:

						StartElement startElement = event.asStartElement();
						String qName = startElement.getName().getLocalPart();

						if(qName.equals("METAR")) { /* System.out.println("Start METAR..."); */ }
						else if (qName.equals("raw_text")) { bRawText = true; }
						else if (qName.equals("station_id")) { bStationId = true; }
						else if (qName.equals("observation_time")) { bObservationTime = true; }
						else if (qName.equals("temp_c")) { bTempC = true; }
						else if (qName.equals("dewpoint_c")) { bDewpointC = true; }
						else if (qName.equals("wind_dir_degrees")) { bWindDirDegrees = true; }
						else if (qName.equals("wind_speed_kt")) { bWindSpeedKt = true; }
						else if (qName.equals("visibility_statute_mi")) { bVisibilityStatuteMi = true; }
						else if (qName.equals("altim_in_hg")) { bAltimInHg = true; }
						else if (qName.equals("sea_level_pressure_mb")) { bSeaLevelPressureMb = true; }
						else if (qName.equals("wx_string")) { bWxString = true; }
						else if (qName.equals("flight_category")) { bFlightCategory = true; }
						else if (qName.equals("precip_in")) { bPrecipIn = true; }
						break;

					case XMLStreamConstants.CHARACTERS:

						Characters characters = event.asCharacters();
						if(bRawText) { tRawMETAR = characters.getData(); bRawText = false; }
						if(bStationId) { thisStation = characters.getData(); bStationId = false; }
						if(bObservationTime) { tTimeString = characters.getData(); bObservationTime = false; }
						if(bTempC) { tTempC = characters.getData(); bTempC = false; }
						if(bDewpointC) { tDewpointC = characters.getData(); bDewpointC = false; }
						if(bWindDirDegrees) { tWindDegrees = characters.getData(); bWindDirDegrees = false; }
						if(bWindSpeedKt) { tWindSpeed = characters.getData(); bWindSpeedKt = false; }
						if(bVisibilityStatuteMi) { tVisibility = characters.getData(); bVisibilityStatuteMi = false; }
						if(bAltimInHg) { tPressureIn = characters.getData(); bAltimInHg = false; }
						if(bSeaLevelPressureMb) { tPressureMb = characters.getData(); bSeaLevelPressureMb = false; }
						if(bWxString) { tWeather = characters.getData(); bWxString = false; }
						if(bFlightCategory) { tFlightCat = characters.getData(); bFlightCategory = false; }
						if(bPrecipIn) { tPrecipIn = characters.getData(); bPrecipIn = false; }
						break;

					case XMLStreamConstants.END_ELEMENT:

						EndElement endElement = event.asEndElement();

						if(endElement.getName().getLocalPart().equals("METAR")) {

							if (wc.isSet(thisStation) && wxStations.contains(thisStation)) {

								JSONObject jStationObj = new JSONObject();
								JSONObject jStationData = new JSONObject();
								jStationObj.put(thisStation, jStationData);

								if (wc.isSet(tTempC)) { jStationData.put("Temperature", tTempC); }
								if (wc.isSet(tDewpointC)) { jStationData.put("Dewpoint", tDewpointC); }
								if (wc.isSet(tPressureMb)) { jStationData.put("Pressure", tPressureMb); }
								if (wc.isSet(tPressureIn)) { jStationData.put("PressureIn", tPressureIn); }
								if (wc.isSet(tTimeString)) { jStationData.put("TimeString", tTimeString); }
								if (wc.isSet(tVisibility)) { jStationData.put("Visibility", tVisibility); }
								if (wc.isSet(tWeather)) { jStationData.put("Weather", tWeather); }
								if (wc.isSet(tWindDegrees)) { jStationData.put("WindDegrees", tWindDegrees); }
								if (wc.isSet(tWindSpeed)) { jStationData.put("WindSpeed", tWindSpeed); }
								if (wc.isSet(tPrecipIn)) { jStationData.put("PrecipIn", tPrecipIn); }
								if (wc.isSet(tRawMETAR)) { jStationData.put("RawMETAR", tRawMETAR); }
								if (wc.isSet(tFlightCat)) { jStationData.put("FlightCategory", tFlightCat); }

								String thisJSONstring = jStationObj.toString().substring(1);
								thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
								if(thisJSONstring.equals("\""+thisStation+"\":{},")) {
									System.out.println("!!! WARN: NO DATA FOR Station "+thisStation+" (METAR Stream Method)!");
								} else {
									try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
									System.out.println(" -> Completed: "+thisStation+" ("+stationType+" Stream)");
								}

							}

							xmlIter++;

						}

						break;


				}

				
			}
		
			
		}
		catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		catch (XMLStreamException xsx) { xsx.printStackTrace(); }
			
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}

}
