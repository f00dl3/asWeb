/*
by Anthony Stump
Split from parent: 4 Aug 2020
Updated: 18 Feb 2021
 */

package asWebRest.action;

import asWebRest.dao.StockDAO;

import java.sql.Connection;
import org.json.JSONArray;

public class GetStockAction {
    
    private StockDAO stockDAO;
    public GetStockAction(StockDAO stockDAO) { this.stockDAO = stockDAO; }

    public JSONArray getCryptoAccount(Connection dbc) { return stockDAO.getCryptoAccount(dbc); }
    public JSONArray getCryptoBalance(Connection dbc) { return stockDAO.getCryptoBalance(dbc); }
    public JSONArray getETradeBrokerageAccount(Connection dbc) { return stockDAO.getETradeBrokerageAccount(dbc); }
    public JSONArray getETradeBalance(Connection dbc) { return stockDAO.getETradeBalance(dbc); }
    public JSONArray getStockHistory(Connection dbc) { return stockDAO.getStockHistory(dbc); }
    public JSONArray getStockList(Connection dbc) { return stockDAO.getStockList(dbc); }
    public JSONArray getStockListPublic(Connection dbc) { return stockDAO.getStockListPublic(dbc); }
    
}
