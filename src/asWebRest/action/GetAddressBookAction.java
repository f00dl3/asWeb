/*
by Anthony Stump
Created: 16 Feb 2018
Updated: 17 Feb 2020
 */

package asWebRest.action;

import asWebRest.dao.AddressBookDAO;
import java.sql.Connection;
import org.json.JSONArray;

public class GetAddressBookAction {
    
    private AddressBookDAO addressBookDAO;
    public GetAddressBookAction(AddressBookDAO addressBookDAO) { this.addressBookDAO = addressBookDAO; }
    
    public JSONArray getAddressBook(Connection dbc) { return addressBookDAO.getAddressBook(dbc); }
	public JSONArray getSubscribedAlerts(Connection dbc) { return addressBookDAO.getSubscribedAlerts(dbc); }    

}
