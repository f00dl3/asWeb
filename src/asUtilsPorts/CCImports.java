/*
by Anthony Stump
Created: 2 Sep 2017
Updated: 27 May 2019
*/

package asUtilsPorts;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;

public class CCImports {
	
	private static void discover() {
		String[] args = { "Discover" };
		main(args);
	}
	
	private static void oldNavy() {
		String[] args = { "OldNavy" };
		main(args);
	}
	
	public static void doDiscover() { discover(); }
	public static void doOldNavy() { oldNavy(); }

	public static void main(String[] args) {

		WebCommon wc = new WebCommon();
		CommonBeans cb = new CommonBeans();
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
                
		final String placeCCImportsHere = cb.getPathChartCache().toString();
		String accountType = args[0];

		if (accountType.equals("OldNavy")) {

			File oldNavyCSV = new File(placeCCImportsHere+"/OldNavy.csv");
			StumpJunk.sedFileDeleteFirstLine(placeCCImportsHere+"/OldNavy.csv");

			String oldNavySQL = "INSERT IGNORE INTO Core.FB_ONCCXX ("
				+ "Date, Description, Debit, Credit, ReferenceNo"
				+ ") VALUES";

			Scanner oldNavyScanner = null; try {		
				oldNavyScanner = new Scanner(oldNavyCSV);
				while(oldNavyScanner.hasNext()) {
					double thisDebit = 0.00;
					double thisCredit = 0.00;				
					String line = oldNavyScanner.nextLine();
					line = line.replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\"");
					String[] lineTmp = line.split(",");

					String thisTransactionDate = lineTmp[0];
					DateTimeFormatter thisTransactionDatePattern = DateTimeFormat.forPattern("MM/dd/yyyy");
					DateTime parsedDateTime = thisTransactionDatePattern.parseDateTime(thisTransactionDate);
					DateTimeFormatter thisTransactionDateSQLPattern = DateTimeFormat.forPattern("yyyy-MM-dd");
					String thisDateForSQL = thisTransactionDateSQLPattern.print(parsedDateTime);

					String thisReferenceNo = lineTmp[2];

					double thisTransactionAmount = Double.parseDouble(lineTmp[3]);
					if (thisTransactionAmount < 0.00) {
						thisCredit = 0.00;
						thisDebit = java.lang.Math.abs(thisTransactionAmount);
					} else {
						thisCredit = java.lang.Math.abs(thisTransactionAmount);
						thisDebit = 0.00;
					}

					String thisDescription = lineTmp[4].replaceAll(" +", " ");
					
					String feedback = "Transaction: \nDate: "+thisTransactionDate+" (SQL: "+thisDateForSQL+")\n"
						+ "ReferenceNo: "+thisReferenceNo+"\n"
						+ "Amount: "+thisTransactionAmount+" (Debit: "+thisDebit+" / Credit: "+thisCredit+")\n"
						+ "Description: "+thisDescription+"\n\n";

					System.out.println(feedback);

					oldNavySQL = oldNavySQL+" ('"+thisDateForSQL+"','"+thisDescription+"',"+thisDebit+","+thisCredit+",'"+thisReferenceNo+"'),";
					
				}
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }

			oldNavySQL = (oldNavySQL+";").replaceAll("\\,\\;", "\\;");

			System.out.println(oldNavySQL);
                        try { wc.q2do1c(dbc, oldNavySQL, null); } catch (Exception e) { e.printStackTrace(); }

			oldNavyCSV.delete();

		}

		if (accountType.equals("Discover")) {

			File discoverCSV = new File(placeCCImportsHere+"/Discover.csv");
			StumpJunk.sedFileDeleteFirstLine(placeCCImportsHere+"/Discover.csv");

			String discoverSQL = "INSERT IGNORE INTO Core.FB_DICC45 ("
				+ "Date, Description, Debit, Credit"
				+ ") VALUES";

			Scanner discoverScanner = null; try {		
				discoverScanner = new Scanner(discoverCSV);
				while(discoverScanner.hasNext()) {
					double thisDebit = 0.00;
					double thisCredit = 0.00;				
					String line = discoverScanner.nextLine();
					line = line.replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\"");
					String[] lineTmp = line.split(",");

					String thisTransactionDate = lineTmp[0];
					DateTimeFormatter thisTransactionDatePattern = DateTimeFormat.forPattern("MM/dd/yyyy");
					DateTime parsedDateTime = thisTransactionDatePattern.parseDateTime(thisTransactionDate);
					DateTimeFormatter thisTransactionDateSQLPattern = DateTimeFormat.forPattern("yyyy-MM-dd");
					String thisDateForSQL = thisTransactionDateSQLPattern.print(parsedDateTime);

					double thisTransactionAmount = Double.parseDouble(lineTmp[3]);
					if (thisTransactionAmount > 0.00) {
						thisCredit = 0.00;
						thisDebit = java.lang.Math.abs(thisTransactionAmount);
					} else {
						thisCredit = java.lang.Math.abs(thisTransactionAmount);
						thisDebit = 0.00;
					}

					String thisDescription = lineTmp[2].replaceAll(" +", " ");
					
					String feedback = "Transaction: \nDate: "+thisTransactionDate+" (SQL: "+thisDateForSQL+")\n"
						+ "Amount: "+thisTransactionAmount+" (Debit: "+thisDebit+" / Credit: "+thisCredit+")\n"
						+ "Description: "+thisDescription+"\n\n";

					System.out.println(feedback);

					discoverSQL = discoverSQL+" ('"+thisDateForSQL+"','"+thisDescription+"',"+thisDebit+","+thisCredit+"),";
					
				}
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }

			discoverSQL = (discoverSQL+";").replaceAll("\\,\\;", "\\;");
                        
			System.out.println(discoverSQL);
            try { wc.q2do1c(dbc, discoverSQL, null); } catch (Exception e) { e.printStackTrace(); }

			discoverCSV.delete();

		}
                
		try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }

	}

}
