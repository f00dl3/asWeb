/*
by Anthony Stump
Created: 22 Mar 2018
 */

package asWebRest.action;

import asWebRest.dao.FitnessDAO;
import java.util.List;

public class UpdateFitnessAction {
    
    private FitnessDAO fitnessDAO;
    public UpdateFitnessAction(FitnessDAO fitnessDAO) { this.fitnessDAO = fitnessDAO; }
    
    public String setUpdateToday(List qParams) { return fitnessDAO.setUpdateToday(qParams); }
        
}
