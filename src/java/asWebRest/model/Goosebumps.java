/*
by Anthony Stump
Created: 18 Feb 2018

Applies to tables:
    Core.GoosebumpsBooks
 */

package asWebRest.model;

public class Goosebumps {

    private String code;
    private String coverImageType;
    private String isbn;
    private int pages;
    private int pdf;
    private String plot;
    private String publishDate;
    private String title;
    
    public String getCode() { return code; }
    public String getCoverImageType() { return coverImageType; }
    public String getIsbn() { return isbn; }
    public int getPages() { return pages; }
    public int getPdf() { return pdf; }
    public String getPlot() { return plot; }
    public String getPublishDate() { return publishDate; }
    public String getTitle() { return title; }
    
    public void setCode(String code) { this.code = code; }
    public void setCoverImageType(String coverImageType) { this.coverImageType = coverImageType; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPages(int pages) { this.pages = pages; }
    public void setPdf(int pdf) { this.pdf = pdf; }
    public void setPlot(String plot) { this.plot = plot; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    public void setTitle(String title) { this.title = title; }
    
}
