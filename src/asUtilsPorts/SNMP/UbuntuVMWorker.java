/*
Ubuntu VM Worker Class
by Anthony Stump
Created: 29 Apr 2019
Updated: 18 Jan 2020
 */

package asUtilsPorts.SNMP;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileNotFoundException;

public class UbuntuVMWorker {
    
        public void main() {
            
            CommonBeans cb = new CommonBeans();
            WebCommon wc = new WebCommon();
        
            final String ramPath = cb.getPathChartCache();
        
            final File duMySQLFile = new File(ramPath+"/UVM_duMySQL.txt");
            final File ns5File = new File(ramPath+"/UVM_ns5out.txt");
        
            Thread s1t1 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("netstat -W | grep \"ass\" | grep \"ESTAB\"", ns5File, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
            Thread s1t2 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("du /var/lib/mysql", duMySQLFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
            Thread thListA[] = { s1t1, s1t2 };
            for (Thread thread : thListA) { thread.start(); try { thread.join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
        
        }
        
}
