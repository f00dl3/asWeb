/* 
SNMP Walk -> Database
Version 5
Java created: 14 Aug 2017
Last updated: 1 Feb 2020
*/

package asUtilsPorts;

import asUtilsPorts.SNMP.DesktopPusher;

public class Walk2DBv5 {
	
	public static void main (String[] args) {
        
        final String thisNode = args[0];                			
          
		switch(thisNode) {
                    
                    case "Desktop": 
                        DesktopPusher desktop = new DesktopPusher();
                        desktop.snmpDesktopPusher();
                        break;
                        
                }
                
        }
    
}
