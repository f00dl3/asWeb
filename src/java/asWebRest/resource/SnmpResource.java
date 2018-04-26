/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 1 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetSnmpAction;
import asWebRest.dao.SnmpDAO;
import asWebRest.hookers.SnmpWalk;
import asWebRest.shared.WebCommon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SnmpResource extends ServerResource {
    
    @Get
    public String represent() {
        List<String> qParams = new ArrayList<>();
        qParams.add(0, "1"); //Test
        qParams.add(1, "1"); //Step
        qParams.add(2, "1"); //DateTest
        qParams.add(3, "20180224"); //Date
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        JSONArray snmpData = getSnmpAction.getRouter(qParams);  
        return snmpData.toString();
    }
    
    @Post
    public String represent(Representation argsIn) {

        WebCommon wc = new WebCommon();
        
        List<String> qParams = new ArrayList<>();      
        List<String> inParams = new ArrayList<>();      
        JSONObject mergedResults = new JSONObject();
        final Form argsInForm = new Form(argsIn);
        
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        if(doWhat != null) {
            switch (doWhat) {
                
                case "RapidSNMP":
                    double loadIndex = 0.00; int runningProcs = 0; String uptime = "";
                    int cpu1Load = 0; int cpu2Load = 0; int cpu3Load = 0; int cpu4Load = 0;
                    int cpu5Load = 0; int cpu6Load = 0; int cpu7Load = 0; int cpu8Load = 0;
                    long memPhysSize = 0; long memPhysUsed = 0; long memBuffUsed = 0; long memCachUsed = 0;
                    long hdd0Used = 0; long hdd1Used = 0;
                    long diskIoRx = 0; long diskIoTx = 0;
                    int tempCase = 0; int tempCPU = 0;
                    long eth0In = 0; long eth0Out = 0;
                    try { 
                        SnmpWalk snmpWalk = new SnmpWalk();
                        try { loadIndex = Double.parseDouble(snmpWalk.get("UCD-SNMP-MIB::laLoad.1")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu1Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196608")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu2Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196609")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu3Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196610")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu4Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196611")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu5Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196612")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu6Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196613")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu7Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196614")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu8Load = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196615")); } catch (Exception e) { e.printStackTrace(); }
                        try { memPhysSize = Long.parseLong(snmpWalk.get("HOST-RESOURCES-MIB::hrStorageSize.1")); } catch (Exception e) { e.printStackTrace(); }
                        try { memPhysUsed = Long.parseLong(snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.1")); } catch (Exception e) { e.printStackTrace(); }
                        try { memBuffUsed = Long.parseLong(snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.6")); } catch (Exception e) { e.printStackTrace(); }
                        try { memCachUsed = Long.parseLong(snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.7")); } catch (Exception e) { e.printStackTrace(); }
                        try { hdd0Used = Long.parseLong(snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.31")); } catch (Exception e) { e.printStackTrace(); }
                        try { hdd1Used = Long.parseLong(snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.68")); } catch (Exception e) { e.printStackTrace(); }
                        try { diskIoRx = Long.parseLong(snmpWalk.get("UCD-SNMP-MIB::ssIORawReceived.0")); } catch (Exception e) { e.printStackTrace(); }
                        try { diskIoTx = Long.parseLong(snmpWalk.get("UCD-SNMP-MIB::ssIORawSent.0")); } catch (Exception e) { e.printStackTrace(); }
                        try { eth0In = Long.parseLong(snmpWalk.get("IF-MIB::ifInOctets.2")); } catch (Exception e) { e.printStackTrace(); }
                        try { eth0Out = Long.parseLong(snmpWalk.get("IF-MIB::ifOutOctets.2")); } catch (Exception e) { e.printStackTrace(); }
                        try { tempCase = Integer.parseInt(snmpWalk.get("LM-SENSORS-MIB::lmTempSensorsValue.22")); } catch (Exception e) { e.printStackTrace(); }
                        try { tempCPU = Integer.parseInt(snmpWalk.get("LM-SENSORS-MIB::lmTempSensorsValue.35")); } catch (Exception e) { e.printStackTrace(); }
                        try { runningProcs = Integer.parseInt(snmpWalk.get("HOST-RESOURCES-MIB::hrSystemProcesses.0")); } catch (Exception e) { e.printStackTrace(); }
                        try { uptime = snmpWalk.get("HOST-RESOURCES-MIB::hrSystemUptime.0"); } catch (Exception e) { e.printStackTrace(); }
                    } catch (IOException ix) {
                        ix.printStackTrace();
                    }
                    JSONObject snmpData = new JSONObject();
                    snmpData
                        .put("loadIndex", loadIndex).put("runningProcs", runningProcs).put("uptime", uptime)
                        .put("cpu1Load", cpu1Load).put("cpu2Load", cpu2Load).put("cpu3Load", cpu3Load).put("cpu4Load", cpu4Load)
                        .put("cpu5Load", cpu5Load).put("cpu6Load", cpu6Load).put("cpu7Load", cpu7Load).put("cpu8Load", cpu8Load)
                        .put("memPhysSize", memPhysSize).put("memPhysUsed", memPhysUsed).put("memBuffUsed", memBuffUsed).put("memCachUsed", memCachUsed)
                        .put("hdd0Used", hdd0Used).put("hdd1Used", hdd1Used)
                        .put("diskIoRx", diskIoRx).put("diskIoTx", diskIoTx)
                        .put("tempCase", tempCase).put("tempCPU", tempCPU)
                        .put("eth0In", eth0In).put("eth0Out", eth0Out);
                    returnData = snmpData.toString();
                    break;
                    
            }
        }
        
        return returnData;
    }
}
