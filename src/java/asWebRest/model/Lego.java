/*
by Anthony Stump
Created: 18 Feb 2018

for tables:
    Core.Lego
*/

package asWebRest.model;

public class Lego {

    private double caPrice;
    private double euPrice;
    private String imageUrl;
    private int imgDown;
    private int minifigs;
    private String name;
    private String number;
    private int pieces;
    private int setId;
    private String subtheme;
    private String theme;
    private double ukPrice;
    private double usPrice;
    private String variant;
    private int year;
    
    public double getCaPrice() { return caPrice; }
    public double getEuPrice() { return euPrice; }
    public String getImageUrl() { return imageUrl; }
    public int getImgDown() { return imgDown; }
    public int getMinifigs() { return minifigs; }
    public String getName() { return name; }
    public String getNumber() { return number; }
    public int getPieces() { return pieces; }
    public int getSetId() { return setId; }
    public String getSubtheme() { return subtheme; }
    public String getTheme() { return theme; }
    public double getUkPrice() { return ukPrice; }
    public double getUsPrice() { return usPrice; }
    public String getVariant() { return variant; }
    public int getYear() { return year; }
    
    public void setCaPrice(double caPrice) { this.caPrice = caPrice; }
    public void setEuPrice(double euPrice) { this.euPrice = euPrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setImgDown(int imgDown) { this.imgDown = imgDown; }
    public void setMinifigs(int minifigs) { this.minifigs = minifigs; }
    public void setName(String name) { this.name = name; }
    public void setNumber(String number) { this.number = number; }
    public void setPieces(int pieces) { this.pieces = pieces; }
    public void setSetId(int setId) { this.setId = setId; }
    public void setSubtheme(String subtheme) { this.subtheme = subtheme; }
    public void setTheme(String theme) { this.theme = theme; }
    public void setUkPrice(double ukPrice) { this.ukPrice = ukPrice; }
    public void setUsPrice(double usPrice) { this.usPrice = usPrice; }
    public void setVariant(String variant) { this.variant = variant; }
    public void setYear(int year) { this.year = year; }
    
}
