/*
by Anthony Stump
adapted from GitHub "Akirad"
Created: 22 Apr 2018
 */

package asWebRest.hookers;

import java.io.IOException;
import java.util.List;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
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
    
    private String execSnmpWalk() throws IOException {
        
        String returnData = "";
        Address targetAddress = GenericAddress.parse("udp:" + targetAddr + "/" + portNum);
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(commStr));
        target.setAddress(targetAddress);
        target.setRetries(3);
        target.setTimeout(1000 * 3);
        target.setVersion(snmpVersion);
        OID oid;
        try {
            oid = new OID(translateNameToOID(oidStr));
        } catch (Exception e) {
            System.err.println("Failed to understand the OID or object name.");
            throw e;
        }
        
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, oid);
        if(events == null || events.size() == 0) {
            returnData += "No results returned.";
            System.exit(1);
        }
        
        for (TreeEvent event : events) {
            
            if (event == null) { continue; }
            if (event.isError()) {
                returnData += "oid [" + oid + "] " + event.getErrorMessage();
                continue;
            }
            
            VariableBinding[] varBindings = event.getVariableBindings();
            if(varBindings == null || varBindings.length == 0) { continue; }
            for(VariableBinding varBinding : varBindings) {
                if (varBinding == null) { continue; }
                returnData += varBinding.getOid() + " : " +
                        varBinding.getVariable().getSyntaxString() + " : " +
                        varBinding.getVariable();
            }
            
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
            snmpBack = snmpwalk.execSnmpWalk();
        }
        catch(Exception e) {
            snmpBack = "----- An Exception happened as follows. Please confirm the usage etc. -----\n" + e.getMessage();
            e.printStackTrace();
        }
        return snmpBack;
    }
}
