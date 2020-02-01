/*
by Anthony Stump
Created: 4 Oct 2017
Updated: 1 Feb 2020
*/

package asUtilsPorts.Weather;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class ModelShare {	
	
    public JunkyBeans junkyBeans = new JunkyBeans();
    public ModelBeans modelBeans = new ModelBeans();
    
	final public double windDirCalc(double tWUin, double tWVin) { return 57.29578*(Math.atan2(tWUin, tWVin))+180; }
	final public double windSpdCalc(double tWUin, double tWVin) { return Math.sqrt(tWUin*tWUin+tWVin*tWVin)*1.944; }
	final public double calcSLCL(double tTCin, double tRHin) { return (20+(tTCin/5))*(100-tRHin); }
	final public double calcDwpt(double tTCin, double tRHin) { return tTCin-(100-tRHin)/5; }
        
	public String get_xsTmp(boolean isProc) {
		String ret_xsTmp = modelBeans.getDiskSwap().toString();
		if(isProc) { ret_xsTmp = junkyBeans.getRamDrive().toString()+"/xsTmpJ_19"; }
		return ret_xsTmp;
	}
	
	public File get_xsOut(boolean isProc) {
		CommonBeans cb = new CommonBeans();
		File webPath = new File(cb.getPersistTomcat()+"/G2Out/xsOut");
		if(isProc) { webPath = new File(junkyBeans.getWebRoot().toString()+"/G2Out/xsOut"); }
		return webPath;
	}
	
	public String pointInputAsString(String tStation) {
		
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
		final String pointsSQL = "SELECT SUBSTRING(Point, 2, CHAR_LENGTH(Point)-2) AS Coords FROM WxObs.Stations WHERE Station='"+tStation+"' ORDER BY Station DESC;";
		List<String> pointInputArray = new ArrayList<String>();
		try (
			ResultSet resultSetPIA = wc.q2rs1c(dbc, pointsSQL, null)
		) { while (resultSetPIA.next()) { pointInputArray.add(resultSetPIA.getString("Coords")); } }
		catch (Exception e) { e.printStackTrace(); }
		String thisGeo = null;
		String pointInputString = "";
		for (String point : pointInputArray) {
			thisGeo = point.replace(",", " ");
			pointInputString += "-lon "+thisGeo+" ";
		}
		return pointInputString;
	}

	public String filters(String whichOne) {
		
        final File helpers = junkyBeans.getHelpers();
		File filtFile = null;
		String filtData = null;
		Scanner filtScanner = null;

		switch(whichOne) { 
		
			case "g2f": 
				filtFile = new File(helpers.getPath()+"/g2Filters.txt");
				try { filtScanner = new Scanner(filtFile); while(filtScanner.hasNext()) { filtData = filtScanner.nextLine(); } }
				catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
				break;
		
			case "g2fd": 
				filtFile = new File(helpers.getPath()+"/g2FiltersD.txt");
				filtScanner = null; try { filtScanner = new Scanner(filtFile); while(filtScanner.hasNext()) { filtData = filtScanner.nextLine(); } }
				catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
				break;
		
			case "g2fr": 
				filtFile = new File(helpers.getPath()+"/g2FiltersR.txt");
				filtScanner = null; try { filtScanner = new Scanner(filtFile); while(filtScanner.hasNext()) { filtData = filtScanner.nextLine(); } }
				catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
				break;

		}

		return filtData;

	}

	public String jsonMerge(String modelName) {
		WebCommon wc = new WebCommon();
        final File xml2Path = modelBeans.getXml2Path();
		String thisJSON = null;
		try { thisJSON = wc.runProcessOutVar("cat "+xml2Path.getPath()+"/"+modelName+"Out*.json"); } catch (IOException ix) { ix.printStackTrace(); }
		thisJSON = thisJSON.replace("\n","").replace(",}", "}").replace("{,","").replace("{","").replace("}","");
		thisJSON = ("{"+thisJSON+"}").replace(",}","}");
		return thisJSON;
	}
		
}
