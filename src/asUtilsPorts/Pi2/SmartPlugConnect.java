/*
by Anthony Stump
Created: 22 Aug 2019
Updated: 29 Jan 2020
 */

package asUtilsPorts.Pi2;

import asUtilsPorts.Shares.PiBeans;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

import org.json.JSONObject;


public class SmartPlugConnect {
    
    private static void runSmartplugAction(String target, String action) {
        
        PiBeans piBeans = new PiBeans();
        WebCommon wc = new WebCommon();
        
        if(action.equals("cycle")) {
            wc.runProcess(piBeans.getSmartplugPythonScript().getPath() + " -t " + target + " -c off");
            wc.runProcess(piBeans.getSmartplugPythonScript().getPath() + " -t " + target + " -c on");
        } else {
            wc.runProcess(piBeans.getSmartplugPythonScript().getPath() + " -t " + target + " -c " + action);
        }
        
    }
    
    private static void runSmartplugJson(String smartPlug, String doWhat) {
        
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        PiBeans piBeans = new PiBeans();
        
        String commandToRun = piBeans.getSmartplugPythonScript().getPath();
        
        JSONObject jsonToRun = new JSONObject();
        JSONObject jsonToRun_Embed = new JSONObject();
        JSONObject jsonToRun_SubEmbed = new JSONObject();
        
        switch(doWhat) {
			
            case "CleanSchedule":
                jsonToRun_Embed.put("delete_all_rules", JSONObject.NULL);
                jsonToRun.put("schedule", jsonToRun_Embed);
                break;
            
            case "TurnOn":
                jsonToRun_SubEmbed.put("state", 1);
                jsonToRun_Embed.put("set_relay_state", jsonToRun_SubEmbed);
                jsonToRun.put("system", jsonToRun_Embed);
                break;
                
            case "TurnOff":
                jsonToRun_SubEmbed.put("state", 0);
                jsonToRun_Embed.put("set_relay_state", jsonToRun_SubEmbed);
                jsonToRun.put("system", jsonToRun_Embed);
                break;
                
            case "GetInfo": default:
                jsonToRun_Embed.put("get_sysinfo", JSONObject.NULL);
                jsonToRun.put("system", jsonToRun_Embed);
                break;
                
                
        }
        
        switch(smartPlug) {
                        
            case "UNASSIGNED":
                commandToRun += " -t " + junkyPrivate.getIpForSmartplug1() 
                        + " -j " + jsonToRun.toString();
                System.out.println(commandToRun);
                break;
                        
            case "Desktop":
                commandToRun += " -t " + junkyPrivate.getIpForSmartplug2() 
                        + " -j " + jsonToRun.toString();
                System.out.println(commandToRun);
                break;
                        
            case "Router": default: 
                commandToRun += " -t " + junkyPrivate.getIpForSmartplug3() 
                        + " -j " + jsonToRun.toString();
                System.out.println(commandToRun);
                break;
                
        }
        
    }
    
    public static void main(String args[]) {
    
        //runSmartplugJson(args[0], args[1]);
        runSmartplugAction(args[0], args[1]);
        
    }    
    
}
