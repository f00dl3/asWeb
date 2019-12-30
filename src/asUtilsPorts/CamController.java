/*
by Anthony Stump
Created: 7 Sep 2017
Updated: 30 Dec 2019
*/

package asUtilsPorts;

import asUtilsPorts.Cams.CamBeans;
import asUtilsPorts.Cams.CamWorkerHF;
import asUtilsPorts.Cams.CamWorkerStream;
import java.io.*;


public class CamController {
        
    private static void mainCamLoop(File camPath) {            
        final String[] hf1Arg = { camPath.getPath(), "X" };
        int tester = 1;            
        //while (tester == tester) {
            CamWorkerHF.main(hf1Arg);
        //}                        
    }

	public static void initCams() {

            CamBeans camBeans = new CamBeans();
        
            final File camPath = camBeans.getCamPath();
            final File pushTemp = camBeans.getPushTemp();

            if (!pushTemp.exists()) {
                    camPath.mkdirs();
                    pushTemp.mkdirs();
            }
            
            Thread ta = new Thread(() -> { mainCamLoop(camPath); });
            Thread tb = new Thread(() -> { streamForker(camPath, pushTemp); });
            Thread procs[] = { ta, tb };
            for (Thread thread : procs) { thread.start(); }
                
	}

    private static void streamForker(File camPath, File pushTemp) {                            
        final String[] hf1Arg = { camPath.getPath() };            
        int tester = 1;            
        //while (tester == tester) {
            CamWorkerStream.main(hf1Arg);
        //}            
    }

}
