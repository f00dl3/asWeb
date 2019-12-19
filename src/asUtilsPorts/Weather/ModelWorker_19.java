/*
by Anthony Stump
Created: 22 Sep 2017
Updated: 19 Dec 2019
*/

package asUtilsPorts.Weather;

public class ModelWorker_19 {
	
	public static void main(String[] args) {
            
		
		final String getHour = args[0];
		final String round = args[1];
		boolean int6h = false;
		boolean int6hO = false;
		boolean int12h = false;
	
		if(getHour.equals("00") || getHour.equals("06") || getHour.equals("12") || getHour.equals("18")) { int6h = true; }
		if(getHour.equals("03") || getHour.equals("09") || getHour.equals("15") || getHour.equals("21")) { int6hO = true; }
		if(getHour.equals("00") || getHour.equals("12")) { int12h = true; }

		final String[] hrrrArgs = { getHour, round }; ModelWorkerHRRR_19.main(hrrrArgs);
		
		if(int6h) {
			final String[] gfsArgs = { getHour, round }; ModelWorkerGFS_19.main(gfsArgs);
			final String[] namArgs = { getHour, round }; ModelWorkerNAM_19.main(namArgs);
			if(int12h) {
				final String[] hrwaArgs = { getHour, round }; ModelWorkerHRWA_19.main(hrwaArgs);
				final String[] hrwnArgs = { getHour, round }; ModelWorkerHRWN_19.main(hrwnArgs);
				final String[] cmcArgs = { getHour, round }; ModelWorkerCMC_19.main(cmcArgs);
			}
		}

		if(int6hO) {
			final String[] srfaArgs = { getHour, round }; ModelWorkerSRFA_19.main(srfaArgs);
			final String[] srfnArgs = { getHour, round }; ModelWorkerSRFN_19.main(srfnArgs);
		}			
		
	}
	
}
