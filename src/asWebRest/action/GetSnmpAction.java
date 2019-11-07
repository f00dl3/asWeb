/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 19 Oct 2019
 */

package asWebRest.action;

import asWebRest.dao.SnmpDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetSnmpAction {
    
    private SnmpDAO snmpDAO;
    public GetSnmpAction(SnmpDAO snmpDAO) { this.snmpDAO = snmpDAO; }
    
    public JSONArray getEmS4(Connection dbc, List qParams) { return snmpDAO.getEmS4(dbc, qParams); }
    public JSONArray getEmS4Geo() { return snmpDAO.getEmS4Geo(); }
    public JSONArray getEmS4GeoHistory() { return snmpDAO.getEmS4GeoHistory(); }
    public JSONArray getLastWalk(Connection dbc) { return snmpDAO.getLastWalk(dbc); }
    public JSONArray getMain(Connection dbc, List qParams, int step) { return snmpDAO.getMain(dbc, qParams, step); }
    public JSONArray getMainLastSSH(Connection dbc) { return snmpDAO.getMainLastSSH(dbc); }
    public JSONArray getMainRecent(Connection dbc) { return snmpDAO.getMainRecent(dbc); }
    public JSONArray getMergedLastTemp(Connection dbc) { return snmpDAO.getMergedLastTemp(dbc); }
    public JSONArray getNote3(Connection dbc, List qParams, int step) { return snmpDAO.getNote3(dbc, qParams, step); }
    public JSONArray getNote3Geo(Connection dbc) { return snmpDAO.getNote3Geo(dbc); }
    public JSONArray getNote3GeoHistory() { return snmpDAO.getNote3GeoHistory(); }
    public JSONArray getNote3RapidGeoHistory() { return snmpDAO.getNote3RapidGeoHistory(); }
    public JSONArray getNote3Recent(Connection dbc) { return snmpDAO.getNote3Recent(dbc); }
    public JSONArray getNote3Sensors(List qParams) { return snmpDAO.getNote3Sensors(qParams); }
    public JSONArray getPi(Connection dbc, List qParams, int step) { return snmpDAO.getPi(dbc, qParams, step); }
    public JSONArray getPi2(Connection dbc, List qParams, int step) { return snmpDAO.getPi2(dbc, qParams, step); }
    public JSONArray getPi2Geo() { return snmpDAO.getPi2Geo(); }
    public JSONArray getPi2GeoHistory() { return snmpDAO.getPi2GeoHistory(); }
    public JSONArray getRouter(Connection dbc, List qParams, int step) { return snmpDAO.getRouter(dbc, qParams, step); }
    public JSONArray getRouterRecent(Connection dbc) { return snmpDAO.getRouterRecent(dbc); }
    public JSONArray getUbuntuVM2(Connection dbc, List qParams, int step) { return snmpDAO.getUbuntuVM2(dbc, qParams, step); }
    public JSONArray getUbuntuVM2Recent(Connection dbc) { return snmpDAO.getUbuntuVM2Recent(dbc); }
    
}
