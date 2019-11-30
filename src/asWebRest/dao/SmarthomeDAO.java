/*
by Anthony Stump
Created: 29 Nov 2019
Updated: 30 Nov 2019
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import asUtils.Secure.JunkyPrivate;
import asUtilsPorts.Mailer;

public class SmarthomeDAO {

	Mailer mailer = new Mailer();
	JunkyPrivate jp = new JunkyPrivate();
    WebCommon wc = new WebCommon();
    
    public JSONArray getDoorEvents(Connection dbc) {
        final String query_DoorEvents = "SELECT EventID, ReceivedTimestamp, OriginalTimestamp, DoorLocation FROM net_snmp.Home_DoorEvents;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_DoorEvents, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EventID", resultSet.getLong("EventID"))
                    .put("ReceivedTimestamp", resultSet.getString("ReceivedTimestamp"))
                    .put("OriginalTimestamp", resultSet.getString("OriginalTimestamp"))
                    .put("DoorLocation", resultSet.getString("DoorLocation"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public String setDoorEvent(Connection dbc, List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_AddDoorEvent = "INSERT INTO net_snmp.Home_DoorEvents (OriginalTimestamp, DoorLocation) values (?, ?);";
        String messageContent = "asWeb Smarthome API Event - " + qParams.get(1) + " detected open!";
        String messageRecipient = jp.getSmsAddress();
        try { returnData = wc.q2do1c(dbc, query_AddDoorEvent, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { mailer.sendMail(messageRecipient, "asWeb Smarthome Door Event", messageContent, null); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
}
