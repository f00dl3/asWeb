/*
by Anthony Stump
Created: 25 Apr 2018
Updated: 2 May 2018
*/

package asWebRest.hookers;

public class SnmpOidConversions {
    
    public static String translate(String nonOidName, String nonOidObject) {
        String oid = "";
        switch(nonOidName) {
            case "dskPercentNode": oid = ".1.3.6.1.4.1.2021.9.1.10." + nonOidObject; break;
            case "hrProcessorLoad": oid = ".1.3.6.1.2.1.25.3.3.1.2." + nonOidObject; break;
            case "hrStorageSize": oid = ".1.3.6.1.2.1.25.2.3.1.5." + nonOidObject; break;
            case "hrStorageUsed": oid = ".1.3.6.1.2.1.25.2.3.1.6." + nonOidObject; break;
            case "hrSystemUptime": oid = ".1.3.6.1.2.1.25.1.1." + nonOidObject; break;
            case "hrSystemProcesses": oid = ".1.3.6.1.2.1.25.1.6." + nonOidObject; break;
            case "ifInOctets": oid = ".1.3.6.1.2.1.2.2.1.10." + nonOidObject; break;
            case "ifOutOctets": oid = ".1.3.6.1.2.1.2.2.1.16." + nonOidObject; break;
            case "laLoad": oid = ".1.3.6.1.4.1.2021.10.1.3." + nonOidObject; break;
            case "lmTempSensorsValue": oid = ".1.3.6.1.4.1.2021.13.16.2.1.3." + nonOidObject; break;
            case "myComDelete": oid = ".1.3.6.1.4.1.20267.200.1.79." + nonOidObject; break;
            case "myComInsert": oid = ".1.3.6.1.4.1.20267.200.1.77." + nonOidObject; break;
            case "myComReplace": oid = ".1.3.6.1.4.1.20267.200.1.80." + nonOidObject; break;
            case "myComSelect": oid = ".1.3.6.1.4.1.20267.200.1.78." + nonOidObject; break;
            case "myComUpdate": oid = ".1.3.6.1.4.1.20267.200.1.76." + nonOidObject; break;
            case "ssIORawReceived": oid = ".1.3.6.1.4.1.2021.11.58." + nonOidObject; break;
            case "ssIORawSent": oid = ".1.3.6.1.4.1.2021.11.57." + nonOidObject; break;
        }
        return oid;
    }

}
