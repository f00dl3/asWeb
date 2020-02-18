/*
by Anthony Stump
Created: 14 Aug 2017
Split to UVM: 16 Oct 2019
Updated: 17 Feb 2020
*/

package asUtilsPorts.UbuntuVM;

import java.io.*;
import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;

public class Feeds {

	public static void main(String[] args) {

        JunkyBeans junkyBeans = new JunkyBeans();
                
		final File ramDrive = junkyBeans.getRamDrive();
		final String ramTemp = ramDrive.getPath()+"/rssXMLFeedsJ";
		final File ramTempF = new File(ramTemp);

		String freq = args[0];

		ramTempF.mkdirs();

		if (freq.equals("OneMinute")) {
			
                try {
                    System.out.println("Executing calls to asWeb API for 1 minute interval fetches:");
                	System.out.println("DEBUG: " + junkyBeans.getApi());
            		SSLHelper.getConnection(junkyBeans.getApi())
                        .data("doWhat", "Feeds")
                        .data("interval", "1m")
                        .post();
                } catch (Exception e) {
                    e.printStackTrace();
                }
			
		}

		if (freq.equals("TwoMinute")) {
			
                try {
                    System.out.println("Executing calls to asWeb API for 2 minute interval fetches:");
                	System.out.println("DEBUG: " + junkyBeans.getApi());
            		SSLHelper.getConnection(junkyBeans.getApi())
                        .data("doWhat", "Feeds")
                        .data("interval", "2m")
                        .post();
                } catch (Exception e) {
                    e.printStackTrace();
                }
			
		}
		
		if (freq.equals("Hour")) {
			
                try {
                    System.out.println("Executing calls to asWeb API for 1 hour interval fetches:");
                    SSLHelper.getConnection(junkyBeans.getApi())
                            .data("doWhat", "Feeds")
                            .data("interval", "1h")
                            .post();
                } catch (Exception e) {
                    e.printStackTrace();
                }
               
        }

	}

}
