/*
by Anthony Stump
Created: 17 Jun 2018
Updated: 18 Jun 2018
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class GpsData {
    
    private WebCommon wc = new WebCommon();
     
    private JSONObject gpsElevation(JSONArray dataIn) {
        final String name = "GPS Altitude";
        JSONObject glob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        props
                .put("dateFormat", "ss")
                .put("chartName", name).put("chartFileName", "gpsElevation")
                .put("sName", "Elevation").put("sColor", "White")
                .put("xLabel", "Feet").put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                JSONObject tLog = tGpsLog.getJSONObject(jStr);
                try {
                    if(wc.isSet(Double.toString(tLog.getDouble("AltitudeFt")))) { data.put(tLog.getDouble("AltitudeFt")); } else { data.put(0.00); }
                } catch (Exception e) {
                    data.put(0.00);
                } finally { 
                    labels.put(jStr);
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
    
    private JSONObject gpsHeartRate(JSONArray dataIn) {
        final String name = "GPS Heart Rate";
        JSONObject glob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        props
                .put("dateFormat", "ss")
                .put("chartName", name).put("chartFileName", "gpsHeartRate")
                .put("sName", "Heart Rate").put("sColor", "White")
                .put("xLabel", "BPM").put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                JSONObject tLog = tGpsLog.getJSONObject(jStr);
                try {
                    if(wc.isSet(Double.toString(tLog.getDouble("HeartRate")))) { data.put(tLog.getDouble("HeartRate")); } else { data.put(0.00); }
                } catch (Exception e) {
                    data.put(0.00);
                } finally { 
                    labels.put(jStr);
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
    
    private JSONObject gpsSpeed(JSONArray dataIn) {
        final String name = "GPS Speed";
        JSONObject glob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        JSONArray debug = new JSONArray();
        props
                .put("dateFormat", "ss")
                .put("chartName", name).put("chartFileName", "gpsSpeed")
                .put("sName", "Speed").put("sColor", "White")
                .put("xLabel", "MPH").put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                JSONObject tLog = tGpsLog.getJSONObject(jStr);
                if(j == 0) { debug.put(tLog); }
                try {
                    if(wc.isSet(Double.toString(tLog.getDouble("SpeedMPH")))) { data.put(tLog.getDouble("SpeedMPH")); } else { data.put(0.00); }
                } catch (Exception e) {
                    data.put(0.00);
                } finally { 
                    labels.put(jStr);
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
        
    private JSONObject gpsTemperature(JSONArray dataIn) {
        final String name = "GPS Temperature";
        JSONObject glob = new JSONObject();
        JSONObject props = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray data = new JSONArray();
        props
                .put("dateFormat", "ss")
                .put("chartName", name).put("chartFileName", "gpsTemperature")
                .put("sName", "Temperature").put("sColor", "White")
                .put("xLabel", "Degrees F").put("yLabel", "Interval");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            JSONObject tGpsLog = thisObject.getJSONObject("gpsLog");
            for(int j = 0; j < tGpsLog.length(); j++) {
                String jStr = String.valueOf(j);
                JSONObject tLog = tGpsLog.getJSONObject(jStr);
                try {
                    if(wc.isSet(Double.toString(tLog.getDouble("TemperatureF")))) { data.put(tLog.getDouble("TemperatureF")); } else { data.put(0.00); }
                } catch (Exception e) {
                    data.put(0.00);
                } finally { 
                    labels.put(jStr);
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
    
    public JSONObject getGpsElevation(JSONArray dataIn) { return gpsElevation(dataIn); }
    public JSONObject getGpsHeartRate(JSONArray dataIn) { return gpsHeartRate(dataIn); }
    public JSONObject getGpsSpeed(JSONArray dataIn) { return gpsSpeed(dataIn); }
    public JSONObject getGpsTemperature(JSONArray dataIn) { return gpsTemperature(dataIn); }
        
}
