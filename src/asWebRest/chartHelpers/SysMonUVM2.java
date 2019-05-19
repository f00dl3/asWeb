/*
by Anthony Stump
Created: 19 May 2019
Updated: on Creation
 */

package asWebRest.chartHelpers;

import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class SysMonUVM2 {
    
    private WebCommon wc = new WebCommon();
    
    private JSONObject mSysCPU(JSONArray dataIn, int intLen, int step) {
        String mSysCPU_ChartName = "Desktop: CPU Load";
        JSONObject mSysCPU_Glob = new JSONObject();
        JSONObject mSysCPU_Props = new JSONObject();
        JSONArray mSysCPU_Labels = new JSONArray();
        JSONArray mSysCPU_Data = new JSONArray();
        mSysCPU_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mSysCPU_ChartName).put("chartFileName", "mUvm2CPU")
                .put("sName", "Avg CPU").put("sColor", "Yellow")
                .put("xLabel", "WalkTime").put("yLabel", "% Use");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            float mSysCPU_LoadAverage = 0.00f;
            int activeCPUs = 0;
            int mSysCPU1 = 0;
            int mSysCPU2 = 0;
            int mSysCPU3 = 0;
            int mSysCPU4 = 0;
            int mSysCPU5 = 0;
            int mSysCPU6 = 0;
            int mSysCPU7 = 0;
            int mSysCPU8 = 0;
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad1")))) { try { mSysCPU1 = thisObject.getInt("CPULoad1"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad2")))) { try { mSysCPU2 = thisObject.getInt("CPULoad2"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad3")))) { try { mSysCPU3 = thisObject.getInt("CPULoad3"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad4")))) { try { mSysCPU4 = thisObject.getInt("CPULoad4"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad5")))) { try { mSysCPU1 = thisObject.getInt("CPULoad5"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad6")))) { try { mSysCPU2 = thisObject.getInt("CPULoad6"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad7")))) { try { mSysCPU3 = thisObject.getInt("CPULoad7"); activeCPUs++; } catch (Exception e) { } }
            if(wc.isSet(Integer.toString(thisObject.getInt("CPULoad8")))) { try { mSysCPU4 = thisObject.getInt("CPULoad8"); activeCPUs++; } catch (Exception e) { } }
            try {
                mSysCPU_LoadAverage = ((mSysCPU1 + mSysCPU2 + mSysCPU3 + mSysCPU4 + mSysCPU5 + mSysCPU6 + mSysCPU7 + mSysCPU8) / activeCPUs );
            } catch (Exception e) { e.printStackTrace(); }
            mSysCPU_Labels.put(thisObject.getString("WalkTime"));
            mSysCPU_Data.put(mSysCPU_LoadAverage);
        }
        mSysCPU_Glob
                .put("labels", mSysCPU_Labels)
                .put("data", mSysCPU_Data)
                .put("props", mSysCPU_Props);
        return mSysCPU_Glob;
    }

    private JSONObject mSysLoad(JSONArray dataIn, int intLen, int step) {
        String mSysLoad_ChartName = "Desktop: System Load";
        JSONObject mSysLoad_Glob = new JSONObject();
        JSONObject mSysLoad_Props = new JSONObject();
        JSONArray mSysLoad_Labels = new JSONArray();
        JSONArray mSysLoad_Data = new JSONArray();
        JSONArray mSysLoad_Data2 = new JSONArray();
        JSONArray mSysLoad_Data3 = new JSONArray();           
        mSysLoad_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mSysLoad_ChartName).put("chartFileName", "mUvm2Load")
                .put("sName", "LoadIndex").put("sColor", "Red")
                .put("s2Name", "Load 5").put("s2Color", "Green")
                .put("s3Name", "Load 15").put("s3Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Load Index");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            mSysLoad_Labels.put(thisObject.getString("WalkTime"));
	double mSysLoad1 = 0.00;
	double mSysLoad5 = 0.00;
	double mSysLoad15 = 0.00;
	if(wc.isSet(Double.toString(thisObject.getDouble("LoadIndex1")))) { try { mSysLoad1 = thisObject.getDouble("LoadIndex1"); } catch (Exception e) { } }
	if(wc.isSet(Double.toString(thisObject.getDouble("LoadIndex5")))) { try { mSysLoad5 = thisObject.getDouble("LoadIndex5"); } catch (Exception e) { } }
	if(wc.isSet(Double.toString(thisObject.getDouble("LoadIndex15")))) { try { mSysLoad15 = thisObject.getDouble("LoadIndex15"); } catch (Exception e) { } }
            mSysLoad_Data.put(mSysLoad1);
            mSysLoad_Data2.put(mSysLoad5);
            mSysLoad_Data3.put(mSysLoad15);
        }
        mSysLoad_Glob
                .put("labels", mSysLoad_Labels)
                .put("data", mSysLoad_Data)
                .put("data2", mSysLoad_Data2)
                .put("data3", mSysLoad_Data3)
                .put("props", mSysLoad_Props);
        return mSysLoad_Glob;
    }
     
    private JSONObject mSysMemory(JSONArray dataIn, int intLen, int step) {
        String mSysMemory_ChartName = "Desktop: Memory Use";
        JSONObject mSysMemory_Glob = new JSONObject();
        JSONObject mSysMemory_Props = new JSONObject();
        JSONArray mSysMemory_Labels = new JSONArray();
        JSONArray mSysMemory_Data = new JSONArray();
        JSONArray mSysMemory_Data2 = new JSONArray();
        JSONArray mSysMemory_Data3 = new JSONArray();
        JSONArray mSysMemory_Data4 = new JSONArray();
        mSysMemory_Props
                .put("dateFormat", "yyyyMMddHHmmss")
                .put("chartName", mSysMemory_ChartName).put("chartFileName", "mUvm2Memory")
                .put("sName", "Used").put("sColor", "Yellow")
                .put("s2Name", "Swap").put("s2Color", "Red")
                .put("s3Name", "Buffers").put("s3Color", "Green")
                .put("s4Name", "Cached").put("s4Color", "Blue")
                .put("xLabel", "WalkTime").put("yLabel", "Use in MBs");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            float mSysMemory_Used = 0.00f;
            try { mSysMemory_Used = ((thisObject.getFloat("KMemPhysU")-(thisObject.getFloat("KMemBuffU")+thisObject.getFloat("KMemCachedU")))/1024); } catch (Exception e) { e.printStackTrace(); }
            mSysMemory_Labels.put(thisObject.getString("WalkTime"));
            mSysMemory_Data.put(mSysMemory_Used);
            mSysMemory_Data2.put(thisObject.getFloat("KSwapU")/1024);
            mSysMemory_Data3.put(thisObject.getFloat("KMemBuffU")/1024);
            mSysMemory_Data4.put(thisObject.getFloat("KMemCachedU")/1024);
        }
        mSysMemory_Glob
                .put("labels", mSysMemory_Labels)
                .put("data", mSysMemory_Data)
                .put("data2", mSysMemory_Data2)
                .put("data3", mSysMemory_Data3)
                .put("data4", mSysMemory_Data4)
                .put("props", mSysMemory_Props);
        return mSysMemory_Glob;
    }
                    
    public JSONObject getSysCPU(JSONArray dataIn, int intLen, int step) { return mSysCPU(dataIn, intLen, step); }
    public JSONObject getSysLoad(JSONArray dataIn, int intLen, int step) { return mSysLoad(dataIn, intLen, step); }
    public JSONObject getSysMemory(JSONArray dataIn, int intLen, int step) { return mSysMemory(dataIn, intLen, step); }
    
}
