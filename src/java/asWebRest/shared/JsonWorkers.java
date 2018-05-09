/*
by Anthony Stump
Created: 9 May 2018
 */

package asWebRest.shared;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonWorkers {
    
    public static String convertToCsv(JSONArray dataIn) {
        String csvData = "";
        JSONObject fromFirstRow = dataIn.getJSONObject(0);
        csvData += fromFirstRow.toString();
        return csvData;
    }
        
    public static String desiredDataType(JSONArray dataIn, String typeDesired, String dataStoreIdentifier) {
        String returnData = "";
        switch(typeDesired) {
            case "json":
                returnData = dataIn.toString();
                break;
            case "dataStore":
                returnData = dojoDataStoreWrapper(dataStoreIdentifier, dataIn).toString();
                break;
        }
        return returnData;
    }
    
    public static JSONObject dojoDataStoreWrapper(String identifier, JSONArray items) {
        JSONObject wrappedDataStore = new JSONObject();
        wrappedDataStore
            .put("label", identifier)
            .put("identifier", identifier)
            .put("items", items);
        return wrappedDataStore;
    }
        
    
}
