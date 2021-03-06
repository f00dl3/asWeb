/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 23 Mar 2019
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseInfoDAO {

    WebCommon wc = new WebCommon();
    
    public JSONArray getDbInfo(Connection dbc) {
        final String query_Logs_DBInfo = "SELECT" +
        " table_schema 'Database'," +
        " SUM(data_length + index_length) AS 'DBSize'," +
        " GREATEST(SUM(TABLE_ROWS), SUM(AUTO_INCREMENT)) AS 'DBRows'" +
        " FROM information_schema.tables" +
        " GROUP BY table_schema;";
        JSONArray dbInfo = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Logs_DBInfo, null);
            while (resultSet.next()) {
                JSONObject tDbInfo = new JSONObject();
                tDbInfo
                    .put("Database", resultSet.getString("Database"))
                    .put("DBSize", resultSet.getLong("DBSize"))
                    .put("DBRows", resultSet.getLong("DBRows"));
                dbInfo.put(tDbInfo);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return dbInfo;
    }
    
    public JSONArray getDbInfoByTable(Connection dbc) {
        final String query_Logs_DBInfo2 = "SELECT" +
        " isT.table_schema AS `Database`," +
        " isT.table_name AS `Table`," +
        " (isT.data_length + isT.index_length) AS `Size`," +
        " isT.table_rows AS `Rows`" +
        " FROM information_schema.TABLES isT" +
        " WHERE data_length > 0" +
        " ORDER BY (data_length + index_length) DESC;";
        JSONArray dbInfo2 = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Logs_DBInfo2, null);
            while (resultSet.next()) {
                JSONObject tDbInfo2 = new JSONObject();
                tDbInfo2
                    .put("Database", resultSet.getString("Database"))
                    .put("Table", resultSet.getString("Table"))
                    .put("Size", resultSet.getLong("Size"))
                    .put("Rows", resultSet.getLong("Rows"));
                dbInfo2.put(tDbInfo2);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return dbInfo2;
    }
    
    public JSONArray getLiveRowCount(Connection dbc) {
        final String query_TableSummary = "SELECT" + 
        		" CONCAT(table_schema, '.', table_name) AS dbTableName" + 
        		" FROM information_schema.tables" + 
        		" WHERE table_schema NOT IN ('performance_schema', 'mysql', 'information_schema', 'sys');";
        JSONArray tContainer = new JSONArray();
        try {
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
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
}
