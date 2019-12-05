/*
by Anthony Stump
Created: 31 Aug 2017
Split from CamWorkerHF: 5 Dec 2019
Updated: 5 Dec 2019
*/

package asUtilsPorts.Cams;

import asUtils.Shares.StumpJunk;
import asUtils.Secure.JunkyPrivate;
import asUtils.Shares.JunkyBeans;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class CamWorkerPi {

	public static void main(String[] args) {

        CamBeans camBeans = new CamBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
                
		final int testVal = 1;

		final String camPath = args[0];
        final String capRes = camBeans.getCapRes();
		final String instance = args[1];
		final String piCam1 = junkyPrivate.getIpForRaspPi1();
		File yWebC4File = new File(camPath+"/"+instance+"webc4-temp.jpeg");

        final String capCommand_191205 = "ffmpeg -i rtsp://"+piCam1+":8554/unicast -f image2 -update 1 "+yWebC4File.getPath();

        StumpJunk.runProcess(capCommand_191205);
        
        /*
        while (testVal == testVal) {

			//yWebC4File.delete();
        	
            final double timeout = 30;
            final String capCommand = "ffmpeg -i rtsp://"+piCam1+":8554/unicast -ss 00:00:00.6 -frames 1 "+yWebC4File.getPath();
            
            try { 
                StumpJunk.runProcess("timeout --kill-after="+timeout+" "+timeout+" "+capCommand);
            } catch (Exception e) {
                e.printStackTrace();
            }
			
            if(!yWebC4File.exists()) { StumpJunk.runProcess("convert -size "+capRes+" -gravity center -annotate 0 \"Cam4 temporarily unavailable!\" -pointsize 42 -fill Yellow xc:navy "+yWebC4File.getPath()); }
            
		} */

	}

}
