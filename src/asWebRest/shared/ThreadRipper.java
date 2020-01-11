/*
by Anthony Stump
Created: 30 Dec 2019
Updated: 7 Jan 2020
 */

package asWebRest.shared;

import java.util.ArrayList;
import java.util.List;

public class ThreadRipper {
    
	private int maxThreads = 4;
	private int minThreads = maxThreads/2;
	private int maxThreadsHost = 8;	
	
	private void execThreads(List<Thread> tPool, boolean asynch) {
		if(!asynch) { 
			for (int j = 0; j < tPool.size(); j++) {
				try {  tPool.get(j).join();  } catch (Exception nx) { }
			}
		}
	}
	
	public String runProcesses(List<Runnable> threadList, boolean pool, boolean asynch) {

		ArrayList<Thread> tPool = new ArrayList<>();
		String runResults = "";
		int taskNo = 0;
		int procLoop = 0;
		int inPool = 0;
		
		if(!pool) {			
			for(Runnable exTask : threadList) {
				runResults += "Task [" + taskNo + "] Loop [" + procLoop + "] Thread [" + inPool + "]: " + exTask + "\n";
				Thread tThread = new Thread(exTask);
				if(inPool <= maxThreads) {
					try { 
						tThread.start();
						tPool.add(tThread); 
					} catch (Exception e) { }
					inPool++;
				} else {
					procLoop++;
					execThreads(tPool, asynch);
					inPool = 0;
					tPool.clear();
					//tThread.start();
					tPool.add(tThread);
				
					try { 
						if(!tPool.isEmpty()) {				
							tThread.start();
							execThreads(tPool, asynch);
							inPool = 0;
							tPool = null;
						}
					} catch (Exception e) { }
				}
				taskNo++;
			}
		} else {
			for(Runnable exTask : threadList) {
				runResults += "Task [" + taskNo + "] Loop [" + procLoop + "] Thread [" + inPool + "]: " + exTask + "\n";
				Thread tThread = new Thread(exTask);
				tThread.start();
				tPool.add(tThread); 
			}
			execThreads(tPool, asynch);
		}
		
		return runResults;
		
	}
	
	public int getMaxThreads() { return maxThreads; }
	public int getMaxThreadsHost() { return maxThreadsHost; }
	public int getMinThreads() { return minThreads; }
    
}
