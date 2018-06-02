/*
by Anthony Stump
Created: 16 Feb 2018
Updated: 2 Jun 2018

Applies to multiple tables:
    Core.Fitness
    Core.Fit_Em

 */

package asWebRest.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Fitness {
    
    private JSONArray altGeoJSON;
    private String bicycle;
    private int bkCln;
    private int bkFlt;
    private int bkNBreakPadF;
    private int bkNBreakPadR;
    private int bkNCasette;
    private int bkNChain;
    private int bkNTireF;
    private int bkNTireFS;
    private int bkNTireR;
    private int bkNTireRS;
    private int bkNWheelF;
    private int bkNWheelR;
    private int bkOH;
    private int bkStudT;
    private int calories;
    private int carbs;
    private int cholest;
    private int commonRoute;
    private int cycCadenceAvg;
    private int cycCadenceMax;
    private JSONArray cycGeoJSON;
    private int cycHeartAvg;
    private int cycHeartMax;
    private int cycPowerAvg;
    private int cycPowerMax;
    private double cycSpeedAvg;
    private double cycSpeedMax;
    private double cycling;
    private String date;
    private double estHoursSleep;
    private int exMin;
    private int fat;
    private int fiber;
    private double fruitsVeggies;
    private JSONObject gpsLogCyc;
    private JSONObject gpsLogCyc2;
    private JSONObject gpsLogRun;
    private JSONObject gpsLogRun2;
    private int gym;
    private String gymWorkout;
    private String mowNotes;
    private int protein;
    private int reelMow;
    private double rsMile;
    private JSONArray runGeoJSON;
    private int runHeartAvg;
    private int runHeartMax;
    private double runSpeedAvg;
    private double runSpeedMax;
    private double runWalk;
    private String shoe;
    private int sodium;
    private int sugar;
    private int swimming;
    private int trackedTime;
    private double trackedDist;
    private int vomit;
    private int water;
    private double weight;
    private String xTags;
    
    public JSONArray altGeoJSON() { return altGeoJSON; }
    public String getBicycle() { return bicycle; }
    public int getBkCln() { return bkCln; }
    public int getBkFlt() { return bkFlt; }
    public int getBkNBreakPadF() { return bkNBreakPadF; }
    public int getBkNBreakPadR() { return bkNBreakPadR; }
    public int getBkNCasette() { return bkNCasette; }
    public int getBkNChain() { return bkNChain; }
    public int getBkNTireF() { return bkNTireF; }
    public int getBkNTireFS() { return bkNTireFS; }
    public int getBkNTireR() { return bkNTireR; }
    public int getBkNTireRS() { return bkNTireRS; }
    public int getBkNWheelF() { return bkNWheelF; }
    public int getBkNWheelR() { return bkNWheelR; }
    public int getBkOH() { return bkOH; }
    public int getBkStudT() { return bkStudT; }
    public int getCalories() { return calories; }
    public int getCarbs() { return carbs; }
    public int getCholest() { return cholest; }
    public int getCommonRoute() { return commonRoute; }
    public int getCycCadenceAvg() { return cycCadenceAvg; }
    public int getCycCadenceMax() { return cycCadenceMax; }
    public JSONArray cycGeoJSON() { return cycGeoJSON; }
    public int getCycHeartAvg() { return cycHeartAvg; }
    public int getCycHeartMax() { return cycHeartMax; }
    public int getCycPowerAvg() { return cycPowerAvg; }
    public int getCycPowerMax() { return cycPowerMax; }
    public double getCycSpeedAvg() { return cycSpeedAvg; }
    public double getCycSpeedMax() { return cycSpeedMax; }
    public double getCycling() { return cycling; }
    public String getDate() { return date; }
    public double getEstHoursSleep() { return estHoursSleep; }
    public int getExMin() { return exMin; }
    public int getFat() { return fat; }
    public int getFiber() { return fiber; }
    public double getFruitsVeggies() { return fruitsVeggies; }
    public JSONObject getGpsLogCyc() { return gpsLogCyc; }
    public JSONObject getGpsLogCyc2() { return gpsLogCyc2; }
    public JSONObject getGpsLogRun() { return gpsLogRun; }
    public JSONObject getGpsLogRun2() { return gpsLogRun2; }
    public int getGym() { return gym; }
    public String getGymWorkout() { return gymWorkout; }
    public String getMowNotes() { return mowNotes; }
    public int getProtein() { return protein; }
    public int getReelMow() { return reelMow; }
    public double getRsMile() { return rsMile; }
    public JSONArray runGeoJSON() { return runGeoJSON; }
    public int getRunHeartAvg() { return runHeartAvg; }
    public int getRunHeartMax() { return runHeartMax; }
    public double getRunSpeedAvg() { return runSpeedAvg; }
    public double getRunSpeedMax() { return runSpeedMax; }
    public double getRunWalk() { return runWalk; }
    public String getShoe() { return shoe; }
    public int getSodium() { return sodium; }
    public int getSugar() { return sugar; }
    public int getSwimming() { return swimming; }
    public int getTrackedTime() { return trackedTime; }
    public double getTrackedDist() { return trackedDist; }
    public int getVomit() { return vomit; }
    public int getWater() { return water; }
    public double getWeight() { return weight; }
    public String getXTags() { return xTags; }
    
    public void setAltGeoJSON(JSONArray altGeoJSON) { this.altGeoJSON = altGeoJSON; }
    public void setBicycle(String bicycle) { this.bicycle = bicycle; }
    public void setBkCln(int bkCln) { this.bkCln = bkCln; }
    public void setBkFlt(int bkFlt) { this.bkFlt = bkFlt; }
    public void setBkNBreakPadF(int bkNBreakPadF) { this.bkNBreakPadF = bkNBreakPadF; }
    public void setBkNBreakPadR(int bkNBreakPadR) { this.bkNBreakPadR = bkNBreakPadR; }
    public void setBkNCasette(int bkNCasette) { this.bkNCasette = bkNCasette; }
    public void setBkNChain(int bkNChain) { this.bkNChain = bkNChain; }
    public void setBkNTireF(int bkNTireF) { this.bkNTireF = bkNTireF; }
    public void setBkNTireFS(int bkNTireFS) { this.bkNTireFS = bkNTireFS; }
    public void setBkNTireR(int bkNTireR) { this.bkNTireR = bkNTireR; }
    public void setBkNTireRS(int bkNTireRS) { this.bkNTireRS = bkNTireRS; }
    public void setBkNWheelF(int bkNWheelF) { this.bkNWheelF = bkNWheelF; }
    public void setBkNWheelR(int bkNWheelR) { this.bkNWheelR = bkNWheelR; }
    public void setBkOH(int bkOH) { this.bkOH = bkOH; }
    public void setBkStudT(int bkStudT) { this.bkStudT = bkStudT; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setCarbs(int carbs) { this.carbs = carbs; }
    public void setCholest(int cholest) { this.cholest = cholest; }
    public void setCommonRoute(int commonRoute) { this.commonRoute = commonRoute; }
    public void setCycCadenceAvg(int cycCadenceAvg) { this.cycCadenceAvg = cycCadenceAvg; }
    public void setCycCadenceMax(int cycCadenceMax) { this.cycCadenceMax = cycCadenceMax; }
    public void setCycGeoJSON(JSONArray cycGeoJSON) { this.cycGeoJSON = cycGeoJSON; }
    public void setCycHeartAvg(int cycHeartAvg) { this.cycHeartAvg = cycHeartAvg; }
    public void setCycHeartMax(int cycHeartMax) { this.cycHeartMax = cycHeartMax; }
    public void setCycPowerAvg(int cycPowerAvg) { this.cycPowerAvg = cycPowerAvg; }
    public void setCycPowerMax(int cycPowerMax) { this.cycPowerMax = cycPowerMax; }
    public void setCycSpeedAvg(double cycSpeedAvg) { this.cycSpeedAvg = cycSpeedAvg; }
    public void setCycSpeedMax(double cycSpeedMax) { this.cycSpeedMax = cycSpeedMax; }
    public void setCycling(double cycling) { this.cycling = cycling; }
    public void setDate(String date) { this.date = date; }
    public void setExMin(int exMin) { this.exMin = exMin; }
    public void setFat(int fat) { this.fat = fat; }
    public void setFiber(int fiber) { this.fiber = fiber; }
    public void setFruitsVeggies(double fruitsVeggies) { this.fruitsVeggies = fruitsVeggies; }
    public void setGpsLogCyc(JSONObject gpsLogCyc) { this.gpsLogCyc = gpsLogCyc; }
    public void setGpsLogCyc2(JSONObject gpsLogCyc2) { this.gpsLogCyc2 = gpsLogCyc2; }
    public void setGpsLogRun(JSONObject gpsLogRun) { this.gpsLogRun = gpsLogRun; }
    public void setGpsLogRun2(JSONObject gpsLogRun2) { this.gpsLogRun2 = gpsLogRun2; }
    public void setGym(int gym) { this.gym = gym; }
    public void setGymWorkout(String gymWorkout) { this.gymWorkout = gymWorkout; }
    public void setMowNotes(String mowNotes) { this.mowNotes = mowNotes; }
    public void setProtein(int protein) { this.protein = protein; }
    public void setReelMow(int reelMow) { this.reelMow = reelMow; }
    public void setRsMile(double rsMile) { this.rsMile = rsMile; }
    public void setRunGeoJSON(JSONArray runGeoJSON) { this.runGeoJSON = runGeoJSON; }
    public void setRunHeartAvg(int runHeartAvg) { this.runHeartAvg = runHeartAvg; }
    public void setRunHeartMax(int runHeartMax) { this.runHeartMax = runHeartMax; }
    public void setRunSpeedAvg(double runSpeedAvg) { this.runSpeedAvg = runSpeedAvg; }
    public void setRunSpeedMax(double runSpeedMax) { this.runSpeedMax = runSpeedMax; }
    public void setRunWalk(double runWalk) { this.runWalk = runWalk; }
    public void setShoe(String shoe) { this.shoe = shoe; }
    public void setSodium(int sodium) { this.sodium = sodium; }
    public void setSugar(int sugar) { this.sugar = sugar; }
    public void setSwimming(int swimming) { this.swimming = swimming; }
    public void setTrackedTime(int trackedTime) { this.trackedTime = trackedTime; }
    public void setTrackedDist(double trackedDist) { this.trackedDist = trackedDist; }
    public void setVomit(int vomit) { this.vomit = vomit; }
    public void setWater(int water) { this.water = water; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setXTags(String xTags) { this.xTags = xTags; }
    
}
