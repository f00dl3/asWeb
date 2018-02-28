/*
by Anthony Stump
Created: 18 Feb 2018

Applies to multiple tables:
    Core.BGames
    Core.Books
    Core.DecorTools
    Core.Licenses

 */

package asWebRest.model;

public class Assets {

    private int active;
    private String author;
    private String category;
    private String checked;
    private double copyright;
    private double count;
    private String description;
    private double ean;
    private double edition;
    private int hardcover;
    private int id;
    private String location;
    private String media;
    private double minAge;
    private String notes;
    private double pages;
    private String platform;
    private String players;
    private String publisher;
    private double quantity;
    private String serial;
    private String title;
    private String type;
    private double upc;
    private double year;
    
    public int getActive() { return active; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getChecked() { return checked; }
    public double getCopyright() { return copyright; }
    public double getCount() { return count; }
    public String getDescription() { return description; }
    public double getEan() { return ean; }
    public double getEdition() { return edition; }
    public int getHardcover() { return hardcover; }
    public int getId() { return id; }
    public String getLocation() { return location; }
    public String getMedia() { return media; }
    public double getMinAge() { return minAge; }
    public String getNotes() { return notes; }
    public double getPages() { return pages; }
    public String getPlatform() { return platform; }
    public String getPlayers() { return players; }
    public String getPublisher() { return publisher; }
    public double getQuantity() { return quantity; }
    public String getSerial() { return serial; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public double getUpc() { return upc; }
    public double getYear() { return year; }
    
    public void setActive(int active) { this.active = active; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setChecked(String checked) { this.checked = checked; }
    public void setCopyright(double copyright) { this.copyright = copyright; }
    public void setCount(double count) { this.count = count; }
    public void setDescription(String description) { this.description = description; }
    public void setEan(double ean) { this.ean = ean; }
    public void setEdition(double edition) { this.edition = edition; }
    public void setHardcover(int hardcover) { this.hardcover = hardcover; }
    public void setId(int id) { this.id = id; }
    public void setLocation(String location) { this.location = location; }
    public void setMedia(String media) { this.media = media; }
    public void setMinAge(double minAge) { this.minAge = minAge; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setPages(double pages) { this.pages = pages; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setPlayers(String players) { this.players = players; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setSerial(String serial) { this.serial = serial; }
    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setUpc(double upc) { this.upc = upc; }
    public void setYear(double year) { this.year = year; }
    
}
