/*
by Anthony Stump
Created: 30 Aug 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;

import java.io.File;
import java.sql.Connection;

import asUtilsPorts.Weather.RadarWorker;
import asUtils.Shares.JunkyBeans;

public class Radar {

	public static void fetchRadars() {
        
        JunkyBeans junkyBeans = new JunkyBeans();
        CommonBeans cb = new CommonBeans();
		final String radPath = cb.getPersistTomcat()+"/Get/Radar";
		final File radPathObj = new File(radPath);
		
		if(!radPathObj.exists()) {
			radPathObj.mkdirs();
		}

		Thread r01 = new Thread(() -> { RadarWorker.fetch("1"); });
		Thread r02 = new Thread(() -> { RadarWorker.fetch("2"); });
		Thread r03 = new Thread(() -> { RadarWorker.fetch("3"); });
		Thread r04 = new Thread(() -> { RadarWorker.fetch("4"); });
		Thread r05 = new Thread(() -> { RadarWorker.fetch("5"); });
		Thread r06 = new Thread(() -> { RadarWorker.fetch("6"); });
/*		Thread r07 = new Thread(() -> { RadarWorker.fetch("7"); });
		Thread r08 = new Thread(() -> { RadarWorker.fetch("8"); }); */
		Thread rList[] = { r01, r02, r03, r04, r05, r06/* , r07, r08 */ };
		for (Thread thread : rList) { thread.start(); }
		for (int i = 0; i < rList.length; i++) { try { rList[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
				
		
	}

}
