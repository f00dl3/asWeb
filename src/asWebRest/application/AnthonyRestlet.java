/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 9 Nov 2020
 */

package asWebRest.application;

import asWebRest.resource.AddressResource;
import asWebRest.resource.AutomotiveResource;
import asWebRest.resource.BackendResource;
import asWebRest.resource.CgwRipperResource;
import asWebRest.resource.Chart3Resource;
import asWebRest.resource.ChartResource;
import asWebRest.resource.CongressResource;
import asWebRest.resource.CookingResource;
import asWebRest.resource.CrashDataResource;
import asWebRest.resource.DatabaseInfoResource;
import asWebRest.resource.EntertainmentResource;
import asWebRest.resource.FfxivResource;
import asWebRest.resource.FinanceResource;
import asWebRest.resource.FitnessResource;
import asWebRest.resource.HomeResource;
import asWebRest.resource.HomicideResource;
import asWebRest.resource.ICalResource;
import asWebRest.resource.LoginResource;
import asWebRest.resource.LogsResource;
import asWebRest.resource.MediaServerResource;
import asWebRest.resource.NewsFeedResource;
import asWebRest.resource.SnmpResource;
import asWebRest.resource.PtoResource;
import asWebRest.resource.SessionResource;
import asWebRest.resource.SmarthomeResource;
import asWebRest.resource.StockResource;
import asWebRest.resource.TestResource;
import asWebRest.resource.ToolsResource;
import asWebRest.resource.TpResource;
import asWebRest.resource.UploadResource;
import asWebRest.resource.UtilityUseResource;
import asWebRest.resource.WeatherResource;
import asWebRest.resource.WebCalResource;
import asWebRest.resource.WebLinkResource;
import asWebRest.resource.WebVersionResource;

import java.util.Arrays;
import java.util.HashSet;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.engine.application.Encoder;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;
import org.restlet.service.EncoderService;

public class AnthonyRestlet extends Application {
    
    public AnthonyRestlet() {    
    	
        CorsService corsService = new CorsService();
        corsService.setAllowingAllRequestedHeaders(true);
        corsService.setAllowedOrigins(new HashSet(Arrays.asList(
                "http://localhost",
                "https://localhost",
                "http://127.0.0.1",
                "https://127.0.0.1"
        )));
        corsService.setAllowedCredentials(true);
        corsService.setSkippingResourceForCorsOptions(true);
        getServices().add(corsService);

        try { RestletOnStart.ExecuteOnStartup(); } catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Override
    public synchronized Restlet createInboundRoot() {
        Filter encoder = new Encoder(getContext(), false, true, new EncoderService(true));
        Router router = new Router(getContext());
        try { router.attach("/Addresses", AddressResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Automotive", AutomotiveResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Backend", BackendResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/CgwRipper", CgwRipperResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Chart", ChartResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Chart3", Chart3Resource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/CrashData", CrashDataResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Congress", CongressResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Cooking", CookingResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/DBInfo", DatabaseInfoResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Entertainment", EntertainmentResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/FFXIV", FfxivResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Finance", FinanceResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Fitness", FitnessResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Home", HomeResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Homicide", HomicideResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/iCal", ICalResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Login", LoginResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Logs", LogsResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/MediaServer", MediaServerResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/NewsFeed", NewsFeedResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/SessionVars", SessionResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Smarthome", SmarthomeResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/SNMP", SnmpResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Stock", StockResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/PTO", PtoResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Test", TestResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Tools", ToolsResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/TP", TpResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Upload", UploadResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/UtilityUse", UtilityUseResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/Wx", WeatherResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/WebCal", WebCalResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/WebLinks", WebLinkResource.class); } catch (Exception e) { e.printStackTrace(); }
        try { router.attach("/WebVersion", WebVersionResource.class); } catch (Exception e) { e.printStackTrace(); }
        encoder.setNext(router);
        return encoder;      
    }
    
}
