/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 27 Mar 2019
 */

package asWebRest.action;

import asWebRest.dao.FitnessDAO;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;

public class GetFitnessAction {
    
    private FitnessDAO fitnessDAO;
    public GetFitnessAction(FitnessDAO fitnessDAO) { this.fitnessDAO = fitnessDAO; }
    
    public JSONArray getAll(Connection dbc, List qParams) { return fitnessDAO.getAll(dbc, qParams); }
    public JSONArray getAllE() { return fitnessDAO.getAllE(); }
    public JSONArray getAllRoutePlans(Connection dbc) { return fitnessDAO.getAllRoutePlans(dbc); }
    public JSONArray getAllRoutes(Connection dbc) { return fitnessDAO.getAllRoutes(dbc); }
    public JSONArray getBike(Connection dbc, String bike) { return fitnessDAO.getBike(dbc, bike); }    
    public JSONArray getBkStats(Connection dbc, String bike) { return fitnessDAO.getBkStats(dbc, bike); }
    public JSONArray getCalories(Connection dbc) { return fitnessDAO.getCalories(dbc); }
    public JSONArray getChCaloriesR(Connection dbc, List qParams) { return fitnessDAO.getChCaloriesR(dbc, qParams); }
    public JSONArray getChWeightA() { return fitnessDAO.getChWeightA(); }
    public JSONArray getChWeightR(Connection dbc, List qParams) { return fitnessDAO.getChWeightR(dbc, qParams); }
    public JSONArray getChWeightRE(List qParams) { return fitnessDAO.getChWeightRE(qParams); }
    public JSONArray getCrsm(Connection dbc) { return fitnessDAO.getCrsm(dbc); }
    public JSONArray getDay(Connection dbc) { return fitnessDAO.getDay(dbc); }
    public JSONArray getDayE(Connection dbc) { return fitnessDAO.getDayE(dbc); }
    public JSONArray getDayY(Connection dbc) { return fitnessDAO.getDayY(dbc); }
    public JSONArray getGeoJSON(Connection dbc, List qParams) { return fitnessDAO.getGeoJSON(dbc, qParams); }
    public JSONArray getJsonLogCyc(Connection dbc, List qParams) { return fitnessDAO.getJsonLogCyc(dbc, qParams); }
    public JSONArray getJsonLogCyc2(Connection dbc, List qParams) { return fitnessDAO.getJsonLogCyc2(dbc, qParams); }
    public JSONArray getJsonLogCyc3(Connection dbc, List qParams) { return fitnessDAO.getJsonLogCyc3(dbc, qParams); }
    public JSONArray getJsonLogCyc4(Connection dbc, List qParams) { return fitnessDAO.getJsonLogCyc4(dbc, qParams); }
    public JSONArray getJsonLogRun(Connection dbc, List qParams) { return fitnessDAO.getJsonLogRun(dbc, qParams); }
    public JSONArray getJsonLogRun2(Connection dbc, List qParams) { return fitnessDAO.getJsonLogRun2(dbc, qParams); }
    public JSONArray getJsonLogRun3(Connection dbc, List qParams) { return fitnessDAO.getJsonLogRun3(dbc, qParams); }
    public JSONArray getJsonLogRun4(Connection dbc, List qParams) { return fitnessDAO.getJsonLogRun4(dbc, qParams); }
    public JSONArray getOverallStats(Connection dbc) { return fitnessDAO.getOverallStats(dbc); }
    public JSONArray getOverallSensors(Connection dbc) { return fitnessDAO.getOverallSensors(dbc); }
    public JSONArray getPhysical(Connection dbc) { return fitnessDAO.getPhysical(dbc); }
    public JSONArray getRelatedPhotos(Connection dbc, List qParams) { return fitnessDAO.getRelatedPhotos(dbc, qParams); }
    public JSONArray getRShoe(Connection dbc) { return fitnessDAO.getRShoe(dbc); }
    public JSONArray getRSMileMax(List qParams) { return fitnessDAO.getRSMileMax(qParams); }
    public JSONArray getRPlanByDesc(Connection dbc, List qParams) { return fitnessDAO.getRPlanByDesc(dbc, qParams); }
    public JSONArray getRPlans(Connection dbc) { return fitnessDAO.getRPlans(dbc); }
    public JSONArray getTot(Connection dbc) { return fitnessDAO.getTot(dbc); }
    public JSONArray getYear(Connection dbc, String yearIn) { return fitnessDAO.getYear(dbc, yearIn); } 
    
}
