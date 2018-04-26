/*
by Anthony Stump
Created: 25 Apr 2018
Updated: 26 Apr 2018
*/

package asWebRest.hookers;

public class SnmpOidConversions {
    
    public static String translate(String nonOid) {
        String oid = "";
        switch(nonOid) {
            case "dskPercentNode.3": oid = ".1.3.6.1.4.1.2021.9.1.10.3"; break;
            case "dskPercentNode.5": oid = ".1.3.6.1.4.1.2021.9.1.10.5"; break;
            case "hrProcessorLoad.196608": oid = ".1.3.6.1.2.1.25.3.3.1.2.196608"; break;
            case "hrProcessorLoad.196609": oid = ".1.3.6.1.2.1.25.3.3.1.2.196609"; break;
            case "hrProcessorLoad.196610": oid = ".1.3.6.1.2.1.25.3.3.1.2.196610"; break;
            case "hrProcessorLoad.196611": oid = ".1.3.6.1.2.1.25.3.3.1.2.196611"; break;
            case "hrProcessorLoad.196612": oid = ".1.3.6.1.2.1.25.3.3.1.2.196612"; break;
            case "hrProcessorLoad.196613": oid = ".1.3.6.1.2.1.25.3.3.1.2.196613"; break;
            case "hrProcessorLoad.196614": oid = ".1.3.6.1.2.1.25.3.3.1.2.196614"; break;
            case "hrProcessorLoad.196615": oid = ".1.3.6.1.2.1.25.3.3.1.2.196615"; break;
            case "hrStorageSize.1": oid = ".1.3.6.1.2.1.25.2.3.1.5.1"; break;
            case "hrStorageSize.3": oid = ".1.3.6.1.2.1.25.2.3.1.5.3"; break;
            case "hrStorageSize.6": oid = ".1.3.6.1.2.1.25.2.3.1.5.6"; break;
            case "hrStorageSize.7": oid = ".1.3.6.1.2.1.25.2.3.1.5.7"; break;
            case "hrStorageSize.8": oid = ".1.3.6.1.2.1.25.2.3.1.5.8"; break;
            case "hrStorageSize.10": oid = ".1.3.6.1.2.1.25.2.3.1.5.10"; break;
            case "hrStorageSize.31": oid = ".1.3.6.1.2.1.25.2.3.1.5.31"; break;
            case "hrStorageUsed.1": oid = ".1.3.6.1.2.1.25.2.3.1.6.1"; break;
            case "hrStorageUsed.3": oid = ".1.3.6.1.2.1.25.2.3.1.6.3"; break;
            case "hrStorageUsed.6": oid = ".1.3.6.1.2.1.25.2.3.1.6.6"; break;
            case "hrStorageUsed.7": oid = ".1.3.6.1.2.1.25.2.3.1.6.7"; break;
            case "hrStorageUsed.8": oid = ".1.3.6.1.2.1.25.2.3.1.6.8"; break;
            case "hrStorageUsed.10": oid = ".1.3.6.1.2.1.25.2.3.1.6.10"; break;
            case "hrStorageUsed.31": oid = ".1.3.6.1.2.1.25.2.3.1.6.31"; break;
            case "hrStorageUsed.68": oid = ".1.3.6.1.2.1.25.2.3.1.6.68"; break;
            case "hrSystemUptime.0": oid = ".1.3.6.1.2.1.25.1.1.0"; break;
            case "hrSystemProcesses.0": oid = ".1.3.6.1.2.1.25.1.6.0"; break;
            case "ifInOctets.2": oid = ".1.3.6.1.2.1.2.2.1.10.2"; break;
            case "ifOutOctets.2": oid = ".1.3.6.1.2.1.2.2.1.16.2"; break;
            case "laLoad.1": oid = ".1.3.6.1.4.1.2021.10.1.3.1"; break;
            case "laLoad.2": oid = ".1.3.6.1.4.1.2021.10.1.3.2"; break;
            case "laLoad.3": oid = ".1.3.6.1.4.1.2021.10.1.3.3"; break;
            case "lmTempSensorsValue.22": oid = ".1.3.6.1.4.1.2021.13.16.2.1.3.22"; break;
            case "lmTempSensorsValue.35": oid = ".1.3.6.1.4.1.2021.13.16.2.1.3.35"; break;
            case "myComDelete.0": oid = ".1.3.6.1.4.1.20267.200.1.79.0"; break;
            case "myComInsert.0": oid = ".1.3.6.1.4.1.20267.200.1.77.0"; break;
            case "myComReplace.0": oid = ".1.3.6.1.4.1.20267.200.1.80.0"; break;
            case "myComSelect.0": oid = ".1.3.6.1.4.1.20267.200.1.78.0"; break;
            case "myComUpdate.0": oid = ".1.3.6.1.4.1.20267.200.1.76.0"; break;
            case "ssIORawReceived.0": oid = ".1.3.6.1.4.1.2021.11.58.0"; break;
            case "ssIORawSent.0": oid = ".1.3.6.1.4.1.2021.11.57.0"; break;
        }
        return oid;
    }

}
