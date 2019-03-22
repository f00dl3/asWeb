/* 
by Anthony Stump
Created: 4 Sep 2017
Ported to asWeb: 10 Feb 2019
Updated: 21 Mar 2019
*/

package asUtilsPorts;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import asUtils.Shares.MyDBConnector;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class GPSParse {

	public static void main(String[] args) {

                JunkyBeans junkyBeans = new JunkyBeans();
		final String dropLocation = junkyBeans.getDesktopPath().toString();
                final String appName = junkyBeans.getApplicationName();
                final String sourceType = args[2];
		final Path gpsInFile = Paths.get(dropLocation+"/"+args[0]+"."+sourceType.toLowerCase());
		final String thisDate = args[0].substring(0, 10);
		final String bicycle = junkyBeans.getBicycle();
		final String legacyFlag = args[1];
                final long garminTrainTimeOffset = 631065600;
                if(args.length != 3) { System.out.println("Please enter args: [fileName] [legacyFlag] [sourceType]"); System.exit(0); }
		String fullGPSjson = "";
		String geoJSONtrace = "";

		System.out.println(appName+".GPSParse\nFile: "+gpsInFile.toString());

		String line = "";
	
		List<Double> intSpeeds = new ArrayList<>();
		List<Double> miles = new ArrayList<>();
		List<Double> wattPowers = new ArrayList<>();
		List<Integer> cadences = new ArrayList<>();
		List<Integer> heartRates = new ArrayList<>();
		List<Integer> logNos = new ArrayList<>();
		List<Long> trackedSeconds = new ArrayList<>();
                JSONArray hrvArray = new JSONArray();
                
                if(sourceType.toLowerCase().equals("csv") || sourceType.toLowerCase().equals("json")) {
                    
                    try (BufferedReader br = Files.newBufferedReader(gpsInFile)) {
                            String firstLine = br.readLine();
                            int jsonLogIterator = 0;

                            while ((line = br.readLine()) != null) {

                                if(sourceType.toLowerCase().equals("csv")) {

                                    String[] thisLine = line.split(";");

                                    String logNo = thisLine[0];
                                    logNos.add(Integer.parseInt(logNo));

                                    double latitude = 0.0;
                                    double longitude = 0.0;

                                    JSONObject gpsLog = new JSONObject();
                                    JSONObject gpsData = new JSONObject();
                                    gpsLog.put(logNo, gpsData);

                                    if(StumpJunk.isSetNotZero(thisLine[1])) { double altFt = StumpJunk.meters2Feet(Integer.parseInt(thisLine[1])/1000); gpsData.put("AltitudeFt", altFt); }
                                    if(StumpJunk.isSetNotZero(thisLine[2])) { double altDiffDownFt = StumpJunk.meters2Feet(Integer.parseInt(thisLine[2]) / 1000); gpsData.put("AltDiffDownFt", altDiffDownFt); }
                                    if(StumpJunk.isSetNotZero(thisLine[3])) { double altDiffUpFt = StumpJunk.meters2Feet(Integer.parseInt(thisLine[3]) / 1000); gpsData.put("AltDiffUpFt", altDiffUpFt); }
                                    if(StumpJunk.isSetNotZero(thisLine[4])) { int cadence = Integer.parseInt(thisLine[4]); gpsData.put("Cadence", cadence); cadences.add(cadence); }
                                    if(StumpJunk.isSetNotZero(thisLine[5])) { double kCal = Double.parseDouble(thisLine[5]); gpsData.put("kcal", kCal); }
                                    if(StumpJunk.isSetNotZero(thisLine[6])) { double distAbsMi = (Double.parseDouble(thisLine[6])/1000)*0.621; gpsData.put("DistTotMiles", distAbsMi); miles.add(distAbsMi); }
                                    if(StumpJunk.isSetNotZero(thisLine[7])) { double distInt = Double.parseDouble(thisLine[7]); gpsData.put("DistIntMeters", distInt); }
                                    if(StumpJunk.isSetNotZero(thisLine[8])) { double distIntDown = Double.parseDouble(thisLine[8]); gpsData.put("DistIntDownFt", StumpJunk.meters2Feet(distIntDown)); }
                                    if(StumpJunk.isSetNotZero(thisLine[9])) { double distIntUp = Double.parseDouble(thisLine[9]); gpsData.put("DistIntUpFt", StumpJunk.meters2Feet(distIntUp)); }
                                    if(StumpJunk.isSetNotZero(thisLine[10])) { int heartrate = Integer.parseInt(thisLine[10]); gpsData.put("HeartRate", heartrate); heartRates.add(heartrate); }
                                    if(StumpJunk.isSetNotZero(thisLine[11])) { double incline = Double.parseDouble(thisLine[11]); gpsData.put("Incline", incline); }
                                    if(StumpJunk.isSetNotZero(thisLine[12])) { int iZone = Integer.parseInt(thisLine[12]); gpsData.put("IntensityZone", iZone); }
                                    if(StumpJunk.isSetNotZero(thisLine[13])) { latitude = Double.parseDouble(thisLine[13]); gpsData.put("Latitude", latitude);  }
                                    if(StumpJunk.isSetNotZero(thisLine[14])) { longitude = Double.parseDouble(thisLine[14]); gpsData.put("Longitude", longitude); }
                                    if(StumpJunk.isSetNotZero(thisLine[15])) { double pHRMax = Double.parseDouble(thisLine[15]); gpsData.put("PercentHRMax", pHRMax); }
                                    if(StumpJunk.isSetNotZero(thisLine[16])) { double powerWatts = Double.parseDouble(thisLine[16]); gpsData.put("PowerWatts", powerWatts); wattPowers.add(powerWatts); }
                                    if(StumpJunk.isSetNotZero(thisLine[17])) { double pWRatio = Double.parseDouble(thisLine[17]); gpsData.put("PowerWeightRatio", pWRatio); }
                                    if(StumpJunk.isSetNotZero(thisLine[18])) { double riseRate = Double.parseDouble(thisLine[18]); gpsData.put("RiseRate", riseRate); }
                                    if(StumpJunk.isSetNotZero(thisLine[19])) { double speedMPH = Double.parseDouble(thisLine[19])*2.237; gpsData.put("SpeedMPH", speedMPH); intSpeeds.add(speedMPH); }
                                    if(StumpJunk.isSetNotZero(thisLine[20])) { String speedRef = thisLine[20]; gpsData.put("SpeedSource", speedRef); }
                                    if(StumpJunk.isSetNotZero(thisLine[21])) { int speedTime = Integer.parseInt(thisLine[21]); gpsData.put("SpeedTime", speedTime); }
                                    if(StumpJunk.isSetNotZero(thisLine[22])) { int targetZone = Integer.parseInt(thisLine[22]); gpsData.put("TargetZone", targetZone); }
                                    if(StumpJunk.isSetNotZero(thisLine[23])) { double tempF = StumpJunk.tempC2F(Double.parseDouble(thisLine[23])); gpsData.put("TemperatureF", tempF); }
                                    if(StumpJunk.isSetNotZero(thisLine[24])) { long trainTime = Integer.parseInt(thisLine[24]); gpsData.put("TrainingTime", trainTime); }
                                    if(StumpJunk.isSetNotZero(thisLine[25])) { long trainTimeTot = Integer.parseInt(thisLine[25]); gpsData.put("TrainingTimeTotalSec", trainTimeTot); trackedSeconds.add(trainTimeTot); }
                                    if(StumpJunk.isSetNotZero(thisLine[26])) { long trainTimeDown = Integer.parseInt(thisLine[26]); gpsData.put("TrainingTimeDownhillSec", trainTimeDown); }
                                    if(StumpJunk.isSetNotZero(thisLine[27])) { long trainTimeUp = Integer.parseInt(thisLine[27]); gpsData.put("TrainingTimeUphillSec", trainTimeUp); }
                                    if(StumpJunk.isSetNotZero(thisLine[28])) { int workKJ = Integer.parseInt(thisLine[28]); gpsData.put("WorkKJ", workKJ); }
                                    if(StumpJunk.isSetNotZero(thisLine[29])) { int powerZone = Integer.parseInt(thisLine[29]); gpsData.put("PowerZone", powerZone); }

                                    geoJSONtrace += "["+longitude+","+latitude+"],";				

                                    String thisJSONstring = gpsLog.toString().substring(1);
                                    thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
                                    if(thisJSONstring.equals("\""+logNo+"\":{},")) {
                                            System.out.println("Empty JSON!");
                                    } else {
                                            fullGPSjson += thisJSONstring;
                                    }

                                }

                                if(sourceType.toLowerCase().equals("json")) {

                                    jsonLogIterator++;
                                    String logNo = String.valueOf(jsonLogIterator);
                                    logNos.add(Integer.parseInt(logNo));

                                    double latitude = 0.0;
                                    double longitude = 0.0;

                                    JSONObject gpsLog = new JSONObject();
                                    JSONObject gpsData = new JSONObject();
                                    gpsLog.put(logNo, gpsData);

                                    JSONObject lineJSON = new JSONObject(line);
                                    System.out.println(lineJSON.toString());
                                    try { double altFt = Double.parseDouble(lineJSON.getString("AltitudeFt")); gpsData.put("AltitudeFt", altFt); } catch (JSONException jx) { jx.printStackTrace(); }
                                    try { float distIntFt = Float.parseFloat(lineJSON.getString("IntervalDistance")); gpsData.put("DistIntFt", distIntFt); } catch (JSONException jx) { jx.printStackTrace(); }
                                    try { latitude = Double.parseDouble(lineJSON.getString("Latitude")); gpsData.put("Latitude", latitude); } catch (JSONException jx) { jx.printStackTrace(); }
                                    try { longitude = Double.parseDouble(lineJSON.getString("Longitude")); gpsData.put("Longitude", longitude); } catch (JSONException jx) { jx.printStackTrace(); }
                                    try { double distAbsMi = (double) lineJSON.getFloat("DistanceTotalMi"); gpsData.put("DistTotMiles", distAbsMi); miles.add(distAbsMi); } catch (JSONException jx) { jx.printStackTrace(); }
                                    try { double speedMPH = (double) lineJSON.getFloat("SpeedMPH"); gpsData.put("SpeedMPH", speedMPH); intSpeeds.add(speedMPH); } catch (JSONException jx) { jx.printStackTrace(); }
                                    try { long trainTimeTot = (long) Math.round(Double.parseDouble(lineJSON.getString("ElapsedSec"))); gpsData.put("TrainingTimeTotalSec", trainTimeTot); trackedSeconds.add(trainTimeTot); } catch (JSONException jx) { jx.printStackTrace(); }

                                    geoJSONtrace += "["+longitude+","+latitude+"],";				

                                    String thisJSONstring = gpsLog.toString().substring(1);
                                    thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
                                    if(thisJSONstring.equals("\""+logNo+"\":{},")) {
                                            System.out.println("Empty JSON!");
                                    } else {
                                            fullGPSjson += thisJSONstring;
                                    }

                                }
                        

                        }
                    }
                    catch (IOException ix) { ix.printStackTrace(); }
                        
                }
		
                if(sourceType.toLowerCase().equals("gpx")) {
                    
                    int gpxLogIterator = 0;
                    
                    try {
                        
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(gpsInFile.toString());
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
                        NodeList nList = doc.getElementsByTagName("trkpt");
                        
                        for (int temp = 0; temp < nList.getLength(); temp++) {
                        
                            Node nNode = nList.item(temp);

                            gpxLogIterator++;
                            String logNo = String.valueOf(gpxLogIterator);
                            logNos.add(Integer.parseInt(logNo));

                            double latitude = 0.0;
                            double longitude = 0.0;
                            double altFt = 0.0;
                            double tempF = 0.0;
                            String trainTime = "";

                            JSONObject gpsLog = new JSONObject();
                            JSONObject gpsData = new JSONObject();
                            gpsLog.put(logNo, gpsData);
                            
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                
                                Element eElement = (Element) nNode;
                                
                                latitude = Double.parseDouble(eElement.getAttribute("lat"));
                                longitude = Double.parseDouble(eElement.getAttribute("lon"));
                                altFt = StumpJunk.meters2Feet(Double.parseDouble(eElement.getElementsByTagName("ele").item(0).getTextContent()));
                                tempF = StumpJunk.tempC2F(Double.parseDouble(eElement.getElementsByTagName("ns3:atemp").item(0).getTextContent())); 
                                trainTime = eElement.getElementsByTagName("time").item(0).getTextContent();
                                                                
                                gpsData.put("Latitude", latitude);
                                gpsData.put("Longitude", longitude);
                                gpsData.put("AltitudeFt", altFt);
                                gpsData.put("TemperatureF", tempF);
                                gpsData.put("TrainingTime", trainTime);
                                
                                
                            }
                                                 
                            geoJSONtrace += "["+longitude+","+latitude+"],";		       
                            String thisJSONstring = gpsLog.toString().substring(1);
                            thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
                            if(thisJSONstring.equals("\""+logNo+"\":{},")) {
                                    System.out.println("Empty JSON!");
                            } else {
                                    fullGPSjson += thisJSONstring;
                            }

                        
                        }
                        
                    } catch (Exception e) {
                        
                        e.printStackTrace();
                        
                    }
                    

                }
                
                if(sourceType.toLowerCase().equals("fit")) {
                    
                    System.out.println("DEBUG WebApp path: " + junkyBeans.getPathWebappCache());
                    File csvOutFile = new File(gpsInFile.toString() + ".csv");
                    if(!(junkyBeans.getPathWebappCache()).contains("null")) { csvOutFile = new File(junkyBeans.getPathWebappCache()+"/"+args[0]+".csv"); }
                    final File fitCsvToolJar = new File(junkyBeans.getHelpers().toString()+"/FitCSVTool.jar");
                    
                    StumpJunk.runProcess("java -jar "+fitCsvToolJar+" -b "+gpsInFile.toString()+" "+csvOutFile.toString());
                    
                    int jsonLogIterator = 0;
                    long initTrainTime = 0;
                    long trainTimeTot = 0;

                    Scanner csvScanner = null; try {			
                        csvScanner = new Scanner(csvOutFile);
                        while(csvScanner.hasNext()) {			
                            
                            String sLine = csvScanner.nextLine();
                            Matcher mainMatcher = Pattern.compile("Data.*record.*timestamp").matcher(sLine);
                            
                            if(mainMatcher.find()) {
                                
                                jsonLogIterator++;

                                boolean errorHitOnLatLon = false;
                                double latitude = 0.0;
                                double longitude = 0.0;
                                String logNo = Integer.toString(jsonLogIterator);
                                logNos.add(jsonLogIterator);
                                JSONObject gpsLog = new JSONObject();
                                JSONObject gpsData = new JSONObject();
                                gpsLog.put(logNo, gpsData);

                                String thisLine[] = sLine.split(",");
                                
                                int cPr = 3;
                                int tRec = 0;
                                int unks = 0;
                                
                                for (int i = 0; i < (thisLine.length-3); i = i + cPr) {
                                    
                                    try {
                                        
                                        String thisDataSet = "tRec: " + tRec + ", i: " + i + " || " +
                                                "p: " + ((tRec*cPr)+0) + ", d: " + (thisLine[(tRec*cPr)+0]) + " [k] | " +
                                                "p: " + ((tRec*cPr)+1) + ", d: " + (thisLine[(tRec*cPr)+1]) + " [v] | " +
                                                "p: " + ((tRec*cPr)+2) + ", d: " + (thisLine[(tRec*cPr)+2]) + " [u];";
                                        String thisSetKey = (thisLine[(tRec*cPr)+0]);
                                        String thisSetValue = (thisLine[(tRec*cPr)+1].replaceAll("\"",""));

                                        gpsData.put("SpeedSource", "gps");

                                        if(StumpJunk.isSetNotZero(thisSetValue)) {

                                            switch(thisSetKey) {

                                                case "timestamp": 
                                                    long trainTime = Long.parseLong(thisSetValue)*100;
                                                    long eTrainTime = garminTrainTimeOffset + trainTime;
                                                    switch(jsonLogIterator) {
                                                        case 1: case 2: initTrainTime = trainTime; trainTimeTot = 0; break;
                                                        default: trainTimeTot = trainTime - initTrainTime; break;
                                                    }
                                                    gpsData.put("GarminTime", trainTime);
                                                    gpsData.put("TrainingTime", eTrainTime);
                                                    gpsData.put("TrainingTimeTotalSec", trainTimeTot);
                                                    trackedSeconds.add(trainTimeTot);
                                                    break;

                                                case "position_lat":
                                                    //System.out.println(thisSetValue); 
                                                    if(thisSetValue.contains("E-")) { errorHitOnLatLon = true; }
                                                    latitude = (Double.parseDouble(thisSetValue))*(180/Math.pow(2,31));
                                                    gpsData.put("Latitude", latitude); 
                                                    break;

                                                case "position_long":
                                                    //System.out.println(thisSetValue); 
                                                    longitude = (Double.parseDouble(thisSetValue))*(180/Math.pow(2,31));
                                                    if(thisSetValue.contains("E-")) { errorHitOnLatLon = true; }
                                                    gpsData.put("Longitude", longitude);
                                                    break;

                                                case "distance":
                                                    double distAbsMi = ((Double.parseDouble(thisSetValue))/1000)*0.621;
                                                    gpsData.put("DistTotMiles", distAbsMi);
                                                    miles.add(distAbsMi);
                                                    break;

                                                case "altitude":
                                                    double altFt = StumpJunk.meters2Feet(Double.parseDouble(thisSetValue));
                                                    gpsData.put("AltitudeFt", altFt);
                                                    break;

                                                case "enhanced_altitude":
                                                    double eAltFt = StumpJunk.meters2Feet(Double.parseDouble(thisSetValue));
                                                    gpsData.put("EnhancedAltitudeFt", eAltFt);
                                                    break;

                                                case "speed":
                                                    double speedMPH = Double.parseDouble(thisSetValue)*2.237;
                                                    gpsData.put("SpeedMPH", speedMPH); intSpeeds.add(speedMPH);
                                                    break;

                                                case "enhanced_speed":
                                                    double eSpeedMPH = StumpJunk.meters2Feet(Double.parseDouble(thisSetValue));
                                                    gpsData.put("EnhancedSpeedMPH", eSpeedMPH);
                                                    break;

                                                case "heart_rate":
                                                    int heartrate = Integer.parseInt(thisSetValue);
                                                    gpsData.put("HeartRate", heartrate);
                                                    heartRates.add(heartrate);
                                                    break;

                                                case "temperature":
                                                    double tempF = StumpJunk.tempC2F(Double.parseDouble(thisSetValue));
                                                    gpsData.put("TemperatureF", tempF);
                                                    break;

                                                case "cadence":
                                                    int cadence = Integer.parseInt(thisSetValue);
                                                    gpsData.put("Cadence", cadence);
                                                    cadences.add(cadence);
                                                    break;

                                                case "fractional_cadence":
                                                    double fCadence = Double.parseDouble(thisSetValue);
                                                    gpsData.put("FractionalCadence", fCadence);
                                                    break;

                                                case "power":
                                                    double powerWatts = Double.parseDouble(thisSetValue);
                                                    gpsData.put("PowerWatts", powerWatts);
                                                    wattPowers.add(powerWatts);
                                                    break;
                                                    
                                                case "unknown":
                                                    double unknownValue = Double.parseDouble(thisSetValue);
                                                    gpsData.put("Unkown_" + unks, unknownValue);
                                                    unks++;
                                                    break;

                                                default: break;

                                            }


                                        } else {

                                            System.out.println("ERROR ON --> " + thisDataSet);

                                        }
                                    
                                    tRec++;
                                    
                                    } catch (Exception e) { 
                                        
                                        e.printStackTrace();
                                        
                                    }
                                    
                                }
                                
                                if(!errorHitOnLatLon) { geoJSONtrace += "["+longitude+","+latitude+"],"; }
                                String thisJSONstring = gpsLog.toString().substring(1);
                                thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
                                if(thisJSONstring.equals("\""+logNo+"\":{},")) {
                                        System.out.println("Empty JSON or bad GPS data!");
                                } else {
                                        fullGPSjson += thisJSONstring;
                                        System.out.println(logNo + " - " + geoJSONtrace);
                                }
                                
                                
                             
                                
                                //csvOutFile.delete();

                            }
                        }
		
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    Scanner csvScanner2 = null; try {			
                        csvScanner2 = new Scanner(csvOutFile);
                        while(csvScanner2.hasNext()) {			
                            
                            String nLine = csvScanner2.nextLine();
                            Matcher mainMatcher = Pattern.compile("Data.*hrv.*time").matcher(nLine);
                            
                            if(mainMatcher.find()) {
                                
                                String thisHrvLine[] = nLine.replaceAll("\"", "").split(",");
                                String hRvArray[] = thisHrvLine[4].split("\\|");
                                double hRvA = Double.parseDouble(hRvArray[0]);
                                double hRvB = Double.parseDouble(hRvArray[1]);
                                JSONArray tHrvArray = new JSONArray();
                                tHrvArray.put(hRvA).put(hRvB);
                                hrvArray.put(tHrvArray);                                
                                
                            }
                        
                        }
		
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
                    
		fullGPSjson = ("{"+fullGPSjson+"}").replace(",}", "}");
		geoJSONtrace = ("["+geoJSONtrace+"]").replace(",]","]");

		double avgCadence = 0.0;
		double avgHeart = 0.0;
		double avgSpeed = 0.0;
		double avgPower = 0.0;
		double maxPower = 0.0;
		double maxSpeed = 0.0;
		double trackedDistance = 0.0;

		int maxCadence = 0;
		int maxHeart = 0;
		long trackedTime = 0;

		String activityType = null;
		String activityDataField = null;
		String activityDataFieldHR = null;
		String geoJSONField = null;
		String hrAvgField = null; String hrMaxField = null;
		String speedAvgField = null; String speedMaxField = null;

		String activityCode = args[0].substring(11);

		try { avgHeart = (StumpJunk.sumListInteger(heartRates) / Collections.max(logNos)); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
		try { avgSpeed = (StumpJunk.sumListDouble(intSpeeds) / Collections.max(logNos)); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
		try { maxHeart = Collections.max(heartRates); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
		try { maxSpeed = Collections.max(intSpeeds); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
		try { trackedDistance = Collections.max(miles); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
		try { trackedTime = Collections.max(trackedSeconds)/100; } catch (NoSuchElementException nse) { nse.printStackTrace(); }

		switch(activityCode) {

			case "C":
				activityType = "Cycling";
				activityDataField = "gpsLogCyc";
                                activityDataFieldHR = "hrvLogCyc";
				geoJSONField = "CycGeoJSON";
				speedAvgField = "CycSpeedAvg";
				speedMaxField = "CycSpeedMax";
				hrAvgField = "CycHeartAvg";
				hrMaxField = "CycHeartMax";
				try { maxCadence = Collections.max(cadences); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
				try { avgCadence = (StumpJunk.sumListInteger(cadences) / Collections.max(logNos)); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
				try { maxPower = Collections.max(wattPowers); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
				try { avgPower = (StumpJunk.sumListDouble(wattPowers) / Collections.max(logNos)); } catch (NoSuchElementException nse) { nse.printStackTrace(); }
				break;

			case "D":
				activityType = "Cycling";
				activityDataField = "gpsLogCyc2";
                                activityDataFieldHR = "hrvLogCyc2";
				geoJSONField = "AltGeoJSON";
				break;

			case "E":
				activityType = "Cycling";
				activityDataField = "gpsLogCyc3";
                                activityDataFieldHR = "hrvLogCyc3";
				geoJSONField = "AltGeoJSON";
				break;
                                
			case "F":
				activityType = "Cycling";
				activityDataField = "gpsLogCyc4";
                                activityDataFieldHR = "hrvLogCyc4";
				geoJSONField = "AltGeoJSON";
				break;

			case "R":
				activityType = "RunWalk";
				activityDataField = "gpsLogRun";
                                activityDataFieldHR = "hrvLogRun";
				geoJSONField = "RunGeoJSON";
				speedAvgField = "RunSpeedAvg";
				speedMaxField = "RunSpeedMax";
				hrAvgField = "RunHeartAvg";
				hrMaxField = "RunHeartMax";
				break;

			case "S":
				activityType = "RunWalk";
				activityDataField = "gpsLogRun2";
                                activityDataFieldHR = "hrvLogRun2";
				geoJSONField = "AltGeoJSON";
				break;
                                
			case "T":
				activityType = "RunWalk";
				activityDataField = "gpsLogRun3";
                                activityDataFieldHR = "hrvLogRun3";
				geoJSONField = "AltGeoJSON";
				break;
                                
			case "U":
				activityType = "RunWalk";
				activityDataField = "gpsLogRun4";
                                activityDataFieldHR = "hrvLogRun4";
				geoJSONField = "AltGeoJSON";
				break;



		}	
	
		String gpsQuery = null;		

		if (legacyFlag.equals("yes")) {

			gpsQuery = "UPDATE Core.Fitness SET "+activityDataField+"='"+fullGPSjson+"' WHERE Date='"+thisDate+"';";

		} else {
		
			int trackedTimeMinutes = (int) (trackedTime/60);
			gpsQuery = "UPDATE Core.Fitness " +
                                "SET "+activityType+" = CASE WHEN "+activityType+" IS NULL THEN "+trackedDistance+" ELSE "+activityType+"+"+trackedDistance+" END," +
                                activityDataField+"='"+fullGPSjson+"', "+geoJSONField+"='"+geoJSONtrace+"'";
			if(activityType.equals("RunWalk")) { gpsQuery += ", TrackedTime="+trackedTime+", TrackedDist="+trackedDistance; }
			if(StumpJunk.isSetNotZero(speedAvgField)) { gpsQuery += ", "+speedAvgField+"="+avgSpeed; }
			if(StumpJunk.isSetNotZero(speedMaxField)) { gpsQuery += ", "+speedMaxField+"="+maxSpeed; }
			if(StumpJunk.isSetNotZero(hrAvgField)) { gpsQuery += ", "+hrAvgField+"="+avgHeart; }
			if(StumpJunk.isSetNotZero(hrMaxField)) { gpsQuery += ", "+hrMaxField+"="+maxHeart; }
                        if(hrvArray.length() != 0) { gpsQuery += ", "+activityDataFieldHR+"='"+hrvArray.toString()+"'"; }
			if(maxCadence > 0) { gpsQuery += ", CycCadMax="+maxCadence; }
			if(avgCadence > 0) { gpsQuery += ", CycCadAvg="+avgCadence; }
			if(maxPower > 0) { gpsQuery += ", CycPowerMax="+maxPower; }
			if(avgPower > 0) { gpsQuery += ", CycPowerAvg="+avgPower; }
			if(activityType.equals("Cycling")) { gpsQuery+= ", Bicycle='"+bicycle+"'"; }
			gpsQuery += ", IntensityMinutes = CASE WHEN IntensityMinutes IS NULL THEN " + trackedTimeMinutes + " ELSE IntensityMinutes+" + trackedTimeMinutes + " END" +
					" WHERE Date='"+thisDate+"';";

		}
		
		String squeezeThisQueryIn = "UPDATE Core.Fitness SET CaloriesBurned=(1600+IFNULL(175*RunWalk,0)+IFNULL(72*Cycling,0)) WHERE Date=CURDATE();";

		System.out.println(gpsQuery);
                if(sourceType.equals("csv") || sourceType.equals("gpx") || sourceType.equals("fit")) {
                    try ( Connection conn = MyDBConnector.getMyConnection(); Statement stmt = conn.createStatement();) {
                    	stmt.executeUpdate(gpsQuery);
                    	stmt.executeUpdate(squeezeThisQueryIn);
                	}
                    catch (SQLException se) { se.printStackTrace(); }
                    catch (Exception e) { e.printStackTrace(); }
                    try { StumpJunk.runProcess("gzip "+gpsInFile.toString()); } catch (Exception e) { e.printStackTrace(); }
                } else {
                    System.out.println("Not running query or gzipping as source type ["+sourceType+"] is still experimental!");
                }
                
	}

}
