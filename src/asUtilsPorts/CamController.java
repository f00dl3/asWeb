/*
by Anthony Stump
Created: 7 Sep 2017
Updated: 4 Dec 2019
*/

package asUtilsPorts;

import asUtilsPorts.Cams.CamBeans;
import asUtilsPorts.Cams.CamWorkerHF;
import asUtilsPorts.Cams.CamWorkerUSB;

import java.io.*;

public class CamController {

	public static void doTest() {

        CamBeans camBeans = new CamBeans();              
        
		final File camPath = camBeans.getCamPath();
		final File pushTemp = camBeans.getPushTemp();
		final String[] hf1Arg = { camPath.getPath(), "X" };

		CamWorkerHF.main(hf1Arg);
		
	}	
	
	public static void initCams() {

        CamBeans camBeans = new CamBeans();
                
		final File camPath = camBeans.getCamPath();
		final File pushTemp = camBeans.getPushTemp();

		final String[] usbArg = { camPath.getPath() };
		final String[] hf1Arg = { camPath.getPath(), "X" };
		final String[] hf2Arg = { camPath.getPath(), "Y" };
		final String[] hf3Arg = { camPath.getPath(), "Z" };
		final String[] hf4Arg = { camPath.getPath(), "A" };
		int tester = 1;

		while (tester == tester) {
			Thread cc1 = new Thread(() -> { CamWorkerUSB.main(usbArg); });
			Thread cc2 = new Thread(() -> { CamWorkerHF.main(hf1Arg); });
			//Thread cc3 = new Thread(() -> { CamWorkerHF.main(hf2Arg); }); // UNLOCK ONCE VM STABILITY CONFIRMED
			//Thread cc4 = new Thread(() -> { CamWorkerHF.main(hf3Arg); }); // WATCH - MAY NEED MORE VIRTUAL CORES - PEGS CPU OVER 50% AT TIMES
			//Thread cc5 = new Thread(() -> { CamWorkerHF.main(hf4Arg); }); NEED MORE VIRTUAL CORES
			Thread cams[] = { cc1 /*, cc2, cc3, cc4 , cc5 */ };
			for (Thread thread : cams) { thread.start(); } 
			for (int i = 0; i < cams.length; i++) {
                try {
                    long sleepPeriod = (long) i * 25;
                    cams[i].sleep(sleepPeriod);
                    cams[i].join();
                } catch (InterruptedException nx) { nx.printStackTrace(); }
            }
		}
                
	}

}
