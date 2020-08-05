/*
by Anthony Stump
Split from parent: 4 Aug 2020
Updated: 4 Aug 2020
 */

package asWebRest.resource;

import asWebRest.action.GetStockAction;
import asWebRest.action.UpdateStockAction;
import asWebRest.dao.StockDAO;
import asWebRest.shared.MyDBConnector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class StockResource extends ServerResource {
        
    @Post
    public String doPost(Representation argsIn) {
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetStockAction getStockAction = new GetStockAction(new StockDAO());
        UpdateStockAction updateStockAction = new UpdateStockAction(new StockDAO());
                        
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
               
                case "getStocks":
                    JSONArray stocksP = getStockAction.getStockListPublic(dbc);
                    returnData += stocksP.toString();
                    break;

                case "getStocksAll":
                    JSONArray stocksA = getStockAction.getStockList(dbc);
                    returnData += stocksA.toString();
                    break;
                    
                case "putStockAdd":
                    qParams.add(argsInForm.getFirstValue("Symbol"));
                    qParams.add(argsInForm.getFirstValue("Count"));
                    qParams.add(argsInForm.getFirstValue("Holder"));
                    qParams.add(argsInForm.getFirstValue("Description"));
                    qParams.add(argsInForm.getFirstValue("Managed"));
                    returnData += updateStockAction.setStockAdd(dbc, qParams);
                    break;
                    
                case "putStockUpdate":
                    qParams.add(argsInForm.getFirstValue("Count"));
                    qParams.add(argsInForm.getFirstValue("Holder"));
                    qParams.add(argsInForm.getFirstValue("Symbol"));
                    returnData += updateStockAction.setStockShareUpdate(dbc, qParams);
                    break;                    
                    
            }
        }
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}