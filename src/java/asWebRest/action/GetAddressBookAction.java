/*
by Anthony Stump
Created: 16 Feb 2018
 */

package asWebRest.action;

import asWebRest.dao.AddressBookDAO;
import org.json.JSONArray;

public class GetAddressBookAction {
    
    private AddressBookDAO addressBookDAO;
    public GetAddressBookAction(AddressBookDAO addressBookDAO) { this.addressBookDAO = addressBookDAO; }
    public JSONArray getAddressBook() { return addressBookDAO.getAddressBook(); }
    
}
