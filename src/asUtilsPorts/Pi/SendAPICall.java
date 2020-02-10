/*
by Anthony Stump
Created: 29 Nov 2019
Updated: 10 Feb 2020
 */

package asUtilsPorts.Pi;

import asUtilsPorts.Shares.PiBeans;
import asUtilsPorts.Shares.SSLHelper;

public class SendAPICall {
    
    public void doDoor(String door) {
	PiBeans piBeans = new PiBeans();
        System.out.println("DEBUG: Logging door event: " + door);
        try {
            SSLHelper.getConnection(piBeans.getApiSmart())
                .data("doWhat", "setDoorEvent")
                .data("OriginalTimestamp", "TODO")
                .data("DoorLocation", door)
                .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void doEnvironmental(String what) {
	PiBeans piBeans = new PiBeans();
        System.out.println("DEBUG: Logging environmental event: " + what);
        try {
            SSLHelper.getConnection(piBeans.getApiSmart())
                .data("doWhat", "setEnvironmentalEvent")
                .data("OriginalTimestamp", "TODO")
                .data("EventLocation", what)
                .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void smarthomeDoorEvent(String door) {        
        try {
            doDoor(door);             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
    	SendAPICall sac = new SendAPICall();
        String inArgs = args[0];
        
        switch(inArgs) {
            
            case "EventDoorBasement": 
                sac.smarthomeDoorEvent("Door Open Basement");
                break;
                
            case "EventDoorDeck": 
            	sac.smarthomeDoorEvent("Door Open Deck");
                break;
                
            case "EventDoorFront":
            	sac.smarthomeDoorEvent("Door Open Front");
                break;
            
            case "EventDownHallCO2":
            	sac.doEnvironmental("Downstairs Hallway CO2 Alarm");
                break;
                
            case "EventDownHallFire":
            	sac.doEnvironmental("Downstairs Hallway Fire Alarm");
                break;
                
            case "EventDownHallOther":
            	sac.doEnvironmental("Downstairs Hallway Other Alarm");
                break;
            
            case "EventUpHallCO2":
            	sac.doEnvironmental("Upstairs Hallway CO2 Alarm");
                break;
                
            case "EventUpHallFire":
            	sac.doEnvironmental("Upstairs Hallway Fire Alarm");
                break;
                
            case "EventUpHallOther":
            	sac.doEnvironmental("Upstairs Hallway Other Alarm");
                break;
                
            case "TEST":
            	sac.smarthomeDoorEvent("API Test Call");
                break;
            
            default:
                System.out.println("NO VALUE ENTERED!");
                break;
            
        }
        
    }
    
}
