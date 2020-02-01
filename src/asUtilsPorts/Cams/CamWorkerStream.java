/*
by Anthony Stump
Created: 31 Aug 2017
Separated from CamWorkerHF: 5 Dec 2019
Updated: 30 Jan 2020
*/

package asUtilsPorts.Cams;

import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.util.ArrayList;

public class CamWorkerStream {

	private static void camImageGet(String url, File outFile) {
		WebCommon wc = new WebCommon();
		while(true) {
			try { wc.jsoupOutBinary(url, outFile, 5.0); } catch (Exception e) { }
		}
	}
    
    private static void ffmpegCall(String url, File outFile, String capRes) {

    	WebCommon wc = new WebCommon();
    	
    	final int fps = 8;
        final int killTime = (1*60)*60;
    	final int tester = 1;
        final int timeout = -1; //(60*1000);
        final int threadTimeout = 5;
        
        String ffmpegCall = "flock -n "+outFile.toString()+".lock" +
        		" timeout --kill-after="+killTime+" "+killTime +
        		" ffmpeg -y" +
		" -i " + url +
                " -timeout " + timeout +
                " -threads 1" +
                " -f image2" +
                " -filter:v fps=fps="+fps +
                " -update 1" +
                " -s " + capRes +
                " " + outFile.toString() +
                " > /dev/null 2>&1 < /dev/null";
        
        while (tester == tester) {
                try { wc.runProcess(ffmpegCall); } catch (Exception e) { }
                try { Thread.sleep(threadTimeout*1000); } catch (Exception e) { }
        }
        
    }

    
	public static void main(String[] args) {
            
        final String camPath = args[0];	

        CamBeans camBeans = new CamBeans();
        final String capRes = camBeans.getCapRes();
        JunkyPrivate jp = new JunkyPrivate();
        ThreadRipper tr = new ThreadRipper();
        
		final String ipCamUser = jp.getIpCamUser();
		final String ipCamPass = jp.getIpCamPass();
            
		File c1_file = new File(camPath+"/webc1-temp.jpeg");
        File c2_file = new File(camPath+"/webc2-temp.jpeg");
		File c3_file = new File(camPath+"/webc3-temp.jpeg");
		File c4_file = new File(camPath+"/webc4-temp.jpeg");
		File c5_file = new File(camPath+"/webc5-temp.jpeg");
        
	final String c1_url = "http://localhost:8555/camLive.jpg";
        //final String c1_url = "rtsp://localhost:8555/unicast";
        final String c2_url = "rtsp://" + ipCamUser + ":" + ipCamPass + "@" + jp.getIpForCam2() + ":88/videoMain";
        final String c3_url = "rtsp://" + ipCamUser + ":" + ipCamPass + "@" + jp.getIpForCam1() + ":88/videoMain";
        final String c4_url = "rtsp://" + jp.getIpForRaspPi1() + ":8554/unicast";
        final String c5_url = "rtsp://admin:@" + jp.getIpForCam3() + ":88/videoMain";
        
		ArrayList<Runnable> cs = new ArrayList<Runnable>();
		//cs.add(() -> camImageGet(c1_url, c1_file));
		//cs.add(() -> ffmpegCall(c1_url, c1_file, capRes));
		cs.add(() -> ffmpegCall(c2_url, c2_file, capRes));
		cs.add(() -> ffmpegCall(c3_url, c3_file, capRes));
		cs.add(() -> ffmpegCall(c4_url, c4_file, capRes));
		cs.add(() -> ffmpegCall(c5_url, c5_file, capRes));
		tr.runProcesses(cs, true, true);
                
	}

}
