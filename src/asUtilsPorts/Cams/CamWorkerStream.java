/*
by Anthony Stump
Created: 31 Aug 2017
Separated from CamWorkerHF: 5 Dec 2019
Updated: 15 Dec 2019
*/

package asUtilsPorts.Cams;

import asUtils.Shares.StumpJunk;
import asUtils.Secure.JunkyPrivate;
import java.io.*;

public class CamWorkerStream {
    
        private static void ffmpegCall(String url, File outFile, String capRes) {
            
            StumpJunk sj = new StumpJunk();
            
            final int tester = 1;
            final int timeout = (120*1000);
            final int threadTimeout = 10;
            
            final String ffmpegCall = "ffmpeg " +
                    " -y" +
                    " -i " + url +
                    " -timeout " + timeout +
                    " -f image2" +
                    " -filter:v fps=fps=8" +
                    " -update 1" +
                    " -s " + capRes +
                    " " + outFile.toString() +
                    " > /dev/null 2>&1 < /dev/null";
            
            while (tester == tester) {
                    try { sj.runProcess(ffmpegCall); } catch (Exception e) { }
                    try { Thread.sleep(threadTimeout*1000); } catch (Exception e) { }
                    System.out.println("WARN: Catpture for " + outFile.toString() + " reset!");
            }
            
        }

    
	public static void main(String[] args) {
            
        final String camPath = args[0];	

        CamBeans camBeans = new CamBeans();
        final String capRes = camBeans.getCapRes();
        JunkyPrivate jp = new JunkyPrivate();
        
		final String ipCamUser = jp.getIpCamUser();
		final String ipCamPass = jp.getIpCamPass();
            
		File c1_file = new File(camPath+"/webc1-temp.jpeg");
        File c2_file = new File(camPath+"/webc2-temp.jpeg");
		File c3_file = new File(camPath+"/webc3-temp.jpeg");
		File c4_file = new File(camPath+"/webc4-temp.jpeg");
		File c5_file = new File(camPath+"/webc5-temp.jpeg");
		
        /* String usbDevice = null;
        try { usbDevice = StumpJunk.runProcessOutVar("ls "+camBeans.getUsbs().toString()+"*"); } catch (IOException ix) { ix.printStackTrace(); }
        usbDevice = usbDevice.replaceAll("\\n", ";");
        String usbDevices[] = usbDevice.split(";");

        try { c1_file.delete(); } catch (Exception e) { e.printStackTrace(); }
        try { c2_file.delete(); } catch (Exception e) { e.printStackTrace(); }
        try { c3_file.delete(); } catch (Exception e) { e.printStackTrace(); }
        try { c4_file.delete(); } catch (Exception e) { e.printStackTrace(); }
        try { c5_file.delete(); } catch (Exception e) { e.printStackTrace(); }
         */
        
        final String c1_url = "/dev/video0"; //usbDevices[1];
        final String c2_url = "rtsp://" + ipCamUser + ":" + ipCamPass + "@" + jp.getIpForCam2() + ":88/videoMain";
        final String c3_url = "rtsp://" + ipCamUser + ":" + ipCamPass + "@" + jp.getIpForCam1() + ":88/videoMain";
        final String c4_url = "rtsp://" + jp.getIpForRaspPi1() + ":8554/unicast";
        final String c5_url = "rtsp://admin:@" + jp.getIpForCam3() + ":88/videoMain";
        
        Thread cs1 = new Thread(() -> { ffmpegCall(c1_url, c1_file, capRes); });
        Thread cs2 = new Thread(() -> { ffmpegCall(c2_url, c2_file, capRes); });
        Thread cs3 = new Thread(() -> { ffmpegCall(c3_url, c3_file, capRes); });
        Thread cs4 = new Thread(() -> { ffmpegCall(c4_url, c4_file, capRes); });
        Thread cs5 = new Thread(() -> { ffmpegCall(c5_url, c5_file, capRes); });
        Thread streams[] = { /* cs1,*/ cs2, cs3, cs4, cs5 };
        for (Thread thread : streams) { thread.start(); }
                
	}

}
