/*
by Anthony Stump
Created: 22 Sep 2017
Updated: 8 Feb 2020
*/

package asUtilsPorts.Weather;

import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerCMC;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerGFS;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerHRRR;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerHRWA;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerHRWN;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerNAM;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerSRFA;
import asUtilsPorts.Weather.OldModelWorkers.ModelWorkerSRFN;

public class ModelWorker {
	
	public void main(String getHour, String round) {            
	
		ModelWorkerCMC mwCMC = new ModelWorkerCMC();
		ModelWorkerGFS mwGFS = new ModelWorkerGFS();
		ModelWorkerHRRR mwHRRR = new ModelWorkerHRRR();
		ModelWorkerHRWA mwHRWA = new ModelWorkerHRWA();
		ModelWorkerHRWN mwHRWN = new ModelWorkerHRWN();
		ModelWorkerNAM mwNAM = new ModelWorkerNAM();
		ModelWorkerSRFA mwSRFA = new ModelWorkerSRFA();
		ModelWorkerSRFN mwSRFN = new ModelWorkerSRFN();
		
		boolean int6h = false;
		boolean int6hO = false;
		boolean int12h = false;
	
		if(getHour.equals("00") || getHour.equals("06") || getHour.equals("12") || getHour.equals("18")) { int6h = true; }
		if(getHour.equals("03") || getHour.equals("09") || getHour.equals("15") || getHour.equals("21")) { int6hO = true; }
		if(getHour.equals("00") || getHour.equals("12")) { int12h = true; }

		mwHRRR.main(getHour, round);
		
		if(int6h) {
			mwGFS.main(getHour, round);
			mwNAM.main(getHour, round);
			if(int12h) {
				mwHRWA.main(getHour, round);
				mwHRWN.main(getHour, round);
				mwCMC.main(getHour, round);
			}
		}

		if(int6hO) {
			mwSRFA.main(getHour, round);
			mwSRFN.main(getHour, round);
		}			
		
	}
	
}
