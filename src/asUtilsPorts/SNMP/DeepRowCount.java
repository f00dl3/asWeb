/*
by Anthony Stump
Created: 28 Mar 2019
Updated: 17 Dec 2020
 */

package asUtilsPorts.SNMP;

import asWebRest.shared.WebCommon;

import java.sql.Connection;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class DeepRowCount {
        
    public JSONArray getLiveRowCount(Connection dbc, String whatTable) {
        
    	WebCommon wc = new WebCommon();
        
        final String query_TableSummary = "SELECT" + 
        		" CONCAT(table_schema, '.', table_name) AS dbTableName" + 
        		" FROM information_schema.tables" + 
        		" WHERE table_schema IN ('" + whatTable + "');";
        JSONArray tContainer = new JSONArray();
        /* try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_TableSummary, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
            	final String tableName = resultSet.getString("dbTableName");
                final String query_ThisTableRowCount = "SELECT COUNT(*) AS RowCount FROM " + tableName + ";";
            	ResultSet subResultSet = wc.q2rs1c(dbc, query_ThisTableRowCount, null);
                while (subResultSet.next()) {
	                tObject
	                    .put("dbTableName", tableName)
	                    .put("RowCount", subResultSet.getLong("RowCount"));                    
	                tContainer.put(tObject);
                }
                subResultSet.close();
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); } */
        return tContainer;
        
    }
    
    public long totalRowCountFromDatabase(Connection dbc, String whatTable) {
        
        long rowCount = 0;
        
        /* JSONArray jsonData = getLiveRowCount(dbc, whatTable);
        
        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject thisObject = jsonData.getJSONObject(i);
            long thisRowCount = thisObject.getLong("RowCount");
            rowCount += thisRowCount;
        } */
        
        return rowCount;
        
    }
    
    public void main(Connection dbc) {
    	        
        long rows_Core = totalRowCountFromDatabase(dbc, "Core");
        long rows_NetSNMP = totalRowCountFromDatabase(dbc, "net_snmp");
        long rows_WxObs = totalRowCountFromDatabase(dbc, "WxObs");
        long rows_Feeds = totalRowCountFromDatabase(dbc, "Feeds");
        long rows_Finances = totalRowCountFromDatabase(dbc, "Finances");
        long rows_WebCal = totalRowCountFromDatabase(dbc, "WebCal");
                
        System.out.println("Core: " + rows_Core + "\n" +
                "Net SNMP: " + rows_NetSNMP + "\n" +
                "WxObs: " + rows_WxObs + "\n" +
                "Feeds: " + rows_Feeds + "\n" +
                "Finances: " + rows_Finances + "\n" +
                "WebCal: " + rows_WebCal);
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
    }

}
