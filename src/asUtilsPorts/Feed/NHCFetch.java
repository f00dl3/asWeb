/*
by Anthony Stump
Created: 14 Aug 2017
Split to NHCFetch: 23 Nov 2019
Updated: 28 Dec 2019
*/

package asUtilsPorts.Feed;

import java.io.File;
import java.sql.Connection;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class NHCFetch {
    
    public static void getNHC(Connection dbc) {

        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
                
        String nhcBase = "http://www.nhc.noaa.gov/";
        final String ramTemp = cb.getPathChartCache().toString();
        final String mysqlShare = ramTemp;
        
		File nhcIAtFileSrc = new File(ramTemp+"/index-at.xml");
		File nhcIEpFileSrc = new File(ramTemp+"/index-ep.xml");
		File nhcDAtFileSrc = new File(ramTemp+"/TWDAT.xml");
		File nhcDEpFileSrc = new File(ramTemp+"/TWDEP.xml");
		
		WebCommon.jsoupOutFile(nhcBase+"index-at.xml", nhcIAtFileSrc);
		WebCommon.jsoupOutFile(nhcBase+"index-ep.xml", nhcIEpFileSrc);
		WebCommon.jsoupOutFile(nhcBase+"xml/TWDAT.xml", nhcDAtFileSrc);
		WebCommon.jsoupOutFile(nhcBase+"xml/TWDEP.xml", nhcDEpFileSrc);
		
		WebCommon.runProcess("iconv -f ISO-8859-1 -t UTF-8 "+ramTemp+"/index-at.xml > "+mysqlShare+"/index-at.xml");
		WebCommon.runProcess("iconv -f ISO-8859-1 -t UTF-8 "+ramTemp+"/index-ep.xml > "+mysqlShare+"/index-ep.xml");
		WebCommon.runProcess("iconv -f ISO-8859-1 -t UTF-8 "+ramTemp+"/TWDAT.xml > "+mysqlShare+"/TWDAT.xml");
		WebCommon.runProcess("iconv -f ISO-8859-1 -t UTF-8 "+ramTemp+"/TWDEP.xml > "+mysqlShare+"/TWDEP.xml");
		
		String nhcIAtSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/index-at.xml' IGNORE INTO TABLE WxObs.NHCFeeds CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET guid = ExtractValue(@tmp, '//guid'), title = ExtractValue(@tmp, '//title'), link = ExtractValue(@tmp, '//link'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";
		String nhcIEpSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/index-ep.xml' IGNORE INTO TABLE WxObs.NHCFeeds CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET guid = ExtractValue(@tmp, '//guid'), title = ExtractValue(@tmp, '//title'), link = ExtractValue(@tmp, '//link'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";
		String nhcDAtSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/TWDAT.xml' IGNORE INTO TABLE WxObs.NHCFeeds CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET guid = ExtractValue(@tmp, '//guid'), title = ExtractValue(@tmp, '//title'), link = ExtractValue(@tmp, '//link'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";
		String nhcDEpSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/TWDEP.xml' IGNORE INTO TABLE WxObs.NHCFeeds CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET guid = ExtractValue(@tmp, '//guid'), title = ExtractValue(@tmp, '//title'), link = ExtractValue(@tmp, '//link'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";

        try { wc.q2do1c(dbc, nhcIAtSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, nhcIEpSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, nhcDAtSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, nhcDEpSQL, null); } catch (Exception e) { e.printStackTrace(); }
        
		new File(mysqlShare+"/index-at.xml").delete();
		new File(mysqlShare+"/index-ep.xml").delete();
		new File(mysqlShare+"/TWDAP.xml").delete();
		new File(mysqlShare+"/TWDEP.xml").delete();
        
    }
    
}