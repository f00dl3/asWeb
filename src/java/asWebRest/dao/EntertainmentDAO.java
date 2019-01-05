/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 5 Jan 2019
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class EntertainmentDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
       
    private JSONArray chicagoSeries(Connection dbc) {
        final String query_MediaServer_ChicagoSeries = "SELECT" +
                " OverallNo, Season, SeasonNo, ProdCode, Title, AirDate, MillionViews, Synopsis" +
                " FROM Core.ChicagoSeries" +
                " ORDER BY AirDate DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_ChicagoSeries, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("OverallNo", resultSet.getString("OverallNo"))
                    .put("Season", resultSet.getInt("Season"))
                    .put("SeasonNo", resultSet.getInt("SeasonNo"))
                    .put("ProdCode", resultSet.getString("ProdCode"))
                    .put("Title", resultSet.getString("Title"))
                    .put("AirDate", resultSet.getString("AirDate"))
                    .put("MillionViews", resultSet.getDouble("MillionViews"))
                    .put("Synopsis", resultSet.getString("Synopsis"));                   
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray ffxivAssets(Connection dbc) {
        final String query_FfxivAssets = "SELECT What, Value, Purchased FROM Core.FFXIV_Assets;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivAssets, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("What", resultSet.getString("What"))
                    .put("Value", resultSet.getInt("Value"))
                    .put("Purchased", resultSet.getString("Purchased"));                    
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray ffxivCounts(Connection dbc) {
        final String query_ffxivCounts = "SELECT" +
                " COUNT(Name) AS Quests," +
                " (SELECT COUNT(Recipie) FROM Core.FFXIV_Crafting) AS Crafting," +
                " (SELECT Count(Name) FROM Core.FFXIV_Dungeons) AS Dungeons," +
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
                    .put("Quests", resultSet.getInt("Quests"))
                    .put("Weapons", resultSet.getInt("Weapons"))
                    .put("Crafting", resultSet.getInt("Crafting"))
                    .put("Wearables", resultSet.getInt("Wearables"))
                    .put("Hunting", resultSet.getInt("Hunting"))
                    .put("Gathering", resultSet.getInt("Gathering"))
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
    
    private String ffxivGatheringDone(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_FFXIV_GatherDone = "UPDATE Core.FFXIV_GatherNodes SET Completed=1, OrigCompDate=CURDATE() WHERE NodeCode=?;";
        System.out.println(query_FFXIV_GatherDone + "\n" + qParams.get(0));
        try { returnData = wc.q2do1c(dbc, query_FFXIV_GatherDone, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
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
                " NULL AS MateriaSlots, NULL AS Stats" +
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
                " NULL AS MateriaSlots, NULL AS Stats" +
                " FROM Core.FFXIV_Crafting" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT Level as MinLevel, Name, NULL AS CoordX, NULL AS CoordY, NULL AS Zone, NULL AS Exp, NULL AS Gil," +
                " Classes, NULL AS QuestOrder, NULL AS OrigCompDate, NULL AS Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, NULL AS Event, NULL AS Type, 'Weapon' as MasterType, NULL AS qcDesc," + 
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, ILEV," +
                " Category, DamageType, Damage, Delay, AutoAttack, NULL AS Defence, NULL AS MagicDefense, MateriaSlots, Stats" +
                " FROM Core.FFXIV_Items_Weapons" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT Level as MinLevel, Name, NULL AS CoordX, NULL AS CoordY, NULL AS Zone, NULL AS Exp, NULL AS Gil," +
                " Classes, NULL AS QuestOrder, NULL AS OrigCompDate, NULL AS Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, NULL AS Event, NULL AS Type, 'Wearable' as MasterType, NULL AS qcDesc," + 
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, ILEV," +
                " Slot AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, Defence, MagicDefense, MateriaSlots, Stats" +
                " FROM Core.FFXIV_Items_Wearable" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT Level AS MinLevel, CONCAT(SUBSTRING(HuntCode, 1, 3), ': ', Enemy, ' x', Quantity) as Name, CoordX, CoordY, Zone, Exp, NULL AS Gil," +
                " Class as Classes, HuntCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " Seals, NULL AS Version, NULL AS Event, NULL AS Type, 'Hunt' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, NULL AS Stats" +
                " FROM Core.FFXIV_Hunting" +
                " WHERE Level BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT MinLevel, CONCAT(Name, ' ', MinLevel) AS Name, CoordX, CoordY, Zone, NULL AS Exp, NULL AS Gil," +
                " Class as Classes, NodeCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " NULL AS Seals, NULL AS Version, NULL AS Event, NULL AS Type, 'Gathering' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxQuality, NULL AS Difficulty, NULL AS ILEV," +
                " NULL AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, CONCAT(Yield, ' ', YieldBonus) AS Stats" +
                " FROM Core.FFXIV_GatherNodes" +
                " WHERE MinLevel BETWEEN " + minRange + " AND " + maxRange +
                " UNION ALL" +
                " SELECT MinLevel, Name, NULL AS CoordX, NULL AS CoordY, Roulette AS Zone, NULL AS Exp, NULL AS Gil," +
                " NULL AS Classes, DungeonCode AS QuestOrder, OrigCompDate, Completed, NULL AS GivingNPC," +
                " NULL AS Seals, Version, UnlockQuest AS Event, NULL AS Type, 'Dungeon' AS MasterType, NULL AS qcDesc," +
                " NULL AS Crystals, NULL AS Materials, NULL AS Durability, NULL AS MaxDurability, NULL AS Difficulty, MinItemLevel AS ILEV," +
                " PartySize AS Category, NULL AS DamageType, NULL AS Damage, NULL AS Delay, NULL AS AutoAttack, NULL AS Defence, NULL AS MagicDefense," +
                " NULL AS MateriaSlots, CONCAT('Poetics: ', TomesPoetics, ' Creation: ', TomesCreation, ' Mendacity: ', TomesMendacity, ' Genesis: ', TomesGenesis) AS Stats" +
                " FROM Core.FFXIV_Dungeons" +
                " WHERE MinLevel BETWEEN " + minRange + " AND " + maxRange +
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
                    .put("Stats", resultSet.getString("Stats"));;
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
        
    private JSONArray ffxivQuestsByDate(Connection dbc) {
        final String query_FfxivQuestByDate = "SELECT" +
		" 	OrigCompDate," +
		" 	SUM(Quests) AS Quests," +
		" 	SUM(Hunting) AS Hunting," +
		" 	SUM(Crafting) AS Crafting," +
                "       SUM(Dungeons) AS Dungeons" +
		" FROM (" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		COUNT(QuestOrder) AS Quests," +
		" 		0 AS Hunting," +
		" 		0 AS Crafting," +
                "               0 AS Dungeons" +
		" 		FROM Core.FFXIV_Quests" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Quests," +
		" 		COUNT(HuntCode) AS Hunting," +
		" 		0 AS Crafting," +
                "               0 AS Dungeons" +
		" 		FROM Core.FFXIV_Hunting" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Quests," +
		" 		0 as Hunting," +
                "               0 AS Dungeons," +
		" 		COUNT(Recipie) AS Crafting" +
		" 		FROM Core.FFXIV_Crafting" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	UNION ALL" +
		" 	SELECT" +
		" 		OrigCompDate," +
		" 		0 as Quests," +
		" 		0 AS Hunting," +
		" 		COUNT(DungeonCode) AS Dungeons," +
		" 		0 AS Crafting" +
		" 		FROM Core.FFXIV_Dungeons" +
		" 		WHERE OrigCompDate IS NOT NULL" +
		" 		GROUP BY OrigCompDate" +
		" 	) AS tmp" +
		" GROUP BY OrigCompDate" +
		" ORDER BY OrigCompDate;";
        JSONArray tContainer = new JSONArray();
        System.out.println(query_FfxivQuestByDate);
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FfxivQuestByDate, null);
            while (resultSet.next()) {
	/* 	System.out.println(resultSet.toString()); */
                JSONObject tObject = new JSONObject();
                tObject
                    .put("OrigCompDate", resultSet.getString("OrigCompDate"))
                    .put("Quests", resultSet.getInt("Quests"))
                    .put("Hunting", resultSet.getInt("Hunting"))
                    .put("Crafting", resultSet.getInt("Crafting"))
                    .put("Dungeons", resultSet.getInt("Dungeons"));
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
    
    private String playedGameHours(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        final String query_UpdateGameHours = "UPDATE Core.GameHours SET Hours=Hours+(?/60), Last=CURDATE() WHERE Name=?;";
        final String query_UpdateGameHoursPart2 = "UPDATE Core.Fitness SET HoursGaming=HoursGaming+(?/60) WHERE Date=CURDATE();";
        try { returnData = wc.q2do1c(dbc, query_UpdateGameHours, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { returnData = wc.q2do1c(dbc, query_UpdateGameHoursPart2, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray trueBlood(Connection dbc) {
        final String query_MediaServer_TrueBlood = "SELECT" +
                " OverallNo, Season, SeasonNo," +
                " Director, Producer, Title, AirDate, AirDateDTS, Viewers," +
                " Synopsis" +
                " FROM Core.TrueBlood" +
                " ORDER BY OverallNo DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_TrueBlood, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("OverallNo", resultSet.getInt("OverallNo"))
                    .put("Season", resultSet.getInt("Season"))
                    .put("SeasonNo", resultSet.getInt("SeasonNo"))
                    .put("Director", resultSet.getString("Director"))
                    .put("Producer", resultSet.getString("Producer"))
                    .put("Title", resultSet.getString("Title"))
                    .put("AirDate", resultSet.getString("AirDate"))
                    .put("AirDateDTS", resultSet.getString("AirDateDTS"))
                    .put("Viewers", resultSet.getDouble("Viewers"))
                    .put("Synopsis", resultSet.getString("Synopsis"));                   
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChicagoSeries(Connection dbc) { return chicagoSeries(dbc); }    
    public JSONArray getFfxivAssets(Connection dbc) { return ffxivAssets(dbc); }
    public JSONArray getFfxivCounts(Connection dbc) { return ffxivCounts(dbc); }
    public JSONArray getFfxivCrafting(Connection dbc) { return ffxivCrafting(dbc); }
    public JSONArray getFfxivDungeons(Connection dbc) { return ffxivDungeons(dbc); }
    public JSONArray getFfxivEmotes(Connection dbc) { return ffxivEmotes(dbc); }
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
    
    public JSONArray getGameHours(Connection dbc) {
        final String query_GameHours = "SELECT Name, CAST(Hours AS DECIMAL(5,0)) AS Hours, Last, Active FROM Core.GameHours ORDER BY Hours DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GameHours, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Name", resultSet.getString("Name"))
                    .put("Hours", resultSet.getDouble("Hours"))
                    .put("Last", resultSet.getString("Last"))
                    .put("Active", resultSet.getInt("Active"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
   
    public JSONArray getGameHoursLatest(Connection dbc) {
        final String query_GameHoursLatest = "SELECT Name, CAST(Hours AS DECIMAL(5,0)) AS Hours, Last, Active FROM Core.GameHours ORDER BY Last DESC LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GameHoursLatest, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Name", resultSet.getString("Name"))
                    .put("Hours", resultSet.getDouble("Hours"))
                    .put("Last", resultSet.getString("Last"))
                    .put("Active", resultSet.getInt("Active"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getGameHoursTotal(Connection dbc) {
        final String query_GameHoursTotal = "SELECT SUM(Hours) AS TotalHours FROM Core.GameHours;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GameHoursTotal, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("TotalHours", resultSet.getDouble("TotalHours"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getGameIndex(Connection dbc) {
        final String query_GameIndex = "SELECT GameName, FoF, SizeG, LinuxTested, Volume," +
                "BurnDate, LastUpdate, PendingBurn FROM Core.GamesIndex ORDER BY GameName ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GameIndex, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("GameName", resultSet.getString("GameName"))
                    .put("FoF", resultSet.getString("FoF"))
                    .put("SizeG", resultSet.getDouble("SizeG"))
                    .put("LinuxTested", resultSet.getString("LinuxTested"))
                    .put("Volume", resultSet.getString("Volume"))
                    .put("BurnDate", resultSet.getString("BurnDate"))
                    .put("LastUpdate", resultSet.getString("LastUpdate"))
                    .put("PendingBurn", resultSet.getInt("PendingBurn"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
  
    public JSONArray getGoosebumpsBooks(Connection dbc) {
        final String query_GoosebumpsBooks = "SELECT Code, Title, PublishDate, Pages, ISBN, Plot, CoverImageType, pdf" +
                " FROM Core.GoosebumpsBooks ORDER BY PublishDate ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GoosebumpsBooks, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Code", resultSet.getString("Code"))
                    .put("Title", resultSet.getString("Title"))
                    .put("PublishDate", resultSet.getString("PublishDate"))
                    .put("Pages", resultSet.getInt("Pages"))
                    .put("ISBN", resultSet.getString("ISBN"))
                    .put("Plot", resultSet.getString("Plot"))
                    .put("CoverImageType", resultSet.getString("CoverImageType"))
                    .put("pdf", resultSet.getInt("pdf"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }  
    
    public JSONArray getLego(Connection dbc) {
        final String query_Lego = "SELECT SetID, Number, Variant, Theme, Subtheme, Year, Name, Minifigs," +
                "Pieces, USPrice, ImageURL, ImgDown FROM Core.Lego ORDER BY SetID;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Lego, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("SetID", resultSet.getInt("SetID"))
                    .put("Number", resultSet.getString("Number"))
                    .put("Variant", resultSet.getString("Variant"))
                    .put("Theme", resultSet.getString("Theme"))
                    .put("Subtheme", resultSet.getString("Subtheme"))
                    .put("Year", resultSet.getInt("Year"))
                    .put("Name", resultSet.getString("Name"))
                    .put("Minifigs", resultSet.getInt("Minifigs"))
                    .put("Pieces", resultSet.getInt("Pieces"))
                    .put("USPrice", resultSet.getDouble("USPrice"))
                    .put("ImageURL", resultSet.getString("ImageURL"))
                    .put("ImgDown", resultSet.getInt("ImgDown"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }  
       
    public JSONArray getPowerRangers(Connection dbc) {
        final String query_MediaServer_PowerRangers = "SELECT" +
                " Series, Season, SeasonEp, SeriesEp, ProdCode, AirDate," +
                " Title, Synopsis, MediaServer" +
                " FROM Core.PowerRangers ORDER BY SeriesEp DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_PowerRangers, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Series", resultSet.getInt("Series"))
                    .put("Season", resultSet.getInt("Season"))
                    .put("SeasonEp", resultSet.getInt("SeasonEp"))
                    .put("SeriesEp", resultSet.getInt("SeriesEp"))
                    .put("ProdCode", resultSet.getString("ProdCode"))
                    .put("AirDate", resultSet.getString("AirDate"))
                    .put("Title", resultSet.getString("Title"))
                    .put("Synopsis", resultSet.getString("Synopsis"))
                    .put("MediaServer", resultSet.getInt("MediaServer"));                       
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }  
    
    public JSONArray getStarTrek(Connection dbc) {
        final String query_StarTrek = "SELECT" +
                " Series, Season, SeasonEp, SeriesEp, ProdCode, AirDate," +
                " Title, Synopsis, MediaServer, StarDate, ViewersM" +
                " FROM Core.StarTrek ORDER BY StarDate DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_StarTrek, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Series", resultSet.getInt("Series"))
                    .put("Season", resultSet.getInt("Season"))
                    .put("SeasonEp", resultSet.getInt("SeasonEp"))
                    .put("ProdCode", resultSet.getString("ProdCode"))
                    .put("AirDate", resultSet.getString("AirDate"))
                    .put("Title", resultSet.getString("Title"))
                    .put("Synopsis", resultSet.getString("Synopsis"))
                    .put("MediaServer", resultSet.getInt("MediaServer"))
                    .put("StarDate", resultSet.getDouble("StarDate"))
                    .put("ViewersM", resultSet.getDouble("ViewersM"));                       
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getTrueBlood(Connection dbc) { return trueBlood(dbc); }
    
    public JSONArray getXFiles(Connection dbc) {
        final String query_MediaServer_XFiles = "SELECT" +
                " Path, Size, DurSec," +
                " File, Description, AlbumArt," +
                " LastSelected, PlayCount," +
                " Burned, BDate, Media" +
                " FROM Core.MediaServer" +
                " WHERE Description LIKE '%XFilesTV%'" +
                " ORDER BY ContentDate DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_MediaServer_XFiles, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Path", resultSet.getString("Path"))
                    .put("Size", resultSet.getInt("Size"))
                    .put("DurSec", resultSet.getInt("DurSec"))
                    .put("File", resultSet.getString("File"))
                    .put("Description", resultSet.getString("Description"))
                    .put("AlbumArt", resultSet.getString("AlbumArt"))
                    .put("LastSelected", resultSet.getString("LastSelected"))
                    .put("PlayCount", resultSet.getInt("PlayCount"))
                    .put("Burned", resultSet.getString("Burned"))
                    .put("BDate", resultSet.getString("BDate"))
                    .put("Media", resultSet.getString("Media"));                       
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getXTags(Connection dbc) {
        final String query_XTags = "SELECT Tag, Description, Detail, TagVer FROM Core.AO_Tags;";
        JSONArray xTags = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_XTags, null);
            while (resultSet.next()) {
                JSONObject tXTag = new JSONObject();
                tXTag
                    .put("Tag", resultSet.getString("Tag"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Detail", resultSet.getString("Detail"))
                    .put("TagVer", resultSet.getInt("TagVer"));
                xTags.put(tXTag);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return xTags;
    }
 
    public String setFfxivQuestDone(Connection dbc, List<String> qParams) { return ffxivQuestDone(dbc, qParams); }
    public String setFfxivCraftingDone(Connection dbc, List<String> qParams) { return ffxivCraftingDone(dbc, qParams); }
    public String setFfxivDungeonDone(Connection dbc, List<String> qParams) { return ffxivDungeonDone(dbc, qParams); }
    public String setFfxivGatheringDone(Connection dbc, List<String> qParams) { return ffxivGatheringDone(dbc, qParams); }
    public String setFfxivHuntingDone(Connection dbc, List<String> qParams) { return ffxivHuntingDone(dbc, qParams); }
    public String setPlayedGameHours(Connection dbc, List<String> qParams) { return playedGameHours(dbc, qParams); }
    
}