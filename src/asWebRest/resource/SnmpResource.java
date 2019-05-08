/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 8 May 2019
 */

package asWebRest.resource;

import asWebRest.action.GetSnmpAction;
import asWebRest.dao.SnmpDAO;
import asWebRest.hookers.SnmpWalk;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        List<String> qParams = new ArrayList<>();
        qParams.add(0, "1"); //DateTest
        qParams.add(1, "20180224"); //Date
        GetSnmpAction getSnmpAction = new GetSnmpAction(new SnmpDAO());
        JSONArray snmpData = getSnmpAction.getRouter(dbc, qParams, 1);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return snmpData.toString();
    }
    
    @Post
    public String represent(Representation argsIn) {

        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
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
                
                 case "snmpWalk": /*
                    String extraDiskID = "999";
                    try { extraDiskID = argsInForm.getFirstValue("extraDiskID"); } catch (Exception e) { e.printStackTrace(); }
                    int cpu1Load = 0;
                    int cpu2Load = 0;
                    int cpu3Load = 0;
                    int cpu4Load = 0;
                    int cpu5Load = 0;
                    int cpu6Load = 0;
                    int cpu7Load = 0;
                    int cpu8Load = 0;
                    int diskIoNode = 0;
                    long diskIoRx = 0;
                    long diskIoTx = 0;
                    long eth0In = 0;
                    long eth0Out = 0;
                    long hdd0Used = 0;
                    long hdd1Used = 0;
                    double loadIndex = 0.00;
                    double loadIndex5 = 0.00;
                    double loadIndex15 = 0.00;
                    long memBuffSize = 0;
                    long memBuffUsed = 0;
                    long memCachSize = 0;
                    long memCachUsed = 0;
                    int memIoNode = 0;
                    long memPhysSize = 0;
                    long memPhysUsed = 0;
                    long memVirtSize = 0;
                    long memVirtUsed = 0;
                    long myDelete = 0;
                    long myInsert = 0;
                    long myReplace = 0;
                    long mySelect = 0;
                    long myUpdate = 0;
                    int runningProcs = 0;
                    int tempCase = 0;
                    int tempCPU = 0;
                    String uptime = "";
                    try { 
                        SnmpWalk snmpWalk = new SnmpWalk();
                        try { cpu1Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196608")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu2Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196609")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu3Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196610")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu4Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196611")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu5Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196612")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu6Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196613")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu7Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196614")); } catch (Exception e) { e.printStackTrace(); }
                        try { cpu8Load = Integer.parseInt(snmpWalk.get("desktop", "hrProcessorLoad", "196615")); } catch (Exception e) { e.printStackTrace(); }
                        try { diskIoNode = Integer.parseInt(snmpWalk.get("desktop", "dskPercentNode", "3")); } catch (Exception e) { e.printStackTrace(); }
                        try { diskIoRx = Long.parseLong(snmpWalk.get("desktop", "ssIORawReceived", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { diskIoTx = Long.parseLong(snmpWalk.get("desktop", "ssIORawSent", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { eth0In = Long.parseLong(snmpWalk.get("desktop", "ifInOctets", "2")); } catch (Exception e) { e.printStackTrace(); }
                        try { eth0Out = Long.parseLong(snmpWalk.get("desktop", "ifOutOctets", "2")); } catch (Exception e) { e.printStackTrace(); }
                        try { hdd0Used = Long.parseLong(snmpWalk.get("desktop", "hrStorageUsed", "31")); } catch (Exception e) { e.printStackTrace(); }
                        try { hdd1Used = Long.parseLong(snmpWalk.get("desktop", "hrStorageUsed", extraDiskID)); } catch (Exception e) { e.printStackTrace(); }
                        try { loadIndex = Double.parseDouble(snmpWalk.get("desktop", "laLoad", "1")); } catch (Exception e) { e.printStackTrace(); }
                        try { loadIndex5 = Double.parseDouble(snmpWalk.get("desktop", "laLoad", "2")); } catch (Exception e) { e.printStackTrace(); }
                        try { loadIndex15 = Double.parseDouble(snmpWalk.get("desktop", "laLoad", "3")); } catch (Exception e) { e.printStackTrace(); }
                        try { memBuffSize = Long.parseLong(snmpWalk.get("desktop", "hrStorageSize", "6")); } catch (Exception e) { e.printStackTrace(); }
                        try { memBuffUsed = Long.parseLong(snmpWalk.get("desktop", "hrStorageUsed", "6")); } catch (Exception e) { e.printStackTrace(); }
                        try { memCachSize = Long.parseLong(snmpWalk.get("desktop", "hrStorageSize", "7")); } catch (Exception e) { e.printStackTrace(); }
                        try { memCachUsed = Long.parseLong(snmpWalk.get("desktop", "hrStorageUsed", "7")); } catch (Exception e) { e.printStackTrace(); }
                        try { memIoNode = Integer.parseInt(snmpWalk.get("desktop", "dskPercentNode", "5")); } catch (Exception e) { e.printStackTrace(); }
                        try { memPhysSize = Long.parseLong(snmpWalk.get("desktop", "hrStorageSize", "1")); } catch (Exception e) { e.printStackTrace(); }
                        try { memPhysUsed = Long.parseLong(snmpWalk.get("desktop", "hrStorageUsed", "1")); } catch (Exception e) { e.printStackTrace(); }
                        try { memVirtSize = Long.parseLong(snmpWalk.get("desktop", "hrStorageSize", "3")); } catch (Exception e) { e.printStackTrace(); }
                        try { memVirtUsed = Long.parseLong(snmpWalk.get("desktop", "hrStorageUsed", "3")); } catch (Exception e) { e.printStackTrace(); }
                        try { myDelete = Long.parseLong(snmpWalk.get("desktop", "myComDelete", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { myInsert = Long.parseLong(snmpWalk.get("desktop", "myComInsert", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { myReplace = Long.parseLong(snmpWalk.get("desktop", "myComReplace", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { mySelect = Long.parseLong(snmpWalk.get("desktop", "myComSelect", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { myUpdate = Long.parseLong(snmpWalk.get("desktop", "myComUpdate", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { runningProcs = Integer.parseInt(snmpWalk.get("desktop", "hrSystemProcesses", "0")); } catch (Exception e) { e.printStackTrace(); }
                        try { tempCase = Integer.parseInt(snmpWalk.get("desktop", "lmTempSensorsValue", "22")); } catch (Exception e) { e.printStackTrace(); }
                        try { tempCPU = Integer.parseInt(snmpWalk.get("desktop", "lmTempSensorsValue", "35")); } catch (Exception e) { e.printStackTrace(); }
                        try { uptime = snmpWalk.get("desktop", "hrSystemUptime", "0"); } catch (Exception e) { e.printStackTrace(); }
                    } catch (IOException ix) {
                        ix.printStackTrace();
                    }
                    JSONObject snmpData = new JSONObject();
                    snmpData
                        .put("cpu1Load", cpu1Load)
                        .put("cpu2Load", cpu2Load)
                        .put("cpu3Load", cpu3Load)
                        .put("cpu4Load", cpu4Load)
                        .put("cpu5Load", cpu5Load)
                        .put("cpu6Load", cpu6Load)
                        .put("cpu7Load", cpu7Load)
                        .put("cpu8Load", cpu8Load)
                        .put("diskIoNode", diskIoNode)
                        .put("diskIoRx", diskIoRx)
                        .put("diskIoTx", diskIoTx)
                        .put("eth0In", eth0In)
                        .put("eth0Out", eth0Out)
                        .put("hdd0Used", hdd0Used)
                        .put("hdd1Used", hdd1Used)
                        .put("loadIndex", loadIndex)
                        .put("loadIndex5", loadIndex5)
                        .put("loadIndex15", loadIndex15)
                        .put("memBuffSize", memBuffSize)
                        .put("memBuffUsed", memBuffUsed)
                        .put("memCachSize", memCachSize)
                        .put("memCachUsed", memCachUsed)
                        .put("memIoNode", memIoNode)
                        .put("memPhysSize", memPhysSize)
                        .put("memPhysUsed", memPhysUsed)
                        .put("memVirtSize", memVirtSize)
                        .put("memVirtUsed", memVirtUsed)
                        .put("myDelete", myDelete)
                        .put("myInsert", myInsert)
                        .put("myReplace", myReplace)
                        .put("mySelect", mySelect)
                        .put("myUpdate", myUpdate)
                        .put("runningProcs", runningProcs)
                        .put("tempCase", tempCase)
                        .put("tempCPU", tempCPU)
                        .put("uptime", uptime);
                    returnData += snmpData.toString(); */
            		System.out.println("SNMP Walk called but has been disabled due to performance!");
                    break;
                    
                case "getLastSSH":
                    JSONArray lastSsh = getSnmpAction.getMainLastSSH(dbc);
                    returnData = lastSsh.toString();
                    break;
                    
                case "getExtraDiskID":
                    //final File theWalkFile = new File(cb.getRamPath()+"/snmpwalk.txt");
                    String extra1DiskID = "";
                    /* Scanner walkFileScanner = null; try {		
                        walkFileScanner = new Scanner(theWalkFile);
                        while(walkFileScanner.hasNext()) {				
                            String line = walkFileScanner.nextLine();
                            if(line.contains("hrStorage") && line.contains("/extra1")) { Pattern p = Pattern.compile("Descr.(.*) ="); Matcher m = p.matcher(line); if (m.find()) { extra1DiskID = m.group(1); }}
                        }
                    } catch (FileNotFoundException fnf) {
                            fnf.printStackTrace();
                    } */
                    returnData += extra1DiskID;
            		System.out.println("SNMP Extra Disk ID called but has been disabled due to performance!");
                    break;
                    
                case "getLastWalk":
                    JSONArray lastWalk = getSnmpAction.getLastWalk(dbc);
                    JSONArray mergedTemps = getSnmpAction.getMergedLastTemp(dbc);
                    mergedResults
                        .put("lastWalk", lastWalk)
                        .put("mergedTemps", mergedTemps);
                    returnData = mergedResults.toString();
                    break;
   
                case "getPhoneTrack":
                	String phoneSelection = "";
            		if(wc.isSet(argsInForm.getFirstValue("SearchString"))) { phoneSelection = argsInForm.getFirstValue("SearchString"); }
                    JSONArray returnPhoneTrack = new JSONArray();
                    switch(phoneSelection) {
	                    case "EmS4": returnPhoneTrack = getSnmpAction.getEmS4GeoHistory(); break;
	                    case "Note3R": returnPhoneTrack = getSnmpAction.getNote3RapidGeoHistory(); break;
	                    case "RasPi2": returnPhoneTrack = getSnmpAction.getPi2GeoHistory(); break;
	                    case "Note3": default: returnPhoneTrack = getSnmpAction.getNote3GeoHistory(); break;
                    }
                    mergedResults
                        .put("Description", phoneSelection)
                        .put("PhoneTrack", returnPhoneTrack);
                    returnData = mergedResults.toString();
                    break;
                    
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
    }
}
