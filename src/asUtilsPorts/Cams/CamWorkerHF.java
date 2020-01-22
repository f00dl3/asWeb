/*
by Anthony Stump
Created: 31 Aug 2017
Updated: 21 Jan 2020
*/

package asUtilsPorts.Cams;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;
import asUtils.Shares.JunkyBeans;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CamWorkerHF {

	public void main(String camPath, String instance) {

        CamBeans camBeans = new CamBeans();
        CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();
        
		final int testVal = 1;

        final String capRes = camBeans.getCapRes();
		final String cachePath = cb.getPathChartCache();

		DateFormat dateOverlayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		DateFormat dateFileFormat = new SimpleDateFormat("yyMMdd-HHmmss-SSS");

		final File lastUPSStatus = new File(camPath+"/LUStatus.txt");
		final File lastCaseTemp = camBeans.getTfOutCase();
		final File lastCPUTemp = camBeans.getTfOutCPU();
        final File urlFile = camBeans.getCamUrl();

        File webCWFile = new File(camPath+"/webcW-temp.jpeg");
		File webC1File = new File(camPath+"/webc1-temp.jpeg");
        File webC2File = new File(camPath+"/webc2-temp.jpeg");
		File webC3File = new File(camPath+"/webc3-temp.jpeg");
		File webC4File = new File(camPath+"/webc4-temp.jpeg");
		File webC5File = new File(camPath+"/webc5-temp.jpeg");
		File webcYaFile = new File(camPath+"/webc"+instance+"a-temp.jpeg");
		File webcYbFile = new File(camPath+"/webc"+instance+"b-temp.jpeg");
		File webcYFile = new File(camPath+"/webc"+instance+"-temp.jpeg");
                
        while (testVal == testVal) {

        	CamSensors cSense = new CamSensors();
			Date date = new Date();
			final String camTimestamp = dateOverlayFormat.format(date);
			final String fileTimestamp = dateFileFormat.format(date);

			Scanner caseScanner = null; int tempCase = 0; try { caseScanner = new Scanner(lastCaseTemp); while(caseScanner.hasNext()) { tempCase = Integer.parseInt(caseScanner.nextLine()); } } catch (FileNotFoundException e) { }
			Scanner cpuScanner = null; int tempCPU = 0; try { cpuScanner = new Scanner(lastCPUTemp); while(cpuScanner.hasNext()) { tempCPU = Integer.parseInt(cpuScanner.nextLine()); } } catch (FileNotFoundException e) { }
            //int tempGarage = cSense.getTemperatureForSnapshot()
			//Scanner garageScanner = null; int tempGarage = 0; try { garageScanner = new Scanner(lastGarageTemp); while(garageScanner.hasNext()) { tempGarage = Integer.parseInt(garageScanner.nextLine()); } } catch (FileNotFoundException e) { }
			Scanner upsScanner = null; String upsStatus = null; try { upsScanner = new Scanner(lastUPSStatus); while(upsScanner.hasNext()) { upsStatus = upsScanner.nextLine(); } } catch (FileNotFoundException e) { }
            Scanner urlScanner = null; String thisUrl = ""; try { urlScanner = new Scanner(urlFile); while(urlScanner.hasNext()) { thisUrl = urlScanner.nextLine(); } } catch (FileNotFoundException e) { }
                        
            final String camCaption = junkyBeans.getApplicationName()+" Cams - "+camTimestamp+" -- IN "+tempCase+"F " /* -- GA "+tempGarage+"F */ + "-- CPU "+tempCPU+"F -- "+upsStatus;
  
			if(!webCWFile.exists()) { wc.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"CamW temporarily unavailable!\n"+thisUrl+"\" -pointsize 42 -fill Yellow xc:navy "+webCWFile.getPath()+" > /dev/null 2>&1 < /dev/null"); }
			if(!webC1File.exists()) { wc.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam1 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+webC1File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
			if(!webC2File.exists()) { wc.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam2 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+webC2File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
            if(!webC3File.exists()) { wc.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam3 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+webC3File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
            if(!webC4File.exists()) { wc.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam4 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+webC4File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
            if(!webC5File.exists()) { wc.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam5 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+webC5File.getPath()+" > /dev/null 2>&1 < /dev/null"); }

			String convertA = "convert \\( "+webC4File.getPath()+" -resize "+capRes+"! "+webC1File.getPath()+" -resize "+capRes+"! "+webCWFile.getPath()+" -resize "+capRes+"! +append \\)"
				+ " -background Black -append "+webcYaFile.getPath();

			String convertB = "convert \\( "+webC3File.getPath()+" -resize "+capRes+"! " + webC5File.getPath()+" -resize "+capRes+"! "+webC2File.getPath()+" -resize "+capRes+"! +append \\)"
				+ " \\( -gravity south -background Black -pointsize 36 -fill Yellow label:\""+camCaption+"\" +append \\)"
				+ " -background Black -append "+webcYbFile.getPath();

			ArrayList<Runnable> cwts = new ArrayList<Runnable>();
			cwts.add(() -> wc.runProcessSilently(convertA));
			cwts.add(() -> wc.runProcessSilently(convertB));
			tr.runProcesses(cwts, false, false);
			
			String convertC = "convert \\( "+webcYaFile.getPath()+" +append \\)"
				+ " \\( "+webcYbFile.getPath()+" +append \\)"
				+ " -background Black -append -resize "+camBeans.getFinalRes()+"! "+webcYFile.getPath();

			wc.runProcessSilently(convertC);

			try { wc.moveFileSilently(webcYFile.getPath(), camBeans.getPushTemp().toString()+"/"+fileTimestamp+".jpeg"); } catch (Exception e) { }
            try { wc.copyFileSilently(webcYbFile.getPath(), camBeans.getPushTempPub().toString()+"/"+fileTimestamp+".jpeg"); } catch (Exception ix) { }           
            try { wc.copyFileSilently(camPath+"/PushTmp/"+fileTimestamp+".jpeg", cachePath+"/CamLive.jpeg"); } catch (Exception ix) { }   
            try { wc.copyFileSilently(webcYbFile.getPath(), cachePath+"/CamLive_Public.jpeg"); } catch (Exception ix) { }           
            
		}

	}

}
