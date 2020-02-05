/*
by Anthony Stump
Created: 30 Dec 2019
Debug split: 4 Feb 2020
Updated: 4 Feb 2020
 */

package asWebRest.shared;

import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThreadRipperDebug {
    
	private int maxThreads = 4;
	private int minThreads = maxThreads/2;
	private int maxThreadsHost = 8;	
	
	private String execThreads(List<Thread> tPool, boolean asynch) {
		String returnData = "";
		if(!asynch) { 
			returnData += "DEBUG: Not asynch - joining threads!";
			for (int j = 0; j < tPool.size(); j++) {
				try {  tPool.get(j).join();  } catch (Exception nx) { }
			}
		} else {
			returnData += "DEBUG: Asynchronous. Not joining threads.\n";
		}
		return returnData;
	}
	
	public String runProcesses(List<Runnable> threadList, boolean pool, boolean asynch) {

		ArrayList<Thread> tPool = new ArrayList<>();
		String runResults = "";
		int taskNo = 0;
		int procLoop = 0;
		int inPool = 0;
		
		if(!pool) {
			runResults += "DEBUG: Non-pooled threads.\n";
			for(Runnable exTask : threadList) {
				runResults += "Task [" + taskNo + "] Loop [" + procLoop + "] Thread [" + inPool + "]: " + exTask + "\n";
				Thread tThread = new Thread(exTask);
				if(inPool <= maxThreads) {
					runResults += "DEBUG: Thread " + taskNo + " in range of max" + maxThreads + "\n";
					try { 
						tThread.start();
						tPool.add(tThread); 
					} catch (Exception e) { }
					inPool++;
				} else {
					runResults += "DEBUG: Thread " + taskNo + " not in range of threads" + maxThreads + "\n";
					procLoop++;
					runResults += execThreads(tPool, asynch);
					inPool = 0;
					tPool.clear();
					//tThread.start();
					tPool.add(tThread);
				
					try { 
						if(!tPool.isEmpty()) {		
							runResults += "DEBUG: Thread pool not empty\n";		
							tThread.start();
							runResults += execThreads(tPool, asynch);
							inPool = 0;
							tPool.clear();
						} else {
							runResults += "DEBUG: Thread pool is empty!\n";
						}
					} catch (Exception e) { }
				}
				taskNo++;
			}
		} else {
			runResults += "DEBUG: Pooled threads.\n";
			for(Runnable exTask : threadList) {
				runResults += "Task [" + taskNo + "] Loop [" + procLoop + "] Thread [" + inPool + "]: " + exTask + "\n";
				Thread tThread = new Thread(exTask);
				tThread.start();
				tPool.add(tThread); 
			}
			runResults += execThreads(tPool, asynch);
		}
		
		return runResults;
		
	}
	
	public int getMaxThreads() { return maxThreads; }
	public int getMaxThreadsHost() { return maxThreadsHost; }
	public int getMinThreads() { return minThreads; }
    
	public String selfTest(int testsToRun, boolean isSys) {
		String returnData = "NOT RAN YET";
		ArrayList<Runnable> testList = new ArrayList<Runnable>();
		for(int i = 0; i < testsToRun; i++) {
			final int thisIterator = i;
			testList.add(() -> testString(thisIterator + " of " + testsToRun, isSys, thisIterator));
		}
		ThreadRipperDebug tr = new ThreadRipperDebug();
		returnData = tr.runProcesses(testList, true, false);		
		return returnData;
	}
	
	private String testString(String what, boolean isSys, int place) {
		JunkyBeans jb = new JunkyBeans();
		WebCommon wc = new WebCommon();
		if(isSys) {
			final File sysTestBase = new File(jb.getRamDrive().toString() + "/ThreadTests");
			if(!sysTestBase.exists()) { try { sysTestBase.mkdirs(); } catch (Exception e) { e.printStackTrace(); } }
			String tIterString = Integer.toString(place);
			final File thisOut = new File(sysTestBase.toString()+"/"+tIterString+".tmp");
			try { wc.varToFile(tIterString, thisOut, false); } catch (Exception e) { e.printStackTrace(); }
			try { Thread.sleep(5000); } catch (Exception e) { e.printStackTrace(); }
		}
		System.out.println(what);
		return what + "\n";
	}

	public static void main(String[] args) {
		String returnData = "NOT ITERATED OUT!";
		ThreadRipperDebug tr = new ThreadRipperDebug();
		int ttp = 1;
		try { ttp = Integer.parseInt(args[0]); } catch (Exception e) { e.printStackTrace(); }
		returnData += tr.selfTest(ttp, true);
		System.out.println(returnData);
	}
	
}
