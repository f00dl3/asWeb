/*
by Anthony Stump
Created: 22 Mar 2018
Updated: 25 Mar 2018
 */

package asWebRest.action;

import asWebRest.dao.FitnessDAO;
import java.util.List;

public class UpdateFitnessAction {
    
    private FitnessDAO fitnessDAO;
    public UpdateFitnessAction(FitnessDAO fitnessDAO) { this.fitnessDAO = fitnessDAO; }
    
    public String setCalories(List qParams) { return fitnessDAO.setCalories(qParams); }
    public String setCaloriesSingle(List qParams) { return fitnessDAO.setCaloriesSingle(qParams); }
    public String setCaloriesSingleE(List qParams) { return fitnessDAO.setCaloriesSingleE(qParams); }
    public String setPlanCyc(List qParams) { return fitnessDAO.setPlanCyc(qParams); }
    public String setPlanMark(List qParams) { return fitnessDAO.setPlanMark(qParams); }
    public String setPlanRun(List qParams) { return fitnessDAO.setPlanRun(qParams); }
    public String setUpdateToday(List qParams) { return fitnessDAO.setUpdateToday(qParams); }
    public String setUpdateTodayEm(List qParams) { return fitnessDAO.setUpdateTodayEm(qParams); }
        
}
