/*
by Anthony Stump
Created: 16 Feb 2018
Updated: 21 Jan 2019
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddressBookDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getAddressBook(Connection dbc) {
        final String query_AddressBook = "SELECT Business, LastName, FirstName,"
            + " Category, Address, City, State, Zip, P_Business, P_Home, P_Cell, P_Cell2,"
            + " EMail, AsOf, Holiday2014, Birthday, Point, Website, QuickName, Active, OldAddresses"
            + " FROM Core.Addresses"
            + " ORDER BY Business, LastName, FirstName"
            + " DESC;";
        JSONArray addressBook = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AddressBook, null);
            while (resultSet.next()) {
                JSONObject tAddressBook = new JSONObject();
                tAddressBook
                    .put("Business", resultSet.getString("Business"))
                    .put("LastName", resultSet.getString("LastName"))
                    .put("FirstName", resultSet.getString("FirstName"))
                    .put("Category", resultSet.getString("Category"))
                    .put("Address", resultSet.getString("Address"))
                    .put("City", resultSet.getString("City"))
                    .put("State", resultSet.getString("State"))
                    .put("Zip", resultSet.getString("Zip")) // May be Double
                    .put("P_Business", resultSet.getString("P_Business"))
                    .put("P_Home", resultSet.getString("P_Home"))
                    .put("P_Cell", resultSet.getString("P_Cell"))
                    .put("P_Cell2", resultSet.getString("P_Cell2"))
                    .put("EMail", resultSet.getString("EMail"))
                    .put("AsOf", resultSet.getString("AsOf"))
                    .put("Holiday2014", resultSet.getInt("Holiday2014"))
                    .put("Active", resultSet.getInt("Active"))
                    .put("Birthday", resultSet.getString("Birthday"))
                    .put("Point", resultSet.getString("Point"))
                    .put("Website", resultSet.getString("Website"))
                    .put("OldAddresses", resultSet.getString("OldAddresses"))
                    .put("QuickName", resultSet.getString("QuickName"));
                addressBook.put(tAddressBook);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return addressBook;
    }
    
}