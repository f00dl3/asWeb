/*
 * 
by Anthony Stump
Created: 26 Mar 2020
Updated: 28 Mar 2020

*/

package asUtilsPorts.Feed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import asUtilsPorts.Mailer;
import asWebRest.action.GetFinanceAction;
import asWebRest.action.UpdateFinanceAction;
import asWebRest.dao.FinanceDAO;

public class Stocks {	
	 
	private String apiCallStock(String quote) {
			String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + quote;
			String dataBack = "qpiCallStock()\n";
			try {
				dataBack = Jsoup.connect(url)
					.ignoreContentType(true)
					.data("region", "US")
					.data("lang", "en-US")
					.execute()
					.body();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dataBack;
	}
	
	public String getStockQuote(Connection dbc, boolean sendEmail) {

		String quote = "Daily stock market report\n\n";
		
		GetFinanceAction getFinanceAction = new GetFinanceAction(new FinanceDAO());
		Mailer mailer = new Mailer();
		UpdateFinanceAction updateFinanceAction = new UpdateFinanceAction(new FinanceDAO());
		
		JSONArray stocksToFetch = getFinanceAction.getStockList(dbc);
		
		for(int i = 0; i < stocksToFetch.length(); i++) {			
			JSONObject tStock = stocksToFetch.getJSONObject(i);
			String tTicker = tStock.getString("Symbol");
			String tDescription = tStock.getString("Description");
			int tShares = tStock.getInt("Count");
			try { 
				JSONObject tStockData = new JSONObject(apiCallStock(tTicker));
				JSONObject tChart = tStockData.getJSONObject("chart");
				JSONArray result = tChart.getJSONArray("result");
				JSONObject first = result.getJSONObject(0);
				JSONObject meta = first.getJSONObject("meta");
				double valueNow = meta.getDouble("regularMarketPrice");
				double totVal = valueNow * tShares;
				String valueNowS = String.valueOf(valueNow);
				List<String> qParams = new ArrayList<>();
				qParams.add(0, valueNowS);
				qParams.add(1, tTicker);
				updateFinanceAction.setStockUpdate(dbc, qParams);
				quote += "\n" + tDescription + " (" + tTicker + ") : " + tShares + " shares @ $" + valueNowS + " (Total $" + totVal + ")";  
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        
		if(sendEmail) { mailer.sendQuickEmail(quote); }
		
		return quote;

	}
	
}