/*
by Anthony Stump
Created: 22 Apr 2018
Updated: 1 Jul 2018
 */

package asWebRest.resource;

import asWebRest.action.GetNewsFeedAction;
import asWebRest.dao.NewsFeedDAO;
import asWebRest.hookers.SnmpWalk;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.JsonWorkers;
import asWebRest.shared.MyDBConnector;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class TestResource extends ServerResource {
    
    @Get
    public String represent() {
        
        JsonWorkers jw = new JsonWorkers();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        CommonBeans cb = new CommonBeans();
        
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        
        //snmpWalk [ -c commName -p portNum -v snmpVer] targetAddr oid
        String snmpArgs[] = {
            "-c", "public",
            "-p", "161",
            "-v", "3",
            "127.0.0.1",
            "1.3.6.1.4.1.7950.2.10.31.3"
        };
        
        String snmpTest1 = "";
        String snmpTest2 = "";
        
        try {
            SnmpWalk snmpWalk = new SnmpWalk();
            //snmpTest1 = snmpWalk.get("HOST-RESOURCES-MIB::hrSystemUptime.0");
            //snmpTest2 = snmpWalk.get("HOST-RESOURCES-MIB::hrSystemProcesses.0");
        } catch (IOException io) { io.printStackTrace(); }
        
        String test1 = getRequest().getRootRef().toString();
        String test2 = System.getProperty("catalina.base");
        
        String testData = "test1: " + test1 + "\n" +
                "test 2: " + test2 + "\n\n" +
                "snmpTest1: " + snmpTest1 + "\n" +
                "snmpTest2: " + snmpTest2 + "\n";
        
        List<String> qParams = new ArrayList<>();
        qParams.add(0, "2018-05-09%");
        testData += "\n" + jw.getDesiredDataType(
                getNewsFeedAction.getRedditFeeds(dbc, qParams),
                "jsonFromCsv",
                null
        );
        
        String testDate = "2018-05-13";
        LocalDate convertedDate = LocalDate.parse(testDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
        testData += "\nTest date end of month: " + convertedDate + "\n\n";
        
        testData += cb.getCatalinaHome();
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                    
        return testData;
        
    }    
       
    @Post
    public String doPost(Representation argsIn) {
        
        JsonWorkers jw = new JsonWorkers();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetNewsFeedAction getNewsFeedAction = new GetNewsFeedAction(new NewsFeedDAO());
        
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
                
                case "dojoDataStoreTest":
                    qParams.add(0, argsInForm.getFirstValue("searchDate"));
                    returnData = jw.getDesiredDataType(
                            getNewsFeedAction.getRedditFeeds(dbc, qParams),
                            "dataStore",
                            null
                    );
                    break;
                
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }

}