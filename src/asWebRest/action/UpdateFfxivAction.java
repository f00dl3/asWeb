/*
by Anthony Stump
Created: 22 May 2018
Split from UpdateEntertainmentAction.java on 14 Nov 2019
Updated: 16 Nov 2019
 */

package asWebRest.action;

import asWebRest.dao.FfxivDAO;
import java.sql.Connection;
import java.util.List;

public class UpdateFfxivAction {
    
    final private FfxivDAO ffxivDAO;
    public UpdateFfxivAction(FfxivDAO ffxivDAO) { this.ffxivDAO = ffxivDAO; }

    public String setFfxivAchievementDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivAchievementDone(dbc, qParams); }
    public String setFfxivAssetAdd(Connection dbc, List qParams) { return ffxivDAO.setFfxivAssetAdd(dbc, qParams); }
    public String setFfxivCraftingDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivCraftingDone(dbc, qParams); }
    public String setFfxivDungeonClear(Connection dbc, List qParams) { return ffxivDAO.setFfxivDungeonClear(dbc, qParams); }
    public String setFfxivDungeonDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivDungeonDone(dbc, qParams); }
    public String setFfxivFateDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivFateDone(dbc, qParams); }
    public String setFfxivGatheringDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivGatheringDone(dbc, qParams); }
    public String setFfxivGil(Connection dbc, List qParams) { return ffxivDAO.setFfxivGil(dbc, qParams); }
    public String setFfxivHuntingDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivHuntingDone(dbc, qParams); }
    public String setFfxivLevelsIncrease(Connection dbc, List qParams) { return ffxivDAO.setFfxivLevelsIncrease(dbc, qParams); }
    public String setFfxivQuestDone(Connection dbc, List qParams) { return ffxivDAO.setFfxivQuestDone(dbc, qParams); }

}
