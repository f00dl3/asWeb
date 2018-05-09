/*
by Anthony Stump
Created: 9 May 2018
 */

package asWebRest.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonWorkers {
    
    public static JSONArray csvToJson(String dataIn, String separator) {
        int lineNo = 0;
        List<String> csvKeys = new ArrayList<>();
        JSONArray dataOut = new JSONArray();
        Scanner csvScanner = null; try {		
            csvScanner = new Scanner(dataIn);
            while(csvScanner.hasNext()) {
                JSONObject jsonRow = new JSONObject();
                String[] lineData = csvScanner.nextLine().split(separator);
                for(int i = 0; i < lineData.length; i++) {
                    int keyLocation = 0;
                    if(lineNo == 0) {
                        csvKeys.add(lineData[i]);
                    } else {
                        for(String key : csvKeys) {
                            String fixedKey = key.replace("\"", "");
                            String fixedLineData = lineData[keyLocation].replace("\"", "");
                            jsonRow.put(fixedKey, fixedLineData);
                            keyLocation++;
                        }
                    }
                }
                if(lineNo != 0) { dataOut.put(jsonRow); }
                lineNo++;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return dataOut;
    }
    
    public static String jsonToCsv(JSONArray dataIn) {
        String csvKeys = "";
        String csvData = "";
        String csvBack = "";
        JSONObject fromFirstRow = dataIn.getJSONObject(0);
        Iterator<?> keys = fromFirstRow.keys();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            csvKeys += "\"" + key + "\",";
        }
        csvKeys = csvKeys.substring(0, csvKeys.length() - 1);
        for(int i = 0; i < dataIn.length(); i++) {
            String csvRowData = "";
            JSONObject thisRow = dataIn.getJSONObject(i);
            Iterator<?> rKeys = thisRow.keys();
            while(rKeys.hasNext()) {
                String rKey = (String) rKeys.next();
                csvRowData += "\"" + thisRow.optString(rKey) + "\",";
            }
            csvRowData = csvRowData.substring(0, csvRowData.length() - 1) + "\n";
            csvData += csvRowData;
        }
        csvBack += csvKeys + "\n" + csvData;
        return csvBack;
    }
        
    public static String desiredDataType(JSONArray dataIn, String typeDesired, String dataStoreIdentifier) {
        String returnData = "";
        switch(typeDesired) {
            case "csv": returnData = jsonToCsv(dataIn); break;
            case "json": returnData = dataIn.toString(); break;
            case "jsonFromCsv": returnData = csvToJson(jsonToCsv(dataIn), ",").toString(); break;
            case "dataStore": returnData = dojoDataStoreWrapper(dataStoreIdentifier, dataIn).toString(); break;
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
