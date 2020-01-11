/*
by Anthony Stump
Created: 30 Aug 2017
Updated: 6 Jan 2020
*/

package asUtilsPorts;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;

import java.io.File;
import java.util.ArrayList;

import asUtilsPorts.Weather.RadarWorker;

public class Radar {

	public void fetchRadars() {

		ThreadRipper tr = new ThreadRipper();
        CommonBeans cb = new CommonBeans();
        
		final String radPath = cb.getPersistTomcat()+"/Get/Radar";
		final File radPathObj = new File(radPath);
		
		if(!radPathObj.exists()) {
			radPathObj.mkdirs();
		}
		
		ArrayList<Runnable> rp = new ArrayList<Runnable>();
		rp.add(() -> RadarWorker.fetch("1"));
		rp.add(() -> RadarWorker.fetch("2"));
		rp.add(() -> RadarWorker.fetch("3"));
		rp.add(() -> RadarWorker.fetch("4"));
		rp.add(() -> RadarWorker.fetch("5"));
		rp.add(() -> RadarWorker.fetch("6"));
		//rp.add(() -> RadarWorker.fetch("7"));
		//rp.add(() -> RadarWorker.fetch("8"));
		
		tr.runProcesses(rp, false, false);
		
	}

}
