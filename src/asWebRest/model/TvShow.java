/*
by Anthony Stump
Created: 20 Feb 2018
Updated: 14 Jun 2018

Applies to:
    Core.PowerRangers
    Core.StarTrek
    Core.TrueBlood
    (and others)

 */

package asWebRest.model;

public class TvShow {
    
    private String airDate;
    private String airDateDts;
    private int mediaServer;
    private int overallEp;
    private String prodCode;
    private int season;
    private int seasonEp;
    private int series;
    private int seriesEp;
    private double starDate;
    private String synopsis;
    private String title;
    private double viewers;
    private double viewersM;
    
    public String getAirDate() { return airDate; }
    public String getAirDateDts() { return airDateDts; }
    public int getMediaServer() { return mediaServer; }
    public int getOverallEp() { return overallEp; }
    public String getProdCode() { return prodCode; }
    public int getSeason() { return season; }
    public int getSeasonEp() { return seasonEp; }
    public int getSeries() { return series; }
    public int getSeriesEp() { return seriesEp; }
    public double getStarDate() { return starDate; }
    public String getSynopsis() { return synopsis; }
    public String getTitle() { return title; }
    public double getViewers() { return viewers; }
    public double getViewersM() { return viewersM; }
    
    public void setAirDate(String airDate) { this.airDate = airDate; }
    public void setAirDateDts(String airDateDts) { this.airDateDts = airDateDts; }
    public void setMediaServer(int mediaServer) { this.mediaServer = mediaServer; }
    public void setOverallEp(int overallEp) { this.overallEp = overallEp; }
    public void setProdCode(String prodCode) { this.prodCode = prodCode; }
    public void setSeason(int season) { this.season = season; }
    public void setSeasonEp(int seasonEp) { this.seasonEp = seasonEp; }
    public void setSeries(int series) { this.series = series; }
    public void setSeriesEp(int seriesEp) { this.seriesEp = seriesEp; }
    public void setStarDate(double starDate) { this.starDate = starDate; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
    public void setTitle(String title) { this.title = title; }
    public void setViewers(double viewers) { this.viewers = viewers; }
    public void setViewersM(double viewersM) { this.viewersM = viewersM; }
    
}
