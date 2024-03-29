/*
by Anthony Stump
Split from Parent: 4 Aug 2020
Updated: 21 Aug 2021
 */

package asWebRest.action;

import asWebRest.dao.StockDAO;

import java.sql.Connection;
import java.util.List;

public class UpdateStockAction {
    
    private StockDAO stockDAO;
    public UpdateStockAction(StockDAO stockDAO) { this.stockDAO = stockDAO; }

    public String setCryptoAccountAdd(Connection dbc, List<String> qParams) { return stockDAO.setCryptoAccountAdd(dbc, qParams); }
    public String setETradeBrokerageAccountAdd(Connection dbc, List<String> qParams) { return stockDAO.setETradeBrokerageAccountAdd(dbc, qParams); }
    public String setShitUpdate(Connection dbc, List<String> qParams) { return stockDAO.setShitUpdate(dbc, qParams); }
    public String setShitUpdateAuto(Connection dbc, List<String> qParams) { return stockDAO.setShitUpdateAuto(dbc, qParams); }
    public String setStockAdd(Connection dbc, List<String> qParams) { return stockDAO.setStockAdd(dbc, qParams); }
    public String setStockIndex(Connection dbc, List<String> qParams) { return stockDAO.setStockIndex(dbc, qParams); }
    public String setStockShareUpdate(Connection dbc, List<String> qParams) { return stockDAO.setStockShareUpdate(dbc, qParams); }
    public String setStockUpdate(Connection dbc, List<String> qParams) { return stockDAO.setStockUpdate(dbc, qParams); }

}
