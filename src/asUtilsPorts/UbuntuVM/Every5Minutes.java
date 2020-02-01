/*
by Anthony Stump
Created: 28 Dec 2019
Updated: 31 Jan 2020
 */

package asUtilsPorts.UbuntuVM;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;

public class Every5Minutes {
    
    public static void execJobs5() {
        
        JunkyBeans jb = new JunkyBeans();
        
        try {
            System.out.println("Executing calls to asWeb API for 5 minute interval fetches:");
		System.out.println("DEBUG: jb.getApi() = " + jb.getApi());
            SSLHelper.getConnection(jb.getApi())
                    .data("doWhat", "Feeds")
                    .data("interval", "5m")
                    .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
        execJobs5();
    }
    
}
