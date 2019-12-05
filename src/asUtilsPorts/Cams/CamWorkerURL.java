/*
by Anthony Stump
Created: 23 Oct 2018
Updated: 4 Dec 2019
*/

package asUtilsPorts.Cams;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.MyDBConnector;
import asUtils.Shares.StumpJunk;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamWorkerURL {

	public static void doJob(Connection dbc, String camPath) {		
		
		WebCommon wc = new WebCommon();
		
        JunkyBeans junkyBeans = new JunkyBeans();
        CamBeans camBeans = new CamBeans();
        Date date = new Date();
		final DateFormat dateOverlayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final String capRes = camBeans.getCapRes();
        final String masterPrefix = "XwebcW-temp";
        final File urlCamFile = new File(camPath+"/"+masterPrefix+".jpeg");
        final File urlTempFile = new File(camPath+"/"+masterPrefix+"-A.jpeg");
        final File taFile = new File(camPath+"/"+masterPrefix+"-m1.jpeg");
		final File tbFile = new File(camPath+"/"+masterPrefix+"-m2.jpeg");
        final File tc1File = new File(camPath+"/"+masterPrefix+"-1.jpeg");
		final File tc2File = new File(camPath+"/"+masterPrefix+"-2.jpeg");
		final File tc3File = new File(camPath+"/"+masterPrefix+"-3.jpeg");
		final File tc4File = new File(camPath+"/"+masterPrefix+"-4.jpeg");
        int camNum = 1;
       
        final String camTimestamp = dateOverlayFormat.format(date);
        final String camCaption = junkyBeans.getApplicationName()+" URL Cams - "+camTimestamp;
		String volCamUrlSQL = "SELECT URL, CamURLBubble FROM Core.WebLinks WHERE CamURLBatch=1 AND Active=1;";
                
        try { taFile.delete(); } catch (Exception e) { }
        try { tbFile.delete(); } catch (Exception e) { }

		try ( ResultSet rs = wc.q2rs1c(dbc, volCamUrlSQL, null); ) { 
	        while (rs.next()) { 
	                
	                final String filePrefix = masterPrefix+"-"+camNum;
	                final File tFile = new File(camPath+"/"+filePrefix+".jpeg");
	                final File tTempFile = new File(camPath+"/"+filePrefix+"-A.jpeg");
	                String thisUrl = rs.getString("URL");
	                StumpJunk.jsoupOutBinary(thisUrl, tTempFile, 5.0);
	                if(tFile.exists()) { tFile.delete(); }
	                if(tTempFile.exists()) { StumpJunk.moveFile(tTempFile.getPath(), tFile.getPath()); } else { System.out.println("URL Capture issues @ " + rs.getString("CamURLBubble") + "!"); }
	                camNum++;
	                
	            }
                
		} catch (Exception e) { e.printStackTrace(); }
                
        if(!tc1File.exists()) { StumpJunk.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"CamU1 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+tc1File.getPath()); }
        if(!tc2File.exists()) { StumpJunk.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"CamU2 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+tc1File.getPath()); }
        if(!tc3File.exists()) { StumpJunk.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"CamU3 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+tc1File.getPath()); }
        if(!tc4File.exists()) { StumpJunk.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"CamU4 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+tc1File.getPath()); }

        String convertA = "convert \\( "+tc1File.getPath()+" -resize "+capRes+"! "+tc2File.getPath()+" -resize "+capRes+"! +append \\)"
                + " -background Black -append "+taFile.getPath();

        String convertB = "convert \\( "+tc3File.getPath()+" -resize "+capRes+"! "+tc4File.getPath()+" -resize "+capRes+"! +append \\)"
                + " \\( -gravity south -background Black -pointsize 36 -fill Yellow label:\""+camCaption+"\" +append \\)"
                + " -background Black -append "+tbFile.getPath();

        Thread ca2a = new Thread(() -> { StumpJunk.runProcess(convertA); });
        Thread ca2b = new Thread(() -> { StumpJunk.runProcess(convertB); });
        Thread thList2[] = { ca2a, ca2b };
        for (Thread thread : thList2) { thread.start(); } 
        for (int i = 0; i < thList2.length; i++) { try { thList2[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

        String convertC = "convert \\( "+taFile.getPath()+" +append \\)"
                + " \\( "+tbFile.getPath()+" +append \\)"
                + " -background Black -append -resize "+capRes+"! "+urlTempFile.getPath();

        StumpJunk.runProcess(convertC);
                    
        if(urlCamFile.exists()) { urlCamFile.delete(); }
        if(urlTempFile.exists()) { StumpJunk.moveFile(urlTempFile.getPath(), urlCamFile.getPath()); } else { System.out.println("URL Capture issues @ Merge!"); }
                
	}

}
