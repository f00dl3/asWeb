/*
by Anthony Stump
Created: 31 Aug 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts.Cams;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import asUtils.Shares.JunkyBeans;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class CamWorkerHF {

	public static void main(String[] args) {

        CamBeans camBeans = new CamBeans();
        CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        
		final int testVal = 1;

		final String camPath = args[0];
        final String capRes = camBeans.getCapRes();
		final String instance = args[1];
		final String cachePath = cb.getPathChartCache();

		DateFormat dateOverlayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		DateFormat dateFileFormat = new SimpleDateFormat("yyMMdd-HHmmss-SSS");

		final File lastUPSStatus = new File(camPath+"/LUStatus.txt");
		final File lastCaseTemp = camBeans.getTfOutCase();
		final File lastCPUTemp = camBeans.getTfOutCPU();
		final File lastGarageTemp = camBeans.getTfOutGarage();
        final File urlFile = camBeans.getCamUrl();

        File xWebCWFile = new File(camPath+"/XwebcW-temp.jpeg");
		File xWebC1File = new File(camPath+"/webc1-temp.jpeg");
        File yWebC2File = new File(camPath+"/webc2-temp.jpeg");
		File yWebC3File = new File(camPath+"/webc3-temp.jpeg");
		File yWebC4File = new File(camPath+"/webc4-temp.jpeg");
		File yWebC5File = new File(camPath+"/webc5-temp.jpeg");
		File webcYaFile = new File(camPath+"/webc"+instance+"a-temp.jpeg");
		File webcYbFile = new File(camPath+"/webc"+instance+"b-temp.jpeg");
		File webcYFile = new File(camPath+"/webc"+instance+"-temp.jpeg");
                
        while (testVal == testVal) {

			Date date = new Date();
			final String camTimestamp = dateOverlayFormat.format(date);
			final String fileTimestamp = dateFileFormat.format(date);

			/* webcYaFile.delete();
			webcYbFile.delete(); */

			Scanner caseScanner = null; int tempCase = 0; try { caseScanner = new Scanner(lastCaseTemp); while(caseScanner.hasNext()) { tempCase = Integer.parseInt(caseScanner.nextLine()); } } catch (FileNotFoundException e) { }
			Scanner cpuScanner = null; int tempCPU = 0; try { cpuScanner = new Scanner(lastCPUTemp); while(cpuScanner.hasNext()) { tempCPU = Integer.parseInt(cpuScanner.nextLine()); } } catch (FileNotFoundException e) { }
            Scanner garageScanner = null; int tempGarage = 0; try { garageScanner = new Scanner(lastGarageTemp); while(garageScanner.hasNext()) { tempGarage = Integer.parseInt(garageScanner.nextLine()); } } catch (FileNotFoundException e) { }
			Scanner upsScanner = null; String upsStatus = null; try { upsScanner = new Scanner(lastUPSStatus); while(upsScanner.hasNext()) { upsStatus = upsScanner.nextLine(); } } catch (FileNotFoundException e) { }
            Scanner urlScanner = null; String thisUrl = ""; try { urlScanner = new Scanner(urlFile); while(urlScanner.hasNext()) { thisUrl = urlScanner.nextLine(); } } catch (FileNotFoundException e) { }
                        
            final String camCaption = junkyBeans.getApplicationName()+" Cams - "+camTimestamp+" -- IN "+tempCase+"F -- GA "+tempGarage+"F -- CPU "+tempCPU+"F -- "+upsStatus;
  
			if(!xWebCWFile.exists()) { WebCommon.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"CamW temporarily unavailable!\n"+thisUrl+"\" -pointsize 42 -fill Yellow xc:navy "+xWebCWFile.getPath()+" > /dev/null 2>&1 < /dev/null"); }
			if(!xWebC1File.exists()) { WebCommon.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam1 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+xWebC1File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
			if(!yWebC2File.exists()) { WebCommon.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam2 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+yWebC2File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
            if(!yWebC3File.exists()) { WebCommon.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam3 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+yWebC3File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
            if(!yWebC4File.exists()) { WebCommon.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam4 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+yWebC4File.getPath()+" > /dev/null 2>&1 < /dev/null"); }
            if(!yWebC5File.exists()) { WebCommon.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam5 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+yWebC5File.getPath()+" > /dev/null 2>&1 < /dev/null"); }

			String convertA = "convert \\( "+yWebC4File.getPath()+" -resize "+capRes+"! "+xWebC1File.getPath()+" -resize "+capRes+"! "+yWebC2File.getPath()+" -resize "+capRes+"! +append \\)"
				+ " -background Black -append "+webcYaFile.getPath();

			String convertB = "convert \\( "+yWebC3File.getPath()+" -resize "+capRes+"! " + yWebC5File.getPath()+" -resize "+capRes+"! "+xWebCWFile.getPath()+" -resize "+capRes+"! +append \\)"
				+ " \\( -gravity south -background Black -pointsize 36 -fill Yellow label:\""+camCaption+"\" +append \\)"
				+ " -background Black -append "+webcYbFile.getPath();

			Thread ca2a = new Thread(() -> { WebCommon.runProcessSilently(convertA); });
			Thread ca2b = new Thread(() -> { WebCommon.runProcessSilently(convertB); });
			Thread thList2[] = { ca2a, ca2b };
			for (Thread thread : thList2) { thread.start(); } 
			for (int i = 0; i < thList2.length; i++) { try { thList2[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

			String convertC = "convert \\( "+webcYaFile.getPath()+" +append \\)"
				+ " \\( "+webcYbFile.getPath()+" +append \\)"
				+ " -background Black -append -resize "+camBeans.getFinalRes()+"! "+webcYFile.getPath();

			WebCommon.runProcessSilently(convertC);

			try { WebCommon.moveFileSilently(webcYFile.getPath(), camPath+"/PushTmp/"+fileTimestamp+".jpeg"); } catch (Exception e) { }
            try { WebCommon.copyFileSilently(camPath+"/PushTmp/"+fileTimestamp+".jpeg", cachePath+"/CamLive.jpeg"); } catch (Exception ix) { }

		}

	}

}
