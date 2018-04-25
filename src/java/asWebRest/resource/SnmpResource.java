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
                    String cpu1Load = ""; String cpu2Load = ""; String cpu3Load = ""; String cpu4Load = "";
                    String cpu5Load = ""; String cpu6Load = ""; String cpu7Load = ""; String cpu8Load = "";
                    String memPhysSize = ""; String memPhysUsed = ""; String memBuffUsed = ""; String memCachUsed = "";
                    String hdd0Used = ""; String hdd1Used = "";
                    String diskIoRx = ""; String diskIoTx = "";
                    String tempCase = ""; String tempCPU = "";
                    try { 
                        SnmpWalk snmpWalk = new SnmpWalk();
                        cpu1Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196608");
                        cpu2Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196609");
                        cpu3Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196610");
                        cpu4Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196611");
                        cpu5Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196612");
                        cpu6Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196613");
                        cpu7Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196614");
                        cpu8Load = snmpWalk.get("HOST-RESOURCES-MIB::hrProcessorLoad.196615");
                        memPhysSize = snmpWalk.get("HOST-RESOURCES-MIB::hrStorageSize.1");
                        memPhysUsed = snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.1");
                        memBuffUsed = snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.6");
                        memCachUsed = snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.7");
                        hdd0Used = snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.31");
                        hdd1Used = snmpWalk.get("HOST-RESOURCES-MIB::hrStorageUsed.68");
                        diskIoRx = snmpWalk.get("UCD-SNMP-MIB::ssIORawReceived.0");
                        diskIoTx = snmpWalk.get("UCD-SNMP-MIB::ssIORawSent.0");
                        tempCase = snmpWalk.get("LM-SENSORS-MIB::lmTempSensorsValue.22");
                        tempCPU = snmpWalk.get("LM-SENSORS-MIB::lmTempSensorsValue.35");
                    } catch (IOException ix) {
                        ix.printStackTrace();
                    }
                    JSONObject snmpData = new JSONObject();
                    snmpData
                        .put("CPU1Load", cpu1Load).put("CPU2Load", cpu2Load).put("CPU3Load", cpu3Load).put("CPU4Load", cpu4Load)
                        .put("CPU5Load", cpu5Load).put("CPU6Load", cpu6Load).put("CPU7Load", cpu7Load).put("CPU8Load", cpu8Load)
                        .put("memPhysSize", memPhysSize).put("memPhysUsed", memPhysUsed).put("memBuffUsed", memBuffUsed).put("memCachUsed", memCachUsed)
                        .put("hdd0Used", hdd0Used).put("hdd1Used", hdd1Used)
                        .put("diskIoRx", diskIoRx).put("diskIoTx", diskIoTx)
                        .put("tempCase", tempCase).put("tempCPU", tempCPU);
                    returnData = snmpData.toString();
                    break;
                    
            }
        }
        
        return returnData;
    }
}
