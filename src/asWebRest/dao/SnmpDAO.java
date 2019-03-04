/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 4 Mar 2019
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SnmpDAO {

    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    public JSONArray getActSqlConns() {
        final String query_MySQL_Active = "show status where `variable_name` = 'Threads_connected';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_MySQL_Active, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Threads_connected", resultSet.getString("Threads_connected"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getEmS4(Connection dbc, List qParams) {
        final String query_SNMP_EmS4 = "(SELECT * FROM (" +
                " SELECT @row := @row+1 AS rownum, WalkTime," +
                " ActiveConn, rmnet0Rx, rmnet0Tx, wlan0Rx, wlan0Tx," +
                " MemoryBuffers, MemoryFree, MemoryShared, MemoryTotal, MemoryUse," +
                " CPUUse, LoadNow, Load5, Load15," +
                " BattLevel, BattVolt, BattCurrent, BattTemp, BattPowered, BattPoweredU, BattHealth," +
                " SigStrGSM, SigStrCDMA, SigStrEVDO, SigStrLTE" +
                " FROM ( SELECT @row :=0 ) r, net_snmp.EmS4 ) ranked" +
                " WHERE (? = 1 OR rownum % ? = 1)" + // Test, Step
                " AND (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + //DateTest, Date
                " ORDER BY WalkTime DESC LIMIT 720) ORDER BY WalkTime ASC;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_EmS4, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("ActiveConn", resultSet.getInt("ActiveConn"))
                    .put("rmnet0Rx", resultSet.getLong("rmnet0Rx"))
                    .put("rmnet0Tx", resultSet.getLong("rmnet0Tx"))
                    .put("wlan0Rx", resultSet.getLong("wlan0Rx"))
                    .put("wlan0Tx", resultSet.getLong("wlan0Tx"))
                    .put("MemoryBuffers", resultSet.getLong("MemoryBuffers"))
                    .put("MemoryFree", resultSet.getLong("MemoryFree"))
                    .put("MemoryShared", resultSet.getLong("MemoryShared"))
                    .put("MemoryTotal", resultSet.getLong("MemoryTotal"))
                    .put("MemoryUse", resultSet.getLong("MemoryUse"))
                    .put("CPUUse", resultSet.getDouble("CPUUse"))
                    .put("LoadNow", resultSet.getDouble("LoadNow"))
                    .put("Load5", resultSet.getDouble("Load5"))
                    .put("Load15", resultSet.getDouble("Load15"))
                    .put("BattLevel", resultSet.getInt("BattLevel"))
                    .put("BattVolt", resultSet.getInt("BattVolt"))
                    .put("BattCurrent", resultSet.getInt("BattCurrent"))
                    .put("BattTemp", resultSet.getInt("BattTemp"))
                    .put("BattPowered", resultSet.getString("BattPowered"))
                    .put("BattPoweredU", resultSet.getString("BattPoweredU"))
                    .put("BattHealth", resultSet.getInt("BattHealth"))
                    .put("SigStrGSM", resultSet.getInt("SigStrGSM"))
                    .put("SigStrCDMA", resultSet.getInt("SigStrCDMA"))
                    .put("SigStrEVDO", resultSet.getInt("SigStrEVDO"))
                    .put("SigStrLTE", resultSet.getInt("SigStrLTE"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getEmS4Geo() {
        final String query_SNMP_EmS4_Geo = "SELECT WalkTime, Location, BattLevel, BattTemp FROM net_snmp.EmS4 ORDER BY WalkTime DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_EmS4_Geo, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("Location", resultSet.getString("Location"))
                    .put("BattLevel", resultSet.getInt("BattLevel"))
                    .put("BattTemp", resultSet.getInt("BattTemp"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getEmS4GeoHistory() {
        final String query_SNMP_EmS4_GeoHistory = "SELECT Location FROM net_snmp.EmS4 ORDER BY WalkTime DESC LIMIT 5000;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_EmS4_GeoHistory, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Location", resultSet.getString("Location"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public JSONArray getLastWalk(Connection dbc) {
        final String query_SNMP_LastWalk = "SELECT" +
                " (SELECT MAX(WalkTime) FROM net_snmp.EmS4 ORDER BY WalkTime ASC) AS PhoneE," +
                " (SELECT MAX(WalkTime) FROM net_snmp.Main ORDER BY WalkTime ASC) AS Main," +
                " (SELECT MAX(WalkTime) FROM net_snmp.Note3 ORDER BY WalkTime ASC) AS Phone," +
                " (SELECT MAX(WalkTime) FROM net_snmp.RaspberryPi ORDER BY WalkTime ASC) AS Pi," +
                " (SELECT MAX(WalkTime) FROM net_snmp.RaspberryPi2 ORDER BY WalkTime ASC) AS Pi2," +
                " (SELECT MAX(WalkTime) FROM net_snmp.Asus3200 ORDER BY WalkTime ASC) AS Router" +
                " FROM net_snmp.Main LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_LastWalk, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("PhoneE", resultSet.getString("PhoneE"))
                    .put("Main", resultSet.getString("Main"))
                    .put("Phone", resultSet.getString("Phone"))
                    .put("Pi", resultSet.getString("Pi"))
                    .put("Pi2", resultSet.getString("Pi2"))
                    .put("Router", resultSet.getString("Router"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getMain(Connection dbc, List qParams) {
        final String query_SNMP_Main = "(SELECT * FROM ( SELECT " +
                " @row := @row+1 AS rownum, " +
                " dt.WalkTime as WalkTime, dt.NumUsers as dtNumUsers, " +
                " dt.CPULoad1 as dtCPULoad1, dt.CPULoad2 as dtCPULoad2, dt.CPULoad3 as dtCPULoad3, dt.CPULoad4 as dtCPULoad4," +
                " dt.CPULoad5 as dtCPULoad5, dt.CPULoad6 as dtCPULoad6, dt.CPULoad7 as dtCPULoad7, dt.CPULoad8 as dtCPULoad8," +
                " dt.LoadIndex1 as dtLoadIndex1, dt.LoadIndex5 as dtLoadIndex5, dt.LoadIndex15 as dtLoadIndex15," +
                " dt.Processes as dtProcesses," +
                " dt.TempCPU as dtTempCPU, dt.TempCase as dtTempCase, dt.Fan1 as dtFan1, dt.Fan2 as dtFan2, dt.Fan3 as dtFan3," +
                " dt.TempCore1 as dtTempCore1, dt.TempCore2 as dtTempCore2, dt.TempCore3 as dtTempCore3, dt.TempCore4 as dtTempCore4," +
                " dt.VoltCPU as dtVoltCPU," +
                " dt.VoltCore1 as dtVoltCore1, dt.VoltCore2 as dtVoltCore2, dt.VoltCore3 as dtVoltCore3, dt.VoltCore4 as dtVoltCore4," + 
                " dt.VoltPlus33 as dtVoltPlus33, dt.VoltPlus5 as dtVoltPlus5, dt.VoltPlus12 as dtVoltPlus12," +
                " dt.VoltBatt as dtVoltBatt," +
                " dt.OctetsIn as dtOctetsIn, dt.OctetsOut as dtOctetsOut," +
                " dt.KMemPhys as dtKMemPhys, dt.KMemVirt as dtKMemVirt, dt.KMemBuff as dtKMemBuff, dt.KMemCached as dtKMemCached," +
                " dt.KMemShared as dtKMemShared, dt.KSwap as dtKSwap, dt.K4Root as dtK4Root," + 
                " dt.KMemPhysU as dtKMemPhysU, dt.KMemVirtU as dtKMemVirtU, dt.KMemBuffU as dtKMemBuffU, dt.KMemCachedU as dtKMemCachedU," +
                " dt.KMemSharedU as dtKMemSharedU, dt.KSwapU as dtKSwapU, dt.K4RootU as dtK4RootU," +
                " dt.DiskIOSysRead as dtDiskIOSysRead, dt.DiskIOSysWrite as dtDiskIOSysWrite," +
                " dt.MySQLUpdate as dtMySQLUpdate, dt.MySQLInsert as dtMySQLInsert, dt.MySQLSelect as dtMySQLSelect," + 
                " dt.MySQLDelete as dtMySQLDelete, dt.MySQLReplace as dtMySQLReplace," +
                " dt.MySQLRowsCore as dtMySQLRowsCore, dt.MySQLRowsMediaWiki as dtMySQLRowsMediaWiki," +
                " dt.MySQLRowsNetSNMP as dtMySQLRowsNetSNMP, dt.MySQLRowsFeeds as dtMySQLRowsFeeds," +
                " dt.MySQLRowsWebCal as dtMySQLRowsWebCal, dt.MySQLRowsWxObs as dtMySQLRowsWxObs," +
                " dt.MySQLSizeCore as dtMySQLSizeCore, dt.MySQLSizeMediaWiki as dtMySQLSizeMediaWiki," + 
                " dt.MySQLSizeNetSNMP as dtMySQLSizeNetSNMP, dt.MySQLSizeFeeds as dtMySQLSizeFeeds," +
                " dt.MySQLSizeWebCal as dtMySQLSizeWebCal, dt.MySQLSizeWxObs as dtMySQLSizeWxObs," +
                " dt.LogApache2GET as dtLogApache2GET, dt.TomcatGET as dtTomcatGET," +
                " dt.LogSecCam1Down as dtLogSecCam1Down, dt.LogSecCam2Down as dtLogSecCam2Down," +
                " dt.LogSecCam3Down as dtLogSecCam3Down, dt.LogSecCam4Down as dtLogSecCam4Down," +
                " dt.UPSLoad as dtUPSLoad, dt.UPSTimeLeft as dtUPSTimeLeft," +
                " dt.TomcatWars as dtTomcatWars, dt.TomcatDeploy as dtTomcatDeploy," +
                " dt.RouterWANrxBs as rtrWANrx, dt.RouterWANtxBs as rtrWANtx," +
                " dt.INodeSDA1 as dtINodeSDA1, dt.INodeSHM as dtINodeSHM," +
                " dt.duMySQLCore as duMySQLCore, dt.duMySQLNetSNMP as duMySQLNetSNMP, dt.duMySQLWebCal as duMySQLWebCal," +
                " dt.duMySQLWxObs as duMySQLWxObs, dt.duMySQLFeeds as duMySQLFeeds, dt.duMySQLTotal as duMySQLTotal," +
                " cmvm.NumUsers as cmvmNumUsers, cmvm.OctetsIn as cmvmOctetsIn, cmvm.OctetsOut as cmvmOctetsOut," +
                " lap.NumUsers as lapNumUsers, lap.OctetsIn as lapOctetsIn, lap.OctetsOut as lapOctetsOut," +
                " uvm.NumUsers as uvmNumUsers, uvm.OctetsIn as uvmOctetsIn, uvm.OctetsOut as uvmOctetsOut," +
                " uvm.MySQLUpdate as uvmMySQLUpdate, uvm.MySQLInsert as uvmMySQLInsert, uvm.MySQLSelect as uvmMySQLSelect," +
                " uvm.MySQLDelete as uvmMySQLDelete, uvm.MySQLReplace as uvmMySQLReplace," +
                " uvm.MySQLRowsCorePub as uvmMySQLRowsCorePub, uvm.MySQLRowsMediaWiki as uvmMySQLRowsMediaWiki," +
                " uvm.MySQLSizeCorePub as uvmMySQLSizeCorePub, uvm.MySQLSizeMediaWiki as uvmMySQLSizeMediaWiki," +
                " w12.NumUsers as w12NumUsers, w12.OctetsIn as w12OctetsIn, w12.OctetsOut as w12OctetsOut," +
                " w16.NumUsers as w16NumUsers, w16.OctetsIn as w16OctetsIn, w16.OctetsOut as w16OctetsOut," +
                " wxp.NumUsers as wxpNumUsers, wxp.OctetsIn as wxpOctetsIn, wxp.OctetsOut as wxpOctetsOut," +
                " pi.OctetsIn as piOctetsIn, pi.OctetsOut as piOctetsOut, pi.WifiOctetsIn as piWifiIn, pi.WifiOctetsOut as piWifiOut," +
                " pi2.OctetsIn as pi2OctetsIn, pi2.OctetsOut as pi2OctetsOut, pi2.WifiOctetsIn as pi2WifiIn, pi2.WifiOctetsOut as pi2WifiOut," +
                " pi.ExtTemp as piExtTemp, pi2.ExtTemp as pi2ExtTemp," +
                " dt.NS5Active as dtNS5Active, dt.NS5ActiveSSH as dtNS5ActiveSSH, " +
                " dt.ExpandedJSONData as dtExpandedJSONData" +
                " FROM ( SELECT @row :=0 ) r, net_snmp.Main dt" +
                " LEFT JOIN net_snmp.Laptop lap ON lap.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.CiscoCMVM cmvm ON cmvm.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.RaspberryPi pi ON pi.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.RaspberryPi2 pi2 ON pi2.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.UbuntuVM uvm ON uvm.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.WinS12VM w12 ON w12.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.WinS16VM w16 ON w16.WalkTime = dt.WalkTime" +
                " LEFT JOIN net_snmp.WinXPVM wxp ON wxp.WalkTime = dt.WalkTime" +
                ") ranked " +
                " WHERE (? = 1 OR rownum % ? = 1)" + // qp1 = Test, qp2 = Step
                " AND (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + // qp3 = DateTest, qp4 = Date
                " ORDER BY WalkTime DESC" +
                " LIMIT 720) ORDER BY WalkTime ASC;";
        //System.out.println(qParams.toString() + "\n" + wcb.getQSetRT0() + "\n" + query_SNMP_Main);
        JSONArray tContainer = new JSONArray();     
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Main, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("dtNumUsers", resultSet.getInt("dtNumUsers"))
                    .put("dtCPULoad1", resultSet.getInt("dtCPULoad1"))
                    .put("dtCPULoad2", resultSet.getInt("dtCPULoad2"))
                    .put("dtCPULoad3", resultSet.getInt("dtCPULoad3"))
                    .put("dtCPULoad4", resultSet.getInt("dtCPULoad4"))
                    .put("dtCPULoad5", resultSet.getInt("dtCPULoad5"))
                    .put("dtCPULoad6", resultSet.getInt("dtCPULoad6"))
                    .put("dtCPULoad7", resultSet.getInt("dtCPULoad7"))
                    .put("dtCPULoad8", resultSet.getInt("dtCPULoad8"))
                    .put("dtLoadIndex1", resultSet.getDouble("dtLoadIndex1"))
                    .put("dtLoadIndex5", resultSet.getDouble("dtLoadIndex5"))
                    .put("dtLoadIndex15", resultSet.getDouble("dtLoadIndex15"))
                    .put("dtProcesses", resultSet.getInt("dtProcesses"))
                    .put("dtTempCPU", resultSet.getInt("dtTempCPU"))
                    .put("dtTempCase", resultSet.getInt("dtTempCase"))
                    .put("dtFan1", resultSet.getInt("dtFan1"))
                    .put("dtFan2", resultSet.getInt("dtFan2"))
                    .put("dtFan3", resultSet.getInt("dtFan3"))
                    .put("dtTempCore1", resultSet.getInt("dtTempCore1"))
                    .put("dtTempCore2", resultSet.getInt("dtTempCore2"))
                    .put("dtTempCore3", resultSet.getInt("dtTempCore3"))
                    .put("dtTempCore4", resultSet.getInt("dtTempCore4"))
                    .put("dtVoltCPU", resultSet.getDouble("dtVoltCPU"))
                    .put("dtVoltCore1", resultSet.getDouble("dtVoltCore1"))
                    .put("dtVoltCore2", resultSet.getDouble("dtVoltCore2"))
                    .put("dtVoltCore3", resultSet.getDouble("dtVoltCore3"))
                    .put("dtVoltCore4", resultSet.getDouble("dtVoltCore4"))
                    .put("dtVoltPlus33", resultSet.getDouble("dtVoltPlus33"))
                    .put("dtVoltPlus5", resultSet.getDouble("dtVoltPlus5"))
                    .put("dtVoltPlus12", resultSet.getDouble("dtVoltPlus12"))
                    .put("dtVoltBatt", resultSet.getDouble("dtVoltBatt"))
                    .put("dtOctetsIn", resultSet.getLong("dtOctetsIn"))
                    .put("dtOctetsOut", resultSet.getLong("dtOctetsOut"))
                    .put("dtKMemPhys", resultSet.getLong("dtKMemPhys"))
                    .put("dtKMemVirt", resultSet.getLong("dtKMemVirt"))
                    .put("dtKMemBuff", resultSet.getLong("dtKMemBuff"))
                    .put("dtKMemCached", resultSet.getLong("dtKMemCached"))
                    .put("dtKMemShared", resultSet.getLong("dtKMemShared"))
                    .put("dtKSwap", resultSet.getLong("dtKSwap"))
                    .put("dtK4Root", resultSet.getLong("dtK4Root"))
                    .put("dtKMemPhysU", resultSet.getLong("dtKMemPhysU"))
                    .put("dtKMemVirtU", resultSet.getLong("dtKMemVirtU"))
                    .put("dtKMemBuffU", resultSet.getLong("dtKMemBuffU"))
                    .put("dtKMemCachedU", resultSet.getLong("dtKMemCachedU"))
                    .put("dtKMemSharedU", resultSet.getLong("dtKMemSharedU"))
                    .put("dtKSwapU", resultSet.getLong("dtKSwapU"))
                    .put("dtK4RootU", resultSet.getLong("dtK4RootU"))
                    .put("dtDiskIOSysRead", resultSet.getLong("dtDiskIOSysRead"))
                    .put("dtDiskIOSysWrite", resultSet.getLong("dtDiskIOSysWrite"))
                    .put("dtMySQLUpdate", resultSet.getLong("dtMySQLUpdate"))
                    .put("dtMySQLInsert", resultSet.getLong("dtMySQLInsert"))
                    .put("dtMySQLSelect", resultSet.getLong("dtMySQLSelect"))
                    .put("dtMySQLDelete", resultSet.getLong("dtMySQLDelete"))
                    .put("dtMySQLReplace", resultSet.getLong("dtMySQLReplace"))
                    .put("dtMySQLRowsCore", resultSet.getLong("dtMySQLRowsCore"))
                    .put("dtMySQLRowsMediaWiki", resultSet.getLong("dtMySQLRowsMediaWiki"))
                    .put("dtMySQLRowsNetSNMP", resultSet.getLong("dtMySQLRowsNetSNMP"))
                    .put("dtMySQLRowsFeeds", resultSet.getLong("dtMySQLRowsFeeds"))
                    .put("dtMySQLRowsWebCal", resultSet.getLong("dtMySQLRowsWebCal"))
                    .put("dtMySQLRowsWxObs", resultSet.getLong("dtMySQLRowsWxObs"))
                    .put("dtMySQLSizeCore", resultSet.getLong("dtMySQLSizeCore"))
                    .put("dtMySQLSizeMediaWiki", resultSet.getLong("dtMySQLSizeMediaWiki"))
                    .put("dtMySQLSizeNetSNMP", resultSet.getLong("dtMySQLSizeNetSNMP"))
                    .put("dtMySQLSizeFeeds", resultSet.getLong("dtMySQLSizeFeeds"))
                    .put("dtMySQLSizeWebCal", resultSet.getLong("dtMySQLSizeWebCal"))
                    .put("dtMySQLSizeWxObs", resultSet.getLong("dtMySQLSizeWxObs"))
                    .put("dtLogApache2GET", resultSet.getLong("dtLogApache2GET"))
                    .put("dtTomcatGET", resultSet.getLong("dtTomcatGET"))
                    .put("dtLogSecCam1Down", resultSet.getLong("dtLogSecCam1Down"))
                    .put("dtLogSecCam2Down", resultSet.getLong("dtLogSecCam2Down"))
                    .put("dtLogSecCam3Down", resultSet.getLong("dtLogSecCam3Down"))
                    .put("dtLogSecCam4Down", resultSet.getLong("dtLogSecCam4Down"))
                    .put("dtUPSTimeLeft", resultSet.getDouble("dtUPSTimeLeft"))
                    .put("dtUPSLoad", resultSet.getDouble("dtUPSLoad"))
                    .put("dtTomcatWars", resultSet.getInt("dtTomcatWars"))
                    .put("dtTomcatDeploy", resultSet.getInt("dtTomcatDeploy"))
                    .put("rtrWANrx", resultSet.getLong("rtrWANrx"))
                    .put("rtrWANtx", resultSet.getLong("rtrWANtx"))
                    .put("dtINodeSDA1", resultSet.getLong("dtINodeSDA1"))
                    .put("dtINodeSHM", resultSet.getLong("dtINodeSHM"))
                    .put("duMySQLCore", resultSet.getLong("duMySQLCore"))
                    .put("duMySQLNetSNMP", resultSet.getLong("duMySQLNetSNMP"))
                    .put("duMySQLWxObs", resultSet.getLong("duMySQLWxObs"))
                    .put("duMySQLWebCal", resultSet.getLong("duMySQLWebCal"))
                    .put("duMySQLFeeds", resultSet.getLong("duMySQLFeeds"))
                    .put("duMySQLTotal", resultSet.getLong("duMySQLTotal"))
                    .put("cmvmNumUsers", resultSet.getInt("cmvmNumUsers"))
                    .put("cmvmOctetsIn", resultSet.getLong("cmvmOctetsIn"))
                    .put("cmvmOctetsOut", resultSet.getLong("cmvmOctetsOut"))
                    .put("lapNumUsers", resultSet.getInt("lapNumUsers"))
                    .put("lapOctetsIn", resultSet.getLong("lapOctetsIn"))
                    .put("lapOctetsOut", resultSet.getLong("lapOctetsOut"))
                    .put("uvmNumUsers", resultSet.getInt("uvmNumUsers"))
                    .put("uvmOctetsIn", resultSet.getLong("uvmOctetsIn"))
                    .put("uvmOctetsOut", resultSet.getLong("uvmOctetsOut"))
                    .put("uvmMySQLUpdate", resultSet.getLong("uvmMySQLUpdate"))
                    .put("uvmMySQLInsert", resultSet.getLong("uvmMySQLInsert"))
                    .put("uvmMySQLSelect", resultSet.getLong("uvmMySQLSelect"))
                    .put("uvmMySQLDelete", resultSet.getLong("uvmMySQLDelete"))
                    .put("uvmMySQLReplace", resultSet.getLong("uvmMySQLReplace"))
                    .put("uvmMySQLRowsCorePub", resultSet.getLong("uvmMySQLRowsCorePub"))
                    .put("uvmMySQLSizeCorePub", resultSet.getLong("uvmMySQLSizeCorePub"))
                    .put("uvmMySQLRowsMediaWiki", resultSet.getLong("uvmMySQLRowsMediaWiki"))
                    .put("uvmMySQLSizeMediaWiki", resultSet.getLong("uvmMySQLSizeMediaWiki"))
                    .put("w12NumUsers", resultSet.getInt("w12NumUsers"))
                    .put("w12OctetsIn", resultSet.getLong("w12OctetsIn"))
                    .put("w12OctetsOut", resultSet.getLong("w12OctetsOut"))
                    .put("w16NumUsers", resultSet.getInt("w16NumUsers"))
                    .put("w16OctetsIn", resultSet.getLong("w16OctetsIn"))
                    .put("w16OctetsOut", resultSet.getLong("w16OctetsOut"))
                    .put("wxpNumUsers", resultSet.getInt("wxpNumUsers"))
                    .put("wxpOctetsIn", resultSet.getLong("wxpOctetsIn"))
                    .put("wxpOctetsOut", resultSet.getLong("wxpOctetsOut"))
                    .put("piOctetsIn", resultSet.getLong("piOctetsIn"))
                    .put("pi2OctetsIn", resultSet.getLong("pi2OctetsIn"))
                    .put("piOctetsOut", resultSet.getLong("piOctetsOut"))
                    .put("pi2OctetsOut", resultSet.getLong("pi2OctetsOut"))
                    .put("piWifiIn", resultSet.getLong("piWifiIn"))
                    .put("pi2WifiIn", resultSet.getLong("pi2WifiIn"))
                    .put("piWifiOut", resultSet.getLong("piWifiOut"))
                    .put("pi2WifiOut", resultSet.getLong("pi2WifiOut"))
                    .put("piExtTemp", resultSet.getDouble("piExtTemp"))
                    .put("pi2ExtTemp", resultSet.getDouble("pi2ExtTemp"))
                    .put("dtNS5Active", resultSet.getInt("dtNS5Active"))
                    .put("dtNS5ActiveSSH", resultSet.getInt("dtNS5ActiveSSH"))
                    .put("dtExpandedJSONData", new JSONObject(resultSet.getString("dtExpandedJSONData")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
            
    public JSONArray getMainLastSSH(Connection dbc) {
        final String query_SNMP_Main_LastCaseTemp = "SELECT WalkTime, SSHClientIP FROM net_snmp.Main ORDER BY WalkTime DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Main_LastCaseTemp, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("SSHClientIP", resultSet.getString("SSHClientIP"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public JSONArray getMergedLastTemp(Connection dbc) {
        final String query_SNMP_MergedTemps = "SELECT Description, WalkTime, ExtTemp FROM (" +
                " (SELECT 'RaspberryPi' as Description, WalkTime, ExtTemp FROM net_snmp.RaspberryPi ORDER BY WalkTime DESC LIMIT 1) UNION ALL" +
                " (SELECT 'RaspberryPi2' as Description, WalkTime, ExtTemp FROM net_snmp.RaspberryPi2 ORDER BY WalkTime DESC LIMIT 1) UNION ALL" +
                " (SELECT 'Desktop' AS Description, WalkTime, TempCase as ExtTemp FROM net_snmp.Main ORDER BY WalkTime DESC LIMIT 1)" +
                " ) as tmp" +
                " ORDER BY Description ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_MergedTemps, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("ExtTemp", resultSet.getInt("ExtTemp"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getNote3(Connection dbc, List qParams) {
        final String query_SNMP_Note3 = "(SELECT * FROM (" +
                " SELECT @row := @row+1 AS rownum, WalkTime," +
                " ActiveConn, rmnet0Rx, rmnet0Tx, wlan0Rx, wlan0Tx," +
                " MemoryBuffers, MemoryFree, MemoryShared, MemoryTotal, MemoryUse," +
                " CPUUse, LoadNow, Load5, Load15," +
                " BattLevel, BattVolt, BattCurrent, BattTemp, BattPowered, BattPoweredU, BattHealth," +
                " SigStrGSM, SigStrCDMA, SigStrEVDO, SigStrLTE" +
                " FROM ( SELECT @row :=0 ) r, net_snmp.Note3 ) ranked" +
                " WHERE (? = 1 OR rownum % ? = 1)" + // Test, Step
                " AND (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + //DateTest, Date
                " ORDER BY WalkTime DESC LIMIT 720) ORDER BY WalkTime ASC;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Note3, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("ActiveConn", resultSet.getInt("ActiveConn"))
                    .put("rmnet0Rx", resultSet.getLong("rmnet0Rx"))
                    .put("rmnet0Tx", resultSet.getLong("rmnet0Tx"))
                    .put("wlan0Rx", resultSet.getLong("wlan0Rx"))
                    .put("wlan0Tx", resultSet.getLong("wlan0Tx"))
                    .put("MemoryBuffers", resultSet.getLong("MemoryBuffers"))
                    .put("MemoryFree", resultSet.getLong("MemoryFree"))
                    .put("MemoryShared", resultSet.getLong("MemoryShared"))
                    .put("MemoryTotal", resultSet.getLong("MemoryTotal"))
                    .put("MemoryUse", resultSet.getLong("MemoryUse"))
                    .put("CPUUse", resultSet.getDouble("CPUUse"))
                    .put("LoadNow", resultSet.getDouble("LoadNow"))
                    .put("Load5", resultSet.getDouble("Load5"))
                    .put("Load15", resultSet.getDouble("Load15"))
                    .put("BattLevel", resultSet.getInt("BattLevel"))
                    .put("BattVolt", resultSet.getInt("BattVolt"))
                    .put("BattCurrent", resultSet.getInt("BattCurrent"))
                    .put("BattTemp", resultSet.getInt("BattTemp"))
                    .put("BattPowered", resultSet.getString("BattPowered"))
                    .put("BattPoweredU", resultSet.getString("BattPoweredU"))
                    .put("BattHealth", resultSet.getInt("BattHealth"))
                    .put("SigStrGSM", resultSet.getInt("SigStrGSM"))
                    .put("SigStrCDMA", resultSet.getInt("SigStrCDMA"))
                    .put("SigStrEVDO", resultSet.getInt("SigStrEVDO"))
                    .put("SigStrLTE", resultSet.getInt("SigStrLTE"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getNote3Geo(Connection dbc) {
        final String query_SNMP_Note3_Geo = "SELECT WalkTime, Location, BattLevel, BattTemp FROM net_snmp.Note3 ORDER BY WalkTime DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Note3_Geo, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("Location", resultSet.getString("Location"))
                    .put("BattLevel", resultSet.getInt("BattLevel"))
                    .put("BattTemp", resultSet.getInt("BattTemp"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getNote3GeoHistory() {
        final String query_SNMP_Note3_GeoHistory = "SELECT Location FROM net_snmp.Note3 ORDER BY WalkTime DESC LIMIT 5000;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_Note3_GeoHistory, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Location", resultSet.getString("Location"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
       
    public JSONArray getNote3RapidGeoHistory() {
        final String query_SNMP_Note3_RapidGeoHistory = "SELECT RapidLocation as Location FROM Note3 WHERE RapidLocation IS NOT NULL ORDER BY WalkTime DESC LIMIT 5000;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_Note3_RapidGeoHistory, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Location", resultSet.getString("Location"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public JSONArray getNote3Sensors(List qParams) {
        final String query_SNMP_Note3_Sensors = "(SELECT " +
                " WalkTime, SensorsRapid FROM net_snmp.Note3" +
                " WHERE (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + // DateTest, Date
                " ORDER BY WalkTime DESC LIMIT 180) ORDER BY WalkTime ASC;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs(wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_Note3_Sensors, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("SensorsRapid", new JSONObject(resultSet.getString("SensorsRapid")));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getPi(Connection dbc, List qParams) {
        final String query_SNMP_Pi = "(SELECT * FROM (" +
                " SELECT @row := @row+1 AS rownum, WalkTime," +
                " CPULoad, CPULoad2, CPULoad3, CPULoad4," +
                " LoadIndex1, LoadIndex5, LoadIndex15, TempCPU," +
                " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root," +
                " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU," +
                " ExtTemp, ExtAmbLight, ExtNoise, UptimeSec" +
                " FROM ( SELECT @row :=0 ) r, net_snmp.RaspberryPi ) ranked" +
                " WHERE (? = 1 OR rownum % ? = 1)" + // Test, Step
                " AND (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + // DateTest, Date
                " ORDER BY WalkTime DESC LIMIT 720) ORDER BY WalkTime ASC;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Pi, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("CPULoad", resultSet.getInt("CPULoad"))
                    .put("CPULoad2", resultSet.getInt("CPULoad2"))
                    .put("CPULoad3", resultSet.getInt("CPULoad3"))
                    .put("CPULoad4", resultSet.getInt("CPULoad4"))
                    .put("LoadIndex1", resultSet.getDouble("LoadIndex1"))
                    .put("LoadIndex5", resultSet.getDouble("LoadIndex5"))
                    .put("LoadIndex15", resultSet.getDouble("LoadIndex15"))
                    .put("TempCPU", resultSet.getInt("TempCPU"))
                    .put("KMemPhys", resultSet.getLong("KMemPhys"))
                    .put("KMemVirt", resultSet.getLong("KMemVirt"))
                    .put("KMemBuff", resultSet.getLong("KMemBuff"))
                    .put("KMemCached", resultSet.getLong("KMemCached"))
                    .put("KMemShared", resultSet.getLong("KMemShared"))
                    .put("KSwap", resultSet.getLong("KSwap"))
                    .put("K4Root", resultSet.getLong("K4Root"))
                    .put("KMemPhysU", resultSet.getLong("KMemPhysU"))
                    .put("KMemVirtU", resultSet.getLong("KMemVirtU"))
                    .put("KMemBuffU", resultSet.getLong("KMemBuffU"))
                    .put("KMemCachedU", resultSet.getLong("KMemCachedU"))
                    .put("KMemSharedU", resultSet.getLong("KMemSharedU"))
                    .put("KSwapU", resultSet.getLong("KSwapU"))
                    .put("K4RootU", resultSet.getLong("K4RootU"))
                    .put("ExtTemp", resultSet.getDouble("ExtTemp"))
                    .put("ExtAmbLight", resultSet.getInt("ExtAmbLight"))
                    .put("ExtNoise", resultSet.getInt("ExtNoise"))
                    .put("UptimeSec", resultSet.getLong("UptimeSec"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getPi2(Connection dbc, List qParams) {
        final String query_SNMP_Pi2 = "(SELECT * FROM (" +
                " SELECT @row := @row+1 AS rownum, WalkTime," +
                " CPULoad, CPULoad2, CPULoad3, CPULoad4," +
                " LoadIndex1, LoadIndex5, LoadIndex15, TempCPU," +
                " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root," +
                " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU," +
                " ExtTemp, UptimeSec, LightLevel," +
                " GPSSpeedMPH, GPSAgeMS, GPSCoords, GPSDataChars, GPSAltiCM" +
                " FROM ( SELECT @row :=0 ) r, net_snmp.RaspberryPi2 ) ranked" +
                " WHERE (? = 1 OR rownum % ? = 1)" + // Test, Step
                " AND (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + // DateTest, Date
                " ORDER BY WalkTime DESC LIMIT 720) ORDER BY WalkTime ASC;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Pi2, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("CPULoad", resultSet.getInt("CPULoad"))
                    .put("CPULoad2", resultSet.getInt("CPULoad2"))
                    .put("CPULoad3", resultSet.getInt("CPULoad3"))
                    .put("CPULoad4", resultSet.getInt("CPULoad4"))
                    .put("LoadIndex1", resultSet.getDouble("LoadIndex1"))
                    .put("LoadIndex5", resultSet.getDouble("LoadIndex5"))
                    .put("LoadIndex15", resultSet.getDouble("LoadIndex15"))
                    .put("TempCPU", resultSet.getInt("TempCPU"))
                    .put("KMemPhys", resultSet.getLong("KMemPhys"))
                    .put("KMemVirt", resultSet.getLong("KMemVirt"))
                    .put("KMemBuff", resultSet.getLong("KMemBuff"))
                    .put("KMemCached", resultSet.getLong("KMemCached"))
                    .put("KMemShared", resultSet.getLong("KMemShared"))
                    .put("KSwap", resultSet.getLong("KSwap"))
                    .put("K4Root", resultSet.getLong("K4Root"))
                    .put("KMemPhysU", resultSet.getLong("KMemPhysU"))
                    .put("KMemVirtU", resultSet.getLong("KMemVirtU"))
                    .put("KMemBuffU", resultSet.getLong("KMemBuffU"))
                    .put("KMemCachedU", resultSet.getLong("KMemCachedU"))
                    .put("KMemSharedU", resultSet.getLong("KMemSharedU"))
                    .put("KSwapU", resultSet.getLong("KSwapU"))
                    .put("K4RootU", resultSet.getLong("K4RootU"))
                    .put("ExtTemp", resultSet.getDouble("ExtTemp"))
                    .put("LightLevel", resultSet.getInt("LightLevel"))
                    .put("GPSSpeedMPH", resultSet.getDouble("GPSSpeedMPH"))
                    .put("GPSAgeMS", resultSet.getInt("GPSAgeMS"))
                    .put("GSPCoords", resultSet.getString("GPSCoords"))
                    .put("GPSDataChars", resultSet.getLong("GPSDataChars"))
                    .put("GPSAltiCm", resultSet.getInt("GPSAltiCM"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
           
    public JSONArray getPi2Geo() {
        final String query_SNMP_Pi2_Geo = "SELECT WalkTime, GPSCoords FROM net_snmp.RaspberryPi2 ORDER BY WalkTime DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_Pi2_Geo, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("GPSCoords", resultSet.getInt("GPSCoords"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
               
    public JSONArray getPi2GeoHistory() {
        final String query_SNMP_Pi2_Geo = "SELECT GPSCoords FROM net_snmp.RaspberryPi2 ORDER BY WalkTime DESC LIMIT 5000;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_SNMP_Pi2_Geo, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("GPSCoords", resultSet.getInt("GPSCoords"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getRouter(Connection dbc, List qParams) {
        final String query_SNMP_Pi2 = "(SELECT * FROM (" +
        " SELECT @row := @row+1 AS rownum, WalkTime," +
        " eth0Rx, eth0Tx, eth1Rx, eth1Tx, eth2Rx, eth2Tx, eth3Rx, eth3Tx," +
        " vlan1Rx, vlan1Tx, vlan2Rx, vlan2Tx, br0Rx, br0Tx, CPULoad1, CPULoad2," +
        " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root," +
        " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU" +
        " FROM ( SELECT @row :=0 ) r, net_snmp.Asus3200 ) ranked" +
        " WHERE (? = 1 OR rownum % ? = 1)" + // Test, Step
        " AND (? = 1 OR WalkTime LIKE CONCAT(?, '%'))" + // DateTest, Date
        " ORDER BY WalkTime DESC LIMIT 720) ORDER BY WalkTime ASC;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT0(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_SNMP_Pi2, qParams);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("WalkTime", resultSet.getString("WalkTime"))
                    .put("CPULoad1", resultSet.getInt("CPULoad1"))
                    .put("CPULoad2", resultSet.getInt("CPULoad2"))
                    .put("KMemPhys", resultSet.getLong("KMemPhys"))
                    .put("KMemVirt", resultSet.getLong("KMemVirt"))
                    .put("KMemBuff", resultSet.getLong("KMemBuff"))
                    .put("KMemCached", resultSet.getLong("KMemCached"))
                    .put("KMemShared", resultSet.getLong("KMemShared"))
                    .put("KSwap", resultSet.getLong("KSwap"))
                    .put("K4Root", resultSet.getLong("K4Root"))
                    .put("KMemPhysU", resultSet.getLong("KMemPhysU"))
                    .put("KMemVirtU", resultSet.getLong("KMemVirtU"))
                    .put("KMemBuffU", resultSet.getLong("KMemBuffU"))
                    .put("KMemCachedU", resultSet.getLong("KMemCachedU"))
                    .put("KMemSharedU", resultSet.getLong("KMemSharedU"))
                    .put("KSwapU", resultSet.getLong("KSwapU"))
                    .put("eth0Rx", resultSet.getLong("eth0Rx"))
                    .put("eth0Tx", resultSet.getLong("eth0Tx"))
                    .put("eth1Rx", resultSet.getLong("eth1Rx"))
                    .put("eth1Tx", resultSet.getLong("eth1Tx"))
                    .put("eth2Rx", resultSet.getLong("eth2Rx"))
                    .put("eth2Tx", resultSet.getLong("eth2Tx"))
                    .put("eth3Rx", resultSet.getLong("eth3Rx"))
                    .put("eth3Tx", resultSet.getLong("eth3Tx"))
                    .put("vlan1Rx", resultSet.getLong("vlan1Rx"))
                    .put("vlan1Tx", resultSet.getLong("vlan1Tx"))
                    .put("vlan2Rx", resultSet.getLong("vlan2Rx"))
                    .put("vlan2Tx", resultSet.getLong("vlan2Tx"))
                    .put("br0Rx", resultSet.getLong("br0Rx"))
                    .put("br0Tx", resultSet.getLong("br0Tx"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
}
