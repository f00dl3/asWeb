/*
by Anthony Stump
Created: 22 Sep 2017
Updated: 28 Apr 2021
*/

package asUtilsPorts.Weather;

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
			mwNAM.main(getHour, round);
			if(int12h) {
				mwGFS.main(getHour, round);
				//mwHRWA.main(getHour, round);
				//mwHRWN.main(getHour, round);
				//mwCMC.main(getHour, round);
			}
		}

		if(int6hO) {
			//mwSRFA.main(getHour, round);
			//mwSRFN.main(getHour, round);
		}			
		
	}
	
}
