/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 8 Apr 2018
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
    public JSONArray getAllRoutes() { return fitnessDAO.getAllRoutes(); }
    public JSONArray getBike(Connection dbc, String bike) { return fitnessDAO.getBike(dbc, bike); }    
    public JSONArray getBkStats(Connection dbc, String bike) { return fitnessDAO.getBkStats(dbc, bike); }
    public JSONArray getCalories(Connection dbc) { return fitnessDAO.getCalories(dbc); }
    public JSONArray getChCaloriesR(List qParams) { return fitnessDAO.getChCaloriesR(qParams); }
    public JSONArray getChWeightA() { return fitnessDAO.getChWeightA(); }
    public JSONArray getChWeightR(List qParams) { return fitnessDAO.getChWeightR(qParams); }
    public JSONArray getChWeightRE(List qParams) { return fitnessDAO.getChWeightRE(qParams); }
    public JSONArray getCrsm(Connection dbc) { return fitnessDAO.getCrsm(dbc); }
    public JSONArray getDay(Connection dbc) { return fitnessDAO.getDay(dbc); }
    public JSONArray getDayE(Connection dbc) { return fitnessDAO.getDayE(dbc); }
    public JSONArray getJsonLogCyc(List qParams) { return fitnessDAO.getJsonLogCyc(qParams); }
    public JSONArray getJsonLogCyc2(List qParams) { return fitnessDAO.getJsonLogCyc2(qParams); }
    public JSONArray getJsonLogRun(List qParams) { return fitnessDAO.getJsonLogRun(qParams); }
    public JSONArray getJsonLogRun2(List qParams) { return fitnessDAO.getJsonLogRun2(qParams); }
    public JSONArray getOverallStats(Connection dbc) { return fitnessDAO.getOverallStats(dbc); }
    public JSONArray getOverallSensors() { return fitnessDAO.getOverallSensors(); }
    public JSONArray getRelatedPhotos(List qParams) { return fitnessDAO.getRelatedPhotos(qParams); }
    public JSONArray getRShoe(Connection dbc) { return fitnessDAO.getRShoe(dbc); }
    public JSONArray getRSMileMax(List qParams) { return fitnessDAO.getRSMileMax(qParams); }
    public JSONArray getRPlans(Connection dbc) { return fitnessDAO.getRPlans(dbc); }
    public JSONArray getTot(Connection dbc) { return fitnessDAO.getTot(dbc); }
    public JSONArray getYear(Connection dbc, String yearIn) { return fitnessDAO.getYear(dbc, yearIn); } 
    
}
