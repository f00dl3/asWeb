/*
by Anthony Stump
Created: 22 Apr 2018
Updated: 2 May 2018
 */

package asWebRest.hookers;

import asWebRest.secure.SNMPBeans;
import asWebRest.shared.WebCommon;
import java.io.IOException;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
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
    
    private String execSnmpWalk(String node, String requestString, String requestObject) throws IOException {
    
        SnmpOidConversions stringToOid = new SnmpOidConversions();
        SNMPBeans snmpBeans = new SNMPBeans();
        
        String oidRequest = stringToOid.translate(requestString, requestObject);
        
        String returnData = "";
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Address targetAddress = GenericAddress.parse("udp:" + targetAddr + "/" + portNum);
        Snmp snmp = new Snmp(transport);
        
        String tSecName = "";
        String tSecPass = "";
        
        switch (node) {
            
            case "desktop": default:
                targetAddr = "127.0.0.1";
                tSecName = snmpBeans.getSnmpUser();
                tSecPass = snmpBeans.getSnmpPass();
                break;
                
        }
        
        OctetString localEngineId = new OctetString(MPv3.createLocalEngineID());
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineId, 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        
        OctetString securityName = new OctetString(tSecName);
        OID authProtocol = AuthMD5.ID;
        OID privProtocol = PrivDES.ID;
        OctetString authPassphrase = new OctetString(tSecPass);
        OctetString privPassphrase = new OctetString(tSecPass);
        
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
        
        PDU pdu = new ScopedPDU();
        pdu.add(new VariableBinding(new OID(oidRequest)));
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));

        ResponseEvent response = snmp.get(pdu, target);
        if(response != null) {
            PDU responsePDU = response.getResponse();
            if(responsePDU != null) {
                int errorStatus = responsePDU.getErrorStatus();
                int errorIndex = responsePDU.getErrorIndex();
                String errorStatusText = responsePDU.getErrorStatusText();
                if(errorStatus == PDU.noError) {
                    returnData += "SUCCESS " + responsePDU.getVariableBindings();
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
    
    public String get(String node, String requestString, String requestObject) {
        WebCommon wc = new WebCommon();
        String snmpBack = "SnmpWalk did not run yet or failed to run!";
        String snmpBackTmp = "";
        try{
            SnmpWalk snmpwalk = new SnmpWalk();
            snmpBackTmp = snmpwalk.execSnmpWalk(node, requestString, requestObject);
        }
        catch(Exception e) {
            snmpBackTmp = "----- An Exception happened as follows. Please confirm the usage etc. -----\n" + e.getMessage();
            e.printStackTrace();
        }
        if(wc.isSet(snmpBackTmp) && snmpBackTmp.contains(" = ")) {
            String[] snmpBackArray = snmpBackTmp.replace(" = ", ";").split(";");
            snmpBack = snmpBackArray[1].replace("]","");
        } else {
            snmpBack = snmpBackTmp;
        }
        return snmpBack;
    }
}
