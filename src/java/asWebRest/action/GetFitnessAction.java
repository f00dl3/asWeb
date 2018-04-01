/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 1 Apr 2018
 */

package asWebRest.action;

import asWebRest.dao.FitnessDAO;
import java.util.List;
import org.json.JSONArray;

public class GetFitnessAction {
    
    private FitnessDAO fitnessDAO;
    public GetFitnessAction(FitnessDAO fitnessDAO) { this.fitnessDAO = fitnessDAO; }
    
    public JSONArray getAll(List qParams) { return fitnessDAO.getAll(qParams); }
    public JSONArray getAllE() { return fitnessDAO.getAllE(); }
    public JSONArray getAllRoutes() { return fitnessDAO.getAllRoutes(); }
    public JSONArray getBike(String bike) { return fitnessDAO.getBike(bike); }    
    public JSONArray getBkStats(String bike) { return fitnessDAO.getBkStats(bike); }
    public JSONArray getCalories() { return fitnessDAO.getCalories(); }
    public JSONArray getChCaloriesR(List qParams) { return fitnessDAO.getChCaloriesR(qParams); }
    public JSONArray getChWeightA() { return fitnessDAO.getChWeightA(); }
    public JSONArray getChWeightR(List qParams) { return fitnessDAO.getChWeightR(qParams); }
    public JSONArray getChWeightRE(List qParams) { return fitnessDAO.getChWeightRE(qParams); }
    public JSONArray getCrsm() { return fitnessDAO.getCrsm(); }
    public JSONArray getDay() { return fitnessDAO.getDay(); }
    public JSONArray getDayE() { return fitnessDAO.getDayE(); }
    public JSONArray getJsonLogCyc(List qParams) { return fitnessDAO.getJsonLogCyc(qParams); }
    public JSONArray getJsonLogCyc2(List qParams) { return fitnessDAO.getJsonLogCyc2(qParams); }
    public JSONArray getJsonLogRun(List qParams) { return fitnessDAO.getJsonLogRun(qParams); }
    public JSONArray getJsonLogRun2(List qParams) { return fitnessDAO.getJsonLogRun2(qParams); }
    public JSONArray getOverallStats() { return fitnessDAO.getOverallStats(); }
    public JSONArray getOverallSensors() { return fitnessDAO.getOverallSensors(); }
    public JSONArray getRelatedPhotos(List qParams) { return fitnessDAO.getRelatedPhotos(qParams); }
    public JSONArray getRShoe() { return fitnessDAO.getRShoe(); }
    public JSONArray getRSMileMax(List qParams) { return fitnessDAO.getRSMileMax(qParams); }
    public JSONArray getRPlans() { return fitnessDAO.getRPlans(); }
    public JSONArray getTot() { return fitnessDAO.getTot(); }
    public JSONArray getYear(String yearIn) { return fitnessDAO.getYear(yearIn); } 
    
}
