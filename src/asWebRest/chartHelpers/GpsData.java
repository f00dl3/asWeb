/*
by Anthony Stump
Created: 17 Jun 2018
Updated: 20 Apr 2019
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class GpsData {
    
    private WebCommon wc = new WebCommon();
          
    private JSONObject gpsElement(JSONArray dataIn, String whatToDo, String xUnits, String dataPoint) {
        final String name = "GPS " + whatToDo;
        final String fileName = "gps" + whatToDo;
        JSONObject glob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        props
                .put("dateFormat", "ss")
                .put("chartName", name).put("chartFileName", fileName)
                .put("sName", whatToDo).put("sColor", "White")
                .put("xLabel", xUnits).put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                try {
                    JSONObject tLog = tGpsLog.getJSONObject(jStr);
                    if(wc.isSet(Double.toString(tLog.getDouble(dataPoint)))) {
                        labels.put(jStr);
                    	data.put(tLog.getDouble(dataPoint));
                	} else { 
                		//data.put(0.00);
            		}
                } catch (Exception e) {
                    // data.put(0.00);
                } finally { 
                	// do something here?
                }
            }
        }
        glob
                .put("labels", labels)
                .put("data", data)
                .put("props", props)
                .put("debug", data);
        return glob;
    }

    private static JSONObject hrvCh(JSONArray dataIn) {
        String tName = "Heart Rate Variability (EKG)";
        JSONObject tGlob = new JSONObject();
        JSONObject tProps = new JSONObject();
        JSONArray tLabels = new JSONArray();
        JSONArray tData = new JSONArray();
        JSONArray tData2 = new JSONArray();
        tProps
            .put("dateFormat", "ss")
            .put("chartName", tName).put("chartFileName", "gpsHrv")
            .put("sName", "HRVa").put("sColor", "Red")
            .put("s2Name", "HRVb").put("s2Color", "Red")
            .put("xLabel", "Date").put("yLabel", "Reading");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONArray tHrvGlob = thisObject.getJSONArray("hrvLog");
            for(int j = 0; j < tHrvGlob.length(); j++) {
	            JSONArray tHrvLog = tHrvGlob.getJSONArray(j);
                String jStr = String.valueOf(j);
	            tLabels.put(jStr);
	            double tHrvAFiltered = tHrvLog.getDouble(0);
	            double tHrvBFiltered = tHrvLog.getDouble(1);
	            if(tHrvAFiltered == 65535) { tHrvAFiltered = 0; }
	            if(tHrvBFiltered == 65535) { tHrvBFiltered = 0; }
	            tData.put(tHrvAFiltered);
	            tData2.put(tHrvBFiltered);
            }
        }
        tGlob
                .put("labels", tLabels)
                .put("data", tData)
                .put("data2", tData2)
                .put("props", tProps);
        return tGlob;
    }
    
    public JSONObject getGpsElement(JSONArray dataIn, String whatToDo, String xUnits, String dataPoint) { return gpsElement(dataIn, whatToDo, xUnits, dataPoint); }
    public JSONObject getHrvCh(JSONArray dataIn) { return hrvCh(dataIn); }
        
}
