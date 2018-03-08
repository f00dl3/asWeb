/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 28 Feb 2018
 */

package asWebRest.application;

import asWebRest.resource.CookingResource;
import asWebRest.resource.DatabaseInfoResource;
import asWebRest.resource.EntertainmentResource;
import asWebRest.resource.FinanceResource;
import asWebRest.resource.FitnessResource;
import asWebRest.resource.HomicideResource;
import asWebRest.resource.LoginResource;
import asWebRest.resource.LogsResource;
import asWebRest.resource.MediaServerResource;
import asWebRest.resource.NewsFeedResource;
import asWebRest.resource.SnmpResource;
import asWebRest.resource.PtoResource;
import asWebRest.resource.UtilityUseResource;
import asWebRest.resource.WeatherResource;
import asWebRest.resource.WebLinkResource;
import asWebRest.resource.WebVersionResource;
import java.util.Arrays;
import java.util.HashSet;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

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
    }
    
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/Cooking", CookingResource.class);
        router.attach("/DatabaseInfo", DatabaseInfoResource.class);
        router.attach("/Entertainment", EntertainmentResource.class);
        router.attach("/Finance", FinanceResource.class);
        router.attach("/Fitness", FitnessResource.class);
        router.attach("/Homicide", HomicideResource.class);
        router.attach("/Login", LoginResource.class);
        router.attach("/Logs", LogsResource.class);
        router.attach("/MediaServer", MediaServerResource.class);
        router.attach("/NewsFeed", NewsFeedResource.class);
        router.attach("/SNMP", SnmpResource.class);
        router.attach("/PTO", PtoResource.class);
        router.attach("/UtilityUse", UtilityUseResource.class);
        router.attach("/Wx", WeatherResource.class);
        router.attach("/WebLinks", WebLinkResource.class);
        router.attach("/WebVersion", WebVersionResource.class);
        return router;      
    }
}
