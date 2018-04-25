/*
by Anthony Stump
Created: 22 Apr 2018
Updated: 25 Apr 2018
 */

package asWebRest.hookers;

import asWebRest.secure.SNMPBeans;
import java.io.IOException;
import java.util.List;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.TSM;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class SnmpWalk {
    
    private String targetAddr;
    private String oidStr;
    private String commStr;
    private int snmpVersion;
    private String portNum;
    private String usage;
    
    public SnmpWalk() throws IOException {
        targetAddr = "127.0.0.1";
        oidStr = null;
        commStr = "public";
        snmpVersion = SnmpConstants.version3;
        portNum = "161";
        usage = "Usage: snmpWalk [ -c commName -p portNum -v snmpVer] targetAddr oid";
    }
    
    private String execSnmpWalk(String oidRequest) throws IOException {
    
        SNMPBeans snmpBeans = new SNMPBeans();
        
        String returnData = "";
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Address targetAddress = GenericAddress.parse("udp:" + targetAddr + "/" + portNum);
        Snmp snmp = new Snmp(transport);
        
        OctetString localEngineId = new OctetString(MPv3.createLocalEngineID());
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineId, 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        
        OctetString securityName = new OctetString(snmpBeans.getSnmpUser());
        OID authProtocol = AuthMD5.ID;
        OID privProtocol = PrivDES.ID;
        OctetString authPassphrase = new OctetString(snmpBeans.getSnmpPass());
        OctetString privPassphrase = new OctetString(snmpBeans.getSnmpPass());
        
        snmp.getUSM().addUser(securityName, new UsmUser(securityName, authProtocol, authPassphrase, privProtocol, privPassphrase));
        SecurityModels.getInstance().addSecurityModel(new TSM(localEngineId, false));
        
        UserTarget target = new UserTarget();
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(securityName);
        target.setAddress(targetAddress);
        target.setRetries(3);
        target.setTimeout(1000 * 3);
        target.setVersion(snmpVersion);

        transport.listen();
        
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oidRequest)));
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        ResponseEvent response = snmp.get(pdu, target); // errors here!
        if(response != null) {
            returnData += "Got response from Agent!";
            PDU responsePDU = response.getResponse();
            if(responsePDU != null) {
                int errorStatus = responsePDU.getErrorStatus();
                int errorIndex = responsePDU.getErrorIndex();
                String errorStatusText = responsePDU.getErrorStatusText();
                if(errorStatus == PDU.noError) {
                    returnData += "\nSnmp v3 Get response = " + responsePDU.getVariableBindings();
                } else {
                    returnData += "\nError: request failed" +
                            "\nError status: " + errorStatus +
                            "\nError index: " + errorIndex +
                            "\nError text: " + errorStatusText;
                }
            } else {
                returnData += "\nError: Response PDU is null!";
            }
        } else {
            returnData += "\nAgent timeout...";
        }
        
        snmp.close();
        
        return returnData;
        
    }
    
    private String translateNameToOID(String oidStr) {
        switch (oidStr) {
            case "mib-2": oidStr = ".1.3.6.1.2.1"; break;
            case "mib2": oidStr = ".1.3.6.1.2.1"; break;
            case "system": oidStr = ".1.3.6.1.2.1.1"; break;
            case "interfaces": oidStr = ".1.3.6.1.2.1.2"; break;
            case "at": oidStr = ".1.3.6.1.2.1.3"; break;
            case "ip": oidStr = ".1.3.6.1.2.1.4"; break;
            case "icmp": oidStr = ".1.3.6.1.2.1.5"; break;
            case "tcp": oidStr = ".1.3.6.1.2.1.6"; break;
            case "udp": oidStr = ".1.3.6.1.2.1.7"; break;
            case "egp": oidStr = ".1.3.6.1.2.1.8"; break;
            case "transmission": oidStr = ".1.3.6.1.2.1.10"; break;
            case "snmp": oidStr = ".1.3.6.1.2.1.11"; break;
        }
        return oidStr;
    }

    private void setArgs(String[] args) {
        
        if(args.length < 2) {
            System.err.println(usage);
            System.exit(1);
        }

        for (int i=0; i<args.length; i++) {
            if("-c".equals(args[i])) {
                commStr = args[++i];
            }
            else if ("-v".equals(args[i])) {
                switch(args[++i]) {
                    case "1": snmpVersion = SnmpConstants.version1;
                    case "2": snmpVersion = SnmpConstants.version2c;
                    case "3": snmpVersion = SnmpConstants.version3;
                }
            }
            else if ("-p".equals(args[i])) {
                portNum = args[++i];
            }
            else{
                targetAddr = args[i++];
                oidStr = args[i];
            }
        }
        if(targetAddr == null || oidStr == null) {
            System.err.println(usage);
            System.exit(1);
        }
    }
    
    public String getSnmpWalk(String[] args) {
        String snmpBack = "SnmpWalk did not run yet or failed to run!";
        try{
            SnmpWalk snmpwalk = new SnmpWalk();
            snmpwalk.setArgs(args);
            snmpBack = snmpwalk.execSnmpWalk(".1.3.6.1.2.1.1.1.0");
        }
        catch(Exception e) {
            snmpBack = "----- An Exception happened as follows. Please confirm the usage etc. -----\n" + e.getMessage();
            e.printStackTrace();
        }
        return snmpBack;
    }
}
