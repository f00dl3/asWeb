/*
by Anthony Stump
Created: 1 Dec 2019
Updated: 10 Jun 2020
 */

package asWebRest.hookers;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;

import asWebRest.secure.JunkyPrivate;
import asUtilsPorts.Mailer;
import asWebRest.action.GetSmarthomeAction;
import asWebRest.dao.SmarthomeDAO;

public class AlarmSystem {
	
	public void notifyOfEvent(Connection dbc, List<String> qParamsIn) {

		String messageContent = qParamsIn.get(1);

        GetSmarthomeAction getSmarthomeAction = new GetSmarthomeAction(new SmarthomeDAO());
		final DateTime nowTime = new DateTime();
        final DateTime constraint = new DateTime(nowTime.minusHours(2));
        final DateTimeFormatter theDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        final DateTimeFormatter theDateFormatOld = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
        boolean exitLoop = false;
        
        JSONArray requestStore = getSmarthomeAction.getArmDisarm(dbc);
        for(int i = 0; i < requestStore.length(); i++) {
        	if(!exitLoop) {
	        	JSONObject tjo = requestStore.getJSONObject(i);
	        	String armType = tjo.getString("ArmType");
	        	String tt2c = tjo.getString("ArmTime");
	            DateTime tati = theDateFormat.parseDateTime(tt2c);
	            if(tati.isAfter(constraint)) {
	            	switch(armType) {
	            		case "Disarm2H":
	            			System.out.println("DEBUG ===> System is disarmed. Suppressing alarm.");
	            			exitLoop = true;
	            			break;
	            		case "ArmStay": case "ArmAway": default: // separate later?
	            			System.out.println("DEBUG ===> System is armed. Sending alarm.");
	            			sendAlarm(messageContent);
	            			exitLoop = true;
	            			break;
	            	}
	            } else {
        			System.out.println("DEBUG ===> No instructions. Sending alarm.");
        			sendAlarm(messageContent);
        			exitLoop = true;
	            }
        	}
        }

	}

	public void notifyOfEventEnvironmental(List<String> qParamsIn) {
	    sendAlarm(qParamsIn.get(1));
	}
    
	private void sendAlarm(String messageContent) {
		JunkyPrivate jp = new JunkyPrivate();
        String messageRecipient = jp.getSmsAddress();
		Mailer mailer = new Mailer();
		try { mailer.sendMail(messageRecipient, "asWeb Smarthome Event", messageContent, null); } catch (Exception e) { e.printStackTrace(); }
	}
	
}


