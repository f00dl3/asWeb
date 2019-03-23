/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 23 Mar 2019
 */

package asWebRest.resource;

import asWebRest.action.GetDatabaseInfoAction;
import asWebRest.dao.DatabaseInfoDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class DatabaseInfoResource extends ServerResource {
    
    @Get
    public String represent() {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetDatabaseInfoAction getDatabaseInfoAction = new GetDatabaseInfoAction(new DatabaseInfoDAO());
        JSONArray dbInfo2 = getDatabaseInfoAction.getDatabaseInfoByTable(dbc);  
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return dbInfo2.toString();
    }
    
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetDatabaseInfoAction getDatabaseInfoAction = new GetDatabaseInfoAction(new DatabaseInfoDAO());
        
        JSONObject mergedResults = new JSONObject();
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                
                case "getDbInfo":
                    JSONArray dbInfo = getDatabaseInfoAction.getDatabaseInfo(dbc);
                    JSONArray dbInfoTables = getDatabaseInfoAction.getDatabaseInfoByTable(dbc);
                    mergedResults
                        .put("dbInfo", dbInfo)
                        .put("dbInfoByTable", dbInfoTables);
                    returnData = mergedResults.toString();
                    break;
                    
                case "getLiveRowCount":
                    JSONArray lrca = getDatabaseInfoAction.getLiveRowCount(dbc);
                    returnData = lrca.toString();
                    break;
                    
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
    
}
