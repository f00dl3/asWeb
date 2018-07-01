/*
by Anthony Stump
Created: 9 May 2018
Updated: 30 Jun 2018
 */

package asWebRest.resource;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.JsonWorkers;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ToolsResource extends ServerResource {
       
    @Post
    public String doPost(Representation argsIn) {
        
        JsonWorkers jw = new JsonWorkers();
        MyDBConnector mdb = new MyDBConnector();
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String errorMessage = "";
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                
                case "JsonToDataStore":
                    JSONArray dataInput = new JSONArray();
                    dataInput.put(argsInForm.getFirstValue("jsonToConvert"));
                    returnData = jw.getDesiredDataType(
                            dataInput,
                            "dataStore",
                            argsInForm.getFirstValue("identifier")
                    );
                    break;
                    
                case "RequestResource":
                    String fileToTransfer = null;
                    if(wc.isSet(argsInForm.getFirstValue("fileToTransfer"))) {
                        fileToTransfer = argsInForm.getFirstValue("fileToTransfer");
                        returnData += "REQUESTED [ " + fileToTransfer + " ]";
                    }
                    break;
                
                case "LukeFolderWalker":
                    JSONObject errorEncapsulator = new JSONObject();
                    JSONObject folderResults = new JSONObject();
                    String folderToScan = "";
                    if(wc.isSet(argsInForm.getFirstValue("scanPath"))) { 
                        try {
                            folderToScan = argsInForm.getFirstValue("scanPath");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        folderToScan = cb.getPathTomcat().toString();
                    }
                    try { folderResults = wc.lukePathWalker(folderToScan); } catch (Exception e) { errorMessage = e.getMessage(); }
                    errorEncapsulator
                            .put("InFolder", folderToScan)
                            .put("Errors", errorMessage)
                            .put("Results", folderResults);
                    returnData = errorEncapsulator.toString();
                    break;
                    
            }
        }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }

}
