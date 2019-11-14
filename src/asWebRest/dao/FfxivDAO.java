/*
by Anthony Stump
Created: 20 Feb 2018
Split from Entertainment.java: 14 Nov 2019
Updated: 14 Nov 2019
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FfxivDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    private String ffxivAchievementDone(Connection dbc, List<String> qParams) {
        final String param = qParams.get(0);
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_AchievementDone = "UPDATE Core.FFXIV_Achievements SET Completed=1, OrigCompDate=CURDATE() WHERE AchCode=?;";
        System.out.println("Param: " + param + "; Query: " + query_FFXIV_AchievementDone);
        try { returnData = wc.q2do1c(dbc, query_FFXIV_AchievementDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray ffxivAssets(Connection dbc) {
        final String query_FfxivAssets = "SELECT What, Value, Purchased, WriteOff FROM Core.FFXIV_Assets" +
                " UNION ALL" +
                " (SELECT 'Gil' AS What, Gil AS Value, AsOf AS Purchased, 0 AS WriteOff FROM Core.FFXIV_Gil ORDER BY Purchased DESC LIMIT 1);";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivAssets, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("What", resultSet.getString("What"))
                    .put("Value", resultSet.getInt("Value"))
                    .put("Purchased", resultSet.getString("Purchased"))
                    .put("WriteOff", resultSet.getInt("WriteOff"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray ffxivCounts(Connection dbc) {
        final String query_ffxivCounts = "SELECT" +
                " COUNT(Name) AS Quests," +
                " (SELECT COUNT(AchCode) FROM Core.FFXIV_Achievements) AS Achievements," +
                " (SELECT COUNT(Recipie) FROM Core.FFXIV_Crafting) AS Crafting," +
                " (SELECT Count(Name) FROM Core.FFXIV_Dungeons) AS Dungeons," +
                " (SELECT Count(Name) FROM Core.FFXIV_FATEs) AS FATEs," +
                " (SELECT COUNT(Name) FROM Core.FFXIV_Items_Weapons) AS Weapons," +
                " (SELECT COUNT(Name) FROM Core.FFXIV_Items_Wearable) AS Wearables," +
                " (SELECT Count(HuntCode) FROM Core.FFXIV_Hunting) AS Hunting," +
                " (SELECT Count(NodeCode) FROM Core.FFXIV_GatherNodes) AS Gathering" +
                " FROM Core.FFXIV_Quests;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ffxivCounts, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Achievements", resultSet.getInt("Achievements"))
                    .put("Quests", resultSet.getInt("Quests"))
                    .put("Weapons", resultSet.getInt("Weapons"))
                    .put("Crafting", resultSet.getInt("Crafting"))
                    .put("Wearables", resultSet.getInt("Wearables"))
                    .put("Hunting", resultSet.getInt("Hunting"))
                    .put("Gathering", resultSet.getInt("Gathering"))
                    .put("FATEs", resultSet.getInt("FATEs"))
                    .put("Dungeons", resultSet.getInt("Dungeons"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray ffxivCrafting(Connection dbc) {
        final String query_ffxivCrafting = "SELECT Recipie, Level, Crafted, Difficulty," +
                " Durability, MaxQuality, Crystals, Materials, Class, Completed, OrigCompDate" +
                " FROM Core.FFXIV_Crafting" +
                " ORDER BY Level ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ffxivCrafting, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Recipie", resultSet.getString("Recipie"))
                    .put("Level", resultSet.getInt("Level"))
                    .put("Crafted", resultSet.getInt("Crafted"))
                    .put("Difficulty", resultSet.getInt("Difficulty"))
                    .put("Durability", resultSet.getInt("Durability"))
                    .put("MaxQuality", resultSet.getInt("MaxQuality"))
                    .put("Crystals", resultSet.getString("Crystals"))
                    .put("Materials", resultSet.getString("Materials"))
                    .put("Class", resultSet.getString("Class"))
                    .put("Completed", resultSet.getInt("Completed"))
                    .put("OrigCompDate", resultSet.getString("OrigCompDate"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private String ffxivCraftingDone(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_CraftingDone = "UPDATE Core.FFXIV_Crafting SET Completed=1, OrigCompDate=CURDATE() WHERE Recipie=?;";
        try { returnData = wc.q2do1c(dbc, query_FFXIV_CraftingDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private String ffxivDungeonDone(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_DungeonDone = "UPDATE Core.FFXIV_Dungeons SET Completed=1, OrigCompDate=CURDATE() WHERE DungeonCode=?;";
        try { returnData = wc.q2do1c(dbc, query_FFXIV_DungeonDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray ffxivDungeons(Connection dbc) {
        final String query_FfxivDungeons = "SELECT d.Name, d.MinLevel, d.MinItemLevel, d.MaxItemLevel," +
                " d.Roulette, d.TomesPoetics, d.TomesCreation, d.TomesMendacity, d.UnlockQuest, d.PartySize," +
                " q.OrigCompDate, q.Completed, d.TomesGenesis" +
                " FROM Core.FFXIV_Dungeons d" +
                " LEFT JOIN Core.FFXIV_Quests q ON d.UnlockQuest = q.Name" +
                " ORDER BY MinLevel ASC;"; // join in Quests to see if unlocked!
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivDungeons, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Name", resultSet.getString("Name"))
                    .put("MinLevel", resultSet.getInt("MinLevel"))
                    .put("MinItemLevel", resultSet.getInt("MinItemLevel"))
                    .put("MaxItemLevel", resultSet.getInt("MaxItemLevel"))
                    .put("Roulette", resultSet.getString("Roulette"))
                    .put("TomesPoetics", resultSet.getInt("TomesPoetics"))
                    .put("TomesCreation", resultSet.getInt("TomesCreation"))
                    .put("TomesMendacity", resultSet.getInt("TomesMendacity"))
                    .put("TomesGenesis", resultSet.getInt("TomesGenesis"))
                    .put("UnlockQuest", resultSet.getString("UnlockQuest"))
                    .put("PartySize", resultSet.getInt("PartySize"))
                    .put("OrigCompDate", resultSet.getString("OrigCompDate"))
                    .put("Completed", resultSet.getInt("Completed"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray ffxivEmotes(Connection dbc) {
        final String query_FfxivEmotes = "SELECT Command, AquiredBy FROM Core.FFXIV_Emotes;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivEmotes, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Command", resultSet.getString("Command"))
                    .put("AcquiredBy", resultSet.getString("AquiredBy"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
	private String ffxivFateDone(Connection dbc, List<String> qParams) {
	    String returnData = wcb.getDefaultNotRanYet();
	    final String query_FFXIV_FATEDone = "UPDATE Core.FFXIV_FATEs SET Completed=1, OrigCompDate=CURDATE() WHERE FateCode=?;";
	    try { returnData = wc.q2do1c(dbc, query_FFXIV_FATEDone, qParams); } catch (Exception e) { e.printStackTrace(); }
	    return returnData;
	}
        
    private String ffxivGatheringDone(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_GatherDone = "UPDATE Core.FFXIV_GatherNodes SET Completed=1, OrigCompDate=CURDATE() WHERE NodeCode=?;";
        System.out.println(query_FFXIV_GatherDone + "\n" + qParams.get(0));
        try { returnData = wc.q2do1c(dbc, query_FFXIV_GatherDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    
	private String ffxivGil(Connection dbc, List<String> qParams) {
	    String returnData = wcb.getDefaultNotRanYet();
	    final String query_FFXIV_Gil = "INSERT INTO Core.FFXIV_Gil (Gil) VALUES (?);";
	    try { returnData = wc.q2do1c(dbc, query_FFXIV_Gil, qParams); } catch (Exception e) { e.printStackTrace(); }
	    return returnData;
	}
        
    private JSONArray ffxivGilByDate(Connection dbc) {
        final String query_FFXIVGilByDate = "SELECT SUBSTRING(Date,1,10) AS Date, Gil AS Worth, " +
                " (SELECT Gil FROM FFXIV_Gil WHERE AsOf < Date ORDER BY AsOf DESC LIMIT 1) AS Gil" +
                " FROM Core.FFXIV_GilByDate GROUP BY SUBSTRING(Date, 1, 10);";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FFXIVGilByDate, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Worth", resultSet.getLong("Worth"))
                    .put("Gil", resultSet.getLong("Gil"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private String ffxivHuntingDone(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_HuntingDone = "UPDATE Core.FFXIV_Hunting SET Completed=1, OrigCompDate=CURDATE() WHERE HuntCode=?;";
        try { returnData = wc.q2do1c(dbc, query_FFXIV_HuntingDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    private JSONArray ffxivImageMaps(Connection dbc) {
        final String query_FfxivImageMaps = "SELECT File, Description FROM Core.FFXIV_ImageMaps;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivImageMaps, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("File", resultSet.getString("File"))
                    .put("Description", resultSet.getString("Description"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    } 
   
    private JSONArray ffxivItems(Connection dbc) {
        final String query_FfxivItems = "SELECT * FROM (" +
            " SELECT Name, Level, ILEV, Classes, Category, Damage, DamageType, Delay, AutoAttack, NULL AS Defence, NULL AS MagicDefense, MateriaSlots, Stats FROM FFXIV_Items_Weapons" +
            " UNION ALL" +
            " SELECT Name, Level, ILEV, Classes, Slot AS Category, NULL AS Damage, NULL AS DamageType, NULL AS Delay, NULL AS AutoAttack, Defence, MagicDefense, MateriaSlots, Stats FROM FFXIV_Items_Wearable" +
            ") as tmp;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivItems, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Name", resultSet.getString("Name"))
                    .put("Level", resultSet.getInt("Level"))
                    .put("ILEV", resultSet.getInt("ILEV"))
                    .put("Classes", resultSet.getString("Classes"))
                    .put("Category", resultSet.getString("Category"))
                    .put("Damage", resultSet.getDouble("Damage"))
                    .put("DamageType", resultSet.getString("DamageType"))
                    .put("Delay", resultSet.getDouble("Delay"))
                    .put("AutoAttack", resultSet.getDouble("AutoAttack"))
                    .put("Defense", resultSet.getInt("Defence"))
                    .put("MagicDefense", resultSet.getInt("MagicDefense"))
                    .put("MateriaSlots", resultSet.getInt("MateriaSlots"))
                    .put("Stats", resultSet.getString("Stats"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    private JSONArray ffxivMerged(Connection dbc, int minRange, int maxRange, String completed) {
        String query_FFXIV_Merged = "SELECT * FROM (" +
                " SELECT q.MinLevel, q.Name, q.CoordX, q.CoordY, q.Zone, q.Exp, q.Gil," +
                " q.Classes, q.QuestOrder, q.OrigCompDate, q.Completed, q.GivingNPC, q.Seals, q.Version, q.Event, q.Type," +
                " 'Quest' as MasterType, c.Description as qcDesc, NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality," +
                " NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Stats, q.Journal" +
                " FROM FFXIV_Quests q" +
        	" LEFT JOIN Core.FFXIV_QuestCodes c ON c.Code = SUBSTRING(q.QuestOrder, 4, 6)" +
                " WHERE MinLevel BETWEEN " + minRange + " AND " + maxRange + " AND Completed LIKE '" + completed + "'";
        if(completed.equals("0")) {
            query_FFXIV_Merged += " AND QuestOrder NOT LIKE '%EV' AND (Classes IS NULL OR Classes = '')"; 
        }
        query_FFXIV_Merged += " UNION ALL" +
                " SELECT Level as MinLevel, Recipie as Name, NULL AS CoordX, NULL AS CoordY, NULL AS Zone, NULL AS Exp, NULL AS Gil," +
                " Class as Classes, NULL AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, NULL AS Event, NULL AS Type, 'Crafting' as MasterType, NULL AS qcDesc," +
                " Crystals, Materials, Durability, MaxQuality, Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Stats, NULL AS Journal" +
                " FROM Core.FFXIV_Crafting" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT Level as MinLevel, Name, NULL AS CoordX, NULL AS CoordY, NULL AS Zone, NULL AS Exp, NULL AS Gil," +
                " Classes, NULL AS QuestOrder, NULL AS OrigCompDate, NULL AS Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, NULL AS Event, NULL AS Type, 'Weapon' as MasterType, NULL AS qcDesc," + 
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, ILEV," +
                " Category, DamageType, Damage, Delay, AutoAttack, NULL AS Defence, NULL AS MagicDefense, MateriaSlots, Stats," +
                " NULL AS Journal" +
                " FROM Core.FFXIV_Items_Weapons" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT Level as MinLevel, Name, NULL AS CoordX, NULL AS CoordY, NULL AS Zone, NULL AS Exp, NULL AS Gil," +
                " Classes, NULL AS QuestOrder, NULL AS OrigCompDate, NULL AS Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, NULL AS Event, NULL AS Type, 'Wearable' as MasterType, NULL AS qcDesc," + 
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, ILEV," +
                " Slot AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, Defence, MagicDefense," +
                " MateriaSlots, Stats, NULL AS Journal" +
                " FROM Core.FFXIV_Items_Wearable" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT Level AS MinLevel, CONCAT(SUBSTRING(HuntCode, 1, 3), ': ', Enemy, ' x', Quantity) as Name, CoordX, CoordY, Zone, Exp, NULL AS Gil," +
                " Class as Classes, HuntCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " Seals, NULL AS Version, NULL AS Event, NULL AS Type, 'Hunt' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Stats, NULL AS Journal" +
                " FROM Core.FFXIV_Hunting" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT MinLevel, CONCAT(Name, ' ', MinLevel) AS Name, CoordX, CoordY, Zone, NULL AS Exp, NULL AS Gil," +
                " Class as Classes, NodeCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " NULL AS Seals, NULL AS Version, NULL AS Event, NULL AS Type, 'Gathering' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, CONCAT(Yield, ' ', YieldBonus) AS Stats, NULL AS Journal" +
                " FROM Core.FFXIV_GatherNodes" +
                " WHERE MinLevel BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT MinLevel, Name, NULL AS CoordX, NULL AS CoordY, Roulette AS Zone, NULL AS Exp, NULL AS Gil," +
                " NULL AS Classes, DungeonCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, UnlockQuest AS Event, NULL AS Type, 'Dungeon' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxDurability, NULL AS Difficulty, MinItemLevel AS ILEV," +
                " PartySize AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Journal," +
                " CONCAT('Clears: ', Clears, ' Poetics: ', TomesPoetics, ' Creation: ', TomesCreation, ' Mendacity: ', TomesMendacity, ' Genesis: ', TomesGenesis) AS Stats" +
                " FROM Core.FFXIV_Dungeons" +
                " WHERE MinLevel BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT '0' AS MinLevel, Title AS Name, NULL AS CoordX, NULL AS CoordY, 'Achievement' AS Zone, NULL AS Exp, NULL AS Gil," +
                " NULL AS Classes, AchCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, NULL AS Event, NULL AS Type, 'Achievement' AS MasterType, Description AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxDurability, NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Stats, NULL AS Journal" +
                " FROM Core.FFXIV_Achievements" +
                " UNION ALL" +
                " SELECT MinLevel, Name, CoordX, CoordY, Zone, XP AS Exp, Gil," +
                " NULL AS Classes, FateCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " Seals, NULL as Version, NULL AS Event, NULL AS Type, 'FATE' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxDurability, NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Stats, NULL AS Journal" +
                " FROM Core.FFXIV_FATEs" +
                " ) as tmp" +
                " ORDER BY MinLevel, QuestOrder";
        //System.out.println(query_FFXIV_Merged);
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FFXIV_Merged, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Name", resultSet.getString("Name"))
                    .put("MinLevel", resultSet.getInt("MinLevel"))
                    .put("CoordX", resultSet.getInt("CoordX"))
                    .put("CoordY", resultSet.getInt("CoordY"))
                    .put("Zone", resultSet.getString("Zone"))
                    .put("Exp", resultSet.getInt("Exp"))
                    .put("Gil", resultSet.getInt("Gil"))
                    .put("Classes", resultSet.getString("Classes"))
                    .put("QuestOrder", resultSet.getString("QuestOrder"))
                    .put("OrigCompDate", resultSet.getString("OrigCompDate"))
                    .put("Completed", resultSet.getInt("Completed"))
                    .put("GivingNPC", resultSet.getString("GivingNPC"))
                    .put("Seals", resultSet.getInt("Seals"))
                    .put("Version", resultSet.getDouble("Version"))
                    .put("Event", resultSet.getString("Event"))
                    .put("Type", resultSet.getString("Type"))
                    .put("qcDesc", resultSet.getString("qcDesc"))
                    .put("MasterType", resultSet.getString("MasterType"))
                    .put("Crystals", resultSet.getString("Crystals"))
                    .put("Materials", resultSet.getString("Materials"))
                    .put("MaxQuality", resultSet.getInt("MaxQuality"))
                    .put("Durability", resultSet.getInt("Durability"))
                    .put("Difficulty", resultSet.getInt("Difficulty"))
                    .put("ILEV", resultSet.getInt("ILEV"))
                    .put("Category", resultSet.getString("Category"))
                    .put("Damage", resultSet.getDouble("Damage"))
                    .put("DamageType", resultSet.getString("DamageType"))
                    .put("Delay", resultSet.getDouble("Delay"))
                    .put("AutoAttack", resultSet.getDouble("AutoAttack"))
                    .put("Defense", resultSet.getInt("Defence"))
                    .put("MagicDefense", resultSet.getInt("MagicDefense"))
                    .put("MateriaSlots", resultSet.getInt("MateriaSlots"))
                    .put("Journal", resultSet.getString("Journal"))
                    .put("Stats", resultSet.getString("Stats"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    private JSONArray ffxivQuestsByDate(Connection dbc) {
        final String query_FfxivQuestByDate = "SELECT" +
		" 	OrigCompDate," +
		" 	SUM(Achievements) AS Achievements," +
		" 	SUM(Crafting) AS Crafting," +
		"   SUM(Dungeons) AS Dungeons," +
		" 	SUM(Gathering) AS Gathering," +
		" 	SUM(Hunting) AS Hunting," +
		"	SUM(FATEs) AS FATEs," +
		" 	SUM(Quests) AS Quests" +
		" FROM (" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Achievements," +
		" 		0 AS Crafting," +
        "       0 AS Dungeons," +
		" 		0 AS Gathering," +
		" 		0 AS FATEs," +
		" 		0 AS Hunting," +
		" 		COUNT(QuestOrder) AS Quests" +
		" 		FROM Core.FFXIV_Quests" +
		" 		WHERE OrigCompDate IS NOT NULL" +
        "     	AND QuestOrder NOT LIKE '%EV'" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Achievements," +
		" 		0 AS Crafting," +
        "       0 AS Dungeons," +
		" 		0 AS Gathering," +
		" 		COUNT(HuntCode) AS Hunting," +
		" 		0 AS FATEs," +
		" 		0 as Quests" +
		" 		FROM Core.FFXIV_Hunting" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Achievements," +
		" 		COUNT(Recipie) AS Crafting," +
                "               0 AS Dungeons," +
		" 		0 AS Gathering," +
		" 		0 as Hunting," +
		" 		0 AS FATEs," +
		" 		0 as Quests" +
		" 		FROM Core.FFXIV_Crafting" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Achievements," +
		" 		0 AS Crafting," +
		" 		COUNT(DungeonCode) AS Dungeons," +
		" 		0 AS Gathering," +
		" 		0 AS Hunting," +
		" 		0 AS FATEs," +
		" 		0 as Quests" +
		" 		FROM Core.FFXIV_Dungeons" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Achievements," +
		" 		0 AS Crafting," +
		" 		0 AS Dungeons," +
		" 		COUNT(NodeCode) AS Gathering," +
		" 		0 AS Hunting," +
		" 		0 AS FATEs," +
		" 		0 as Quests" +
		" 		FROM Core.FFXIV_GatherNodes" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		COUNT(AchCode) as Achievements," +
		" 		0 AS Crafting," +
		" 		0 AS Dungeons," +
		" 		0 AS Gathering," +
		" 		0 AS Hunting," +
		" 		0 AS FATEs," +
		" 		0 as Quests" +
		" 		FROM Core.FFXIV_Achievements" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		COUNT(FateCode) as FATEs," +
		" 		0 AS Crafting," +
		" 		0 AS Dungeons," +
		" 		0 AS Gathering," +
		" 		0 AS Hunting," +
		" 		0 AS Achievements," +
		" 		0 as Quests" +
		" 		FROM Core.FFXIV_FATEs" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	) AS tmp" +
		" GROUP BY OrigCompDate" +
		" ORDER BY OrigCompDate" +
		" LIMIT 720;";
        JSONArray tContainer = new JSONArray();
        //System.out.println(query_FfxivQuestByDate);
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivQuestByDate, null);
            while (resultSet.next()) {
	/* 	System.out.println(resultSet.toString()); */
                JSONObject tObject = new JSONObject();
                tObject
                    .put("OrigCompDate", resultSet.getString("OrigCompDate"))
                    .put("Achievements", resultSet.getInt("Achievements"))
                    .put("Quests", resultSet.getInt("Quests"))
                    .put("Hunting", resultSet.getInt("Hunting"))
                    .put("Crafting", resultSet.getInt("Crafting"))
                    .put("Gathering", resultSet.getInt("Gathering"))
                    .put("Dungeons", resultSet.getInt("Dungeons"))
                    .put("FATEs", resultSet.getInt("FATEs"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    private String ffxivQuestDone(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_QuestDone = "UPDATE Core.FFXIV_Quests SET Completed=1, OrigCompDate=CURDATE() WHERE QuestOrder=?;";
        try { returnData = wc.q2do1c(dbc, query_FFXIV_QuestDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public JSONArray getFfxivAssets(Connection dbc) { return ffxivAssets(dbc); }
    public JSONArray getFfxivCounts(Connection dbc) { return ffxivCounts(dbc); }
    public JSONArray getFfxivCrafting(Connection dbc) { return ffxivCrafting(dbc); }
    public JSONArray getFfxivDungeons(Connection dbc) { return ffxivDungeons(dbc); }
    public JSONArray getFfxivEmotes(Connection dbc) { return ffxivEmotes(dbc); }
    public JSONArray getFfxivGilByDate(Connection dbc) { return ffxivGilByDate(dbc); }
    public JSONArray getFfxivImageMaps(Connection dbc) { return ffxivImageMaps(dbc); }
    public JSONArray getFfxivItems(Connection dbc) { return ffxivItems(dbc); }
    public JSONArray getFfxivMerged(Connection dbc,  int minRange, int maxRange, String completed) { return ffxivMerged(dbc, minRange, maxRange, completed); }
    
    public JSONArray getFfxivQuests(Connection dbc, int minRange, int maxRange, String completed) {
        String query_FFXIV_Quests = "SELECT q.MinLevel, q.Name, q.CoordX, q.CoordY, q.Zone, q.Exp, q.Gil," +
                " q.Classes, q.QuestOrder, q.OrigCompDate, q.Completed, q.GivingNPC, q.Seals, q.Version, q.Event, q.Type," +
                " c.Description as qcDesc" +
                " FROM FFXIV_Quests q" +
        	" LEFT JOIN Core.FFXIV_QuestCodes c ON c.Code = SUBSTRING(q.QuestOrder, 4, 6)" +
                " WHERE MinLevel BETWEEN " + minRange + " AND " + maxRange + " AND Completed LIKE '" + completed + "'";
        if(completed.equals("0")) {
            query_FFXIV_Quests += " AND QuestOrder NOT LIKE '%EV' AND (Classes IS NULL OR Classes = '')"; 
        }
        query_FFXIV_Quests += " ORDER BY MinLevel, QuestOrder";
        System.out.println(query_FFXIV_Quests);
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FFXIV_Quests, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Name", resultSet.getString("Name"))
                    .put("MinLevel", resultSet.getInt("MinLevel"))
                    .put("CoordX", resultSet.getInt("CoordX"))
                    .put("CoordY", resultSet.getInt("CoordY"))
                    .put("Zone", resultSet.getString("Zone"))
                    .put("Exp", resultSet.getInt("Exp"))
                    .put("Gil", resultSet.getInt("Gil"))
                    .put("Classes", resultSet.getString("Classes"))
                    .put("QuestOrder", resultSet.getString("QuestOrder"))
                    .put("OrigCompDate", resultSet.getString("OrigCompDate"))
                    .put("Completed", resultSet.getInt("Completed"))
                    .put("GivingNPC", resultSet.getString("GivingNPC"))
                    .put("Seals", resultSet.getInt("Seals"))
                    .put("Version", resultSet.getDouble("Version"))
                    .put("Event", resultSet.getString("Event"))
                    .put("Type", resultSet.getString("Type"))
                    .put("qcDesc", resultSet.getString("qcDesc"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    public JSONArray getFfxivQuestsByDate(Connection dbc) { return ffxivQuestsByDate(dbc); }
    
    public String setFfxivAchievementDone(Connection dbc, List<String> qParams) { return ffxivAchievementDone(dbc, qParams); }
    public String setFfxivQuestDone(Connection dbc, List<String> qParams) { return ffxivQuestDone(dbc, qParams); }
    public String setFfxivCraftingDone(Connection dbc, List<String> qParams) { return ffxivCraftingDone(dbc, qParams); }
    public String setFfxivDungeonDone(Connection dbc, List<String> qParams) { return ffxivDungeonDone(dbc, qParams); }
    public String setFfxivFateDone(Connection dbc, List<String> qParams) { return ffxivFateDone(dbc, qParams); }
    public String setFfxivGatheringDone(Connection dbc, List<String> qParams) { return ffxivGatheringDone(dbc, qParams); }
    public String setFfxivGil(Connection dbc, List<String> qParams) { return ffxivGil(dbc, qParams); }
    public String setFfxivHuntingDone(Connection dbc, List<String> qParams) { return ffxivHuntingDone(dbc, qParams); }
   
}