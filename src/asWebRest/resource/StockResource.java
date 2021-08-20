/*
by Anthony Stump
Split from parent: 4 Aug 2020
Updated: 20 Aug 2021
 */

package asWebRest.resource;

import asWebRest.action.GetStockAction;
import asWebRest.action.UpdateStockAction;
import asWebRest.dao.StockDAO;
import asWebRest.hookers.StockFinanceCSV;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
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
        WebCommon wc = new WebCommon();
    	StockFinanceCSV sfc = new StockFinanceCSV();
                        
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
        JSONObject mergedResults = new JSONObject();
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
            
	            case "generateCSV":
	                returnData += sfc.generateCSV(dbc);
	                break;

	            case "getCryptoAccount":
	                JSONArray cra = getStockAction.getCryptoAccount(dbc);
	                returnData += cra.toString();
	                break;
	                
	            case "getETradeBrokerageAccount":
	                JSONArray etba = getStockAction.getETradeBrokerageAccount(dbc);
	                returnData += etba.toString();
	                break;
                
                case "getStocks":
                    JSONArray stocksP = getStockAction.getStockListPublic(dbc);
                    returnData += stocksP.toString();
                    break;

                case "getStocksAll":
                    JSONArray stocksA = getStockAction.getStockList(dbc);
                    JSONArray etbaData = getStockAction.getETradeBrokerageAccount(dbc);
                    JSONArray crData = getStockAction.getCryptoAccount(dbc);
                    JSONArray shitCoins = getStockAction.getShitCoins(dbc);
                    String doNotReturn = "";
                    doNotReturn += sfc.generateCSV(dbc);
                    mergedResults
                    	.put("etba", etbaData)
                    	.put("stocksA", stocksA)
                    	.put("crypto", crData)
                    	.put("shitCoins", shitCoins);
                    returnData += mergedResults.toString();
                    break;
                    
                case "putShitUpdate":
                	String scCount = "0";
                	String scValue = "0";
                	try { scCount = argsInForm.getFirstValue("Count"); } catch (Exception e) { }
                	try { scValue = argsInForm.getFirstValue("Value"); } catch (Exception e) { }
                	String scSymbol = argsInForm.getFirstValue("Symbol");
                	qParams.add(scCount);
                	qParams.add(scValue);
                	qParams.add(scSymbol);
                	returnData += updateStockAction.setShitUpdate(dbc, qParams);
                	break;
                    
                case "putStockAdd":
                	String sLastBuya = "0";
                	String sLastBuyFIRIANa = "0";
                	String sLastBuyFI4KANa = "0";
                	String sLastBuyEJTI15a = "0";
                	String sLastBuyEJRI07a = "0";
                	try { sLastBuya = argsInForm.getFirstValue("LastBuy"); } catch (Exception e) { }
                	try { sLastBuyFIRIANa = argsInForm.getFirstValue("LastBuyFIRIAN"); } catch (Exception e) { }
                	try { sLastBuyFI4KANa = argsInForm.getFirstValue("LastBuyFI4KAN"); } catch (Exception e) { }
                	try { sLastBuyEJTI15a = argsInForm.getFirstValue("LastBuyEJTI15"); } catch (Exception e) { }
                	try { sLastBuyEJRI07a = argsInForm.getFirstValue("LastBuyEJRI07"); } catch (Exception e) { }
                    qParams.add(argsInForm.getFirstValue("Symbol"));
                    qParams.add(argsInForm.getFirstValue("Count"));
                    qParams.add(argsInForm.getFirstValue("Holder"));
                    qParams.add(argsInForm.getFirstValue("Description"));
                    qParams.add(argsInForm.getFirstValue("Managed"));
                    qParams.add(sLastBuya);
                    qParams.add(sLastBuyFIRIANa);
                    qParams.add(sLastBuyFI4KANa);
                    qParams.add(sLastBuyEJTI15a);
                    qParams.add(sLastBuyEJRI07a);
                    returnData += updateStockAction.setStockAdd(dbc, qParams);
                    break;
                    
                case "putStockUpdate":
                	String sEJTI15 = "0";
                	String sEJRI07 = "0";
                	String sFI4KAN = "0";
                	String sFIRIAN = "0";
                	String sFIIBAN = "0";
                	String sLastBuy = "0";
                	String sLastBuyFIRIAN = "0";
                	String sLastBuyFI4KAN = "0";
                	String sLastBuyEJTI15 = "0";
                	String sLastBuyEJRI07 = "0";
                	try { sEJTI15 = argsInForm.getFirstValue("EJTI15"); } catch (Exception e) { }
                	try { sEJRI07 = argsInForm.getFirstValue("EJRI07"); } catch (Exception e) { }
                	try { sFI4KAN = argsInForm.getFirstValue("FI4KAN"); } catch (Exception e) { }
                	try { sFIRIAN = argsInForm.getFirstValue("FIRIAN"); } catch (Exception e) { }
                	try { sFIIBAN = argsInForm.getFirstValue("FIIBAN"); } catch (Exception e) { }
                	try { sLastBuy = argsInForm.getFirstValue("LastBuy"); } catch (Exception e) { }
                	try { sLastBuyFIRIAN = argsInForm.getFirstValue("LastBuyFIRIAN"); } catch (Exception e) { }
                	try { sLastBuyFI4KAN = argsInForm.getFirstValue("LastBuyFI4KAN"); } catch (Exception e) { }
                	try { sLastBuyEJTI15 = argsInForm.getFirstValue("LastBuyEJTI15"); } catch (Exception e) { }
                	try { sLastBuyEJRI07 = argsInForm.getFirstValue("LastBuyEJRI07"); } catch (Exception e) { }
                    qParams.add(argsInForm.getFirstValue("Count"));
                    qParams.add(argsInForm.getFirstValue("Holder"));
                    qParams.add(sEJTI15);
                    qParams.add(sEJRI07);
                    qParams.add(sFI4KAN);
                    qParams.add(sFIRIAN);
                    qParams.add(sFIIBAN);
                    qParams.add(sLastBuy);
                    qParams.add(sLastBuyFIRIAN);
                    qParams.add(sLastBuyFI4KAN);
                    qParams.add(sLastBuyEJTI15);
                    qParams.add(sLastBuyEJRI07);
                    qParams.add(argsInForm.getFirstValue("Symbol"));
                    returnData += updateStockAction.setStockShareUpdate(dbc, qParams);
                    break;                    

                case "putCryptoAccountAdd":
                    String crCredit = "0.00";
                    String crDebit = "0.00";
                    String crDescription = "";
                    if(wc.isSet(argsInForm.getFirstValue("crCredit"))) { crCredit = argsInForm.getFirstValue("crCredit"); }
                    if(wc.isSet(argsInForm.getFirstValue("crDebit"))) { crDebit = argsInForm.getFirstValue("crDebit"); }
                    if(wc.isSet(argsInForm.getFirstValue("crDescription"))) { crDescription = argsInForm.getFirstValue("crDescription"); }
                    qParams.add(argsInForm.getFirstValue("crDate"));
                    qParams.add(crDebit);
                    qParams.add(crCredit);
                    qParams.add(crDescription);
                    returnData += updateStockAction.setCryptoAccountAdd(dbc, qParams);
                    break;
                    
                case "putETradeBrokerageAccountAdd":
                    String etbaCredit = "0.00";
                    String etbaDebit = "0.00";
                    if(wc.isSet(argsInForm.getFirstValue("etbaCredit"))) { etbaCredit = argsInForm.getFirstValue("etbaCredit"); }
                    if(wc.isSet(argsInForm.getFirstValue("etbaDebit"))) { etbaDebit = argsInForm.getFirstValue("etbaDebit"); }
                    qParams.add(argsInForm.getFirstValue("etbaDate"));
                    qParams.add(etbaDebit);
                    qParams.add(etbaCredit);
                    returnData += updateStockAction.setETradeBrokerageAccountAdd(dbc, qParams);
                    break;
                     
            }
        }
    
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
        
    }
    
}
