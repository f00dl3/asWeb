/*
by Anthony Stump
Created: 1 May 2021
Updated: 2 May 2021
 */

package asWebRest.hookers;

import java.io.File;
import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.action.GetFinanceAction;
import asWebRest.action.GetStockAction;
import asWebRest.dao.FinanceDAO;
import asWebRest.dao.StockDAO;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class StockFinanceCSV {
	
	public String generateCSV(Connection dbc) {
		
		String csvString = "Symbol,Portfolio,Shares,Price\n";
		
		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();
		GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
        GetStockAction getStockAction = new GetStockAction(new StockDAO());
        
        JSONArray stonks = getStockAction.getStockList(dbc);
        
        for(int i = 0; i < stonks.length(); i++) {
        	JSONObject thisStonk = stonks.getJSONObject(i);
        	String tHolder = thisStonk.getString("Holder");
        	String tSymbol = thisStonk.getString("Symbol");
        	Double tCount = thisStonk.getDouble("Count");
        	Double tPrice = thisStonk.getDouble("LastBuy");
        	Double lPrice = Double.valueOf(thisStonk.getString("LastValue"));
        	if(tHolder.equals("Crypto")) {
        		if(tPrice == 0) { tPrice = lPrice; }
        		csvString += tSymbol + ",Crypto," + tCount + "," + tPrice + "\n";
        	} else {
        		if(thisStonk.getDouble("FIIBAN") != 0) {
            		if(tPrice == 0) { tPrice = lPrice; }
				tCount = (thisStonk.getDouble("FIIBAN") * thisStonk.getDouble("Multiplier")) - thisStonk.getDouble("Unvested");
				if(thisStonk.getDouble("Unvested") == 0) {
	        			csvString += tSymbol + ",FIIBAN," + tCount + "," + tPrice + "\n";
				} else {
					csvString += tSymbol + ",FIIBAN," + tCount + "," + tPrice + "\n";
					Double tReward = (thisStonk.getDouble("Unvested") * thisStonk.getDouble("Multiplier"));
					csvString += tSymbol + ",Reward," + tReward + "," + tPrice + "\n";
				}
        		}
        		if(thisStonk.getDouble("FIRIAN") != 0) {
        			tPrice = thisStonk.getDouble("LastBuyFIRIAN");
            		if(tPrice == 0) { tPrice = lPrice; }
        			tCount = (thisStonk.getDouble("FIRIAN") * thisStonk.getDouble("Multiplier")) - thisStonk.getDouble("Unvested");
        			csvString += tSymbol + ",FIRIAN," + tCount + "," + tPrice + "\n";
        		}
        		if(thisStonk.getDouble("FI4KAN") != 0) {
        			tPrice = thisStonk.getDouble("LastBuyFI4KAN");
            		if(tPrice == 0) { tPrice = lPrice; }
        			tCount = (thisStonk.getDouble("FI4KAN") * thisStonk.getDouble("Multiplier")) - thisStonk.getDouble("Unvested");
        			csvString += tSymbol + ",FI4KAN," + tCount + "," + tPrice + "\n";
        		}
        		if(thisStonk.getDouble("EJTI15") != 0) {
        			tPrice = thisStonk.getDouble("LastBuyEJTI15");
            		if(tPrice == 0) { tPrice = lPrice; }
        			tCount = (thisStonk.getDouble("EJTI15") * thisStonk.getDouble("Multiplier")) - thisStonk.getDouble("Unvested");
        			csvString += tSymbol + ",EJTI15," + tCount + "," + tPrice + "\n";
        		}
        		if(thisStonk.getDouble("EJRI07") != 0) {
        			tPrice = thisStonk.getDouble("LastBuyEJRI07");
            		if(tPrice == 0) { tPrice = lPrice; }
        			tCount = (thisStonk.getDouble("EJRI07") * thisStonk.getDouble("Multiplier")) - thisStonk.getDouble("Unvested");
        			csvString += tSymbol + ",EJRI07," + tCount + "," + tPrice + "\n";
        		}
        	}
        }
        
        JSONArray enwData = getFinanceAction.getEnwChartRapid(dbc);
        JSONObject lfd = enwData.getJSONObject(0);
        csvString += "FDRXX,Fixed," + lfd.getDouble("AsFix") + ",1\n" +
        		"FDRXX,Checking," + lfd.getDouble("AsLiq_FBCFCK01") + ",1\n" +
        		"FDRXX,Insurance," + lfd.getDouble("Life") + ",1\n";        		
        
        File cacherFolder = new File(cb.getPathChartCache());
        if(!cacherFolder.exists()) { cacherFolder.mkdirs(); }
        File csvOut = new File(cacherFolder.toString() + "/stockExport.csv");
        try { wc.varToFile(csvString, csvOut, false); } catch (Exception e) { csvString += "\n\n" + e.getMessage(); }
        
        return csvString;
		
	}
	
}


