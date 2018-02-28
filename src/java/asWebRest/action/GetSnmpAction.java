/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 25 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.SnmpDAO;
import java.util.List;
import org.json.JSONArray;

public class GetSnmpAction {
    
    private SnmpDAO snmpDAO;
    public GetSnmpAction(SnmpDAO snmpDAO) { this.snmpDAO = snmpDAO; }
    
    public JSONArray getEmS4Geo() { return snmpDAO.getEmS4Geo(); }
    public JSONArray getEmS4GeoHistory() { return snmpDAO.getEmS4GeoHistory(); }
    public JSONArray getLastWalk() { return snmpDAO.getLastWalk(); }
    public JSONArray getMain(List qParams) { return snmpDAO.getMain(qParams); }
    public JSONArray getMainLastCaseTemp() { return snmpDAO.getMainLastCaseTemp(); }
    public JSONArray getNote3(List qParams) { return snmpDAO.getNote3(qParams); }
    public JSONArray getNote3Geo() { return snmpDAO.getNote3Geo(); }
    public JSONArray getNote3GeoHistory() { return snmpDAO.getNote3GeoHistory(); }
    public JSONArray getNote3RapidGeoHistory() { return snmpDAO.getNote3RapidGeoHistory(); }
    public JSONArray getNote3Sensors(List qParams) { return snmpDAO.getNote3Sensors(qParams); }
    public JSONArray getPi(List qParams) { return snmpDAO.getPi(qParams); }
    public JSONArray getPiLastTemp() { return snmpDAO.getPiLastTemp(); }
    public JSONArray getPi2(List qParams) { return snmpDAO.getPi2(qParams); }
    public JSONArray getPi2Geo() { return snmpDAO.getPi2Geo(); }
    public JSONArray getPi2GeoHistory() { return snmpDAO.getPi2GeoHistory(); }
    public JSONArray getPi2LastTemp() { return snmpDAO.getPi2LastTemp(); }
    public JSONArray getRouter(List qParams) { return snmpDAO.getRouter(qParams); }
    
}
