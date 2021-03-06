/*
by Anthony Stump
Created: 2 Sep 2017
Updated: 15 Apr 2021
*/

package asUtilsPorts;

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
	
	private static void fidelity() {
		String[] args = { "Fidelity" };
		main(args);
	}
	private static void oldNavy() {
		String[] args = { "OldNavy" };
		main(args);
	}
	
	public static void doDiscover() { discover(); }
	public static void doFidelity() { fidelity(); }
	public static void doOldNavy() { oldNavy(); }

	public static void main(String[] args) {

        MyDBConnector mdb = new MyDBConnector();
		WebCommon wc = new WebCommon();
		CommonBeans cb = new CommonBeans();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
                
		final String placeCCImportsHere = cb.getPathChartCache().toString();
		String accountType = args[0];

		if (accountType.equals("OldNavy")) {

			File oldNavyCSV = new File(placeCCImportsHere+"/OldNavy.csv");
			wc.sedFileDeleteFirstLine(placeCCImportsHere+"/OldNavy.csv");

			String oldNavySQL = "INSERT IGNORE INTO Finances.FB_ONCCXX ("
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
			wc.sedFileDeleteFirstLine(placeCCImportsHere+"/Discover.csv");

			String discoverSQL = "INSERT IGNORE INTO Finances.FB_DICC45 ("
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
		
		if (accountType.equals("Fidelity")) {
			
			File fidelityCSV = new File(placeCCImportsHere+"/Fidelity.csv");
			wc.sedFileDeleteFirstLine(placeCCImportsHere+"/Fidelity.csv");
			
			String fidelitySQL = "INSERT IGNORE INTO Finances.FB_FICCXX ("
					+ "Date, Description, Debit, Credit"
					+ ") VALUES";
			
			Scanner fidelityScanner = null; try {
				fidelityScanner = new Scanner(fidelityCSV);
				while(fidelityScanner.hasNext()) {

					double thisDebit = 0.00;
					double thisCredit = 0.00;				
					String line = fidelityScanner.nextLine();
					line = line.replaceAll("\'", "\\\\\'").replaceAll("\"", "\\\\\"");
					String[] lineTmp = line.split(",");

					String thisTransactionDate = lineTmp[0];
					DateTimeFormatter thisTransactionDatePattern = DateTimeFormat.forPattern("MM/dd/yyyy");
					DateTime parsedDateTime = thisTransactionDatePattern.parseDateTime(thisTransactionDate);
					DateTimeFormatter thisTransactionDateSQLPattern = DateTimeFormat.forPattern("yyyy-MM-dd");
					String thisDateForSQL = thisTransactionDateSQLPattern.print(parsedDateTime);

					double thisTransactionAmount = Double.parseDouble(lineTmp[4]);
					if (thisTransactionAmount < 0.00) {
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

					fidelitySQL = fidelitySQL+" ('"+thisDateForSQL+"','"+thisDescription+"',"+thisDebit+","+thisCredit+"),";
					
				}
			} catch (Exception e) { e.printStackTrace(); }

			fidelitySQL = (fidelitySQL+";").replaceAll("\\,\\;", "\\;");
                        
			System.out.println(fidelitySQL);
            try { wc.q2do1c(dbc, fidelitySQL, null); } catch (Exception e) { e.printStackTrace(); }

            fidelityCSV.delete();

		}
                
		try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }

	}

}
