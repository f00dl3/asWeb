/*
by Anthony Stump
Created: 24 Sep 2017
Updated: 2 Jan 2020
*/

package asUtilsPorts.Weather;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class xsNDBCUpdate {

	public String getAttributeValue(String attr, Element tElement) throws NullPointerException {
		String nVal = tElement.getAttributes().getNamedItem(attr).getNodeValue();
		if(nVal == null) { return null; }
		return nVal;
	}

	public void main(String xsTmp) {

        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

		final String activeStationURL = "http://www.ndbc.noaa.gov/activestations.xml";
		final File activeFile = new File(xsTmp+"/activestations.xml");

		wc.jsoupOutBinary(activeStationURL, activeFile, 15.0);

		List<String> wxStations = new ArrayList<>();
		final String getStationListSQL = "SELECT Station FROM WxObs.Stations;";
		String addStationTestSQL = "INSERT IGNORE INTO WxObs.Stations (Station, Point, City, State, Active, Priority, Region, DataSource, Frequency) VALUES";

		try ( ResultSet resultSetStations = wc.q2rs1c(dbc, getStationListSQL, null); ) {
                    while (resultSetStations.next()) { wxStations.add(resultSetStations.getString("Station")); } 
                } catch (Exception e) { e.printStackTrace(); }

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.parse(activeFile);
			NodeList nodeList = xmlDoc.getElementsByTagName("station");
	
			if (nodeList != null) {
				for(int i=0; i < nodeList.getLength(); i++) {
					Node thisData = nodeList.item(i);
					if(thisData.getNodeType() == Node.ELEMENT_NODE) {

						int tPri = 6;
						Element tElement = (Element) thisData;	
						String thisStation = null;
						String latitude = null;
						String longitude = null;
						String name = null;

						try { thisStation = getAttributeValue("id", tElement); } catch (NullPointerException npx) { npx.printStackTrace(); }
						try { latitude = getAttributeValue("lat", tElement); } catch (NullPointerException npx) { npx.printStackTrace(); }
						try { longitude = getAttributeValue("lon", tElement); } catch (NullPointerException npx) { npx.printStackTrace(); }
						try { name = getAttributeValue("name", tElement).replace("\'","\\\'").replace(" +", " "); } catch (NullPointerException npx) { npx.printStackTrace(); }

						thisStation = thisStation.toUpperCase();
						if (thisStation.substring(0,1).matches("\\d")) { thisStation = "B"+thisStation; tPri = 6; } else { tPri = 7; }

						if(!wxStations.contains(thisStation) && latitude != null && longitude != null) {
							addStationTestSQL = addStationTestSQL+" ('"+thisStation+"','["+longitude+","+latitude+"]','"+name+"',Null,1,"+tPri+",'BH2','NWS',60),";
						}
					}
				}
			}

		}

		catch (SAXException sex) { sex.printStackTrace(); }
		catch (ParserConfigurationException pcx) { pcx.printStackTrace(); }
		catch (IOException iox) { iox.printStackTrace(); }

		addStationTestSQL = (addStationTestSQL+";").replace(",;",";");
		System.out.println(addStationTestSQL);		
		try { wc.q2do1c(dbc, addStationTestSQL, null); } catch (Exception e) { e.printStackTrace(); }
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}

}
