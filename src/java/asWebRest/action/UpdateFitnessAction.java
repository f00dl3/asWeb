/*
by Anthony Stump
Created: 22 Mar 2018
Updated: 24 Mar 2018
 */

package asWebRest.action;

import asWebRest.dao.FitnessDAO;
import java.util.List;

public class UpdateFitnessAction {
    
    private FitnessDAO fitnessDAO;
    public UpdateFitnessAction(FitnessDAO fitnessDAO) { this.fitnessDAO = fitnessDAO; }
    
    public String setCalories(List qParams) { return fitnessDAO.setCalories(qParams); }
    public String setRunPlanDoC(List qParams) { return fitnessDAO.setRunPlanDoC(qParams); }
    public String setRunPlanDoM(List qParams) { return fitnessDAO.setRunPlanDoM(qParams); }
    public String setRunPlanDoR(List qParams) { return fitnessDAO.setRunPlanDoR(qParams); }
    public String setUpdateToday(List qParams) { return fitnessDAO.setUpdateToday(qParams); }
    public String setUpdateTodayEm(List qParams) { return fitnessDAO.setUpdateTodayEm(qParams); }
        
}
