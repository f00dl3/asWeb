/*
 * 
by Anthony Stump
Created: 26 Mar 2020
Updated: 4 Aug 2020

*/

package asUtilsPorts.Feed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import asUtilsPorts.Mailer;
import asWebRest.action.GetStockAction;
import asWebRest.action.UpdateStockAction;
import asWebRest.dao.StockDAO;
import asWebRest.secure.FinnHubBeans;

public class Stocks {	
	
//https://query1.finance.yahoo.com/v7/finance/spark?symbols=TMUS,HAL
	 
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
	
	public String apiCallStock_FinnHub(String quote) {
		FinnHubBeans fhb = new FinnHubBeans();
		String url = "https://finnhub.io/api/v1/quote";
		String dataBack = "qpiCallStockFIO()\n";
		final String fhApiKey = fhb.getApiKey();
		try {
			dataBack = Jsoup.connect(url)
				.ignoreContentType(true)
				.data("token", fhApiKey)
				.data("symbol", quote)
				.method(Method.GET)
				.execute()
				.body();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataBack;
	}
	
	private String apiCallStock_Yahoo7Multi(String quotes) {
		String url = "https://query1.finance.yahoo.com/v7/finance/spark";
		String dataBack = "qpiCallStock()\n";
		try {
			dataBack = Jsoup.connect(url)
				.ignoreContentType(true)
				.data("region", "US")
				.data("lang", "en-US")
				.data("symbols", quotes)
				.execute()
				.body();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataBack;
}


	public String getStockQuote(Connection dbc, boolean sendEmail) {

		String quote = "Daily stock market report\n\n";
		
		GetStockAction getStockAction = new GetStockAction(new StockDAO());
		Mailer mailer = new Mailer();
		UpdateStockAction updateStockAction = new UpdateStockAction(new StockDAO());
		
		JSONArray stocksToFetch = getStockAction.getStockList(dbc);

		JSONObject stockIndex = new JSONObject();
		
		for(int i = 0; i < stocksToFetch.length(); i++) {			
			try { 
				JSONObject tStock = stocksToFetch.getJSONObject(i);
				String tTicker = tStock.getString("Symbol");
				String tDescription = tStock.getString("Description");
				double tShares = tStock.getDouble("Count");
				int tManaged = tStock.getInt("Managed");
				JSONObject tStockData = null;
				double valueNow = 0.0;
				String valueNowS = "";				
				String previousClose = "";
				/* if(tManaged == 0) { */
					tStockData = new JSONObject(apiCallStock(tTicker));
					JSONObject tChart = tStockData.getJSONObject("chart");
					JSONArray result = tChart.getJSONArray("result");
					JSONObject first = result.getJSONObject(0);
					JSONObject meta = first.getJSONObject("meta");
					valueNow = meta.getDouble("regularMarketPrice");
					previousClose = String.valueOf(meta.getDouble("chartPreviousClose"));
				/* } else {
					tStockData = new JSONObject(apiCallStock_FinnHub(tTicker));
					//quote += tTicker + " : " + tStockData.toString();
					valueNow = tStockData.getDouble("c");
					previousClose = String.valueOf(tStockData.getDouble("pc"));
				} */
				valueNowS = String.valueOf(valueNow);
				if(valueNowS.equals("")) { valueNowS = previousClose; }
				double totVal = valueNow * tShares;
				if(!valueNowS
						.equals("0.0")) {
					List<String> qParams = new ArrayList<>();
					qParams.add(0, valueNowS);
					qParams.add(1, previousClose);
					qParams.add(2, tTicker);
					updateStockAction.setStockUpdate(dbc, qParams);
					quote += "\n" + tDescription + " (" + tTicker + ") : " + tShares + " shares @ $" + valueNowS + " (Total $" + totVal + ")";  
					JSONObject tSubStock = new JSONObject();
					tSubStock
						.put("description", tDescription)
						.put("shares",  tShares)
						.put("valueEach", valueNowS)
						.put("totalValue", totVal)
						.put("previousClose", previousClose);
					stockIndex.put(tTicker, tSubStock);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        
		List<String> qParams2 = new ArrayList<>();
		qParams2.add(stockIndex.toString());
		updateStockAction.setStockIndex(dbc, qParams2);
		
		if(sendEmail) { mailer.sendQuickEmail(quote); }
		
		return quote;

	}	

	public String getStockQuote_FinnHub(Connection dbc, boolean sendEmail) {

		String quote = "Daily stock market report FH\n\n";
		
		GetStockAction getStockAction = new GetStockAction(new StockDAO());
		Mailer mailer = new Mailer();
		UpdateStockAction updateStockAction = new UpdateStockAction(new StockDAO());
		
		JSONArray stocksToFetch = getStockAction.getStockList(dbc);

		JSONObject stockIndex = new JSONObject();
		
		for(int i = 0; i < stocksToFetch.length(); i++) {			
			//if(i == 0) { 
				try { 
					JSONObject tStock = stocksToFetch.getJSONObject(i);
					String tTicker = tStock.getString("Symbol");
					String tDescription = tStock.getString("Description");
					int tShares = tStock.getInt("Count");
					//quote += "TICK: " + tTicker;
					JSONObject tStockData = new JSONObject(apiCallStock_FinnHub(tTicker));
					//quote += tStockData.toString();
					double valueNow = tStockData.getDouble("c");
					String previousClose = String.valueOf(tStockData.getDouble("pc"));
					double totVal = valueNow * tShares;
					String valueNowS = String.valueOf(valueNow);
					List<String> qParams = new ArrayList<>();
					qParams.add(0, valueNowS);
					qParams.add(1, previousClose);
					qParams.add(2, tTicker);
					updateStockAction.setStockUpdate(dbc, qParams);
					quote += "\n" + tDescription + " (" + tTicker + ") : " + tShares + " shares @ $" + valueNowS + " (Total $" + totVal + ")";  
					JSONObject tSubStock = new JSONObject();
					tSubStock
						.put("description", tDescription)
						.put("shares",  tShares)
						.put("valueEach", valueNowS)
						.put("totalValue", totVal)
						.put("previousClose", previousClose);
					stockIndex.put(tTicker, tSubStock);
				} catch (Exception e) {
					e.printStackTrace();
				}
			//} else { }
		}
        
		List<String> qParams2 = new ArrayList<>();
		qParams2.add(stockIndex.toString());
		updateStockAction.setStockIndex(dbc, qParams2);
		
		if(sendEmail) { mailer.sendQuickEmail(quote); }
		
		return quote;

	}	
	

	public String getStockQuote_Yahoo7Multi(Connection dbc) {

		String quote = "Daily stock market report using MultiQuote Yahoo v7 API\n\n";
		String stockQuoteList = "";
		
		GetStockAction getStockAction = new GetStockAction(new StockDAO());
		Mailer mailer = new Mailer();
		UpdateStockAction updateStockAction = new UpdateStockAction(new StockDAO());
		
		JSONArray stocksToFetch = getStockAction.getStockList(dbc);

		JSONObject stockIndex = new JSONObject();
		
		
		for(int i = 0; i < stocksToFetch.length(); i++) {		
			JSONObject tStock = stocksToFetch.getJSONObject(i);
			String tTicker = tStock.getString("Symbol");
			stockQuoteList += tTicker + ",";
		}
		
		quote += "\nDBG: Ticker List: " + stockQuoteList;
		
		try { 
			JSONObject tStockData = null;
			tStockData = new JSONObject(apiCallStock_Yahoo7Multi(stockQuoteList));
			//quote += "\nDBG: " + tStockData.toString();
			JSONObject tChart = tStockData.getJSONObject("spark");
			JSONArray result = tChart.getJSONArray("result");
			for(int i = 0; i < result.length(); i++) {
				String tTicker = "";
				double valueNow = 0.0;
				String valueNowS = "";				
				String previousClose = "";
				JSONObject quoteHolder = result.getJSONObject(i);
				JSONArray quoteData = quoteHolder.getJSONArray("response");
				JSONObject first = quoteData.getJSONObject(0);
				JSONObject meta = first.getJSONObject("meta");
				tTicker = meta.getString("symbol");
				valueNow = meta.getDouble("regularMarketPrice");
				previousClose = String.valueOf(meta.getDouble("chartPreviousClose"));
				valueNowS = String.valueOf(valueNow);
				if(valueNowS.equals("")) { valueNowS = previousClose; }
				//quote += "\n" + tTicker + ": " + valueNowS;  
				if(!valueNowS.equals("0.0")) {
					List<String> qParams = new ArrayList<>();
					qParams.add(0, valueNowS);
					qParams.add(1, previousClose);
					qParams.add(2, tTicker);
					updateStockAction.setStockUpdate(dbc, qParams);
					JSONObject tSubStock = new JSONObject();
					tSubStock
						.put("valueEach", valueNowS)
						.put("previousClose", previousClose);
					stockIndex.put(tTicker, tSubStock);
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		List<String> qParams2 = new ArrayList<>();
		qParams2.add(stockIndex.toString());
		updateStockAction.setStockIndex(dbc, qParams2);
		
		return quote;

	}	

	
}
