/*
by Anthony Stump
Created: 19 Nov 2019
Updated: 20 Nov 2019

 */

package asWebRest.hookers;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class CgwRipper {

	final public static String baseUrl = "https://ffxiv.consolegameswiki.com/wiki/";
	
    public static JSONArray parseArmor(String url) {
        
        Document doc = null;
        JSONArray container = new JSONArray();
        WebCommon wc = new WebCommon();
        
        try {
            doc = Jsoup.connect(url).get();
            Elements testElements = doc.select("tr");
            for(Element tElement : testElements) {
                String tiName = tElement.child(0).text();
                if(wc.isSet(tiName) && !tiName.equals("Item")) {
                    String tiLevel = tElement.child(2).text();
                    String tiILEV = tElement.child(3).text();
                    String tiDef = tElement.child(5).text();
                    String tiMDef = tElement.child(6).text();
                    String tiMSlot = tElement.child(7).text();
                    String tiStat = tElement.child(8).text();
                    JSONObject innerObject = new JSONObject();
                    innerObject
                            .put("Name", tiName)
                            .put("Level", tiLevel)
                            .put("Item Level", tiILEV)
                            .put("Defense", tiDef)
                            .put("Magic Defense", tiMDef)
                            .put("Materia Slots", tiMSlot)
                            .put("Stats", tiStat);
                    container.put(innerObject);
                }
                
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
                
        return container;
        
    }
       
    public static JSONArray parseOther(String url) {
        
        Document doc = null;
        JSONArray container = new JSONArray();
        WebCommon wc = new WebCommon();
        
        try {
            doc = Jsoup.connect(url).get();
            Elements testElements = doc.select("tr");
            for(Element tElement : testElements) {
                String tiName = tElement.child(0).text();
                if(wc.isSet(tiName) && !tiName.equals("Item")) {
                    String tiEffects = tElement.child(2).text();
                    JSONObject innerObject = new JSONObject();
                    innerObject
                            .put("Name", tiName)
                            .put("Stats", tiEffects);
                    container.put(innerObject);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
                
        return container;
        
    }
    
    public static JSONArray parseTools(String url) {
        
        Document doc = null;
        JSONArray container = new JSONArray();
        WebCommon wc = new WebCommon();
        
        try {
            doc = Jsoup.connect(url).get();
            Elements testElements = doc.select("tr");
            for(Element tElement : testElements) {
                String tiName = tElement.child(0).text();
                if(wc.isSet(tiName) && !tiName.equals("Item")) {
                    String tiLevel = tElement.child(2).text();
                    String tiILEV = tElement.child(3).text();
                    String tiStat = tElement.child(4).text();
                    JSONObject innerObject = new JSONObject();
                    innerObject
                            .put("Name", tiName)
                            .put("Level", tiLevel)
                            .put("Item Level", tiILEV)
                            .put("Stats", tiStat);
                    container.put(innerObject);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
                
        return container;
        
    }
    
    public static JSONArray parseWeapons(String url) {
        
        Document doc = null;
        JSONArray container = new JSONArray();
        WebCommon wc = new WebCommon();
        
        try {
            doc = Jsoup.connect(url).get();
            Elements testElements = doc.select("tr");
            for(Element tElement : testElements) {
                String tiName = tElement.child(0).text();
                if(wc.isSet(tiName) && !tiName.equals("Item")) {
                    String tiLevel = tElement.child(2).text();
                    String tiILEV = tElement.child(3).text();
                    String tiDam = tElement.child(4).text();
                    String tiDelay = tElement.child(5).text();
                    String tiAutoAtt = tElement.child(6).text();
                    String tiMateria = tElement.child(7).text();
                    String tiStats = tElement.child(8).text();
                    JSONObject innerObject = new JSONObject();
                    innerObject
                            .put("Name", tiName)
                            .put("Level", tiLevel)
                            .put("Item Level", tiILEV)
                            .put("Damage", tiDam)
                            .put("Delay", tiDelay)
                            .put("Auto Attack", tiAutoAtt)
                            .put("Materia Slots", tiMateria)
                            .put("Stats", tiStats);
                    container.put(innerObject);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
                
        return container;
        
    }
    
    public static String sqlSanitize(String inputString) {
    	
    	String outputString = inputString + ";";
    	outputString = outputString.replace(",;", ";");
    	return outputString;
    	
    }
    
    public static String sqlString1_Armor() {

        String[] parserSet1 = {
            "Bracelets",
            "Earrings",
            "Necklace",
            "Ring",
            "Body",
            "Feet",
            "Hands",
            "Head",
            "Legs",
            "Shield",
            "Waist"
        };
        
        String queryData1 = "INSERT IGNORE INTO Core.FFXIV_Items_Auto" +
                " (Name, Level, ItemLevel, Defense, MagicDefense, Stats) VALUES";        
        for(int i = 0; i < parserSet1.length; i++) {
        	String tUrl = baseUrl + parserSet1[i];
            JSONArray tJa = parseArmor(tUrl);
            for(int j = 0; j < tJa.length(); j++) {
                JSONObject tJo = tJa.getJSONObject(j);
                String tRow = " (" +
                        "'" + tJo.getString("Name").replaceAll("\'","\\\\\'") + "'," +
                        "'" + tJo.getString("Level") + "'," +
                        "'" + tJo.getString("Item Level") + "'," +
                        "'" + tJo.getString("Defense") + "'," +
                        "'" + tJo.getString("Magic Defense") + "'," +
                        "'" + tJo.getString("Stats").replaceAll("\'","\\\\\'") + "'" +
                        "),";
                queryData1 += tRow;
            }
        }
        
        return sqlSanitize(queryData1);
        
    }
    
    public static String sqlString2_Items() {

        String[] parserSet2 = {
            "Soul_Crystals",
            "Alchemist_Tools",
            "Armorer_Tools",
            "Blacksmith_Tools",
            "Botanist_Tools",
            "Carpenter_Tools",
            "Culinarian_Tools",
            "Fisher_Tools",
            "Goldsmith_Tools",
            "Leatherworker_Tools",
            "Miner_Tools",
            "Weaver_Tools"
        };
        
        String queryData2 = "INSERT IGNORE INTO Core.FFXIV_Items_Auto" +
                " (Name, Level, ItemLevel, Stats) VALUES";
        for(int i = 0; i < parserSet2.length; i++) {
        	String tUrl = baseUrl + parserSet2[i];
            JSONArray tJa = parseTools(tUrl);
            for(int j = 0; j < tJa.length(); j++) {
                JSONObject tJo = tJa.getJSONObject(j);
                String tRow = " (" +
                        "'" + tJo.getString("Name").replaceAll("\'","\\\\\'") + "'," +
                        "'" + tJo.getString("Level") + "'," +
                        "'" + tJo.getString("Item Level") + "'," +
                        "'" + tJo.getString("Stats").replaceAll("\'","\\\\\'") + "'" +
                        "),";
                queryData2 += tRow;
            }
        }
        
        return sqlSanitize(queryData2);
        
    }
    
    public static String sqlString3_Weapons() {
        
        String[] parserSet3 = {
            "Books",
            "Bows",
            "Canes",
            "Canes_(Blue_Mage)",
            "Daggers",
            "Firearms",
            "Fist_Weapons",
            "Greataxes",
            "Greatswords",
            "Gunblades",
            "Katanas",
            "Rapiers",
            "Scepters",
            "Star_Globes",
            "Staves",
            "Swords",
            "Throwing_Weapons",
            "Wands"
        };

        String queryData3 = "INSERT IGNORE INTO Core.FFXIV_Items_Auto" +
                " (Name, Level, ItemLevel, Damage, Delay, AutoAttack, MateriaSlots, Stats) VALUES";
        for(int i = 0; i < parserSet3.length; i++) {
        	String tUrl = baseUrl + parserSet3[i];    
            JSONArray tJa = parseWeapons(tUrl);
            for(int j = 0; j < tJa.length(); j++) {
                JSONObject tJo = tJa.getJSONObject(j);
                String tRow = " (" +
                        "'" + tJo.getString("Name").replaceAll("\'","\\\\\'") + "'," +
                        "'" + tJo.getString("Level") + "'," +
                        "'" + tJo.getString("Item Level") + "'," +
                        "'" + tJo.getString("Damage") + "'," +
                        "'" + tJo.getString("Delay") + "'," +
                        "'" + tJo.getString("Auto Attack") + "'," +
                        "'" + tJo.getString("Materia Slots") + "'," +
                        "'" + tJo.getString("Stats").replaceAll("\'","\\\\\'") + "'" +
                        "),";
                queryData3 += tRow;                
            }
        }
        
        return sqlSanitize(queryData3);
        
    }
    
    public static String sqlString4_Other() {

        String[] parserSet4 = {
            "Meals",
            "Medicine"
        };
        
    	String queryData4 = "INSERT IGNORE INTO Core.FFXIV_Items_Auto" +
                " (Name, Stats) VALUES";
        for(int i = 0; i < parserSet4.length; i++) {
        	String tUrl = baseUrl + parserSet4[i];    
            JSONArray tJa = parseOther(tUrl);
            for(int j = 0; j < tJa.length(); j++) {
                JSONObject tJo = tJa.getJSONObject(j);
                String tRow = " (" +
                        "'" + tJo.getString("Name").replaceAll("\'","\\\\\'") + "'," +
                        "'" + tJo.getString("Stats").replaceAll("\'","\\\\\'") + "'" +
                        "),";
                queryData4 += tRow;      
            }
            
        }
        
        return sqlSanitize(queryData4);
        
    }
    
    public static String parserMain() {

        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        WebCommon wc = new WebCommon();
        
    	String returnData = "";
    	
        String queryData1 = sqlString1_Armor();
    	String queryData2 = sqlString2_Items();
    	String queryData3 = sqlString3_Weapons();
        String queryData4 = sqlString4_Other();
        
        try { returnData += wc.q2do1c(dbc, queryData1, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, queryData2, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, queryData3, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, queryData4, null); } catch (Exception e) { e.printStackTrace(); }
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        returnData += "\n"+queryData1;
        
        return returnData;
    	
    }
    
    
    public static void main(String[] args) {
    	
    	String returnData = parserMain();
    	System.out.println(returnData);
    	
    }
    
}


