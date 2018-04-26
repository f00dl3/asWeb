/*
by Anthony Stump
Created: 25 Apr 2018
*/

package asWebRest.hookers;

public class SnmpOidConversions {
    
    public static String translate(String nonOid) {
        String oid = "";
        switch(nonOid) {
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196608": oid = ".1.3.6.1.2.1.25.3.3.1.2.196608"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196609": oid = ".1.3.6.1.2.1.25.3.3.1.2.196609"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196610": oid = ".1.3.6.1.2.1.25.3.3.1.2.196610"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196611": oid = ".1.3.6.1.2.1.25.3.3.1.2.196611"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196612": oid = ".1.3.6.1.2.1.25.3.3.1.2.196612"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196613": oid = ".1.3.6.1.2.1.25.3.3.1.2.196613"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196614": oid = ".1.3.6.1.2.1.25.3.3.1.2.196614"; break;
            case "HOST-RESOURCES-MIB::hrProcessorLoad.196615": oid = ".1.3.6.1.2.1.25.3.3.1.2.196615"; break;
            case "HOST-RESOURCES-MIB::hrStorageSize.1": oid = ".1.3.6.1.2.1.25.2.3.1.5.1"; break;
            case "HOST-RESOURCES-MIB::hrStorageUsed.1": oid = ".1.3.6.1.2.1.25.2.3.1.6.1"; break;
            case "HOST-RESOURCES-MIB::hrStorageUsed.6": oid = ".1.3.6.1.2.1.25.2.3.1.6.6"; break;
            case "HOST-RESOURCES-MIB::hrStorageUsed.7": oid = ".1.3.6.1.2.1.25.2.3.1.6.7"; break;
            case "HOST-RESOURCES-MIB::hrStorageUsed.31": oid = ".1.3.6.1.2.1.25.2.3.1.6.31"; break;
            case "HOST-RESOURCES-MIB::hrStorageUsed.68": oid = ".1.3.6.1.2.1.25.2.3.1.6.68"; break;
            case "HOST-RESOURCES-MIB::hrSystemUptime.0": oid = ".1.3.6.1.2.1.25.1.1.0"; break;
            case "HOST-RESOURCES-MIB::hrSystemProcesses.0": oid = ".1.3.6.1.2.1.25.1.6.0"; break;
            case "IF-MIB::ifInOctets.2": oid = ".1.3.6.1.2.1.2.2.1.10.2"; break;
            case "IF-MIB::ifOutOctets.2": oid = ".1.3.6.1.2.1.2.2.1.16.2"; break;
            case "LM-SENSORS-MIB::lmTempSensorsValue.22": oid = ".1.3.6.1.4.1.2021.13.16.2.1.3.22"; break;
            case "LM-SENSORS-MIB::lmTempSensorsValue.35": oid = ".1.3.6.1.4.1.2021.13.16.2.1.3.35"; break;
            case "UCD-SNMP-MIB::laLoad.1": oid = ".1.3.6.1.4.1.2021.10.1.3.1"; break;
            case "UCD-SNMP-MIB::ssIORawReceived.0": oid = ".1.3.6.1.4.1.2021.11.58.0"; break;
            case "UCD-SNMP-MIB::ssIORawSent.0": oid = ".1.3.6.1.4.1.2021.11.57.0"; break;
        }
        return oid;
    }

}
