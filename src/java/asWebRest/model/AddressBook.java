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
    
    public String getAddress() { return address; }
    public String getAsOf() { return asOf; }
    public String getBusiness() { return business; }
    public String getBirthday() { return birthday; }
    public String getCategory() { return category; }
    public String getCity() { return city; }
    public String getEMail() { return eMail; }
    public String getFirstName() { return firstName; }
    public int getHoliday2014() { return holiday2014; }
    public String getLastName() { return lastName; }
    public String getP_Business() { return p_Business; }
    public String getP_Cell() { return p_Cell; }
    public String getP_Cell2() { return p_Cell2; }
    public String getP_Home() { return p_Home; }
    public String getPoint() { return point; }
    public String getState() { return state; }
    public String getWebsite() { return website; }
    public int getZip() { return zip; }
    
    public void setAddress(String address) { this.address = address; }
    public void setAsOf(String asOf) { this.asOf = asOf; }
    public void setBusiness(String business) { this.business = business; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setCategory(String category) { this.category = category; }
    public void setCity(String city) { this.city = city; }
    public void setEMail(String eMail) { this.eMail = eMail; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setHoliday2014(int holiday2014) { this.holiday2014 = holiday2014; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setP_Business(String p_Business) { this.p_Business = p_Business; }
    public void setP_Cell(String p_Cell) { this.p_Cell = p_Cell; }
    public void setP_Cell2(String p_Cell2) { this.p_Cell2 = p_Cell2; }
    public void setP_Home(String p_Home) { this.p_Home = p_Home; }
    public void setPoint(String point) { this.point = point; }
    public void setState(String state) { this.state = state; }
    public void setWebsite(String website) { this.website = website; }
    public void setZip(int zip) { this.zip = zip; }
    
}
