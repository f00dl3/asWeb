/*
by Anthony Stump
Created: 21 Sep 2017
Updated: 19 Dec 2019
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

import asUtils.Shares.MyDBConnector;

public class xsMETARAutoAdd {

	public static String getTagValue(String tag, Element eElement) throws NullPointerException {
		NodeList nl = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nVal = (Node) nl.item(0);
		if(nVal == null) { return null; }
		return nVal.getNodeValue();
	}

	public static void main(String[] args) {

        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

		final String xsTmp = args[0];

		List<String> wxStations = new ArrayList<>();
		final String getStationListSQL = "SELECT Station FROM WxObs.Stations;";
		final File xmlMetarsIn = new File(xsTmp+"/metars.xml");
		String addStationTestSQL = "INSERT IGNORE INTO WxObs.Stations (Station, Point, City, State, Active, Priority, Region, DataSource, Frequency) VALUES";

		try ( ResultSet resultSetStations = mdb.q2rs1c(dbc, getStationListSQL, null); ) {
                    while (resultSetStations.next()) { wxStations.add(resultSetStations.getString("Station")); }
                } catch (Exception e) { e.printStackTrace(); }

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.parse(xmlMetarsIn);
			NodeList nodeList = xmlDoc.getElementsByTagName("METAR");
	
			if (nodeList != null) {
				for(int i=0; i < nodeList.getLength(); i++) {
					Node thisMetar = nodeList.item(i);
					if(thisMetar.getNodeType() == Node.ELEMENT_NODE) {
						Element tElement = (Element) thisMetar;	
						String thisStation = tElement.getElementsByTagName("station_id").item(0).getTextContent();
						String latitude = null;
						String longitude = null;
						try { latitude = getTagValue("latitude", tElement); } catch (NullPointerException npx) { npx.printStackTrace(); }
						try { longitude = getTagValue("longitude", tElement); } catch (NullPointerException npx) { npx.printStackTrace(); }
						if(!wxStations.contains(thisStation) && latitude != null && longitude != null) {
							addStationTestSQL = addStationTestSQL+" ('"+thisStation+"','["+longitude+","+latitude+"]','Auto METAR Add',Null,1,5,'AUT','NWS',60),";
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
		try { mdb.q2do1c(dbc, addStationTestSQL, null); } catch (Exception e) { e.printStackTrace(); }
		
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
	}

}
