/*
by Anthony Stump
Created: 25 Dec 2020
Updated: on creation
 */

package asWebRest.hookers;

import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.secure.EJBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class EJProcessor {
	
	
	public String processData() {

		MyDBConnector mdb = new MyDBConnector();
		Connection dbc = null;
		try { dbc = mdb.getMyConnection(); } catch (Exception e) { }
		CommonBeans cb = new CommonBeans();
		EJBeans ejb = new EJBeans();
		WebCommon wc = new WebCommon();
		
		String dataBack = "";
		String jsonIn = "";
			
		File jsonFile = new File(cb.getPathChartCache()+"/data.ejones");
		
        Scanner ejScanner = null; try {		
        	ejScanner = new Scanner(jsonFile);
                while(ejScanner.hasNext()) {				
                        String line = ejScanner.nextLine();
                        jsonIn += line;
                }
        } catch (Exception e) { dataBack += e.getMessage(); }
		
        JSONArray masterBlob = new JSONArray();
        
		try { masterBlob = new JSONArray(jsonIn); } catch (Exception e) { dataBack += e.getMessage(); }
		
		for(int i = 0; i < masterBlob.length(); i++) {
			
			JSONObject thisBundle = masterBlob.getJSONObject(i);
			String realAcct = "";
			String accountId = thisBundle.getString("acctId");
			
			if(accountId.equals(ejb.get_acct_EJRI07())) { realAcct = "RI07"; }
			else if(accountId.equals(ejb.get_acct_EJRI23())) { realAcct = "RI23"; }
			else if(accountId.equals(ejb.get_acct_EJTI15())) { realAcct = "TI15"; } 
			else { realAcct = "ERROR!"; }
		
			JSONArray dataPoints = thisBundle.getJSONArray("perfDataPoints");
			
			for(int j = 0; j < dataPoints.length(); j++) {
				JSONObject thisData = dataPoints.getJSONObject(j);
				String tDate = thisData.getString("dataPointDt");
				double tBalance = thisData.getDouble("balance");
				double sBalance = tBalance/1000;
				String query = "UPDATE Finances.FB_ENWT SET AsLiq_EJ" + realAcct + "=" + sBalance + " WHERE AsOf = '" + tDate +"';";
		        try { wc.q2do1c(dbc, query, null); } catch (Exception e) { dataBack += e.getMessage(); }
				dataBack += query;				
			}
			
		}
		
		System.out.println(dataBack);
		return dataBack;
		
	}
	
}
