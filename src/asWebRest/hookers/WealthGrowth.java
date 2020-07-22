/*
by Anthony Stump
Created: 19 Jul 2020
Updated: 19 Jul 2020
 */

package asWebRest.hookers;

import java.sql.Connection;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import asWebRest.action.GetFinanceAction;
import asWebRest.dao.FinanceDAO;

public class WealthGrowth {
	
	public JSONArray generateProjections(Connection dbc) {
		
		String returnData = "Projected growth based on: \n\n";
		
		GetFinanceAction gfa = new GetFinanceAction(new FinanceDAO());
		double growth_week = 0.0;
		double growth_month = 0.0;
		double growth_3month = 0.0;
		double growth_year = 0.0;
		double netWorth = 0.0;
		
		try {
			JSONArray nwData = gfa.getEnw(dbc);
			JSONObject thisData = nwData.getJSONObject(0);
			netWorth = thisData.getDouble("NetWorth");
			JSONArray growthData = gfa.getNwga(dbc);
			JSONObject thisGData = growthData.getJSONObject(0);
			growth_week = (1 + ((thisGData.getDouble("p7day") * 4.2) / 100));
			growth_month = (1 + (thisGData.getDouble("p30day") / 100));
			growth_3month = (1 + ((thisGData.getDouble("p90day") / 3) / 100));
			growth_year = (1 + ((thisGData.getDouble("p1year") / 12) / 100));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		returnData += "Estimated worth: $" + netWorth + "\n" +
				"Weekly growth (to month): " + growth_week + "\n" +
				"Monthly growth (to month): " + growth_month + "\n" +
				"3-month Growth (to month): " + growth_3month + "\n" +
				"Annual growth (to month): " + growth_year + "\n\n";		
		
	    LocalDate startDate = LocalDate.now();
	    int thisYear = startDate.getYear();
	    int retirementYear = 2060;
	    int forwardProjection = retirementYear - thisYear;
	    int periods = 1;
	    LocalDate endDate = startDate.plusYears(forwardProjection);

	    returnData += "Projection start: " + startDate + "\n" +
	    	"Projection end: " + endDate + "\n\n";

	    JSONArray dataset = new JSONArray();

		int mW = (int) netWorth;
		int mM = (int) netWorth;
		int mQ = (int) netWorth;
		int mY = (int) netWorth;
	    
	    for(LocalDate currentdate = startDate; 
	            currentdate.isBefore(endDate) || currentdate.isEqual(endDate); 
	            currentdate= currentdate.plusMonths(1)){
	    		    	
	    	JSONObject gpp = new JSONObject();
	    	
    		mW = (int) (mW * growth_week);
    		mM = (int) (mM * growth_month);
    		mQ = (int) (mQ * growth_3month);
    		mY = (int) (mY * growth_year);
	    	
	    	gpp
	    		.put("period", periods)
	    		.put("asOf", currentdate.toString("yyyy-MM-dd"))
	    		.put("mW", mW)
	    		.put("mM", mM)
	    		.put("mQ", mQ)
	    		.put("mY", mY);
	    	
	        dataset.put(gpp);
	        
	        periods++;
	        
	    }
		
	    returnData += "\n" + dataset.toString();
	    
		return dataset;

	}

	
}


