/*
by Anthony Stump
Created: 16 Feb 2018

Applies to tables:
    Core.Addresses

 */

package asWebRest.model;

public class AddressBook {
    
    private String address;
    private String asOf;
    private String birthday;
    private String business;
    private String category;
    private String city;
    private String eMail;
    private String firstName;
    private int holiday2014;
    private String lastName;
    private String p_Business;
    private String p_Cell;
    private String p_Cell2;
    private String p_Home;
    private String point;
    private String state;
    private String website;
    private int zip;
    
    private String getAddress() { return address; }
    private String getAsOf() { return asOf; }
    private String getBusiness() { return business; }
    private String getBirthday() { return birthday; }
    private String getCategory() { return category; }
    private String getCity() { return city; }
    private String getEMail() { return eMail; }
    private String getFirstName() { return firstName; }
    private int getHoliday2014() { return holiday2014; }
    private String getLastName() { return lastName; }
    private String getP_Business() { return p_Business; }
    private String getP_Cell() { return p_Cell; }
    private String getP_Cell2() { return p_Cell2; }
    private String getP_Home() { return p_Home; }
    private String getPoint() { return point; }
    private String getState() { return state; }
    private String getWebsite() { return website; }
    private int getZip() { return zip; }
    
    private void setAddress(String address) { this.address = address; }
    private void setAsOf(String asOf) { this.asOf = asOf; }
    private void setBusiness(String business) { this.business = business; }
    private void setBirthday(String birthday) { this.birthday = birthday; }
    private void setCategory(String category) { this.category = category; }
    private void setCity(String city) { this.city = city; }
    private void setEMail(String eMail) { this.eMail = eMail; }
    private void setFirstName(String firstName) { this.firstName = firstName; }
    private void setHoliday2014(int holiday2014) { this.holiday2014 = holiday2014; }
    private void setLastName(String lastName) { this.lastName = lastName; }
    private void setP_Business(String p_Business) { this.p_Business = p_Business; }
    private void setP_Cell(String p_Cell) { this.p_Cell = p_Cell; }
    private void setP_Cell2(String p_Cell2) { this.p_Cell2 = p_Cell2; }
    private void setP_Home(String p_Home) { this.p_Home = p_Home; }
    private void setPoint(String point) { this.point = point; }
    private void setState(String state) { this.state = state; }
    private void setWebsite(String website) { this.website = website; }
    private void setZip(int zip) { this.zip = zip; }
    
}
