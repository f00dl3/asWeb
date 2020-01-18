/*
by Anthony Stump
Created: 7 Sep 2017
Updated: 16 Jan 2020
*/

package asUtilsPorts;

import asUtilsPorts.Cams.CamBeans;
import asUtilsPorts.Cams.CamWorkerHF;
import asUtilsPorts.Cams.CamWorkerStream;
import asWebRest.shared.ThreadRipper;

import java.io.*;
import java.util.ArrayList;


public class CamController {
        
    private static void mainCamLoop(File camPath) {           
    	CamWorkerHF cwhf = new CamWorkerHF();
        cwhf.main(camPath.getPath(), "X");
    }

	public static void initCams() {

            CamBeans camBeans = new CamBeans();
            ThreadRipper tr = new ThreadRipper();
        
            final File camPath = camBeans.getCamPath();
            final File pushTemp = camBeans.getPushTemp();
            final File pushTempPub = camBeans.getPushTempPub();

            if (!pushTemp.exists()) {
                    camPath.mkdirs();
                    pushTemp.mkdirs();
                    pushTempPub.mkdirs();
            }

    		ArrayList<Runnable> cct = new ArrayList<Runnable>();
    		cct.add(() -> mainCamLoop(camPath));
    		cct.add(() -> streamForker(camPath, pushTemp));
    		tr.runProcesses(cct, false, true);
                
	}

    private static void streamForker(File camPath, File pushTemp) {                            
        final String[] hf1Arg = { camPath.getPath() };            
        int tester = 1;            
        CamWorkerStream.main(hf1Arg);
    }

}
