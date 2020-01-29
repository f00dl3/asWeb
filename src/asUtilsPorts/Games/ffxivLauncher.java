/*
by Anthony Stump
Created: 2 Jan 2018
Updated: 28 Jan 2020
*/

package asUtilsPorts.Games;

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ffxivLauncher {
    
    public static void main(String[] args) {
        
        if(args.length == 0) { System.out.println("Please enter user to run as!"); System.exit(0); }
        
        final String runAs = args[0];
        
        JunkyBeans junkyBeans = new JunkyBeans();
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        NumberFormat formatter = new DecimalFormat("#0.00");
        
        final String dbGameName = "Final Fantasy 14";
        final File ramTempLauncher = new File(junkyBeans.getHelpers().toString()+"/ffxivLauncherSimple.sh");
        final File desktopLauncher = new File(junkyBeans.getDesktopPath().toString()+"/ffxivLauncherSimple.sh");
		final String queryGetHours = "SELECT Hours FROM Core.GameHours WHERE Name='"+dbGameName+"';";
		final String querySetActive = "UPDATE Core.GameHours SET Active=1 WHERE Name='"+dbGameName+"';";
        final long timeStart = System.currentTimeMillis(); 
        
        if(!desktopLauncher.exists()) {
            try {
                final File registryFix = new File(junkyBeans.getHelpers().toString()+"/ffxivRegFix.reg");
                wc.copyFile(ramTempLauncher.toString(), desktopLauncher.toString());
                wc.copyFile(registryFix.toString(), junkyBeans.getDesktopPath().toString()+"/ffxivRegFix.reg");
                wc.runProcess("chown astump "+desktopLauncher.toString());
                System.out.println(" --> Installed Launcher and WINE Registry Fix to Desktop! [ "+desktopLauncher.toString()+" ]");
            } catch (IOException ix) { ix.printStackTrace(); }
        }
       
        String buildCommandToRun = "";
        double previousHours = 0.0;
        
        if(runAs.equals("astump")) {
            buildCommandToRun = "bash "+junkyBeans.getDesktopPath().toString()+"/ffxivLauncherSimple.sh";
        } else {
            buildCommandToRun = "sudo -i -u astump bash "+junkyBeans.getDesktopPath().toString()+"/ffxivLauncherSimple.sh";
        }
        
        try ( Connection conn = mdb.getMyConnection(); Statement stmt = conn.createStatement(); ResultSet resultSet = stmt.executeQuery(queryGetHours);) {		
            while (resultSet.next()) { previousHours = resultSet.getDouble("Hours"); }
        }
        catch (SQLException sqx) { sqx.printStackTrace(); }
        catch (Exception ex) { ex.printStackTrace(); }
        
        try ( Connection conn2 = mdb.getMyConnection(); Statement stmt2 = conn2.createStatement();) { stmt2.executeUpdate(querySetActive); }
        catch (SQLException se) { se.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }                

        System.out.println(" NOTE: Previous total hours before playing: "+previousHours);
	
        System.out.println(buildCommandToRun);
        wc.runProcess(buildCommandToRun);
        
        final long timeEnd = System.currentTimeMillis();
        double timeDiffSec = (double) ((timeEnd-timeStart)/1000);
        double timeDiffHours = (timeDiffSec / 60.0 / 60.0);
        double timeDiffHoursFormatted = Double.parseDouble(formatter.format(timeDiffHours));
        
        System.out.println(" NOTE: Time program was ran: " + timeDiffHoursFormatted);
        
        double newHours = previousHours + timeDiffHoursFormatted;
        System.out.println(" NOTE: New total play time: " + newHours);
        
        final String queryClose = "UPDATE Core.GameHours SET Active=0, Hours="+newHours+", Last=CURDATE() WHERE Name='"+dbGameName+"';";
        try ( Connection conn3 = mdb.getMyConnection(); Statement stmt3 = conn3.createStatement();) { stmt3.executeUpdate(queryClose); }
        catch (SQLException se) { se.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }

    }
    
}
