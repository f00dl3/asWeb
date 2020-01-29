/*
by Anthony Stump
Created: 16 Jan 2020
Updated: 29 Jan 2020
*/

package asUtilsPorts.Cams;

import java.sql.Connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

public class CamSensors {

	public int getTemperature(String camIp, String camUser, String camPass) {

        Document doc = null;
		int camTemp = -99;
		final String url = "http://"+camIp+":88/cgi-bin/CGIProxy.fcgi?cmd=getTemperatureState&usr="+camUser+"&pwd="+camPass;
		
        try {
            doc = Jsoup.connect(url).timeout(500).get();
            Element testElements = doc.select("degree").first();
            String tempText = testElements.text();            
            camTemp = Integer.parseInt(tempText);
        } catch (Exception e) { }                
		
		return camTemp;
		
	}
	
	public int getTemperatureForSnapshot() {
		JunkyPrivate jp = new JunkyPrivate();
		WebCommon wc = new WebCommon();
		int c3Temp = -99;
		final String cam3Ip = jp.getIpForCam3();
		final String cam3User = "admin";
		final String cam3Pass = "";
		try { c3Temp = (int)wc.tempC2F(getTemperature(cam3Ip, cam3User, cam3Pass)); } catch (Exception e) { }		
		return c3Temp;
	}
	
	public void logTemperature(Connection dbc) {
		
		JunkyPrivate jp = new JunkyPrivate();
		WebCommon wc = new WebCommon();
		
		int c1Temp = -99;
		int c2Temp = -99;
		int c3Temp = -99;
		
		final String cam1Ip = jp.getIpForCam1();
		final String cam2Ip = jp.getIpForCam2();
		final String cam3Ip = jp.getIpForCam3();
		
		final String cam12User = jp.getIpCamUser();
		final String cam12Pass = jp.getIpCamPass();
		final String cam3User = "admin";
		final String cam3Pass = "";

		try { c1Temp = getTemperature(cam1Ip, cam12User, cam12Pass); } catch (Exception e) { }
		try { c2Temp = getTemperature(cam2Ip, cam12User, cam12Pass); } catch (Exception e) { }
		try { c3Temp = getTemperature(cam3Ip, cam3User, cam3Pass); } catch (Exception e) { }

		final String queryAdd1 = "INSERT INTO net_snmp.Foscam1 (TempC) VALUES ('"+c1Temp+"');";
		final String queryAdd2 = "INSERT INTO net_snmp.Foscam2 (TempC) VALUES ('"+c2Temp+"');";
		final String queryAdd3 = "INSERT INTO net_snmp.Foscam3 (TempC) VALUES ('"+c3Temp+"');";

        try { wc.q2do1c(dbc, queryAdd1, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, queryAdd2, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, queryAdd3, null); } catch (Exception e) { e.printStackTrace(); }
		
	}

}
